package com.ing.mortgage.repository;

import com.ing.mortgage.repository.model.InterestRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRateEntity, Long> {


    @Query("SELECT e FROM InterestRateEntity e WHERE e.validFrom <= :date AND (e.validTo IS NULL OR e.validTo >= :date)")
    List<InterestRateEntity> findAllValidOn(@Param("date") LocalDate date);

    @Query("SELECT e FROM InterestRateEntity e WHERE e.maturityPeriod = :maturityPeriod AND e.validFrom <= :date AND (e.validTo IS NULL OR e.validTo >= :date)")
    Optional<InterestRateEntity> findByMaturityPeriodValidOn(@Param("maturityPeriod") Integer maturityPeriod, @Param("date") LocalDate date);
}
