package com.example.advantumconverter.gen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * RouteWithDictionaryDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class RouteWithDictionaryDto {
    @JsonProperty("baseCostInRubles")
    private Double baseCostInRubles = null;

    @JsonProperty("bodyTonnageInKg")
    private Float bodyTonnageInKg = null;

    @JsonProperty("bodyVolumeInCubicMeters")
    private Float bodyVolumeInCubicMeters = null;

    @JsonProperty("completeOnLastPoint")
    private Boolean completeOnLastPoint = null;

    @JsonProperty("distanceInMeters")
    private Double distanceInMeters = null;

    @JsonProperty("numberOfPallets")
    private Integer numberOfPallets = null;

    @JsonProperty("qrCode")
    private String qrCode = null;

    @JsonProperty("routingListNumber")
    private Integer routingListNumber = null;

    @JsonProperty("routingService")
    private Boolean routingService = null;

    @JsonProperty("secondRoute")
    private Boolean secondRoute = null;

    @JsonProperty("zoneNum")
    private Integer zoneNum = null;

    @JsonProperty("externalId")
    private String externalId = null;

    @JsonProperty("routeNum")
    private String routeNum = null;

    @JsonProperty("customer")
    private ExternalOrganizationDto customer = null;

    @JsonProperty("carrier")
    private ExternalOrganizationDto carrier = null;

    @JsonProperty("costInRubles")
    private BigDecimal costInRubles = null;

    @JsonProperty("factCostInRubles")
    private BigDecimal factCostInRubles = null;

    @JsonProperty("comment")
    private String comment = null;

    @JsonProperty("temperatureMin")
    private BigDecimal temperatureMin = null;

    @JsonProperty("temperatureMax")
    private BigDecimal temperatureMax = null;

    @JsonProperty("secondChamberTemperatureMin")
    private BigDecimal secondChamberTemperatureMin = null;

    @JsonProperty("secondChamberTemperatureMax")
    private BigDecimal secondChamberTemperatureMax = null;

    @JsonProperty("tech")
    private Boolean tech = null;

    @JsonProperty("vehicle")
    private ExternalVehicleDto vehicle = null;

    @JsonProperty("trailer")
    private ExternalVehicleDto trailer = null;

    @JsonProperty("driver")
    private ExternalDriverDto driver = null;

    @JsonProperty("points")
//  @Valid
    private List<RoutePointDto> points = new ArrayList<RoutePointDto>();

    public RouteWithDictionaryDto baseCostInRubles(Double baseCostInRubles) {
        this.baseCostInRubles = baseCostInRubles;
        return this;
    }

    /**
     * Базовая стоимость перевозки согласно тарифам
     *
     * @return baseCostInRubles
     **/
    //@ApiModelProperty(value = "Базовая стоимость перевозки согласно тарифам")
    public Double getBaseCostInRubles() {
        return baseCostInRubles;
    }

    public void setBaseCostInRubles(Double baseCostInRubles) {
        this.baseCostInRubles = baseCostInRubles;
    }

    public RouteWithDictionaryDto bodyTonnageInKg(Float bodyTonnageInKg) {
        this.bodyTonnageInKg = bodyTonnageInKg;
        return this;
    }

    /**
     * Get bodyTonnageInKg
     *
     * @return bodyTonnageInKg
     **/


    public Float getBodyTonnageInKg() {
        return bodyTonnageInKg;
    }

    public void setBodyTonnageInKg(Float bodyTonnageInKg) {
        this.bodyTonnageInKg = bodyTonnageInKg;
    }

    public RouteWithDictionaryDto bodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
        this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
        return this;
    }

    /**
     * Get bodyVolumeInCubicMeters
     *
     * @return bodyVolumeInCubicMeters
     **/


    public Float getBodyVolumeInCubicMeters() {
        return bodyVolumeInCubicMeters;
    }

    public void setBodyVolumeInCubicMeters(Float bodyVolumeInCubicMeters) {
        this.bodyVolumeInCubicMeters = bodyVolumeInCubicMeters;
    }

    public RouteWithDictionaryDto completeOnLastPoint(Boolean completeOnLastPoint) {
        this.completeOnLastPoint = completeOnLastPoint;
        return this;
    }

    /**
     * Завершить рейс по въезду в последнюю точку
     *
     * @return completeOnLastPoint
     **/
    //@ApiModelProperty(example = "false", value = "Завершить рейс по въезду в последнюю точку")
    public Boolean isCompleteOnLastPoint() {
        return completeOnLastPoint;
    }

    public void setCompleteOnLastPoint(Boolean completeOnLastPoint) {
        this.completeOnLastPoint = completeOnLastPoint;
    }

    public RouteWithDictionaryDto distanceInMeters(Double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
        return this;
    }

    /**
     * Дистанция в метрах
     *
     * @return distanceInMeters
     **/
    //@ApiModelProperty(value = "Дистанция в метрах")
    public Double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(Double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public RouteWithDictionaryDto numberOfPallets(Integer numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
        return this;
    }

    /**
     * Get numberOfPallets
     *
     * @return numberOfPallets
     **/


    public Integer getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(Integer numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public RouteWithDictionaryDto qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    /**
     * QR код рейса
     *
     * @return qrCode
     **/
    //@ApiModelProperty(value = "QR код рейса")
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public RouteWithDictionaryDto routingListNumber(Integer routingListNumber) {
        this.routingListNumber = routingListNumber;
        return this;
    }

    /**
     * Номер маршрутного листа
     *
     * @return routingListNumber
     **/
    //@ApiModelProperty(value = "Номер маршрутного листа")
    public Integer getRoutingListNumber() {
        return routingListNumber;
    }

    public void setRoutingListNumber(Integer routingListNumber) {
        this.routingListNumber = routingListNumber;
    }

    public RouteWithDictionaryDto routingService(Boolean routingService) {
        this.routingService = routingService;
        return this;
    }

    /**
     * Рейс получен из сервиса маршрутизации
     *
     * @return routingService
     **/
    //@ApiModelProperty(example = "false", value = "Рейс получен из сервиса маршрутизации")
    public Boolean isRoutingService() {
        return routingService;
    }

    public void setRoutingService(Boolean routingService) {
        this.routingService = routingService;
    }

    public RouteWithDictionaryDto secondRoute(Boolean secondRoute) {
        this.secondRoute = secondRoute;
        return this;
    }

    /**
     * Второй рейс
     *
     * @return secondRoute
     **/
    //@ApiModelProperty(value = "Второй рейс")
    public Boolean isSecondRoute() {
        return secondRoute;
    }

    public void setSecondRoute(Boolean secondRoute) {
        this.secondRoute = secondRoute;
    }

    public RouteWithDictionaryDto zoneNum(Integer zoneNum) {
        this.zoneNum = zoneNum;
        return this;
    }

    /**
     * Зона
     *
     * @return zoneNum
     **/
    //@ApiModelProperty(value = "Зона")
    public Integer getZoneNum() {
        return zoneNum;
    }

    public void setZoneNum(Integer zoneNum) {
        this.zoneNum = zoneNum;
    }

    public RouteWithDictionaryDto externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    /**
     * ИД рейса в ИС Предприятия
     *
     * @return externalId
     **/
    //@ApiModelProperty(required = true, value = "ИД рейса в ИС Предприятия")
    //@NotNull
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public RouteWithDictionaryDto routeNum(String routeNum) {
        this.routeNum = routeNum;
        return this;
    }

    /**
     * Номер рейса
     *
     * @return routeNum
     **/
    //@ApiModelProperty(value = "Номер рейса")
    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public RouteWithDictionaryDto customer(ExternalOrganizationDto customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Клиент (Заказчик)
     *
     * @return customer
     **/
    //@ApiModelProperty(value = "Клиент (Заказчик)")

//  @Valid
    public ExternalOrganizationDto getCustomer() {
        return customer;
    }

    public void setCustomer(ExternalOrganizationDto customer) {
        this.customer = customer;
    }

    public RouteWithDictionaryDto carrier(ExternalOrganizationDto carrier) {
        this.carrier = carrier;
        return this;
    }

    /**
     * Перевозчик
     *
     * @return carrier
     **/
    //@ApiModelProperty(required = true, value = "Перевозчик")
    //@NotNull

//  @Valid
    public ExternalOrganizationDto getCarrier() {
        return carrier;
    }

    public void setCarrier(ExternalOrganizationDto carrier) {
        this.carrier = carrier;
    }

    public RouteWithDictionaryDto costInRubles(BigDecimal costInRubles) {
        this.costInRubles = costInRubles;
        return this;
    }

    /**
     * Стоимость выполнения рейса
     *
     * @return costInRubles
     **/
    //@ApiModelProperty(value = "Стоимость выполнения рейса")

//  @Valid
    public BigDecimal getCostInRubles() {
        return costInRubles;
    }

    public void setCostInRubles(BigDecimal costInRubles) {
        this.costInRubles = costInRubles;
    }

    public RouteWithDictionaryDto factCostInRubles(BigDecimal factCostInRubles) {
        this.factCostInRubles = factCostInRubles;
        return this;
    }

    /**
     * Фактическая Стоимость выполнения рейса
     *
     * @return factCostInRubles
     **/
    //@ApiModelProperty(value = "Фактическая Стоимость выполнения рейса")

//  @Valid
    public BigDecimal getFactCostInRubles() {
        return factCostInRubles;
    }

    public void setFactCostInRubles(BigDecimal factCostInRubles) {
        this.factCostInRubles = factCostInRubles;
    }

    public RouteWithDictionaryDto comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Комментарий к рейсу
     *
     * @return comment
     **/
    //@ApiModelProperty(value = "Комментарий к рейсу")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RouteWithDictionaryDto temperatureMin(BigDecimal temperatureMin) {
        this.temperatureMin = temperatureMin;
        return this;
    }

    /**
     * Минимальная температура перевозки в 1-м отсеке
     *
     * @return temperatureMin
     **/
    //@ApiModelProperty(value = "Минимальная температура перевозки в 1-м отсеке")

//  @Valid
    public BigDecimal getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(BigDecimal temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public RouteWithDictionaryDto temperatureMax(BigDecimal temperatureMax) {
        this.temperatureMax = temperatureMax;
        return this;
    }

    /**
     * Максимальная температура перевозки в 1-м отсеке
     *
     * @return temperatureMax
     **/
    //@ApiModelProperty(value = "Максимальная температура перевозки в 1-м отсеке")

//  @Valid
    public BigDecimal getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(BigDecimal temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public RouteWithDictionaryDto secondChamberTemperatureMin(BigDecimal secondChamberTemperatureMin) {
        this.secondChamberTemperatureMin = secondChamberTemperatureMin;
        return this;
    }

    /**
     * Минимальная температура перевозки во 2-м отсеке
     *
     * @return secondChamberTemperatureMin
     **/
    //@ApiModelProperty(value = "Минимальная температура перевозки во 2-м отсеке")

//  @Valid
    public BigDecimal getSecondChamberTemperatureMin() {
        return secondChamberTemperatureMin;
    }

    public void setSecondChamberTemperatureMin(BigDecimal secondChamberTemperatureMin) {
        this.secondChamberTemperatureMin = secondChamberTemperatureMin;
    }

    public RouteWithDictionaryDto secondChamberTemperatureMax(BigDecimal secondChamberTemperatureMax) {
        this.secondChamberTemperatureMax = secondChamberTemperatureMax;
        return this;
    }

    /**
     * Максимальная температура перевозки во 2-м отсеке
     *
     * @return secondChamberTemperatureMax
     **/
    //@ApiModelProperty(value = "Максимальная температура перевозки во 2-м отсеке")

//  @Valid
    public BigDecimal getSecondChamberTemperatureMax() {
        return secondChamberTemperatureMax;
    }

    public void setSecondChamberTemperatureMax(BigDecimal secondChamberTemperatureMax) {
        this.secondChamberTemperatureMax = secondChamberTemperatureMax;
    }

    public RouteWithDictionaryDto tech(Boolean tech) {
        this.tech = tech;
        return this;
    }

    /**
     * Признак технологического рейса
     *
     * @return tech
     **/
    //@ApiModelProperty(required = true, value = "Признак технологического рейса")
    //@NotNull
    public Boolean isTech() {
        return tech;
    }

    public void setTech(Boolean tech) {
        this.tech = tech;
    }

    public RouteWithDictionaryDto vehicle(ExternalVehicleDto vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    /**
     * Транспортное средство (единичное/головное ТС)
     *
     * @return vehicle
     **/
    //@ApiModelProperty(required = true, value = "Транспортное средство (единичное/головное ТС)")
    //@NotNull

//  @Valid
    public ExternalVehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(ExternalVehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public RouteWithDictionaryDto trailer(ExternalVehicleDto trailer) {
        this.trailer = trailer;
        return this;
    }

    /**
     * Прицеп
     *
     * @return trailer
     **/
    //@ApiModelProperty(value = "Прицеп")

//  @Valid
    public ExternalVehicleDto getTrailer() {
        return trailer;
    }

    public void setTrailer(ExternalVehicleDto trailer) {
        this.trailer = trailer;
    }

    public RouteWithDictionaryDto driver(ExternalDriverDto driver) {
        this.driver = driver;
        return this;
    }

    /**
     * Водитель
     *
     * @return driver
     **/
    //@ApiModelProperty(value = "Водитель")

//  @Valid
    public ExternalDriverDto getDriver() {
        return driver;
    }

    public void setDriver(ExternalDriverDto driver) {
        this.driver = driver;
    }

    public RouteWithDictionaryDto points(List<RoutePointDto> points) {
        this.points = points;
        return this;
    }

    public RouteWithDictionaryDto addPointsItem(RoutePointDto pointsItem) {
        this.points.add(pointsItem);
        return this;
    }

    /**
     * Маршрут рейса
     *
     * @return points
     **/
    //@ApiModelProperty(required = true, value = "Маршрут рейса")
    //@NotNull

//  @Valid
    public List<RoutePointDto> getPoints() {
        return points;
    }

    public void setPoints(List<RoutePointDto> points) {
        this.points = points;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteWithDictionaryDto routeWithDictionaryDto = (RouteWithDictionaryDto) o;
        return Objects.equals(this.baseCostInRubles, routeWithDictionaryDto.baseCostInRubles) &&
                Objects.equals(this.bodyTonnageInKg, routeWithDictionaryDto.bodyTonnageInKg) &&
                Objects.equals(this.bodyVolumeInCubicMeters, routeWithDictionaryDto.bodyVolumeInCubicMeters) &&
                Objects.equals(this.completeOnLastPoint, routeWithDictionaryDto.completeOnLastPoint) &&
                Objects.equals(this.distanceInMeters, routeWithDictionaryDto.distanceInMeters) &&
                Objects.equals(this.numberOfPallets, routeWithDictionaryDto.numberOfPallets) &&
                Objects.equals(this.qrCode, routeWithDictionaryDto.qrCode) &&
                Objects.equals(this.routingListNumber, routeWithDictionaryDto.routingListNumber) &&
                Objects.equals(this.routingService, routeWithDictionaryDto.routingService) &&
                Objects.equals(this.secondRoute, routeWithDictionaryDto.secondRoute) &&
                Objects.equals(this.zoneNum, routeWithDictionaryDto.zoneNum) &&
                Objects.equals(this.externalId, routeWithDictionaryDto.externalId) &&
                Objects.equals(this.routeNum, routeWithDictionaryDto.routeNum) &&
                Objects.equals(this.customer, routeWithDictionaryDto.customer) &&
                Objects.equals(this.carrier, routeWithDictionaryDto.carrier) &&
                Objects.equals(this.costInRubles, routeWithDictionaryDto.costInRubles) &&
                Objects.equals(this.factCostInRubles, routeWithDictionaryDto.factCostInRubles) &&
                Objects.equals(this.comment, routeWithDictionaryDto.comment) &&
                Objects.equals(this.temperatureMin, routeWithDictionaryDto.temperatureMin) &&
                Objects.equals(this.temperatureMax, routeWithDictionaryDto.temperatureMax) &&
                Objects.equals(this.secondChamberTemperatureMin, routeWithDictionaryDto.secondChamberTemperatureMin) &&
                Objects.equals(this.secondChamberTemperatureMax, routeWithDictionaryDto.secondChamberTemperatureMax) &&
                Objects.equals(this.tech, routeWithDictionaryDto.tech) &&
                Objects.equals(this.vehicle, routeWithDictionaryDto.vehicle) &&
                Objects.equals(this.trailer, routeWithDictionaryDto.trailer) &&
                Objects.equals(this.driver, routeWithDictionaryDto.driver) &&
                Objects.equals(this.points, routeWithDictionaryDto.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCostInRubles, bodyTonnageInKg, bodyVolumeInCubicMeters, completeOnLastPoint, distanceInMeters, numberOfPallets, qrCode, routingListNumber, routingService, secondRoute, zoneNum, externalId, routeNum, customer, carrier, costInRubles, factCostInRubles, comment, temperatureMin, temperatureMax, secondChamberTemperatureMin, secondChamberTemperatureMax, tech, vehicle, trailer, driver, points);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RouteWithDictionaryDto {\n");

        sb.append("    baseCostInRubles: ").append(toIndentedString(baseCostInRubles)).append("\n");
        sb.append("    bodyTonnageInKg: ").append(toIndentedString(bodyTonnageInKg)).append("\n");
        sb.append("    bodyVolumeInCubicMeters: ").append(toIndentedString(bodyVolumeInCubicMeters)).append("\n");
        sb.append("    completeOnLastPoint: ").append(toIndentedString(completeOnLastPoint)).append("\n");
        sb.append("    distanceInMeters: ").append(toIndentedString(distanceInMeters)).append("\n");
        sb.append("    numberOfPallets: ").append(toIndentedString(numberOfPallets)).append("\n");
        sb.append("    qrCode: ").append(toIndentedString(qrCode)).append("\n");
        sb.append("    routingListNumber: ").append(toIndentedString(routingListNumber)).append("\n");
        sb.append("    routingService: ").append(toIndentedString(routingService)).append("\n");
        sb.append("    secondRoute: ").append(toIndentedString(secondRoute)).append("\n");
        sb.append("    zoneNum: ").append(toIndentedString(zoneNum)).append("\n");
        sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
        sb.append("    routeNum: ").append(toIndentedString(routeNum)).append("\n");
        sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
        sb.append("    carrier: ").append(toIndentedString(carrier)).append("\n");
        sb.append("    costInRubles: ").append(toIndentedString(costInRubles)).append("\n");
        sb.append("    factCostInRubles: ").append(toIndentedString(factCostInRubles)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    temperatureMin: ").append(toIndentedString(temperatureMin)).append("\n");
        sb.append("    temperatureMax: ").append(toIndentedString(temperatureMax)).append("\n");
        sb.append("    secondChamberTemperatureMin: ").append(toIndentedString(secondChamberTemperatureMin)).append("\n");
        sb.append("    secondChamberTemperatureMax: ").append(toIndentedString(secondChamberTemperatureMax)).append("\n");
        sb.append("    tech: ").append(toIndentedString(tech)).append("\n");
        sb.append("    vehicle: ").append(toIndentedString(vehicle)).append("\n");
        sb.append("    trailer: ").append(toIndentedString(trailer)).append("\n");
        sb.append("    driver: ").append(toIndentedString(driver)).append("\n");
        sb.append("    points: ").append(toIndentedString(points)).append("\n");
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

