package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import com.example.advantumconverter.gen.model.TransportDocumentDto;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;




/**
 * ShipmentStatePointDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentStatePointDto   {
  @JsonProperty("customerPointExternalId")
  private String customerPointExternalId = null;

  @JsonProperty("customerPointId")
  private Long customerPointId = null;

  @JsonProperty("dispatcherFinishTime")
  private OffsetDateTime dispatcherFinishTime = null;

  @JsonProperty("dispatcherStartTime")
  private OffsetDateTime dispatcherStartTime = null;

  @JsonProperty("documents")
//  @Valid
  private List<TransportDocumentDto> documents = null;

  @JsonProperty("driverFinishTime")
  private OffsetDateTime driverFinishTime = null;

  @JsonProperty("driverStartTime")
  private OffsetDateTime driverStartTime = null;

  @JsonProperty("finishTime")
  private OffsetDateTime finishTime = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("lat")
  private Double lat = null;

  @JsonProperty("localFinishTime")
  private OffsetDateTime localFinishTime = null;

  @JsonProperty("localFinishTimeOffset")
  private String localFinishTimeOffset = null;

  @JsonProperty("localStartTime")
  private OffsetDateTime localStartTime = null;

  @JsonProperty("localStartTimeOffset")
  private String localStartTimeOffset = null;

  @JsonProperty("locationAddress")
  private String locationAddress = null;

  @JsonProperty("locationName")
  private String locationName = null;

  @JsonProperty("lon")
  private Double lon = null;

  @JsonProperty("offset")
  private String offset = null;

  /**
   * Gets or Sets operations
   */
  public enum OperationsEnum {
    LOAD("LOAD"),
    
    UNLOAD("UNLOAD"),
    
    LOAD_AND_UNLOAD("LOAD_AND_UNLOAD"),
    
    VISIT("VISIT"),
    
    REFUELLING("REFUELLING");

    private String value;

    OperationsEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static OperationsEnum fromValue(String text) {
      for (OperationsEnum b : OperationsEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("operations")
//  @Valid
  private List<OperationsEnum> operations = new ArrayList<OperationsEnum>();

  @JsonProperty("sequenceNumber")
  private Integer sequenceNumber = null;

  @JsonProperty("startTime")
  private OffsetDateTime startTime = null;

  /**
   * Gets or Sets tag
   */
  public enum TagEnum {
    NOT_PASSED_YET("NOT_PASSED_YET"),
    
    ON_POINT("ON_POINT"),
    
    OK("OK"),
    
    NEAR("NEAR"),
    
    FAIL("FAIL");

    private String value;

    TagEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static TagEnum fromValue(String text) {
      for (TagEnum b : TagEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("tag")
  private TagEnum tag = null;

  @JsonProperty("telematicsFinishTime")
  private OffsetDateTime telematicsFinishTime = null;

  @JsonProperty("telematicsStartTime")
  private OffsetDateTime telematicsStartTime = null;

  public ShipmentStatePointDto customerPointExternalId(String customerPointExternalId) {
    this.customerPointExternalId = customerPointExternalId;
    return this;
  }

  /**
   * Внешний Id точки клиента
   * @return customerPointExternalId
  **/
  //@ApiModelProperty(value = "Внешний Id точки клиента")


  public String getCustomerPointExternalId() {
    return customerPointExternalId;
  }

  public void setCustomerPointExternalId(String customerPointExternalId) {
    this.customerPointExternalId = customerPointExternalId;
  }

  public ShipmentStatePointDto customerPointId(Long customerPointId) {
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

  public ShipmentStatePointDto dispatcherFinishTime(OffsetDateTime dispatcherFinishTime) {
    this.dispatcherFinishTime = dispatcherFinishTime;
    return this;
  }

  /**
   * Get dispatcherFinishTime
   * @return dispatcherFinishTime
  **/
  

//  @Valid

  public OffsetDateTime getDispatcherFinishTime() {
    return dispatcherFinishTime;
  }

  public void setDispatcherFinishTime(OffsetDateTime dispatcherFinishTime) {
    this.dispatcherFinishTime = dispatcherFinishTime;
  }

  public ShipmentStatePointDto dispatcherStartTime(OffsetDateTime dispatcherStartTime) {
    this.dispatcherStartTime = dispatcherStartTime;
    return this;
  }

  /**
   * Get dispatcherStartTime
   * @return dispatcherStartTime
  **/
  

//  @Valid

  public OffsetDateTime getDispatcherStartTime() {
    return dispatcherStartTime;
  }

  public void setDispatcherStartTime(OffsetDateTime dispatcherStartTime) {
    this.dispatcherStartTime = dispatcherStartTime;
  }

  public ShipmentStatePointDto documents(List<TransportDocumentDto> documents) {
    this.documents = documents;
    return this;
  }

  public ShipmentStatePointDto addDocumentsItem(TransportDocumentDto documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<TransportDocumentDto>();
    }
    this.documents.add(documentsItem);
    return this;
  }

  /**
   * Транспортные документы
   * @return documents
  **/
  //@ApiModelProperty(value = "Транспортные документы")

//  @Valid

  public List<TransportDocumentDto> getDocuments() {
    return documents;
  }

  public void setDocuments(List<TransportDocumentDto> documents) {
    this.documents = documents;
  }

  public ShipmentStatePointDto driverFinishTime(OffsetDateTime driverFinishTime) {
    this.driverFinishTime = driverFinishTime;
    return this;
  }

  /**
   * Get driverFinishTime
   * @return driverFinishTime
  **/
  

//  @Valid

  public OffsetDateTime getDriverFinishTime() {
    return driverFinishTime;
  }

  public void setDriverFinishTime(OffsetDateTime driverFinishTime) {
    this.driverFinishTime = driverFinishTime;
  }

  public ShipmentStatePointDto driverStartTime(OffsetDateTime driverStartTime) {
    this.driverStartTime = driverStartTime;
    return this;
  }

  /**
   * Get driverStartTime
   * @return driverStartTime
  **/
  

//  @Valid

  public OffsetDateTime getDriverStartTime() {
    return driverStartTime;
  }

  public void setDriverStartTime(OffsetDateTime driverStartTime) {
    this.driverStartTime = driverStartTime;
  }

  public ShipmentStatePointDto finishTime(OffsetDateTime finishTime) {
    this.finishTime = finishTime;
    return this;
  }

  /**
   * Время окончания
   * @return finishTime
  **/
  //@ApiModelProperty(value = "Время окончания")

//  @Valid

  public OffsetDateTime getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(OffsetDateTime finishTime) {
    this.finishTime = finishTime;
  }

  public ShipmentStatePointDto id(Long id) {
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

  public ShipmentStatePointDto lat(Double lat) {
    this.lat = lat;
    return this;
  }

  /**
   * Get lat
   * @return lat
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public ShipmentStatePointDto localFinishTime(OffsetDateTime localFinishTime) {
    this.localFinishTime = localFinishTime;
    return this;
  }

  /**
   * Время окончания
   * @return localFinishTime
  **/
  //@ApiModelProperty(value = "Время окончания")

//  @Valid

  public OffsetDateTime getLocalFinishTime() {
    return localFinishTime;
  }

  public void setLocalFinishTime(OffsetDateTime localFinishTime) {
    this.localFinishTime = localFinishTime;
  }

  public ShipmentStatePointDto localFinishTimeOffset(String localFinishTimeOffset) {
    this.localFinishTimeOffset = localFinishTimeOffset;
    return this;
  }

  /**
   * Время окончания
   * @return localFinishTimeOffset
  **/
  //@ApiModelProperty(value = "Время окончания")


  public String getLocalFinishTimeOffset() {
    return localFinishTimeOffset;
  }

  public void setLocalFinishTimeOffset(String localFinishTimeOffset) {
    this.localFinishTimeOffset = localFinishTimeOffset;
  }

  public ShipmentStatePointDto localStartTime(OffsetDateTime localStartTime) {
    this.localStartTime = localStartTime;
    return this;
  }

  /**
   * Время начала
   * @return localStartTime
  **/
  //@ApiModelProperty(value = "Время начала")

//  @Valid

  public OffsetDateTime getLocalStartTime() {
    return localStartTime;
  }

  public void setLocalStartTime(OffsetDateTime localStartTime) {
    this.localStartTime = localStartTime;
  }

  public ShipmentStatePointDto localStartTimeOffset(String localStartTimeOffset) {
    this.localStartTimeOffset = localStartTimeOffset;
    return this;
  }

  /**
   * Время начала
   * @return localStartTimeOffset
  **/
  //@ApiModelProperty(value = "Время начала")


  public String getLocalStartTimeOffset() {
    return localStartTimeOffset;
  }

  public void setLocalStartTimeOffset(String localStartTimeOffset) {
    this.localStartTimeOffset = localStartTimeOffset;
  }

  public ShipmentStatePointDto locationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
    return this;
  }

  /**
   * Get locationAddress
   * @return locationAddress
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public String getLocationAddress() {
    return locationAddress;
  }

  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  public ShipmentStatePointDto locationName(String locationName) {
    this.locationName = locationName;
    return this;
  }

  /**
   * Get locationName
   * @return locationName
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public ShipmentStatePointDto lon(Double lon) {
    this.lon = lon;
    return this;
  }

  /**
   * Get lon
   * @return lon
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public Double getLon() {
    return lon;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public ShipmentStatePointDto offset(String offset) {
    this.offset = offset;
    return this;
  }

  /**
   * Get offset
   * @return offset
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public ShipmentStatePointDto operations(List<OperationsEnum> operations) {
    this.operations = operations;
    return this;
  }

  public ShipmentStatePointDto addOperationsItem(OperationsEnum operationsItem) {
    this.operations.add(operationsItem);
    return this;
  }

  /**
   * Get operations
   * @return operations
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public List<OperationsEnum> getOperations() {
    return operations;
  }

  public void setOperations(List<OperationsEnum> operations) {
    this.operations = operations;
  }

  public ShipmentStatePointDto sequenceNumber(Integer sequenceNumber) {
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

  public ShipmentStatePointDto startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Время начала
   * @return startTime
  **/
  //@ApiModelProperty(value = "Время начала")

//  @Valid

  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public ShipmentStatePointDto tag(TagEnum tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get tag
   * @return tag
  **/
  


  public TagEnum getTag() {
    return tag;
  }

  public void setTag(TagEnum tag) {
    this.tag = tag;
  }

  public ShipmentStatePointDto telematicsFinishTime(OffsetDateTime telematicsFinishTime) {
    this.telematicsFinishTime = telematicsFinishTime;
    return this;
  }

  /**
   * Get telematicsFinishTime
   * @return telematicsFinishTime
  **/
  

//  @Valid

  public OffsetDateTime getTelematicsFinishTime() {
    return telematicsFinishTime;
  }

  public void setTelematicsFinishTime(OffsetDateTime telematicsFinishTime) {
    this.telematicsFinishTime = telematicsFinishTime;
  }

  public ShipmentStatePointDto telematicsStartTime(OffsetDateTime telematicsStartTime) {
    this.telematicsStartTime = telematicsStartTime;
    return this;
  }

  /**
   * Get telematicsStartTime
   * @return telematicsStartTime
  **/
  

//  @Valid

  public OffsetDateTime getTelematicsStartTime() {
    return telematicsStartTime;
  }

  public void setTelematicsStartTime(OffsetDateTime telematicsStartTime) {
    this.telematicsStartTime = telematicsStartTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentStatePointDto shipmentStatePointDto = (ShipmentStatePointDto) o;
    return Objects.equals(this.customerPointExternalId, shipmentStatePointDto.customerPointExternalId) &&
        Objects.equals(this.customerPointId, shipmentStatePointDto.customerPointId) &&
        Objects.equals(this.dispatcherFinishTime, shipmentStatePointDto.dispatcherFinishTime) &&
        Objects.equals(this.dispatcherStartTime, shipmentStatePointDto.dispatcherStartTime) &&
        Objects.equals(this.documents, shipmentStatePointDto.documents) &&
        Objects.equals(this.driverFinishTime, shipmentStatePointDto.driverFinishTime) &&
        Objects.equals(this.driverStartTime, shipmentStatePointDto.driverStartTime) &&
        Objects.equals(this.finishTime, shipmentStatePointDto.finishTime) &&
        Objects.equals(this.id, shipmentStatePointDto.id) &&
        Objects.equals(this.lat, shipmentStatePointDto.lat) &&
        Objects.equals(this.localFinishTime, shipmentStatePointDto.localFinishTime) &&
        Objects.equals(this.localFinishTimeOffset, shipmentStatePointDto.localFinishTimeOffset) &&
        Objects.equals(this.localStartTime, shipmentStatePointDto.localStartTime) &&
        Objects.equals(this.localStartTimeOffset, shipmentStatePointDto.localStartTimeOffset) &&
        Objects.equals(this.locationAddress, shipmentStatePointDto.locationAddress) &&
        Objects.equals(this.locationName, shipmentStatePointDto.locationName) &&
        Objects.equals(this.lon, shipmentStatePointDto.lon) &&
        Objects.equals(this.offset, shipmentStatePointDto.offset) &&
        Objects.equals(this.operations, shipmentStatePointDto.operations) &&
        Objects.equals(this.sequenceNumber, shipmentStatePointDto.sequenceNumber) &&
        Objects.equals(this.startTime, shipmentStatePointDto.startTime) &&
        Objects.equals(this.tag, shipmentStatePointDto.tag) &&
        Objects.equals(this.telematicsFinishTime, shipmentStatePointDto.telematicsFinishTime) &&
        Objects.equals(this.telematicsStartTime, shipmentStatePointDto.telematicsStartTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerPointExternalId, customerPointId, dispatcherFinishTime, dispatcherStartTime, documents, driverFinishTime, driverStartTime, finishTime, id, lat, localFinishTime, localFinishTimeOffset, localStartTime, localStartTimeOffset, locationAddress, locationName, lon, offset, operations, sequenceNumber, startTime, tag, telematicsFinishTime, telematicsStartTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentStatePointDto {\n");
    
    sb.append("    customerPointExternalId: ").append(toIndentedString(customerPointExternalId)).append("\n");
    sb.append("    customerPointId: ").append(toIndentedString(customerPointId)).append("\n");
    sb.append("    dispatcherFinishTime: ").append(toIndentedString(dispatcherFinishTime)).append("\n");
    sb.append("    dispatcherStartTime: ").append(toIndentedString(dispatcherStartTime)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    driverFinishTime: ").append(toIndentedString(driverFinishTime)).append("\n");
    sb.append("    driverStartTime: ").append(toIndentedString(driverStartTime)).append("\n");
    sb.append("    finishTime: ").append(toIndentedString(finishTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    localFinishTime: ").append(toIndentedString(localFinishTime)).append("\n");
    sb.append("    localFinishTimeOffset: ").append(toIndentedString(localFinishTimeOffset)).append("\n");
    sb.append("    localStartTime: ").append(toIndentedString(localStartTime)).append("\n");
    sb.append("    localStartTimeOffset: ").append(toIndentedString(localStartTimeOffset)).append("\n");
    sb.append("    locationAddress: ").append(toIndentedString(locationAddress)).append("\n");
    sb.append("    locationName: ").append(toIndentedString(locationName)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    operations: ").append(toIndentedString(operations)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    telematicsFinishTime: ").append(toIndentedString(telematicsFinishTime)).append("\n");
    sb.append("    telematicsStartTime: ").append(toIndentedString(telematicsStartTime)).append("\n");
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

