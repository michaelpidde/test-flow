create database if not exists testflow;

use testflow;

/*
 * A package is the top level container for suites and tests.
 */
create table if not exists packages (
	id int not null primary key auto_increment,
	name varchar(50),
	baseUrl varchar(100)
);

/*
 * A test is tied to a package. It can also be tied to a suite.
 */
create table if not exists tests (
	id int not null primary key auto_increment,
	packageId int not null,
	name varchar(100)
);

/*
 * A suite shows up in the test window for a given package.
 */
create table if not exists suites (
	id int not null primary key auto_increment,
	packageId int not null,
	name varchar(50)
);

create table if not exists packageTestLink (
	packageId int not null,
	testId int not null
);

create table if not exists suiteTestLink (
	suiteId int not null,
	testId int not null
);

/*
 * Add some defaults
 */
insert into packages values(1, 'google', 'http://google.com');
insert into tests values(1, 1, 'SearchMamaJama');
insert into suites values(1, 1, 'TestSuite');