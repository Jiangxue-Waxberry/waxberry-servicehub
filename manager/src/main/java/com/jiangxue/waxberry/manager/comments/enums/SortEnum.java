package com.jiangxue.waxberry.manager.comments.enums;


public enum SortEnum {

    DEFAULT("DEFAULT", "0"),
    RECENT("RECENT", "1");

    private String name;

    private String value;

    SortEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
