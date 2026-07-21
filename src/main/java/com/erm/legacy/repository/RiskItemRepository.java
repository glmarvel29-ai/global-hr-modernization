package com.erm.legacy.repository;

import com.erm.legacy.model.RiskDomain;
import com.erm.legacy.model.RiskItem;
import com.erm.legacy.model.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RiskItemRepository extends JpaRepository<RiskItem, Long> {

    List<RiskItem> findByDomain(RiskDomain domain);

    List<RiskItem> findBySeverity(Severity severity);

    @Query("SELECT r.domain, COUNT(r) FROM RiskItem r GROUP BY r.domain")
    List<Object[]> countGroupByDomain();
}
