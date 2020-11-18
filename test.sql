DROP DATABASE IF EXISTS THEATER;
CREATE DATABASE THEATER;
USE theater;

DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Seats;

CREATE TABLE Employee (eID INT NOT NULL, name VARCHAR(100), role VARCHAR(70));
CREATE TABLE Customer(cID INT NOT NULL, reservID INT NOT NULL, name text);
CREATE TABLE Seats(rID INT NOT NULL, seatID INT NOT NULL, status text);


INSERT INTO Employee VALUES(123, "Cashier");


INSERT INTO Customer VALUES(1, 1, "Tyler");
INSERT INTO CUSTOMER VALUES(2, 2, "Amy");

INSERT INTO Seats Values(1, 1, "reserved");
INSERT INTO Seats Values(1, 2, "reserved");
INSERT INTO Seats Values(1, 3, "vacant");
INSERT INTO Seats Values(1, 4, "reserved");
INSERT INTO Seats Values(2, 5, "reserved");
INSERT INTO Seats Values(1, 6, "vacant");