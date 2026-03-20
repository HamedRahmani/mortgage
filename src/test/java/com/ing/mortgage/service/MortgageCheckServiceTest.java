package com.ing.mortgage.service;

import com.ing.mortgage.dto.InterestRateResponse;
import com.ing.mortgage.dto.MortgageCheckRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageCheckServiceTest {

    @Mock
    private InterestRateService interestRateService;

    @InjectMocks
    private MortgageCheckService mortgageCheckService;

    private InterestRateResponse interestRateResponse;

    @BeforeEach
    void setUp() {
        interestRateResponse = new InterestRateResponse(10, new BigDecimal("4.25"), LocalDate.of(2026, 1, 1), null);
    }

    private MortgageCheckRequest createRequest(String income, String loanAmount, String homeValue, int maturityPeriod) {
        return new MortgageCheckRequest(
                new BigDecimal(income),
                new BigDecimal(loanAmount),
                new BigDecimal(homeValue),
                maturityPeriod
        );
    }

    @Test
    @DisplayName("Should return feasible when loan is within income and home value limits")
    void checkMortgageFeasibility_ShouldReturnFeasible_WhenWithinLimits() {
        var request = createRequest("100000", "300000", "400000", 10);
        when(interestRateService.getInterestRateByMaturityPeriod(10)).thenReturn(interestRateResponse);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertTrue(result.feasible());
        assertNotEquals(BigDecimal.ZERO, result.monthlyCosts());
        verify(interestRateService, times(1)).getInterestRateByMaturityPeriod(10);
    }

    @Test
    @DisplayName("Should return not feasible when loan exceeds income limit (4x income)")
    void checkMortgageFeasibility_ShouldReturnNotFeasible_WhenExceedsIncomeLimit() {
        var request = createRequest("50000", "300000", "400000", 10);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertFalse(result.feasible());
        assertEquals(BigDecimal.ZERO, result.monthlyCosts());
        verify(interestRateService, never()).getInterestRateByMaturityPeriod(anyInt());
    }

    @Test
    @DisplayName("Should return not feasible when loan exceeds home value")
    void checkMortgageFeasibility_ShouldReturnNotFeasible_WhenExceedsHomeValue() {
        var request = createRequest("200000", "500000", "400000", 10);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertFalse(result.feasible());
        assertEquals(BigDecimal.ZERO, result.monthlyCosts());
        verify(interestRateService, never()).getInterestRateByMaturityPeriod(anyInt());
    }

    @Test
    @DisplayName("Should calculate correct monthly costs for feasible mortgage")
    void checkMortgageFeasibility_ShouldCalculateCorrectMonthlyCosts() {
        var request = createRequest("100000", "200000", "300000", 10);
        when(interestRateService.getInterestRateByMaturityPeriod(10)).thenReturn(interestRateResponse);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertTrue(result.feasible());
        assertTrue(result.monthlyCosts().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should return feasible when loan equals exactly 4x income")
    void checkMortgageFeasibility_ShouldReturnFeasible_WhenLoanEqualsMaxIncomeLimit() {
        var request = createRequest("100000", "400000", "500000", 10);
        when(interestRateService.getInterestRateByMaturityPeriod(10)).thenReturn(interestRateResponse);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertTrue(result.feasible());
        assertNotEquals(BigDecimal.ZERO, result.monthlyCosts());
    }

    @Test
    @DisplayName("Should return feasible when loan equals home value")
    void checkMortgageFeasibility_ShouldReturnFeasible_WhenLoanEqualsHomeValue() {
        var request = createRequest("150000", "400000", "400000", 10);
        when(interestRateService.getInterestRateByMaturityPeriod(10)).thenReturn(interestRateResponse);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertTrue(result.feasible());
        assertNotEquals(BigDecimal.ZERO, result.monthlyCosts());
    }

    @Test
    @DisplayName("Should use different maturity periods")
    void checkMortgageFeasibility_ShouldUseDifferentMaturityPeriods() {
        var request = createRequest("100000", "200000", "300000", 20);
        var rate20Years = new InterestRateResponse(20, new BigDecimal("4.75"), LocalDate.of(2026, 1, 1), null);
        when(interestRateService.getInterestRateByMaturityPeriod(20)).thenReturn(rate20Years);

        var result = mortgageCheckService.checkMortgageFeasibility(request);

        assertTrue(result.feasible());
        verify(interestRateService, times(1)).getInterestRateByMaturityPeriod(20);
    }
}
