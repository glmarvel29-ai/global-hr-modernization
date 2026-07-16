package com.erm.legacy.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {
    @Autowired private MockMvc mockMvc;
    @Test void health_returnsUpStatus() throws Exception {
        mockMvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status", is("UP")));
    }
    @Test void risks_returnsSeededRegister() throws Exception {
        mockMvc.perform(get("/api/risks")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(9))));
    }
    @Test void risks_filtersByDomainCode() throws Exception {
        mockMvc.perform(get("/api/risks").param("domain", "CYB")).andExpect(status().isOk()).andExpect(jsonPath("$[*].domain", everyItem(is("CYBER_RISK"))));
    }
    @Test void risks_unknownDomainReturnsAll() throws Exception {
        mockMvc.perform(get("/api/risks").param("domain", "NOT_A_DOMAIN")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(9))));
    }
    @Test void domains_returnsNineDomains() throws Exception {
        mockMvc.perform(get("/api/domains")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(9)));
    }
    @Test void securityPosture_returnsZeroTrustFlags() throws Exception {
        mockMvc.perform(get("/api/security/posture")).andExpect(status().isOk()).andExpect(jsonPath("$.zeroTrust", is(true)));
    }
    @Test void domainCounts_returnsNonZeroTotals() throws Exception {
        mockMvc.perform(get("/api/domain-counts")).andExpect(status().isOk()).andExpect(jsonPath("$.['Cyber Risk']", greaterThanOrEqualTo(1)));
    }
}
