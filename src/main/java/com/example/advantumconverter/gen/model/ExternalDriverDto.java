package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ExternalDriverDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ExternalDriverDto   {
  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("middleName")
  private String middleName = null;

  public ExternalDriverDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * ИД водителя в ИС Предприятия
   * @return externalId
  **/
  //@ApiModelProperty(required = true, value = "ИД водителя в ИС Предприятия")
  //@NotNull


  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public ExternalDriverDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Фамилия
   * @return lastName
  **/
  //@ApiModelProperty(required = true, value = "Фамилия")
  //@NotNull


  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public ExternalDriverDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Имя
   * @return firstName
  **/
  //@ApiModelProperty(required = true, value = "Имя")
  //@NotNull


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public ExternalDriverDto middleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  /**
   * Отчество
   * @return middleName
  **/
  //@ApiModelProperty(value = "Отчество")


  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalDriverDto externalDriverDto = (ExternalDriverDto) o;
    return Objects.equals(this.externalId, externalDriverDto.externalId) &&
        Objects.equals(this.lastName, externalDriverDto.lastName) &&
        Objects.equals(this.firstName, externalDriverDto.firstName) &&
        Objects.equals(this.middleName, externalDriverDto.middleName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalId, lastName, firstName, middleName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalDriverDto {\n");
    
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
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

