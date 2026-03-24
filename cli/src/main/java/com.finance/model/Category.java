package com.finance.model;

public enum Category {
    FOOD, TRANSPORT, SALARY, ENTERTAINMENT, HEALTH, OTHER;


    public static Category getCategory (String tmpCategory) {
        tmpCategory = tmpCategory.toLowerCase();

        if (tmpCategory.equals(ENTERTAINMENT.name().toLowerCase())) return ENTERTAINMENT;

        if (tmpCategory.equals(FOOD.name().toLowerCase())) return FOOD;

        if (tmpCategory.equals(HEALTH.name().toLowerCase())) return HEALTH;

        if (tmpCategory.equals(TRANSPORT.name().toLowerCase())) return TRANSPORT;

        if (tmpCategory.equals(OTHER.name().toLowerCase())) return OTHER;

        return SALARY;
    }
}
