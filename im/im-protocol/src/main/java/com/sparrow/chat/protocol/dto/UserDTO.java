package com.sparrow.chat.protocol.dto;

import com.sparrow.protocol.POJO;

public class UserDTO implements POJO,Comparable<UserDTO>{
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 国旗url
     */
    private String flagUrl;
    /**
     * 所在单位
     */
    private String unit;
    /**
     * 单位类型
     */
    private String unitType;
    /**
     * 单位icon
     */
    private String unitIcon;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头象
     */
    private String avatar;
    /**
     * 加入时间
     */
    private Long addTime;

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitIcon() {
        return unitIcon;
    }

    public void setUnitIcon(String unitIcon) {
        this.unitIcon = unitIcon;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    @Override public int compareTo(UserDTO o) {
        return this.userName.compareTo(o.getUserName());
    }
}
