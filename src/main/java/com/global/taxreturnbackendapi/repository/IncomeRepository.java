package com.global.taxreturnbackendapi.repository;

import com.global.taxreturnbackendapi.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
