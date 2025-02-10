package com.example.advantumconverter.gen.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;


import com.example.advantumconverter.gen.model.ExternalPointDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;




/**
 * RoutePointDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class RoutePointDto   {
  @JsonProperty("point")
  private ExternalPointDto point = null;


  @JsonProperty("startTime")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private LocalDateTime startTime = null;

  @JsonProperty("finishTime")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private LocalDateTime finishTime = null;

  /**
   * Тип операции в точке
   */
  public enum OperationEnum {
    LOAD("LOAD"),
    
    UNLOAD("UNLOAD"),
    
    LOAD_AND_UNLOAD("LOAD_AND_UNLOAD"),
    
    VISIT("VISIT"),
    
    REFUELLING("REFUELLING");

    private String value;

    OperationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    //@JsonCreator
    public static OperationEnum fromValue(String text) {
      for (OperationEnum b : OperationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("operation")
  private OperationEnum operation = null;

  @JsonProperty("sequenceNumber")
  private Integer sequenceNumber = null;

  @JsonProperty("orderList")
//  @Valid
  private List<String> orderList = null;

  public RoutePointDto point(ExternalPointDto point) {
    this.point = point;
    return this;
  }

  /**
   * Точка маршрута рейса
   * @return point
  **/
  //@ApiModelProperty(required = true, value = "Точка маршрута рейса")
  //@NotNull

//  @Valid

  public ExternalPointDto getPoint() {
    return point;
  }

  public void setPoint(ExternalPointDto point) {
    this.point = point;
  }

  public RoutePointDto startTime(LocalDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Плановые дата и время подачи ТС на погрузку/разгрузку (UTC)
   * @return startTime
  **/
  //@ApiModelProperty(required = true, value = "Плановые дата и время подачи ТС на погрузку/разгрузку (UTC)")
  //@NotNull

//  @Valid

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public RoutePointDto finishTime(LocalDateTime finishTime) {
    this.finishTime = finishTime;
    return this;
  }

  /**
   * Плановые дата и время убытия из точки маршрута (UTC)
   * @return finishTime
  **/
  //@ApiModelProperty(value = "Плановые дата и время убытия из точки маршрута (UTC)")

//  @Valid

  public LocalDateTime getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(LocalDateTime finishTime) {
    this.finishTime = finishTime;
  }

  public RoutePointDto operation(OperationEnum operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Тип операции в точке
   * @return operation
  **/
  //@ApiModelProperty(required = true, value = "Тип операции в точке")
  //@NotNull


  public OperationEnum getOperation() {
    return operation;
  }

  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }

  public RoutePointDto sequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Порядковый номер в точки рейсе 
   * @return sequenceNumber
  **/
  //@ApiModelProperty(value = "Порядковый номер в точки рейсе ")


  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public RoutePointDto orderList(List<String> orderList) {
    this.orderList = orderList;
    return this;
  }

  public RoutePointDto addOrderListItem(String orderListItem) {
    if (this.orderList == null) {
      this.orderList = new ArrayList<String>();
    }
    this.orderList.add(orderListItem);
    return this;
  }

  /**
   * Список номеров заказов
   * @return orderList
  **/
  //@ApiModelProperty(value = "Список номеров заказов")


  public List<String> getOrderList() {
    return orderList;
  }

  public void setOrderList(List<String> orderList) {
    this.orderList = orderList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoutePointDto routePointDto = (RoutePointDto) o;
    return Objects.equals(this.point, routePointDto.point) &&
        Objects.equals(this.startTime, routePointDto.startTime) &&
        Objects.equals(this.finishTime, routePointDto.finishTime) &&
        Objects.equals(this.operation, routePointDto.operation) &&
        Objects.equals(this.sequenceNumber, routePointDto.sequenceNumber) &&
        Objects.equals(this.orderList, routePointDto.orderList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(point, startTime, finishTime, operation, sequenceNumber, orderList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoutePointDto {\n");
    
    sb.append("    point: ").append(toIndentedString(point)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    finishTime: ").append(toIndentedString(finishTime)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    orderList: ").append(toIndentedString(orderList)).append("\n");
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

