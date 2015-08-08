DROP TABLE IF EXISTS Bids;
DROP TABLE IF EXISTS RunLog;
DROP TABLE IF EXISTS Runs;
DROP TABLE IF EXISTS RunnerAuth;
DROP TABLE IF EXISTS ClientAuth;
DROP TABLE IF EXISTS Clients;
DROP TABLE IF EXISTS Runners;
DROP TABLE IF EXISTS Areas;

CREATE TABLE Clients (
	clientId		int primary key auto_increment,
	firstName		varchar(20) NOT NULL,
	lastName		varchar(20) NOT NULL,
	dPhone			varchar(20),
	dAddress		varchar(40),
	aptNo			varchar(10),
	addAddrInfo		varchar(40),
	dZipCode		varchar(10),
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	completeRuns	int,
	totalRuns		int,
	isAdmin			int
);

CREATE TABLE ClientAuth (
	cid				int,
	username		varchar(21) unique,
	passHash		varchar(100) NOT NULL,
	foreign key(cid) references Clients(clientId),
	primary key(cid, username)
);

CREATE TABLE Runners (
	runnerId		int primary key auto_increment,
	firstName		varchar(20) NOT NULL,
	lastName		varchar(20) NOT NULL,
	phone			varchar(20) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	birthday		date NOT NULL,
	rating			double NOT NULL,
	completeRuns	int,
	failedRuns		int,
	isAdmin			int
);

CREATE TABLE RunnerAuth (
	rid				int,
	username		varchar(7) unique,
	passHash		varchar(100) NOT NULL,
	foreign key(rid) references Runners(runnerId),
	primary key(rid, username)
);

CREATE TABLE Areas (
	areaId			int primary key auto_increment,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	runnersInArea	int
);

CREATE TABLE Runs (
	runId			int primary key auto_increment,
	clntId			int NOT NULL,
	rnerId			int,
	description		varchar(100) NOT NULL,
	moneyReqd		varchar(10) NOT NULL,
	runType			varchar(30) NOT NULL,
	boAmount		double,
	timeNeeded		datetime NOT NULL,
	cPhone			varchar(20) NOT NULL,
	cAddress		varchar(40) NOT NULL,
	aptNo			varchar(10),
	addAddrInfo		varchar(40),
	cZipCode		varchar(10) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	timePosted		datetime NOT NULL,
	timeAccepted	datetime,
	estimatedTtC	varchar(20),
	foreign key(clntId) references Clients(clientId),
	foreign key(rnerId) references Runners(runnerId)
);

CREATE TABLE Bids (
	ruId			int,
	runrId			int,
	bAmount			double NOT NULL,
	foreign key(ruId) references Runs(runId),
	foreign key(runrId) references Runners(runnerId),
	primary key(ruId, runrId)
);

CREATE TABLE RunLog (
	runId			int primary key auto_increment,
	clntId			int NOT NULL,
	rnerId			int,
	description		varchar(100) NOT NULL,
	moneyReqd		varchar(10) NOT NULL,
	runType			varchar(30) NOT NULL,
	boAmount		double,
	timeNeeded		datetime NOT NULL,
	cPhone			varchar(20) NOT NULL,
	cAddress		varchar(40) NOT NULL,
	aptNo			varchar(10),
	addAddrInfo		varchar(40),
	cZipCode		varchar(10) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	timePosted		datetime NOT NULL,
	timeAccepted	datetime,
	estimatedTtC	varchar(20),
	timeCompleted	datetime,
	foreign key(clntId) references Clients(clientId),
	foreign key(rnerId) references Runners(runnerId)
);

INSERT INTO Clients(firstName, lastName, city, state)
	VALUES ('John', 'Perez', 'San Antonio', 'Texas');
	
INSERT INTO Clients(firstName, lastName, dPhone, city, state)
	VALUES ('Denise', 'Gonzales', '2101234123', 'San Antonio', 'Texas');
	
