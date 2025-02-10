package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;







/**
 * PublicCardBindingRequestDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class PublicCardBindingRequestDto   {
  @JsonProperty("bind")
  private Boolean bind = null;

  @JsonProperty("cardNumber")
  private String cardNumber = null;

  @JsonProperty("externalGosNum")
  private String externalGosNum = null;

  @JsonProperty("externalTrId")
  private String externalTrId = null;

  @JsonProperty("trId")
  private Integer trId = null;

  public PublicCardBindingRequestDto bind(Boolean bind) {
    this.bind = bind;
    return this;
  }

  /**
   * Get bind
   * @return bind
  **/
  


  public Boolean isBind() {
    return bind;
  }

  public void setBind(Boolean bind) {
    this.bind = bind;
  }

  public PublicCardBindingRequestDto cardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
    return this;
  }

  /**
   * Get cardNumber
   * @return cardNumber
  **/
  


  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public PublicCardBindingRequestDto externalGosNum(String externalGosNum) {
    this.externalGosNum = externalGosNum;
    return this;
  }

  /**
   * Get externalGosNum
   * @return externalGosNum
  **/
  


  public String getExternalGosNum() {
    return externalGosNum;
  }

  public void setExternalGosNum(String externalGosNum) {
    this.externalGosNum = externalGosNum;
  }

  public PublicCardBindingRequestDto externalTrId(String externalTrId) {
    this.externalTrId = externalTrId;
    return this;
  }

  /**
   * Get externalTrId
   * @return externalTrId
  **/
  


  public String getExternalTrId() {
    return externalTrId;
  }

  public void setExternalTrId(String externalTrId) {
    this.externalTrId = externalTrId;
  }

  public PublicCardBindingRequestDto trId(Integer trId) {
    this.trId = trId;
    return this;
  }

  /**
   * Get trId
   * @return trId
  **/
  


  public Integer getTrId() {
    return trId;
  }

  public void setTrId(Integer trId) {
    this.trId = trId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PublicCardBindingRequestDto publicCardBindingRequestDto = (PublicCardBindingRequestDto) o;
    return Objects.equals(this.bind, publicCardBindingRequestDto.bind) &&
        Objects.equals(this.cardNumber, publicCardBindingRequestDto.cardNumber) &&
        Objects.equals(this.externalGosNum, publicCardBindingRequestDto.externalGosNum) &&
        Objects.equals(this.externalTrId, publicCardBindingRequestDto.externalTrId) &&
        Objects.equals(this.trId, publicCardBindingRequestDto.trId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bind, cardNumber, externalGosNum, externalTrId, trId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PublicCardBindingRequestDto {\n");
    
    sb.append("    bind: ").append(toIndentedString(bind)).append("\n");
    sb.append("    cardNumber: ").append(toIndentedString(cardNumber)).append("\n");
    sb.append("    externalGosNum: ").append(toIndentedString(externalGosNum)).append("\n");
    sb.append("    externalTrId: ").append(toIndentedString(externalTrId)).append("\n");
    sb.append("    trId: ").append(toIndentedString(trId)).append("\n");
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

