package com.erm.legacy.repository;

import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import com.erm.legacy.model.RiskStatus;
import com.erm.legacy.model.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RiskItemRepositoryTest {

    @Autowired
    private RiskItemRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        entityManager.persist(build("RG-1001", "Cyber drill failed", RiskDomain.CYBER_RISK, Severity.CRITICAL, RiskStatus.OPEN));
        entityManager.persist(build("RG-1002", "Cyber control aging", RiskDomain.CYBER_RISK, Severity.HIGH, RiskStatus.MITIGATING));
        entityManager.persist(build("AP-2001", "Audit backlog", RiskDomain.INTERNAL_AUDIT, Severity.MEDIUM, RiskStatus.ASSESSING));
        entityManager.flush();
    }

    @Test
    void findByDomainReturnsOnlyMatchingRecords() {
        List<RiskItem> cyber = repository.findByDomain(RiskDomain.CYBER_RISK);
        assertEquals(2, cyber.size());
    }

    @Test
    void countGroupByDomainAggregatesCounts() {
        List<Object[]> grouped = repository.countGroupByDomain();
        assertEquals(2, grouped.size());
    }

    @Test
    void enumValuesPersistAndLoadAsUppercaseEnums() {
        List<RiskItem> all = repository.findAll();
        assertEquals(3, all.size());
        assertTrue(all.stream().allMatch(item -> item.getSeverity() != null));
        assertTrue(all.stream().allMatch(item -> item.getStatus() != null));
    }

    private RiskItem build(String riskId, String title, RiskDomain domain, Severity severity, RiskStatus status) {
        RiskItem item = new RiskItem();
        item.setRiskId(riskId);
        item.setTitle(title);
        item.setDomain(domain);
        item.setSeverity(severity);
        item.setStatus(status);
        item.setOwner("test");
        item.setDescription("test description");
        return item;
    }
}
