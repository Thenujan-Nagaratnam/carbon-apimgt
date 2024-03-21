package org.wso2.carbon.apimgt.rest.api.store.v1;

import org.wso2.carbon.apimgt.rest.api.store.v1.*;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.apimgt.api.APIManagementException;

import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatExecuteRequestDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatExecuteResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ApiChatPreparationResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ErrorDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatApiCountResponseDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatRequestDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatResponseDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface AiApiService {
      public Response apiChatExecute(String apiChatRequestId, ApiChatExecuteRequestDTO apiChatExecuteRequestDTO, MessageContext messageContext) throws APIManagementException;
      public Response apiChatPrepare(String apiChatRequestId, String apiId, MessageContext messageContext) throws APIManagementException;
      public Response getApiChatHealth(MessageContext messageContext) throws APIManagementException;
      public Response getMarketplaceChatApiCount(MessageContext messageContext) throws APIManagementException;
      public Response marketplaceChatExecute(MarketplaceChatRequestDTO marketplaceChatRequestDTO, MessageContext messageContext) throws APIManagementException;
}
