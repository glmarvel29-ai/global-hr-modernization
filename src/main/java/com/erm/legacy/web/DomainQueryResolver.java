package com.erm.legacy.web;

import com.erm.legacy.model.RiskDomain;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DomainQueryResolver {

    public RiskDomain resolve(String rawValue, String fieldName) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return null;
        }

        String normalized = rawValue.trim();
        for (RiskDomain domain : RiskDomain.values()) {
            if (domain.name().equalsIgnoreCase(normalized)
                    || domain.getCode().equalsIgnoreCase(normalized)
                    || domain.getLabel().equalsIgnoreCase(normalized)) {
                return domain;
            }
        }

        throw new InvalidDomainException(fieldName, normalized, allowedValues());
    }

    private List<String> allowedValues() {
        List<String> allowed = new ArrayList<String>();
        for (RiskDomain domain : RiskDomain.values()) {
            allowed.add(domain.name());
            allowed.add(domain.getCode());
        }
        return allowed;
    }
}
