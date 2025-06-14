package com.example.demo.global.enums;

public enum ValidErrorType {
    EMAIL, PASSWORD, USERNAME;

    public static ValidErrorType of(String value){
        return EnumValueOf.fromName(ValidErrorType.class, value, Enum::name);
    }
}
