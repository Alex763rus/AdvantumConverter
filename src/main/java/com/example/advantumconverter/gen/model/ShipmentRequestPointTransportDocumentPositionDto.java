package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import java.time.OffsetDateTime;




/**
 * ShipmentRequestPointTransportDocumentPositionDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentRequestPointTransportDocumentPositionDto   {
  @JsonProperty("acceptedQuantity")
  private Integer acceptedQuantity = null;

  @JsonProperty("changeDateTime")
  private OffsetDateTime changeDateTime = null;

  @JsonProperty("changedByLogin")
  private String changedByLogin = null;

  @JsonProperty("changedByUserId")
  private Integer changedByUserId = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("createDateTime")
  private OffsetDateTime createDateTime = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("measureUnits")
  private String measureUnits = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("sequenceNumber")
  private Integer sequenceNumber = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    NOT_ACCEPTED("NOT_ACCEPTED"),
    
    ACCEPTED("ACCEPTED"),
    
    PARTIALLY_ACCEPTED("PARTIALLY_ACCEPTED");

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

  @JsonProperty("totalQuantity")
  private Integer totalQuantity = null;

  public ShipmentRequestPointTransportDocumentPositionDto acceptedQuantity(Integer acceptedQuantity) {
    this.acceptedQuantity = acceptedQuantity;
    return this;
  }

  /**
   * Get acceptedQuantity
   * @return acceptedQuantity
  **/
  


  public Integer getAcceptedQuantity() {
    return acceptedQuantity;
  }

  public void setAcceptedQuantity(Integer acceptedQuantity) {
    this.acceptedQuantity = acceptedQuantity;
  }

  public ShipmentRequestPointTransportDocumentPositionDto changeDateTime(OffsetDateTime changeDateTime) {
    this.changeDateTime = changeDateTime;
    return this;
  }

  /**
   * Get changeDateTime
   * @return changeDateTime
  **/
  

//  @Valid

  public OffsetDateTime getChangeDateTime() {
    return changeDateTime;
  }

  public void setChangeDateTime(OffsetDateTime changeDateTime) {
    this.changeDateTime = changeDateTime;
  }

  public ShipmentRequestPointTransportDocumentPositionDto changedByLogin(String changedByLogin) {
    this.changedByLogin = changedByLogin;
    return this;
  }

  /**
   * Get changedByLogin
   * @return changedByLogin
  **/
  


  public String getChangedByLogin() {
    return changedByLogin;
  }

  public void setChangedByLogin(String changedByLogin) {
    this.changedByLogin = changedByLogin;
  }

  public ShipmentRequestPointTransportDocumentPositionDto changedByUserId(Integer changedByUserId) {
    this.changedByUserId = changedByUserId;
    return this;
  }

  /**
   * Get changedByUserId
   * @return changedByUserId
  **/
  


  public Integer getChangedByUserId() {
    return changedByUserId;
  }

  public void setChangedByUserId(Integer changedByUserId) {
    this.changedByUserId = changedByUserId;
  }

  public ShipmentRequestPointTransportDocumentPositionDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
  **/
  


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public ShipmentRequestPointTransportDocumentPositionDto createDateTime(OffsetDateTime createDateTime) {
    this.createDateTime = createDateTime;
    return this;
  }

  /**
   * Get createDateTime
   * @return createDateTime
  **/
  

//  @Valid

  public OffsetDateTime getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(OffsetDateTime createDateTime) {
    this.createDateTime = createDateTime;
  }

  public ShipmentRequestPointTransportDocumentPositionDto id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ShipmentRequestPointTransportDocumentPositionDto measureUnits(String measureUnits) {
    this.measureUnits = measureUnits;
    return this;
  }

  /**
   * Get measureUnits
   * @return measureUnits
  **/
  


  public String getMeasureUnits() {
    return measureUnits;
  }

  public void setMeasureUnits(String measureUnits) {
    this.measureUnits = measureUnits;
  }

  public ShipmentRequestPointTransportDocumentPositionDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ShipmentRequestPointTransportDocumentPositionDto sequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Get sequenceNumber
   * @return sequenceNumber
  **/
  


  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public ShipmentRequestPointTransportDocumentPositionDto status(StatusEnum status) {
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

  public ShipmentRequestPointTransportDocumentPositionDto totalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
    return this;
  }

  /**
   * Get totalQuantity
   * @return totalQuantity
  **/
  


  public Integer getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentRequestPointTransportDocumentPositionDto shipmentRequestPointTransportDocumentPositionDto = (ShipmentRequestPointTransportDocumentPositionDto) o;
    return Objects.equals(this.acceptedQuantity, shipmentRequestPointTransportDocumentPositionDto.acceptedQuantity) &&
        Objects.equals(this.changeDateTime, shipmentRequestPointTransportDocumentPositionDto.changeDateTime) &&
        Objects.equals(this.changedByLogin, shipmentRequestPointTransportDocumentPositionDto.changedByLogin) &&
        Objects.equals(this.changedByUserId, shipmentRequestPointTransportDocumentPositionDto.changedByUserId) &&
        Objects.equals(this.comment, shipmentRequestPointTransportDocumentPositionDto.comment) &&
        Objects.equals(this.createDateTime, shipmentRequestPointTransportDocumentPositionDto.createDateTime) &&
        Objects.equals(this.id, shipmentRequestPointTransportDocumentPositionDto.id) &&
        Objects.equals(this.measureUnits, shipmentRequestPointTransportDocumentPositionDto.measureUnits) &&
        Objects.equals(this.name, shipmentRequestPointTransportDocumentPositionDto.name) &&
        Objects.equals(this.sequenceNumber, shipmentRequestPointTransportDocumentPositionDto.sequenceNumber) &&
        Objects.equals(this.status, shipmentRequestPointTransportDocumentPositionDto.status) &&
        Objects.equals(this.totalQuantity, shipmentRequestPointTransportDocumentPositionDto.totalQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acceptedQuantity, changeDateTime, changedByLogin, changedByUserId, comment, createDateTime, id, measureUnits, name, sequenceNumber, status, totalQuantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentRequestPointTransportDocumentPositionDto {\n");
    
    sb.append("    acceptedQuantity: ").append(toIndentedString(acceptedQuantity)).append("\n");
    sb.append("    changeDateTime: ").append(toIndentedString(changeDateTime)).append("\n");
    sb.append("    changedByLogin: ").append(toIndentedString(changedByLogin)).append("\n");
    sb.append("    changedByUserId: ").append(toIndentedString(changedByUserId)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    createDateTime: ").append(toIndentedString(createDateTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    measureUnits: ").append(toIndentedString(measureUnits)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalQuantity: ").append(toIndentedString(totalQuantity)).append("\n");
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

