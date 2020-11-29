DROP DATABASE IF EXISTS THEATER;
CREATE DATABASE THEATER;
USE theater;

/*-----------creating tables--------------*/
/*  Creating 'Movie' table  */
DROP TABLE IF EXISTS Movie;
CREATE TABLE Movie (
mID int NOT NULL AUTO_INCREMENT,
title VARCHAR(100) NOT NULL,
duration INT NOT NULL,
rating VARCHAR(20) NOT NULL,
releaseDate datetime,
endDate datetime,
primary key(mId)
);

/*  Creating 'Screening' table  */
DROP TABLE IF EXISTS Screening;
CREATE TABLE Screening (
screenID INT, 
mID INT,
roomID INT NOT NULL,
showingTime datetime,
updatedAt timestamp on update current_timestamp,
foreign key(mID) REFERENCES Movie(mID)
);

/*  Creating 'Room' table  */
DROP TABLE IF EXISTS Room;
CREATE TABLE Room (
roomID INT NOT NULL,
status VARCHAR(10),
primary key(roomID)
);

/*  Creating 'Seats' table  */
DROP TABLE IF EXISTS Seats;
CREATE TABLE Seats (
seatID INT NOT NULL,
roomID INT NOT NULL, 
status VARCHAR(10),
primary key(seatID),
foreign key(roomID) REFERENCES Room(roomID)
);

/*  Creating 'Customer' table  */
DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer(
cID INT NOT NULL, 
name VARCHAR(50),
age INT,
primary key(cID)
);

/*  Creating 'Reservation' table  */
DROP TABLE IF EXISTS Reservation;
CREATE TABLE Reservation (
resID INT NOT NULL AUTO_INCREMENT,
cID INT,
screenID INT,
seatID INT, 
date datetime,
primary key(resID),
foreign key(cID) REFERENCES Customer(cID),
foreign key(screenID) REFERENCES Screening(screenID),
foreign key(seatID) REFERENCES Seats(seatID)
);

/*  Creating 'Ticket' table  */
DROP TABLE IF EXISTS Ticket;
CREATE TABLE Ticket (
price decimal(5, 2), 
primary key(price)
);

/*  Creating 'Employee' table  */
DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee (
eID INT NOT NULL, 
name VARCHAR(100), 
role VARCHAR(70)
);

/*  Creating 'Transactions' table  */
DROP TABLE IF EXISTS Transactions;
CREATE TABLE Transactions (
tID INT NOT NULL,
eID INT, 
transactionDate datetime NOT NULL, 
total decimal(5,2) NOT NULL, 
resID INT,
primary key(tID),
foreign key(tid) REFERENCES Employee(eID),
foreign key(resID) REFERENCES Reservation(resID) /*not listed in report, but made a foreign key */
);


DROP TABLE IF EXISTS MovieStats;
CREATE TABLE MovieStats ( 
stars INT, 
mID int NOT NULL AUTO_INCREMENT,
totalSales DECIMAL(10,2),
foreign key(mID) REFERENCES Movie(mID)
);

DROP TABLE IF EXISTS Archive;
CREATE TABLE Archive (
screenID INT, 
mID int NOT NULL, 
roomID INT NOT NULL, 
showingTime datetime
);


INSERT INTO Employee VALUES(123, "Cashier");

INSERT INTO Customer VALUES(1, 1, "Tyler");
INSERT INTO CUSTOMER VALUES(2, 2, "Amy");

INSERT INTO Seats Values(1, 1, "reserved");
INSERT INTO Seats Values(1, 2, "reserved");
INSERT INTO Seats Values(1, 3, "vacant");
INSERT INTO Seats Values(1, 4, "reserved");
INSERT INTO Seats Values(2, 5, "reserved");
INSERT INTO Seats Values(1, 6, "vacant");