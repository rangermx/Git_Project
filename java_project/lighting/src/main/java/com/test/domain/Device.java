package com.test.domain;

public class Device {
    private String devMac;

    private String devName;

    private Integer devNet;

    private Integer userid;

    private String gprsPhone;

    public String getDevMac() {
        return devMac;
    }

    public void setDevMac(String devMac) {
        this.devMac = devMac == null ? null : devMac.trim();
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName == null ? null : devName.trim();
    }

    public Integer getDevNet() {
        return devNet;
    }

    public void setDevNet(Integer devNet) {
        this.devNet = devNet;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getGprsPhone() {
        return gprsPhone;
    }

    public void setGprsPhone(String gprsPhone) {
        this.gprsPhone = gprsPhone == null ? null : gprsPhone.trim();
    }

	@Override
	public String toString() {
		return "Device [devMac=" + devMac + ", devName=" + devName + ", devNet=" + devNet + ", userid=" + userid
				+ ", gprsPhone=" + gprsPhone + "]";
	}
}