package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.time.OffsetDateTime;




/**
 * ShipmentFileDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentFileDto   {
  @JsonProperty("fileId")
  private String fileId = null;

  @JsonProperty("filename")
  private String filename = null;

  @JsonProperty("addedDateTime")
  private OffsetDateTime addedDateTime = null;

  @JsonProperty("addedByFullName")
  private String addedByFullName = null;

  @JsonProperty("addedByLogin")
  private String addedByLogin = null;

  public ShipmentFileDto fileId(String fileId) {
    this.fileId = fileId;
    return this;
  }

  /**
   * Идентификатор файла (UUID)
   * @return fileId
  **/
  //@ApiModelProperty(value = "Идентификатор файла (UUID)")


  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public ShipmentFileDto filename(String filename) {
    this.filename = filename;
    return this;
  }

  /**
   * Наименование файла
   * @return filename
  **/
  //@ApiModelProperty(value = "Наименование файла")


  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public ShipmentFileDto addedDateTime(OffsetDateTime addedDateTime) {
    this.addedDateTime = addedDateTime;
    return this;
  }

  /**
   * Добавлен (Дата) – дата и время добавления файла
   * @return addedDateTime
  **/
  //@ApiModelProperty(value = "Добавлен (Дата) – дата и время добавления файла")

//  @Valid

  public OffsetDateTime getAddedDateTime() {
    return addedDateTime;
  }

  public void setAddedDateTime(OffsetDateTime addedDateTime) {
    this.addedDateTime = addedDateTime;
  }

  public ShipmentFileDto addedByFullName(String addedByFullName) {
    this.addedByFullName = addedByFullName;
    return this;
  }

  /**
   * Добавлен (ФИО)
   * @return addedByFullName
  **/
  //@ApiModelProperty(value = "Добавлен (ФИО)")


  public String getAddedByFullName() {
    return addedByFullName;
  }

  public void setAddedByFullName(String addedByFullName) {
    this.addedByFullName = addedByFullName;
  }

  public ShipmentFileDto addedByLogin(String addedByLogin) {
    this.addedByLogin = addedByLogin;
    return this;
  }

  /**
   * Добавлен (Логин)
   * @return addedByLogin
  **/
  //@ApiModelProperty(value = "Добавлен (Логин)")


  public String getAddedByLogin() {
    return addedByLogin;
  }

  public void setAddedByLogin(String addedByLogin) {
    this.addedByLogin = addedByLogin;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentFileDto shipmentFileDto = (ShipmentFileDto) o;
    return Objects.equals(this.fileId, shipmentFileDto.fileId) &&
        Objects.equals(this.filename, shipmentFileDto.filename) &&
        Objects.equals(this.addedDateTime, shipmentFileDto.addedDateTime) &&
        Objects.equals(this.addedByFullName, shipmentFileDto.addedByFullName) &&
        Objects.equals(this.addedByLogin, shipmentFileDto.addedByLogin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId, filename, addedDateTime, addedByFullName, addedByLogin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentFileDto {\n");
    
    sb.append("    fileId: ").append(toIndentedString(fileId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    addedDateTime: ").append(toIndentedString(addedDateTime)).append("\n");
    sb.append("    addedByFullName: ").append(toIndentedString(addedByFullName)).append("\n");
    sb.append("    addedByLogin: ").append(toIndentedString(addedByLogin)).append("\n");
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

