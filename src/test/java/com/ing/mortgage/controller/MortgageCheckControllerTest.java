package com.ing.mortgage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import com.ing.mortgage.service.MortgageCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageCheckController.class)
class MortgageCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MortgageCheckService mortgageCheckService;

    @Test
    @DisplayName("Should return feasible mortgage check response")
    void checkMortgageFeasibility_ShouldReturnFeasibleResponse() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("100000"),
                new BigDecimal("300000"),
                new BigDecimal("400000"),
                10
        );
        var expectedResponse = new MortgageCheckResponse(true, new BigDecimal("3037.35"));

        when(mortgageCheckService.checkMortgageFeasibility(any(MortgageCheckRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCosts").value(3037.35));

        verify(mortgageCheckService, times(1)).checkMortgageFeasibility(any(MortgageCheckRequest.class));
    }

    @Test
    @DisplayName("Should return not feasible mortgage check response")
    void checkMortgageFeasibility_ShouldReturnNotFeasibleResponse() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("50000"),
                new BigDecimal("300000"),
                new BigDecimal("400000"),
                10
        );
        var expectedResponse = new MortgageCheckResponse(false, BigDecimal.ZERO);

        when(mortgageCheckService.checkMortgageFeasibility(any(MortgageCheckRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.feasible").value(false))
                .andExpect(jsonPath("$.monthlyCosts").value(0));

        verify(mortgageCheckService, times(1)).checkMortgageFeasibility(any(MortgageCheckRequest.class));
    }

    @Test
    @DisplayName("Should return bad request when income is invalid")
    void checkMortgageFeasibility_ShouldReturnBadRequest_WhenIncomeIsInvalid() throws Exception {
        var invalidRequest = """
                {
                    "income": 0,
                    "loanAmount": 300000,
                    "homeValue": 400000,
                    "maturityPeriod": 10
                }
                """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(mortgageCheckService, never()).checkMortgageFeasibility(any());
    }

    @Test
    @DisplayName("Should return bad request when maturity period is less than 1")
    void checkMortgageFeasibility_ShouldReturnBadRequest_WhenMaturityPeriodInvalid() throws Exception {
        var invalidRequest = """
                {
                    "income": 100000,
                    "loanAmount": 300000,
                    "homeValue": 400000,
                    "maturityPeriod": 0
                }
                """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(mortgageCheckService, never()).checkMortgageFeasibility(any());
    }
}

