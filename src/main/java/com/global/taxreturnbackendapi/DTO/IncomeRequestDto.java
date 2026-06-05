package com.global.taxreturnbackendapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeRequestDto {
    @NotNull(message = "Income amount is a required field.")
    @Positive(message = "Income amount must be greater than 0.")
    private Double amount;

    @NotNull(message = "Income date is a required field.")
    private LocalDate date;

    @NotBlank(message = "income source cannot be empty.")
    private String source;

    private String note;
}