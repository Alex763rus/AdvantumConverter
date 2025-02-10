package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.time.OffsetDateTime;




/**
 * ShipmentFileWebDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class ShipmentFileWebDto   {
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

  @JsonProperty("deletedDateTime")
  private OffsetDateTime deletedDateTime = null;

  @JsonProperty("deletedByFullName")
  private String deletedByFullName = null;

  @JsonProperty("deletedByLogin")
  private String deletedByLogin = null;

  public ShipmentFileWebDto fileId(String fileId) {
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

  public ShipmentFileWebDto filename(String filename) {
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

  public ShipmentFileWebDto addedDateTime(OffsetDateTime addedDateTime) {
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

  public ShipmentFileWebDto addedByFullName(String addedByFullName) {
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

  public ShipmentFileWebDto addedByLogin(String addedByLogin) {
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

  public ShipmentFileWebDto deletedDateTime(OffsetDateTime deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
    return this;
  }

  /**
   * Удален (Дата) – дата и время удаления файла
   * @return deletedDateTime
  **/
  //@ApiModelProperty(value = "Удален (Дата) – дата и время удаления файла")

//  @Valid

  public OffsetDateTime getDeletedDateTime() {
    return deletedDateTime;
  }

  public void setDeletedDateTime(OffsetDateTime deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
  }

  public ShipmentFileWebDto deletedByFullName(String deletedByFullName) {
    this.deletedByFullName = deletedByFullName;
    return this;
  }

  /**
   * Удален (ФИО)
   * @return deletedByFullName
  **/
  //@ApiModelProperty(value = "Удален (ФИО)")


  public String getDeletedByFullName() {
    return deletedByFullName;
  }

  public void setDeletedByFullName(String deletedByFullName) {
    this.deletedByFullName = deletedByFullName;
  }

  public ShipmentFileWebDto deletedByLogin(String deletedByLogin) {
    this.deletedByLogin = deletedByLogin;
    return this;
  }

  /**
   * Удален (Логин)
   * @return deletedByLogin
  **/
  //@ApiModelProperty(value = "Удален (Логин)")


  public String getDeletedByLogin() {
    return deletedByLogin;
  }

  public void setDeletedByLogin(String deletedByLogin) {
    this.deletedByLogin = deletedByLogin;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentFileWebDto shipmentFileWebDto = (ShipmentFileWebDto) o;
    return Objects.equals(this.fileId, shipmentFileWebDto.fileId) &&
        Objects.equals(this.filename, shipmentFileWebDto.filename) &&
        Objects.equals(this.addedDateTime, shipmentFileWebDto.addedDateTime) &&
        Objects.equals(this.addedByFullName, shipmentFileWebDto.addedByFullName) &&
        Objects.equals(this.addedByLogin, shipmentFileWebDto.addedByLogin) &&
        Objects.equals(this.deletedDateTime, shipmentFileWebDto.deletedDateTime) &&
        Objects.equals(this.deletedByFullName, shipmentFileWebDto.deletedByFullName) &&
        Objects.equals(this.deletedByLogin, shipmentFileWebDto.deletedByLogin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId, filename, addedDateTime, addedByFullName, addedByLogin, deletedDateTime, deletedByFullName, deletedByLogin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentFileWebDto {\n");
    
    sb.append("    fileId: ").append(toIndentedString(fileId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    addedDateTime: ").append(toIndentedString(addedDateTime)).append("\n");
    sb.append("    addedByFullName: ").append(toIndentedString(addedByFullName)).append("\n");
    sb.append("    addedByLogin: ").append(toIndentedString(addedByLogin)).append("\n");
    sb.append("    deletedDateTime: ").append(toIndentedString(deletedDateTime)).append("\n");
    sb.append("    deletedByFullName: ").append(toIndentedString(deletedByFullName)).append("\n");
    sb.append("    deletedByLogin: ").append(toIndentedString(deletedByLogin)).append("\n");
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

