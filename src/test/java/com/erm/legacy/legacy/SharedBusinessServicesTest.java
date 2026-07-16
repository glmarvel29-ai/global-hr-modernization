package com.erm.legacy.legacy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SharedBusinessServicesTest {
    @Test void resolveTenantDialect_euRegionReturnsPostgresql() { assertEquals("POSTGRESQL", SharedBusinessServices.resolveTenantDialect("EU-WEST")); }
    @Test void resolveTenantDialect_usRegionReturnsOracle() { assertEquals("ORACLE", SharedBusinessServices.resolveTenantDialect("US-EAST")); }
    @Test void resolveTenantDialect_nullRegionReturnsOracle() { assertEquals("ORACLE", SharedBusinessServices.resolveTenantDialect(null)); }
    @Test void composeLegacyRiskKey_joinsPrefixAndNumericId() { assertEquals("RG-1001", SharedBusinessServices.composeLegacyRiskKey("RG", "1001")); }
}
