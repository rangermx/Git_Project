package com.test.domain;

import java.util.Date;

public class UnusualTemperature {
    private Integer id;

    private String zigbeeMac;

    private Date date;

    private Float temperature;

    private Float humidity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZigbeeMac() {
        return zigbeeMac;
    }

    public void setZigbeeMac(String zigbeeMac) {
        this.zigbeeMac = zigbeeMac == null ? null : zigbeeMac.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }
}