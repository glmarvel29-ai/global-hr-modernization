package com.erm.legacy.controller;

import com.erm.legacy.integration.IntegrationHub;
import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.security.AccessControlService;
import com.erm.legacy.service.RiskService;
import com.erm.legacy.web.DomainQueryResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Classic Spring MVC page controllers returning JSP views.
 * Frontend shell uses AngularJS 1.x for interactive panels.
 */
@Controller
public class PageController {

    private final RiskService riskService;
    private final IntegrationHub integrationHub;
    private final AccessControlService accessControlService;
    private final DomainQueryResolver domainQueryResolver;

    public PageController(RiskService riskService,
                          IntegrationHub integrationHub,
                          AccessControlService accessControlService,
                          DomainQueryResolver domainQueryResolver) {
        this.riskService = riskService;
        this.integrationHub = integrationHub;
        this.accessControlService = accessControlService;
        this.domainQueryResolver = domainQueryResolver;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Technical & Business Complexity");
        model.addAttribute("scale", riskService.scaleMetrics());
        model.addAttribute("domains", riskService.domains());
        model.addAttribute("domainCounts", riskService.countsByDomain());
        model.addAttribute("challenges", riskService.legacyChallenges());
        model.addAttribute("techStack", techStackNotes());
        model.addAttribute("sla", slaNotes());
        model.addAttribute("ops", opsNotes());
        model.addAttribute("business", businessNotes());
        return "dashboard";
    }

    @GetMapping("/risks")
    public String risks(@RequestParam(value = "domainCode", required = false) String domainCode,
                        @RequestParam(value = "domain", required = false) String domain,
                        Model model) {
        model.addAttribute("pageTitle", "Enterprise Risk Register");
        model.addAttribute("domains", riskService.domains());

        String requested = domainCode != null ? domainCode : domain;
        RiskDomain selected = domainQueryResolver.resolve(requested, "domain");
        if (selected == null) {
            model.addAttribute("risks", riskService.findAll());
        } else {
            model.addAttribute("selectedDomain", selected);
            model.addAttribute("risks", riskService.findByDomain(selected));
        }
        return "risks";
    }

    @GetMapping("/integrations")
    public String integrations(Model model) {
        model.addAttribute("pageTitle", "Integration Complexity");
        model.addAttribute("integrations", integrationHub.listIntegrations());
        return "integrations";
    }

    @GetMapping("/security")
    public String security(Model model) {
        model.addAttribute("pageTitle", "Security Complexity");
        model.addAttribute("posture", accessControlService.securityPosture());
        model.addAttribute("rbacAdmin", accessControlService.hasPermission("ADMIN", "risk:write"));
        model.addAttribute("abacSample",
                accessControlService.abacAllow("VENDOR_ANALYST", "EU-WEST", "RESTRICTED"));
        return "security";
    }

    @GetMapping("/legacy")
    public String legacy(Model model) {
        model.addAttribute("pageTitle", "Legacy Technical Challenges");
        model.addAttribute("challenges", riskService.legacyChallenges());
        model.addAttribute("dialectUs", riskService.dialectForRegion("US-EAST"));
        model.addAttribute("dialectEu", riskService.dialectForRegion("EU-WEST"));
        return "legacy";
    }

    private String[] techStackNotes() {
        return new String[]{
                "Backend: Java 8, Spring MVC",
                "Frontend: JSP, AngularJS",
                "Database: Oracle, PostgreSQL (H2 for local demo)"
        };
    }

    private String[] slaNotes() {
        return new String[]{
                "24x7 Follow-the-Sun Support",
                "P1 Response within 15 Minutes",
                "P1 Resolution within 2 Hours",
                "99.99% Platform Availability",
                "Zero Data Loss",
                "Continuous Compliance Readiness"
        };
    }

    private String[] opsNotes() {
        return new String[]{
                "24x7 Global Operations",
                "Multi-region deployments",
                "High Availability (>99.99%)",
                "Disaster Recovery",
                "Zero-downtime releases",
                "Continuous compliance monitoring"
        };
    }

    private String[] businessNotes() {
        return new String[]{
                "Enterprise Risk Registers",
                "Regulatory Compliance",
                "Internal & External Audits",
                "Security Controls",
                "Vendor Risk Assessments",
                "Compliance Certifications",
                "Executive Risk Reporting"
        };
    }
}
