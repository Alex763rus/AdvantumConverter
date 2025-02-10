package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.time.OffsetDateTime;




/**
 * OffsetDateTimeWithOffsetDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class OffsetDateTimeWithOffsetDto   {
  @JsonProperty("offset")
  private String offset = null;

  @JsonProperty("time")
  private OffsetDateTime time = null;

  public OffsetDateTimeWithOffsetDto offset(String offset) {
    this.offset = offset;
    return this;
  }

  /**
   * Get offset
   * @return offset
  **/
  


  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public OffsetDateTimeWithOffsetDto time(OffsetDateTime time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
  **/
  

//  @Valid

  public OffsetDateTime getTime() {
    return time;
  }

  public void setTime(OffsetDateTime time) {
    this.time = time;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OffsetDateTimeWithOffsetDto offsetDateTimeWithOffsetDto = (OffsetDateTimeWithOffsetDto) o;
    return Objects.equals(this.offset, offsetDateTimeWithOffsetDto.offset) &&
        Objects.equals(this.time, offsetDateTimeWithOffsetDto.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offset, time);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OffsetDateTimeWithOffsetDto {\n");
    
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
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

