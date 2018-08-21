package com.test.domain;

public class PloyOperate {
    private Integer id;

    private Integer ployid;

    private String date;

    private Integer hours;

    private Integer minutes;

    private Integer operateType;

    private Integer operateParam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPloyid() {
        return ployid;
    }

    public void setPloyid(Integer ployid) {
        this.ployid = ployid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getOperateParam() {
        return operateParam;
    }

    public void setOperateParam(Integer operateParam) {
        this.operateParam = operateParam;
    }

	@Override
	public String toString() {
		return "PloyOperate [id=" + id + ", ployid=" + ployid + ", date=" + date + ", hours=" + hours + ", minutes="
				+ minutes + ", operateType=" + operateType + ", operateParam=" + operateParam + "]";
	}
}