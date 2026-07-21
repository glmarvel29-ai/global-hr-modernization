package com.erm.legacy.service;

import com.erm.legacy.legacy.SharedBusinessServices;
import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import com.erm.legacy.model.RiskStatus;
import com.erm.legacy.model.Severity;
import com.erm.legacy.repository.RiskItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds a small risk register so the app is immediately viewable on run.
 * ID prefixes encode acquired-product provenance (legacy smell).
 */
@Component
public class DemoDataLoader implements CommandLineRunner {

    private final RiskItemRepository repository;

    public DemoDataLoader(RiskItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        save("RG", "1001", "Incomplete enterprise risk appetite statement",
                RiskDomain.ENTERPRISE_RISK_MANAGEMENT, Severity.HIGH, RiskStatus.OPEN, "CRO Office",
                "Board appetite not refreshed since acquired RiskGuard merge.");
        save("RG", "1002", "Process failure in trade settlement controls",
                RiskDomain.OPERATIONAL_RISK, Severity.MEDIUM, RiskStatus.MITIGATING, "Ops Risk",
                "Shared business service timeout under hybrid monolith load.");
        save("AP", "2001", "Sox control testing backlog",
                RiskDomain.INTERNAL_AUDIT, Severity.HIGH, RiskStatus.OPEN, "Internal Audit",
                "AuditPro schedule engine diverges from Spring MVC calendar.");
        save("AP", "2002", "Stale acceptable-use policy",
                RiskDomain.POLICY_MANAGEMENT, Severity.LOW, RiskStatus.OPEN, "Policy Office",
                "JSP policy viewer cannot render new AngularJS editor drafts.");
        save("RG", "1003", "GDPR DPIA gaps for APAC region",
                RiskDomain.REGULATORY_COMPLIANCE, Severity.CRITICAL, RiskStatus.OPEN, "Compliance",
                "Multi-region deployment model missing DPIA evidence pack.");
        save("RG", "1004", "Privileged access review overdue",
                RiskDomain.IT_RISK_MANAGEMENT, Severity.HIGH, RiskStatus.OPEN, "IT Risk",
                "Entra ID + Okta dual IdP creates review blind spots.");
        save("RG", "1005", "Ransomware recovery drill failed RTO",
                RiskDomain.CYBER_RISK, Severity.CRITICAL, RiskStatus.OPEN, "CISO",
                "SIEM alert routing still points at decommissioned WebLogic farm.");
        save("VS", "3001", "Critical vendor without SOC2",
                RiskDomain.THIRD_PARTY_RISK, Severity.HIGH, RiskStatus.ASSESSING, "TPRM",
                "VendorSight questionnaire schema not aligned with risk_register.");
        save("VS", "3002", "Concentrated cloud provider dependency",
                RiskDomain.VENDOR_RISK_MANAGEMENT, Severity.MEDIUM, RiskStatus.OPEN, "Procurement",
                "Oracle ERP PO feed lags SAP alternate vendor scoring.");
    }

    private void save(String prefix, String num, String title, RiskDomain domain,
                      Severity severity, RiskStatus status, String owner, String description) {
        RiskItem item = new RiskItem();
        item.setRiskId(SharedBusinessServices.composeLegacyRiskKey(prefix, num));
        item.setTitle(title);
        item.setDomain(domain);
        item.setSeverity(severity);
        item.setStatus(status);
        item.setOwner(owner);
        item.setDescription(description);
        repository.save(item);
    }
}
