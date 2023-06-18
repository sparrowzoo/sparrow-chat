package com.sparrow.chat.contact.protocol.enums;

public enum Nationality {

    CHINA(1, "中国");

    private Integer id;
    private String name;

    Nationality(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
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
