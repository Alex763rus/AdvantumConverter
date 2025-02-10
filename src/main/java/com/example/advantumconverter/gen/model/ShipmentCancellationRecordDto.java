package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;







/**
 * ShipmentCancellationRecordDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentCancellationRecordDto   {
  @JsonProperty("cancelationReasonId")
  private Integer cancelationReasonId = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("name")
  private String name = null;

  public ShipmentCancellationRecordDto cancelationReasonId(Integer cancelationReasonId) {
    this.cancelationReasonId = cancelationReasonId;
    return this;
  }

  /**
   * ID причины отмены рейса
   * @return cancelationReasonId
  **/
  //@ApiModelProperty(value = "ID причины отмены рейса")


  public Integer getCancelationReasonId() {
    return cancelationReasonId;
  }

  public void setCancelationReasonId(Integer cancelationReasonId) {
    this.cancelationReasonId = cancelationReasonId;
  }

  public ShipmentCancellationRecordDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Комментарий пользователя при отмене рейса
   * @return comment
  **/
  //@ApiModelProperty(value = "Комментарий пользователя при отмене рейса")


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public ShipmentCancellationRecordDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание типа причины
   * @return description
  **/
  //@ApiModelProperty(value = "Описание типа причины")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ShipmentCancellationRecordDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Тип причины отмены
   * @return name
  **/
  //@ApiModelProperty(value = "Тип причины отмены")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentCancellationRecordDto shipmentCancellationRecordDto = (ShipmentCancellationRecordDto) o;
    return Objects.equals(this.cancelationReasonId, shipmentCancellationRecordDto.cancelationReasonId) &&
        Objects.equals(this.comment, shipmentCancellationRecordDto.comment) &&
        Objects.equals(this.description, shipmentCancellationRecordDto.description) &&
        Objects.equals(this.name, shipmentCancellationRecordDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cancelationReasonId, comment, description, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentCancellationRecordDto {\n");
    
    sb.append("    cancelationReasonId: ").append(toIndentedString(cancelationReasonId)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

