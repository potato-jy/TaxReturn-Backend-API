package com.global.taxreturnbackendapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // default Constructor for JPA
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description; // expenditure history (ex: MacBook Pro for Work)

    @Column(nullable = false)
    private Double amount;      // expenditure money

    @Column(nullable = false)
    private LocalDate date;     // expenditure date

    @Column(nullable = false)
    private String category;    // category (Work, Travel, Education..)

    // memo function good  to later tax
    private String note;

    public void update(String description, Double amount, LocalDate date, String category, String note) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
    }
}


