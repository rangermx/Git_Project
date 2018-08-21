package com.test.domain;

public class Group {
    private Integer groupid;

    private String groupName;

    private Integer userid;

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

	@Override
	public String toString() {
		return "Group [groupid=" + groupid + ", groupName=" + groupName + ", userid=" + userid + "]";
	}
}