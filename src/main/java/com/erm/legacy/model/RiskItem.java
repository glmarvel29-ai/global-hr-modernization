package com.erm.legacy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Mini risk_register row. Production estate claims 8,000+ tables;
 * this demo keeps a single demormalized entity deliberately.
 *
 * LEGACY PROBLEM: High code dependencies — column names mirror RiskGuard
 * Oracle schema (RG_*) AND AuditPro PostgreSQL snake_case aliases elsewhere.
 */
@Entity
@Table(name = "risk_register")
public class RiskItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String riskId;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private RiskDomain domain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private RiskStatus status;

    @Column(length = 64)
    private String owner;

    @Column(length = 512)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RiskDomain getDomain() {
        return domain;
    }

    public void setDomain(RiskDomain domain) {
        this.domain = domain;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public RiskStatus getStatus() {
        return status;
    }

    public void setStatus(RiskStatus status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
