package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import com.example.advantumconverter.gen.model.ExternalBrandDto;
import com.example.advantumconverter.gen.model.ExternalModelDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ExternalVehicleDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ExternalVehicleDto   {
  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("gosNumber")
  private String gosNumber = null;

  @JsonProperty("brand")
  private ExternalBrandDto brand = null;

  @JsonProperty("model")
  private ExternalModelDto model = null;

  @JsonProperty("bodyVolumeInCubicMeters")
  private Float bodyVolumeInCubicMeters = null;

  @JsonProperty("tonnageFactInKg")
  private Float tonnageFactInKg = null;

  @JsonProperty("numberOfPallet")
  private Integer numberOfPallet = null;

  public ExternalVehicleDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * ИД ТС в ИС Предприятия
   * @return externalId
  **/
  //@ApiModelProperty(required = true, value = "ИД ТС в ИС Предприятия")
  //@NotNull


  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public ExternalVehicleDto gosNumber(String gosNumber) {
    this.gosNumber = gosNumber;
    return this;
  }

  /**
   * Гос. номер
   * @return gosNumber
  **/
  //@ApiModelProperty(required = true, value = "Гос. номер")
  //@NotNull


  public String getGosNumber() {
    return gosNumber;
  }

  public void setGosNumber(String gosNumber) {
    this.gosNumber = gosNumber;
  }

  public ExternalVehicleDto brand(ExternalBrandDto brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Марка ТС
   * @return brand
  **/
  //@ApiModelProperty(value = "Марка ТС")

//  @Valid

  public ExternalBrandDto getBrand() {
    return brand;
  }

  public void setBrand(ExternalBrandDto brand) {
    this.brand = brand;
  }

  public ExternalVehicleDto model(ExternalModelDto model) {
    this.model = model;
    return this;
  }

  /**
   * Модель ТС
   * @return model
  **/
  //@ApiModelProperty(value = "Модель ТС")

//  @Valid

  public ExternalModelDto getModel() {
    return model;
  }

  public void setModel(ExternalModelDto model) {
    this.model = model;
  }

  public ExternalVehicleDto bodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
    this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
    return this;
  }

  /**
   * Объём кузова в м3
   * @return bodyVolumeInCubicMeters
  **/
  //@ApiModelProperty(value = "Объём кузова в м3")


  public Float getBodyVolumeInCubicMeters() {
    return bodyVolumeInCubicMeters;
  }

  public void setBodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
    this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
  }

  public ExternalVehicleDto tonnageFactInKg(Float tonnageFactInKg) {
    this.tonnageFactInKg = tonnageFactInKg;
    return this;
  }

  /**
   * Фактическая грузоподъёмность в кг
   * @return tonnageFactInKg
  **/
  //@ApiModelProperty(value = "Фактическая грузоподъёмность в кг")


  public Float getTonnageFactInKg() {
    return tonnageFactInKg;
  }

  public void setTonnageFactInKg(Float tonnageFactInKg) {
    this.tonnageFactInKg = tonnageFactInKg;
  }

  public ExternalVehicleDto numberOfPallet(Integer numberOfPallet) {
    this.numberOfPallet = numberOfPallet;
    return this;
  }

  /**
   * Вместимость кузова в штуках
   * @return numberOfPallet
  **/
  //@ApiModelProperty(value = "Вместимость кузова в штуках")


  public Integer getNumberOfPallet() {
    return numberOfPallet;
  }

  public void setNumberOfPallet(Integer numberOfPallet) {
    this.numberOfPallet = numberOfPallet;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalVehicleDto externalVehicleDto = (ExternalVehicleDto) o;
    return Objects.equals(this.externalId, externalVehicleDto.externalId) &&
        Objects.equals(this.gosNumber, externalVehicleDto.gosNumber) &&
        Objects.equals(this.brand, externalVehicleDto.brand) &&
        Objects.equals(this.model, externalVehicleDto.model) &&
        Objects.equals(this.bodyVolumeInCubicMeters, externalVehicleDto.bodyVolumeInCubicMeters) &&
        Objects.equals(this.tonnageFactInKg, externalVehicleDto.tonnageFactInKg) &&
        Objects.equals(this.numberOfPallet, externalVehicleDto.numberOfPallet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalId, gosNumber, brand, model, bodyVolumeInCubicMeters, tonnageFactInKg, numberOfPallet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalVehicleDto {\n");
    
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    gosNumber: ").append(toIndentedString(gosNumber)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    bodyVolumeInCubicMeters: ").append(toIndentedString(bodyVolumeInCubicMeters)).append("\n");
    sb.append("    tonnageFactInKg: ").append(toIndentedString(tonnageFactInKg)).append("\n");
    sb.append("    numberOfPallet: ").append(toIndentedString(numberOfPallet)).append("\n");
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

