package com.global.taxreturnbackendapi.DTO;

import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeResponseDto {
    private Long id;
    private Double amount;
    private LocalDate date;
    private String source;
    private String note;
}