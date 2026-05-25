package com.global.taxreturnbackendapi.DTO;

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
    private String description;
    private Double amount;
    private LocalDate date;
    private String category;
    private String note;
}
