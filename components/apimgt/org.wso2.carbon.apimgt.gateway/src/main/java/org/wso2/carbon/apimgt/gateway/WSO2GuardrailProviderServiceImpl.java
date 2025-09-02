/*
 * Copyright (c) 2025 WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.GuardrailProviderService;
import org.wso2.carbon.apimgt.api.dto.GuardrailProviderConfigurationDTO;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WSO2GuardrailProviderServiceImpl implements GuardrailProviderService {

    private String endpoint;

    private long retrievalTimeout;
    private int maxRetryCount;
    private double retryProgressionFactor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(GuardrailProviderConfigurationDTO providerConfig) throws APIManagementException {
        if (providerConfig == null || providerConfig.getProperties() == null) {
            throw new APIManagementException("WSO2 provider configuration not found");
        }

        endpoint = providerConfig.getProperties()
                .get(APIConstants.AI.GUARDRAIL_PROVIDER_AZURE_CONTENTSAFETY_ENDPOINT);

        if (endpoint == null) {
            throw new APIManagementException(
                    "Missing required WSO2 guardrail configuration: 'endpoint'");
        }

        // Retry parameters
        try {
            retrievalTimeout = Long.parseLong(providerConfig.getProperties()
                    .getOrDefault(APIConstants.AI.RETRIEVAL_TIMEOUT, APIConstants.AI.DEFAULT_RETRIEVAL_TIMEOUT));
            maxRetryCount = Integer.parseInt(providerConfig.getProperties()
                    .getOrDefault(APIConstants.AI.RETRY_COUNT, APIConstants.AI.DEFAULT_RETRY_COUNT));
            retryProgressionFactor = Double.parseDouble(providerConfig.getProperties()
                    .getOrDefault(APIConstants.AI.RETRY_PROGRESSION_FACTOR,
                            APIConstants.AI.DEFAULT_RETRY_PROGRESSION_FACTOR));
        } catch (NumberFormatException e) {
            throw new APIManagementException("Invalid retry configuration provided: " +
                    "'retrieval_timeout', 'retry_count', 'retry_progression_factor'");
        }
    }

    @Override
    public String getType() { return APIConstants.AI.GUARDRAIL_PROVIDER_WSO2_TYPE; }

    @Override
    public String callOut(Map<String, Object> callOutConfig) throws APIManagementException {
        Object payloadObj = callOutConfig.get(APIConstants.AI.GUARDRAIL_PROVIDER_AZURE_CONTENTSAFETY_CALLOUT_PAYLOAD);
        String resource = callOutConfig.getOrDefault(
                APIConstants.AI.GUARDRAIL_PROVIDER_WSO2_CALLOUT_RESOURCE, "").toString().trim();

        if (StringUtils.isBlank(resource) || !(payloadObj instanceof Map)) {
            throw new APIManagementException("Missing or invalid callout configuration: 'resource', 'payload'");
        }
        String url = endpoint + resource;
        HttpClient httpClient = APIUtil.getHttpClient(url);
        HttpPost post = new HttpPost(url);
        post.setHeader(APIConstants.HEADER_CONTENT_TYPE, APIConstants.APPLICATION_JSON_MEDIA_TYPE);

        try {
            String body = objectMapper.writeValueAsString(payloadObj);
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = APIUtil.executeHTTPRequestWithRetries(
                    post, httpClient, retrievalTimeout, maxRetryCount, retryProgressionFactor)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (statusCode == HttpStatus.SC_OK) {
                    JsonNode root = objectMapper.readTree(responseBody);
                    return root.toString();
                } else {
                    throw new APIManagementException("Unexpected status code " + statusCode + ": " + responseBody);
                }
            }
        } catch (IOException e) {
            throw new APIManagementException("Error occurred while calling out to wso2 guardrail resource: " + resource, e);
        }
    }
}
