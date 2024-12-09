package com.tvpss.enums;

public enum EqStatus {
	
	BERFUNGSI("Berfungsi"),
    TIDAK_BERFUNGSI("Tidak Berfungsi"),
    PENYELENGGARAAN("Penyelenggaraan");

    private final String value;

    EqStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String[] getValues() {
        EqStatus[] values = EqStatus.values();
        String[] valueStrings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            valueStrings[i] = values[i].getValue();
        }
        return valueStrings;
    }

    public static EqStatus fromString(String value) {
        for (EqStatus status : EqStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
