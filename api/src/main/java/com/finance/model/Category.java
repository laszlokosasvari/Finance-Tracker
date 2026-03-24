package com.finance.model;

import java.util.Arrays;

public enum Category {
    FOOD, TRANSPORT, SALARY, ENTERTAINMENT, HEALTH, OTHER;

    public static Category getCategory(String tmpCategory) {
        return Arrays.stream(Category.values())
                .filter(c -> c.name().equalsIgnoreCase(tmpCategory))
                .findFirst()
                .orElse(OTHER); // Defaulting to OTHER is safer than SALARY
    }
}
