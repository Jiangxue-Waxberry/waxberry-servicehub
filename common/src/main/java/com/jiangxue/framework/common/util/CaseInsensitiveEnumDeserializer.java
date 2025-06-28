package com.jiangxue.framework.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public  class CaseInsensitiveEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private final Class<T> enumType;

    public CaseInsensitiveEnumDeserializer(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getText();
        return Enum.valueOf(enumType, value.toUpperCase());
    }
}
