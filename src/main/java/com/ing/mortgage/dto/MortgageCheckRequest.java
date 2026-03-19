package com.ing.mortgage.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MortgageCheckRequest(@NotNull @DecimalMin(value = "0.01") BigDecimal income,
                                   @NotNull @DecimalMin(value = "0.01") BigDecimal loanAmount,
                                   @NotNull @DecimalMin(value = "0.01") BigDecimal homeValue,
                                   @Min(1) int maturityPeriod) {
}
