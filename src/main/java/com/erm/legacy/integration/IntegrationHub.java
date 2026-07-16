package com.erm.legacy.integration;

import com.erm.legacy.model.IntegrationStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Integration Complexity column — simulated connectors.
 * LEGACY PROBLEM: Each acquired product brought its own adapter; no single SDK.
 */
@Component
public class IntegrationHub {

    public List<IntegrationStatus> listIntegrations() {
        return Collections.unmodifiableList(Arrays.asList(
                new IntegrationStatus("SAP", "ERP", "CONNECTED",
                        "RiskGuard SAP RFC adapter (2013) still in use"),
                new IntegrationStatus("Oracle ERP", "ERP", "CONNECTED",
                        "Oracle primary for Americas risk finance export"),
                new IntegrationStatus("ServiceNow", "ITSM", "CONNECTED",
                        "Incident bridge for P1 cyber risks"),
                new IntegrationStatus("Microsoft Entra ID / Active Directory", "IAM", "CONNECTED",
                        "Primary IdP; MFA enforced"),
                new IntegrationStatus("Okta", "IAM", "DEGRADED",
                        "Secondary IdP for vendor portal — VendorSight leftover"),
                new IntegrationStatus("SIEM platforms", "Security", "CONNECTED",
                        "Syslog + REST fan-out to regional SIEMs"),
                new IntegrationStatus("IAM platforms", "Security", "CONNECTED",
                        "Shared identity sync with Entra / Okta"),
                new IntegrationStatus("Vulnerability scanners", "Security", "CONNECTED",
                        "Nightly import into IT Risk domain"),
                new IntegrationStatus("HRMS systems", "HR", "CONNECTED",
                        "Org hierarchy feed for control ownership")
        ));
    }
}
