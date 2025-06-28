package com.jiangxue.waxberry.user.enums;

public enum YccNumEnum {
    PERSONAL("PERSONAL", 5),
    ENTERPRISE("ENTERPRISE", 10),
    COLLEGE("COLLEGE", 10);

    private String name;

    private int value;

    YccNumEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static int getValueByName(String name) {
        for (YccNumEnum templateEnum : YccNumEnum.values()) {
            if (templateEnum.getName().equalsIgnoreCase(name)) {
                return templateEnum.getValue();
            }
        }
        return 0;
    }
}
