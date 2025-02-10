package com.example.advantumconverter.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.math.BigDecimal;
import java.time.OffsetDateTime;




/**
 * RnCardTransactionPublicDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class RnCardTransactionPublicDto   {
  @JsonProperty("address")
  private String address = null;

  @JsonProperty("dPrice")
  private BigDecimal dPrice = null;

  @JsonProperty("holderATMS")
  private String holderATMS = null;

  @JsonProperty("contract")
  private String contract = null;

  @JsonProperty("organization")
  private String organization = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("operationTime")
  private String operationTime = null;

  @JsonProperty("posCode")
  private String posCode = null;

  @JsonProperty("timezone")
  private String timezone = null;

  @JsonProperty("utcTime")
  private OffsetDateTime utcTime = null;

  @JsonProperty("card")
  private String card = null;

  @JsonProperty("operationType")
  private String operationType = null;

  @JsonProperty("value")
  private BigDecimal value = null;

  @JsonProperty("sum")
  private BigDecimal sum = null;

  @JsonProperty("dSum")
  private BigDecimal dSum = null;

  @JsonProperty("price")
  private BigDecimal price = null;

  @JsonProperty("gCode")
  private String gCode = null;

  @JsonProperty("gName")
  private String gName = null;

  @JsonProperty("holder")
  private String holder = null;

  @JsonProperty("dtl")
  private String dtl = null;

  @JsonProperty("ref")
  private String ref = null;

  @JsonProperty("gCat")
  private String gCat = null;

  @JsonProperty("createTime")
  private OffsetDateTime createTime = null;

  @JsonProperty("updateTime")
  private OffsetDateTime updateTime = null;

  public RnCardTransactionPublicDto address(String address) {
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

  public RnCardTransactionPublicDto dPrice(BigDecimal dPrice) {
    this.dPrice = dPrice;
    return this;
  }

  /**
   * Get dPrice
   * @return dPrice
  **/
  

//  @Valid

  public BigDecimal getDPrice() {
    return dPrice;
  }

  public void setDPrice(BigDecimal dPrice) {
    this.dPrice = dPrice;
  }

  public RnCardTransactionPublicDto holderATMS(String holderATMS) {
    this.holderATMS = holderATMS;
    return this;
  }

  /**
   * Гос. номер ТС, к которому привязана карта по данным АТМС
   * @return holderATMS
  **/
  //@ApiModelProperty(value = "Гос. номер ТС, к которому привязана карта по данным АТМС")


  public String getHolderATMS() {
    return holderATMS;
  }

  public void setHolderATMS(String holderATMS) {
    this.holderATMS = holderATMS;
  }

  public RnCardTransactionPublicDto contract(String contract) {
    this.contract = contract;
    return this;
  }

  /**
   * Договор
   * @return contract
  **/
  //@ApiModelProperty(value = "Договор")


  public String getContract() {
    return contract;
  }

  public void setContract(String contract) {
    this.contract = contract;
  }

  public RnCardTransactionPublicDto organization(String organization) {
    this.organization = organization;
    return this;
  }

  /**
   * Держатель договора
   * @return organization
  **/
  //@ApiModelProperty(value = "Держатель договора")


  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public RnCardTransactionPublicDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * ID операции
   * @return code
  **/
  //@ApiModelProperty(value = "ID операции")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public RnCardTransactionPublicDto operationTime(String operationTime) {
    this.operationTime = operationTime;
    return this;
  }

  /**
   * Дата операции
   * @return operationTime
  **/
  //@ApiModelProperty(value = "Дата операции")


  public String getOperationTime() {
    return operationTime;
  }

  public void setOperationTime(String operationTime) {
    this.operationTime = operationTime;
  }

  public RnCardTransactionPublicDto posCode(String posCode) {
    this.posCode = posCode;
    return this;
  }

  /**
   * Идентификатор АЗС
   * @return posCode
  **/
  //@ApiModelProperty(value = "Идентификатор АЗС")


  public String getPosCode() {
    return posCode;
  }

  public void setPosCode(String posCode) {
    this.posCode = posCode;
  }

  public RnCardTransactionPublicDto timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Часовой пояс АЗС
   * @return timezone
  **/
  //@ApiModelProperty(value = "Часовой пояс АЗС")


  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public RnCardTransactionPublicDto utcTime(OffsetDateTime utcTime) {
    this.utcTime = utcTime;
    return this;
  }

  /**
   * Дата операции UTC
   * @return utcTime
  **/
  //@ApiModelProperty(value = "Дата операции UTC")

