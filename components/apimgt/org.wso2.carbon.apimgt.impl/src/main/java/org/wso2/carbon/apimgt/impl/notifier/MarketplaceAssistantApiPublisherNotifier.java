/*
 *  Copyright (c) 2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.apimgt.impl.notifier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dto.ai.MarketplaceAssistantConfigurationDTO;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.notifier.events.APIEvent;
import org.wso2.carbon.apimgt.impl.notifier.events.Event;
import org.wso2.carbon.apimgt.impl.notifier.exceptions.NotifierException;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default API notification service implementation in which API creation, update, delete and LifeCycle change
 * events are published to gateway.
 */
public class MarketplaceAssistantApiPublisherNotifier extends ApisNotifier{
    protected ApiMgtDAO apiMgtDAO;
    private static final Log log = LogFactory.getLog(MarketplaceAssistantApiPublisherNotifier.class);
    private static MarketplaceAssistantConfigurationDTO marketplaceAssistantConfigurationDto =
            new MarketplaceAssistantConfigurationDTO();

    @Override
    public boolean publishEvent(Event event) throws NotifierException {
        APIManagerConfiguration configuration = ServiceReferenceHolder.
                getInstance().getAPIManagerConfigurationService().getAPIManagerConfiguration();

        if (configuration == null) {
            log.error("API Manager configuration is not initialized.");
        } else {
            marketplaceAssistantConfigurationDto = configuration.getMarketplaceAssistantConfigurationDto();
            process(event);
        }
        return true;
    }

    /**
     * Add or Delete APIs from external DB when life cycle state changes
     *
     * @param event APIEvent to undeploy APIs from external gateway
     * @throws NotifierException if error occurs
     */
    private void process (Event event) throws NotifierException {
        APIEvent apiEvent;
        apiEvent = (APIEvent) event;

        if (!APIConstants.API_GLOBAL_VISIBILITY.equals(apiEvent.getApiVisibility())) {
            return;
        }

        if (APIConstants.EventType.API_LIFECYCLE_CHANGE.name().equals(event.getType())) {
            String lifecycleEvent = apiEvent.getLifecycleEvent();
            String currentStatus = apiEvent.getCurrentStatus().toUpperCase();
            switch (lifecycleEvent) {
                case APIConstants.DEMOTE_TO_CREATED:
                case APIConstants.BLOCK:
                    deleteRequest(apiEvent);
                    break;
                case APIConstants.DEPRECATE:
                    if (APIConstants.PUBLISHED.equals(currentStatus)){
                        deleteRequest(apiEvent);
                        break;
                    }
                case APIConstants.PUBLISH:
                case APIConstants.DEPLOY_AS_A_PROTOTYPE:
                    if (APIConstants.CREATED.equals(currentStatus)) {
                        postRequest(apiEvent);
                    }
                    break;
                case APIConstants.REPUBLISH:
                    postRequest(apiEvent);
                    break;
                default:
                    break;
            }
        } else if (APIConstants.EventType.API_DELETE.name().equals(event.getType())) {
            String currentStatus = apiEvent.getApiStatus().toUpperCase();
            switch (currentStatus) {
                case APIConstants.PROTOTYPED:
                case APIConstants.PUBLISHED:
                    deleteRequest(apiEvent);
                    break;
                default:
                    break;
            }
        } else if (APIConstants.EventType.API_UPDATE.name().equals(event.getType())) {
            String currentStatus = apiEvent.getApiStatus().toUpperCase();
            switch (currentStatus) {
                case APIConstants.PROTOTYPED:
                case APIConstants.PUBLISHED:
                    postRequest(apiEvent);
                    break;
                default:
                    break;
            }
        }
    }

