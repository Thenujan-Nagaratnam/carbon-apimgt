/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.notifier.events.APIEvent;
import org.wso2.carbon.apimgt.impl.notifier.events.Event;
import org.wso2.carbon.apimgt.impl.notifier.exceptions.NotifierException;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * The default API notification service implementation in which API creation, update, delete and LifeCycle change
 * events are published to gateway.
 */
public class AIAssistantApiPublisherNotifier extends ApisNotifier{
    protected ApiMgtDAO apiMgtDAO;
    private static final Log log = LogFactory.getLog(ExternalGatewayNotifier.class);

    @Override
    public boolean publishEvent(Event event) throws NotifierException {
        if (APIUtil.isMarketplaceAssistantEnabled()) {
            process(event);
        }
        return true;
    }

    /**
     * Add or Delete APIs from external DB when life cycle state changes
     *
     * @param event APIEvent to undeploy APIs from external gateway
     * @return
     * @throws NotifierException if error occurs
     */
    private void process (Event event) throws NotifierException {
        APIEvent apiEvent;
        apiEvent = (APIEvent) event;

//        if (!APIConstants.API_GLOBAL_VISIBILITY.equals(apiEvent.getApiVisibility())) {
//            return;
//        }

        // [ **Publish**, **Deploy as a Prototype**, **Demote to Created**, **Block**, **Deprecate**, **Re-Publish**, **Retire** ]
        if (APIConstants.EventType.API_LIFECYCLE_CHANGE.name().equals(event.getType())) {
            String lifecycleEvent = apiEvent.getLifecycleEvent();
            String currentStatus = apiEvent.getCurrentStatus().toUpperCase();
            switch (lifecycleEvent) {
                case APIConstants.DEMOTE_TO_CREATED:
                case APIConstants.BLOCK:
                case APIConstants.DEPRECATE:
                    deleteRequest(apiEvent);
                    break;
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
        }
    }

    private void postRequest(APIEvent apiEvent) throws NotifierException {
        apiMgtDAO = ApiMgtDAO.getInstance();
        String apiId = apiEvent.getUuid();

        try {
            APIProvider apiProvider = APIManagerFactory.getInstance().getAPIProvider(CarbonContext.
                    getThreadLocalCarbonContext().getUsername());
            API api = apiProvider.getAPIbyUUID(apiId, apiMgtDAO.getOrganizationByAPIUUID(apiId));

            String api_type = api.getType();

            JSONObject payload = new JSONObject();

            switch (api_type) {
                case APIConstants.API_TYPE_GRAPHQL:
                    payload.put(APIConstants.API_SPEC_TYPE_GRAPHQL, api.getGraphQLSchema());
                    payload.put(APIConstants.API_SPEC_TYPE, APIConstants.API_TYPE_GRAPHQL);
                    break;
                case APIConstants.API_TYPE_ASYNC:
                case APIConstants.API_TYPE_WS:
                case APIConstants.API_TYPE_WEBSUB:
                case APIConstants.API_TYPE_SSE:
                case APIConstants.API_TYPE_WEBHOOK:
                    payload.put(APIConstants.API_SPEC_TYPE_ASYNC, api.getAsyncApiDefinition());
                    payload.put(APIConstants.API_SPEC_TYPE, APIConstants.API_TYPE_ASYNC);
                    break;
                case APIConstants.API_TYPE_HTTP:
                case APIConstants.API_TYPE_SOAP:
                case APIConstants.API_TYPE_SOAPTOREST:
                    payload.put(APIConstants.API_SPEC_TYPE_REST, api.getSwaggerDefinition());
                    payload.put(APIConstants.API_SPEC_TYPE, APIConstants.API_TYPE_REST);
                    break;
                default:
                    break;
            }

            payload.put(APIConstants.UUID, api.getUuid());
            payload.put(APIConstants.DESCRIPTION, api.getDescription());
            payload.put(APIConstants.API_SPEC_NAME, api.getId().getApiName());
            payload.put(APIConstants.TENANT_DOMAIN, apiEvent.getTenantDomain());
            payload.put(APIConstants.VERSION, apiEvent.getApiVersion());
            CloseableHttpResponse response = APIUtil.invokeAIService(APIConstants.MARKETPLACE_ASSISTANT_ENDPOINT,
                    APIConstants.MARKETPLACE_ASSISTANT_AUTH_TOKEN, APIConstants.MARKETPLACE_ASSISTANT_PUBLISH_API_RESOURCE, payload.toString(),
                    "");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully completed the Marketplace Chat API Publisher publish call with status code: " + statusCode);
                }
            } else {
                String errorMessage = "Error encountered while Uploading the API to the vector database service to accommodate the " +
                        "Marketplace assistant";
                log.error(errorMessage);
            }
        } catch (APIManagementException e) {
            String errorMessage = "Error encountered while Uploading the API to the vector database";
            log.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

    private void deleteRequest(APIEvent apiEvent) throws NotifierException {

        try {
            JSONObject payload = new JSONObject();
            payload.put(APIConstants.UUID, apiEvent.getUuid());
            payload.put(APIConstants.TENANT_DOMAIN, apiEvent.getTenantDomain());
            CloseableHttpResponse response = APIUtil.invokeAIService(APIConstants.MARKETPLACE_ASSISTANT_ENDPOINT,
                    APIConstants.MARKETPLACE_ASSISTANT_AUTH_TOKEN, APIConstants.MARKETPLACE_ASSISTANT_DELETE_API_RESOURCE, payload.toString(),
                    "");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully completed the Marketplace Chat API publisher delete call with status code: " + statusCode);
                }
            } else {
                String errorMessage = "Error encountered while Deleting the API from the vector database service to accommodate the " +
                        "Marketplace assistant";
                log.error(errorMessage);
            }
        } catch (APIManagementException e) {
            String errorMessage = "Error encountered while Deleting the API from the vector database";
            log.error(errorMessage);
            throw new RuntimeException(e);
        }
    }
}
