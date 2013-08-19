CREATE TABLE user (
	id VARCHAR(100) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	email_address VARCHAR(100) NOT NULL,
	organisation_id VARCHAR(100) NOT NULL,
	type VARCHAR(25) NOT NULL,
	home_location_name VARCHAR(100),
	home_location_latitude NUMERIC(12,9),
	home_location_longitude NUMERIC(12,9),
	default_location_id varchar(100)
);

CREATE TABLE credential (
	id VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	type VARCHAR(25) NOT NULL,
	username VARCHAR(100),
	encrypted_password VARCHAR(50)
);

CREATE TABLE authentication (
	user_id VARCHAR(100) NOT NULL,
	id VARCHAR(100) NOT NULL,
	expires_on TIMESTAMP
);

CREATE TABLE organisation (
	id VARCHAR(100) NOT NULL,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE organisation_location (
	id VARCHAR(100) NOT NULL,
	organisation_id VARCHAR(100),
	name VARCHAR(100) NOT NULL,
	address VARCHAR(250) NOT NULL,
	longitude NUMERIC(12,9) NOT NULL,
	latitude NUMERIC(12,9) NOT NULL,
	sort_order NUMERIC(10,0) NOT NULL
);

CREATE TABLE route (
	id VARCHAR(100) NOT NULL,
	distance NUMERIC(12,4) NOT NULL,
	duration VARCHAR(25) NOT NULL,
	travel_mode VARCHAR(25) NOT NULL,
	code VARCHAR(4000) NOT NULL,
	origin_name VARCHAR(100) NOT NULL,
	origin_latitude NUMERIC(12,9) NOT NULL,
	origin_longitude NUMERIC(12,9) NOT NULL,
	destination_name VARCHAR(100) NOT NULL,
	destination_latitude NUMERIC(12,9) NOT NULL,
	destination_longitude NUMERIC(12,9) NOT NULL,
	parent_route_id VARCHAR(100),
	departure_time TIMESTAMP,
	arrival_time TIMESTAMP,
	headsign VARCHAR(100),
	vehicle_type VARCHAR(25)
);

CREATE TABLE scored_route (
	id VARCHAR(100) NOT NULL,
	route_id VARCHAR(100) NOT NULL,
	user_id VARCHAR(100) NOT NULL,
	`day` DATE NOT NULL,
	score NUMERIC(10,0) NOT NULL
);

CREATE TABLE office_day (
	id VARCHAR(100) NOT NULL,
	day_of_week NUMERIC NOT NULL,
	arrival DATETIME,
	departure DATETIME,
	user_id VARCHAR(100)
);

CREATE TABLE system_settings (
	id VARCHAR(100) NOT NULL,
	google_api_key VARCHAR(100),
	wunderground_api_key VARCHAR(100)
);


ALTER TABLE user ADD CONSTRAINT user_PK PRIMARY KEY (id);
ALTER TABLE user ADD CONSTRAINT user_email_address_UK UNIQUE KEY (email_address);
ALTER TABLE credential ADD CONSTRAINT credential_PK PRIMARY KEY (id);
ALTER TABLE authentication ADD CONSTRAINT authentication_PK PRIMARY KEY (user_id);
ALTER TABLE organisation ADD CONSTRAINT organisation_PK PRIMARY KEY (id);
ALTER TABLE organisation_location ADD CONSTRAINT organisation_location_PK PRIMARY KEY (id);
ALTER TABLE route ADD CONSTRAINT route_PK PRIMARY KEY (id);
ALTER TABLE scored_route ADD CONSTRAINT scored_route_PK PRIMARY KEY (id);
ALTER TABLE office_day ADD CONSTRAINT office_day_PK PRIMARY KEY (id);
ALTER TABLE office_day ADD CONSTRAINT office_day_UK UNIQUE KEY (day_of_week,user_id);
ALTER TABLE .system_settings ADD CONSTRAINT system_settings_PK PRIMARY KEY (id);

ALTER TABLE user ADD CONSTRAINT user_organisation_FK FOREIGN KEY (organisation_id) REFERENCES organisation(id);
ALTER TABLE user ADD CONSTRAINT user_organisation_location_FK FOREIGN KEY (default_location_id) REFERENCES organisation_location(id);
ALTER TABLE credential ADD CONSTRAINT credential_user_FK FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE authentication ADD CONSTRAINT authentication_user_FK FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE organisation_location ADD CONSTRAINT organisation_location_organisation_FK FOREIGN KEY (organisation_id) REFERENCES organisation(id);
ALTER TABLE route ADD CONSTRAINT route_route_FK FOREIGN KEY (parent_route_id) REFERENCES route(id);
ALTER TABLE scored_route ADD CONSTRAINT scored_route_route_FK FOREIGN KEY (route_id) REFERENCES route(id);
ALTER TABLE scored_route ADD CONSTRAINT scored_route_user_FK FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE office_day ADD CONSTRAINT office_day_user_FK FOREIGN KEY (user_id) REFERENCES user(id);
