CREATE DATABASE IF NOT EXISTS theater;
USE theater;
DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee (eid INT NOT NULL, name VARCHAR(100), role VARCHAR(70));
INSERT INTO Employee VALUES(123, "Daniel Cardenas", "Cashier"); 
