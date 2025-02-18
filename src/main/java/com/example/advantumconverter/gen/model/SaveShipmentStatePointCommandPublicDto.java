package com.example.advantumconverter.gen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




/**
 * SaveShipmentStatePointCommandPublicDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class SaveShipmentStatePointCommandPublicDto   {
  @JsonProperty("customerPointExternalId")
  private String customerPointExternalId = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("offsetInterval")
  private OffsetIntervalPublicDto offsetInterval = null;

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

  public SaveShipmentStatePointCommandPublicDto customerPointExternalId(String customerPointExternalId) {
    this.customerPointExternalId = customerPointExternalId;
    return this;
  }

  /**
   * Get customerPointExternalId
   * @return customerPointExternalId
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull


  public String getCustomerPointExternalId() {
    return customerPointExternalId;
  }

  public void setCustomerPointExternalId(String customerPointExternalId) {
    this.customerPointExternalId = customerPointExternalId;
  }

  public SaveShipmentStatePointCommandPublicDto id(Long id) {
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

  public SaveShipmentStatePointCommandPublicDto offsetInterval(OffsetIntervalPublicDto offsetInterval) {
    this.offsetInterval = offsetInterval;
    return this;
  }

  /**
   * Get offsetInterval
   * @return offsetInterval
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull

//  @Valid

  public OffsetIntervalPublicDto getOffsetInterval() {
    return offsetInterval;
  }

  public void setOffsetInterval(OffsetIntervalPublicDto offsetInterval) {
    this.offsetInterval = offsetInterval;
  }

  public SaveShipmentStatePointCommandPublicDto operations(List<OperationsEnum> operations) {
    this.operations = operations;
    return this;
  }

  public SaveShipmentStatePointCommandPublicDto addOperationsItem(OperationsEnum operationsItem) {
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

  public SaveShipmentStatePointCommandPublicDto sequenceNumber(Integer sequenceNumber) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveShipmentStatePointCommandPublicDto saveShipmentStatePointCommandPublicDto = (SaveShipmentStatePointCommandPublicDto) o;
    return Objects.equals(this.customerPointExternalId, saveShipmentStatePointCommandPublicDto.customerPointExternalId) &&
        Objects.equals(this.id, saveShipmentStatePointCommandPublicDto.id) &&
        Objects.equals(this.offsetInterval, saveShipmentStatePointCommandPublicDto.offsetInterval) &&
        Objects.equals(this.operations, saveShipmentStatePointCommandPublicDto.operations) &&
        Objects.equals(this.sequenceNumber, saveShipmentStatePointCommandPublicDto.sequenceNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerPointExternalId, id, offsetInterval, operations, sequenceNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SaveShipmentStatePointCommandPublicDto {\n");
    
    sb.append("    customerPointExternalId: ").append(toIndentedString(customerPointExternalId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    offsetInterval: ").append(toIndentedString(offsetInterval)).append("\n");
    sb.append("    operations: ").append(toIndentedString(operations)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
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

