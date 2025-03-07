package com.example.advantumconverter.gen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




/**
 * ShipmentRequestPointDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentRequestPointDto   {
  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("customerPointId")
  private Long customerPointId = null;

  @JsonProperty("documents")
//  @Valid
  private List<ShipmentRequestPointTransportDocumentDto> documents = null;

  @JsonProperty("finishTime")
  private OffsetDateTime finishTime = null;

  @JsonProperty("finishTimeOffSet")
  private String finishTimeOffSet = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("interval")
  private OffsetIntervalDto interval = null;

  @JsonProperty("location")
  private LocationDto location = null;

  /**
   * Gets or Sets operation
   */
  public enum OperationEnum {
    LOAD("LOAD"),
    
    UNLOAD("UNLOAD"),
    
    LOAD_AND_UNLOAD("LOAD_AND_UNLOAD"),
    
    VISIT("VISIT"),
    
    REFUELLING("REFUELLING");

    private String value;

    OperationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static OperationEnum fromValue(String text) {
      for (OperationEnum b : OperationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("operation")
  private OperationEnum operation = null;

  @JsonProperty("orderList")
//  @Valid
  private List<String> orderList = null;

  @JsonProperty("sequenceNumber")
  private Integer sequenceNumber = null;

  @JsonProperty("startTime")
  private OffsetDateTime startTime = null;

  @JsonProperty("startTimeOffset")
  private String startTimeOffset = null;

  public ShipmentRequestPointDto comment(String comment) {
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

  public ShipmentRequestPointDto customerPointId(Long customerPointId) {
    this.customerPointId = customerPointId;
    return this;
  }

  /**
   * Get customerPointId
   * @return customerPointId
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public Long getCustomerPointId() {
    return customerPointId;
  }

  public void setCustomerPointId(Long customerPointId) {
    this.customerPointId = customerPointId;
  }

  public ShipmentRequestPointDto documents(List<ShipmentRequestPointTransportDocumentDto> documents) {
    this.documents = documents;
    return this;
  }

  public ShipmentRequestPointDto addDocumentsItem(ShipmentRequestPointTransportDocumentDto documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<ShipmentRequestPointTransportDocumentDto>();
    }
    this.documents.add(documentsItem);
    return this;
  }

  /**
   * Get documents
   * @return documents
  **/
  

//  @Valid

  public List<ShipmentRequestPointTransportDocumentDto> getDocuments() {
    return documents;
  }

  public void setDocuments(List<ShipmentRequestPointTransportDocumentDto> documents) {
    this.documents = documents;
  }

  public ShipmentRequestPointDto finishTime(OffsetDateTime finishTime) {
    this.finishTime = finishTime;
    return this;
  }

  /**
   * Get finishTime
   * @return finishTime
  **/
  

//  @Valid

  public OffsetDateTime getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(OffsetDateTime finishTime) {
    this.finishTime = finishTime;
  }

  public ShipmentRequestPointDto finishTimeOffSet(String finishTimeOffSet) {
    this.finishTimeOffSet = finishTimeOffSet;
    return this;
  }

  /**
   * Get finishTimeOffSet
   * @return finishTimeOffSet
  **/
  


  public String getFinishTimeOffSet() {
    return finishTimeOffSet;
  }

  public void setFinishTimeOffSet(String finishTimeOffSet) {
    this.finishTimeOffSet = finishTimeOffSet;
  }

  public ShipmentRequestPointDto id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Не заполняется при создании нового шаблона маршрута
   * @return id
  **/
  //@ApiModelProperty(value = "Не заполняется при создании нового шаблона маршрута")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ShipmentRequestPointDto interval(OffsetIntervalDto interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Get interval
   * @return interval
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull

//  @Valid

  public OffsetIntervalDto getInterval() {
    return interval;
  }

  public void setInterval(OffsetIntervalDto interval) {
    this.interval = interval;
  }

  public ShipmentRequestPointDto location(LocationDto location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull

//  @Valid

  public LocationDto getLocation() {
    return location;
  }

  public void setLocation(LocationDto location) {
    this.location = location;
  }

  public ShipmentRequestPointDto operation(OperationEnum operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Get operation
   * @return operation
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public OperationEnum getOperation() {
    return operation;
  }

  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }

  public ShipmentRequestPointDto orderList(List<String> orderList) {
    this.orderList = orderList;
    return this;
  }

  public ShipmentRequestPointDto addOrderListItem(String orderListItem) {
    if (this.orderList == null) {
      this.orderList = new ArrayList<String>();
    }
    this.orderList.add(orderListItem);
    return this;
  }

  /**
   * Get orderList
   * @return orderList
  **/
  


  public List<String> getOrderList() {
    return orderList;
  }

  public void setOrderList(List<String> orderList) {
    this.orderList = orderList;
  }

  public ShipmentRequestPointDto sequenceNumber(Integer sequenceNumber) {
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

  public ShipmentRequestPointDto startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
  **/
  

//  @Valid

  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public ShipmentRequestPointDto startTimeOffset(String startTimeOffset) {
    this.startTimeOffset = startTimeOffset;
    return this;
  }

  /**
   * Get startTimeOffset
   * @return startTimeOffset
  **/
  


  public String getStartTimeOffset() {
    return startTimeOffset;
  }

  public void setStartTimeOffset(String startTimeOffset) {
    this.startTimeOffset = startTimeOffset;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentRequestPointDto shipmentRequestPointDto = (ShipmentRequestPointDto) o;
    return Objects.equals(this.comment, shipmentRequestPointDto.comment) &&
        Objects.equals(this.customerPointId, shipmentRequestPointDto.customerPointId) &&
        Objects.equals(this.documents, shipmentRequestPointDto.documents) &&
        Objects.equals(this.finishTime, shipmentRequestPointDto.finishTime) &&
        Objects.equals(this.finishTimeOffSet, shipmentRequestPointDto.finishTimeOffSet) &&
        Objects.equals(this.id, shipmentRequestPointDto.id) &&
        Objects.equals(this.interval, shipmentRequestPointDto.interval) &&
        Objects.equals(this.location, shipmentRequestPointDto.location) &&
        Objects.equals(this.operation, shipmentRequestPointDto.operation) &&
        Objects.equals(this.orderList, shipmentRequestPointDto.orderList) &&
        Objects.equals(this.sequenceNumber, shipmentRequestPointDto.sequenceNumber) &&
        Objects.equals(this.startTime, shipmentRequestPointDto.startTime) &&
        Objects.equals(this.startTimeOffset, shipmentRequestPointDto.startTimeOffset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment, customerPointId, documents, finishTime, finishTimeOffSet, id, interval, location, operation, orderList, sequenceNumber, startTime, startTimeOffset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentRequestPointDto {\n");
    
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    customerPointId: ").append(toIndentedString(customerPointId)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    finishTime: ").append(toIndentedString(finishTime)).append("\n");
    sb.append("    finishTimeOffSet: ").append(toIndentedString(finishTimeOffSet)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    orderList: ").append(toIndentedString(orderList)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    startTimeOffset: ").append(toIndentedString(startTimeOffset)).append("\n");
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

