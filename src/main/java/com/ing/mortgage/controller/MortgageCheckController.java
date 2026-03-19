package com.ing.mortgage.controller;


import com.ing.mortgage.dto.MortgageCheckRequest;
import com.ing.mortgage.dto.MortgageCheckResponse;
import com.ing.mortgage.service.MortgageCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mortgage-check")
@Tag(name = "Mortgage Check", description = "APIs for checking mortgage feasibility")
public class MortgageCheckController {
    private final MortgageCheckService mortgageCheckService;

    public MortgageCheckController(MortgageCheckService mortgageCheckService) {
        this.mortgageCheckService = mortgageCheckService;
    }

    @PostMapping
    @Operation(summary = "Check mortgage feasibility",
            description = "Validates if a mortgage is feasible based on income, loan value, and home value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mortgage check completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Interest rate for the specified maturity period not found")
    })
    public ResponseEntity<MortgageCheckResponse> checkMortgageFeasibility(@Valid @RequestBody MortgageCheckRequest request) {
        return ResponseEntity.ok(mortgageCheckService.checkMortgageFeasibility(request));
    }
}
