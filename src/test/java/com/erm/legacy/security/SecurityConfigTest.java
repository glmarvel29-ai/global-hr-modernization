package com.erm.legacy.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pageRoutesRequireAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginPageIsPublic() throws Exception {
        // MAD-133 AC-D06
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void h2ConsoleDeniedOnDefaultProfile() throws Exception {
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "auditor", roles = {"INTERNAL_AUDIT"})
    void auditorCanReadSecurityPosture() throws Exception {
        mockMvc.perform(get("/api/security/posture"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"RISK_ANALYST"})
    void analystCanReadRiskEndpoints() throws Exception {
        // MAD-133 AC-D03
        mockMvc.perform(get("/api/risks"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"RISK_ANALYST"})
    void analystCannotReadSecurityPosture() throws Exception {
        // MAD-133 AC-D05
        mockMvc.perform(get("/api/security/posture"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "vendor", roles = {"VENDOR_ANALYST"})
    void vendorCannotReadSecurityPosture() throws Exception {
        mockMvc.perform(get("/api/security/posture"))
                .andExpect(status().isForbidden());
    }
}
