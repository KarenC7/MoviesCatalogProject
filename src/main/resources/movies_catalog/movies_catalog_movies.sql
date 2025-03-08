CREATE DATABASE  IF NOT EXISTS `movies_catalog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `movies_catalog`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: movies_catalog
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `poster_url` varchar(255) DEFAULT NULL,
  `release_year` int DEFAULT NULL,
  `synopsis` varchar(2000) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb28y4p1aqcewgyxk4bxsa75nd` (`created_by`),
  CONSTRAINT `FKb28y4p1aqcewgyxk4bxsa75nd` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (2,'2025-03-07 08:07:37.720381','Movie to Rate','http://example.com/poster_rate.jpg',2022,'A movie to be rated.',12),(3,'2025-03-07 08:12:46.095412','The Godfather','https://m.media-amazon.com/images/M/MV5BNGEwYjgwOGQtYjg5ZS00Njc1LTk2ZGEtM2QwZWQ2NjdhZTE5XkEyXkFqcGc@._V1_.jpg',1972,'Sample test for a synopsis',10),(5,'2025-03-07 08:32:53.092323','Movie to Rate','http://example.com/poster_rate.jpg',2022,'A movie to be rated.',12),(6,'2025-03-08 01:27:27.482737','Interestellar','https://m.media-amazon.com/images/M/MV5BYzdjMDAxZGItMjI2My00ODA1LTlkNzItOWFjMDU5ZDJlYWY3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg',2014,'Sample test for a synopsis',10),(7,'2025-03-08 01:28:45.639947','Gladiator','https://es.web.img3.acsta.net/medias/nmedia/18/70/92/02/20149073.jpg',2000,'Sample test for a synopsis',10),(8,'2025-03-08 01:29:51.038207','Shrek','https://pics.filmaffinity.com/Shrek-148818104-large.jpg',2001,'Sample test for a synopsis',10),(9,'2025-03-08 01:30:45.453833','Shrek 2','https://m.media-amazon.com/images/M/MV5BYTE2ODE0ZTAtYzAyNC00OWY2LWIyMWYtZDljZWYyNWYzNmY2XkEyXkFqcGc@._V1_.jpg',2004,'Updated synopsis',10),(10,'2025-03-08 01:32:14.450176','Shrek 3','https://m.media-amazon.com/images/M/MV5BYTE2ODE0ZTAtYzAyNC00OWY2LWIyMWYtZDljZWYyNWYzNmY2XkEyXkFqcGc@._V1_.jpg',2008,'Sample test for a synopsis',10),(11,'2025-03-08 01:35:22.249480','Pride and Prejudice','https://m.media-amazon.com/images/M/MV5BMTA1NDQ3NTcyOTNeQTJeQWpwZ15BbWU3MDA0MzA4MzE@._V1_.jpg',2005,'Sample test for a synopsis',10);
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-07 19:48:52
