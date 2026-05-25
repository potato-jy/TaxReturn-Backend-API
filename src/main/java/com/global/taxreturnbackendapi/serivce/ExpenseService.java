package com.global.taxreturnbackendapi.serivce;

import com.global.taxreturnbackendapi.DTO.ExpenseRequestDto;
import com.global.taxreturnbackendapi.DTO.ExpenseResponseDto;
import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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