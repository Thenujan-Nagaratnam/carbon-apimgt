package org.wso2.carbon.apimgt.rest.api.store.v1.impl;

import org.wso2.carbon.apimgt.rest.api.store.v1.*;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.MessageContext;

import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ChatMessageListDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ChatMessageDTO;


import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public class AiSearchAssistantApiServiceImpl implements AiSearchAssistantApiService {

    private ChatMessageListDTO chatMessageListDTO = new ChatMessageListDTO();

    public Response getAisearchassistant(String query, String action, MessageContext messageContext) {

        if (action.equals("chat")){

            ChatMessageDTO assistantMessage = new ChatMessageDTO().role("assistant");
            ChatMessageDTO userMessage = new ChatMessageDTO().role("user");

            assistantMessage.setContent("Hi there! I'm Chatbot UI, an AI assistant. I can help you with things like answering questions, providing information, and helping with tasks. How can I help you?");
            userMessage.setContent(query);

            List<ChatMessageDTO> messages = chatMessageListDTO.getMessages();

            messages.add(userMessage);
            messages.add(assistantMessage);

            chatMessageListDTO.setMessages(messages);

            return Response.ok().entity(chatMessageListDTO).build();

        } else if (action.equals("clearChat")){
            ChatMessageDTO assistantMessage = new ChatMessageDTO().role("assistant");
            assistantMessage.setContent("Hi there! I'm Chatbot UI, an AI assistant. I can help you with things like answering questions, providing information, and helping with tasks. How can I help you?");

            List<ChatMessageDTO> messages = chatMessageListDTO.getMessages();

            messages.clear();

            messages.add(assistantMessage);

            chatMessageListDTO.setMessages(messages);
            return Response.ok().entity(chatMessageListDTO).build();

        } else if (action.equals("getChatHistory")){

            List<ChatMessageDTO> messages = chatMessageListDTO.getMessages();
            ChatMessageDTO assistantMessage = new ChatMessageDTO().role("assistant");
            assistantMessage.setContent("Hi there! I'm Chatbot UI, an AI assistant. I can help you with things like answering questions, providing information, and helping with tasks. How can I help you?");

            if (messages.isEmpty()) {
                messages.add(assistantMessage);
            }

            chatMessageListDTO.setMessages(messages);

            return Response.ok().entity(chatMessageListDTO).build();
        } else {
            ErrorDTO errorObject = new ErrorDTO();
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            errorObject.setCode((long) status.getStatusCode());
            errorObject.setMessage(status.toString());
            errorObject.setDescription("INVALID ACTION");
            return Response.status(status).entity(errorObject).build();
        }
//        // remove errorObject and add implementation code!

    }
}
