CREATE DATABASE  IF NOT EXISTS `project_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `project_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: project_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `checkouts`
--

DROP TABLE IF EXISTS `checkouts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkouts` (
  `checkout_id` int NOT NULL AUTO_INCREMENT,
  `reservation_id` int DEFAULT NULL,
  `checked_out_by` varchar(10) DEFAULT NULL,
  `checked_out_date` datetime NOT NULL,
  `check_in_date` datetime DEFAULT NULL,
  PRIMARY KEY (`checkout_id`),
  KEY `reservation_id` (`reservation_id`),
  KEY `checked_out_by` (`checked_out_by`),
  CONSTRAINT `checkouts_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`) ON DELETE CASCADE,
  CONSTRAINT `checkouts_ibfk_2` FOREIGN KEY (`checked_out_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkouts`
--

LOCK TABLES `checkouts` WRITE;
/*!40000 ALTER TABLE `checkouts` DISABLE KEYS */;
INSERT INTO `checkouts` VALUES (1,1,'A001','2025-02-26 11:31:50','2025-02-26 11:37:32'),(2,2,'A001','2025-02-26 11:36:39','2025-02-26 11:47:55'),(3,3,'A002','2025-02-26 11:50:56','2025-02-26 11:51:58'),(4,4,'A002','2025-02-26 11:51:10','2025-02-26 11:52:37');
/*!40000 ALTER TABLE `checkouts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` enum('Available','Reserved','CheckedOut') DEFAULT 'Available',
  `state` enum('New','Good','Fair','Poor') DEFAULT 'Good',
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES ('AUD001','Test H4n Recorder','Audio Recorder','4-track recording, portable device','Available','Fair'),('AUD003','Sony PCM-D100','Audio Recorder','High-resolution portable recorder','Available','Fair'),('AUD004','Marantz PMD661','Audio Recorder','Professional portable recorder','Available','Good'),('AUD005','Roland R-07','Audio Recorder','Compact, high-quality recorder','Available','Good'),('AUD006','Zoom H6','Audio Recorder','Handy recorder with modular mic system','Available','Good'),('AUD007','Tascam DR-10L','Audio Recorder','Lavalier mic with recorder','Available','Good'),('AUD008','Olympus LS-P4','Audio Recorder','Pocket-sized recorder with FLAC recording','Available','Good'),('AUD009','Sound Devices MixPre-3','Audio Recorder','High-end field recorder','Available','Good'),('AUD010','Fostex FR-2LE','Audio Recorder','Portable field recorder with timecode','Available','Good'),('CAM002','Nikon D7500','Camera','DSLR camera with 24MP resolution','Available','Good'),('CAM003','Sony Alpha A7 III','Camera','Mirrorless camera with 4K video capability','Available','Good'),('CAM004','Canon EOS R5','Camera','Mirrorless camera with 45MP resolution','Available','Good'),('CAM005','Fujifilm X-T4','Camera','Mirrorless camera for filmmakers','Available','Good'),('CAM006','Panasonic Lumix GH5','Camera','4K camera for videographers','Available','Good'),('CAM007','Olympus OM-D E-M1 Mark III','Camera','Weather-sealed mirrorless camera','Available','Good'),('CAM008','Sony Alpha A7C','Camera','Compact mirrorless camera','Available','Good'),('CAM009','Canon PowerShot G7 X Mark III','Camera','Compact vlogging camera','Available','Good'),('CAM010','Nikon Z50','Camera','Compact mirrorless camera','Available','Good'),('DRN001','DJI Mavic Air 2','Drone','Compact drone with 4K video capability','Available','Good'),('DRN002','Autel Robotics EVO Lite+','Drone','Portable drone with long battery life','Available','Good'),('DRN003','Parrot Anafi','Drone','Compact drone with 4K HDR video','Available','Good'),('DRN004','DJI Phantom 4 Pro','Drone','Professional-grade drone for filmmakers','Available','Good'),('DRN005','Skydio 2+','Drone','AI-powered drone with obstacle avoidance','Available','Good'),('LAP001','MacBook Pro 2021','Laptop','M1 Pro Chip, 16GB RAM, 512GB SSD','Available','Good'),('LAP002','Dell XPS 15','Laptop','Intel i7, 16GB RAM, 1TB SSD','Available','Good'),('LAP003','HP Envy 13','Laptop','Lightweight laptop, Intel i5, 8GB RAM, 512GB SSD','Available','Good'),('LAP004','Lenovo ThinkPad X1 Carbon','Laptop','Lightweight business laptop','Available','Good'),('LAP005','Microsoft Surface Laptop 4','Laptop','AMD Ryzen, 16GB RAM, 512GB SSD','Available','Good'),('LAP006','Asus ZenBook Duo','Laptop','Dual-screen laptop for multitasking','Available','Good'),('LAP007','Acer Swift 3','Laptop','Compact and affordable laptop','Available','Good'),('LAP008','Razer Blade 15','Laptop','Gaming laptop with RTX 3070','Available','Good'),('LAP009','LG Gram 17','Laptop','Ultralight laptop with 17-inch screen','Available','Good'),('LAP010','Apple MacBook Air M2','Laptop','M2 chip, compact laptop','Available','Good'),('LGT001','LED Panel Light','Lighting','Adjustable brightness and color temperature','Available','Good'),('LGT002','Ring Light','Lighting','Perfect for vlogging and photography','Available','Good'),('LGT003','Godox SL-60W','Lighting','Affordable continuous light','Available','Good'),('LGT004','Aputure Light Storm LS C300d','Lighting','Professional-grade LED light','Available','Good'),('LGT005','Nanlite Forza 60','Lighting','Compact LED light for filmmakers','Available','Good'),('LGT006','Elgato Key Light','Lighting','Light for streaming and video calls','Available','Good'),('LGT007','Neewer Softbox Kit','Lighting','Softbox kit for portrait photography','Available','Good'),('LGT008','FalconEyes RX-18TD','Lighting','Flexible LED light','Available','Good'),('LGT009','Yongnuo YN360','Lighting','RGB LED light stick','Available','Good'),('LGT010','Dracast LED500','Lighting','Portable and durable LED light','Available','Good'),('PRJ001','Epson Projector','Projector','Full HD resolution, HDMI support','Available','Good'),('PRJ002','BenQ W2700','Projector','4K HDR Home Theater Projector','Available','Good'),('PRJ003','Optoma UHD50X','Projector','Gaming projector with low latency','Available','Good'),('PRJ004','Sony VPL-VW295ES','Projector','4K HDR projector for home theater','Available','Good'),('PRJ005','LG CineBeam HU85LA','Projector','Ultra short throw 4K projector','Available','Good'),('PRJ006','ViewSonic PX747-4K','Projector','Affordable 4K UHD projector','Available','Good'),('PRJ007','Anker Nebula Capsule II','Projector','Portable projector with Android','Available','Good'),('PRJ008','Samsung The Premiere','Projector','4K triple laser projector','Available','Good'),('PRJ009','Acer H7850','Projector','4K UHD projector with HDR','Available','Good'),('PRJ010','JVC DLA-NX5','Projector','Premium 4K home theater projector','Available','Good'),('VRH001','Oculus Quest 2','VR Headset','All-in-one VR headset, 128GB storage','Available','Good'),('VRH002','HTC Vive Pro','VR Headset','High-resolution VR system with base stations','Available','Good'),('VRH003','Valve Index','VR Headset','Premium VR headset for gamers','Available','Good');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturers`
--

DROP TABLE IF EXISTS `lecturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturers` (
  `lecturer_id` varchar(10) NOT NULL,
  `department` varchar(100) NOT NULL,
  PRIMARY KEY (`lecturer_id`),
  CONSTRAINT `fk_lecturer_user` FOREIGN KEY (`lecturer_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturers`
--

LOCK TABLES `lecturers` WRITE;
/*!40000 ALTER TABLE `lecturers` DISABLE KEYS */;
INSERT INTO `lecturers` VALUES ('L001','Sport Management'),('L002','Sport Management'),('L003','Architecture'),('L004','Arts & Social Studies'),('L005','Built Environment'),('L006','Business'),('L007','Computing'),('L008','Engineering'),('L009','Hospitality, Tourism & Culinary Arts'),('L010','Early Years Education'),('L011','Media'),('L012','Design & Music'),('L013','Nursing'),('L014','Health & Psychology'),('L015','Science'),('L016','Law'),('L017','Sport Science');
/*!40000 ALTER TABLE `lecturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(10) DEFAULT NULL,
  `equipment_id` varchar(10) DEFAULT NULL,
  `reservation_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('Pending','Approved','Rejected') NOT NULL DEFAULT 'Pending',
  `approved_by` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `user_id` (`user_id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `reservations_ibfk_3` (`approved_by`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_3` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,'C00123456','AUD001','2025-02-27','2025-02-28','Approved','A001'),(2,'C00123456','AUD003','2025-02-27','2025-02-26','Approved','A001'),(3,'C00123456','AUD001','2025-02-26','2025-02-26','Approved','A001'),(4,'C00123456','AUD003','2025-02-26','2025-02-26','Approved','A002');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` varchar(10) NOT NULL,
  `course` varchar(100) NOT NULL,
  `department` varchar(100) NOT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`student_id`),
  CONSTRAINT `fk_student_user` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('C00123456','BSc in Computer Science','Computing',3),('C00123457','BA in Sociology','Arts & Social Studies',3),('C00123458','BEng in Civil Engineering','Engineering',1),('C00123459','BA in Early Childhood Education','Early Years Education',4),('C00123460','BSc in Biomedical Science','Science',2),('C00123499','Software Development','Computing',2),('C00188894','Biotechnology and Genetics','Science',1);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Student','Lecturer','Admin','MediaStaff') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('A001','Markus Mukiiza','markus.mukiiza@setu.ie','password123','Admin'),('A002','Hamed Zon','hamed.zon@setu.ie','password123','Admin'),('C00123456','Alice Johnson','C00123456@setu.ie','password123','Student'),('C00123457','Bob Smith','C00123457@setu.ie','password123','Student'),('C00123458','Charlie Green','C00123458@setu.ie','password123','Student'),('C00123459','Diana Brown','C00123459@setu.ie','password123','Student'),('C00123460','Eve White','C00123460@setu.ie','password123','Student'),('C00123499','Szymon Stepnyash','C00123499@setu.ie','password123','Student'),('C00188894','Erlong Johnson','C00188894@setu.ie','password123','Student'),('L001','Szymon Stepniak','szymon.stepnia@setu.ie','password123','Lecturer'),('L002','Jack Martinez','jack.martinez@setu.ie','password123','Lecturer'),('L003','Maria Johnson','maria.johnson@setu.ie','password123','Lecturer'),('L004','David Brown','david.brown@setu.ie','password123','Lecturer'),('L005','Dr. Sophia White','sophia.white@setu.ie','password123','Lecturer'),('L006','James Green','james.green@setu.ie','password123','Lecturer'),('L007','Dr. Olivia Adams','olivia.adams@setu.ie','password123','Lecturer'),('L008','Emma Watson','emma.watson@setu.ie','password123','Lecturer'),('L009','Liam Jones','liam.jones@setu.ie','password123','Lecturer'),('L010','Ava Taylor','ava.taylor@setu.ie','password123','Lecturer'),('L011','Noah Evans','noah.evans@setu.ie','password123','Lecturer'),('L012','Dr. Lucas Anderson','lucas.anderson@setu.ie','password123','Lecturer'),('L013','Dr. Ethan Clark','ethan.clark@setu.ie','password123','Lecturer'),('L014','Mia Wilson','mia.wilson@setu.ie','password123','Lecturer'),('L015','Dr. Oliver Johnson','oliver.johnson@setu.ie','password123','Lecturer'),('L016','Amelia Brown','amelia.brown@setu.ie','password123','Lecturer'),('L017','Dr. Harper Taylor','harper.taylor@setu.ie','password123','Lecturer'),('M001','Liam Clarke','liam.clarke@setu.ie','password123','MediaStaff'),('M002','Noah Kim','noah.kim@setu.ie','password123','MediaStaff'),('M003','Ava Martinez','ava.martinez@setu.ie','password123','MediaStaff'),('M004','John Connor','john.connor@setu.ie','password123','MediaStaff'),('M005','Mad Hatter','mad.hatter@setu.ie','password123','MediaStaff');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'project_db'
--

--
-- Dumping routines for database 'project_db'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddEquipment`(
    IN p_name VARCHAR(100),
    IN p_type VARCHAR(50),
    IN p_description VARCHAR(255),
    IN p_state ENUM('New', 'Good', 'Fair', 'Poor'),
    IN p_user_id VARCHAR(10)
)
BEGIN
    -- Declare variables
    DECLARE p_equipment_id VARCHAR(10);
    DECLARE p_prefix VARCHAR(10);
    DECLARE affected_rows INT DEFAULT 0;

    -- General error handler
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while adding equipment.';
    END;

    -- Validate user role
    IF (SELECT COUNT(*) FROM Users WHERE user_id = p_user_id AND role IN ('Admin', 'MediaStaff')) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Admins or MediaStaff can add equipment.';
    END IF;

    -- Validate equipment type
    IF p_type NOT IN ('Camera', 'Microphone', 'Audio Recorder', 'Lighting', 'Projector',
                      'Laptop', 'Tripod', 'Gimbal', 'Headphones', 'Tablet', 'VR Headset', 'Drone') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid equipment type.';
    END IF;

    -- Map type to prefix
    SET p_prefix = CASE p_type
        WHEN 'Camera' THEN 'CAM'
        WHEN 'Microphone' THEN 'MIC'
        WHEN 'Audio Recorder' THEN 'AUD'
        WHEN 'Lighting' THEN 'LGT'
        WHEN 'Projector' THEN 'PRJ'
        WHEN 'Laptop' THEN 'LAP'
        WHEN 'Tripod' THEN 'TRP'
        WHEN 'Gimbal' THEN 'GMB'
        WHEN 'Headphones' THEN 'HPN'
        WHEN 'Tablet' THEN 'TAB'
        WHEN 'VR Headset' THEN 'VRH'
        WHEN 'Drone' THEN 'DRN'
        ELSE 'OTH'
    END;

    -- Generate unique equipment ID
    SET p_equipment_id = CONCAT(p_prefix, LPAD(
        (SELECT COALESCE(MAX(CAST(SUBSTRING(equipment_id, LENGTH(p_prefix) + 1) AS UNSIGNED)), 0) + 1
         FROM Equipment
         WHERE LEFT(equipment_id, LENGTH(p_prefix)) = p_prefix), 
        3, '0'
    ));

    -- Insert into Equipment table
    INSERT INTO Equipment (equipment_id, name, type, description, status, state)
    VALUES (p_equipment_id, p_name, p_type, p_description, 'Available', p_state);

    -- Get affected rows count
    SET affected_rows = ROW_COUNT();

    -- Return affected rows and equipment ID
    SELECT affected_rows AS RowsAffected, p_equipment_id AS EquipmentID;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AddUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddUser`(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255),  -- Must be a hashed password.
    IN p_role ENUM('Student', 'Lecturer', 'Admin', 'MediaStaff'),
    IN p_name VARCHAR(100),
    IN p_course VARCHAR(100),
    IN p_department VARCHAR(100),
    IN p_year INT,
    IN p_creator_id VARCHAR(10)
)
BEGIN
    DECLARE p_user_id VARCHAR(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while adding user.';
    END;

    START TRANSACTION;

    IF (SELECT role FROM Users WHERE user_id = p_creator_id) != 'Admin' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Admins can add users.';
    END IF;

    IF p_name IS NULL OR p_name = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Name is required.';
    END IF;

    IF EXISTS (SELECT 1 FROM Users WHERE email = p_email) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email already exists.';
    END IF;

    CASE p_role
        WHEN 'Student' THEN
            SET p_user_id = SUBSTRING_INDEX(p_email, '@', 1);
        WHEN 'Lecturer' THEN
            SET p_user_id = CONCAT('L', LPAD((SELECT COUNT(*) + 1 FROM Users WHERE role = 'Lecturer'), 3, '0'));
        WHEN 'MediaStaff' THEN
            SET p_user_id = CONCAT('M', LPAD((SELECT COUNT(*) + 1 FROM Users WHERE role = 'MediaStaff'), 3, '0'));
        WHEN 'Admin' THEN
            SET p_user_id = CONCAT('A', LPAD((SELECT COUNT(*) + 1 FROM Users WHERE role = 'Admin'), 3, '0'));
        ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid role.';
    END CASE;

    INSERT INTO Users (user_id, name, email, password, role)
    VALUES (p_user_id, p_name, p_email, p_password, p_role);

    IF p_role = 'Student' THEN
        IF p_course IS NULL OR p_course = '' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Course is required for students.';
        END IF;
        IF p_year NOT BETWEEN 1 AND 5 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid academic year.';
        END IF;

        INSERT INTO Students (student_id, course, department, year)
        VALUES (p_user_id, p_course, p_department, p_year);
    ELSEIF p_role = 'Lecturer' THEN
        IF p_department IS NULL OR p_department = '' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Department is required for lecturers.';
        END IF;

        INSERT INTO Lecturers (lecturer_id, department)
        VALUES (p_user_id, p_department);
    END IF;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ApproveReservation` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ApproveReservation`(
    IN p_reservation_id INT,
    IN p_admin_id VARCHAR(10),
    IN p_status ENUM('Approved', 'Rejected')
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while approving reservation.';
    END;

    -- Validate admin role
    IF (SELECT role FROM users WHERE user_id = p_admin_id) != 'Admin' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Admins can approve reservations.';
    END IF;

    -- Ensure reservation exists and is pending
    IF (SELECT COUNT(*) FROM reservations WHERE reservation_id = p_reservation_id AND status = 'Pending') = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid reservation or already processed.';
    END IF;

    -- Approve or Reject the reservation
    UPDATE reservations
    SET status = p_status, approved_by = p_admin_id
    WHERE reservation_id = p_reservation_id;

    -- If approved, update equipment status to "Reserved"
    IF p_status = 'Approved' THEN
        UPDATE Equipment
        SET status = 'Reserved'
        WHERE equipment_id = (SELECT equipment_id FROM reservations WHERE reservation_id = p_reservation_id);
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CheckOutEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckOutEquipment`(
    IN p_reservation_id INT,
    IN p_user_id VARCHAR(10),
    OUT p_rows_affected INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while checking out equipment.';
    END;

    -- Validate role of user performing checkout
    IF (SELECT role FROM Users WHERE user_id = p_user_id) NOT IN ('MediaStaff', 'Admin') THEN
        SET p_rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only MediaStaff or Admins can check out equipment.';
    END IF;

    -- Ensure equipment is reserved
    IF (SELECT status FROM Equipment WHERE equipment_id = (SELECT equipment_id FROM Reservations WHERE reservation_id = p_reservation_id)) != 'Reserved' THEN
        SET p_rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Equipment is not reserved.';
    END IF;

    -- Perform checkout
    INSERT INTO Checkouts (reservation_id, checked_out_by, checked_out_date)
    VALUES (p_reservation_id, p_user_id, NOW());

    -- Update equipment status
    UPDATE Equipment
    SET status = 'CheckedOut'
    WHERE equipment_id = (SELECT equipment_id FROM Reservations WHERE reservation_id = p_reservation_id);

    -- Set the affected row count manually
    SET p_rows_affected = ROW_COUNT();

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteEquipment`(
    IN p_equipment_id VARCHAR(10),
    IN p_requester_id VARCHAR(10)
)
BEGIN
    -- Declare variables
    DECLARE requester_role VARCHAR(20);
    DECLARE equipment_exists INT;
    DECLARE equipment_status VARCHAR(20);
    DECLARE affected_rows INT DEFAULT 0;

    -- Capture requester role
    SELECT role INTO requester_role FROM Users WHERE user_id = p_requester_id;
    
    -- Capture equipment existence
    SELECT COUNT(*) INTO equipment_exists FROM Equipment WHERE equipment_id = p_equipment_id;
    
    -- Capture equipment status
    SELECT status INTO equipment_status FROM Equipment WHERE equipment_id = p_equipment_id;

    -- Validate requester role
    IF requester_role NOT IN ('Admin', 'MediaStaff') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Admins or MediaStaff can delete equipment.';
    END IF;

    -- Ensure equipment exists
    IF equipment_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Equipment not found.';
    END IF;

    -- Check equipment status (Prevent deletion if Reserved or CheckedOut)
    IF equipment_status IN ('Reserved', 'CheckedOut') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot delete equipment that is Reserved or Checked Out.';
    END IF;

    -- Perform deletion
    DELETE FROM Equipment WHERE equipment_id = p_equipment_id;
    
    -- Capture affected rows
    SET affected_rows = ROW_COUNT();

    -- ✅ Return affected rows as the ONLY SELECT statement
    SELECT affected_rows AS RowsAffected;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteUser`(
    IN p_user_id VARCHAR(10),
    IN p_requester_id VARCHAR(10)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error deleting user.';
    END;

    START TRANSACTION;

    -- Validate admin permissions
    IF (SELECT role FROM Users WHERE user_id = p_requester_id) != 'Admin' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Requires admin privileges.';
    END IF;

    -- Validate user exists
    IF NOT EXISTS (SELECT 1 FROM Users WHERE user_id = p_user_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not found.';
    END IF;

    DELETE FROM Users WHERE user_id = p_user_id;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ReserveEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ReserveEquipment`(
    IN p_user_id VARCHAR(10),
    IN p_equipment_id VARCHAR(10),
    IN p_reservation_date DATE
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while reserving equipment.';
    END;

    -- Validate user role
    IF (SELECT role FROM users WHERE user_id = p_user_id) NOT IN ('Student', 'Lecturer') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Students and Lecturers can reserve equipment.';
    END IF;

    -- Validate equipment state (Ensure it's not already reserved)
    IF (SELECT COUNT(*) FROM reservations WHERE equipment_id = p_equipment_id AND status = 'Approved' AND reservation_date = p_reservation_date) > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Equipment is already reserved for this date.';
    END IF;

    -- Insert reservation with status = 'Pending' and NULL return_date
    INSERT INTO reservations (user_id, equipment_id, reservation_date, return_date, status)
    VALUES (p_user_id, p_equipment_id, p_reservation_date, NULL, 'Pending');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ReturnCheckedOutEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ReturnCheckedOutEquipment`(
    IN p_reservation_id INT,
    IN p_user_id VARCHAR(10),
    IN p_equipment_state ENUM('Good', 'Fair', 'Poor'),
    OUT p_rows_affected INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_rows_affected = 0;
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while returning equipment.';
    END;

    -- Start transaction
    START TRANSACTION;

    -- Validate role
    IF (SELECT role FROM Users WHERE user_id = p_user_id) NOT IN ('MediaStaff', 'Admin') THEN
        SET p_rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only MediaStaff or Admins can return checked-out equipment.';
    END IF;

    -- Validate equipment state
    IF (SELECT status FROM Equipment WHERE equipment_id = (SELECT equipment_id FROM Reservations WHERE reservation_id = p_reservation_id)) != 'CheckedOut' THEN
        SET p_rows_affected = 0;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Equipment is not currently checked out.';
    END IF;

    -- Update Checkouts table
    UPDATE Checkouts
    SET check_in_date = NOW()
    WHERE reservation_id = p_reservation_id AND check_in_date IS NULL;

    -- Update Reservations table
    UPDATE Reservations
    SET return_date = NOW()
    WHERE reservation_id = p_reservation_id;

    -- Update Equipment table with new state and set as available
    UPDATE Equipment
    SET status = 'Available', state = p_equipment_state
    WHERE equipment_id = (SELECT equipment_id FROM Reservations WHERE reservation_id = p_reservation_id);

    -- Set affected row count
    SET p_rows_affected = ROW_COUNT();

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateEquipment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateEquipment`(
    IN p_equipment_id VARCHAR(10),
    IN p_name VARCHAR(100),
    IN p_type VARCHAR(50),
    IN p_description VARCHAR(255),
    IN p_state ENUM('New', 'Good', 'Fair', 'Poor'),
    IN p_status ENUM('Available', 'Reserved', 'CheckedOut'),
    IN p_requester_id VARCHAR(10)
)
BEGIN
    DECLARE affected_rows INT DEFAULT 0;

    -- General error handler
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error occurred while updating equipment.';
    END;

    -- Validate role of requester
    IF (SELECT COUNT(*) FROM Users WHERE user_id = p_requester_id AND role IN ('Admin', 'MediaStaff')) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Only Admins or MediaStaff can update equipment.';
    END IF;

    -- Ensure equipment exists
    IF (SELECT COUNT(*) FROM Equipment WHERE equipment_id = p_equipment_id) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Equipment not found.';
    END IF;

    -- Update equipment
    UPDATE Equipment
    SET name = p_name,
        type = p_type,
        description = p_description,
        state = p_state,
        status = p_status
    WHERE equipment_id = p_equipment_id;

    -- Get affected rows count
    SET affected_rows = ROW_COUNT();

    -- Return affected rows
    SELECT affected_rows AS RowsAffected;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateUser`(
    IN p_user_id VARCHAR(10),
    IN p_new_email VARCHAR(100),
    IN p_new_name VARCHAR(100),
    IN p_new_password VARCHAR(255),  -- Optional hashed password. If provided, update password.
    IN p_new_course VARCHAR(100),
    IN p_new_department VARCHAR(100),
    IN p_new_year INT,
    IN p_admin_id VARCHAR(10)
)
BEGIN
    DECLARE admin_role VARCHAR(20);
    DECLARE user_role VARCHAR(20);
    
    SELECT role INTO admin_role FROM users WHERE user_id = p_admin_id;
    IF admin_role != 'Admin' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permission denied: Requires Admin privileges';
    END IF;

    SELECT role INTO user_role FROM users WHERE user_id = p_user_id;

    -- Update users table; update password only if a new hashed password is provided
    IF p_new_password IS NOT NULL AND p_new_password <> '' THEN
        UPDATE users SET
            email = COALESCE(p_new_email, email),
            name = COALESCE(p_new_name, name),
            password = p_new_password
        WHERE user_id = p_user_id;
    ELSE
        UPDATE users SET
            email = COALESCE(p_new_email, email),
            name = COALESCE(p_new_name, name)
        WHERE user_id = p_user_id;
    END IF;

    IF user_role = 'Student' THEN
        UPDATE students SET
            course = COALESCE(p_new_course, course),
            department = COALESCE(p_new_department, department),
            year = COALESCE(p_new_year, year)
        WHERE student_id = p_user_id;
    ELSEIF user_role = 'Lecturer' THEN
        UPDATE lecturers SET
            department = COALESCE(p_new_department, department)
        WHERE lecturer_id = p_user_id;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-26 19:35:23
