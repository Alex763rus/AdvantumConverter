package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import com.example.advantumconverter.gen.model.ShipmentRequestPointTransportDocumentPositionDto;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;




/**
 * ShipmentRequestPointTransportDocumentDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentRequestPointTransportDocumentDto   {
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

  @JsonProperty("date")
  private OffsetDateTime date = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("number")
  private String number = null;

  @JsonProperty("positions")
//  @Valid
  private List<ShipmentRequestPointTransportDocumentPositionDto> positions = null;

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

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TRANSPORT_NAKL("TRANSPORT_NAKL"),
    
    TOVAR_TRANSPORT_NAKL("TOVAR_TRANSPORT_NAKL"),
    
    INVOICE("INVOICE"),
    
    UNIVERSAL_TRANSFER_DOCUMENT("UNIVERSAL_TRANSFER_DOCUMENT");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  public ShipmentRequestPointTransportDocumentDto changeDateTime(OffsetDateTime changeDateTime) {
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

  public ShipmentRequestPointTransportDocumentDto changedByLogin(String changedByLogin) {
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

  public ShipmentRequestPointTransportDocumentDto changedByUserId(Integer changedByUserId) {
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

  public ShipmentRequestPointTransportDocumentDto comment(String comment) {
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

  public ShipmentRequestPointTransportDocumentDto createDateTime(OffsetDateTime createDateTime) {
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

  public ShipmentRequestPointTransportDocumentDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  **/
  

//  @Valid

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShipmentRequestPointTransportDocumentDto id(Long id) {
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

  public ShipmentRequestPointTransportDocumentDto number(String number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
  **/
  


  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public ShipmentRequestPointTransportDocumentDto positions(List<ShipmentRequestPointTransportDocumentPositionDto> positions) {
    this.positions = positions;
    return this;
  }

  public ShipmentRequestPointTransportDocumentDto addPositionsItem(ShipmentRequestPointTransportDocumentPositionDto positionsItem) {
    if (this.positions == null) {
      this.positions = new ArrayList<ShipmentRequestPointTransportDocumentPositionDto>();
    }
    this.positions.add(positionsItem);
    return this;
  }

  /**
   * Get positions
   * @return positions
  **/
  

//  @Valid

  public List<ShipmentRequestPointTransportDocumentPositionDto> getPositions() {
    return positions;
  }

  public void setPositions(List<ShipmentRequestPointTransportDocumentPositionDto> positions) {
    this.positions = positions;
  }

  public ShipmentRequestPointTransportDocumentDto status(StatusEnum status) {
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

  public ShipmentRequestPointTransportDocumentDto type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  **/
  


  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentRequestPointTransportDocumentDto shipmentRequestPointTransportDocumentDto = (ShipmentRequestPointTransportDocumentDto) o;
    return Objects.equals(this.changeDateTime, shipmentRequestPointTransportDocumentDto.changeDateTime) &&
        Objects.equals(this.changedByLogin, shipmentRequestPointTransportDocumentDto.changedByLogin) &&
        Objects.equals(this.changedByUserId, shipmentRequestPointTransportDocumentDto.changedByUserId) &&
        Objects.equals(this.comment, shipmentRequestPointTransportDocumentDto.comment) &&
        Objects.equals(this.createDateTime, shipmentRequestPointTransportDocumentDto.createDateTime) &&
        Objects.equals(this.date, shipmentRequestPointTransportDocumentDto.date) &&
        Objects.equals(this.id, shipmentRequestPointTransportDocumentDto.id) &&
        Objects.equals(this.number, shipmentRequestPointTransportDocumentDto.number) &&
        Objects.equals(this.positions, shipmentRequestPointTransportDocumentDto.positions) &&
        Objects.equals(this.status, shipmentRequestPointTransportDocumentDto.status) &&
        Objects.equals(this.type, shipmentRequestPointTransportDocumentDto.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(changeDateTime, changedByLogin, changedByUserId, comment, createDateTime, date, id, number, positions, status, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentRequestPointTransportDocumentDto {\n");
    
    sb.append("    changeDateTime: ").append(toIndentedString(changeDateTime)).append("\n");
    sb.append("    changedByLogin: ").append(toIndentedString(changedByLogin)).append("\n");
    sb.append("    changedByUserId: ").append(toIndentedString(changedByUserId)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    createDateTime: ").append(toIndentedString(createDateTime)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    positions: ").append(toIndentedString(positions)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

