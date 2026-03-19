package com.ing.mortgage.repository;

import com.ing.mortgage.repository.model.InterestRateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
class InterestRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InterestRateRepository interestRateRepository;

    private LocalDate today;
    private LocalDate validFrom;

    @BeforeEach
    void setUp() {
        today = LocalDate.of(2026, 3, 19);
        validFrom = LocalDate.of(2026, 1, 1);
    }

    @Test
    @DisplayName("Should save and retrieve interest rate entity")
    void shouldSaveAndRetrieveEntity() {
        var entity = new InterestRateEntity(15, new BigDecimal("4.50"), validFrom, null);

        entityManager.persistAndFlush(entity);
        entityManager.clear();

        var found = interestRateRepository.findByMaturityPeriodValidOn(15, today);

        assertTrue(found.isPresent());
        assertEquals(15, found.get().getMaturityPeriod());
        assertEquals(new BigDecimal("4.50"), found.get().getInterestRate());
    }

    @Test
    @DisplayName("Should return empty when entity not found")
    void shouldReturnEmptyWhenNotFound() {
        var found = interestRateRepository.findByMaturityPeriodValidOn(999, today);

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find all valid interest rate entities")
    void shouldFindAllValidEntities() {
        var entity1 = new InterestRateEntity(5, new BigDecimal("4.00"), validFrom, null);
        var entity2 = new InterestRateEntity(10, new BigDecimal("4.25"), validFrom, null);
        var entity3 = new InterestRateEntity(20, new BigDecimal("4.75"), validFrom, null);

        entityManager.persistAndFlush(entity1);
        entityManager.persistAndFlush(entity2);
        entityManager.persistAndFlush(entity3);
        entityManager.clear();

        List<InterestRateEntity> result = interestRateRepository.findAllValidOn(today);

        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no valid entities exist")
    void shouldReturnEmptyListWhenNoValidEntities() {
        // Insert expired entity
        var expiredEntity = new InterestRateEntity(5, new BigDecimal("4.00"), 
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        entityManager.persistAndFlush(expiredEntity);
        entityManager.clear();

        List<InterestRateEntity> result = interestRateRepository.findAllValidOn(today);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should not return expired interest rates")
    void shouldNotReturnExpiredRates() {
        var expiredEntity = new InterestRateEntity(10, new BigDecimal("4.00"), 
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        var currentEntity = new InterestRateEntity(10, new BigDecimal("4.25"), validFrom, null);

        entityManager.persistAndFlush(expiredEntity);
        entityManager.persistAndFlush(currentEntity);
        entityManager.clear();

        var found = interestRateRepository.findByMaturityPeriodValidOn(10, today);

        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("4.25"), found.get().getInterestRate());
    }

    @Test
    @DisplayName("Should not return future interest rates")
    void shouldNotReturnFutureRates() {
        var futureEntity = new InterestRateEntity(10, new BigDecimal("5.00"), 
                LocalDate.of(2027, 1, 1), null);

        entityManager.persistAndFlush(futureEntity);
        entityManager.clear();

        var found = interestRateRepository.findByMaturityPeriodValidOn(10, today);

        assertTrue(found.isEmpty());
    }
}