//  @Valid

  public OffsetDateTime getUtcTime() {
    return utcTime;
  }

  public void setUtcTime(OffsetDateTime utcTime) {
    this.utcTime = utcTime;
  }

  public RnCardTransactionPublicDto card(String card) {
    this.card = card;
    return this;
  }

  /**
   * ID карты
   * @return card
  **/
  //@ApiModelProperty(value = "ID карты")


  public String getCard() {
    return card;
  }

  public void setCard(String card) {
    this.card = card;
  }

  public RnCardTransactionPublicDto operationType(String operationType) {
    this.operationType = operationType;
    return this;
  }

  /**
   * Тип операции
   * @return operationType
  **/
  //@ApiModelProperty(value = "Тип операции")


  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public RnCardTransactionPublicDto value(BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Объем
   * @return value
  **/
  //@ApiModelProperty(value = "Объем")

//  @Valid

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public RnCardTransactionPublicDto sum(BigDecimal sum) {
    this.sum = sum;
    return this;
  }

  /**
   * Сумма
   * @return sum
  **/
  //@ApiModelProperty(value = "Сумма")

//  @Valid

  public BigDecimal getSum() {
    return sum;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  public RnCardTransactionPublicDto dSum(BigDecimal dSum) {
    this.dSum = dSum;
    return this;
  }

  /**
   * Размер скидки (Отриц - наценка, Положит - скидка)
   * @return dSum
  **/
  //@ApiModelProperty(value = "Размер скидки (Отриц - наценка, Положит - скидка)")

//  @Valid

  public BigDecimal getDSum() {
    return dSum;
  }

  public void setDSum(BigDecimal dSum) {
    this.dSum = dSum;
  }

  public RnCardTransactionPublicDto price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Цена
   * @return price
  **/
  //@ApiModelProperty(value = "Цена")

//  @Valid

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public RnCardTransactionPublicDto gCode(String gCode) {
    this.gCode = gCode;
    return this;
  }

  /**
   * Код услуги
   * @return gCode
  **/
  //@ApiModelProperty(value = "Код услуги")


  public String getGCode() {
    return gCode;
  }

  public void setGCode(String gCode) {
    this.gCode = gCode;
  }

  public RnCardTransactionPublicDto gName(String gName) {
    this.gName = gName;
    return this;
  }

  /**
   * Название  услуги
   * @return gName
  **/
  //@ApiModelProperty(value = "Название  услуги")


  public String getGName() {
    return gName;
  }

  public void setGName(String gName) {
    this.gName = gName;
  }

  public RnCardTransactionPublicDto holder(String holder) {
    this.holder = holder;
    return this;
  }

  /**
   * Держатель карты (в процессинге)
   * @return holder
  **/
  //@ApiModelProperty(value = "Держатель карты (в процессинге)")


  public String getHolder() {
    return holder;
  }

  public void setHolder(String holder) {
    this.holder = holder;
  }

  public RnCardTransactionPublicDto dtl(String dtl) {
    this.dtl = dtl;
    return this;
  }

  /**
   * Расшифровка (детализация) услуги
   * @return dtl
  **/
  //@ApiModelProperty(value = "Расшифровка (детализация) услуги")


  public String getDtl() {
    return dtl;
  }

  public void setDtl(String dtl) {
    this.dtl = dtl;
  }

  public RnCardTransactionPublicDto ref(String ref) {
    this.ref = ref;
    return this;
  }

  /**
   * Ссылка на главную операцию
   * @return ref
  **/
  //@ApiModelProperty(value = "Ссылка на главную операцию")


  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public RnCardTransactionPublicDto gCat(String gCat) {
    this.gCat = gCat;
    return this;
  }

  /**
   * Код категории товара
   * @return gCat
  **/
  //@ApiModelProperty(value = "Код категории товара")


  public String getGCat() {
    return gCat;
  }

  public void setGCat(String gCat) {
    this.gCat = gCat;
  }

  public RnCardTransactionPublicDto createTime(OffsetDateTime createTime) {
    this.createTime = createTime;
    return this;
  }

  /**
   * Дата добавления записи
   * @return createTime
  **/
  //@ApiModelProperty(value = "Дата добавления записи")

//  @Valid

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }

  public RnCardTransactionPublicDto updateTime(OffsetDateTime updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  /**
   * Дата актуализации записи
   * @return updateTime
  **/
  //@ApiModelProperty(value = "Дата актуализации записи")

//  @Valid

  public OffsetDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(OffsetDateTime updateTime) {
    this.updateTime = updateTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RnCardTransactionPublicDto rnCardTransactionPublicDto = (RnCardTransactionPublicDto) o;
    return Objects.equals(this.address, rnCardTransactionPublicDto.address) &&
        Objects.equals(this.dPrice, rnCardTransactionPublicDto.dPrice) &&
        Objects.equals(this.holderATMS, rnCardTransactionPublicDto.holderATMS) &&
        Objects.equals(this.contract, rnCardTransactionPublicDto.contract) &&
        Objects.equals(this.organization, rnCardTransactionPublicDto.organization) &&
        Objects.equals(this.code, rnCardTransactionPublicDto.code) &&
        Objects.equals(this.operationTime, rnCardTransactionPublicDto.operationTime) &&
        Objects.equals(this.posCode, rnCardTransactionPublicDto.posCode) &&
        Objects.equals(this.timezone, rnCardTransactionPublicDto.timezone) &&
        Objects.equals(this.utcTime, rnCardTransactionPublicDto.utcTime) &&
        Objects.equals(this.card, rnCardTransactionPublicDto.card) &&
        Objects.equals(this.operationType, rnCardTransactionPublicDto.operationType) &&
        Objects.equals(this.value, rnCardTransactionPublicDto.value) &&
        Objects.equals(this.sum, rnCardTransactionPublicDto.sum) &&
        Objects.equals(this.dSum, rnCardTransactionPublicDto.dSum) &&
        Objects.equals(this.price, rnCardTransactionPublicDto.price) &&
        Objects.equals(this.gCode, rnCardTransactionPublicDto.gCode) &&
        Objects.equals(this.gName, rnCardTransactionPublicDto.gName) &&
        Objects.equals(this.holder, rnCardTransactionPublicDto.holder) &&
        Objects.equals(this.dtl, rnCardTransactionPublicDto.dtl) &&
        Objects.equals(this.ref, rnCardTransactionPublicDto.ref) &&
        Objects.equals(this.gCat, rnCardTransactionPublicDto.gCat) &&
        Objects.equals(this.createTime, rnCardTransactionPublicDto.createTime) &&
        Objects.equals(this.updateTime, rnCardTransactionPublicDto.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, dPrice, holderATMS, contract, organization, code, operationTime, posCode, timezone, utcTime, card, operationType, value, sum, dSum, price, gCode, gName, holder, dtl, ref, gCat, createTime, updateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RnCardTransactionPublicDto {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    dPrice: ").append(toIndentedString(dPrice)).append("\n");
    sb.append("    holderATMS: ").append(toIndentedString(holderATMS)).append("\n");
    sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
    sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    operationTime: ").append(toIndentedString(operationTime)).append("\n");
    sb.append("    posCode: ").append(toIndentedString(posCode)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    utcTime: ").append(toIndentedString(utcTime)).append("\n");
    sb.append("    card: ").append(toIndentedString(card)).append("\n");
    sb.append("    operationType: ").append(toIndentedString(operationType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    sum: ").append(toIndentedString(sum)).append("\n");
    sb.append("    dSum: ").append(toIndentedString(dSum)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    gCode: ").append(toIndentedString(gCode)).append("\n");
    sb.append("    gName: ").append(toIndentedString(gName)).append("\n");
    sb.append("    holder: ").append(toIndentedString(holder)).append("\n");
    sb.append("    dtl: ").append(toIndentedString(dtl)).append("\n");
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    gCat: ").append(toIndentedString(gCat)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
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

