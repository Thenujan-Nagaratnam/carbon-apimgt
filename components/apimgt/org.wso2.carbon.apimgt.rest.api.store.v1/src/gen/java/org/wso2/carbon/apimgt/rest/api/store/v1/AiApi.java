package org.wso2.carbon.apimgt.rest.api.store.v1;

import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatExecuteRequestDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatExecuteResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatPreparationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ErrorDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatApiCountResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatRequestDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.AiApiService;
import org.wso2.carbon.apimgt.rest.api.store.v1.impl.AiApiServiceImpl;
import org.wso2.carbon.apimgt.api.APIManagementException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.inject.Inject;

import io.swagger.annotations.*;
import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
@Path("/ai")

@Api(description = "the ai API")




public class AiApi  {

  @Context MessageContext securityContext;

AiApiService delegate = new AiApiServiceImpl();


    @POST
    @Path("/api-chat/execute")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Execute a single API test case while caching the progress", notes = "Executes a test scenario against an API; which iteratively provide resources that need to be invoked alongside the inputs such as parameters, payloads, etc. while caching the progress. ", response = ApiChatExecuteResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "API Chat",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created. API Chat execute response payload. ", response = ApiChatExecuteResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An error occurred while executing test using API Chat service. ", response = Void.class) })
    public Response apiChatExecute( @ApiParam(value = "Request ID " )@HeaderParam("apiChatRequestId") String apiChatRequestId, @ApiParam(value = "API Chat execute request payload " ) ApiChatExecuteRequestDTO apiChatExecuteRequestDTO) throws APIManagementException{
        return delegate.apiChatExecute(apiChatRequestId, apiChatExecuteRequestDTO, securityContext);
    }

    @POST
    @Path("/api-chat/prepare")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Prepare API Chat service by feeding the OpenAPI specification of the API to be tested", notes = "Processing the OpenAPI specification to extract the API endpoint definitions and generate sample queries ", response = ApiChatPreparationResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "API Chat",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created. API Chat preparation completed. Successful response with enriched API specification and sample queries. ", response = ApiChatPreparationResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An error occurred while preparing the API Chat service. ", response = Void.class) })
    public Response apiChatPrepare( @ApiParam(value = "Request ID " )@HeaderParam("apiChatRequestId") String apiChatRequestId,  @ApiParam(value = "**API ID** consisting of the **UUID** of the API. ")  @QueryParam("apiId") String apiId) throws APIManagementException{
        return delegate.apiChatPrepare(apiChatRequestId, apiId, securityContext);
    }

    @GET
    @Path("/api-chat/health")
    
    
    @ApiOperation(value = "Heath check endpoint", notes = "Get the health status of API Chat AI service ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "API Chat",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Health status is returned. ", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An error occurred while checking the health status of API Chat service ", response = Void.class) })
    public Response getApiChatHealth() throws APIManagementException{
        return delegate.getApiChatHealth(securityContext);
    }

    @GET
    @Path("/marketplace-chat/api-count")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "API Count endpoint", notes = "Get the api count of Marketplace Chat AI service ", response = MarketplaceChatApiCountResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Marketplace Chat",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Api Count is returned. ", response = MarketplaceChatApiCountResponseDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An error occurred while checking the health status of API Chat service ", response = Void.class) })
    public Response getMarketplaceChatApiCount() throws APIManagementException{
        return delegate.getMarketplaceChatApiCount(securityContext);
    }

    @POST
    @Path("/marketplace-chat/chat")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Chat Endpoint", notes = "Send a single query to the service and get the response. ", response = MarketplaceChatResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Marketplace Chat" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created. Marketplace Chat execute response payload. ", response = MarketplaceChatResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error.", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized. The user is not authorized.", response = ErrorDTO.class),
        @ApiResponse(code = 429, message = "Too many requests.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error. An error occurred while executing test using Marketplace Chat service. ", response = Void.class) })
    public Response marketplaceChatExecute(@ApiParam(value = "Marketplace Chat execute request payload " ) MarketplaceChatRequestDTO marketplaceChatRequestDTO) throws APIManagementException{
        return delegate.marketplaceChatExecute(marketplaceChatRequestDTO, securityContext);
    }
}
