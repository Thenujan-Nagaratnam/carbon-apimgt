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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIAssistantApiPublisherNotifier extends ApisNotifier{
    protected ApiMgtDAO apiMgtDAO;
    private static final Log log = LogFactory.getLog(ExternalGatewayNotifier.class);

    @Override
    public boolean publishEvent(Event event) throws NotifierException {
        apiMgtDAO = ApiMgtDAO.getInstance();
        process(event);
        return true;
    }

    /**
     * Process API lifecycle notifier events related to APIs
     *
     * @param event related to deployments
     * @throws NotifierException if error occurs when casting event
     */
    private void process (Event event) throws NotifierException {
        APIEvent apiEvent;
        apiEvent = (APIEvent) event;

        if (APIConstants.EventType.API_LIFECYCLE_CHANGE.name().equals(event.getType())) {
            if (APIConstants.RETIRED.equals(apiEvent.getApiStatus())){
                deleteRequest(apiEvent);
            } else if (APIConstants.PUBLISHED.equals(apiEvent.getApiStatus())) {
                postRequest(apiEvent);
            }
        } else if (APIConstants.EventType.API_DELETE.name().equals(event.getType())) {
            deleteRequest(apiEvent);
        }
    }

    /**
     * Add or Delete APIs from external DB when life cycle state changes
     *
     * @param apiEvent APIEvent to undeploy APIs from external gateway
     * @throws NotifierException if error occurs
     */
    private void postRequest(APIEvent apiEvent) throws NotifierException {
        apiMgtDAO = ApiMgtDAO.getInstance();
        String apiId = apiEvent.getUuid();

        try {
            APIProvider apiProvider = APIManagerFactory.getInstance().getAPIProvider(CarbonContext.
                    getThreadLocalCarbonContext().getUsername());
            API api = apiProvider.getAPIbyUUID(apiId, apiMgtDAO.getOrganizationByAPIUUID(apiId));

            try {
                URL url = new URL("http://127.0.0.1:8000/add_vector/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("api_spec", api.getSwaggerDefinition());
                jsonInput.put("version", apiEvent.getApiVersion());
                jsonInput.put("status", apiEvent.getApiStatus());
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.writeBytes(String.valueOf(jsonInput));
                    outputStream.flush();
                }
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Response: " + response.toString());
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (APIManagementException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteRequest(APIEvent apiEvent) throws NotifierException {

        apiMgtDAO = ApiMgtDAO.getInstance();

            try {
                URL url = new URL("http://127.0.0.1:8000/remove_vector/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("api_title", apiEvent.getApiName());
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.writeBytes(String.valueOf(jsonInput));
                    outputStream.flush();
                }
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Response: " + response.toString());
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }


}
