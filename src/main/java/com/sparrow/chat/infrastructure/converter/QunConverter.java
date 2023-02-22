package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.po.Qun;
import com.sparrow.chat.protocol.QunDTO;
import javax.inject.Named;

@Named
public class QunConverter {
    public QunDTO po2dto(Qun qun) {
        QunDTO qunDto = new QunDTO();
        qunDto.setQunId(qun.getId() + "");
        qunDto.setQunName(qun.getName());
        qunDto.setNationality(qun.getNationality());
        //通过国籍能拿到国旗
        //todo 获取国旗
        qunDto.setFlagUrl("");
        //todo 获取机构信息
        qunDto.setUnit("");
        qunDto.setUnitType("");
        qunDto.setUnitIcon("");
        qunDto.setAnnouncement(qun.getAnnouncement());
        qunDto.setCreateUserId(qun.getCreateUserId().intValue());
        return null;
    }
}