    private void postRequest(APIEvent apiEvent) throws NotifierException {
        String apiId = apiEvent.getUuid();

        try {
            apiMgtDAO = ApiMgtDAO.getInstance();
            APIProvider apiProvider = APIManagerFactory.getInstance().getAPIProvider(CarbonContext.
                    getThreadLocalCarbonContext().getUsername());
            API api = apiProvider.getAPIbyUUID(apiId, apiMgtDAO.getOrganizationByAPIUUID(apiId));

            try {
                String api_type = api.getType();
                JSONObject payload = new JSONObject();

                payload.put(APIConstants.API_SPEC_TYPE, api_type);

                APISpecReducer apiSpecReducer = new APISpecReducer();

                switch (api_type) {
                    case APIConstants.API_TYPE_GRAPHQL:
                        payload.put(APIConstants.API_SPEC_TYPE_GRAPHQL, api.getGraphQLSchema());
                        System.out.println(apiSpecReducer.reduceGraphQLSchema(api.getGraphQLSchema()));
                        break;
                    case APIConstants.API_TYPE_ASYNC:
                    case APIConstants.API_TYPE_WS:
                    case APIConstants.API_TYPE_WEBSUB:
                    case APIConstants.API_TYPE_SSE:
                    case APIConstants.API_TYPE_WEBHOOK:
                        payload.put(APIConstants.API_SPEC_TYPE_ASYNC, api.getAsyncApiDefinition());
                        System.out.println(apiSpecReducer.reduceAsyncAPISpec(api.getAsyncApiDefinition()));
                        break;
                    case APIConstants.API_TYPE_HTTP:
                    case APIConstants.API_TYPE_PRODUCT:
                    case APIConstants.API_TYPE_SOAP:
                    case APIConstants.API_TYPE_SOAPTOREST:
                        payload.put(APIConstants.API_SPEC_TYPE_REST, api.getSwaggerDefinition());
                        System.out.println(apiSpecReducer.reduceOpenAPISpec(api.getSwaggerDefinition()));
                        break;
                    default:
                        break;
                }

                payload.put(APIConstants.UUID, api.getUuid());
                payload.put(APIConstants.DESCRIPTION, api.getDescription());
                payload.put(APIConstants.API_SPEC_NAME, api.getId().getApiName());
                payload.put(APIConstants.TENANT_DOMAIN, apiEvent.getTenantDomain());
                payload.put(APIConstants.VERSION, apiEvent.getApiVersion());

                APIUtil.invokeAIService(marketplaceAssistantConfigurationDto.getEndpoint(),
                        marketplaceAssistantConfigurationDto.getAccessToken(),
                        marketplaceAssistantConfigurationDto.getApiPublishResource(), payload.toString(), null);
            } catch (APIManagementException e) {
                String errorMessage = "Error encountered while Uploading the API with UUID: " +
                        apiId + " to the vector database" + e.getMessage();
                log.error(errorMessage, e);
            }

        } catch (APIManagementException e) {
            String errorMessage = "Error encountered while Uploading the API with UUID: " +
                    apiId + " to the vector database" + e.getMessage();
            log.error(errorMessage, e);
        }
    }

    private void deleteRequest(APIEvent apiEvent) throws NotifierException {
        String uuid = apiEvent.getUuid();
        MarketplaceAssistantDeletionTask task = new MarketplaceAssistantDeletionTask(uuid);
        Thread thread = new Thread(task, "MarketplaceAssistantDeletionThread");
        thread.start();
    }

    class MarketplaceAssistantPostTask implements Runnable {
        private API api;
        private APIEvent apiEvent;
        private String apiId;
        public MarketplaceAssistantPostTask(API api, APIEvent apiEvent, String apiId) {
            this.api = api;
            this.apiEvent = apiEvent;
            this.apiId = apiId;
        }

        @Override
        public void run() {

        }
    }

    class MarketplaceAssistantDeletionTask implements Runnable {
        private String uuid;
        public MarketplaceAssistantDeletionTask(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public void run() {
            try {
                APIUtil.marketplaceAssistantDeleteService(marketplaceAssistantConfigurationDto.getEndpoint(),
                        marketplaceAssistantConfigurationDto.getAccessToken(),
                        marketplaceAssistantConfigurationDto.getApiDeleteResource(), uuid);
            } catch (APIManagementException e) {
                String errorMessage = "Error encountered while Deleting the API with UUID: " +
                        uuid + " from the vector database" + e.getMessage();
                log.error(errorMessage, e);
            }
        }
    }


    public class EndpointInfo {
        private String method;
        private String summary;
        private JsonObject docs;

        public EndpointInfo(String method, String summary, JsonObject docs) {
            this.method = method;
            this.summary = summary;
            this.docs = docs;
        }

        public String getMethod() {
            return method;
        }

        public String getSummary() {
            return summary;
        }

        public JsonObject getDocs() {
            return docs;
        }
    }

    public class ChannelInfo {
        private String channelName;
        private String description;

        public ChannelInfo(String channelName, String description) {
            this.channelName = channelName;
            this.description = description;
        }

        public String getChannelName() {
            return channelName;
        }

        public String getDescription() {
            return description;
        }
    }



    public class ReducedOpenAPISpec {
        private String title;
        private String description;
        private List<String> endpoints;

        public ReducedOpenAPISpec(String title, String description, List<String> endpoints) {
            this.title = title;
            this.description = description;
            this.endpoints = endpoints;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getEndpoints() {
            return endpoints;
        }
    }

    public class ReducedAsyncAPISpec {
        private String title;
        private String description;
        private List<ChannelInfo> channels;

        public ReducedAsyncAPISpec(String title, String description, List<ChannelInfo> channels) {
            this.title = title;
            this.description = description;
            this.channels = channels;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<ChannelInfo> getChannels() {
            return channels;
        }
    }

    public class APISpecReducer {

