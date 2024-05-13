package org.alex.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IotDeviceThresholdInfoVo {

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private String result;

    private BigDecimal value;

}
