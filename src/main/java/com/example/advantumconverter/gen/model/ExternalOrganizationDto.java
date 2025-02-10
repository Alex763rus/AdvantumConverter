package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ExternalOrganizationDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ExternalOrganizationDto   {
  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("name")
  private String name = null;

  public ExternalOrganizationDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * ИД организации в ИС предприятия
   * @return externalId
  **/
  //@ApiModelProperty(required = true, value = "ИД организации в ИС предприятия")
  //@NotNull


  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public ExternalOrganizationDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Наименование организации
   * @return name
  **/
  //@ApiModelProperty(required = true, value = "Наименование организации")
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
    ExternalOrganizationDto externalOrganizationDto = (ExternalOrganizationDto) o;
    return Objects.equals(this.externalId, externalOrganizationDto.externalId) &&
        Objects.equals(this.name, externalOrganizationDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalOrganizationDto {\n");
    
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

