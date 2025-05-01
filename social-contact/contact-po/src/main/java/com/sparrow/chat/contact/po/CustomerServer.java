package com.sparrow.chat.contact.po;

import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "t_customer_service")
public class CustomerServer extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED ")
    private Long id;

    @Column(name = "tenant_id", columnDefinition = "varchar(64) NOT NULL DEFAULT '' comment '租户标识'")
    private String tenantId;

    @Column(name = "category", columnDefinition = "int(11)  NOT NULL DEFAULT '0' comment '租户类型 平台/中介'")
    private Integer category;

    @Column(name = "server_id", columnDefinition = "int(11)  NOT NULL DEFAULT '0' comment '客服ID'")
    private Integer serverId;

    @Column(name = "server_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' comment '客服名称'")
    private String serverName;

    @Column(name = "status", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0' comment '状态'")
    private StatusRecord status;
}
