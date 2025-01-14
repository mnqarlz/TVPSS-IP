package com.tvpss.enums;

public enum RecordStatus {
    ADA("Ada"),
    TIADA("Tiada");

    private final String displayName;

    RecordStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
