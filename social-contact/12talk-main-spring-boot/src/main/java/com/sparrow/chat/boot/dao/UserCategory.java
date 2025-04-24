package com.sparrow.chat.boot.dao;

public enum UserCategory {
    REGISTER(1, "注册用户"),
    MANAGE(2, "管理员"),
    STUDENT(3, "学生"),
    TEACHER(4, "教师"),
    AFFILIATE(5, "代理商"),
    SERVE(6, "客服");

    private Integer id;
    private String name;

    UserCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
