package com.sparrow.chat.controller;

import com.sparrow.chat.protocol.qun.QunCreateParam;
import com.sparrow.chat.protocol.qun.QunModifyAnnounceParam;
import com.sparrow.chat.protocol.qun.QunModifyNameParam;
import com.sparrow.chat.protocol.qun.QunModifyRemarkParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("qun")
public class QunController {
    @PostMapping("create")
    public Boolean create(QunCreateParam qunCreateParam) {
        return true;
    }

    @PostMapping("modify-announce")
    public Boolean modifyAnnounce(QunModifyAnnounceParam qunCreateParam) {
        return true;
    }

    @PostMapping("modify-name")
    public Boolean modifyName(QunModifyNameParam qunModifyNameParam) {
        return true;
    }

    @PostMapping("modify-remark")
    public Boolean modifyRemark(QunModifyRemarkParam qunModifyRemarkParam) {
        return true;
    }
}
