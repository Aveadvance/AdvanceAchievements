*** DEVELOPMENT
	DROP SEQUENCE user_accounts_seq;
	DROP TABLE user_accounts CASCADE CONSTRAINTS;
	DROP TABLE user_account_authorities CASCADE CONSTRAINTS;
	DROP TABLE user_tasks CASCADE CONSTRAINTS;
***

CREATE SEQUENCE user_accounts_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_task_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE user_accounts (
	id NUMBER(19,0) NOT NULL,
	email VARCHAR2(80 CHAR) NOT NULL,
	password VARCHAR2(80 CHAR) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	enabled NUMBER(1,0) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT unique_email UNIQUE (email)
);


/*
 * Situation:
 * 1 ROLE_USER
 * 2 ROLE_USER
 * 3 ROLE_USER
 * 4 ROLE_USER
 * 5 ROLE_ADMIN
 *
 * Should be
 * 1 ROLE_USER
 * 2 ROLE_ADMIN
 */
CREATE TABLE user_account_authorities (
	id NUMBER(19,0) NOT NULL,
	authority VARCHAR2(30 CHAR) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT authority_account_foreign_key FOREIGN KEY (id) REFERENCES user_accounts,
	CONSTRAINT unique_authorities_for_user UNIQUE (id, authority)
);

CREATE TABLE user_tasks (
	id NUMBER(19,0) NOT NULL,
	title VARCHAR2(100 CHAR) NOT NULL,
	description VARCHAR2(1000 CHAR),
	priority VARCHAR2(30 CHAR) NOT NULL,
	owner_user_account_id NUMBER(19, 0) NOT NULL,
	state VARCHAR2(20 CHAR) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT task_account_foreign_key FOREIGN KEY (owner_user_account_id) REFERENCES user_accounts
);