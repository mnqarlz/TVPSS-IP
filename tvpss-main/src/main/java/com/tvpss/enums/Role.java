package com.tvpss.enums;

public enum Role {
	SUPER_ADMIN(1),
    STATE_ADMIN(2),
    PPD_ADMIN(3),
    SCHOOL_ADMIN(4);

    private final int roleValue;

    Role(int roleValue) {
        this.roleValue = roleValue;
    }

    public int getRoleValue() {
        return roleValue;
    }
    
    public static Role fromInt(int i) {
        for (Role role : Role.values()) {
            if (role.getRoleValue() == i) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}
