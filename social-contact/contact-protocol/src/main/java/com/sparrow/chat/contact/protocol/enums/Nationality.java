package com.sparrow.chat.contact.protocol.enums;

public enum Nationality {

    CHINA(1, "中国", "china");

    private Integer id;
    private String name;
    private String flag;

    Nationality(Integer id, String name, String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public static Nationality getById(Integer id) {
        for (Nationality nationality : Nationality.values()) {
            if (nationality.getId().equals(id)) {
                return nationality;
            }
        }
        return null;
    }
}
