package com.global.taxreturnbackendapi.controller;

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
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseService.saveExpense(expense);
    }

    @Operation(summary = "view all expenditure details", description = "Retrieves saved all expense lists")
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @Operation(summary = "view total expense sum", description = "return the sum of all expense saved")
    @GetMapping("/total")
    public Double getTotalAmount() {
        return expenseService.calculateTotalAmount();
    }
}