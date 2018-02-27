CREATE DATABASE core;

CREATE DATABASE rec;

CREATE LOGIN tenant WITH PASSWORD = 'tenant12345@!';  

USE core;

CREATE USER tenant from LOGIN tenant WITH DEFAULT_SCHEMA = tenant;
CREATE SCHEMA tenant AUTHORIZATION tenant;

GRANT CREATE TABLE to tenant;
GRANT CREATE VIEW to tenant;

execute as login = 'tenant'

CREATE TABLE clientes (
	id int NOT NULL,
	nome varchar(100),
	CONSTRAINT PK__clientes__3213E83FC1E529DF PRIMARY KEY (id)
)

revert

use rec

CREATE USER tenant from LOGIN tenant WITH DEFAULT_SCHEMA = tenant;
CREATE SCHEMA tenant AUTHORIZATION tenant
GRANT CREATE TABLE to tenant;
GRANT CREATE VIEW to tenant;

execute as login = 'tenant'

CREATE TABLE receita (
	id int NOT NULL,
	idcliente int,
	idrt int,
	[data] date,
	numero varchar(20),
	recomendacao varchar(1000),
	CONSTRAINT PK__receita__3213E83F0F072EEF PRIMARY KEY (id)
);

create view clientes as select * from core.tenant.clientes;

revert

execute as login = 'tenant'
insert into core.tenant.clientes values (2, 'Lauro');
insert into core.tenant.clientes values (3, 'João');

revert

user master;

CREATE LOGIN tenant2 WITH PASSWORD = 'tenant212345@!';  

USE core;

CREATE USER tenant2 from LOGIN tenant2 WITH DEFAULT_SCHEMA = tenant2;
CREATE SCHEMA tenant2 AUTHORIZATION tenant2;

GRANT CREATE TABLE to tenant2;
GRANT CREATE VIEW to tenant2;

execute as login = 'tenant2'

CREATE TABLE clientes (
	id int NOT NULL,
	nome varchar(100),
	CONSTRAINT PK__clientes__3213E83FC1E529DF PRIMARY KEY (id)
)

revert

use rec

CREATE USER tenant2 from LOGIN tenant2 WITH DEFAULT_SCHEMA = tenant2;
CREATE SCHEMA tenant2 AUTHORIZATION tenant2
GRANT CREATE TABLE to tenant2;
GRANT CREATE VIEW to tenant2;

execute as login = 'tenant2'

CREATE TABLE receita (
	id int NOT NULL,
	idcliente int,
	idrt int,
	[data] date,
	numero varchar(20),
	recomendacao varchar(1000),
	CONSTRAINT PK__receita__3213E83F0F072EEF PRIMARY KEY (id)
);

create view clientes as select * from core.tenant2.clientes;

revert

execute as login = 'tenant2'
insert into core.tenant2.clientes values (2, 'Lauro 2');
insert into core.tenant2.clientes values (3, 'João 2');

revert