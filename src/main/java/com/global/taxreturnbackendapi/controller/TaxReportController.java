package com.global.taxreturnbackendapi.controller;

import com.global.taxreturnbackendapi.DTO.TaxReportResponseDto;
import com.global.taxreturnbackendapi.service.TaxReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tax reports API", description = "tax reports API")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class TaxReportController {
    private final TaxReportService taxReportService;

    @GetMapping("/tax")
    public TaxReportResponseDto getTaxReport() {
        return taxReportService.generateTaxReport();
    }
}
