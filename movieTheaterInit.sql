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
releaseDate DATE,
endDate DATE,
primary key(mId)
);

/*  Creating 'Screening' table  */
DROP TABLE IF EXISTS Screening;
CREATE TABLE Screening (
screenID INT, 
mID INT,
roomID INT NOT NULL,
showingTime datetime,
updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
primary key(screenID),
foreign key(mID) REFERENCES Movie(mID)
);

/*  Creating 'Room' table  */
DROP TABLE IF EXISTS Room;
CREATE TABLE Room (
roomID INT NOT NULL,
capacity INT NOT NULL,
primary key(roomID)
);

/*  Creating 'Seat' table  */
DROP TABLE IF EXISTS Seat;
CREATE TABLE Seat (
seatID INT NOT NULL,
roomID INT NOT NULL, 
status VARCHAR(10),
primary key(seatID, roomID),
foreign key(roomID) REFERENCES Room(roomID),
CONSTRAINT statusCheck CHECK(status = "vacant" OR status = "reserved")
);

/*  Creating 'Customer' table  */
DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer(
cID INT NOT NULL AUTO_INCREMENT, 
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
roomID INT,
seatID INT, 
date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
primary key(resID),
foreign key(cID) REFERENCES Customer(cID),
foreign key(screenID) REFERENCES Screening(screenID),
foreign key(roomID, seatID) REFERENCES Seat(roomID, seatID)
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
role VARCHAR(70),
primary key(eID)
);

/*  Creating 'Transactions' table  */
DROP TABLE IF EXISTS Transactions;
CREATE TABLE Transactions (
tID INT NOT NULL,
eID INT, 
transactionDate date NOT NULL, 
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

# Procedure to make a phase screenings out:
DELIMITER //
CREATE PROCEDURE PhaseScreeningOut(
    IN cutoff DATE
)
BEGIN
    INSERT INTO Archive
    SELECT screenID, mID, roomID, showingTime
	FROM Screening
	WHERE DATE(updatedAt) < cutoff;
    
    DELETE FROM Screening
    WHERE DATE(updatedAt) < cutoff;
END //
DELIMITER ;

# Procedure to fill the seats in the movie theater gets called downwards in the sql file.
DELIMITER //
CREATE PROCEDURE fillSeat()
BEGIN
	DECLARE cap INT;
    DECLARE id INT;
    DECLARE i INT DEFAULT 1;
    DECLARE j INT DEFAULT 1;
    DECLARE numroom INT;
    
    SELECT COUNT(*) INTO numroom FROM Room;
    
    WHILE i <= numroom DO
		SELECT capacity INTO cap FROM Room WHERE roomID = i;
        SET j = 1;
		WHILE j <= cap DO
			INSERT INTO Seat Values(j, i, "vacant");
            SET j = j + 1;
		END WHILE;
        SET i = i + 1;
	END WHILE;
END //
DELIMITER ;

DELIMITER //
# Procedure finalizes a reservation updating all the required tables
CREATE PROCEDURE reserve(
    IN inputcid INT,
	inputmid INT,
    inputscreenid INT,
    inputseatid INT
)
BEGIN
    INSERT INTO Reservation Values(DEFAULT, inputcid, inputscreenid, inputseatID, DEFAULT);
	UPDATE Seat SET status = "reserved" WHERE seatID = inputseatID;
END //
DELIMITER ;




INSERT INTO Employee VALUES(123, "Daniel", "Cashier");

INSERT INTO Customer VALUES(1, "Tyler", 10);
INSERT INTO CUSTOMER VALUES(2, "Amy", 20);
INSERT INTO CUSTOMER VALUES(3, "James", 17);
INSERT INTO CUSTOMER VALUES(4, "Sarah", 45);
INSERT INTO Customer VALUES(5, "Tyler", 18);

INSERT INTO Movie VALUES(DEFAULT, "Gone with the wind", 136, "R", '2000-02-16', '2020-12-20');
INSERT INTO Movie VALUES(DEFAULT, "Boyhood", 145, "PG-13", '2010-04-24', '2020-12-26');
INSERT INTO Movie VALUES(DEFAULT, "Batman", 100, "PG-13", '2010-04-24', '2019-12-26');

INSERT INTO Screening VALUES(1, 1, 1, '2020-12-19 12:30:00', DEFAULT);
INSERT INTO Screening VALUES(2, 1, 1, '2020-12-19 15:30:00', DEFAULT);
INSERT INTO Screening VALUES(3, 2, 2, '2020-12-18 09:00:00', DEFAULT);

INSERT INTO Room VALUES(1, 40);
INSERT INTO Room VALUES(2, 60);
INSERT INTO Room VALUES(3, 40);
INSERT INTO Room VALUES(4, 120);


CALL fillSeat;
UPDATE Seat SET status = "reserved" WHERE roomID = 1 AND seatID = 1;
UPDATE Seat SET status = "reserved" WHERE roomID = 1 AND seatID = 2;
UPDATE Seat SET status = "reserved" WHERE roomID = 2 AND seatID = 1;
UPDATE Seat SET status = "reserved" WHERE roomID = 2 AND seatID = 2;
UPDATE Seat SET status = "reserved" WHERE roomID = 2 AND seatID = 3;
UPDATE Seat SET status = "reserved" WHERE roomID = 3 AND seatID = 1;

INSERT INTO Reservation VALUES(DEFAULT, 1, 1, 1, 1, '2020-11-18');
INSERT INTO Reservation VALUES(DEFAULT, 2, 1, 1, 2, '2020-11-18');
INSERT INTO Reservation VALUES(DEFAULT, 3, 1, 2, 3, '2020-11-20');
INSERT INTO Reservation VALUES(DEFAULT, 4, 1, 2, 1, '2020-11-05');
INSERT INTO Reservation VALUES(DEFAULT, 2, 2, 3, 1, '2020-11-18');
INSERT INTO Reservation VALUES(DEFAULT, 1, 1, 2, 2, '2020-11-18');

INSERT INTO Transactions VALUES()