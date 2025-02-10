package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.util.UUID;




/**
 * DriverDocumentFileDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class DriverDocumentFileDto   {
  @JsonProperty("fileName")
  private String fileName = null;

  @JsonProperty("fileType")
  private String fileType = null;

  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("path")
  private String path = null;

  public DriverDocumentFileDto fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * Get fileName
   * @return fileName
  **/
  


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public DriverDocumentFileDto fileType(String fileType) {
    this.fileType = fileType;
    return this;
  }

  /**
   * Get fileType
   * @return fileType
  **/
  


  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public DriverDocumentFileDto id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  //@ApiModelProperty(required = true, value = "")
  //@NotNull

//  @Valid

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public DriverDocumentFileDto path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
  **/
  


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DriverDocumentFileDto driverDocumentFileDto = (DriverDocumentFileDto) o;
    return Objects.equals(this.fileName, driverDocumentFileDto.fileName) &&
        Objects.equals(this.fileType, driverDocumentFileDto.fileType) &&
        Objects.equals(this.id, driverDocumentFileDto.id) &&
        Objects.equals(this.path, driverDocumentFileDto.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileName, fileType, id, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DriverDocumentFileDto {\n");
    
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    fileType: ").append(toIndentedString(fileType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
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

