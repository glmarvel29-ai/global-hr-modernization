package com.erm.legacy.model;

/**
 * Legacy Technology column domains from the Complexity table.
 * Product age 15+ years — enum names frozen from acquired product merges.
 */
public enum RiskDomain {
    ENTERPRISE_RISK_MANAGEMENT("Enterprise Risk Management", "ERM"),
    OPERATIONAL_RISK("Operational Risk", "OPR"),
    INTERNAL_AUDIT("Internal Audit", "IAU"),
    POLICY_MANAGEMENT("Policy Management", "POL"),
    REGULATORY_COMPLIANCE("Regulatory Compliance", "REG"),
    IT_RISK_MANAGEMENT("IT Risk Management", "ITR"),
    CYBER_RISK("Cyber Risk", "CYB"),
    THIRD_PARTY_RISK("Third-Party Risk", "TPR"),
    VENDOR_RISK_MANAGEMENT("Vendor Risk Management", "VRM");

    private final String label;
    private final String code;

    RiskDomain(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }
}
