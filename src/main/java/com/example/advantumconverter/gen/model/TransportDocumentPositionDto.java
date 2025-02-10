package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import java.time.OffsetDateTime;




/**
 * TransportDocumentPositionDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class TransportDocumentPositionDto   {
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

  public TransportDocumentPositionDto acceptedQuantity(Integer acceptedQuantity) {
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

  public TransportDocumentPositionDto changeDateTime(OffsetDateTime changeDateTime) {
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

  public TransportDocumentPositionDto changedByLogin(String changedByLogin) {
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

  public TransportDocumentPositionDto changedByUserId(Integer changedByUserId) {
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

  public TransportDocumentPositionDto comment(String comment) {
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

  public TransportDocumentPositionDto createDateTime(OffsetDateTime createDateTime) {
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

  public TransportDocumentPositionDto id(Long id) {
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

  public TransportDocumentPositionDto measureUnits(String measureUnits) {
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

  public TransportDocumentPositionDto name(String name) {
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

  public TransportDocumentPositionDto sequenceNumber(Integer sequenceNumber) {
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

  public TransportDocumentPositionDto status(StatusEnum status) {
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

  public TransportDocumentPositionDto totalQuantity(Integer totalQuantity) {
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
    TransportDocumentPositionDto transportDocumentPositionDto = (TransportDocumentPositionDto) o;
    return Objects.equals(this.acceptedQuantity, transportDocumentPositionDto.acceptedQuantity) &&
        Objects.equals(this.changeDateTime, transportDocumentPositionDto.changeDateTime) &&
        Objects.equals(this.changedByLogin, transportDocumentPositionDto.changedByLogin) &&
        Objects.equals(this.changedByUserId, transportDocumentPositionDto.changedByUserId) &&
        Objects.equals(this.comment, transportDocumentPositionDto.comment) &&
        Objects.equals(this.createDateTime, transportDocumentPositionDto.createDateTime) &&
        Objects.equals(this.id, transportDocumentPositionDto.id) &&
        Objects.equals(this.measureUnits, transportDocumentPositionDto.measureUnits) &&
        Objects.equals(this.name, transportDocumentPositionDto.name) &&
        Objects.equals(this.sequenceNumber, transportDocumentPositionDto.sequenceNumber) &&
        Objects.equals(this.status, transportDocumentPositionDto.status) &&
        Objects.equals(this.totalQuantity, transportDocumentPositionDto.totalQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acceptedQuantity, changeDateTime, changedByLogin, changedByUserId, comment, createDateTime, id, measureUnits, name, sequenceNumber, status, totalQuantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransportDocumentPositionDto {\n");
    
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

