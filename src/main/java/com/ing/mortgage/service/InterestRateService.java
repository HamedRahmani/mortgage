package com.ing.mortgage.service;

import com.ing.mortgage.dto.InterestRateResponse;
import com.ing.mortgage.exception.InterestRateNotFoundException;
import com.ing.mortgage.repository.InterestRateRepository;
import com.ing.mortgage.repository.model.InterestRateEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Service for retrieving mortgage interest rates.
 */
@Service
public class InterestRateService {

    private final InterestRateRepository repository;

    public InterestRateService(InterestRateRepository repository) {
        this.repository = repository;
    }

    public List<InterestRateResponse> getInterestRates() {
        return repository.findAllValidOn(LocalDate.now())
                .stream()
                .sorted(Comparator.comparingInt(InterestRateEntity::getMaturityPeriod))
                .map(this::mapToResponse)
                .toList();
    }

    public InterestRateResponse getInterestRateByMaturityPeriod(int maturityPeriod) {
        return repository.findByMaturityPeriodValidOn(maturityPeriod, LocalDate.now())
                .map(this::mapToResponse)
                .orElseThrow(() -> new InterestRateNotFoundException(maturityPeriod));
    }

    private InterestRateResponse mapToResponse(InterestRateEntity entity) {
        return new InterestRateResponse(
                entity.getMaturityPeriod(),
                entity.getInterestRate(),
                entity.getValidFrom(),
                entity.getValidTo()
        );
    }
}
