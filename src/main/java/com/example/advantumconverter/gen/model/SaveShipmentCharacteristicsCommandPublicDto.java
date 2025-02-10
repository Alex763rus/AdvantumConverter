package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import java.math.BigDecimal;




/**
 * SaveShipmentCharacteristicsCommandPublicDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class SaveShipmentCharacteristicsCommandPublicDto   {
  @JsonProperty("acceptableTemperatureDeviation")
  private Float acceptableTemperatureDeviation = null;

  @JsonProperty("bodyTonnageInKg")
  private Float bodyTonnageInKg = null;

  /**
   * Gets or Sets bodyType
   */
  public enum BodyTypeEnum {
    AWNING("AWNING"),
    
    REF("REF"),
    
    ISO("ISO"),
    
    BT("BT"),
    
    TRANSPORTER("TRANSPORTER"),
    
    ONBOARD("ONBOARD"),
    
    CONTAINER("CONTAINER"),
    
    PLATFORM("PLATFORM"),
    
    MANUFACTURE("MANUFACTURE"),
    
    DUMP("DUMP"),
    
    TRAWL("TRAWL"),
    
    ALL_METAL("ALL_METAL"),
    
    CISTERN("CISTERN"),
    
    SEDAN("SEDAN"),
    
    MINIVAN("MINIVAN"),
    
    BUS("BUS"),
    
    ISOTERM("ISOTERM"),
    
    REF_MULTI("REF_MULTI"),
    
    AWNING_CMR("AWNING_CMR"),
    
    ONBOARD_CMR("ONBOARD_CMR"),
    
    CEMENT("CEMENT");

    private String value;

    BodyTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static BodyTypeEnum fromValue(String text) {
      for (BodyTypeEnum b : BodyTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("bodyType")
  private BodyTypeEnum bodyType = null;

  @JsonProperty("bodyVolumeInCubicMeters")
  private Float bodyVolumeInCubicMeters = null;

  @JsonProperty("carrierDivisionExternalId")
  private String carrierDivisionExternalId = null;

  @JsonProperty("carrierOrganizationExternalId")
  private String carrierOrganizationExternalId = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("completeOnLastPoint")
  private Boolean completeOnLastPoint = null;

  @JsonProperty("costInRubles")
  private BigDecimal costInRubles = null;

  @JsonProperty("numberOfPallets")
  private Integer numberOfPallets = null;

  @JsonProperty("qrCode")
  private String qrCode = null;

  @JsonProperty("routeNum")
  private String routeNum = null;

  @JsonProperty("secondChamberAcceptableTemperatureDeviation")
  private Float secondChamberAcceptableTemperatureDeviation = null;

  @JsonProperty("secondChamberTemperatureMax")
  private Float secondChamberTemperatureMax = null;

  @JsonProperty("secondChamberTemperatureMin")
  private Float secondChamberTemperatureMin = null;

  @JsonProperty("secondRoute")
  private Boolean secondRoute = null;

  @JsonProperty("speedModeTypeId")
  private Integer speedModeTypeId = null;

  @JsonProperty("temperatureMax")
  private Float temperatureMax = null;

  @JsonProperty("temperatureMin")
  private Float temperatureMin = null;

  @JsonProperty("trailerExternalId")
  private String trailerExternalId = null;

  @JsonProperty("zoneNum")
  private Integer zoneNum = null;

  public SaveShipmentCharacteristicsCommandPublicDto acceptableTemperatureDeviation(Float acceptableTemperatureDeviation) {
    this.acceptableTemperatureDeviation = acceptableTemperatureDeviation;
    return this;
  }

  /**
   * Get acceptableTemperatureDeviation
   * @return acceptableTemperatureDeviation
  **/
  


  public Float getAcceptableTemperatureDeviation() {
    return acceptableTemperatureDeviation;
  }

  public void setAcceptableTemperatureDeviation(Float acceptableTemperatureDeviation) {
    this.acceptableTemperatureDeviation = acceptableTemperatureDeviation;
  }

  public SaveShipmentCharacteristicsCommandPublicDto bodyTonnageInKg(Float bodyTonnageInKg) {
    this.bodyTonnageInKg = bodyTonnageInKg;
    return this;
  }

  /**
   * Get bodyTonnageInKg
   * @return bodyTonnageInKg
  **/
  


  public Float getBodyTonnageInKg() {
    return bodyTonnageInKg;
  }

  public void setBodyTonnageInKg(Float bodyTonnageInKg) {
    this.bodyTonnageInKg = bodyTonnageInKg;
  }

  public SaveShipmentCharacteristicsCommandPublicDto bodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  /**
   * Get bodyType
   * @return bodyType
  **/
  


  public BodyTypeEnum getBodyType() {
    return bodyType;
  }

  public void setBodyType(BodyTypeEnum bodyType) {
    this.bodyType = bodyType;
  }

  public SaveShipmentCharacteristicsCommandPublicDto bodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
    this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
    return this;
  }

  /**
   * Get bodyVolumeInCubicMeters
   * @return bodyVolumeInCubicMeters
  **/
  


  public Float getBodyVolumeInCubicMeters() {
    return bodyVolumeInCubicMeters;
  }

  public void setBodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
    this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
  }

  public SaveShipmentCharacteristicsCommandPublicDto carrierDivisionExternalId(String carrierDivisionExternalId) {
    this.carrierDivisionExternalId = carrierDivisionExternalId;
    return this;
  }

  /**
   * Get carrierDivisionExternalId
   * @return carrierDivisionExternalId
  **/
  


  public String getCarrierDivisionExternalId() {
    return carrierDivisionExternalId;
  }

  public void setCarrierDivisionExternalId(String carrierDivisionExternalId) {
    this.carrierDivisionExternalId = carrierDivisionExternalId;
  }

  public SaveShipmentCharacteristicsCommandPublicDto carrierOrganizationExternalId(String carrierOrganizationExternalId) {
    this.carrierOrganizationExternalId = carrierOrganizationExternalId;
    return this;
  }

  /**
   * Get carrierOrganizationExternalId
   * @return carrierOrganizationExternalId
  **/
  


  public String getCarrierOrganizationExternalId() {
    return carrierOrganizationExternalId;
  }

  public void setCarrierOrganizationExternalId(String carrierOrganizationExternalId) {
    this.carrierOrganizationExternalId = carrierOrganizationExternalId;
  }

  public SaveShipmentCharacteristicsCommandPublicDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
  **/
  


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public SaveShipmentCharacteristicsCommandPublicDto completeOnLastPoint(Boolean completeOnLastPoint) {
    this.completeOnLastPoint = completeOnLastPoint;
    return this;
  }

  /**
   * Get completeOnLastPoint
   * @return completeOnLastPoint
  **/
  


  public Boolean isCompleteOnLastPoint() {
    return completeOnLastPoint;
  }

  public void setCompleteOnLastPoint(Boolean completeOnLastPoint) {
    this.completeOnLastPoint = completeOnLastPoint;
  }

  public SaveShipmentCharacteristicsCommandPublicDto costInRubles(BigDecimal costInRubles) {
    this.costInRubles = costInRubles;
    return this;
  }

  /**
   * Get costInRubles
   * @return costInRubles
  **/
  

//  @Valid

  public BigDecimal getCostInRubles() {
    return costInRubles;
  }

  public void setCostInRubles(BigDecimal costInRubles) {
    this.costInRubles = costInRubles;
  }

  public SaveShipmentCharacteristicsCommandPublicDto numberOfPallets(Integer numberOfPallets) {
    this.numberOfPallets = numberOfPallets;
    return this;
  }

  /**
   * Get numberOfPallets
   * @return numberOfPallets
  **/
  


  public Integer getNumberOfPallets() {
    return numberOfPallets;
  }

  public void setNumberOfPallets(Integer numberOfPallets) {
    this.numberOfPallets = numberOfPallets;
  }

  public SaveShipmentCharacteristicsCommandPublicDto qrCode(String qrCode) {
    this.qrCode = qrCode;
    return this;
  }

  /**
   * Get qrCode
   * @return qrCode
  **/
  


  public String getQrCode() {
    return qrCode;
  }

  public void setQrCode(String qrCode) {
    this.qrCode = qrCode;
  }

  public SaveShipmentCharacteristicsCommandPublicDto routeNum(String routeNum) {
    this.routeNum = routeNum;
    return this;
  }

  /**
   * Get routeNum
   * @return routeNum
  **/
  


  public String getRouteNum() {
    return routeNum;
  }

  public void setRouteNum(String routeNum) {
    this.routeNum = routeNum;
  }

  public SaveShipmentCharacteristicsCommandPublicDto secondChamberAcceptableTemperatureDeviation(Float secondChamberAcceptableTemperatureDeviation) {
    this.secondChamberAcceptableTemperatureDeviation = secondChamberAcceptableTemperatureDeviation;
    return this;
  }

  /**
   * Get secondChamberAcceptableTemperatureDeviation
   * @return secondChamberAcceptableTemperatureDeviation
  **/
  


  public Float getSecondChamberAcceptableTemperatureDeviation() {
    return secondChamberAcceptableTemperatureDeviation;
  }

  public void setSecondChamberAcceptableTemperatureDeviation(Float secondChamberAcceptableTemperatureDeviation) {
    this.secondChamberAcceptableTemperatureDeviation = secondChamberAcceptableTemperatureDeviation;
  }

  public SaveShipmentCharacteristicsCommandPublicDto secondChamberTemperatureMax(Float secondChamberTemperatureMax) {
    this.secondChamberTemperatureMax = secondChamberTemperatureMax;
    return this;
  }

  /**
   * Get secondChamberTemperatureMax
   * @return secondChamberTemperatureMax
  **/
  


  public Float getSecondChamberTemperatureMax() {
    return secondChamberTemperatureMax;
  }

  public void setSecondChamberTemperatureMax(Float secondChamberTemperatureMax) {
    this.secondChamberTemperatureMax = secondChamberTemperatureMax;
  }

  public SaveShipmentCharacteristicsCommandPublicDto secondChamberTemperatureMin(Float secondChamberTemperatureMin) {
    this.secondChamberTemperatureMin = secondChamberTemperatureMin;
    return this;
  }

  /**
   * Get secondChamberTemperatureMin
   * @return secondChamberTemperatureMin
  **/
  


  public Float getSecondChamberTemperatureMin() {
    return secondChamberTemperatureMin;
  }

  public void setSecondChamberTemperatureMin(Float secondChamberTemperatureMin) {
    this.secondChamberTemperatureMin = secondChamberTemperatureMin;
  }

  public SaveShipmentCharacteristicsCommandPublicDto secondRoute(Boolean secondRoute) {
    this.secondRoute = secondRoute;
    return this;
  }

  /**
   * Get secondRoute
   * @return secondRoute
  **/
  


  public Boolean isSecondRoute() {
    return secondRoute;
  }

  public void setSecondRoute(Boolean secondRoute) {
    this.secondRoute = secondRoute;
  }

  public SaveShipmentCharacteristicsCommandPublicDto speedModeTypeId(Integer speedModeTypeId) {
    this.speedModeTypeId = speedModeTypeId;
    return this;
  }

  /**
   * Get speedModeTypeId
   * @return speedModeTypeId
  **/
  


  public Integer getSpeedModeTypeId() {
    return speedModeTypeId;
  }

  public void setSpeedModeTypeId(Integer speedModeTypeId) {
    this.speedModeTypeId = speedModeTypeId;
  }

  public SaveShipmentCharacteristicsCommandPublicDto temperatureMax(Float temperatureMax) {
    this.temperatureMax = temperatureMax;
    return this;
  }

  /**
   * Get temperatureMax
   * @return temperatureMax
  **/
  


  public Float getTemperatureMax() {
    return temperatureMax;
  }

  public void setTemperatureMax(Float temperatureMax) {
    this.temperatureMax = temperatureMax;
  }

  public SaveShipmentCharacteristicsCommandPublicDto temperatureMin(Float temperatureMin) {
    this.temperatureMin = temperatureMin;
    return this;
  }

  /**
   * Get temperatureMin
   * @return temperatureMin
  **/
  


  public Float getTemperatureMin() {
    return temperatureMin;
  }

  public void setTemperatureMin(Float temperatureMin) {
    this.temperatureMin = temperatureMin;
  }

  public SaveShipmentCharacteristicsCommandPublicDto trailerExternalId(String trailerExternalId) {
    this.trailerExternalId = trailerExternalId;
    return this;
  }

  /**
   * Get trailerExternalId
   * @return trailerExternalId
  **/
  


  public String getTrailerExternalId() {
    return trailerExternalId;
  }

  public void setTrailerExternalId(String trailerExternalId) {
    this.trailerExternalId = trailerExternalId;
  }

  public SaveShipmentCharacteristicsCommandPublicDto zoneNum(Integer zoneNum) {
    this.zoneNum = zoneNum;
    return this;
  }

  /**
   * Get zoneNum
   * @return zoneNum
  **/
  


  public Integer getZoneNum() {
    return zoneNum;
  }

  public void setZoneNum(Integer zoneNum) {
    this.zoneNum = zoneNum;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveShipmentCharacteristicsCommandPublicDto saveShipmentCharacteristicsCommandPublicDto = (SaveShipmentCharacteristicsCommandPublicDto) o;
    return Objects.equals(this.acceptableTemperatureDeviation, saveShipmentCharacteristicsCommandPublicDto.acceptableTemperatureDeviation) &&
        Objects.equals(this.bodyTonnageInKg, saveShipmentCharacteristicsCommandPublicDto.bodyTonnageInKg) &&
        Objects.equals(this.bodyType, saveShipmentCharacteristicsCommandPublicDto.bodyType) &&
        Objects.equals(this.bodyVolumeInCubicMeters, saveShipmentCharacteristicsCommandPublicDto.bodyVolumeInCubicMeters) &&
        Objects.equals(this.carrierDivisionExternalId, saveShipmentCharacteristicsCommandPublicDto.carrierDivisionExternalId) &&
        Objects.equals(this.carrierOrganizationExternalId, saveShipmentCharacteristicsCommandPublicDto.carrierOrganizationExternalId) &&
        Objects.equals(this.comment, saveShipmentCharacteristicsCommandPublicDto.comment) &&
        Objects.equals(this.completeOnLastPoint, saveShipmentCharacteristicsCommandPublicDto.completeOnLastPoint) &&
        Objects.equals(this.costInRubles, saveShipmentCharacteristicsCommandPublicDto.costInRubles) &&
        Objects.equals(this.numberOfPallets, saveShipmentCharacteristicsCommandPublicDto.numberOfPallets) &&
        Objects.equals(this.qrCode, saveShipmentCharacteristicsCommandPublicDto.qrCode) &&
        Objects.equals(this.routeNum, saveShipmentCharacteristicsCommandPublicDto.routeNum) &&
        Objects.equals(this.secondChamberAcceptableTemperatureDeviation, saveShipmentCharacteristicsCommandPublicDto.secondChamberAcceptableTemperatureDeviation) &&
        Objects.equals(this.secondChamberTemperatureMax, saveShipmentCharacteristicsCommandPublicDto.secondChamberTemperatureMax) &&
        Objects.equals(this.secondChamberTemperatureMin, saveShipmentCharacteristicsCommandPublicDto.secondChamberTemperatureMin) &&
        Objects.equals(this.secondRoute, saveShipmentCharacteristicsCommandPublicDto.secondRoute) &&
        Objects.equals(this.speedModeTypeId, saveShipmentCharacteristicsCommandPublicDto.speedModeTypeId) &&
        Objects.equals(this.temperatureMax, saveShipmentCharacteristicsCommandPublicDto.temperatureMax) &&
        Objects.equals(this.temperatureMin, saveShipmentCharacteristicsCommandPublicDto.temperatureMin) &&
        Objects.equals(this.trailerExternalId, saveShipmentCharacteristicsCommandPublicDto.trailerExternalId) &&
        Objects.equals(this.zoneNum, saveShipmentCharacteristicsCommandPublicDto.zoneNum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acceptableTemperatureDeviation, bodyTonnageInKg, bodyType, bodyVolumeInCubicMeters, carrierDivisionExternalId, carrierOrganizationExternalId, comment, completeOnLastPoint, costInRubles, numberOfPallets, qrCode, routeNum, secondChamberAcceptableTemperatureDeviation, secondChamberTemperatureMax, secondChamberTemperatureMin, secondRoute, speedModeTypeId, temperatureMax, temperatureMin, trailerExternalId, zoneNum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SaveShipmentCharacteristicsCommandPublicDto {\n");
    
    sb.append("    acceptableTemperatureDeviation: ").append(toIndentedString(acceptableTemperatureDeviation)).append("\n");
    sb.append("    bodyTonnageInKg: ").append(toIndentedString(bodyTonnageInKg)).append("\n");
    sb.append("    bodyType: ").append(toIndentedString(bodyType)).append("\n");
    sb.append("    bodyVolumeInCubicMeters: ").append(toIndentedString(bodyVolumeInCubicMeters)).append("\n");
    sb.append("    carrierDivisionExternalId: ").append(toIndentedString(carrierDivisionExternalId)).append("\n");
    sb.append("    carrierOrganizationExternalId: ").append(toIndentedString(carrierOrganizationExternalId)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    completeOnLastPoint: ").append(toIndentedString(completeOnLastPoint)).append("\n");
    sb.append("    costInRubles: ").append(toIndentedString(costInRubles)).append("\n");
    sb.append("    numberOfPallets: ").append(toIndentedString(numberOfPallets)).append("\n");
    sb.append("    qrCode: ").append(toIndentedString(qrCode)).append("\n");
    sb.append("    routeNum: ").append(toIndentedString(routeNum)).append("\n");
    sb.append("    secondChamberAcceptableTemperatureDeviation: ").append(toIndentedString(secondChamberAcceptableTemperatureDeviation)).append("\n");
    sb.append("    secondChamberTemperatureMax: ").append(toIndentedString(secondChamberTemperatureMax)).append("\n");
    sb.append("    secondChamberTemperatureMin: ").append(toIndentedString(secondChamberTemperatureMin)).append("\n");
    sb.append("    secondRoute: ").append(toIndentedString(secondRoute)).append("\n");
    sb.append("    speedModeTypeId: ").append(toIndentedString(speedModeTypeId)).append("\n");
    sb.append("    temperatureMax: ").append(toIndentedString(temperatureMax)).append("\n");
    sb.append("    temperatureMin: ").append(toIndentedString(temperatureMin)).append("\n");
    sb.append("    trailerExternalId: ").append(toIndentedString(trailerExternalId)).append("\n");
    sb.append("    zoneNum: ").append(toIndentedString(zoneNum)).append("\n");
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

