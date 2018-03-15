-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 12, 2018 at 08:24 AM
-- Server version: 10.1.10-MariaDB
-- PHP Version: 7.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `petbetter`
--

-- --------------------------------------------------------

--
-- Table structure for table `facilities`
--

CREATE TABLE `facilities` (
  `faci_id` int(11) NOT NULL,
  `faci_name` text NOT NULL,
  `location` text NOT NULL,
  `hours_open` text NOT NULL,
  `hours_close` text NOT NULL,
  `contact_info` text NOT NULL,
  `vet_id` int(11) NOT NULL,
  `rating` double NOT NULL,
  `faci_photo` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facilities`
--

INSERT INTO `facilities` (`faci_id`, `faci_name`, `location`, `hours_open`, `hours_close`, `contact_info`, `vet_id`, `rating`, `faci_photo`) VALUES
(1, 'Ivanhoe Veterinary Clinic', 'Solenad 1 Nuvali, Santa Rosa, Laguna ', '8:00', '17:00', '8704421', 1, 1.8333334, ''),
(2, 'Bookmark', 'Manila', '', '', '', 1, 4, '');

-- --------------------------------------------------------

--
-- Table structure for table `followers`
--

CREATE TABLE `followers` (
  `_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `followers`
--

INSERT INTO `followers` (`_id`, `topic_id`, `user_id`) VALUES
(163, 1, 3),
(165, 2, 1),
(166, 1, 2),
(417, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `markers`
--

CREATE TABLE `markers` (
  `_id` int(11) NOT NULL,
  `bldg_name` text NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `location` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `faci_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `markers`
--

INSERT INTO `markers` (`_id`, `bldg_name`, `longitude`, `latitude`, `location`, `user_id`, `type`, `faci_id`) VALUES
(1, 'De La Salle University', 121, 15, '', 1, 1, 0),
(3, 'Facility', 122, 14, '', 1, 2, 0),
(139, 'Another Location', 120.9938468, 15, '', 1, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `messagereps`
--

CREATE TABLE `messagereps` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `message_id` int(11) DEFAULT NULL,
  `rep_content` text,
  `is_sent` int(11) DEFAULT NULL,
  `date_performed` text,
  `message_photo` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messagereps`
--

INSERT INTO `messagereps` (`_id`, `user_id`, `sender_id`, `message_id`, `rep_content`, `is_sent`, `date_performed`, `message_photo`) VALUES
(2, 1, 2, 10, 'Hi!', 1, '', NULL),
(3, 1, 2, 10, 'Yes? :D', 1, '', NULL),
(4, 1, 1, 1, '?', 1, '', NULL),
(29, 3, 1, 1, '!!', 1, NULL, NULL),
(30, 3, 1, 1, '!!', 1, NULL, NULL),
(31, 3, 1, 1, '!!', 1, NULL, NULL),
(33, 3, 1, 1, 'wassaup', 1, NULL, NULL),
(34, 1, 2, 10, '!!', 1, NULL, NULL),
(35, 3, 1, 1, 'yoh', 1, '2018-03-06T17:35:15Z', NULL),
(36, 3, 1, 1, 'eyyy', 1, '2018-03-06T17:35:15Z', NULL),
(37, 1, 3, 1, 'eyyy', 1, '2018-03-06T17:35:15Z', NULL),
(38, 3, 1, 1, 'sinigang', 1, '2018-03-06T23:53:07Z', NULL),
(39, 3, 1, 1, 'yoh', 1, '2018-03-06T23:57:49Z', NULL),
(40, 3, 1, 1, 'WASSAP', 1, '2018-03-07T00:00:19Z', NULL),
(41, 3, 1, 1, 'MEN', 1, '2018-03-07T00:04:46Z', NULL),
(42, 3, 1, 1, 'YOH', 1, '2018-03-07T00:08:54Z', NULL),
(43, 3, 1, 1, 'wat', 1, '2018-03-07T00:10:31Z', NULL),
(44, 3, 1, 1, 'working?', 1, '2018-03-07T00:11:34Z', 'uploads/messagereps/fac0532.jpg'),
(45, 3, 1, 1, 'eyyy john!!', 1, '2018-03-07T16:33:51Z', NULL),
(46, 1, 3, 1, 'eyy edward', 1, '2018-03-07T16:36:14Z', NULL),
(47, 3, 1, 1, 'didn''t get a notif. :(', 1, '2018-03-07T16:36:34Z', 'uploads/messagereps/9560584.jpg'),
(48, 1, 3, 1, 'sad lah', 1, '2018-03-07T16:38:45Z', NULL),
(49, 3, 1, 1, 'do you get notifs?', 1, '2018-03-07T16:38:27Z', NULL),
(50, 1, 3, 1, 'yup', 1, '2018-03-07T16:40:15Z', NULL),
(51, 1, 3, 1, 'yoh', 1, '2018-03-07T16:41:38Z', 'uploads/messagereps/649a347.jpg'),
(52, 1, 3, 1, 'test', 1, '2018-03-07T17:09:01Z', 'uploads/messagereps/1533e36.jpg'),
(53, 1, 3, 1, 'test', 1, '2018-03-07T17:09:01Z', 'uploads/messagereps/cb1791d.jpg'),
(54, 1, 3, 1, '1', 1, '2018-03-07T17:26:25Z', 'uploads/messagereps/31163fe.jpg'),
(55, 1, 3, 1, '2', 1, '2018-03-07T17:34:17Z', 'uploads/messagereps/b8e13fb.jpg'),
(56, 1, 3, 1, 'yoh', 1, '2018-03-07T17:49:04Z', NULL),
(57, 1, 3, 1, 'eyy', 1, '2018-03-07T17:50:22Z', NULL),
(58, 1, 3, 1, 'check', 1, '2018-03-07T17:50:52Z', NULL),
(59, 1, 3, 1, '?', 1, '2018-03-07T17:51:19Z', NULL),
(60, 3, 1, 1, 'g', 1, '2018-03-07T17:50:20Z', NULL),
(61, 3, 1, 1, 'j', 1, '2018-03-07T18:02:42Z', 'uploads/messagereps/0cb5ebb.jpg'),
(62, 2, 1, 10, 'ee', 1, '2018-03-08T02:59:33Z', 'uploads/messagereps/6f942b9.jpg'),
(64, 2, 1, 10, 'wfff', 1, '2018-03-08T03:56:50Z', 'uploads/messagereps/965052c.jpg'),
(65, 2, 1, 10, 'lol', 1, '2018-03-08T03:57:42Z', 'uploads/messagereps/883e881.jpg'),
(66, 2, 1, 10, 'eyy', 1, '2018-03-08T04:01:45Z', 'uploads/messagereps/ae0eb3e.jpg'),
(67, 2, 1, 10, 'dgsgs', 1, '2018-03-08T04:02:35Z', 'uploads/messagereps/ccf4769.jpg'),
(70, 4, 2, 14, ' Hi doc', 1, '2018-03-12T01:46:49Z', NULL),
(71, 3, 2, 15, 'JOHNNNN BY BOIIIIII', 1, '2018-03-12T01:47:23Z', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `_id` int(11) NOT NULL,
  `user_one` int(11) NOT NULL,
  `user_two` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`_id`, `user_one`, `user_two`) VALUES
(1, 3, 1),
(10, 1, 2),
(14, 4, 2),
(15, 3, 2);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `doer_id` int(11) DEFAULT NULL,
  `is_read` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `date_performed` text,
  `source_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pets`
--

CREATE TABLE `pets` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `name` text,
  `classification` text,
  `breed` text,
  `height` float DEFAULT NULL,
  `weight` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pets`
--

INSERT INTO `pets` (`_id`, `user_id`, `name`, `classification`, `breed`, `height`, `weight`) VALUES
(1, 2, 'Ming Ming', 'Cat', 'Persian', 4, 4),
(19, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(100, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(101, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(102, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(103, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(104, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(105, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(106, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(107, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(108, 1, 'Sue', 'Cat', 'Siamese', 5, 5);

-- --------------------------------------------------------

--
-- Table structure for table `postreps`
--

CREATE TABLE `postreps` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `rep_content` text,
  `date_performed` text,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `postreps`
--

INSERT INTO `postreps` (`_id`, `user_id`, `post_id`, `parent_id`, `rep_content`, `date_performed`, `is_deleted`) VALUES
(1, 2, 1, 0, 'Ok I will when I have the time.', '', 0),
(2, 3, 2, 0, 'I''m a doctor! :)', '', 0),
(3, 2, 1, 1, 'We give free treats~', '', 0),
(4, 1, 1, 1, 'Loking forward to this. :D', '', 0),
(5, 1, 2, 1, 'Yay~', '', 1),
(14, 1, 3, 0, 'hi', '2018-03-04T01:36:00Z', 0),
(15, 1, 3, 5, 'hello\n', '2018-03-04T01:37:10Z', 1),
(16, 2, 12, 0, 'wazzap\r\n', '2018-03-04T01:37:10Z', 0),
(17, 2, 12, 0, 'wazzap boi\r\n', '2018-03-04T01:37:10Z', 0),
(18, 1, 12, 0, 'ye buiiii', '2018-03-05T01:54:23Z', 1),
(19, 1, 12, 0, 'weird stuff', '2018-03-05T01:57:04Z', 0),
(20, 1, 12, 0, 'tell me about it', '2018-03-05T01:57:04Z', 0),
(21, 1, 12, 0, 'o rly?', '2018-03-05T01:57:04Z', 0),
(22, 2, 12, 0, 'yuh', '2018-03-05T01:57:04Z', 0);

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `topic_name` text,
  `topic_content` text,
  `topic_id` int(11) DEFAULT NULL,
  `date_created` text,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`_id`, `user_id`, `topic_name`, `topic_content`, `topic_id`, `date_created`, `is_deleted`) VALUES
(1, 3, 'Come visit my clinic!', 'Hi guys! I''m John Ivanhoe. If you are a pet owner you may want to visit my clinic for regular check ups.', 1, '', 0),
(2, 1, 'Hi guys! Looking for a doctor to treat my dog.', 'Could I get suggestions as to who I could contact or where to go?', 1, '', 0),
(3, 1, 'I love dogs', 'Hi guys! I love dogs', 2, '', 0),
(4, 2, 'Hey!', 'Hi!', 2, '', 1),
(91, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(92, 1, 'Eyy', 'Yoh', 1, '2018-03-09T20:30:58Z', 0),
(94, 1, 'Eyy2', 'Yoh2', 1, '2018-03-09T20:30:58Z', 0),
(95, 1, 'Eyy3', 'watup', 1, '2018-03-09T20:47:42Z', 0),
(96, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(97, 1, 'Hello ', 'Hi', 1, '2018-03-09T20:56:59Z', 0),
(98, 1, 'yuu', 'yuyuyuu', 1, '2018-03-09T20:59:06Z', 0),
(99, 1, 'don''t duplicate huuu', 'plssss', 1, '2018-03-09T21:12:48Z', 0),
(100, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(101, 1, 'ehuedh', 'wdeudh', 1, '2018-03-09T21:14:59Z', 0),
(102, 1, 'We oki?', 'oki?', 1, '2018-03-09T21:17:37Z', 0),
(103, 1, 'Oks?', 'Manoks?', 1, '2018-03-09T21:20:59Z', 0),
(104, 1, 'test', 'test', 1, '2018-03-09T21:26:06Z', 0),
(105, 1, 'check', 'check', 1, '2018-03-09T21:27:27Z', 0),
(106, 1, 'We cool?', '???', 1, '2018-03-09T21:36:20Z', 0),
(107, 1, 'We okay dude?', '???', 1, '2018-03-09T21:36:20Z', 0),
(108, 1, 'plsssss', '???', 1, '2018-03-09T21:36:20Z', 0);

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `_id` int(11) NOT NULL,
  `rater_id` int(11) NOT NULL,
  `rated_id` int(11) NOT NULL,
  `rating` double NOT NULL,
  `comment` text NOT NULL,
  `rating_type` int(11) NOT NULL,
  `date_created` text NOT NULL,
  `is_deleted` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ratings`
--

INSERT INTO `ratings` (`_id`, `rater_id`, `rated_id`, `rating`, `comment`, `rating_type`, `date_created`, `is_deleted`) VALUES
(1, 2, 1, 4, 'Very nice.', 1, '', 0),
(2, 4, 1, 1, 'Very bad', 1, '', 0),
(3, 2, 58, 2, '', 1, '2018-03-11T03:45:07Z', 0),
(6, 4, 1, 1, '', 2, '2018-03-11T04:27:54Z', 0),
(7, 6, 1, 1.5, '', 2, '2018-03-11T04:27:54Z', 0),
(9, 6, 1, 4, '', 1, '2018-03-11T11:25:50Z', 0);

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `_id` int(11) NOT NULL,
  `faci_id` int(11) DEFAULT NULL,
  `service_name` text,
  `service_price` float DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`_id`, `faci_id`, `service_name`, `service_price`, `is_deleted`) VALUES
(0, 1, 'Grooming', 100, 0),
(3, 1, 'Grooming', 100, 0),
(4, 1, 'Consultation', 200, 0),
(169, 1, 'Grooming', 100, 0),
(170, 1, 'Consultation', 200, 0),
(171, 1, 'Grooming', 100, 0),
(172, 1, 'Consultation', 200, 0),
(173, 1, 'Grooming', 100, 0),
(174, 1, 'Consultation', 200, 0),
(175, 1, 'Grooming', 100, 0),
(176, 1, 'Consultation', 200, 0),
(177, 1, 'Grooming', 100, 0),
(178, 1, 'Consultation', 200, 0),
(179, 1, 'Grooming', 100, 0),
(180, 1, 'Consultation', 200, 0),
(181, 1, 'Grooming', 100, 0),
(182, 1, 'Consultation', 200, 0),
(183, 1, 'Grooming', 100, 0),
(184, 1, 'Consultation', 200, 0);

-- --------------------------------------------------------

--
-- Table structure for table `topics`
--

CREATE TABLE `topics` (
  `_id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `topic_name` text,
  `topic_desc` text,
  `date_created` text,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `topics`
--

INSERT INTO `topics` (`_id`, `creator_id`, `topic_name`, `topic_desc`, `date_created`, `is_deleted`) VALUES
(0, 2, 'Cats are better', 'They just are', NULL, 0),
(1, 0, 'General', 'Default', '', 0),
(2, 1, 'For dog lovers only', 'Basically everything about dogs are welcome.', '', 0),
(3, 3, 'Another topic', 'For general use', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `upvotes`
--

CREATE TABLE `upvotes` (
  `_id` int(11) NOT NULL,
  `feed_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  `type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `upvotes`
--

INSERT INTO `upvotes` (`_id`, `feed_id`, `user_id`, `value`, `type`) VALUES
(1, 1, 1, -1, 1),
(2, 1, 3, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` text NOT NULL,
  `last_name` text NOT NULL,
  `mobile_num` text NOT NULL,
  `phone_num` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `age` int(11) NOT NULL,
  `user_type` int(11) NOT NULL,
  `user_photo` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `mobile_num`, `phone_num`, `email`, `password`, `age`, `user_type`, `user_photo`) VALUES
(1, 'Edward', 'Tiro', '9152794135.', '1234568', 'edward_tiro@dlsu.edu.ph', '123', 20, 1, 'uploads/users/1534184.jpg'),
(2, 'Kristian', 'Sisayan', '9567761376.', '9876543', 'kristian_sisayan@dlsu.edu.ph', '113', 20, 2, 'uploads/users/238f646.jpg'),
(3, 'John', 'Ivanhoe', '', '8704421', 'john_ivanhoe@gmail.com', '1234', 30, 1, NULL),
(4, 'John', 'Dion', '09152791111', '1234567', 'john_dion@gmail.com', '111', 0, 2, NULL),
(5, 'Kristian', 'Sisayan', '', '', 'sisayan.kristian@gmail.com', 'glmrklls', 0, 1, NULL),
(6, 'John', 'Woa', '', '', 'johnwoa@gmail.com', '123', 0, 2, NULL),
(7, 'Gap', 'Blue', '', '', 'gblue@gmail.com', 'gap', 0, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `veterinarians`
--

CREATE TABLE `veterinarians` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `specialty` text NOT NULL,
  `rating` double NOT NULL,
  `phone_num` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `veterinarians`
--

INSERT INTO `veterinarians` (`_id`, `user_id`, `specialty`, `rating`, `phone_num`) VALUES
(1, 3, 'Animal Behaviour', 3, '098654321'),
(2, 1, 'Veterinary Specialist', 0, '0987654321'),
(58, 4, 'Breeding', 2, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `facilities`
--
ALTER TABLE `facilities`
  ADD PRIMARY KEY (`faci_id`);

--
-- Indexes for table `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `markers`
--
ALTER TABLE `markers`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `messagereps`
--
ALTER TABLE `messagereps`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `pets`
--
ALTER TABLE `pets`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `postreps`
--
ALTER TABLE `postreps`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `upvotes`
--
ALTER TABLE `upvotes`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `veterinarians`
--
ALTER TABLE `veterinarians`
  ADD PRIMARY KEY (`_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `facilities`
--
ALTER TABLE `facilities`
  MODIFY `faci_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=148;
--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=418;
--
-- AUTO_INCREMENT for table `markers`
--
ALTER TABLE `markers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=148;
--
-- AUTO_INCREMENT for table `messagereps`
--
ALTER TABLE `messagereps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=72;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;
--
-- AUTO_INCREMENT for table `pets`
--
ALTER TABLE `pets`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=109;
--
-- AUTO_INCREMENT for table `postreps`
--
ALTER TABLE `postreps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;
--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;
--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=185;
--
-- AUTO_INCREMENT for table `topics`
--
ALTER TABLE `topics`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;
--
-- AUTO_INCREMENT for table `upvotes`
--
ALTER TABLE `upvotes`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `veterinarians`
--
ALTER TABLE `veterinarians`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
