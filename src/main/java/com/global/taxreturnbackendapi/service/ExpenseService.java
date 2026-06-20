package com.global.taxreturnbackendapi.service;

import com.global.taxreturnbackendapi.DTO.ExpenseRequestDto;
import com.global.taxreturnbackendapi.DTO.ExpenseResponseDto;
import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import com.global.taxreturnbackendapi.repository.IncomeRepository;
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

    private final IncomeRepository incomeRepository;

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

    public List<ExpenseResponseDto> getAllExpenses(String category) {

        List<Expense> expenses;

        if (category != null && !category.trim().isEmpty()) {
            expenses = expenseRepository.findByCategoryOrderByDateDesc(category);
        } else {
            expenses = expenseRepository.findAllByOrderByDateDesc();
        }

        return expenses.stream()
                .map(expense -> ExpenseResponseDto.builder()
                        .id(expense.getId())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .category(expense.getCategory())
                        .description(expense.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public Double calculateTotalAmount() {
        return expenseRepository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Calculation of refund amount based on dynamic tax rates according to total income brackets
     *
     */
    public Double calculateExpectedRefund() {

        double totalIncome = incomeRepository.findAll().stream()
                .mapToDouble(com.global.taxreturnbackendapi.entity.Income::getAmount)
                .sum();

        double dynamicTaxRate;
        if (totalIncome <= 50000) {
            dynamicTaxRate = 0.15; // 15%
        } else if (totalIncome <= 100000) {
            dynamicTaxRate = 0.30; // 30%
        } else {
            dynamicTaxRate = 0.45; // 45%
        }

        double totalDeduction = expenseRepository.findAll().stream()
                .mapToDouble(expense -> {
                    double amount = expense.getAmount();
                    String category = expense.getCategory().toUpperCase();
                    switch (category) {
                        case "WORK":      return amount * 1.0;
                        case "TRAVEL":    return amount * 0.8;
                        case "EDUCATION": return amount * 0.7;
                        default:          return amount * 0.5;
                    }
                })
                .sum();

        double finalRefund = totalDeduction * dynamicTaxRate;

        return finalRefund;
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