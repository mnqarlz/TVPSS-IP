package com.tvpss.enums;

public enum Role {
	SUPER_ADMIN(0),
    STATE_ADMIN(1),
    PPD_ADMIN(2),
    SCHOOL_ADMIN(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
