package com.ing.mortgage.service;

import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service for evaluating mortgage feasibility and calculating monthly payments.
 */
@Service
public class MortgageCheckService {

    private static final int MONTHS_IN_YEAR = 12;
    private static final int SCALE_10 = 10;
    private static final int SCALE_2 = 2;
    private static final BigDecimal TWELVE = BigDecimal.valueOf(MONTHS_IN_YEAR);
    private static final BigDecimal INCOME_MULTIPLIER = BigDecimal.valueOf(4);

    private final InterestRateService interestRateService;

    public MortgageCheckService(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    public MortgageCheckResponse checkMortgageFeasibility(MortgageCheckRequest request) {
        var feasible = isFeasible(request);
        if (!feasible) {
            return new MortgageCheckResponse(false, BigDecimal.ZERO);
        }

        var interestRateResponse = interestRateService.getInterestRateByMaturityPeriod(request.maturityPeriod());
        var monthlyCosts = calculateMonthlyCosts(request.loanAmount(), interestRateResponse.interestRate(), request.maturityPeriod());
        return new MortgageCheckResponse(true, monthlyCosts);
    }

    /**
     * Checks if the mortgage is feasible based on business rules:
     * - Loan amount must not exceed 4x annual income
     * - Loan amount must not exceed home value
     */
    private boolean isFeasible(MortgageCheckRequest request) {
        var maxLoanBasedOnIncome = request.income().multiply(INCOME_MULTIPLIER);
        var maxLoanBasedOnHomeValue = request.homeValue();

        return request.loanAmount().compareTo(maxLoanBasedOnIncome) <= 0 &&
                request.loanAmount().compareTo(maxLoanBasedOnHomeValue) <= 0;
    }

    private BigDecimal calculateMonthlyCosts(BigDecimal principal, BigDecimal interestRate, int maturityPeriod) {
        var annualRate = convertPercentageToDecimal(interestRate);
        var monthlyRate = annualRate.divide(TWELVE, SCALE_10, RoundingMode.HALF_UP);

        var totalMonths = maturityPeriod * MONTHS_IN_YEAR;

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(totalMonths), SCALE_2, RoundingMode.HALF_UP);
        }

        return calculateAmortizedPayment(principal, monthlyRate, totalMonths);
    }

    /**
     * Converts a percentage value to its decimal representation.
     *
     * @param percentage The percentage value (e.g., 4.5 for 4.5%)
     * @return The decimal representation (e.g., 0.045)
     */
    private BigDecimal convertPercentageToDecimal(BigDecimal percentage) {
        return percentage.divide(BigDecimal.valueOf(100), SCALE_10, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the monthly payment using the standard amortization formula.
     * Formula: M = P × [r(1 + r)^n] / [(1 + r)^n - 1]
     *
     * @param principal   The loan amount
     * @param monthlyRate The monthly interest rate as a decimal
     * @param totalMonths The total number of payment periods
     * @return The monthly payment amount rounded to 2 decimal places
     */
    private BigDecimal calculateAmortizedPayment(BigDecimal principal, BigDecimal monthlyRate, int totalMonths) {

        var onePlusRate = BigDecimal.ONE.add(monthlyRate);

        var compoundFactor = onePlusRate.pow(totalMonths);// Calculate (1 + r)^n

        // Calculate numerator: r × (1 + r)^n
        var numerator = monthlyRate.multiply(compoundFactor);

        // Calculate denominator: (1 + r)^n - 1
        var denominator = compoundFactor.subtract(BigDecimal.ONE);

        // Calculate final monthly payment: P × [numerator / denominator]
        return principal.multiply(numerator.divide(denominator, SCALE_10, RoundingMode.HALF_UP))
                .setScale(SCALE_2, RoundingMode.HALF_UP);
    }
}
