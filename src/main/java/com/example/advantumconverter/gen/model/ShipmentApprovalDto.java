package com.example.advantumconverter.gen.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;







/**
 * ShipmentApprovalDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentApprovalDto   {
  @JsonProperty("approverFullName")
  private String approverFullName = null;

  @JsonProperty("approverLogin")
  private String approverLogin = null;

  @JsonProperty("time")
  private OffsetDateTime time = null;

  public ShipmentApprovalDto approverFullName(String approverFullName) {
    this.approverFullName = approverFullName;
    return this;
  }

  /**
   * Get approverFullName
   * @return approverFullName
  **/
  


  public String getApproverFullName() {
    return approverFullName;
  }

  public void setApproverFullName(String approverFullName) {
    this.approverFullName = approverFullName;
  }

  public ShipmentApprovalDto approverLogin(String approverLogin) {
    this.approverLogin = approverLogin;
    return this;
  }

  /**
   * Get approverLogin
   * @return approverLogin
  **/
  


  public String getApproverLogin() {
    return approverLogin;
  }

  public void setApproverLogin(String approverLogin) {
    this.approverLogin = approverLogin;
  }

  public ShipmentApprovalDto time(OffsetDateTime time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
  **/
  

//  @Valid

  public OffsetDateTime getTime() {
    return time;
  }

  public void setTime(OffsetDateTime time) {
    this.time = time;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentApprovalDto shipmentApprovalDto = (ShipmentApprovalDto) o;
    return Objects.equals(this.approverFullName, shipmentApprovalDto.approverFullName) &&
        Objects.equals(this.approverLogin, shipmentApprovalDto.approverLogin) &&
        Objects.equals(this.time, shipmentApprovalDto.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approverFullName, approverLogin, time);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentApprovalDto {\n");
    
    sb.append("    approverFullName: ").append(toIndentedString(approverFullName)).append("\n");
    sb.append("    approverLogin: ").append(toIndentedString(approverLogin)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
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

