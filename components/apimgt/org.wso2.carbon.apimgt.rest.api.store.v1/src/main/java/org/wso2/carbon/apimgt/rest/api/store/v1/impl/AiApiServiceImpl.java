package org.wso2.carbon.apimgt.rest.api.store.v1.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.rest.api.common.RestApiCommonUtil;
import org.wso2.carbon.apimgt.rest.api.store.v1.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.wso2.carbon.apimgt.rest.api.store.v1.dto.*;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.io.IOException;

import javax.ws.rs.core.Response;

public class AiApiServiceImpl implements AiApiService {
    private static final Log log = LogFactory.getLog(AiApiServiceImpl.class);

    public Response apiChatExecute(String apiChatRequestId, ApiChatExecuteRequestDTO apiChatExecuteRequestDTO,
            MessageContext messageContext) {
        if (StringUtils.isEmpty(apiChatRequestId)) {
            String errorMessage = "Error executing the API Chat service. API ID is a required parameter";
            RestApiUtil.handleBadRequest(errorMessage, log);
            return null;
        }

        // Determine whether the request body is valid. Request should either initialize test or provide
        // test execution progress.
        boolean isTestInitializationRequest = !StringUtils.isEmpty(apiChatExecuteRequestDTO.getCommand()) && apiChatExecuteRequestDTO.getApiSpec() != null;
        boolean isTestExecutionRequest = apiChatExecuteRequestDTO.getResponse() != null;
        if (!(isTestInitializationRequest || isTestExecutionRequest)) {
            String errorMessage = "Payload is badly formatted. Expected to have either 'command' and 'apiSpec' " +
                    "or 'response'";
            RestApiUtil.handleBadRequest(errorMessage, log);
            return null;
        }

        try {
            if (APIUtil.isApiChatEnabled()) {
                String payload = payload = new Gson().toJson(apiChatExecuteRequestDTO);
                CloseableHttpResponse response = APIUtil.invokeAIService(APIConstants.API_CHAT_ENDPOINT,
                        APIConstants.API_CHAT_AUTH_TOKEN, APIConstants.API_CHAT_EXECUTE_RESOURCE, payload,
                        apiChatRequestId);
                int statusCode = response.getStatusLine().getStatusCode();
                String responseStr = EntityUtils.toString(response.getEntity());
                if (statusCode == HttpStatus.SC_CREATED) {
                    if (log.isDebugEnabled()) {
                        log.debug("Successfully completed the API Chat execute call with status code: " + statusCode);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    ApiChatExecuteResponseDTO executeResponseDTO = objectMapper.readValue(responseStr,
                            ApiChatExecuteResponseDTO.class);
                    return Response.status(Response.Status.CREATED).entity(executeResponseDTO).build();
                } else {
                    String errorMessage = "Error encountered while executing the API Chat service to accomodate the " +
                            "specified testing requirement";
                    log.error(errorMessage);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseStr).build();
                }
            }
        } catch (APIManagementException | IOException e) {
            String errorMessage = "Error encountered while executing the execute statement of API Chat service";
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    public Response apiChatPrepare(String apiChatRequestId, String apiId, MessageContext messageContext) {
        if (StringUtils.isEmpty(apiChatRequestId) || StringUtils.isEmpty(apiId)) {
            String errorMessage = "Error while executing the prepare statement. Both API ID and request ID are " +
                    "required parameters";
            RestApiUtil.handleBadRequest(errorMessage, log);
            return null;
        }
        try {
            if (APIUtil.isApiChatEnabled()) {
                String organization = RestApiUtil.getValidatedOrganization(messageContext);
                APIConsumer apiConsumer = RestApiCommonUtil.getLoggedInUserConsumer();
                String swaggerDefinition = apiConsumer.getOpenAPIDefinition(apiId, organization);
                String payload = "{\"openapi\": " + swaggerDefinition + "}";

                CloseableHttpResponse response = APIUtil.invokeAIService(APIConstants.API_CHAT_ENDPOINT,
                        APIConstants.API_CHAT_AUTH_TOKEN, APIConstants.API_CHAT_PREPARE_RESOURCE, payload,
                        apiChatRequestId);
                int statusCode = response.getStatusLine().getStatusCode();
                String responseStr = EntityUtils.toString(response.getEntity());
                if (statusCode == HttpStatus.SC_CREATED) {
                    if (log.isDebugEnabled()) {
                        log.debug("Successfully executed the API Chat preparation with status code: " + statusCode);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    ApiChatPreparationResponseDTO preparationResponseDTO = objectMapper.readValue(responseStr,
                            ApiChatPreparationResponseDTO.class);
                    return Response.status(Response.Status.CREATED).entity(preparationResponseDTO).build();
                } else {
                    log.error("Error encountered while executing the prepare statement of API Chat service");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseStr).build();
                }
            }
        } catch (APIManagementException | IOException e) {
            String errorMessage = "Error encountered while executing the prepare statement of API Chat service";
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    public Response getApiChatHealth(MessageContext messageContext) {
        return null;
    }

    @Override
    public Response getMarketplaceChatApiCount(MessageContext messageContext) throws APIManagementException {
        try {
            if (APIUtil.isMarketplaceAssistantEnabled()) {
                CloseableHttpResponse response = APIUtil.getMarketplaceChatApiCount(APIConstants.MARKETPLACE_ASSISTANT_ENDPOINT, APIConstants.MARKETPLACE_ASSISTANT_API_COUNT_RESOURCE);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String responseStr = EntityUtils.toString(response.getEntity());
                    if (log.isDebugEnabled()) {
                        log.debug("Successfully completed the Marketplace Assistant api count call with status code: " + statusCode);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    MarketplaceChatApiCountResponseDTO executeResponseDTO = objectMapper.readValue(responseStr,
                            MarketplaceChatApiCountResponseDTO.class);
                    return Response.status(Response.Status.OK).entity(executeResponseDTO).build();
                } else {
                    String errorMessage = "Error encountered while executing the Marketplace Assistant service to accomodate the " +
                            "specified testing requirement";
                    log.error(errorMessage);
                    RestApiUtil.handleInternalServerError(errorMessage, log);
                }
            }
        } catch (APIManagementException | IOException e) {
            String errorMessage = "Error encountered while executing the execute statement of Marketplace Assistant service";
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    @Override
    public Response marketplaceChatExecute(MarketplaceChatRequestDTO marketplaceChatRequestDTO, MessageContext messageContext) throws APIManagementException {
        // Determine whether the request body is valid. Request should either initialize test or provide
        // test execution progress.
        boolean isChatRequest = !StringUtils.isEmpty(marketplaceChatRequestDTO.getMessage());
        boolean isOrgProvided = !StringUtils.isEmpty(marketplaceChatRequestDTO.getTenantDomain());
        if (!(isChatRequest || isOrgProvided)) {
            String errorMessage = "Payload is badly formatted. Expected to have either 'message', 'TenantDomain' and 'Organization' " +
                    "or 'response'";
            RestApiUtil.handleBadRequest(errorMessage, log);
            return null;
        }

        try {
            if (APIUtil.isMarketplaceAssistantEnabled()) {
                String payload = payload = new Gson().toJson(marketplaceChatRequestDTO);
                CloseableHttpResponse response = APIUtil.invokeAIService(APIConstants.MARKETPLACE_ASSISTANT_ENDPOINT,
                        APIConstants.MARKETPLACE_ASSISTANT_AUTH_TOKEN, APIConstants.MARKETPLACE_ASSISTANT_CHAT_RESOURCE, payload,
                        "");
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_CREATED) {
                    String responseStr = EntityUtils.toString(response.getEntity());
                    if (log.isDebugEnabled()) {
                        log.debug("Successfully completed the Marketplace Assistant execute call with status code: " + statusCode);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    MarketplaceChatResponseDTO executeResponseDTO = objectMapper.readValue(responseStr,
                            MarketplaceChatResponseDTO.class);
                    return Response.status(Response.Status.CREATED).entity(executeResponseDTO).build();
                } else {
                    String errorMessage = "Error encountered while executing the Marketplace Assistant service to accomodate the " +
                            "specified testing requirement";
                    log.error(errorMessage);
                    RestApiUtil.handleInternalServerError(errorMessage, log);
                }
            }
        } catch (APIManagementException | IOException e) {
            String errorMessage = "Error encountered while executing the execute statement of Marketplace Assistant service";
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }
}
