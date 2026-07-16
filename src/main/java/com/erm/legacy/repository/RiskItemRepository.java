package com.erm.legacy.repository;

import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskItemRepository extends JpaRepository<RiskItem, Long> {

    List<RiskItem> findByDomain(RiskDomain domain);

    List<RiskItem> findBySeverityIgnoreCase(String severity);
}
