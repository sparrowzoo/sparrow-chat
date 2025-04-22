package com.sparrow.chat.boot.dao;

public enum UserCategory {
    MANAGE(1, "管理员"),
    STUDENT(2, "学生"),
    TEACHER(3, "教师"),
    AFFILIATE(4, "代理商"),
    SERVE(5, "客服");

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
