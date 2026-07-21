package com.erm.legacy.service;

import com.erm.legacy.legacy.LegacyChallengeRegistry;
import com.erm.legacy.legacy.SharedBusinessServices;
import com.erm.legacy.model.LegacyChallenge;
import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import com.erm.legacy.model.ScaleMetrics;
import com.erm.legacy.repository.RiskItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiskService {

    private final RiskItemRepository repository;
    private final LegacyChallengeRegistry challengeRegistry;

    public RiskService(RiskItemRepository repository, LegacyChallengeRegistry challengeRegistry) {
        this.repository = repository;
        this.challengeRegistry = challengeRegistry;
    }

    @Transactional(readOnly = true)
    public List<RiskItem> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RiskItem> findByDomain(RiskDomain domain) {
        return repository.findByDomain(domain);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> countsByDomain() {
        Map<String, Long> counts = new HashMap<String, Long>();
        for (RiskDomain domain : RiskDomain.values()) {
            counts.put(domain.name(), 0L);
        }
        List<Object[]> groupedCounts = repository.countGroupByDomain();
        for (Object[] row : groupedCounts) {
            if (row != null && row.length == 2 && row[0] instanceof RiskDomain && row[1] instanceof Number) {
                RiskDomain domain = (RiskDomain) row[0];
                counts.put(domain.name(), ((Number) row[1]).longValue());
            }
        }
        return counts;
    }

    public ScaleMetrics scaleMetrics() {
        // Complexity table scale, with demo LOC capped (<5K)
        return new ScaleMetrics(
                "15+ Years",
                "< 5,000 LOC (this demo)",
                "~2.2 Million LOC (production estate)",
                RiskDomain.values().length,
                "9 demo modules / 250+ microservices in production",
                "REST controllers here / 4,500+ APIs in production",
                "1 risk_register table / 8,000+ tables in production",
                "100+ compliance frameworks (represented on UI)",
                "9 demo connectors / 300+ integrations in production",
                "Global Multi-Cloud (dual Oracle + PostgreSQL dialects)"
        );
    }

    public List<LegacyChallenge> legacyChallenges() {
        return challengeRegistry.all();
    }

    public String dialectForRegion(String region) {
        return SharedBusinessServices.resolveTenantDialect(region);
    }

    public RiskDomain[] domains() {
        return RiskDomain.values();
    }
}
