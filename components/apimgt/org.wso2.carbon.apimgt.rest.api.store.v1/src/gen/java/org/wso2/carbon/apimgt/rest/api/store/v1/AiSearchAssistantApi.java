package org.wso2.carbon.apimgt.rest.api.store.v1;

import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ChatMessageListDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ErrorDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.AiSearchAssistantApiService;
import org.wso2.carbon.apimgt.rest.api.store.v1.impl.AiSearchAssistantApiServiceImpl;
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
@Path("/aiSearchAssistant")

@Api(description = "the aiSearchAssistant API")




public class AiSearchAssistantApi  {

  @Context MessageContext securityContext;

AiSearchAssistantApiService delegate = new AiSearchAssistantApiServiceImpl();


    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Service for the API developer to find the most suitable API for them. Users can search for APIs using a natural language query. Ask the API to find the most suitable API for you. Also can ask about the API details.", notes = "", response = ChatMessageListDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "APIs" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = ChatMessageListDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error.", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response getAisearchassistant( @NotNull @ApiParam(value = "",required=true)  @QueryParam("query") String query,  @NotNull @ApiParam(value = "",required=true)  @QueryParam("action") String action) throws APIManagementException{
        return delegate.getAisearchassistant(query, action, securityContext);
    }
}
