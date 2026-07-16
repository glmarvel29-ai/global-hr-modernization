package com.erm.legacy.model;

/**
 * Documented technical challenge / legacy debt item.
 */
public class LegacyChallenge {

    private final String title;
    private final String detail;
    private final String severity;

    public LegacyChallenge(String title, String detail, String severity) {
        this.title = title;
        this.detail = detail;
        this.severity = severity;
    }

    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public String getSeverity() { return severity; }
}
