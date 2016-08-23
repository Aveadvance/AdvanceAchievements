CREATE SEQUENCE user_accounts_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE user_accounts (
	id NUMBER(19,0) NOT NULL,
	email NVARCHAR2(80 CHAR),
	password NVARCHAR2(80 CHAR),
	creation_date TIMESTAMP NOT NULL,
	enabled NUMBER(1,0) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE user_account_authorities (
	id NUMBER(19,0) NOT NULL,
	authority NVARCHAR2(30 CHAR),
	PRIMARY KEY (id),
	CONSTRAINT user_account_authorities_foreign_key FOREIGN KEY (id) REFERENCES user_accounts,
	CONSTRAINT unique_authorities_for_user UNIQUE (id, authority)
)