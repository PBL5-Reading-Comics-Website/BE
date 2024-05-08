package com.example.readingcomicwebsite.util;

public class CommonUtils {
    public static boolean isNotEmptyOrNullString(String order) {
        return order != null && !order.isEmpty();
    }

    public static String convertToCamelCase(String sortBy) {
        return sortBy.substring(0, 1).toLowerCase() + sortBy.substring(1);
    }
}
