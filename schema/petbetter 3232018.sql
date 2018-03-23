-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 23, 2018 at 07:26 AM
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
-- Table structure for table `bookmarks`
--

CREATE TABLE `bookmarks` (
  `_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `bookmark_type` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `facilities`
--

CREATE TABLE `facilities` (
  `faci_id` int(11) NOT NULL,
  `faci_name` varchar(100) NOT NULL,
  `location` varchar(200) NOT NULL,
  `hours_open` varchar(5) NOT NULL,
  `hours_close` varchar(5) NOT NULL,
  `contact_info` varchar(15) NOT NULL,
  `rating` double NOT NULL,
  `faci_photo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facilities`
--

INSERT INTO `facilities` (`faci_id`, `faci_name`, `location`, `hours_open`, `hours_close`, `contact_info`, `rating`, `faci_photo`) VALUES
(1, 'Ivanhoe Veterinary Clinic', 'Solenad 1 Nuvali, Santa Rosa, Laguna ', '8:00', '17:00', '8704421', 0, NULL),
(2, 'Bookmark', 'Manila', '', '', '', 0, NULL),
(57, 'Another Clinic', 'Somewhere in Phil', '0:00', '0:00', '87654321', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `facility_membership`
--

CREATE TABLE `facility_membership` (
  `_id` int(11) NOT NULL,
  `faci_id` int(11) NOT NULL,
  `vet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `followers`
--

CREATE TABLE `followers` (
  `_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `is_allowed` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `followers`
--

INSERT INTO `followers` (`_id`, `topic_id`, `user_id`, `is_allowed`) VALUES
(163, 1, 3, 1),
(168, 2, 3, 1),
(361, 2, 1, 1),
(368, 3, 2, 0),
(377, 2, 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `markers`
--

CREATE TABLE `markers` (
  `_id` int(11) NOT NULL,
  `bldg_name` varchar(100) NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `location` varchar(200) NOT NULL,
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
(139, 'getbetter', 14.261746839947, 121.04436926544, 'laguna', 1, 1, 1);

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
(68, 1, 2, 10, 'eyyy', 1, '2018-03-14T11:41:45Z', NULL),
(69, 2, 1, 10, 'yoh', 1, '2018-03-14T11:43:47Z', NULL),
(70, 1, 2, 10, 'meng', 1, '2018-03-14T11:47:33Z', NULL),
(71, 2, 1, 10, 'watup', 1, '2018-03-14T11:46:19Z', NULL),
(72, 1, 2, 10, 'Hi', 1, '2018-03-14T14:34:15Z', NULL),
(73, 1, 2, 10, 'Hi edward', 1, '2018-03-14T14:34:58Z', NULL),
(74, 1, 2, 10, 'Yo', 1, '2018-03-14T14:39:50Z', NULL),
(82, 1, 2, 10, 'hi', 1, '2018-03-20T10:37:56Z', NULL),
(86, 1, 2, 10, 'hi par', 1, '2018-03-20T11:35:54Z', NULL),
(87, 1, 2, 10, 'sup', 1, '2018-03-20T11:37:00Z', NULL),
(88, 2, 1, 10, 'oy bakit error', 1, '2018-03-20T11:36:07Z', NULL),
(89, 2, 1, 10, 'test', 1, '2018-03-20T11:44:30Z', NULL),
(90, 2, 1, 10, 'Test2', 1, '2018-03-20T11:46:22Z', NULL),
(91, 1, 2, 10, 'work pls thanks', 1, '2018-03-20T11:53:04Z', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `_id` int(11) NOT NULL,
  `user_one` int(11) NOT NULL,
  `user_two` int(11) NOT NULL,
  `is_allowed` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`_id`, `user_one`, `user_two`, `is_allowed`) VALUES
(1, 3, 1, 1),
(10, 1, 2, 1),
(19, 3, 2, 0),
(29, 6, 2, 0);

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
  `date_performed` varchar(50) DEFAULT NULL,
  `source_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`_id`, `user_id`, `doer_id`, `is_read`, `type`, `date_performed`, `source_id`) VALUES
(1, 1, 2, 1, 2, '', 2),
(181, 1, 2, 0, 2, '2018-03-14T14:34:15Z', 10),
(182, 1, 2, 0, 2, '2018-03-14T14:34:58Z', 10),
(183, 1, 2, 0, 2, '2018-03-14T14:39:50Z', 10),
(194, 3, 2, 0, 4, '2018-03-17T00:38:14Z', 3),
(195, NULL, 2, 0, 3, '2018-03-18T01:02:42Z', 1),
(196, 3, 2, 0, 1, '2018-03-18T12:58:21Z', 1),
(197, 3, 2, 0, 1, '2018-03-18T12:58:21Z', 1),
(198, 3, 2, 0, 1, '2018-03-18T12:58:26Z', 1),
(199, 3, 2, 0, 1, '2018-03-18T12:58:21Z', 1),
(200, 3, 2, 0, 1, '2018-03-18T12:58:26Z', 1),
(201, 3, 2, 0, 1, '2018-03-18T14:18:26Z', 1),
(202, 3, 2, 0, 1, '2018-03-18T14:18:26Z', 1),
(204, 6, 2, 0, 2, '2018-03-19T12:33:29Z', 0),
(205, 6, 2, 0, 2, '2018-03-19T12:37:39Z', 0),
(206, 6, 2, 0, 2, '2018-03-19T12:38:35Z', 0),
(207, 6, 2, 0, 2, '2018-03-19T12:41:42Z', 0),
(208, 6, 2, 0, 2, '2018-03-19T12:46:00Z', 0),
(209, 6, 2, 0, 2, '2018-03-19T12:49:27Z', 0),
(210, 6, 2, 0, 2, '2018-03-19T12:51:42Z', 0),
(211, NULL, 2, 0, 3, '2018-03-20T09:23:57Z', 2),
(212, NULL, 2, 0, 3, '2018-03-20T09:23:57Z', 2),
(213, NULL, 2, 0, 3, '2018-03-20T09:23:57Z', 2),
(214, NULL, 2, 0, 3, '2018-03-20T09:29:05Z', 2),
(215, NULL, 2, 0, 3, '2018-03-20T09:29:05Z', 2),
(216, NULL, 2, 0, 3, '2018-03-20T09:29:05Z', 2),
(217, NULL, 2, 0, 3, '2018-03-20T09:35:28Z', 2),
(218, NULL, 2, 0, 3, '2018-03-20T09:35:28Z', 2),
(219, NULL, 2, 0, 3, '2018-03-20T09:35:28Z', 2),
(220, 1, 2, 0, 2, '2018-03-20T10:37:56Z', 10),
(221, 6, 2, 0, 2, '2018-03-20T10:38:24Z', 0),
(222, 6, 2, 0, 2, '2018-03-20T10:40:46Z', 0),
(223, 1, 2, 0, 2, '2018-03-20T11:35:54Z', 10),
(224, 1, 2, 1, 2, '2018-03-20T11:37:00Z', 10),
(225, 2, 1, 0, 2, '2018-03-20T11:36:07Z', 10),
(226, 2, 1, 0, 2, '2018-03-20T11:44:30Z', 10),
(227, 2, 1, 0, 2, '2018-03-20T11:46:22Z', 10),
(228, 1, 2, 0, 2, '2018-03-20T11:53:04Z', 10),
(230, 1, 2, 0, 4, '2018-03-20T15:10:37Z', 2),
(231, 3, 1, 0, 1, '2018-03-20T19:34:09Z', 1),
(232, 3, 1, 0, 1, '2018-03-20T19:34:09Z', 1),
(233, NULL, 1, 0, 3, '2018-03-22T11:36:18Z', 2),
(234, NULL, 1, 0, 3, '2018-03-22T11:36:18Z', 2),
(235, NULL, 1, 0, 3, '2018-03-22T11:36:18Z', 2),
(236, NULL, 1, 0, 3, '2018-03-22T11:36:43Z', 2),
(237, NULL, 1, 0, 3, '2018-03-22T11:36:43Z', 2),
(238, NULL, 1, 0, 3, '2018-03-22T11:36:43Z', 2),
(239, NULL, 1, 0, 3, '2018-03-22T11:37:04Z', 2),
(240, NULL, 1, 0, 3, '2018-03-22T11:37:04Z', 2),
(241, NULL, 1, 0, 3, '2018-03-22T11:37:04Z', 2),
(242, NULL, 1, 0, 3, '2018-03-22T11:39:36Z', 2),
(243, NULL, 1, 0, 3, '2018-03-22T11:39:36Z', 2),
(244, NULL, 1, 0, 3, '2018-03-22T11:39:36Z', 2),
(245, NULL, 1, 0, 3, '2018-03-22T11:40:06Z', 2),
(246, NULL, 1, 0, 3, '2018-03-22T11:40:06Z', 2),
(247, NULL, 1, 0, 3, '2018-03-22T11:40:06Z', 2),
(248, NULL, 1, 0, 3, '2018-03-22T11:41:14Z', 2),
(249, NULL, 1, 0, 3, '2018-03-22T11:41:14Z', 2),
(250, NULL, 1, 0, 3, '2018-03-22T11:41:14Z', 2),
(251, NULL, 1, 0, 3, '2018-03-22T11:42:34Z', 2),
(252, NULL, 1, 0, 3, '2018-03-22T11:42:34Z', 2),
(253, NULL, 1, 0, 3, '2018-03-22T11:42:34Z', 2),
(254, NULL, 1, 0, 3, '2018-03-22T11:43:50Z', 2),
(255, NULL, 1, 0, 3, '2018-03-22T11:43:50Z', 2),
(256, NULL, 1, 0, 3, '2018-03-22T11:43:50Z', 2),
(257, NULL, 1, 0, 3, '2018-03-22T11:45:13Z', 2),
(258, NULL, 1, 0, 3, '2018-03-22T11:45:13Z', 2),
(259, NULL, 1, 0, 3, '2018-03-22T11:45:13Z', 2),
(260, NULL, 1, 0, 3, '2018-03-22T11:48:07Z', 2),
(261, NULL, 1, 0, 3, '2018-03-22T11:48:07Z', 2),
(262, NULL, 1, 0, 3, '2018-03-22T11:48:07Z', 2);

-- --------------------------------------------------------

--
-- Table structure for table `pending`
--

CREATE TABLE `pending` (
  `_id` int(11) NOT NULL,
  `foreign_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `is_approved` int(11) NOT NULL
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
(19, 1, 'Sue', 'Cat', 'Siamese', 5, 5);

-- --------------------------------------------------------

--
-- Table structure for table `postreps`
--

CREATE TABLE `postreps` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `rep_content` varchar(200) DEFAULT NULL,
  `date_performed` varchar(50) DEFAULT NULL,
  `postrep_photo` varchar(100) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `postreps`
--

INSERT INTO `postreps` (`_id`, `user_id`, `post_id`, `parent_id`, `rep_content`, `date_performed`, `postrep_photo`, `is_deleted`) VALUES
(1, 2, 1, 0, 'Ok I will when I have the time.', '', NULL, 0),
(2, 3, 2, 0, 'I''m a doctor! :)', '', NULL, 0),
(3, 2, 1, 1, 'We give free treats~', '', NULL, 0),
(4, 1, 1, 1, 'Loking forward to this. :D', '', NULL, 0),
(5, 1, 2, 1, 'Yay~', '', NULL, 1),
(14, 1, 3, 0, 'hi', '2018-03-04T01:36:00Z', NULL, 0),
(15, 1, 3, 5, 'hello\n', '2018-03-04T01:37:10Z', NULL, 1),
(16, 2, NULL, 0, 'wazzap\r\n', '2018-03-04T01:37:10Z', NULL, 0),
(17, 2, NULL, 0, 'wazzap boi\r\n', '2018-03-04T01:37:10Z', NULL, 0),
(18, 1, NULL, 0, 'ye buiiii', '2018-03-05T01:54:23Z', NULL, 1),
(19, 1, NULL, 0, 'weird stuff', '2018-03-05T01:57:04Z', NULL, 0),
(20, 1, NULL, 0, 'tell me about it', '2018-03-05T01:57:04Z', NULL, 0),
(21, 1, NULL, 0, 'o rly?', '2018-03-05T01:57:04Z', NULL, 0),
(22, 2, NULL, 0, 'yuh', '2018-03-05T01:57:04Z', NULL, 0),
(23, 2, 1, 0, 'hey', '2018-03-18T12:58:21Z', NULL, 0),
(24, 2, 1, 0, 'test', '2018-03-18T12:58:26Z', NULL, 1),
(25, 2, 1, 0, 'eyyy', '2018-03-18T14:18:26Z', NULL, 1),
(26, 1, 1, 0, 'https://www.google.com/search?ie=UTF-8&source=android-browser&q=pokemon', '2018-03-20T19:34:09Z', NULL, 0);

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
  `post_photo` text,
  `id_link` int(11) DEFAULT NULL,
  `id_type` int(11) NOT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`_id`, `user_id`, `topic_name`, `topic_content`, `topic_id`, `date_created`, `post_photo`, `id_link`, `id_type`, `is_deleted`) VALUES
(1, 3, 'Come visit my clinic!', 'Hi guys! I''m John Ivanhoe. If you are a pet owner you may want to visit my clinic for regular check ups.', 1, '', 'h', NULL, 0, 0),
(2, 1, 'Hi guys! Looking for a doctor to treat my dog.', 'Could I get suggestions as to who I could contact or where to go?', 1, '', 'e', NULL, 0, 0),
(3, 1, 'I love dogs', 'Hi guys! I love dogs', 2, '', 'l', NULL, 0, 0),
(4, 2, 'Hey!', 'Hi!', 2, '', 'p', NULL, 0, 1),
(8, 2, 'Trying image out', 'Successful image upload', 2, '2018-03-20T09:35:28Z', 'uploads/posts/a5ae355.jpg', 57, 2, 0),
(9, 1, 'vet', 'vet boi', 2, '2018-03-22T11:36:18Z', NULL, 1, 1, 0),
(12, 1, 'topic', 'topic po', 2, '2018-03-22T11:39:36Z', NULL, 1, 3, 0),
(18, 1, 'post', 'post par', 2, '2018-03-22T11:48:07Z', NULL, 2, 4, 0);

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
(2, 2, 2, 3, '', 1, '2018-03-20T11:27:03Z', 0),
(3, 1, 1, 3, '', 1, '2018-03-22T10:29:22Z', 0);

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `_id` int(11) NOT NULL,
  `faci_id` int(11) DEFAULT NULL,
  `service_name` varchar(50) DEFAULT NULL,
  `service_price` float DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`_id`, `faci_id`, `service_name`, `service_price`, `is_deleted`) VALUES
(3, 1, 'Grooming', 100, 0),
(4, 1, 'Consultation', 200, 0);

-- --------------------------------------------------------

--
-- Table structure for table `specialty`
--

CREATE TABLE `specialty` (
  `_id` int(11) NOT NULL,
  `specialization` varchar(50) NOT NULL,
  `vet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `topics`
--

CREATE TABLE `topics` (
  `_id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `topic_name` varchar(50) DEFAULT NULL,
  `topic_desc` varchar(100) DEFAULT NULL,
  `date_created` varchar(50) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `topics`
--

INSERT INTO `topics` (`_id`, `creator_id`, `topic_name`, `topic_desc`, `date_created`, `is_deleted`) VALUES
(0, 2, 'Adding topicsssss', 'Yay!!', '2018-03-20T08:39:48Z', 0),
(1, NULL, 'General', 'Default', '', 0),
(2, 1, 'For dog lovers only', 'Basically everything about dogs are welcome.', '', 0),
(3, 3, 'Another topic', 'For general use', '', 0),
(4, 1, 'Adding live?', 'For general use', '', 0),
(5, 1, 'Adding live? 2', 'For general use', '', 0);

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
(2, 1, 3, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `mobile_num` varchar(15) NOT NULL,
  `phone_num` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL,
  `age` int(11) NOT NULL,
  `user_type` int(11) NOT NULL,
  `user_photo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `mobile_num`, `phone_num`, `email`, `password`, `age`, `user_type`, `user_photo`) VALUES
(1, 'Edward', 'Tiro', '9152794135', '1234568', 'edward_tiro@dlsu.edu.ph', '123', 20, 1, 'uploads/users/009a551.jpg'),
(2, 'Kristian', 'Sisayan', '9567761376.', '9876543', 'kristian_sisayan@dlsu.edu.ph', '113', 20, 2, 'uploads/users/f6f7d7b.jpg'),
(3, 'John', 'Ivanhoe', '', '8704421', 'john_ivanhoe@gmail.com', '1234', 30, 1, NULL),
(4, 'John', 'Dion', '09152791111', '1234567', 'john_dion@gmail.com', '111', 0, 1, NULL),
(5, 'Kristian', 'Sisayan', '', '', 'sisayan.kristian@gmail.com', 'glmrklls', 0, 1, NULL),
(6, 'John', 'Woa', '', '', 'johnwoa@gmail.com', '123', 0, 2, NULL),
(19, 'Par', 'Koi', '', '', 'parkoi@gmail.com', '111', 0, 1, NULL),
(20, 'Edward', 'Tiro', '', '', 'dward@petbetter.com', 'admin', 21, 3, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `veterinarians`
--

CREATE TABLE `veterinarians` (
  `_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `specialty` int(11) NOT NULL,
  `rating` double NOT NULL,
  `education` text NOT NULL,
  `is_licensed` int(11) NOT NULL,
  `profile_desc` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `veterinarians`
--

INSERT INTO `veterinarians` (`_id`, `user_id`, `specialty`, `rating`, `education`, `is_licensed`, `profile_desc`) VALUES
(1, 3, 0, 3.5, '098654321', 0, ''),
(2, 1, 0, 3, '0987654321', 0, ''),
(59, 5, 0, 0, '0987654321', 0, ''),
(60, 19, 0, 0, '', 0, ''),
(61, 4, 0, 0, '', 0, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookmarks`
--
ALTER TABLE `bookmarks`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `facilities`
--
ALTER TABLE `facilities`
  ADD PRIMARY KEY (`faci_id`),
  ADD KEY `faci_id` (`faci_id`);

--
-- Indexes for table `facility_membership`
--
ALTER TABLE `facility_membership`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `topic_id` (`topic_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `markers`
--
ALTER TABLE `markers`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`,`faci_id`);

--
-- Indexes for table `messagereps`
--
ALTER TABLE `messagereps`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`,`sender_id`,`message_id`),
  ADD KEY `message_id` (`message_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_one` (`user_one`,`user_two`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`,`doer_id`,`source_id`);

--
-- Indexes for table `pending`
--
ALTER TABLE `pending`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`);

--
-- Indexes for table `pets`
--
ALTER TABLE `pets`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `postreps`
--
ALTER TABLE `postreps`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`,`post_id`),
  ADD KEY `post_id` (`post_id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`,`topic_id`,`id_link`),
  ADD KEY `topic_id` (`topic_id`),
  ADD KEY `faci_link` (`id_link`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `rater_id` (`rater_id`,`rated_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `faci_id` (`faci_id`);

--
-- Indexes for table `specialty`
--
ALTER TABLE `specialty`
  ADD PRIMARY KEY (`_id`);

--
-- Indexes for table `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `creator_id` (`creator_id`);

--
-- Indexes for table `upvotes`
--
ALTER TABLE `upvotes`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `feed_id` (`feed_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `veterinarians`
--
ALTER TABLE `veterinarians`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `_id` (`_id`),
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookmarks`
--
ALTER TABLE `bookmarks`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `facilities`
--
ALTER TABLE `facilities`
  MODIFY `faci_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;
--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=378;
--
-- AUTO_INCREMENT for table `markers`
--
ALTER TABLE `markers`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=140;
--
-- AUTO_INCREMENT for table `messagereps`
--
ALTER TABLE `messagereps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=92;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=263;
--
-- AUTO_INCREMENT for table `pending`
--
ALTER TABLE `pending`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `pets`
--
ALTER TABLE `pets`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `postreps`
--
ALTER TABLE `postreps`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;
--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `topics`
--
ALTER TABLE `topics`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `upvotes`
--
ALTER TABLE `upvotes`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `veterinarians`
--
ALTER TABLE `veterinarians`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `followers`
--
ALTER TABLE `followers`
  ADD CONSTRAINT `followers_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`_id`),
  ADD CONSTRAINT `followers_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `markers`
--
ALTER TABLE `markers`
  ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `messagereps`
--
ALTER TABLE `messagereps`
  ADD CONSTRAINT `messagereps_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `messagereps_ibfk_2` FOREIGN KEY (`message_id`) REFERENCES `messages` (`_id`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`user_one`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `pets`
--
ALTER TABLE `pets`
  ADD CONSTRAINT `pets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `postreps`
--
ALTER TABLE `postreps`
  ADD CONSTRAINT `postreps_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `postreps_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`_id`);

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`_id`),
  ADD CONSTRAINT `posts_ibfk_3` FOREIGN KEY (`id_link`) REFERENCES `facilities` (`faci_id`);

--
-- Constraints for table `ratings`
--
ALTER TABLE `ratings`
  ADD CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`rater_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `services`
--
ALTER TABLE `services`
  ADD CONSTRAINT `services_ibfk_1` FOREIGN KEY (`faci_id`) REFERENCES `facilities` (`faci_id`);

--
-- Constraints for table `topics`
--
ALTER TABLE `topics`
  ADD CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `upvotes`
--
ALTER TABLE `upvotes`
  ADD CONSTRAINT `upvotes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `veterinarians`
--
ALTER TABLE `veterinarians`
  ADD CONSTRAINT `veterinarians_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
