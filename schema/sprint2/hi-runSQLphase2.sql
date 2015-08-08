DROP TABLE IF EXISTS Bids;
DROP TABLE IF EXISTS RunLog;
DROP TABLE IF EXISTS Runs;
DROP TABLE IF EXISTS UserAuth;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Areas;

CREATE TABLE Users (
	userId			int primary key auto_increment,
	firstName		varchar(30) NOT NULL,
	lastName		varchar(30) NOT NULL,
	role			varchar(20) NOT NULL,
	email			varchar(40) unique NOT NULL,
	phone			varchar(20) default "",
	address			varchar(40) default "",
	aptNo			varchar(10) default "",
	addAddrInfo		varchar(110) default "",
	zipCode			varchar(10) default "",
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	birthday		date NOT NULL,
	ratingR			double default 0.0,
	completeRunsR	int default 0,
	failedRunsR		int default 0,
	completeRunsC	int default 0,
	totalRunsC		int default 0,
	isAdmin			int default 0
);

CREATE TABLE UserAuth (
	uid				int,
	username		varchar(21) unique,
	passHash		varchar(100) NOT NULL,
	foreign key(uid) references Users(userId),
	primary key(uid, username)
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
	foreign key(clntId) references Users(userId),
	foreign key(rnerId) references Users(userId)
);

CREATE TABLE Bids (
	ruId			int,
	runrId			int,
	bAmount			double NOT NULL,
	foreign key(ruId) references Runs(runId),
	foreign key(runrId) references Users(userId),
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
	foreign key(clntId) references Users(userId),
	foreign key(rnerId) references Users(userId)
);

INSERT INTO Users(firstName, lastName, role, email, city, state, birthday)
	VALUES ('John', 'Perez', 'Client', 'email1@gmail.com', 'San Antonio', 'Texas', '1991-04-27');
	
INSERT INTO Users(firstName, lastName, role, email, phone, city, state, birthday)
	VALUES ('Denise', 'Gonzales', 'Client', 'email2@gmail.com', '2101234123', 'San Antonio', 'Texas', '1991-04-27');
	
INSERT INTO Users(firstName, lastName, role, email, phone, address, aptNo, addAddrInfo, zipCode, city, state, 
	completeRunsC, totalRunsC, birthday)
	VALUES ('Jack', 'Vance', 'Client', 'email3@gmail.com' ,'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas',
	5, 6, '1991-04-27');
	
INSERT INTO Users(firstName, lastName, role, email, phone, city, state, isAdmin, birthday)
	VALUES ('Anders', 'Bylander', 'Client', 'email4@gmail.com', '2104549248', 'San Antonio', 'Texas', 1, '1991-04-27');
	
INSERT INTO UserAuth(uid, username, passHash)
	VALUES (1, 'korn1275', 'thisisnotahash3');
	
INSERT INTO UserAuth(uid, username, passHash)
	VALUES (2, 'deNotNice', 'notahash2');
	
INSERT INTO UserAuth(uid, username, passHash)
	VALUES (3, 'JackBback', 'thisisstillnotahash1');

INSERT INTO UserAuth(uid, username, passHash)
	VALUES (4, 'anderstb', 'nopenotahash5');
	
INSERT INTO Users(firstName, lastName, role, email, phone, city, state, birthday, ratingR, completeRunsR, failedRunsR)
	VALUES ('Denise', 'Gonzales', 'Runner', 'email5@gmail.com', '2101234123', 'San Antonio', 'Texas', '1991-04-27', 2.5, 3, 2);
	
INSERT INTO Users(firstName, lastName, role, email, phone, city, state, birthday, ratingR, completeRunsR, failedRunsR, isAdmin)
	VALUES ('Anders', 'Bylander', 'Runner', 'email6@gmail.com', '2104549248', 'San Antonio', 'Texas', '1990-07-22', 4, 2, 0, 1);
	
INSERT INTO UserAuth(uid, username, passHash)
	VALUES (5, 'wre537', 'thisisnotahash1');
	
INSERT INTO UserAuth(uid, username, passHash)
	VALUES (6, 'rcq719', 'thisisstillnotahash2');
	
INSERT INTO Areas(city, state, runnersInArea)
	VALUES ('San Antonio', 'Texas', 1);
	
INSERT INTO Runs(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC)
	VALUES (6, 2, 5, 'Need couch moved from 8331 Fredericksburg, San Antonio, TX 78229', 'No', 'Offer', 50, '2015-06-10 15:00:00',
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
	VALUES (8, 5, 25);
	
INSERT INTO Bids(ruId, runrId, bAmount)
	VALUES (8, 6, 35);
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (1, 2, 5, 'Need printer ink, hp 98 blk', 'Yes', 'Gratuity w/o approval', 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-05 11:04:53','2015-06-05 17:43:02', '15m', '2015-06-05 18:02:22');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (2, 2, 6, 'Need some brush moved', 'No', 'Bid', 30 , '2015-06-06 15:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 12:06:45','2015-06-06 14:35:30', '10m', '2015-06-06 14:59:45');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (3, 2, 5, 'Blu ray Intersteller', 'Yes', 'Gratuity w/ approval', 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 17:44:08','2015-06-06 17:49:52', '20m', '2015-06-06 18:12:34');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (4, 2, 6, 'Need my yard mowed', 'No', 'Offer w/o aprroval', 20 , '2015-06-07 14:00:00',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-06 18:08:49','2015-06-07 04:38:57', '1hr', '2015-06-07 14:58:02');
	
INSERT INTO RunLog(runId, clntId, rnerId, description, moneyReqd, runType, boAmount, timeNeeded, 
	cPhone, cAddress, aptNo, addAddrInfo, cZipCode, city, state, timePosted, timeAccepted, estimatedTtC, timeCompleted)
	VALUES (5, 2, 5, 'need wall a/c unit fixed', 'No', 'Offer w/ approval', 150, 'ASAP',
	'2105555555', '7575 Callaghan', '1401', 'gc #1234', '78229', 'San Antonio', 'Texas', '2015-06-08 02:16:55','2015-06-08 12:54:23', '2hr', '2015-06-08 21:00:43');