package com.global.taxreturnbackendapi.controller;

import com.global.taxreturnbackendapi.DTO.ExpenseRequestDto;
import com.global.taxreturnbackendapi.DTO.ExpenseResponseDto;
import com.global.taxreturnbackendapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expense API", description = "Expense CRUD API")
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Expense history save", description = "Saves work-related expense details")
    @PostMapping
    public ExpenseResponseDto createExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto) {
        return expenseService.saveExpense(expenseRequestDto);
    }

    @Operation(summary = "view all expenditure details", description = "Retrieves saved all expense lists")
    @GetMapping
    public List<ExpenseResponseDto> getAllExpenses(@RequestParam(required = false) String category) {
        return expenseService.getAllExpenses(category);
    }

    @Operation(summary = "view total expense sum", description = "return the sum of all expense saved")
    @GetMapping("/total")
    public Double getTotalAmount() {
        return expenseService.calculateTotalAmount();
    }

    @Operation(summary = "edit expense details", description = "edit previsouly saved expense details")
    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDto updateDto) {
        return expenseService.updateExpense(id, updateDto);
    }

    @Operation(summary = "delete expense history", description = "delete specific expense history")
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "deleted expense history successfully. ID: " + id;
    }

    @Operation(summary = "Check Estimated Tax Refund", description = "Calculate the estimated refund by applying the category-specific deduction rate and tax rate (30%) of the saved expenses..")
    @GetMapping("/expected-refund")
    public Double getExpectedRefund() {
        return expenseService.calculateExpectedRefund();
    }
}