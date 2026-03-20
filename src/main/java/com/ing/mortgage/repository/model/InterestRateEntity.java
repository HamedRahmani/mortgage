package com.ing.mortgage.repository.model;


import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "interest_rates")
public class InterestRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maturity_period", nullable = false)
    private Integer maturityPeriod;
    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;
    @Column(name = "valid_to")
    private LocalDate validTo;

    // Default constructor for JPA
    protected InterestRateEntity() {
    }

    public InterestRateEntity(Integer maturityPeriod, BigDecimal interestRate, LocalDate validFrom, LocalDate validTo) {
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Long getId() {
        return id;
    }

    public Integer getMaturityPeriod() {
        return maturityPeriod;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }
}
