package com.erm.legacy.legacy;

import com.erm.legacy.model.LegacyChallenge;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Exposes Technical Challenges column to services/controllers for the UI.
 * LEGACY PROBLEM: This catalog itself became a "shared business service" dependency.
 */
@Component
public class LegacyChallengeRegistry {

    public List<LegacyChallenge> all() {
        return Collections.unmodifiableList(Arrays.asList(
                new LegacyChallenge("Multiple acquired products",
                        LegacyDebtCatalog.ACQUIRED_PRODUCTS, "HIGH"),
                new LegacyChallenge("Hybrid monolithic and microservices architecture",
                        LegacyDebtCatalog.HYBRID_ARCHITECTURE, "CRITICAL"),
                new LegacyChallenge("Shared business services",
                        LegacyDebtCatalog.SHARED_BUSINESS_SERVICES, "HIGH"),
                new LegacyChallenge("High code dependencies",
                        LegacyDebtCatalog.HIGH_DEPENDENCIES, "MEDIUM"),
                new LegacyChallenge("Multiple deployment models",
                        LegacyDebtCatalog.MULTIPLE_DEPLOYMENTS, "HIGH"),
                new LegacyChallenge("Legacy frameworks",
                        LegacyDebtCatalog.LEGACY_FRAMEWORKS, "MEDIUM")
        ));
    }
}
