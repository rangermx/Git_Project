package com.test.domain;

public class Zigbee {
    private String zigbeeMac;

    private String zigbeeName;

    private Integer zigbeeNet;

    private Integer zigbeeBright;

    private Integer zigbeeStatus;

    private String zigbeeSaddr;

    private String devMac;

    public String getZigbeeMac() {
        return zigbeeMac;
    }

    public void setZigbeeMac(String zigbeeMac) {
        this.zigbeeMac = zigbeeMac == null ? null : zigbeeMac.trim();
    }

    public String getZigbeeName() {
        return zigbeeName;
    }

    public void setZigbeeName(String zigbeeName) {
        this.zigbeeName = zigbeeName == null ? null : zigbeeName.trim();
    }

    public Integer getZigbeeNet() {
        return zigbeeNet;
    }

    public void setZigbeeNet(Integer zigbeeNet) {
        this.zigbeeNet = zigbeeNet;
    }

    public Integer getZigbeeBright() {
        return zigbeeBright;
    }

    public void setZigbeeBright(Integer zigbeeBright) {
        this.zigbeeBright = zigbeeBright;
    }

    public Integer getZigbeeStatus() {
        return zigbeeStatus;
    }

    public void setZigbeeStatus(Integer zigbeeStatus) {
        this.zigbeeStatus = zigbeeStatus;
    }

    public String getZigbeeSaddr() {
        return zigbeeSaddr;
    }

    public void setZigbeeSaddr(String zigbeeSaddr) {
        this.zigbeeSaddr = zigbeeSaddr == null ? null : zigbeeSaddr.trim();
    }

    public String getDevMac() {
        return devMac;
    }

    public void setDevMac(String devMac) {
        this.devMac = devMac == null ? null : devMac.trim();
    }

	@Override
	public String toString() {
		return "Zigbee [zigbeeMac=" + zigbeeMac + ", zigbeeName=" + zigbeeName + ", zigbeeNet=" + zigbeeNet
				+ ", zigbeeBright=" + zigbeeBright + ", zigbeeStatus=" + zigbeeStatus + ", zigbeeSaddr=" + zigbeeSaddr
				+ ", devMac=" + devMac + "]";
	}
}