DROP TABLE IF EXISTS ACCOUNT;
CREATE TABLE ACCOUNT (
  ID      INT PRIMARY KEY AUTO_INCREMENT,
  BALANCE DOUBLE          DEFAULT 0.0 CHECK (BALANCE >= 0),
  OWNERID INT NOT NULL
);

DROP TABLE IF EXISTS CLIENT;
CREATE TABLE CLIENT (
  ID        INT PRIMARY KEY AUTO_INCREMENT,
  FIRSTNAME VARCHAR(100) NOT NULL,
  LASTNAME  VARCHAR(100) NOT NULL,
  ACCOUNTID INT
);

DROP TABLE IF EXISTS PAYMENT;
CREATE TABLE PAYMENT (
  ID             INT PRIMARY KEY AUTO_INCREMENT,
  SENDERACCID    INT      NOT NULL,
  RECIPIENTACCID INT      NOT NULL,
  AMOUNT         DOUBLE   NOT NULL,
  WHEN           DATETIME NOT NULL
);