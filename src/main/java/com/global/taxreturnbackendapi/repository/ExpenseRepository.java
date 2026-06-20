package com.global.taxreturnbackendapi.repository;

import com.global.taxreturnbackendapi.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // SQL: SELECT * FROM expense WHERE category = ? ORDER BY date DESC
    List<Expense> findByCategoryOrderByDateDesc(String category);

    // SQL: SELECT * FROM expense ORDER BY date DESC
    List<Expense> findAllByOrderByDateDesc();
}
