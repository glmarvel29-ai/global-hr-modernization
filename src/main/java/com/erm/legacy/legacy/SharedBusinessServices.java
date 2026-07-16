package com.erm.legacy.legacy;

/**
 * Stub for the "shared business services" jar surface area.
 *
 * LEGACY PROBLEM: Controllers, batch jobs, and microservices all import this facade.
 * In production this couples 250+ services; here it is a deliberate smell marker.
 */
public final class SharedBusinessServices {

    private SharedBusinessServices() {
    }

    public static String resolveTenantDialect(String region) {
        // LEGACY: Oracle for Americas (RiskGuard), PostgreSQL for EMEA (AuditPro merge)
        if (region != null && region.toUpperCase().startsWith("EU")) {
            return "POSTGRESQL";
        }
        return "ORACLE";
    }

    public static String composeLegacyRiskKey(String productPrefix, String numericId) {
        // Competing ID schemes from acquired products
        return productPrefix + "-" + numericId;
    }
}
