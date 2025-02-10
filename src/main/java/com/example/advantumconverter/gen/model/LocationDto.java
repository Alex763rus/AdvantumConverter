package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import com.example.advantumconverter.gen.model.GeoPointDto;




/**
 * LocationDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class LocationDto   {
  @JsonProperty("address")
  private String address = null;

  @JsonProperty("locationName")
  private String locationName = null;

  @JsonProperty("point")
  private GeoPointDto point = null;

  public LocationDto address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
  **/
  


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocationDto locationName(String locationName) {
    this.locationName = locationName;
    return this;
  }

  /**
   * Get locationName
   * @return locationName
  **/
  


  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public LocationDto point(GeoPointDto point) {
    this.point = point;
    return this;
  }

  /**
   * Get point
   * @return point
  **/
  

//  @Valid

  public GeoPointDto getPoint() {
    return point;
  }

  public void setPoint(GeoPointDto point) {
    this.point = point;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDto locationDto = (LocationDto) o;
    return Objects.equals(this.address, locationDto.address) &&
        Objects.equals(this.locationName, locationDto.locationName) &&
        Objects.equals(this.point, locationDto.point);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, locationName, point);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDto {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    locationName: ").append(toIndentedString(locationName)).append("\n");
    sb.append("    point: ").append(toIndentedString(point)).append("\n");
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