INSERT INTO Clients(firstName, lastName, dPhone, dAddress, aptNo, addAddrInfo, dZipCode, city, state, 
	completeRuns, totalRuns)
	VALUES ('Jack', 'Vance', '2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas',
	5, 6);
	
INSERT INTO Clients(firstName, lastName, dPhone, city, state, isAdmin)
	VALUES ('Anders', 'Bylander', '2104549248', 'San Antonio', 'Texas', 1);
	
INSERT INTO ClientAuth(cid, username, passHash)
	VALUES (1, 'korn1275', 'thisisnotahash');
	
INSERT INTO ClientAuth(cid, username, passHash)
	VALUES (2, 'deNotNice', 'notahash');
	
INSERT INTO ClientAuth(cid, username, passHash)
	VALUES (3, 'JackBback', 'thisisstillnotahash');

INSERT INTO ClientAuth(cid, username, passHash)
	VALUES (4, 'anderstb', 'nopenotahash');
	
INSERT INTO Runners(firstName, lastName, phone, city, state, birthday, rating, completeRuns, failedRuns)
	VALUES ('Denise', 'Gonzales', '2101234123', 'San Antonio', 'Texas', '1991-04-27', 2.5, 3, 2);
	
INSERT INTO Runners(firstName, lastName, phone, city, state, birthday, rating, completeRuns, failedRuns, isAdmin)
	VALUES ('Anders', 'Bylander', '2104549248', 'San Antonio', 'Texas', '1990-07-22', 4, 2, 0, 1);
	
INSERT INTO RunnerAuth(rid, username, passHash)
	VALUES (1, 'wre537', 'thisisnotahash');
	
INSERT INTO RunnerAuth(rid, username, passHash)
	VALUES (2, 'rcq719', 'thisisstillnotahash');
	
INSERT INTO Areas(city, state, runnersInArea)
	VALUES ('San Antonio', 'Texas', 1);
	
INSERT INTO Runs(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC)
	VALUES (6, 2, 1, 'Need couch moved from 8331 Fredericksburg, San Antonio, TX 78229', 'No', 'Offer', 50, '2015-06-10 15:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-09 13:54:23','2015-06-09 17:43:02', '30m');
	
INSERT INTO Runs(runId, clntId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted)
	VALUES (7, 2, 'Chickfilet #1 w/ sweet Tea, no pickles', 'Yes', 'Gratuity w/o approval', 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-09 18:06:45');
	
INSERT INTO Runs(runId, clntId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted)
	VALUES (8, 2, 'Delicate pickup from my address to 6510 San Pedro 78216', 'No', 'Bid', '2015-06-11 12:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-09 18:14:01');
	
INSERT INTO Bids(ruId, runrId, bAmount)
	VALUES (8, 1, 25);
	
INSERT INTO Bids(ruId, runrId, bAmount)
	VALUES (8, 2, 35);
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (1, 2, 1, 'Need printer ink, hp 98 blk', 'Yes', 'Gratuity w/o approval', 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-05 11:04:53','2015-06-05 17:43:02', '15m', '2015-06-05 18:02:22');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (2, 2, 2, 'Need some brush moved', 'No', 'Bid', 30 , '2015-06-06 15:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 12:06:45','2015-06-06 14:35:30', '10m', '2015-06-06 14:59:45');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (3, 2, 1, 'Blu ray Intersteller', 'Yes', 'Gratuity w/ approval', 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 17:44:08','2015-06-06 17:49:52', '20m', '2015-06-06 18:12:34');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (4, 2, 2, 'Need my yard mowed', 'No', 'Offer w/o aprroval', 20 , '2015-06-07 14:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 18:08:49','2015-06-07 04:38:57', '1hr', '2015-06-07 14:58:02');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (5, 2, 1, 'need wall a/c unit fixed', 'No', 'Offer w/ approval', 150, 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-08 02:16:55','2015-06-08 12:54:23', '2hr', '2015-06-08 21:00:43');