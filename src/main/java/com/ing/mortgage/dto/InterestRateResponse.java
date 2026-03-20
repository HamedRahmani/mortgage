package com.ing.mortgage.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InterestRateResponse(
        int maturityPeriod,
        BigDecimal interestRate,
        LocalDate validFrom,
        LocalDate validTo) {
}
