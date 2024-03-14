package org.wso2.carbon.apimgt.rest.api.store.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.ChatMessageListDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class MarketplaceChatRequestDTO   {
  
    private String organization = null;
    private String tenantDomain = null;
    private String message = null;
    private ChatMessageListDTO messageList = null;

  /**
   **/
  public MarketplaceChatRequestDTO organization(String organization) {
    this.organization = organization;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("organization")
  @NotNull
  public String getOrganization() {
    return organization;
  }
  public void setOrganization(String organization) {
    this.organization = organization;
  }

  /**
   **/
  public MarketplaceChatRequestDTO tenantDomain(String tenantDomain) {
    this.tenantDomain = tenantDomain;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("tenantDomain")
  @NotNull
  public String getTenantDomain() {
    return tenantDomain;
  }
  public void setTenantDomain(String tenantDomain) {
    this.tenantDomain = tenantDomain;
  }

  /**
   **/
  public MarketplaceChatRequestDTO message(String message) {
    this.message = message;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("message")
  @NotNull
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   **/
  public MarketplaceChatRequestDTO messageList(ChatMessageListDTO messageList) {
    this.messageList = messageList;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
      @Valid
  @JsonProperty("messageList")
  @NotNull
  public ChatMessageListDTO getMessageList() {
    return messageList;
  }
  public void setMessageList(ChatMessageListDTO messageList) {
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
    MarketplaceChatRequestDTO marketplaceChatRequest = (MarketplaceChatRequestDTO) o;
    return Objects.equals(organization, marketplaceChatRequest.organization) &&
        Objects.equals(tenantDomain, marketplaceChatRequest.tenantDomain) &&
        Objects.equals(message, marketplaceChatRequest.message) &&
        Objects.equals(messageList, marketplaceChatRequest.messageList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organization, tenantDomain, message, messageList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketplaceChatRequestDTO {\n");
    
    sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
    sb.append("    tenantDomain: ").append(toIndentedString(tenantDomain)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

