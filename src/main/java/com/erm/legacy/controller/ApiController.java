package com.erm.legacy.controller;

import com.erm.legacy.integration.IntegrationHub;
import com.erm.legacy.model.IntegrationStatus;
import com.erm.legacy.model.LegacyChallenge;
import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import com.erm.legacy.model.ScaleMetrics;
import com.erm.legacy.security.AccessControlService;
import com.erm.legacy.service.RiskService;
import com.erm.legacy.web.DomainQueryResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON APIs consumed by AngularJS controllers on the JSP pages.
 * Production estate: 4,500+ APIs — this demo keeps a small REST surface.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final RiskService riskService;
    private final IntegrationHub integrationHub;
    private final AccessControlService accessControlService;
    private final DomainQueryResolver domainQueryResolver;

    public ApiController(RiskService riskService,
                         IntegrationHub integrationHub,
                         AccessControlService accessControlService,
                         DomainQueryResolver domainQueryResolver) {
        this.riskService = riskService;
        this.integrationHub = integrationHub;
        this.accessControlService = accessControlService;
        this.domainQueryResolver = domainQueryResolver;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("status", "UP");
        body.put("stack", "Java 8 / Spring MVC / JSP / AngularJS");
        body.put("databases", "Oracle + PostgreSQL (demo: H2)");
        body.put("locCap", "<5000");
        body.put("architecture", "hybrid-monolith-microservices");
        return body;
    }

    @GetMapping("/risks")
    public List<RiskItem> risks(@RequestParam(value = "domain", required = false) String domain) {
        RiskDomain resolved = domainQueryResolver.resolve(domain, "domain");
        if (resolved == null) {
            return riskService.findAll();
        }
        return riskService.findByDomain(resolved);
    }

    @GetMapping("/domains")
    public RiskDomain[] domains() {
        return riskService.domains();
    }

    @GetMapping("/scale")
    public ScaleMetrics scale() {
        return riskService.scaleMetrics();
    }

    @GetMapping("/legacy-challenges")
    public List<LegacyChallenge> challenges() {
        return riskService.legacyChallenges();
    }

    @GetMapping("/integrations")
    public List<IntegrationStatus> integrations() {
        return integrationHub.listIntegrations();
    }

    @GetMapping("/security/posture")
    public Map<String, Object> security() {
        return accessControlService.securityPosture();
    }

    @GetMapping("/domain-counts")
    public Map<String, Long> domainCounts() {
        return riskService.countsByDomain();
    }
}
