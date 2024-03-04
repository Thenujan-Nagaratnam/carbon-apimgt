/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.apimgt.impl.workflow;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;

/**
 * Simple workflow for API state change. This is the default worklow
 * 
 */
public class APIStateChangeSimpleWorkflowExecutor extends WorkflowExecutor {
    private static final Log log = LogFactory.getLog(APIStateChangeSimpleWorkflowExecutor.class);
    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_API_STATE;
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        return Collections.emptyList();
    }

    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        APIStateWorkflowDTO apiStateWorkflowDTO = (APIStateWorkflowDTO) workflowDTO;

        String apiLCAction = apiStateWorkflowDTO.getApiLCAction();

        if (apiLCAction.equals("Publish") || apiLCAction.equals("Deploy as a Prototype") || apiLCAction.equals("Re-Publish")){   // Publish , Deploy as a Prototype , Re-Publish
            publishAPIData(apiStateWorkflowDTO);
        } else if (apiLCAction.equals("Retire") || apiLCAction.equals("Demote to Created") || apiLCAction.equals("Block") || apiLCAction.equals("Deprecate")) {  // Demote to Created , Block , Deprecate , Retire
            deleteAPIData(apiStateWorkflowDTO);
        }

        workflowDTO.setStatus(WorkflowStatus.APPROVED);
        WorkflowResponse workflowResponse = complete(workflowDTO);     
        return workflowResponse;
    }

    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {
        return new GeneralWorkflowResponse();
    }
    private void deleteAPIData(APIStateWorkflowDTO apiStateWorkflowDTO){
        try {
            URL url = new URL("http://localhost:5000/api");

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable output and input streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/json");

            // Create JSON data to send
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("deleted", true);
            jsonInput.put("action", apiStateWorkflowDTO.getApiLCAction());

            // Write JSON data to the connection output stream
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(String.valueOf(jsonInput));
                outputStream.flush();
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response from the server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("Response: " + response.toString());
            }

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void publishAPIData(APIStateWorkflowDTO apiStateWorkflowDTO){
        try {
            URL url = new URL("http://localhost:5000/api");

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable output and input streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/json");

            // Create JSON data to send
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("swaggerDefinition", apiStateWorkflowDTO.getSwaggerDefinition());
            jsonInput.put("action", apiStateWorkflowDTO.getApiLCAction());

            // Write JSON data to the connection output stream
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(String.valueOf(jsonInput));
                outputStream.flush();
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response from the server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("Response: " + response.toString());
            }

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
