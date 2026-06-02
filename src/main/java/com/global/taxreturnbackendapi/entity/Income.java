package com.global.taxreturnbackendapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount; //income amount

    @Column(nullable = false)
    private LocalDate date; //Income accrual date

    @Column(nullable = false)
    private String source; // income source (ex: Salary, Contractor, Bonus ..)

    private String note;

    public void update(Double amount, LocalDate date, String source, String note) {
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.note = note;
    }
}