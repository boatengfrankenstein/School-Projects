DROP TABLE IF EXISTS Runs;
DROP TABLE IF EXISTS UsersFinal;

CREATE TABLE UsersFinal (
	userId			int primary key auto_increment,
	username		varchar(21) unique NOT NULL,
	passHash		varchar(100) NOT NULL,
	email			varchar(40) unique NOT NULL,
	firstName		varchar(30) NOT NULL,
	lastName		varchar(30) NOT NULL,
	role			varchar(20) NOT NULL,
	phone			varchar(20) NOT NULL,
	address			varchar(50) default "",
	zipCode			varchar(10) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	birthday		varchar(20) NOT NULL,
	ratingR			double default 0.0,
	completeRunsR	int default 0,
	failedRunsR		int default 0,
	completeRunsC	int default 0,
	totalRunsC		int default 0
);

CREATE TABLE Runs (
	runId			int primary key auto_increment,
	clntId			int NOT NULL,
	rnerId			int,
	title			varchar(25) NOT NULL,
	description		varchar(100) NOT NULL,
	status			varchar(20) NOT NULL,
	moneyReqd		varchar(10) NOT NULL default 'Yes',
	runType			varchar(30) NOT NULL default 'Offer',
	amount			double default 0.0,
	timeNeeded		varchar(30) NOT NULL default 'ASAP',
	cAddress		varchar(50) NOT NULL,
	cZipCode		varchar(10) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	timePosted		varchar(20) NOT NULL,
	timeAccepted	varchar(20) default "",
	timeCompleted	varchar(20) default "",
	timesFlagged	int NOT NULL default 0,
	foreign key(clntId) references UsersFinal(userId),
	foreign key(rnerId) references UsersFinal(userId)
);

