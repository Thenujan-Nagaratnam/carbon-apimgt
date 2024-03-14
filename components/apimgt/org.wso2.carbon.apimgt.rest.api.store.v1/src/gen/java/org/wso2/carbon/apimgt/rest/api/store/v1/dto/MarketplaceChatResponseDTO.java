package org.wso2.carbon.apimgt.rest.api.store.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.MarketplaceChatApiListDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class MarketplaceChatResponseDTO   {
  
    private String response = null;
    private List<MarketplaceChatApiListDTO> apis = new ArrayList<MarketplaceChatApiListDTO>();

  /**
   **/
  public MarketplaceChatResponseDTO response(String response) {
    this.response = response;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("response")
  @NotNull
  public String getResponse() {
    return response;
  }
  public void setResponse(String response) {
    this.response = response;
  }

  /**
   **/
  public MarketplaceChatResponseDTO apis(List<MarketplaceChatApiListDTO> apis) {
    this.apis = apis;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
      @Valid
  @JsonProperty("apis")
  @NotNull
  public List<MarketplaceChatApiListDTO> getApis() {
    return apis;
  }
  public void setApis(List<MarketplaceChatApiListDTO> apis) {
    this.apis = apis;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketplaceChatResponseDTO marketplaceChatResponse = (MarketplaceChatResponseDTO) o;
    return Objects.equals(response, marketplaceChatResponse.response) &&
        Objects.equals(apis, marketplaceChatResponse.apis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(response, apis);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketplaceChatResponseDTO {\n");
    
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    apis: ").append(toIndentedString(apis)).append("\n");
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

