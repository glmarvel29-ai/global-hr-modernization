package com.erm.legacy.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/** MBA-48 / AC-S04 — Security path unit tests for RBAC and ABAC gates. */
class AccessControlServiceTest {
    private AccessControlService service;
    @BeforeEach void setUp() { service = new AccessControlService(); }
    @Test void riskAdmin_hasRiskWritePermission() { assertTrue(service.hasPermission("RISK_ADMIN", "RISK_WRITE")); }
    @Test void viewer_deniedRiskWritePermission() { assertFalse(service.hasPermission("VIEWER", "RISK_WRITE")); }
    @Test void unknownRole_deniedAnyPermission() { assertFalse(service.hasPermission("UNKNOWN_ROLE", "RISK_READ")); }
    @Test void abac_publicClassification_alwaysAllowed() { assertTrue(service.abacAllow("VIEWER", null, "PUBLIC")); }
    @Test void abac_restrictedDeniedForViewer() { assertFalse(service.abacAllow("VIEWER", "US", "RESTRICTED")); }
    @Test void abac_restrictedAllowedForRiskAdmin() { assertTrue(service.abacAllow("RISK_ADMIN", "US", "RESTRICTED")); }
    @Test void abac_missingRegionDeniedForNonPublic() { assertFalse(service.abacAllow("AUDITOR", null, "INTERNAL")); }
    @Test void abac_validRegionAllowsInternalData() { assertTrue(service.abacAllow("AUDITOR", "EU-WEST", "INTERNAL")); }
    @Test void securityPosture_reportsZeroTrustEnabled() {
        Map<String, Object> posture = service.securityPosture();
        assertEquals(Boolean.TRUE, posture.get("zeroTrust"));
        assertEquals(Boolean.TRUE, posture.get("mfaRequired"));
        assertEquals(Boolean.TRUE, posture.get("rbac"));
        assertEquals(Boolean.TRUE, posture.get("abac"));
    }
}
