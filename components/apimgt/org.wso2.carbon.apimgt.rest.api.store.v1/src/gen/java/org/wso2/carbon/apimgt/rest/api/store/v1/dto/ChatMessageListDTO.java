package org.wso2.carbon.apimgt.rest.api.store.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ChatMessageDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class ChatMessageListDTO   {
  
    private List<ChatMessageDTO> messageList = new ArrayList<ChatMessageDTO>();

  /**
   **/
  public ChatMessageListDTO messageList(List<ChatMessageDTO> messageList) {
    this.messageList = messageList;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
      @Valid
  @JsonProperty("messageList")
  @NotNull
  public List<ChatMessageDTO> getMessageList() {
    return messageList;
  }
  public void setMessageList(List<ChatMessageDTO> messageList) {
    this.messageList = messageList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatMessageListDTO chatMessageList = (ChatMessageListDTO) o;
    return Objects.equals(messageList, chatMessageList.messageList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatMessageListDTO {\n");
    
    sb.append("    messageList: ").append(toIndentedString(messageList)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

