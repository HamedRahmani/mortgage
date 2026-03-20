package com.ing.mortgage.dto;

import java.math.BigDecimal;

public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCosts) {
}
