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
        rolePermissions.put("ADMIN", new HashSet<String>(Arrays.asList(
                "risk:read", "risk:write", "audit:read", "vendor:assess", "compliance:read", "integration:read", "security:read")));
        rolePermissions.put("RISK_ANALYST", new HashSet<String>(Arrays.asList(
                "risk:read", "integration:read")));
        rolePermissions.put("INTERNAL_AUDIT", new HashSet<String>(Arrays.asList(
                "risk:read", "audit:read", "compliance:read", "integration:read", "security:read")));
        rolePermissions.put("VENDOR_ANALYST", new HashSet<String>(Collections.singletonList("risk:read")));
    }

    /** RBAC check */
    public boolean hasPermission(String role, String permission) {
        if (role == null || permission == null) {
            return false;
        }
        Set<String> perms = rolePermissions.get(role.toUpperCase());
        return perms != null && perms.contains(permission.toLowerCase());
    }

    /** ABAC-style attribute gate (region + clearance) */
    public boolean abacAllow(String role, String region, String dataClassification) {
        if ("PUBLIC".equalsIgnoreCase(dataClassification)) {
            return true;
        }
        if ("RESTRICTED".equalsIgnoreCase(dataClassification)
                && !"ADMIN".equalsIgnoreCase(role) && !"INTERNAL_AUDIT".equalsIgnoreCase(role)) {
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
        map.put("rolePermissions", rolePermissions);
        return map;
    }
}