        public Map<String, Object> convertStringToMap(String input) {
            Map<String, Object> resultMap = new HashMap<>();

            // Remove leading and trailing whitespace
            input = input.trim();

            // Check if input starts with "{" and ends with "}"
            if (input.startsWith("{") && input.endsWith("}")) {
                // Remove enclosing braces
                input = input.substring(1, input.length() - 1).trim();

                // Split input into key-value pairs based on ","
                String[] pairs = input.split(",");
                for (String pair : pairs) {
                    // Split each pair into key and value based on ":"
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replaceAll("\"", ""); // Remove surrounding quotes from key
                        String valueString = keyValue[1].trim();

                        // Convert valueString to appropriate object type (String, Number, Boolean, Map, List)
                        Object value = this.parseValue(valueString);

                        // Add key-value pair to resultMap
                        resultMap.put(key, value);
                    }
                }
            }

            return resultMap;
        }

        private Object parseValue(String valueString) {
            if (valueString.startsWith("\"") && valueString.endsWith("\"")) {
                return valueString.substring(1, valueString.length() - 1); // Remove surrounding quotes
            }

            try {
                return Double.parseDouble(valueString); // Try to parse as double
            } catch (NumberFormatException e) {
                if (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(valueString);
                }
            }

            return null;
        }

        public String reduceOpenAPISpec(String specString) {

            Gson gson = new Gson();

            JsonObject spec = gson.fromJson(specString, JsonObject.class);

            JsonObject info = spec.get("info").getAsJsonObject();
            String title = info.get("title").getAsString();
            String description = info.get("description").getAsString();

            List<String> endpoints = new ArrayList<>();
            JsonObject paths = spec.get("paths").getAsJsonObject();

            List<String> validMethods = new ArrayList<>(Arrays.asList("get", "post", "patch", "delete", "put"));

            for (String path : paths.keySet()) {
                JsonObject pathJson = paths.get(path).getAsJsonObject();
                for (String method : pathJson.keySet()) {
                    if (validMethods.contains(method)) {
                        JsonObject operation = pathJson.get(method).getAsJsonObject();
                        String operation_description;
                        if (!operation.get("description").getAsString().equals("")) {
                            operation_description = operation.get("description").getAsString();
                        } else {
                            operation_description = operation.get("summary").getAsString();
                        }
                        String res = method.toUpperCase() + " " + path + " " + operation_description;
                        endpoints.add(res);
                    }
                }
            }
            ReducedOpenAPISpec reducedOpenAPISpec =  new ReducedOpenAPISpec(title, description, endpoints);

            String json = gson.toJson(reducedOpenAPISpec);

            return json;
        }

        public ReducedAsyncAPISpec reduceAsyncAPISpec(String specString) {
            String title = "";
            String description = "";

            Map<String, Object> spec = this.convertStringToMap(specString);

            if (spec.containsKey("info") && spec.get("info") instanceof Map) {
                Map<String, Object> info = (Map<String, Object>) spec.get("info");
                title = (String) info.getOrDefault("title", "");
                description = (String) info.getOrDefault("description", "");
            }

            List<ChannelInfo> channels = new ArrayList<>();
            if (spec.containsKey("channels") && spec.get("channels") instanceof Map) {
                Map<String, Object> channelsMap = (Map<String, Object>) spec.get("channels");
                for (Map.Entry<String, Object> entry : channelsMap.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        Map<String, Object> channel = (Map<String, Object>) entry.getValue();
                        String channelName = entry.getKey();
                        String channelDescription = (String) channel.getOrDefault("description", "");
                        channels.add(new ChannelInfo(channelName, channelDescription));
                    }
                }
            }
            return new ReducedAsyncAPISpec(title, description, channels);
        }

        public Map<String, String> reduceGraphQLSchema(String schemaText) {
            // Remove white spaces
            String cleanedSchema = schemaText.replaceAll("\\s+", " ");

            // Extract queries, mutations, and subscriptions
            Map<String, String> schemaMap = new HashMap<>();

            Pattern queriesPattern = Pattern.compile("type Query \\{([^}]*)");
            Matcher queriesMatcher = queriesPattern.matcher(cleanedSchema);
            if (queriesMatcher.find()) {
                schemaMap.put("Queries", queriesMatcher.group(1).trim());
            }

            Pattern mutationsPattern = Pattern.compile("type Mutation \\{([^}]*)");
            Matcher mutationsMatcher = mutationsPattern.matcher(cleanedSchema);
            if (mutationsMatcher.find()) {
                schemaMap.put("Mutations", mutationsMatcher.group(1).trim());
            }

            Pattern subscriptionsPattern = Pattern.compile("type Subscription \\{([^}]*)");
            Matcher subscriptionsMatcher = subscriptionsPattern.matcher(cleanedSchema);
            if (subscriptionsMatcher.find()) {
                schemaMap.put("Subscriptions", subscriptionsMatcher.group(1).trim());
            }

            return schemaMap;
        }

    }
}
