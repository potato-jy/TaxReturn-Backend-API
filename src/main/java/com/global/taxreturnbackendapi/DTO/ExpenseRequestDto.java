package com.global.taxreturnbackendapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequestDto {
    @NotNull(message = "The expenditure amount is a required field.")
    @Positive(message = "The expenditure amount must be greater than 0.")
    private Double amount;

    @NotNull(message = "The expenditure date is a required field.")
    private LocalDate date;

    @NotBlank(message = "Categories cannot be empty.")
    private String category;

    private String description;

    private String note;

}
