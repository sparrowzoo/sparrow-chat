package com.sparrow.chat.protocol;

import com.sparrow.protocol.DTO;
import java.util.List;

public class QunDTO implements DTO {
    /**
     * 群id
     */
    private String qunId;
    /**
     * 群名称
     */
    private String qunName;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 国旗url
     */
    private String flagUrl;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单位类型
     */
    private String unitType;
    /**
     * 单位图标
     */
    private String unitIcon;
    /**
     * 群公告
     */
    private String announcement;
    /**
     * 创建人 群主
     */
    private Integer createUserId;
    /**
     * 群成员
     */
    private List<UserDTO> members;

    public String getQunId() {
        return qunId;
    }

    public void setQunId(String qunId) {
        this.qunId = qunId;
    }

    public String getQunName() {
        return qunName;
    }

    public void setQunName(String qunName) {
        this.qunName = qunName;
    }

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

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public List<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }
}
