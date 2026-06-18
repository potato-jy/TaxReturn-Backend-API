package com.global.taxreturnbackendapi.service;

import com.global.taxreturnbackendapi.DTO.TaxReportResponseDto;
import com.global.taxreturnbackendapi.entity.Expense;
import com.global.taxreturnbackendapi.entity.Income;
import com.global.taxreturnbackendapi.repository.ExpenseRepository;
import com.global.taxreturnbackendapi.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxReportService {
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;

    public TaxReportResponseDto generateTaxReport() {
        List<Income> incomes = incomeRepository.findAll();
        List<Expense> expenses = expenseRepository.findAll();

        // 1. total income sum
        double totalIncome = incomes.stream()
                .mapToDouble(Income::getAmount)
                .sum();

        // 2. total expense sum
        double totalExpense = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        // 3. Calculation of expenditure-to-income ratio (Exception handling to prevent NaN when income is 0)
        // 소득 대비 지출 비율 계산 (소득이 0일 때 NaN 방지 예외 처리)
        double ratio = totalIncome > 0 ? (totalExpense / totalIncome) * 100 : 0.0;
        // Round to the second decimal place
        ratio = (double) Math.round(ratio * 100) / 100;

        // 4. Import refund amount from existing calculator
        double expectedRefund = expenseService.calculateExpectedRefund();

        // 5. Grouping expenses by category (GroupingBy)
        Map<String, Double> expenseByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // 6. Information on generating customized tax-saving tips
        String savingTip = createSavingTip(expenseByCategory, expectedRefund);

        return TaxReportResponseDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .expenseToIncomeRatio(ratio)
                .expectedRefund(expectedRefund)
                .expenseByCategory(expenseByCategory)
                .savingTip(savingTip)
                .build();
    }

    // 💡 Simple statistical analysis-based tip maker
    private String createSavingTip(Map<String, Double> expenseByCategory, double expectedRefund) {
        double workExpense = expenseByCategory.getOrDefault("WORK", 0.0);

        if (expectedRefund == 0.0) {
            //현재 소득 구간에서는 환급받을 세금이 없습니다. 소득 증빙 내역을 다시 확인해 보세요.
            return "There is no tax refund available in your current income bracket. Please double-check your income verification details.";
        }
        if (workExpense == 0.0) {
            //일과 관련된 지출(WORK 카테고리) 항목이 없습니다. 호주에서는 업무용 자격증, 유니폼, 공구 구입비 등이 100% 소득 공제되니 적극 활용해 보세요!
            return "There are no work-related expenses (WORK category). In Australia, the cost of professional certifications, uniforms, tools, and more is 100% tax deductible, so be sure to take full advantage of this!";
        }
        //WORK 카테고리를 통해 소득 공제를 잘 이끌어내고 계십니다. 영수증 원본을 안전하게 보관하는 것을 잊지 마세요!
        return "You are effectively claiming income tax deductions through the WORK category. Don't forget to keep your original receipts safe!";
    }
}
