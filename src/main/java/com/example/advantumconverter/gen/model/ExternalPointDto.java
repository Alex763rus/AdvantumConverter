package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import com.example.advantumconverter.gen.model.ExternalOrganizationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;




/**
 * ExternalPointDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ExternalPointDto   {
  @JsonProperty("coordsNotNull")
  private Boolean coordsNotNull = null;

  @JsonProperty("valid")
  private Boolean valid = null;

  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("lat")
  private BigDecimal lat = null;

  @JsonProperty("lon")
  private BigDecimal lon = null;

  @JsonProperty("consignee")
  private ExternalOrganizationDto consignee = null;

  public ExternalPointDto coordsNotNull(Boolean coordsNotNull) {
    this.coordsNotNull = coordsNotNull;
    return this;
  }

  /**
   * Get coordsNotNull
   * @return coordsNotNull
  **/
  


  public Boolean isCoordsNotNull() {
    return coordsNotNull;
  }

  public void setCoordsNotNull(Boolean coordsNotNull) {
    this.coordsNotNull = coordsNotNull;
  }

  public ExternalPointDto valid(Boolean valid) {
    this.valid = valid;
    return this;
  }

  /**
   * Get valid
   * @return valid
  **/
  


  public Boolean isValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public ExternalPointDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * ИД точки в ИС Предприятия
   * @return externalId
  **/
  //@ApiModelProperty(required = true, value = "ИД точки в ИС Предприятия")
  //@NotNull


  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public ExternalPointDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Наименование
   * @return name
  **/
  //@ApiModelProperty(required = true, value = "Наименование")
  //@NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ExternalPointDto address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Адрес
   * @return address
  **/
  //@ApiModelProperty(required = true, value = "Адрес")
  //@NotNull


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public ExternalPointDto lat(BigDecimal lat) {
    this.lat = lat;
    return this;
  }

  /**
   * Широта
   * @return lat
  **/
  //@ApiModelProperty(value = "Широта")

//  @Valid

  public BigDecimal getLat() {
    return lat;
  }

  public void setLat(BigDecimal lat) {
    this.lat = lat;
  }

  public ExternalPointDto lon(BigDecimal lon) {
    this.lon = lon;
    return this;
  }

  /**
   * Долгота
   * @return lon
  **/
  //@ApiModelProperty(value = "Долгота")

//  @Valid

  public BigDecimal getLon() {
    return lon;
  }

  public void setLon(BigDecimal lon) {
    this.lon = lon;
  }

  public ExternalPointDto consignee(ExternalOrganizationDto consignee) {
    this.consignee = consignee;
    return this;
  }

  /**
   * Грузополучатель
   * @return consignee
  **/
  //@ApiModelProperty(value = "Грузополучатель")

//  @Valid

  public ExternalOrganizationDto getConsignee() {
    return consignee;
  }

  public void setConsignee(ExternalOrganizationDto consignee) {
    this.consignee = consignee;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalPointDto externalPointDto = (ExternalPointDto) o;
    return Objects.equals(this.coordsNotNull, externalPointDto.coordsNotNull) &&
        Objects.equals(this.valid, externalPointDto.valid) &&
        Objects.equals(this.externalId, externalPointDto.externalId) &&
        Objects.equals(this.name, externalPointDto.name) &&
        Objects.equals(this.address, externalPointDto.address) &&
        Objects.equals(this.lat, externalPointDto.lat) &&
        Objects.equals(this.lon, externalPointDto.lon) &&
        Objects.equals(this.consignee, externalPointDto.consignee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordsNotNull, valid, externalId, name, address, lat, lon, consignee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalPointDto {\n");
    
    sb.append("    coordsNotNull: ").append(toIndentedString(coordsNotNull)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    consignee: ").append(toIndentedString(consignee)).append("\n");
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

