package com.erm.legacy.model;

/**
 * Simulated third-party connector status (Integration Complexity column).
 */
public class IntegrationStatus {

    private final String name;
    private final String category;
    private final String status;
    private final String legacyNote;

    public IntegrationStatus(String name, String category, String status, String legacyNote) {
        this.name = name;
        this.category = category;
        this.status = status;
        this.legacyNote = legacyNote;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getLegacyNote() { return legacyNote; }
}
