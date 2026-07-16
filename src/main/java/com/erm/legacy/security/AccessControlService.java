package com.erm.legacy.security;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Security Complexity column (demo stubs — not a full IdP).
 * Zero Trust + MFA + RBAC + ABAC + encryption flags for the dashboard.
 *
 * LEGACY PROBLEM: RBAC matrices from RiskGuard and ABAC rules from VendorSight
 * overlap; effective-permission computation still dual-path.
 */
@Component
public class AccessControlService {

    private final Map<String, Set<String>> rolePermissions = new HashMap<String, Set<String>>();

    public AccessControlService() {
        rolePermissions.put("RISK_ADMIN", new HashSet<String>(Arrays.asList(
                "RISK_READ", "RISK_WRITE", "AUDIT_READ", "VENDOR_ASSESS", "COMPLIANCE_READ")));
        rolePermissions.put("AUDITOR", new HashSet<String>(Arrays.asList(
                "RISK_READ", "AUDIT_READ", "COMPLIANCE_READ")));
        rolePermissions.put("VENDOR_ANALYST", new HashSet<String>(Arrays.asList(
                "RISK_READ", "VENDOR_ASSESS")));
        rolePermissions.put("VIEWER", new HashSet<String>(Collections.singletonList("RISK_READ")));
    }

    /** RBAC check */
    public boolean hasPermission(String role, String permission) {
        Set<String> perms = rolePermissions.get(role);
        return perms != null && perms.contains(permission);
    }

    /** ABAC-style attribute gate (region + clearance) */
    public boolean abacAllow(String role, String region, String dataClassification) {
        if ("PUBLIC".equalsIgnoreCase(dataClassification)) {
            return true;
        }
        if ("RESTRICTED".equalsIgnoreCase(dataClassification)
                && !"RISK_ADMIN".equals(role) && !"AUDITOR".equals(role)) {
            return false;
        }
        // Zero Trust: deny missing region claim
        return region != null && region.trim().length() > 0;
    }

    public Map<String, Object> securityPosture() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("zeroTrust", true);
        map.put("mfaRequired", true);
        map.put("rbac", true);
        map.put("abac", true);
        map.put("encryptionAtRest", true);
        map.put("encryptionInTransit", true);
        map.put("secretsManagement", "Vault + Entra ID managed identities (simulated)");
        map.put("incidentMonitoring", "SIEM feed connected (simulated)");
        map.put("vulnerabilityManagement", "Continuous (simulated)");
        map.put("legacyNote",
                "RBAC matrices (RiskGuard) and ABAC policies (VendorSight) co-exist — dual evaluation path.");
        return map;
    }
}
