package com.global.taxreturnbackendapi.controller;

import com.global.taxreturnbackendapi.DTO.ExpenseRequestDto;
import com.global.taxreturnbackendapi.DTO.ExpenseResponseDto;
import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import com.global.taxreturnbackendapi.serivce.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Expense history save", description = "Saves work-related expense details")
    @PostMapping
    public ExpenseResponseDto createExpense(@RequestBody ExpenseRequestDto expenseRequestDto) {
        return expenseService.saveExpense(expenseRequestDto);
    }

    @Operation(summary = "view all expenditure details", description = "Retrieves saved all expense lists")
    @GetMapping
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @Operation(summary = "view total expense sum", description = "return the sum of all expense saved")
    @GetMapping("/total")
    public Double getTotalAmount() {
        return expenseService.calculateTotalAmount();
    }

    @Operation(summary = "edit expense details", description = "edit previsouly saved expense details")
    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto updateDto) {
        return expenseService.updateExpense(id, updateDto);
    }

    @Operation(summary = "delete expense history", description = "delete specific expense history")
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "deleted expense history successfully. ID: " + id;
    }
}