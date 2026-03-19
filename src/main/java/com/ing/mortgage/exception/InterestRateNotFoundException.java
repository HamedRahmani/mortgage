package com.ing.mortgage.exception;

public class InterestRateNotFoundException extends RuntimeException{
    public InterestRateNotFoundException(int maturityPeriod) {
        super("Interest rate not found for maturity period: " + maturityPeriod);
    }
}
