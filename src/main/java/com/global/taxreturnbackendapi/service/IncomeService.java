package com.global.taxreturnbackendapi.service;

import com.global.taxreturnbackendapi.DTO.IncomeRequestDto;
import com.global.taxreturnbackendapi.DTO.IncomeResponseDto;
import com.global.taxreturnbackendapi.entity.Income;
import com.global.taxreturnbackendapi.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    @Transactional
    public IncomeResponseDto createIncome(IncomeRequestDto requestDto) {

        Income income = Income.builder()
                .amount(requestDto.getAmount())
                .date(requestDto.getDate())
                .source(requestDto.getSource())
                .note(requestDto.getNote())
                .build();

        Income savedIncome = incomeRepository.save(income);
        return convertToResponseDto(savedIncome);
    }

    @Transactional(readOnly = true)
    public List<IncomeResponseDto> getAllIncomes() {
        return incomeRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Transactional
    public IncomeResponseDto updateIncome(Long id, IncomeRequestDto requestDto) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no relevant income history.id=" + id));

        income.update(
                requestDto.getAmount(),
                requestDto.getDate(),
                requestDto.getSource(),
                requestDto.getNote()
        );
        return convertToResponseDto(income);
    }

    @Transactional
    public void deleteIncome(Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no relevant income history. id=" + id));
        incomeRepository.delete(income);
    }

    private IncomeResponseDto convertToResponseDto(Income income) {
        return IncomeResponseDto.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .date(income.getDate())
                .source(income.getSource())
                .note(income.getNote())
                .build();
    }
}