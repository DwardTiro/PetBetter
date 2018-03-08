-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 02, 2018 at 04:33 PM
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
  `rating` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facilities`
--

INSERT INTO `facilities` (`faci_id`, `faci_name`, `location`, `hours_open`, `hours_close`, `contact_info`, `vet_id`, `rating`) VALUES
(1, 'Ivanhoe Veterinary Clinic', 'Solenad 1 Nuvali, Santa Rosa, Laguna ', '8:00', '17:00', '8704421', 1, 4),
(2, 'Bookmark', 'Manila', '1:00', '2:00', '', 1, 3),
(58, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 1),
(59, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(60, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(61, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(62, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(63, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(64, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(65, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(66, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(67, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(68, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(69, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2),
(70, 'Another Clinic', 'Somewhere in Phil', '8:00', '17:00', '8765432', 3, 2);

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
(164, 1, 1),
(165, 2, 2),
(166, 1, 3),
(167, 1, 1),
(168, 2, 1),
(169, 1, 3),
(170, 1, 1),
(171, 2, 1),
(172, 1, 3),
(173, 1, 1),
(174, 2, 1),
(175, 1, 3),
(176, 1, 1),
(177, 2, 1),
(178, 1, 3),
(179, 1, 1),
(180, 2, 1),
(181, 1, 3),
(182, 1, 1),
(183, 2, 1),
(184, 1, 3),
(185, 1, 1),
(186, 2, 1),
(187, 1, 3),
(188, 1, 1),
(189, 2, 1),
(190, 1, 3),
(191, 1, 1),
(192, 2, 1),
(193, 1, 3),
(194, 1, 1),
(195, 2, 1),
(196, 1, 3),
(197, 1, 1),
(198, 2, 1),
(199, 1, 3),
(200, 1, 1),
(201, 2, 1),
(202, 1, 3),
(203, 1, 1),
(204, 2, 1),
(205, 1, 3),
(206, 1, 1),
(207, 2, 1),
(208, 1, 3),
(209, 1, 1),
(210, 2, 1),
(211, 1, 3),
(212, 1, 1),
(213, 2, 1),
(214, 1, 3),
(215, 1, 1),
(216, 2, 1);

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
(58, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(59, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(60, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(61, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(62, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(63, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(64, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(65, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(66, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(67, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(68, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(69, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(70, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(71, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(72, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(73, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(74, 'Another Location', 120.9938468, 15, '', 1, 1, 0),
(75, 'Another Location', 120.9938468, 15, '', 1, 1, 0);

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
  `message_photo` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messagereps`
--

INSERT INTO `messagereps` (`_id`, `user_id`, `sender_id`, `message_id`, `rep_content`, `is_sent`, `date_performed`, `message_photo`) VALUES
(40, 1, 2, 2, 'hi', 1, '2018-03-01T14:44:11Z', NULL),
(41, 1, 2, 2, 'eyyy', 1, '2018-03-01T15:07:55Z', NULL),
(42, 1, 2, 2, 'watup', 1, '2018-03-01T15:09:16Z', NULL),
(43, 1, 2, 2, 'eyy yoh~', 1, '2018-03-01T15:12:44Z', NULL),
(44, 1, 2, 2, 'heybruh', 1, '2018-03-01T15:16:49Z', NULL),
(49, 1, 2, 2, 'Lodi~', 1, NULL, NULL),
(74, 1, 2, 2, 'first test', 1, NULL, NULL),
(75, 1, 2, 2, 'test2', 1, NULL, NULL),
(89, 1, 3, 1, 'Hi!', 1, NULL, NULL),
(90, 2, 1, 2, 'hi dude', 1, '2018-03-02T15:35:00Z', NULL),
(94, 2, 1, 2, 'wassap', 1, '2018-03-02T15:53:53Z', NULL),
(95, 2, 1, 2, 'eyy watup', 1, '2018-03-02T15:56:50Z', NULL),
(96, 2, 1, 2, 'meng', 1, '2018-03-02T16:04:17Z', NULL),
(97, 2, 1, 2, 'yoh?', 1, '2018-03-02T16:26:58Z', NULL),
(98, 1, 2, 2, 'eyyy', 1, '2018-03-02T16:04:17Z', NULL),
(99, 1, 2, 2, 'eyyy', 1, '2018-03-02T16:04:17Z', NULL),
(100, 1, 2, 2, 'eyyy', 1, '2018-03-02T16:04:17Z', NULL),
(102, 1, 2, 2, 'working now bois', 1, '2018-03-02T16:04:17Z', NULL),
(103, 1, 2, 2, 'still working?', 1, '2018-03-02T16:04:17Z', NULL),
(104, 1, 2, 2, 'still working??', 1, '2018-03-02T16:04:17Z', NULL),
(105, 1, 2, 2, 'still working???', 1, '2018-03-02T16:04:17Z', NULL);

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
(2, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `doer_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `date_performed` text,
  `source_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`_id`, `user_id`, `doer_id`, `type`, `date_performed`, `source_id`) VALUES
