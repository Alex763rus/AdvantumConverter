package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;






/**
 * ShipmentRequestStatusChangeRequestDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentRequestStatusChangeRequestDto   {
  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    NEW("NEW"),
    
    REVIEW("REVIEW"),
    
    PROCESSING("PROCESSING"),
    
    ROUTES("ROUTES"),
    
    ACCEPTED("ACCEPTED"),
    
    EXECUTING("EXECUTING"),
    
    EXECUTED("EXECUTED"),
    
    ERROR("ERROR"),
    
    REJ_CUST("REJ_CUST"),
    
    REJECTED("REJECTED"),
    
    FREE("FREE");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("statusChangeReason")
  private String statusChangeReason = null;

  public ShipmentRequestStatusChangeRequestDto status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ShipmentRequestStatusChangeRequestDto statusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
    return this;
  }

  /**
   * Get statusChangeReason
   * @return statusChangeReason
  **/
  


  public String getStatusChangeReason() {
    return statusChangeReason;
  }

  public void setStatusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentRequestStatusChangeRequestDto shipmentRequestStatusChangeRequestDto = (ShipmentRequestStatusChangeRequestDto) o;
    return Objects.equals(this.status, shipmentRequestStatusChangeRequestDto.status) &&
        Objects.equals(this.statusChangeReason, shipmentRequestStatusChangeRequestDto.statusChangeReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, statusChangeReason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentRequestStatusChangeRequestDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusChangeReason: ").append(toIndentedString(statusChangeReason)).append("\n");
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

