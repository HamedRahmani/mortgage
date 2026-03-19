package com.ing.mortgage.service;

import com.ing.mortgage.exception.InterestRateNotFoundException;
import com.ing.mortgage.repository.InterestRateRepository;
import com.ing.mortgage.repository.model.InterestRateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestRateServiceTest {

    @Mock
    private InterestRateRepository repository;

    @InjectMocks
    private InterestRateService interestRateService;

    private InterestRateEntity entity1;
    private InterestRateEntity entity2;
    private InterestRateEntity entity3;
    private LocalDate validFrom;

    @BeforeEach
    void setUp() {
        validFrom = LocalDate.of(2026, 1, 1);
        entity1 = new InterestRateEntity(10, new BigDecimal("4.25"), validFrom, null);
        entity2 = new InterestRateEntity(5, new BigDecimal("4.00"), validFrom, null);
        entity3 = new InterestRateEntity(20, new BigDecimal("4.75"), validFrom, null);
    }

    @Test
    @DisplayName("Should return sorted list of interest rates")
    void getInterestRates_ShouldReturnSortedRates() {
        when(repository.findAllValidOn(any(LocalDate.class))).thenReturn(List.of(entity1, entity2, entity3));

        var result = interestRateService.getInterestRates();

        assertEquals(3, result.size());
        assertEquals(5, result.get(0).maturityPeriod());
        assertEquals(10, result.get(1).maturityPeriod());
        assertEquals(20, result.get(2).maturityPeriod());
        verify(repository, times(1)).findAllValidOn(any(LocalDate.class));
    }

    @Test
    @DisplayName("Should return empty list when no rates exist")
    void getInterestRates_ShouldReturnEmptyList_WhenNoRatesExist() {
        when(repository.findAllValidOn(any(LocalDate.class))).thenReturn(Collections.emptyList());

        var result = interestRateService.getInterestRates();

        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllValidOn(any(LocalDate.class));
    }

    @Test
    @DisplayName("Should return interest rate by maturity period")
    void getInterestRateByMaturityPeriod_ShouldReturnRate_WhenExists() {
        when(repository.findByMaturityPeriodValidOn(eq(10), any(LocalDate.class))).thenReturn(Optional.of(entity1));

        var result = interestRateService.getInterestRateByMaturityPeriod(10);

        assertNotNull(result);
        assertEquals(10, result.maturityPeriod());
        assertEquals(new BigDecimal("4.25"), result.interestRate());
        assertEquals(validFrom, result.validFrom());
        verify(repository, times(1)).findByMaturityPeriodValidOn(eq(10), any(LocalDate.class));
    }

    @Test
    @DisplayName("Should throw exception when maturity period not found")
    void getInterestRateByMaturityPeriod_ShouldThrowException_WhenNotFound() {
        when(repository.findByMaturityPeriodValidOn(eq(99), any(LocalDate.class))).thenReturn(Optional.empty());

        var exception = assertThrows(InterestRateNotFoundException.class,
                () -> interestRateService.getInterestRateByMaturityPeriod(99));

        assertEquals("Interest rate not found for maturity period: 99", exception.getMessage());
        verify(repository, times(1)).findByMaturityPeriodValidOn(eq(99), any(LocalDate.class));
    }

    @Test
    @DisplayName("Should correctly map entity to response")
    void getInterestRateByMaturityPeriod_ShouldMapEntityToResponse() {
        when(repository.findByMaturityPeriodValidOn(eq(5), any(LocalDate.class))).thenReturn(Optional.of(entity2));

        var result = interestRateService.getInterestRateByMaturityPeriod(5);

        assertEquals(entity2.getMaturityPeriod(), result.maturityPeriod());
        assertEquals(entity2.getInterestRate(), result.interestRate());
        assertEquals(entity2.getValidFrom(), result.validFrom());
    }
}
