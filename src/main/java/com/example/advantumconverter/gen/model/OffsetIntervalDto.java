package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import com.example.advantumconverter.gen.model.OffsetDateTimeWithOffsetDto;




/**
 * OffsetIntervalDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class OffsetIntervalDto   {
  @JsonProperty("finishTime")
  private OffsetDateTimeWithOffsetDto finishTime = null;

  @JsonProperty("startTime")
  private OffsetDateTimeWithOffsetDto startTime = null;

  public OffsetIntervalDto finishTime(OffsetDateTimeWithOffsetDto finishTime) {
    this.finishTime = finishTime;
    return this;
  }

  /**
   * Get finishTime
   * @return finishTime
  **/
  

//  @Valid

  public OffsetDateTimeWithOffsetDto getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(OffsetDateTimeWithOffsetDto finishTime) {
    this.finishTime = finishTime;
  }

  public OffsetIntervalDto startTime(OffsetDateTimeWithOffsetDto startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
  **/
  

//  @Valid

  public OffsetDateTimeWithOffsetDto getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTimeWithOffsetDto startTime) {
    this.startTime = startTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OffsetIntervalDto offsetIntervalDto = (OffsetIntervalDto) o;
    return Objects.equals(this.finishTime, offsetIntervalDto.finishTime) &&
        Objects.equals(this.startTime, offsetIntervalDto.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(finishTime, startTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OffsetIntervalDto {\n");
    
    sb.append("    finishTime: ").append(toIndentedString(finishTime)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
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

