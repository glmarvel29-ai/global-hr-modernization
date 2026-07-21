package com.erm.legacy.service;

import com.erm.legacy.legacy.LegacyChallengeRegistry;
import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.repository.RiskItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {

    @Mock
    private RiskItemRepository repository;

    @Mock
    private LegacyChallengeRegistry challengeRegistry;

    @InjectMocks
    private RiskService riskService;

    @Test
    void countsByDomainUsesSingleAggregationAndZeroFillsMap() {
        when(repository.countGroupByDomain()).thenReturn(Arrays.<Object[]>asList(
                new Object[]{RiskDomain.CYBER_RISK, 2L},
                new Object[]{RiskDomain.INTERNAL_AUDIT, 1L}
        ));

        Map<String, Long> counts = riskService.countsByDomain();

        assertEquals(RiskDomain.values().length, counts.size());
        assertEquals(Long.valueOf(2L), counts.get("CYBER_RISK"));
        assertEquals(Long.valueOf(1L), counts.get("INTERNAL_AUDIT"));
        assertEquals(Long.valueOf(0L), counts.get("VENDOR_RISK_MANAGEMENT"));

        verify(repository, times(1)).countGroupByDomain();
        verify(repository, never()).findByDomain(any(RiskDomain.class));
    }
}
