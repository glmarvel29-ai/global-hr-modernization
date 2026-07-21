package com.erm.legacy.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ErmUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users;

    public ErmUserDetailsService(PasswordEncoder passwordEncoder) {
        users = new HashMap<String, UserDetails>();
        users.put("admin", User.withUsername("admin")
                .password(passwordEncoder.encode("demo123"))
                .roles("ADMIN")
                .build());
        users.put("analyst", User.withUsername("analyst")
                .password(passwordEncoder.encode("demo123"))
                .roles("RISK_ANALYST")
                .build());
        users.put("auditor", User.withUsername("auditor")
                .password(passwordEncoder.encode("demo123"))
                .roles("INTERNAL_AUDIT")
                .build());
        users.put("vendor", User.withUsername("vendor")
                .password(passwordEncoder.encode("demo123"))
                .roles("VENDOR_ANALYST")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = users.get(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        return userDetails;
    }
}
