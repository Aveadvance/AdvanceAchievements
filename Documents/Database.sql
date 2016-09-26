*** DEVELOPMENT
	DROP SEQUENCE user_accounts_seq;
	DROP SEQUENCE user_task_seq;
	DROP SEQUENCE user_task_category_seq;
	DROP TABLE user_accounts CASCADE CONSTRAINTS;
	DROP TABLE user_account_authorities CASCADE CONSTRAINTS;
	DROP TABLE workspaces CASCADE CONSTRAINTS;
	DROP TABLE user_account_workspace CASCADE CONSTRAINTS;
	DROP TABLE user_tasks CASCADE CONSTRAINTS;
	DROP TABLE user_task_categories CASCADE CONSTRAINTS;

	DELETE FROM user_accounts;
	DELETE FROM user_account_authorities;
	DELETE FROM user_account_workspace;
	DELETE FROM user_tasks;
	DELETE FROM user_task_categories;
	DELETE FROM workspaces;

	SELECT acc.id, email, authority FROM user_accounts acc 
		INNER JOIN user_account_authorities auth ON acc.id=auth.id;
	SELECT id, user_task_category_id FROM user_tasks;
***

CREATE SEQUENCE user_accounts_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE workspace_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_task_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_task_category_seq START WITH 1 INCREMENT BY 1;

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

CREATE TABLE workspaces (
	id NUMBER(19,0) NOT NULL,
	type VARCHAR2(30 CHAR) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE user_account_workspace (
	user_account_id NUMBER(19,0) NOT NULL,
	workspace_id NUMBER(19,0) NOT NULL,
	PRIMARY KEY (user_account_id, workspace_id)
);

CREATE TABLE user_task_categories (
	id NUMBER(19,0) NOT NULL,
	name VARCHAR2(100 CHAR) NOT NULL,
	workspace_id NUMBER(19,0) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT task_category_workspace_fk FOREIGN KEY (workspace_id) REFERENCES workspaces
);

CREATE TABLE user_tasks (
	id NUMBER(19,0) NOT NULL,
	title VARCHAR2(100 CHAR) NOT NULL,
	description VARCHAR2(1000 CHAR),
	priority VARCHAR2(30 CHAR) NOT NULL,
	owner_user_account_id NUMBER(19, 0) NOT NULL,
	workspace_id NUMBER(19, 0),
	user_task_category_id NUMBER(19, 0),
	state VARCHAR2(20 CHAR) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT task_account_foreign_key FOREIGN KEY (owner_user_account_id) REFERENCES user_accounts,
	CONSTRAINT task_workspace_fk FOREIGN KEY (workspace_id) REFERENCES workspaces,
	CONSTRAINT task_category_fk FOREIGN KEY (user_task_category_id) REFERENCES user_task_categories
);