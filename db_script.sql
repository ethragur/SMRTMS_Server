DROP database IF EXISTS SMRTMS;
CREATE database IF NOT EXISTS SMRTMS;
USE SMRTMS;

CREATE TABLE IF NOT EXISTS User 
(	
	ID int unique auto_increment,
	Username char(50),
  	Email char(50),
    	Password char(20),
	Longitude double,
	Latitude double,
	Avatar char(15),
	IsOnline boolean,

	PRIMARY KEY (ID)
);
    
CREATE TABLE IF NOT EXISTS User_Friends
(	Friender_ID int,
	Friendee_ID int,
    	Tracking_Flag bool,

	FOREIGN KEY (Friender_ID) REFERENCES User(ID),
	FOREIGN KEY (Friendee_ID) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS Event
(
	ID int unique auto_increment,
        Name char(50),
	Description text,
	Longitude double,
	Latitude double,
        Attendees int,

    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS Event_Attendees
(
	User_ID int,
    	Event_ID int,

	FOREIGN KEY (User_ID) REFERENCES User(ID),
	FOREIGN KEY (Event_ID) REFERENCES Event(ID)
);

CREATE TABLE IF NOT EXISTS Friend_Request_Stash
(
	Friender_ID int,
	Friendee_ID int,

	FOREIGN KEY (Friender_ID) REFERENCES User(ID),
	FOREIGN KEY (Friendee_ID) REFERENCES User(ID)
);

INSERT INTO User
(Username, Email, Password, Longitude, Latitude, Avatar) VALUES
("Hubert", "h@gmail.com", "123456", 13.23, 19.234, "f34tg45gh34.png");
