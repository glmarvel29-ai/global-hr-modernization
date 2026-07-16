package com.erm.legacy.legacy;

/**
 * Central catalog of Technical Challenges from the Complexity table.
 *
 * These comments and stubs are intentional — the demo must surface legacy debt
 * visibly in the codebase and on the running UI.
 *
 * Production context (scaled down here to &lt;5K LOC):
 *   ~2.2M LOC | 30+ modules | 250+ microservices | 4,500+ APIs | 8,000+ tables
 */
public final class LegacyDebtCatalog {

    private LegacyDebtCatalog() {
    }

    /**
     * LEGACY PROBLEM #1 — Multiple acquired products.
     * RiskGuard (2012), AuditPro (2015), VendorSight (2018) were never fully
     * homogenized. Shared package names collide; ID formats differ per product.
     */
    public static final String ACQUIRED_PRODUCTS =
            "Multiple acquired products (RiskGuard / AuditPro / VendorSight) stitched "
                    + "with adapter layers; domain codes and identity formats still diverge.";

    /**
     * LEGACY PROBLEM #2 — Hybrid monolithic and microservices architecture.
     * Core ERM remains a Spring MVC monolith; cyber/vendor risk were extracted to
     * services but still call into the monolith via shared business services.
     */
    public static final String HYBRID_ARCHITECTURE =
            "Hybrid monolithic + microservices: monolith owns risk_register; sidecar "
                    + "services call SharedBusinessServices facade (circular affinity risk).";

    /**
     * LEGACY PROBLEM #3 — Shared business services.
     * Single jar (erm-shared-biz) pulled by 40+ deployables — classic dependency hell.
     */
    public static final String SHARED_BUSINESS_SERVICES =
            "Shared business services jar is a bottleneck: version skew across regions "
                    + "causes intermittent ClassNotFoundException in multi-cloud rollouts.";

    /**
     * LEGACY PROBLEM #4 — High code dependencies.
     * Spring MVC 4-era interceptors, legacy AngularJS 1.5 directives, and JSP taglibs
     * cross-import each other.
     */
    public static final String HIGH_DEPENDENCIES =
            "High code dependencies: JSP includes AngularJS partials that call Spring MVC "
                    + "controllers that re-enter JSP fragment tags — tight coupling.";

    /**
     * LEGACY PROBLEM #5 — Multiple deployment models.
     * WAR on Tomcat, EAR on WebLogic (legacy), containers on EKS/AKS, and batch JARs.
     */
    public static final String MULTIPLE_DEPLOYMENTS =
            "Multiple deployment models across Global Multi-Cloud (WAR/EAR/K8s) with "
                    + "inconsistent config property keys per region.";

    /**
     * LEGACY PROBLEM #6 — Legacy frameworks.
     * Java 8 + Spring MVC + JSP + AngularJS remain mandated for parity with the
     * Complexity-table Application Complexity column.
     */
    public static final String LEGACY_FRAMEWORKS =
            "Legacy frameworks locked: Java 8, Spring MVC, JSP, AngularJS 1.x; dual "
                    + "Oracle + PostgreSQL dialects; upgrade blocked by acquired product contracts.";
}
