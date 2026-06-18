package com.global.taxreturnbackendapi.DTO;

import com.global.taxreturnbackendapi.entity.Income;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxReportResponseDto {
    private Double totalIncome;         // total income
    private Double totalExpense;        // total expense
    private Double expenseToIncomeRatio; //Spending to Income Ratio (%) - 소득대비 지출비용
    private Double expectedRefund;       // Expected refund amount - 예상 환급액

    // 💡 Expenditure statistics by category (e.g., "WORK" -> 1500.0, "TRAVEL" -> 500.0)
    private Map<String, Double> expenseByCategory;

    private String savingTip;
}
