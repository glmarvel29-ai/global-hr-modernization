package com.erm.legacy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthIsPublic() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void risksRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/risks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"RISK_ANALYST"})
    void analystCanReadRisks() throws Exception {
        mockMvc.perform(get("/api/risks"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"RISK_ANALYST"})
    void invalidDomainReturns400WithContract() throws Exception {
        mockMvc.perform(get("/api/risks").param("domain", "NOT_A_DOMAIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_DOMAIN"))
                .andExpect(jsonPath("$.field").value("domain"))
                .andExpect(jsonPath("$.path").value("/api/risks"));
    }

    @Test
    @WithMockUser(username = "vendor", roles = {"VENDOR_ANALYST"})
    void vendorCannotReadIntegrations() throws Exception {
        mockMvc.perform(get("/api/integrations"))
                .andExpect(status().isForbidden());
    }
}
