package com.sparrow.chat.boot.dao;

import com.sparrow.protocol.POJO;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "user")
public class User12Talk implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "account_code", unique = true)
    private String accountCode;
    @Column(name = "manager_id")
    private Long managerId;
    @Column(name = "password")
    private String password;
    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "email")
    private String email;
    @Column(name = "remark")
    private String remark;
    @Column(name = "enabled")
    private Integer enabled;
    @Column(name = "deleted")
    private Integer deleted;
}
