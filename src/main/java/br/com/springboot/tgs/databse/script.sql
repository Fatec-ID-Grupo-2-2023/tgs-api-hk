CREATE DATABASE tgs;

USE tgs;

CREATE TABLE patients (
	cpf_patient CHAR(11) PRIMARY KEY,
	rg_patient CHAR(10) NOT NULL UNIQUE,
	name_patient VARCHAR(25) NOT NULL,
	surname VARCHAR(35) NOT NULL,
	nickname VARCHAR(20),
	birth_date DATETIME,
	height CHAR(4),
	weight_patient CHAR(6),
	email VARCHAR(30),
	telephone CHAR(10),
	cellphone CHAR(11) NOT NULL,
	street VARCHAR(30) NOT NULL,
	neighborhood VARCHAR(15) NOT NULL,
	city VARCHAR(15) NOT NULL,
	district CHAR(2) NOT NULL,
	cep CHAR(8) NOT NULL,
	complement CHAR(6),
	number CHAR(4) NOT NULL,
	status BIT DEFAULT 1,
);

CREATE TABLE procedures (
	id_procedure INT PRIMARY KEY IDENTITY,
	title VARCHAR(15) UNIQUE NOT NULL,
	description VARCHAR(50),
	status BIT DEFAULT 1,
);

CREATE TABLE users (
	user_id CHAR(14) PRIMARY KEY,
	document CHAR(11) NOT NULL UNIQUE,
	name_user VARCHAR(25) NOT NULL,
	surname VARCHAR(35) NOT NULL,
	expertise VARCHAR(15) NOT NULL,
	email VARCHAR(30) NOT NULL,
	telephone CHAR(10),
	cellphone CHAR (11) NOT NULL,
	password VARCHAR(MAX) NOT NULL,
	status BIT DEFAULT 1,
);


CREATE TABLE consults (
	id_consult INT PRIMARY KEY IDENTITY,
	cpf_patient CHAR(11) REFERENCES patients,
	datetime_consult DATETIME NOT NULL,
	status BIT DEFAULT 0,
	id_procedure INT REFERENCES procedures,
	id_dentist CHAR(14) NOT NULL REFERENCES users,
	id_employee CHAR(14) REFERENCES users,
);

INSERT INTO users VALUES ('EPL00000000000','00000000000','admin', '','','','','','$2a$10$9J4TfJclO5JmsXUO.LxDkOT35PjBOG/3smws8GNGaDqy6AgSL/5/S',DEFAULT);
