package com.global.taxreturnbackendapi.service;

import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.entity.Income;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import com.global.taxreturnbackendapi.repository.IncomeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    @DisplayName("Verify whether the 15% dynamic tax rate is properly applied when total income is $50,000 or less")
    void calculateExpectedRefund_LowIncome_Apply15Percent() {
        // given [Preparation]: Fake income data (Total $40,000 -> 15% range)
        Income income = Income.builder().amount(40000.0).date(LocalDate.now()).source("Salary").build();

        // given [Preparation]: Fake expense data (WORK category $1,000 -> 100% recognized -> Total deduction $1,000)
        Expense expense = Expense.builder().amount(1000.0).date(LocalDate.now()).category("WORK").build();

        when(incomeRepository.findAll()).thenReturn(List.of(income));
        when(expenseRepository.findAll()).thenReturn(List.of(expense));

        Double finalRefund = expenseService.calculateExpectedRefund();

        // then [Verification]: $1,000 (deduction) * 0.15 (tax rate) = $150.0
        assertThat(finalRefund).isEqualTo(150.0);
    }

    @Test
    @DisplayName("Verify whether a bracket jump to a 30% dynamic tax rate occurs when total income exceeds $50,000.")
    void calculateExpectedRefund_MiddleIncome_Apply30Percent() {
        // given [Preparation]: Fake income data (Total $80,000 -> 30% range)
        Income income = Income.builder().amount(80000.0).date(LocalDate.now()).source("Contractor").build();

        // given [Preparation]: Fake expense data (WORK category $1,000 -> Total deduction $1,000)
        Expense expense = Expense.builder().amount(1000.0).date(LocalDate.now()).category("WORK").build();

        when(incomeRepository.findAll()).thenReturn(List.of(income));
        when(expenseRepository.findAll()).thenReturn(List.of(expense));

        Double finalRefund = expenseService.calculateExpectedRefund();

        // then [Verification]: $1,000 (deduction) * 0.30 (tax rate) = $300.0
        assertThat(finalRefund).isEqualTo(300.0);
    }
}