package com.example.advantumconverter.gen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


/**
 * OffsetIntervalPublicDto
 */

//@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-12-04T17:32:34.945Z")


public class OffsetIntervalPublicDto {
    @JsonProperty("finishTime")
    private OffsetDateTimeWithOffsetPublicDto finishTime = null;

    @JsonProperty("startTime")
    private OffsetDateTimeWithOffsetPublicDto startTime = null;

    public OffsetIntervalPublicDto finishTime(OffsetDateTimeWithOffsetPublicDto finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    /**
     * Get finishTime
     *
     * @return finishTime
     **/


//  @Valid
    public OffsetDateTimeWithOffsetPublicDto getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(OffsetDateTimeWithOffsetPublicDto finishTime) {
        this.finishTime = finishTime;
    }

    public OffsetIntervalPublicDto startTime(OffsetDateTimeWithOffsetPublicDto startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Get startTime
     *
     * @return startTime
     **/


//  @Valid
    public OffsetDateTimeWithOffsetPublicDto getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTimeWithOffsetPublicDto startTime) {
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
        OffsetIntervalPublicDto offsetIntervalPublicDto = (OffsetIntervalPublicDto) o;
        return Objects.equals(this.finishTime, offsetIntervalPublicDto.finishTime) &&
                Objects.equals(this.startTime, offsetIntervalPublicDto.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finishTime, startTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OffsetIntervalPublicDto {\n");

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

