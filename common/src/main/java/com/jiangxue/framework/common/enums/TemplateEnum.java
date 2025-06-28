package com.jiangxue.framework.common.enums;

public enum TemplateEnum {
    JIANGXUE("signName", "绛雪智能"),
    NATTOPASS("natto_pass", "SMS_489755377"),
    NATTOREFUSE("natto_refuse", "SMS_489945096"),
    READYACCEPTPASS("ready_accept_pass", "SMS_489800451"),
    READYACCEPTREFUSE("ready_accept_refuse", "SMS_489690398"),
    READYACCEPTNOTICE("ready_accept_notice", "SMS_489755470"),
    ACCEPTPASS("accept_pass", "SMS_489860418"),
    ACCEPTREFUSE("accept_refuse", "SMS_489945104"),
    ACCEPTNOTIFY("accept_notify", "SMS_489755469"),
    SUBMITREFUSE("submit_refuse", "SMS_489650375"),
    SUBMITNOTICE("submit_notice", "SMS_489725404"),
    SENDPASS("send_pass", "SMS_489850443"),
    SENDREFUSE("send_refuse", "SMS_489860417"),
    Login("login", "SMS_489110161"),
    ResetPassword("resetPassword", "SMS_488470068"),
    Register("register", "SMS_489065183"),
    PROCESS("process", "SMS_488475086"),
    PASS("pass", "SMS_489270193"),
    REFUSE("refuse", "SMS_489165211");

    private String name;

    private String value;

    TemplateEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByName(String name) {
        for (TemplateEnum templateEnum : TemplateEnum.values()) {
            if (templateEnum.getName().equalsIgnoreCase(name)) {
                return templateEnum.getValue();
            }
        }
        return null;
    }
}
