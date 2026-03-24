CREATE TABLE TRANSACTIONS (
      id UUID PRIMARY KEY,
      description TEXT,
      amount NUMERIC(19,2),
      type VARCHAR(255),
      category VARCHAR(255),
      date DATE
);