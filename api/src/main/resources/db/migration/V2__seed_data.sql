INSERT INTO transactions (id, description, amount, type, category, date) VALUES
(gen_random_uuid(), 'Monthly Salary', 5000.00, 'INCOME', 'SALARY', '2026-03-01'),
(gen_random_uuid(), 'Grocery Store', 150.50, 'EXPENSE', 'FOOD', '2026-03-02'),
(gen_random_uuid(), 'Monthly Rent', 1200.00, 'EXPENSE', 'OTHER', '2026-03-03'),
(gen_random_uuid(), 'Gas Station', 60.00, 'EXPENSE', 'TRANSPORT', '2026-03-04'),
(gen_random_uuid(), 'Freelance Project', 800.00, 'INCOME', 'OTHER', '2026-03-05');