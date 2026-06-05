package com.global.taxreturnbackendapi.controller;

import com.global.taxreturnbackendapi.DTO.IncomeRequestDto;
import com.global.taxreturnbackendapi.DTO.IncomeResponseDto;
import com.global.taxreturnbackendapi.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Income API", description = "Income CRUD API")
@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @Operation(summary = "Register your income details")
    @PostMapping
    public IncomeResponseDto createIncome(@Valid  @RequestBody IncomeRequestDto requestDto) {
        return incomeService.createIncome(requestDto);
    }

    @Operation(summary = "View all income details")
    @GetMapping
    public List<IncomeResponseDto> getAllIncomes() {
        return incomeService.getAllIncomes();
    }

    @Operation(summary = "Edit your income details")
    @PutMapping("/{id}")
    public IncomeResponseDto updateIncome(@Valid @PathVariable Long id, @RequestBody IncomeRequestDto requestDto) {
        return incomeService.updateIncome(id, requestDto);
    }

    @Operation(summary = "Delete income history")
    @DeleteMapping("/{id}")
    public String deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return "Income history has been deleted. ID: " + id;
    }
}