(1, 1, 2, 2, '', 2),
(2, 1, 3, 1, '', 2),
(27, 1, 2, 1, NULL, 2),
(28, 1, 2, 1, NULL, 2),
(29, 1, 2, 1, NULL, 2),
(30, 1, 2, 1, NULL, 2),
(31, 1, 2, 1, NULL, 2),
(32, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(36, 1, 2, 1, NULL, 1),
(38, 1, 3, 1, NULL, 3),
(39, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(40, 1, 2, 1, NULL, 1),
(42, 1, 2, 3, NULL, 2),
(43, 1, 2, 4, NULL, 163),
(44, 2, 1, 2, '2018-03-01T14:44:11Z', 2),
(45, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(46, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(47, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(48, 2, 1, 2, '2018-03-01T15:07:55Z', 2),
(49, 2, 1, 2, '2018-03-01T15:09:16Z', 2),
(50, 2, 1, 2, '2018-03-01T15:12:44Z', 2),
(51, 2, 1, 2, '2018-03-01T15:16:49Z', 2),
(52, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(56, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(61, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(62, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(63, 1, 2, 2, '2018-02-19T17:42:35Z', 3),
(64, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(84, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(85, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(86, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(87, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(88, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(89, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(90, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(91, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(92, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(93, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(94, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(95, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(96, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(97, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(98, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(99, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(100, 1, 2, 1, '2018-02-19T17:42:35Z', 3),
(101, 2, 1, 2, '2018-03-02T15:35:00Z', 2),
(105, 2, 1, 2, '2018-03-02T15:53:53Z', 1),
(106, 2, 1, 2, '2018-03-02T15:56:50Z', 0),
(107, 2, 1, 2, '2018-03-02T16:04:17Z', 2),
(108, 2, 1, 2, '2018-03-02T16:26:58Z', 2),
(109, 1, 2, 2, '2018-03-02T16:04:17Z', 2),
(110, 1, 2, 2, '2018-03-02T16:04:17Z', 2),
(111, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(112, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(113, 1, 2, 2, '2018-03-01T16:34:02Z\r\n', 11),
(114, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(115, 1, 2, 2, '2018-03-01T16:34:02Z', 11),
(116, 1, 2, 2, '2018-03-01T16:34:02Z', 11);

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
(20, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(21, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(22, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(23, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(24, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(25, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(26, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(27, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(28, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(29, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(30, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(31, 1, 'Sue', 'Cat', 'Siamese', 5, 5),
(32, 1, 'Sue', 'Cat', 'Siamese', 5, 5);

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
(8, 1, 1, 1, 'Wew', '', 0),
(9, 1, 1, 1, 'Wew', '', 0),
(10, 1, 1, 1, 'Wew', '', 0),
(11, 1, 1, 1, 'Wew', '', 0),
(12, 1, 1, 1, 'Wew', '', 0),
(13, 1, 1, 1, 'Wew', '', 0),
(14, 1, 1, 1, 'Wew', '', 0),
(15, 1, 1, 1, 'Wew', '', 0),
(16, 1, 1, 1, 'Wew', '', 0),
(17, 1, 1, 1, 'Wew', '', 0),
(18, 1, 1, 1, 'Wew', '', 0),
(19, 1, 1, 1, 'Wew', '', 0),
(20, 1, 1, 1, 'Wew', '', 0),
(21, 1, 1, 1, 'Wew', '', 0);

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
(4, 2, 'Hey!', 'Hi!', 2, '', 0),
(5, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(6, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(7, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(8, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(9, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(10, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(11, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0),
(12, 4, 'Pets update!', 'So I bought a cat and I named her Sue.', 1, NULL, 0);

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
(1, 2, 1, 4, 'Very nice.', 1, '', 0);

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
(3, 1, 'Grooming', 100, 0),
(4, 1, 'Consultation', 200, 0),
(5, 1, 'Grooming', 100, 0),
(6, 1, 'Consultation', 200, 0),
(7, 1, 'Grooming', 100, 0),
(8, 1, 'Consultation', 200, 0),
(9, 1, 'Grooming', 100, 0),
(10, 1, 'Consultation', 200, 0),
(11, 1, 'Grooming', 100, 0),
(12, 1, 'Consultation', 200, 0),
(13, 1, 'Grooming', 100, 0),
(14, 1, 'Consultation', 200, 0),
(15, 1, 'Grooming', 100, 0),
(16, 1, 'Consultation', 200, 0),
(17, 1, 'Grooming', 100, 0),
(18, 1, 'Consultation', 200, 0),
(19, 1, 'Grooming', 100, 0),
(20, 1, 'Consultation', 200, 0),
(21, 1, 'Grooming', 100, 0),
(22, 1, 'Consultation', 200, 0),
(23, 1, 'Grooming', 100, 0),
(24, 1, 'Consultation', 200, 0),
(25, 1, 'Grooming', 100, 0),
(26, 1, 'Consultation', 200, 0),
(27, 1, 'Grooming', 100, 0),
(28, 1, 'Consultation', 200, 0),
(29, 1, 'Grooming', 100, 0),
(30, 1, 'Consultation', 200, 0);

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
(1, 0, 'General', 'Default', '', 0),
(2, 1, 'For dog lovers only', 'Basically everything about dogs are welcome.', '', 0),
(3, 3, 'Another topic', 'For general use', '', 0),
(4, 2, 'Cats are better', 'They just are', NULL, 0),
(5, 2, 'Cats are better', 'They just are', NULL, 0),
(6, 2, 'Cats are better', 'They just are', NULL, 0),
(7, 2, 'Cats are better', 'They just are', NULL, 0),
(8, 2, 'Cats are better', 'They just are', NULL, 0),
(9, 2, 'Cats are better', 'They just are', NULL, 0),
(10, 2, 'Cats are better', 'They just are', NULL, 0),
(11, 2, 'Cats are better', 'They just are', NULL, 0);

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
(1, 'Edward', 'Tiro', '9152794135', '123456890', 'edward_tiro@dlsu.edu.ph', '123', 20, 1, NULL),
(2, 'Kristian', 'Sisayan', '9567761376.', '9876543', 'kristian_sisayan@dlsu.edu.ph', '113', 20, 2, NULL),
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
(1, 3, 'Animal Behaviour', 4, '098654321'),
(2, 1, 'Veterinary Specialist', 0, '0987654321'),
(60, 4, 'Breeding', 1.5, ''),
(61, 2, 'Animal Research', 1, '12345678');

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
  MODIFY `faci_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;
--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=217;
--
-- AUTO_INCREMENT for table `markers`
--
ALTER TABLE `markers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;
--
-- AUTO_INCREMENT for table `messagereps`
--
ALTER TABLE `messagereps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;
--
-- AUTO_INCREMENT for table `pets`
--
ALTER TABLE `pets`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- AUTO_INCREMENT for table `postreps`
--
ALTER TABLE `postreps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;
--
-- AUTO_INCREMENT for table `topics`
--
ALTER TABLE `topics`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `veterinarians`
--
ALTER TABLE `veterinarians`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
