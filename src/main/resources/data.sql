-- Insert initial interest rates data (valid_to = NULL means currently active)
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (1, 3.50, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (5, 4.00, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (10, 4.25, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (15, 4.50, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (20, 4.75, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (25, 5.00, '2026-01-01', NULL);
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (30, 5.25, '2026-01-01', NULL);

-- Example: Historical rate (was valid until 2025-12-31)
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (1, 4.00, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (5, 4.10, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (10, 4.20, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (15, 4.30, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (20, 4.40, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (25, 5.10, '2025-01-01', '2025-12-31');
INSERT INTO interest_rates (maturity_period, interest_rate, valid_from, valid_to) VALUES (30, 5.30, '2025-01-01', '2025-12-31');
