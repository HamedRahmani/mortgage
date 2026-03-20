package com.ing.mortgage.controller;

import com.ing.mortgage.dto.InterestRateResponse;
import com.ing.mortgage.service.InterestRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/interest-rates")
@Tag(name = "Interest Rates", description = "Interest rate management APIs")
public class InterestRateController {
    private final InterestRateService interestRateService;

    public InterestRateController(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    @GetMapping
    @Operation(summary = "Get all interest rates", description = "Returns a list of all available interest rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interest rates retrieved successfully")
    })
    public ResponseEntity<List<InterestRateResponse>> getInterestRates() {
        return ResponseEntity.ok(interestRateService.getInterestRates());
    }
}
