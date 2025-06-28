package com.jiangxue.waxberry.manager.comments.enums;


public enum CommentsEnum {

    PARENT("PARENT", "PARENT"),
    CHILD("CHILD", "CHILD"),
    CANCEL("CANCEL", "0"),
    CONFIRM("CONFIRM", "1");

    private String name;

    private String value;

    CommentsEnum(String name, String value) {
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
