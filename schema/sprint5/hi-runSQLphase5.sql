DROP TABLE IF EXISTS ClientRatings;
DROP TABLE IF EXISTS RunnerRatings;
DROP TABLE IF EXISTS RunsFinal;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
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
	ratingR			decimal(16,2) default 0.00,
	ratingC			decimal(16,2) default 0.00,
	completeRunsR	int default 0,
	completeRunsC	int default 0,
	isVerified		varchar(20) NOT NULL,
	flaggedRuns		int default 0
);

CREATE TABLE RunsFinal (
	runId			int primary key auto_increment,
	clntId			int NOT NULL,
	rnerId			int,
	title			varchar(25) NOT NULL,
	description		varchar(100) NOT NULL,
	status			varchar(20) NOT NULL,
	moneyReqd		varchar(10) NOT NULL default 'Yes',
	runType			varchar(30) NOT NULL default 'Offer',
	amount			decimal(16,2) default 0.00,
	timeNeeded		varchar(30) NOT NULL default 'ASAP',
	cAddress		varchar(50) NOT NULL,
	cZipCode		varchar(10) NOT NULL,
	city			varchar(20) NOT NULL,
	state			varchar(20) NOT NULL,
	timePosted		varchar(20) NOT NULL,
	timeAccepted	varchar(20) default "",
	timeCompleted	varchar(20) default "",
	timesFlagged	int NOT NULL default 0,
	cReviewed		int default 0,
	rReviewed		int default 0,
	version			varchar(20) NOT NULL,
	foreign key(clntId) references Users(userId),
	foreign key(rnerId) references Users(userId)
);

CREATE TABLE ClientRatings (
	rId				int primary key,
	clientId		int NOT NULL,
	runnerName		varchar(21) NOT NULL,
	rating			double NOT NULL,
	review			varchar(100) default "",
	foreign key(rId) references RunsFinal(runId),
	foreign key(clientId) references Users(userId),
	foreign key(runnerName) references Users(username)
);

CREATE TABLE RunnerRatings (
	rId				int primary key,
	runnerId		int NOT NULL,
	clientName		varchar(21) NOT NULL,
	rating			double NOT NULL,
	review			varchar(100) default "",
	foreign key(rId) references RunsFinal(runId),
	foreign key(runnerId) references Users(userId),
	foreign key(clientName) references Users(username)
);

CREATE TRIGGER `avg_client_rating` AFTER INSERT ON `ClientRatings`
 FOR EACH ROW BEGIN
	UPDATE Users SET ratingC = (SELECT AVG(rating) FROM ClientRatings WHERE Users.userId = clientId)
	WHERE userId = NEW.clientId; 
	UPDATE RunsFinal SET rReviewed = 1
	WHERE runId = NEW.rId;
END

CREATE TRIGGER `avg_runner_rating` AFTER INSERT ON `RunnerRatings`
 FOR EACH ROW BEGIN
	UPDATE Users SET ratingR = (SELECT AVG(rating) FROM RunnerRatings WHERE Users.userId = runnerId)
	WHERE userId = NEW.runnerId; 
	UPDATE RunsFinal SET cReviewed = 1
	WHERE runId = NEW.rId;
END

CREATE TRIGGER `run_completed` AFTER UPDATE ON `RunsFinal`
 FOR EACH ROW BEGIN
	IF NEW.timeCompleted <> OLD.timeCompleted THEN 
		UPDATE Users
		SET completeRunsR = completeRunsR + 1 
		WHERE NEW.rnerId = userId;
		UPDATE Users
		SET completeRunsC = completeRunsC + 1 
		WHERE NEW.clntId = userId;
	END IF;
	IF NEW.timesFlagged = 5 THEN
		UPDATE Users
		SET flaggedRuns = flaggedRuns + 1
		WHERE NEW.clntId = userId;
	END IF;
END