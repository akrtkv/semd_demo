package com.github.akrtkv.semd_demo.util;

import java.util.Arrays;

public enum Validity {
    ONE("1", "15 дней"),
    TWO("2", "30 дней"),
    THREE("3", "60 дней"),
    FOUR("4", "90 дней"),
    FIVE("5", "до 1 года"),
    SIX("6", "1 месяц"),
    SEVEN("7", "3 месяца");

    public final String code;

    public final String name;

    Validity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Validity getValidityByName(String name) {
        return Arrays.stream(Validity.values())
                .filter(validity -> validity.name.equals(name))
                .findFirst().orElse(ONE);
    }
}
