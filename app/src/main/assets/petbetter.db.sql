BEGIN TRANSACTION;
CREATE TABLE "veterinarians" (
	`_id`	INTEGER,
	`user_id`	INTEGER,
	`specialty`	TEXT,
	`rating`	REAL,
	`phone_num`	TEXT,
	PRIMARY KEY(`_id`)
);
INSERT INTO `veterinarians` (_id,user_id,specialty,rating,phone_num) VALUES (1,3,'Animal Behaviour',0.0,'098654321'),
 (2,1,'Veterinary Specialist',0.0,'0987654321');
CREATE TABLE "users" (
	`_id`	INTEGER,
	`first_name`	TEXT,
	`last_name`	TEXT,
	`mobile_num`	TEXT,
	`phone_num`	TEXT,
	`email`	TEXT,
	`password`	TEXT,
	`age`	INTEGER,
	`user_type`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `users` (_id,first_name,last_name,mobile_num,phone_num,email,password,age,user_type) VALUES (1,'Edward','Tiro','9152794135','123456890','edward_tiro@dlsu.edu.ph','123',20,1),
 (2,'Kristian','Sisayan','9567761376','98765432','kristian_sisayan@dlsu.edu.ph','113',20,2),
 (3,'John','Ivanhoe','','8704421','john_ivanhoe@gmail.com','1234',30,1);
CREATE TABLE `topics` (
	`_id`	INTEGER,
	`creator_id`	INTEGER,
	`topic_name`	TEXT,
	`topic_desc`	TEXT,
	`date_created`	TEXT,
	`is_deleted`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `topics` (_id,creator_id,topic_name,topic_desc,date_created,is_deleted) VALUES (1,0,'General','Default',NULL,0),
 (2,1,'For dog lovers only','Basically everything about dogs are welcome.',NULL,0);
CREATE TABLE `ratings` (
	`_id`	INTEGER,
	`rater_id`	INTEGER,
	`rated_id`	INTEGER,
	`rating`	REAL,
	`comment`	TEXT,
	`rating_type`	INTEGER,
	`date_created`	TEXT,
	`is_deleted`	INTEGER,
	PRIMARY KEY(`_id`)
);
CREATE TABLE "posts" (
	`_id`	INTEGER,
	`user_id`	INTEGER,
	`topic_name`	TEXT,
	`topic_content`	TEXT,
	`topic_id`	INTEGER,
	`date_created`	TEXT,
	`is_deleted`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `posts` (_id,user_id,topic_name,topic_content,topic_id,date_created,is_deleted) VALUES (1,3,'Come visit my clinic!','Hi guys! I''m John Ivanhoe. If you are a pet owner you may want to visit my clinic for regular check ups.',1,NULL,0),
 (2,1,'Hi guys! Looking for a doctor to treat my dog.','Could I get suggestions as to who I could contact or where to go?',1,NULL,0),
 (3,1,'I love dogs','Hi guys! I love dogs',2,NULL,0);
CREATE TABLE "postreps" (
	`_id`	INTEGER,
	`user_id`	INTEGER,
	`post_id`	INTEGER,
	`parent_id`	INTEGER,
	`rep_content`	TEXT,
	`date_performed`	TEXT,
	`is_deleted`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `postreps` (_id,user_id,post_id,parent_id,rep_content,date_performed,is_deleted) VALUES (1,2,1,0,'Ok I will when I have the time.',NULL,0),
 (2,3,2,0,'I''m a doctor! :)',NULL,0),
 (3,2,1,1,'We give free treats~',NULL,0),
 (4,1,1,1,'Loking forward to this. :D',NULL,0);
CREATE TABLE "notifications" (
	`_id`	INTEGER,
	`user_id`	INTEGER,
	`doer_id`	INTEGER,
	`is_read`	INTEGER,
	`type`	INTEGER,
	`date_performed`	TEXT,
	`source_id`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `notifications` (_id,user_id,doer_id,is_read,type,date_performed,source_id) VALUES (1,1,2,0,2,NULL,2),
 (2,1,3,0,1,NULL,2);
CREATE TABLE `notification_type` (
	`_id`	INTEGER,
	`type`	TEXT,
	PRIMARY KEY(`_id`)
);
INSERT INTO `notification_type` (_id,type) VALUES (1,'Post'),
 (2,'Message');
CREATE TABLE "messages" (
	`_id`	INTEGER,
	`user_one`	INTEGER,
	`user_two`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `messages` (_id,user_one,user_two) VALUES (1,3,1),
 (2,1,2);
CREATE TABLE "messagereps" (
	`_id`	INTEGER,
	`user_id`	INTEGER,
	`message_id`	INTEGER,
	`rep_content`	TEXT,
	`is_sent`	INTEGER,
	`date_performed`	TEXT,
	PRIMARY KEY(`_id`)
);
INSERT INTO `messagereps` (_id,user_id,message_id,rep_content,is_sent,date_performed) VALUES (1,3,1,'Are you a pet owner?',1,NULL),
 (2,2,2,'Hi!',1,NULL),
 (3,1,2,'Yes? :D',1,NULL);
CREATE TABLE "markers" (
	`_id`	INTEGER,
	`bldg_name`	TEXT,
	`longitude`	NUMERIC,
	`latitude`	NUMERIC,
	`location`	TEXT,
	`user_id`	INTEGER,
	`type`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `markers` (_id,bldg_name,longitude,latitude,location,user_id,type) VALUES (1,'De La Salle University',120.9938468,14.5643338,NULL,1,1);
CREATE TABLE `followers` (
	`_id`	INTEGER,
	`topic_id`	INTEGER,
	`user_id`	INTEGER,
	PRIMARY KEY(`_id`)
);
INSERT INTO `followers` (_id,topic_id,user_id) VALUES (1,1,3),
 (2,1,1),
 (3,2,1),
 (4,1,2);
CREATE TABLE "facilities" (
	`_id`	INTEGER,
	`faci_name`	TEXT,
	`location`	TEXT,
	`hours_open`	TEXT,
	`hours_close`	TEXT,
	`contact_info`	TEXT,
	`vet_id`	INTEGER,
	`rating`	REAL,
	PRIMARY KEY(`_id`)
);
INSERT INTO `facilities` (_id,faci_name,location,hours_open,hours_close,contact_info,vet_id,rating) VALUES (1,'Ivanhoe Veterinary Clinic','Solenad 1 Nuvali, Santa Rosa, Laguna ','8:00','17:00','8704421',1,0.0);
CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US');
INSERT INTO `android_metadata` (locale) VALUES ('en_US');
COMMIT;
