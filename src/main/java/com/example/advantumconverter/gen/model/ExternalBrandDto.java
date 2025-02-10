package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;







/**
 * ExternalBrandDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ExternalBrandDto   {
  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("name")
  private String name = null;

  public ExternalBrandDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * ИД марки ТС в ИС Предприятия
   * @return externalId
  **/
  //@ApiModelProperty(required = true, value = "ИД марки ТС в ИС Предприятия")
  //@NotNull


  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public ExternalBrandDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Наименование марки ТС
   * @return name
  **/
  //@ApiModelProperty(required = true, value = "Наименование марки ТС")
  //@NotNull


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
    ExternalBrandDto externalBrandDto = (ExternalBrandDto) o;
    return Objects.equals(this.externalId, externalBrandDto.externalId) &&
        Objects.equals(this.name, externalBrandDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalBrandDto {\n");
    
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
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

