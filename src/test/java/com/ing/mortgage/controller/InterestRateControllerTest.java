package com.ing.mortgage.controller;

import com.ing.mortgage.dto.InterestRateResponse;
import com.ing.mortgage.service.InterestRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InterestRateController.class)
class InterestRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterestRateService interestRateService;

    @Test
    @DisplayName("Should return list of interest rates with HTTP 200")
    void getInterestRates_ShouldReturnListOfRates() throws Exception {
        LocalDate validFrom = LocalDate.of(2026, 1, 1);
        var sampleRates = List.of(
                new InterestRateResponse(5, new BigDecimal("4.00"), validFrom),
                new InterestRateResponse(10, new BigDecimal("4.25"), validFrom),
                new InterestRateResponse(20, new BigDecimal("4.75"), validFrom)
        );
        when(interestRateService.getInterestRates()).thenReturn(sampleRates);

        mockMvc.perform(get("/api/interest-rates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].maturityPeriod").value(5))
                .andExpect(jsonPath("$[0].interestRate").value(4.00))
                .andExpect(jsonPath("$[0].validFrom").value("2026-01-01"))
                .andExpect(jsonPath("$[1].maturityPeriod").value(10))
                .andExpect(jsonPath("$[2].maturityPeriod").value(20));

        verify(interestRateService, times(1)).getInterestRates();
    }

    @Test
    @DisplayName("Should return empty list when no rates exist")
    void getInterestRates_ShouldReturnEmptyList_WhenNoRatesExist() throws Exception {
        when(interestRateService.getInterestRates()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/interest-rates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(interestRateService, times(1)).getInterestRates();
    }
}
