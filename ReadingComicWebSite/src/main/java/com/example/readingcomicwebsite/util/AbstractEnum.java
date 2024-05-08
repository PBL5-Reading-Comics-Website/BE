package com.example.readingcomicwebsite.util;

public abstract class AbstractEnum {
    public static <E extends Enum<E>> E fromString(E[] enumValues, String name) {
        for (E enumValue : enumValues) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("There is no value with name '" + name + "' in Enum " + enumValues[0].getClass().getName());
    }
}