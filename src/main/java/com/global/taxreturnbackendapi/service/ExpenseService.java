package com.global.taxreturnbackendapi.service;

import com.global.taxreturnbackendapi.DTO.ExpenseRequestDto;
import com.global.taxreturnbackendapi.DTO.ExpenseResponseDto;
import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseResponseDto saveExpense(ExpenseRequestDto expenseRequestDto) {

        Expense expense = Expense.builder()
                .description(expenseRequestDto.getDescription())
                .category(expenseRequestDto.getCategory())
                .note(expenseRequestDto.getNote())
                .amount(expenseRequestDto.getAmount())
                .date(expenseRequestDto.getDate())
                            .build();
        Expense savedExpense = expenseRepository.save(expense);
        return getExpenseResponseDto(savedExpense);
    }

    public List<ExpenseResponseDto> getAllExpenses() {

        return expenseRepository.findAll().stream()
                .map(this::getExpenseResponseDto)
                .collect(Collectors.toList());
    }

    public Double calculateTotalAmount() {
        return expenseRepository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Calculation of estimated tax refund applying category-specific deduction rates
     * (amount spent * Deduction rate by category) * basic tax rate(30%)
     */
    public Double calculateExpectedRefund() {
        double baseTaxRate = 0.30; // Assuming an average Australian tax rate of 30%

        double totalDeduction = expenseRepository.findAll().stream()
                .mapToDouble(expense -> {
                    double amount = expense.getAmount();
                    String category = expense.getCategory().toUpperCase();
                    log.debug("현재 도는 데이터 카테고리: {}", expense.getCategory());
                    switch (category) {
                        case "WORK":
                            return amount * 1.0;  // 100%
                        case "TRAVEL":
                            return amount * 0.8;  // 80%
                        case "EDUCATION":
                            return amount * 0.7;  // 70%
                        default:
                            return amount * 0.5;  // 50%
                    }
                })
                .sum();
        return totalDeduction * baseTaxRate;
    }

    private ExpenseResponseDto getExpenseResponseDto(Expense expense) {
        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .note(expense.getNote())
                .description(expense.getDescription())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .build();
    }

    @org.springframework.transaction.annotation.Transactional
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto requestDto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not exist applicable expense details id=" + id));

        expense.update(
                requestDto.getDescription(),
                requestDto.getAmount(),
                requestDto.getDate(),
                requestDto.getCategory(),
                requestDto.getNote()
        );
        return getExpenseResponseDto(expense);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not exist expense history id=" + id));

        expenseRepository.delete(expense);
    }
}