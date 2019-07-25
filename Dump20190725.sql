CREATE DATABASE  IF NOT EXISTS `agility` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `agility`;
-- MySQL dump 10.13  Distrib 5.6.19, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: agility
-- ------------------------------------------------------
-- Server version	5.6.33-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acc_plan`
--

DROP TABLE IF EXISTS `acc_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acc_plan` (
  `id` int(3) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `obs` varchar(255) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned zerofill NOT NULL,
  `modified_by` int(4) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by_idx` (`created_by`,`modified_by`),
  KEY `fk_acc_plan_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_acc_plan_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_acc_plan_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_plan`
--

LOCK TABLES `acc_plan` WRITE;
/*!40000 ALTER TABLE `acc_plan` DISABLE KEYS */;
INSERT INTO `acc_plan` VALUES (001,'Aluguel','obs','Pagamentos',1,'2019-01-21 12:19:06','2019-01-21 12:21:48',0009,0009),(003,'Abastecimento de Água (Cagece)','','Pagamentos',1,'2019-01-21 12:19:24','2019-01-21 12:22:29',0009,0009),(004,'Mensalidade','','Recebimentos',1,'2019-01-21 12:19:33','2019-01-21 12:23:09',0009,0009),(005,'Fardamento','rec','Recebimentos',1,'2019-01-21 12:19:41','2019-01-21 12:23:20',0009,0009),(006,'Outros','rec','Recebimentos',1,'2019-01-21 12:19:48','2019-01-21 12:24:55',0009,0009),(008,'Internet + Tel.Fixo (Oi)','','Pagamentos',1,'2019-01-21 12:22:50','0000-00-00 00:00:00',0009,NULL),(009,'Outros','--','Pagamentos',1,'2019-01-21 13:30:37','2019-02-07 16:13:34',0009,0009);
/*!40000 ALTER TABLE `acc_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adresses`
--

DROP TABLE IF EXISTS `adresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adresses` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cep` varchar(45) DEFAULT NULL,
  `logra` varchar(50) NOT NULL,
  `num` varchar(10) NOT NULL,
  `compl` varchar(45) DEFAULT NULL,
  `bairro` varchar(45) NOT NULL,
  `muni` varchar(45) NOT NULL,
  `refer` varchar(80) NOT NULL,
  `uf` varchar(45) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned zerofill NOT NULL,
  `modified_by` int(4) unsigned zerofill DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `created_by_idx` (`created_by`,`modified_by`),
  KEY `modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_adresses_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_adresses_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adresses`
--

LOCK TABLES `adresses` WRITE;
/*!40000 ALTER TABLE `adresses` DISABLE KEYS */;
INSERT INTO `adresses` VALUES (1,'65.600-435','Rua Cinco ','975','','São Francisco ','Caxias','','MA','2019-01-17 14:31:25',NULL,0009,NULL,1),(2,'60.541-600','Vila Francisco Domingos','673','','Granja Portugal','Fortaleza','','CE','2019-01-17 14:34:18',NULL,0009,NULL,1),(3,'60.541-600','Vila Francisco Domingos','673','','Granja Portugal','Fortaleza','','CE','2019-01-17 14:34:53','2019-01-17 14:38:54',0009,0009,1),(4,'77.820-068','Rua 50 ','287','--','Jardim dos Ipês I ','Araguaína','--','TO','2019-01-17 14:36:59','2019-02-22 01:11:02',0009,0009,1),(5,'77.824-665','Rua São Francisco','377','','Vila Goiás','Araguaína','','TO','2019-01-21 12:08:03','2019-02-22 01:11:29',0009,0009,1),(6,'60.541-660','Rua Pomar Carioca','123','','Bonsucesso','Fortaleza','','CE','2019-02-01 01:32:15',NULL,0009,NULL,1),(7,'60.541-660','Rua Pomar Carioca','874','','Bonsucesso','Fortaleza','','CE','2019-02-16 11:37:35','2019-02-19 15:19:23',0009,0009,1);
/*!40000 ALTER TABLE `adresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boletins`
--

DROP TABLE IF EXISTS `boletins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boletins` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `student_cod` varchar(20) NOT NULL,
  `course_id` int(11) unsigned NOT NULL,
  `class_id` int(11) unsigned NOT NULL,
  `discipline_id` int(11) unsigned NOT NULL,
  `bim1_n1` double(4,2) DEFAULT NULL,
  `bim1_n2` double(4,2) DEFAULT NULL,
  `bim2_n1` double(4,2) DEFAULT NULL,
  `bim2_n2` double(4,2) DEFAULT NULL,
  `bim3_n1` double(4,2) DEFAULT NULL,
  `bim3_n2` double(4,2) DEFAULT NULL,
  `bim4_n1` double(4,2) DEFAULT NULL,
  `bim4_n2` double(4,2) DEFAULT NULL,
  `rec_bim1` double(4,2) DEFAULT NULL,
  `rec_bim2` double(4,2) DEFAULT NULL,
  `rec_bim3` double(4,2) DEFAULT NULL,
  `rec_bim4` double(4,2) DEFAULT NULL,
  `misses_bim1` int(3) DEFAULT NULL,
  `misses_bim2` int(3) DEFAULT NULL,
  `misses_bim3` int(3) DEFAULT NULL,
  `misses_bim4` int(3) DEFAULT NULL,
  `misses_rec` int(3) DEFAULT NULL,
  `academic_year` int(4) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(11) unsigned NOT NULL,
  `modified_by` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_boletins_student_cod_idx` (`student_cod`),
  KEY `fk_boletins_course_id_idx` (`course_id`),
  KEY `fk_boletins_class_id_idx` (`class_id`),
  KEY `fk_boletins_discipline_id_idx` (`discipline_id`),
  KEY `fk_boletins_created_by_idx` (`created_by`),
  KEY `fk_boletins_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_boletins_class_id` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_boletins_course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_boletins_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_boletins_discipline_id` FOREIGN KEY (`discipline_id`) REFERENCES `disciplines` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_boletins_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_boletins_student_cod` FOREIGN KEY (`student_cod`) REFERENCES `students` (`cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boletins`
--

LOCK TABLES `boletins` WRITE;
/*!40000 ALTER TABLE `boletins` DISABLE KEYS */;
INSERT INTO `boletins` VALUES (1,'2019001',6,11,13,4.00,3.00,4.00,2.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,2019,'2019-01-31 23:52:43','2019-02-01 00:54:09',9,9),(2,'2019002',6,11,13,6.00,7.00,2.00,6.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,2019,'2019-01-31 23:52:43','2019-01-31 23:55:18',9,9),(3,'2019001',6,11,14,4.00,5.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,2019,'2019-02-16 11:58:56',NULL,9,NULL),(4,'2019002',6,11,14,8.00,6.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,2019,'2019-02-16 11:58:56',NULL,9,NULL),(5,'2019005',6,11,14,9.00,10.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,2019,'2019-02-16 11:58:56',NULL,9,NULL);
/*!40000 ALTER TABLE `boletins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `id` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL,
  `author` varchar(150) NOT NULL,
  `publisher` varchar(150) NOT NULL,
  `isbn` varchar(30) DEFAULT NULL,
  `course_id` int(11) unsigned NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(11) unsigned NOT NULL,
  `modified_by` int(11) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_books_course_id_idx` (`course_id`),
  KEY `fk_books_created_by_idx` (`created_by`),
  KEY `fk_books_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_books_course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_books_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_books_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (0010,'Matemática – 1º Ano (Livro e Caderno de Atividades)','Ênio Silveira e Cláudio Marques','Moderna, 4ª Edição (reformulada)','123',6,'2019-01-17 11:05:37',NULL,9,NULL,1),(0011,'Sistema Marista de Educação – Ensino Religioso','?','FTD','?',6,'2019-01-17 11:06:43',NULL,9,NULL,1),(0012,'“ORBIT” 1','?','Richmond, 1º Edição','?',6,'2019-01-17 11:07:18',NULL,9,NULL,1),(0013,'OS AMIGOS DO MARCELO','Ruth Rocha ','Salamandra','?',6,'2019-01-17 11:07:53',NULL,9,NULL,1),(0014,'BEM-TE-VERDE','Santuza Abras','Formato','?',6,'2019-01-17 11:08:13',NULL,9,NULL,1),(0015,'A DESCOBERTA DA JOANINHA','Bellah Leite Cordeiro','Paulinas','?',6,'2019-01-17 11:08:37',NULL,9,NULL,1),(0016,'Ligados.com','Angélica Prado e Cristina Hulle.','Saraiva (Última edição) ','?',7,'2019-01-17 11:09:24','2019-01-17 13:34:00',9,9,1),(0017,'Matemática – 2º Ano (Livro e Caderno de Atividades)','Ênio Silveira e Cláudio Marques','Moderna (Última Edição)','?',7,'2019-01-17 11:09:55',NULL,9,NULL,1),(0018,'Aprender juntos','Cristiane Motta.','SM (Última Edição).','--',7,'2019-01-17 11:10:31','2019-01-17 11:14:25',9,9,1),(0020,'Sistema Marista de Educação – Ensino Religioso','?','FTD','?',7,'2019-01-17 11:11:34',NULL,9,NULL,1),(0021,'Projeto Buriti História (Livro) ','Edições Educativas','Editora Moderna','123',7,'2019-01-17 11:12:04',NULL,9,NULL,1),(0022,'Projeto Buriti Geografia (Livro)','Edições Educativas','Editora Moderna ','123',7,'2019-01-17 11:12:28',NULL,9,NULL,1),(0023,'Livro do estudante de Educação Tecnológica','--','--','--',7,'2019-01-17 11:12:49',NULL,9,NULL,1),(0024,'“ORBIT” 2, 1º Edição','--','Richmond','--',7,'2019-01-17 11:15:56',NULL,9,NULL,1);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cod` varchar(10) NOT NULL,
  `name` varchar(40) NOT NULL,
  `turno` varchar(10) NOT NULL,
  `capacity` int(4) unsigned NOT NULL,
  `course_id` int(11) unsigned NOT NULL,
  `classroom_id` int(11) unsigned NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod` (`cod`),
  KEY `fk_classes_course_id_idx` (`course_id`),
  KEY `fk_classes_classroom_id_idx` (`classroom_id`),
  KEY `fk_classes_created_by_idx` (`created_by`),
  KEY `fk_classes_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_classes_classroom_id` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (11,'EF1A','Turma A','Manhã',30,6,7,'2019-01-17 13:38:57','2019-02-19 15:18:58',9,9,1),(12,'EF1B','Turma B','Tarde',30,6,7,'2019-01-17 13:39:11',NULL,9,NULL,1),(13,'EF2A','Turma A','Manhã',30,7,8,'2019-01-17 13:39:32',NULL,9,NULL,1),(14,'EF2B','Turma B','Tarde',30,7,8,'2019-01-17 13:39:43',NULL,9,NULL,1),(15,'a','a','Manhã',1,6,9,'2019-01-17 13:40:34',NULL,9,NULL,1);
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classrooms`
--

DROP TABLE IF EXISTS `classrooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classrooms` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `cod` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `sede` varchar(30) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(11) unsigned NOT NULL,
  `modified_by` int(11) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod` (`cod`),
  KEY `fk_classrooms_created_by_idx` (`created_by`),
  KEY `fk_classrooms_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_classrooms_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_classrooms_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classrooms`
--

LOCK TABLES `classrooms` WRITE;
/*!40000 ALTER TABLE `classrooms` DISABLE KEYS */;
INSERT INTO `classrooms` VALUES (7,'S01','Sala 01',NULL,'2019-01-17 10:56:54',NULL,9,NULL,1),(8,'S02','Sala 02',NULL,'2019-01-17 10:57:00','2019-02-07 15:44:40',9,9,1),(9,'S03','Sala 03',NULL,'2019-01-17 10:57:06',NULL,9,NULL,1),(10,'S04','Sala 04',NULL,'2019-01-17 10:57:13',NULL,9,NULL,1);
/*!40000 ALTER TABLE `classrooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientes` (
  `cod` int(3) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `tel` varchar(15) NOT NULL,
  `cli_id` int(4) unsigned zerofill NOT NULL,
  `mac` varchar(45) DEFAULT NULL,
  `prazo` date NOT NULL,
  `status` int(1) unsigned DEFAULT NULL,
  `modified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cod`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (3,'Escola Teste','000.000.000','(85) 8888-8888',0001,'00-0C-6E-3C-D1-6D','2019-06-30',1,'2019-02-15 17:05:36');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cod` varchar(20) NOT NULL,
  `name` varchar(80) NOT NULL,
  `priceBase` decimal(6,2) NOT NULL,
  `freq` decimal(6,2) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod` (`cod`),
  KEY `fk_courses_created_by_idx` (`created_by`),
  KEY `fk_courses_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_courses_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_courses_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (6,'EF1','1º Ano do Ensino Fundamental',100.00,95.00,'2019-01-17 10:59:36',NULL,9,NULL,1),(7,'EF2','2º Ano do Ensino Fundamental',900.00,95.00,'2019-01-17 10:59:43',NULL,9,NULL,1),(8,'EF3','3º Ano do Ensino Fundamental',40.00,95.00,'2019-01-17 10:59:51',NULL,9,NULL,1),(9,'EF4','4º Ano do Ensino Fundamental',456.00,95.00,'2019-01-17 10:59:58',NULL,9,NULL,1),(10,'EF5','5º Ano do Ensino Fundamental',149.99,95.00,'2019-01-17 11:00:05','2019-01-17 11:02:05',9,9,1),(11,'EF6','6º Ano do Ensino Fundamental',300.00,95.00,'2019-01-17 11:00:14',NULL,9,NULL,1),(12,'EF7','7º Ano do Ensino Fundamental',350.00,95.00,'2019-01-17 11:00:23',NULL,9,NULL,1),(13,'EF8','8º Ano do Ensino Fundamental',399.00,95.00,'2019-01-17 11:00:33',NULL,9,NULL,1),(14,'EM1','1º Ano do Ensino Médio',450.00,95.00,'2019-01-17 11:00:45',NULL,9,NULL,1),(15,'EM2','2º Ano do Ensino Médio',500.00,95.00,'2019-01-17 11:00:55',NULL,9,NULL,1),(16,'EM3','3º Ano do Ensino Médio',450.00,95.00,'2019-01-17 11:01:06',NULL,9,NULL,1),(17,'teste','teste',123.00,123.00,'2019-01-17 11:03:02','2019-02-07 16:00:02',9,9,0),(18,'123','12',123.00,123.00,'2019-02-07 16:03:58','2019-02-07 16:04:02',9,9,0);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disciplines`
--

DROP TABLE IF EXISTS `disciplines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `disciplines` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cod` varchar(20) NOT NULL,
  `name` varchar(40) NOT NULL,
  `workLoad` int(10) NOT NULL,
  `course_id` int(11) unsigned DEFAULT NULL,
  `book_id` int(4) unsigned NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod` (`cod`),
  KEY `fk_disciplines_course_id_idx` (`course_id`),
  KEY `fk_disciplines_book_id_idx` (`book_id`),
  KEY `fk_disciplines_created_by_idx` (`created_by`),
  KEY `fk_disciplines_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_disciplines_book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_disciplines_course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_disciplines_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_disciplines_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disciplines`
--

LOCK TABLES `disciplines` WRITE;
/*!40000 ALTER TABLE `disciplines` DISABLE KEYS */;
INSERT INTO `disciplines` VALUES (13,'EF1MAT','Matemática 1',500,6,10,'2019-01-17 11:17:46','2019-01-17 11:24:48',9,9,1),(14,'EF1REL','Religião',200,6,11,'2019-01-17 11:19:04','2019-01-17 11:24:39',9,9,1),(15,'EF1ING','Inglês 1',123,6,12,'2019-01-17 11:24:31',NULL,9,NULL,1),(16,'EF1LIT','Literatura',123,6,13,'2019-01-17 11:26:41',NULL,9,NULL,1),(17,'EF2MAT','Matemática 2',123,7,17,'2019-01-17 11:27:00','2019-01-17 13:34:44',9,9,1),(18,'EF2POR','Português 2',123,7,16,'2019-01-17 11:27:17',NULL,9,NULL,1),(19,'EF2LIT','Literatura 2',123,7,18,'2019-01-17 11:27:33','2019-01-17 13:37:27',9,9,1),(20,'EF2HIS','História 2',231,7,21,'2019-01-17 13:36:03',NULL,9,NULL,1),(21,'EF2GEO','Geografia 2',123,7,22,'2019-01-17 13:36:22',NULL,9,NULL,1),(22,'EF2ING','Inglês 2',123,7,24,'2019-01-17 13:36:39',NULL,9,NULL,1),(23,'opa','opa',123,6,10,'2019-01-17 13:37:42',NULL,9,NULL,1);
/*!40000 ALTER TABLE `disciplines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employees` (
  `id` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `dataNasc` varchar(15) NOT NULL,
  `sexo` varchar(15) NOT NULL,
  `cpf` varchar(20) NOT NULL,
  `rg` varchar(20) NOT NULL,
  `dataRg` varchar(20) NOT NULL,
  `orgaoRg` varchar(50) NOT NULL,
  `tel1` varchar(20) NOT NULL,
  `tel2` varchar(20) DEFAULT NULL,
  `estCivil` varchar(20) NOT NULL,
  `nis` varchar(20) DEFAULT NULL,
  `ctps` varchar(20) NOT NULL,
  `ctps_serie` varchar(20) NOT NULL,
  `ctps_uf` varchar(20) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `first_job` varchar(45) NOT NULL,
  `form_contrato` varchar(50) NOT NULL,
  `h_entry` varchar(10) DEFAULT NULL COMMENT '(desativ)',
  `h_exit` varchar(10) DEFAULT NULL COMMENT '(desativ)',
  `h_interval` varchar(20) DEFAULT NULL COMMENT '(desativ)',
  `h_week` varchar(20) DEFAULT NULL COMMENT '(desativ)',
  `clearance_week` varchar(10) DEFAULT NULL COMMENT '(desativ)',
  `date_admission` varchar(25) NOT NULL,
  `date_resignation` varchar(25) DEFAULT NULL,
  `pass` varchar(100) DEFAULT NULL,
  `reset_key` varchar(100) DEFAULT NULL,
  `adress_id` int(11) unsigned NOT NULL,
  `occupation_id` int(10) unsigned NOT NULL,
  `created` varchar(45) NOT NULL,
  `modified` varchar(45) DEFAULT NULL,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` varchar(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_employees_adress_id_idx` (`adress_id`),
  KEY `fk_employees_occupation_id_idx` (`occupation_id`),
  KEY `fk_employees_created_by_idx` (`created_by`),
  KEY `fk_employees_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_employees_adress_id` FOREIGN KEY (`adress_id`) REFERENCES `adresses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employees_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employees_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employees_occupation_id` FOREIGN KEY (`occupation_id`) REFERENCES `occupations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (0009,'Jeosafá Ferreira','11/11/1111','Masculino','064.410.693-00','21231231','11/11/1111','123123123','(85) 12345 - 6789','','Solteiro(a)','','123123','123123','CE','123','NÃO','CLT - Carteira Assinada',NULL,NULL,NULL,NULL,NULL,'17/01/2019','17/01/2019','123',NULL,0,7,'','17/01/2019 - 10:53:40',0,9,'0'),(0010,'Elias Nicolas Luiz Pinto','05/04/1997','Masculino','533.733.429-88','487849486','11/11/1111','asdasd','(98) 3825 - 4248','(98) 98272 - 0236','Solteiro(a)','123123','1111111','123123','AM','eeliasnicolasluizpinto@2014fwcao.com','SIM','CLT - Carteira Assinada',NULL,NULL,NULL,NULL,NULL,'17/01/2019',NULL,NULL,NULL,27,7,'17/01/2019 - 10:49:20',NULL,9,NULL,'1'),(0011,'Administrador','19/12/1995','Masculino','064.410.693-00','123456789','11/11/1111','sspce','(85) 98887 - 9921','','Solteiro(a)','654987','123456789','1234596','CE','a.a.j.d@hotmail.com','NÃO','CLT - Carteira Assinada',NULL,NULL,NULL,NULL,NULL,'17/01/2019',NULL,NULL,NULL,28,7,'17/01/2019 - 10:51:01',NULL,9,NULL,'1'),(0012,'Diretor','11/11/1111','Masculino','111.111.111-11','1111111111111','11/11/1111','1111','(12) 3165 - 4987','(85) 1234 - 5678','Casado(a)','111111111111','11111','11111','DF','8765465','NÃO','CLT - Carteira Assinada',NULL,NULL,NULL,NULL,NULL,'17/01/2019',NULL,NULL,NULL,29,7,'17/01/2019 - 10:51:46',NULL,9,NULL,'1'),(0013,'teste','11/11/1111','Masculino','111.111.111-11','11111111','11/11/1111','12123','','','Solteiro(a)','','1111111','11111111','AC','','NÃO','CLT - Carteira Assinada',NULL,NULL,NULL,NULL,NULL,'01/02/2019','',NULL,NULL,6,7,'01/02/2019 - 01:32:15','07/02/2019 - 15:39:07',9,9,'1');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees_education`
--

DROP TABLE IF EXISTS `employees_education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employees_education` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course` varchar(200) DEFAULT NULL,
  `instituition` varchar(100) DEFAULT NULL,
  `concYear` varchar(20) DEFAULT NULL,
  `employee_id` int(4) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employees_education_employee_id_idx` (`employee_id`),
  CONSTRAINT `fk_employees_education_employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees_education`
--

LOCK TABLES `employees_education` WRITE;
/*!40000 ALTER TABLE `employees_education` DISABLE KEYS */;
INSERT INTO `employees_education` VALUES (1,'Curso1','Instituição1','2017',10),(2,'Curso2','Instituição2','2018',10),(3,'Curso3','Instituição3','2019',10),(4,'Curso4','Instituição4','2020',10),(5,'Curso5','Instituição5','2021',10),(6,'TI','IFCE','2020',11),(7,'1231231','2312312','123123',12),(9,'abc','inst','2030',9),(10,'123','123','123',13);
/*!40000 ALTER TABLE `employees_education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `errors`
--

DROP TABLE IF EXISTS `errors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `errors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cod` varchar(200) DEFAULT NULL,
  `error` longtext,
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `errors`
--

LOCK TABLES `errors` WRITE;
/*!40000 ALTER TABLE `errors` DISABLE KEYS */;
INSERT INTO `errors` VALUES (118,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:52:17'),(119,'#1055','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:52:21'),(120,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:53:18'),(121,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:53:20'),(122,'#1055','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:53:25'),(123,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-01-17 13:53:39'),(124,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 16:45:11'),(125,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 16:45:21'),(126,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 16:45:25'),(127,'#1161','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-01-17 16:57:24'),(128,'#0007','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-01-17 16:57:47'),(129,'#1161','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-01-17 16:58:38'),(130,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 17:15:59'),(131,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 17:26:24'),(132,'#0008','java.sql.SQLException: No value specified for parameter 11','2019-01-17 17:26:31'),(133,'#1044','java.lang.NullPointerException','2019-01-21 15:08:45'),(134,'#1044','java.lang.NullPointerException','2019-01-21 15:09:13'),(135,'#1044','java.lang.NullPointerException','2019-01-21 15:09:24'),(136,'#1044','java.lang.NullPointerException','2019-01-21 15:09:29'),(137,'#1044','java.lang.NullPointerException','2019-01-21 15:10:12'),(138,'#1044','java.lang.NullPointerException','2019-01-21 15:10:16'),(139,'#1044','java.lang.NullPointerException','2019-01-21 15:10:19'),(140,'#1044','java.lang.NullPointerException','2019-01-21 15:10:32'),(141,'#1044','java.lang.NullPointerException','2019-01-21 15:10:39'),(142,'#1044','java.lang.NullPointerException','2019-01-21 15:10:47'),(143,'#1044','java.lang.NullPointerException','2019-01-21 15:10:56'),(144,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-01 04:31:34'),(145,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-01 04:31:40'),(146,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-01 04:31:43'),(147,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-01 04:31:46'),(148,'#1133','net.sf.jasperreports.engine.JRException: Unknown column name : user_id','2019-02-01 19:30:41'),(149,'#1133','net.sf.jasperreports.engine.JRException: Unknown column name : user_id','2019-02-01 19:31:16'),(150,'#1132','net.sf.jasperreports.engine.JRException: Error loading object from file : /home/jferreira/agility/Financeiro/FinConPagar.jasper','2019-02-05 05:47:40'),(151,'#1097','net.sf.jasperreports.engine.JRException: Error loading object from file : /home/jferreira/agility/Alunos/AluDados.jasper','2019-02-05 05:47:58'),(152,'#1097','net.sf.jasperreports.engine.JRException: Error loading object from file : /home/jferreira/agility/Alunos/AluDados.jasper','2019-02-05 05:48:03'),(153,'#1151','java.lang.IllegalArgumentException: Cannot format given Object as a Date','2019-02-05 07:15:02'),(154,'#1151','java.lang.IllegalArgumentException: Cannot format given Object as a Date','2019-02-05 07:16:36'),(155,'#1151','java.lang.IllegalArgumentException: Cannot format given Object as a Date','2019-02-05 07:17:37'),(156,'#1004','java.lang.IllegalArgumentException: Cannot format given Object as a Date','2019-02-05 07:17:38'),(157,'#1004','java.lang.IllegalArgumentException: Cannot format given Object as a Date','2019-02-05 07:18:40'),(158,'#1133','net.sf.jasperreports.engine.JRException: Unknown column name : user_id','2019-02-05 07:21:05'),(159,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-07 18:38:50'),(160,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-07 18:38:53'),(161,'#0010','java.sql.SQLException: Illegal operation on empty result set.','2019-02-16 14:55:42'),(162,'#0010','java.sql.SQLException: Illegal operation on empty result set.','2019-02-16 14:58:07'),(163,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-16 15:06:40'),(164,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-16 15:06:48'),(165,'#1061','java.sql.SQLException: Illegal operation on empty result set.','2019-02-16 15:06:54'),(166,'#1161','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-02-18 19:05:21'),(167,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-21 19:28:13'),(168,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-21 19:28:29'),(169,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 03:26:04'),(170,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:35:29'),(171,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:35:32'),(172,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:35:34'),(173,'#1149','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /home/jferreira/JaspersoftWorkspace/Agility/Ocorrencias/OcoByAlu.jasper','2019-02-22 03:35:37'),(174,'#1147','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /home/jferreira/JaspersoftWorkspace/Agility/Responsaveis/RespDados.jasper','2019-02-22 03:35:40'),(175,'#1148','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /home/jferreira/JaspersoftWorkspace/Agility/Responsaveis/RespDados.jasper','2019-02-22 03:35:42'),(176,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:35:54'),(177,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:36:02'),(178,'#1147','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /home/jferreira/JaspersoftWorkspace/Agility/Responsaveis/RespDados.jasper','2019-02-22 03:36:24'),(179,'#1146','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:36:43'),(180,'#1097','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:37:16'),(181,'#1097','net.sf.jasperreports.engine.JRException: Unknown column name : type','2019-02-22 03:37:22'),(182,'#1097','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /media/jferreira/Arquivos/NetBeans Projects/JaspersoftWorkspace/Agility/Alunos/AluDados.jasper','2019-02-22 03:42:08'),(183,'#1097','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /media/jferreira/Arquivos/NetBeans Projects/JaspersoftWorkspace/Agility/Alunos/AluDados.jasper','2019-02-22 03:43:55'),(184,'#1097','net.sf.jasperreports.engine.JRException: java.io.FileNotFoundException: /media/jferreira/Arquivos/NetBeans Projects/JaspersoftWorkspace/Agility/Alunos/AluDados.jasper','2019-02-22 03:44:47'),(185,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 03:49:24'),(186,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 03:55:53'),(187,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 03:56:00'),(188,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:06:18'),(189,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:07:00'),(190,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:07:17'),(191,'#1130','net.sf.jasperreports.engine.JRException: Error executing SQL statement for : Ficha do Aluno','2019-02-22 04:15:14'),(192,'#0010','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:15:24'),(193,'#0011','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:23:21'),(194,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:23:22'),(195,'#0011','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:23:33'),(196,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:23:34'),(197,'#0011','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:27:33'),(198,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:27:34'),(199,'#0011','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:28:45'),(200,'#1150','java.sql.SQLException: Illegal operation on empty result set.','2019-02-22 04:28:46'),(201,'#1068','null','2019-02-22 05:07:56'),(202,'#1068','Desculpe, ocorreu um erro ao gerar as mensalidades deste aluno(a).\n\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1068','2019-02-22 05:14:14'),(203,'#1161','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-03-08 04:20:23'),(204,'#0007','java.net.NoRouteToHostException: Não há rota para o host (Host unreachable)','2019-03-08 04:20:32'),(205,'#1130','net.sf.jasperreports.engine.JRException: Error executing SQL statement for : Ficha do Aluno','2019-03-22 05:29:21'),(206,'#1130','net.sf.jasperreports.engine.JRException: Error executing SQL statement for : Ficha do Aluno','2019-03-22 05:29:28'),(207,'#0010','java.sql.SQLException: Illegal operation on empty result set.','2019-03-22 05:36:46');
/*!40000 ALTER TABLE `errors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_types`
--

DROP TABLE IF EXISTS `event_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_types` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `occupation_id` varchar(11) NOT NULL COMMENT 'Quais níveis de usuário podem cadastrar esse event.',
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(20) NOT NULL,
  `modified_by` varchar(20) DEFAULT NULL,
  `active` int(2) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_types`
--

LOCK TABLES `event_types` WRITE;
/*!40000 ALTER TABLE `event_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `events` (
  `id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `date_time` varchar(30) NOT NULL,
  `class_id` int(11) unsigned NOT NULL,
  `course_id` int(11) unsigned NOT NULL,
  `event_type_id` int(6) unsigned NOT NULL,
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(20) NOT NULL,
  `modified_by` varchar(20) DEFAULT NULL,
  `active` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `discipline_id` varchar(150) NOT NULL,
  `recipients_title` varchar(255) NOT NULL,
  `created` varchar(25) NOT NULL,
  `modified` varchar(25) DEFAULT NULL,
  `teacher_id` int(11) NOT NULL,
  `modified_by` int(15) DEFAULT NULL,
  `academic_year` varchar(10) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finances`
--

DROP TABLE IF EXISTS `finances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finances` (
  `id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `titulo` varchar(45) DEFAULT NULL,
  `parcela` int(4) unsigned DEFAULT NULL,
  `tot_parcela` int(4) NOT NULL,
  `pri_parcela` int(6) unsigned zerofill NOT NULL,
  `valor` double(20,2) unsigned DEFAULT NULL,
  `valor_pago` double(20,2) DEFAULT NULL,
  `venc` date DEFAULT NULL,
  `data_pag` date DEFAULT NULL,
  `multa` double(10,3) DEFAULT NULL,
  `juros` double(10,3) DEFAULT NULL,
  `desc` double(10,3) DEFAULT NULL,
  `obs` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `form_of_pay_id` int(11) unsigned DEFAULT NULL,
  `acc_plan_id` int(3) unsigned DEFAULT NULL,
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `data_baixa` date DEFAULT NULL,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_finances_form_of_pay_id_idx` (`form_of_pay_id`),
  KEY `fk_finances_acc_plan_id_idx` (`acc_plan_id`),
  KEY `fk_finances_created_by_idx` (`created_by`),
  KEY `fk_finances_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_finances_acc_plan_id` FOREIGN KEY (`acc_plan_id`) REFERENCES `acc_plan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_finances_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_finances_form_of_pay_id` FOREIGN KEY (`form_of_pay_id`) REFERENCES `form_of_pay` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finances`
--

LOCK TABLES `finances` WRITE;
/*!40000 ALTER TABLE `finances` DISABLE KEYS */;
INSERT INTO `finances` VALUES (000013,'Notebook Positivo',1,1,000013,599.00,599.00,'2019-01-19','2019-02-05',0.000,0.000,0.000,'',NULL,3,9,'2019-01-21 13:31:39','2019-02-05 04:25:34',NULL,9,9),(000014,'Aluguel 2019',1,12,000014,500.00,NULL,'2019-01-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:48','2019-02-05 02:46:23',NULL,9,9),(000015,'Aluguel 2019',2,12,000014,500.00,NULL,'2019-02-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:48','2019-02-05 02:46:24',NULL,9,9),(000016,'Aluguel 2019',3,12,000014,500.00,NULL,'2019-03-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:48','2019-02-05 02:46:24',NULL,9,9),(000017,'Aluguel 2019',4,12,000014,500.00,500.00,'2019-04-05','2019-02-01',0.000,0.000,0.000,'',NULL,1,1,'2019-02-01 16:20:49','2019-02-05 02:46:24',NULL,9,9),(000018,'Aluguel 2019',5,12,000014,500.00,NULL,'2019-05-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:24',NULL,9,9),(000019,'Aluguel 2019',6,12,000014,500.00,NULL,'2019-06-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:24',NULL,9,9),(000020,'Aluguel 2019',7,12,000014,500.00,NULL,'2019-07-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:24',NULL,9,9),(000021,'Aluguel 2019',8,12,000014,500.00,NULL,'2019-08-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:24',NULL,9,9),(000022,'Aluguel 2019',9,12,000014,500.00,NULL,'2019-09-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:25',NULL,9,9),(000023,'Aluguel 2019',10,12,000014,500.00,NULL,'2019-10-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:25',NULL,9,9),(000024,'Aluguel 2019',11,12,000014,500.00,NULL,'2019-11-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:25',NULL,9,9),(000025,'Aluguel 2019',12,12,000014,500.00,NULL,'2019-12-05',NULL,NULL,NULL,NULL,'',NULL,NULL,1,'2019-02-01 16:20:49','2019-02-05 02:46:25',NULL,9,9);
/*!40000 ALTER TABLE `finances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_of_pay`
--

DROP TABLE IF EXISTS `form_of_pay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_of_pay` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_of_pay`
--

LOCK TABLES `form_of_pay` WRITE;
/*!40000 ALTER TABLE `form_of_pay` DISABLE KEYS */;
INSERT INTO `form_of_pay` VALUES (1,'Cartão HiperCard','P','2019-01-21 12:28:49',NULL,1),(2,'Cartão MasterCard','P','2019-01-21 12:29:03',NULL,1),(3,'Dinheiro','P','2019-01-21 12:29:13',NULL,1),(4,'Dinheiro','R','2019-01-21 12:29:23',NULL,1),(5,'Cartão','R','2019-01-21 12:29:30',NULL,1);
/*!40000 ALTER TABLE `form_of_pay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) NOT NULL,
  `message` mediumtext NOT NULL,
  `sender` varchar(150) NOT NULL,
  `recipients` mediumtext NOT NULL,
  `recipientsTitle` varchar(255) NOT NULL,
  `created` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `occupations`
--

DROP TABLE IF EXISTS `occupations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `occupations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `salary_base` varchar(8) NOT NULL,
  `created` varchar(25) NOT NULL,
  `modified` varchar(25) DEFAULT NULL,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` varchar(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_occupations_created_by_idx` (`created_by`),
  KEY `fk_occupations_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_occupations_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_occupations_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupations`
--

LOCK TABLES `occupations` WRITE;
/*!40000 ALTER TABLE `occupations` DISABLE KEYS */;
INSERT INTO `occupations` VALUES (7,'Professor Efetivo Nv.1','1100,95','17/01/2019 - 10:42:17','17/01/2019 - 10:45:12',9,9,'1'),(8,'Professor Efetivo Nv.2','1200,95','17/01/2019 - 10:42:31',NULL,9,NULL,'1'),(9,'Professor Efetivo Nv.3','1300,95','17/01/2019 - 10:42:50',NULL,9,NULL,'1'),(10,'Professor Efetivo Nv.4','1400.97','17/01/2019 - 10:45:42',NULL,9,NULL,'0');
/*!40000 ALTER TABLE `occupations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `occurrences`
--

DROP TABLE IF EXISTS `occurrences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `occurrences` (
  `id` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `occurrence` varchar(100) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `sentence` varchar(250) NOT NULL,
  `student_cod` varchar(20) NOT NULL,
  `date` varchar(12) NOT NULL,
  `time` varchar(8) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_occurrences_student_cod_idx` (`student_cod`),
  KEY `fk_occurrences_created_by_idx` (`created_by`),
  KEY `fk_occurrences_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_occurrences_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_occurrences_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_occurrences_student_cod` FOREIGN KEY (`student_cod`) REFERENCES `students` (`cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occurrences`
--

LOCK TABLES `occurrences` WRITE;
/*!40000 ALTER TABLE `occurrences` DISABLE KEYS */;
INSERT INTO `occurrences` VALUES (0001,'teste2','teste2','teste2','2019001','21/01/2019','13:45h','2019-01-21 12:17:16','2019-01-21 12:18:39',9,9,1),(0002,'tes','tste','asdasdasd','2019005','19/02/2019','16:52h','2019-02-19 16:52:59',NULL,9,NULL,1);
/*!40000 ALTER TABLE `occurrences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `responsibles`
--

DROP TABLE IF EXISTS `responsibles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `responsibles` (
  `id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(70) NOT NULL,
  `dataNasc` date NOT NULL,
  `sexo` varchar(15) NOT NULL,
  `cpf` varchar(20) DEFAULT NULL,
  `rg` varchar(20) NOT NULL,
  `dataRg` date NOT NULL,
  `orgaoRg` varchar(40) NOT NULL,
  `tel1` varchar(20) NOT NULL,
  `tel2` varchar(20) DEFAULT NULL,
  `estCivil` varchar(15) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_responsibles_created_by_idx` (`created_by`),
  KEY `fk_responsibles_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_responsibles_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_responsibles_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `responsibles`
--

LOCK TABLES `responsibles` WRITE;
/*!40000 ALTER TABLE `responsibles` DISABLE KEYS */;
INSERT INTO `responsibles` VALUES (00001,'Murilo Bernardo Matheus dos Santos','1997-10-21','Masculino','732.003.326-70','473764337','1997-10-21','SSPAS','6938585628','69987194067','Casado(a)','2019-01-17 14:31:25',NULL,1,9,NULL),(00002,'Josefa Fabiana Alessandra Barros','1997-09-09','Feminino','615.162.128-00','229799036','1997-09-09','SSPAS','2728620660','27988242017','Casado(a)','2019-01-17 14:31:25','2019-01-21 12:10:44',1,9,9),(00003,'Leonardo Yago Melo','1997-05-27','Masculino','816.615.981-37','268739821','1997-05-27','SSPCE','8138973180','81985211503','Casado(a)','2019-01-17 14:34:19',NULL,1,9,NULL),(00004,'Laura Mariana Lopes','1997-07-12','Feminino','710.080.228-88','158202296','1997-07-12','SSPCE','9239484696','92989418724','Casado(a)','2019-01-17 14:34:19',NULL,1,9,NULL),(00005,'Leonardo Yago Melo','1997-05-27','Masculino','816.615.981-37','268739821','1997-05-27','SSPCE','8138973180','81985211503','Casado(a)','2019-01-17 14:34:53',NULL,1,9,NULL),(00006,'Laura Mariana Lopes','1997-07-12','Feminino','710.080.228-88','158202296','1997-07-12','SSPCE','9239484696','92989418724','Casado(a)','2019-01-17 14:34:53',NULL,1,9,NULL),(00007,'Maria Maitê Nina','1997-06-25','Feminino','770.169.877-95','144638113','1997-06-25','SSPSP','8526586505','','Solteiro(a)','2019-01-17 14:36:59',NULL,1,9,NULL),(00008,'Tomás Rafael Filipe Vieira','1997-01-13','Masculino','811.853.047-70','10.922.866-2','1997-01-13','SSPCE','(61) 3519-8940','(61) 98746-3770','Casado(a)','2019-01-21 12:08:03',NULL,1,9,NULL),(00009,'teste','1111-11-11','Masculino','111.111.111-11','111111','1111-11-11','1231','111111111','1111','Casado(a)','2019-01-21 12:09:56','2019-01-21 12:11:04',0,9,NULL),(00010,'Pai','1994-10-19','Masculino','064.410.593-00','123123','1111-11-11','SSP','34848998','','Casado(a)','2019-02-16 11:37:35','2019-02-22 00:35:03',1,9,NULL),(00011,'Mãe','1998-11-10','Feminino','000.000.000-00','111111','2002-12-19','SSPCE','34848998','','Casado(a)','2019-02-16 11:37:35',NULL,1,9,NULL);
/*!40000 ALTER TABLE `responsibles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedules`
--

DROP TABLE IF EXISTS `schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weekday` int(2) NOT NULL COMMENT 'Domingo = 1, Seg=2...',
  `time` int(2) NOT NULL,
  `class_id` int(11) unsigned DEFAULT NULL,
  `disc_id` int(11) unsigned DEFAULT NULL,
  `employee_id` int(4) unsigned DEFAULT NULL,
  `created` varchar(50) NOT NULL,
  `modified` varchar(50) DEFAULT NULL,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_schedules_employee_id_idx` (`employee_id`),
  KEY `fk_schedules_class_id_idx` (`class_id`),
  KEY `fk_schedules_disc_id_idx` (`disc_id`),
  KEY `fk_schedules_created_by_idx` (`created_by`),
  KEY `fk_schedules_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_schedules_class_id` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_disc_id` FOREIGN KEY (`disc_id`) REFERENCES `disciplines` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedules`
--

LOCK TABLES `schedules` WRITE;
/*!40000 ALTER TABLE `schedules` DISABLE KEYS */;
INSERT INTO `schedules` VALUES (24,2,1,13,17,11,'01/02/2019 - 01:28:25',NULL,9,NULL),(25,2,2,13,17,11,'01/02/2019 - 01:28:25',NULL,9,NULL),(26,2,3,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(27,3,1,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(28,3,2,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(29,3,3,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(30,4,1,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(31,4,2,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(32,4,3,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(33,5,1,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(34,5,2,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(35,5,3,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(36,6,1,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(37,6,2,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(38,6,3,13,NULL,NULL,'01/02/2019 - 01:28:25',NULL,9,NULL),(54,2,1,11,15,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(55,2,2,11,15,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(56,2,3,11,14,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(57,3,1,11,13,11,'16/02/2019 - 11:54:16',NULL,9,NULL),(58,3,2,11,13,11,'16/02/2019 - 11:54:16',NULL,9,NULL),(59,3,3,11,NULL,NULL,'16/02/2019 - 11:54:16',NULL,9,NULL),(60,4,1,11,14,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(61,4,2,11,14,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(62,4,3,11,NULL,NULL,'16/02/2019 - 11:54:16',NULL,9,NULL),(63,5,1,11,14,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(64,5,2,11,14,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(65,5,3,11,NULL,NULL,'16/02/2019 - 11:54:16',NULL,9,NULL),(66,6,1,11,15,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(67,6,2,11,15,10,'16/02/2019 - 11:54:16',NULL,9,NULL),(68,6,3,11,NULL,NULL,'16/02/2019 - 11:54:16',NULL,9,NULL);
/*!40000 ALTER TABLE `schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school_data`
--

DROP TABLE IF EXISTS `school_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school_data` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `academic_year` int(5) NOT NULL,
  `school_cod` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school_data`
--

LOCK TABLES `school_data` WRITE;
/*!40000 ALTER TABLE `school_data` DISABLE KEYS */;
INSERT INTO `school_data` VALUES (1,'Escola de Testes',2019,'0001');
/*!40000 ALTER TABLE `school_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cod` varchar(20) DEFAULT NULL,
  `name` varchar(70) NOT NULL,
  `datanasc` date NOT NULL,
  `cpf` varchar(20) DEFAULT NULL,
  `rg` varchar(20) NOT NULL,
  `orgaoRg` varchar(40) NOT NULL,
  `dataRg` date NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `estCivil` varchar(15) NOT NULL,
  `sexo` varchar(15) NOT NULL,
  `tel` varchar(20) NOT NULL,
  `bloodType` varchar(4) DEFAULT NULL,
  `medicines` varchar(255) DEFAULT NULL,
  `allergies` varchar(100) DEFAULT NULL,
  `medicalObs` varchar(45) DEFAULT NULL,
  `extraInfo` text,
  `exitDate` varchar(25) DEFAULT NULL,
  `adress_id` int(11) unsigned NOT NULL,
  `respFin` int(5) unsigned NOT NULL,
  `respAcad` int(5) unsigned DEFAULT NULL,
  `respEmerg` int(5) unsigned DEFAULT NULL,
  `course_id` int(11) unsigned DEFAULT NULL,
  `class_id` int(11) unsigned DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1',
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cod` (`cod`) USING BTREE,
  KEY `fk_students_adress_id_idx` (`adress_id`),
  KEY `fk_students_respFin_idx` (`respFin`),
  KEY `fk_students_respAcad_idx` (`respAcad`),
  KEY `fk_students_respEmerg_idx` (`respEmerg`),
  KEY `fk_students_course_id_idx` (`course_id`),
  KEY `fk_students_class_id_idx` (`class_id`),
  KEY `fk_students_created_by_idx` (`created_by`),
  KEY `fk_students_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_students_adress_id` FOREIGN KEY (`adress_id`) REFERENCES `adresses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_class_id` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_course_id` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_respAcad` FOREIGN KEY (`respAcad`) REFERENCES `responsibles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_respEmerg` FOREIGN KEY (`respEmerg`) REFERENCES `responsibles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_students_respFin` FOREIGN KEY (`respFin`) REFERENCES `responsibles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'2019001','Miguel Murilo Severino da Paz','1997-04-11','   .   .   -  ','162942771','SSPAS','1997-04-11','miguelmuriloseverinodapaz-88@vmetaiscba.com.br','Solteiro(a)','Masculino','9938634699',NULL,'','','','',NULL,1,1,2,1,6,11,'2019-01-17 14:31:25','2019-01-21 12:13:49',1,9,NULL),(2,'2019002','Larissa Renata Marli Martins','1997-05-15','180.113.779-06','474419836','SSPCE','1997-05-15','larissarenatamarlimartins..larissarenatamarlimartins@locare-eventos.com.br','Solteiro(a)','Feminino','4325950268','A+','--','--','--','--',NULL,3,5,6,5,6,11,'2019-01-17 14:34:53','2019-02-01 00:50:16',1,9,NULL),(3,'2019003','Calebe Giovanni Teixeira','1997-07-09','375.661.331-36','217334325','SSPSP','1997-07-09','calebegiovanniteixeira_@tjsp.jus.br','Solteiro(a)','Masculino','6338986932','A+','--','--','--','--',NULL,4,7,7,7,7,13,'2019-01-17 14:36:59','2019-02-22 01:41:05',1,9,NULL),(4,'2019004','Renato Carlos Eduardo Carlos Peixoto','1997-03-05','549.398.392-30','29.200.358-4','SSPCE','1997-03-05','','Solteiro(a)','Masculino','(63) 99545-5236','A-','','','','',NULL,5,8,8,8,6,11,'2019-01-21 12:08:03','2019-02-22 01:40:38',1,9,NULL),(5,'2019005','Aluno de Teste','1994-10-30','   .   .   -  ','12312313123','SSPCE','1994-10-30','','Solteiro(a)','Masculino','98887-9921','O-','N','N','--','--','21/02/2019',7,10,11,10,6,11,'2019-02-16 11:37:35','2019-02-22 02:14:12',1,9,NULL);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_discipline`
--

DROP TABLE IF EXISTS `teacher_discipline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher_discipline` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(4) unsigned NOT NULL,
  `discipline_id` int(11) unsigned NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` datetime DEFAULT NULL,
  `created_by` int(4) unsigned NOT NULL,
  `modified_by` int(4) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_teacher_discipline_teacher_id_idx` (`employee_id`),
  KEY `fk_teacher_discipline_discipline_id_idx` (`discipline_id`),
  KEY `fk_teacher_discipline_created_by_idx` (`created_by`),
  KEY `fk_teacher_discipline_modified_by_idx` (`modified_by`),
  CONSTRAINT `fk_teacher_discipline_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_teacher_discipline_discipline_id` FOREIGN KEY (`discipline_id`) REFERENCES `disciplines` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_teacher_discipline_employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_teacher_discipline_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_discipline`
--

LOCK TABLES `teacher_discipline` WRITE;
/*!40000 ALTER TABLE `teacher_discipline` DISABLE KEYS */;
INSERT INTO `teacher_discipline` VALUES (1,10,15,'2019-02-01 01:02:45',NULL,9,NULL),(2,10,22,'2019-02-01 01:02:45',NULL,9,NULL),(3,10,14,'2019-02-01 01:02:45',NULL,9,NULL),(6,12,21,'2019-02-01 01:04:28',NULL,9,NULL),(7,12,20,'2019-02-01 01:04:28',NULL,9,NULL),(8,11,13,'2019-02-01 01:05:10',NULL,9,NULL),(9,11,17,'2019-02-01 01:05:10',NULL,9,NULL),(10,11,14,'2019-02-01 01:05:10',NULL,9,NULL);
/*!40000 ALTER TABLE `teacher_discipline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tuitions`
--

DROP TABLE IF EXISTS `tuitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tuitions` (
  `id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `student_cod` varchar(20) NOT NULL,
  `parcela` int(4) unsigned DEFAULT NULL,
  `tot_parcela` int(4) NOT NULL,
  `pri_parcela` int(6) unsigned zerofill NOT NULL,
  `valor` double(20,2) unsigned DEFAULT NULL,
  `valor_pago` double(20,2) unsigned DEFAULT NULL,
  `venc` date DEFAULT NULL,
  `refer` varchar(20) NOT NULL,
  `data_baixa` date DEFAULT NULL,
  `multa` double(10,3) DEFAULT NULL,
  `juros` double(10,3) DEFAULT NULL,
  `desco` double(10,3) DEFAULT NULL,
  `obs` varchar(255) DEFAULT NULL,
  `academic_year` int(4) NOT NULL,
  `form_of_pay_id` int(11) unsigned DEFAULT NULL,
  `acc_plan_id` int(3) unsigned zerofill DEFAULT NULL,
  `created` varchar(25) NOT NULL,
  `modified` varchar(25) DEFAULT NULL,
  `created_by` int(11) unsigned NOT NULL,
  `modified_by` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tuitions_student_cod_idx` (`student_cod`),
  KEY `fk_tuitions_form_of_pay_id_idx` (`form_of_pay_id`),
  KEY `fk_tuitions_acc_plan_id_idx` (`acc_plan_id`),
  KEY `fk_tuitions_modified_by_idx` (`modified_by`),
  KEY `fk_tuitions_created_by_idx` (`created_by`),
  CONSTRAINT `fk_tuitions_acc_plan_id` FOREIGN KEY (`acc_plan_id`) REFERENCES `acc_plan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tuitions_created_by` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tuitions_form_of_pay_id` FOREIGN KEY (`form_of_pay_id`) REFERENCES `form_of_pay` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tuitions_modified_by` FOREIGN KEY (`modified_by`) REFERENCES `employees` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tuitions_student_cod` FOREIGN KEY (`student_cod`) REFERENCES `students` (`cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tuitions`
--

LOCK TABLES `tuitions` WRITE;
/*!40000 ALTER TABLE `tuitions` DISABLE KEYS */;
INSERT INTO `tuitions` VALUES (000001,'2019001',1,12,000001,100.00,103.04,'2019-01-01','JANEIRO','2019-02-01',2.000,1.040,0.000,NULL,2019,4,NULL,'21/01/2019 - 12:13:49',NULL,9,9),(000002,'2019001',2,12,000001,100.00,0.00,'2019-02-01','FEVEREIRO','2019-02-01',2.000,0.000,0.000,NULL,2019,4,NULL,'21/01/2019 - 12:13:49',NULL,9,9),(000003,'2019001',3,12,000001,100.00,NULL,'2019-03-01','MARÇO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:49',NULL,9,NULL),(000004,'2019001',4,12,000001,100.00,NULL,'2019-04-01','ABRIL',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:49',NULL,9,NULL),(000005,'2019001',5,12,000001,100.00,NULL,'2019-05-01','MAIO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:49',NULL,9,NULL),(000006,'2019001',6,12,000001,100.00,NULL,'2019-06-01','JUNHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:49',NULL,9,NULL),(000007,'2019001',7,12,000001,100.00,NULL,'2019-07-01','JULHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000008,'2019001',8,12,000001,100.00,NULL,'2019-08-01','AGOSTO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000009,'2019001',9,12,000001,100.00,NULL,'2019-09-01','SETEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000010,'2019001',10,12,000001,100.00,NULL,'2019-10-01','OUTUBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000011,'2019001',11,12,000001,100.00,NULL,'2019-11-01','NOVEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000012,'2019001',12,12,000001,100.00,NULL,'2019-12-01','DEZEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:50',NULL,9,NULL),(000013,'2019002',1,12,000013,100.00,103.04,'2019-01-01','JANEIRO','2019-02-01',2.000,1.040,0.000,NULL,2019,4,NULL,'21/01/2019 - 12:13:58',NULL,9,9),(000014,'2019002',2,12,000013,100.00,NULL,'2019-02-05','FEVEREIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58',NULL,9,NULL),(000015,'2019002',3,12,000013,100.00,NULL,'2019-03-01','MARÇO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000016,'2019002',4,12,000013,100.00,NULL,'2019-04-01','ABRIL',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000017,'2019002',5,12,000013,100.00,NULL,'2019-05-01','MAIO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000018,'2019002',6,12,000013,100.00,NULL,'2019-06-01','JUNHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000019,'2019002',7,12,000013,100.00,NULL,'2019-07-01','JULHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000020,'2019002',8,12,000013,100.00,NULL,'2019-08-01','AGOSTO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000021,'2019002',9,12,000013,100.00,NULL,'2019-09-01','SETEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000022,'2019002',10,12,000013,100.00,NULL,'2019-10-01','OUTUBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000023,'2019002',11,12,000013,100.00,NULL,'2019-11-01','NOVEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000024,'2019002',12,12,000013,100.00,NULL,'2019-12-01','DEZEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:13:58','01/02/2019 - 00:50:16',9,9),(000025,'2019003',1,12,000025,100.00,NULL,'2019-01-01','JANEIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:13',NULL,9,NULL),(000026,'2019003',2,12,000025,100.00,NULL,'2019-02-01','FEVEREIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:13',NULL,9,NULL),(000027,'2019003',3,12,000025,900.00,NULL,'2019-03-01','MARÇO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:05',9,9),(000028,'2019003',4,12,000025,900.00,NULL,'2019-04-01','ABRIL',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:05',9,9),(000029,'2019003',5,12,000025,900.00,NULL,'2019-05-01','MAIO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000030,'2019003',6,12,000025,900.00,NULL,'2019-06-01','JUNHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000031,'2019003',7,12,000025,900.00,NULL,'2019-07-01','JULHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000032,'2019003',8,12,000025,900.00,NULL,'2019-08-01','AGOSTO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000033,'2019003',9,12,000025,900.00,NULL,'2019-09-01','SETEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000034,'2019003',10,12,000025,900.00,NULL,'2019-10-01','OUTUBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000035,'2019003',11,12,000025,900.00,NULL,'2019-11-01','NOVEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000036,'2019003',12,12,000025,900.00,NULL,'2019-12-01','DEZEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:14','22/02/2019 - 01:41:06',9,9),(000037,'2019004',1,12,000037,100.00,NULL,'2019-01-01','JANEIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:22',NULL,9,NULL),(000038,'2019004',2,12,000037,100.00,NULL,'2019-02-01','FEVEREIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:22',NULL,9,NULL),(000039,'2019004',3,12,000037,100.00,NULL,'2019-03-01','MARÇO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:22','22/02/2019 - 01:40:38',9,9),(000040,'2019004',4,12,000037,100.00,NULL,'2019-04-01','ABRIL',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:22','22/02/2019 - 01:40:38',9,9),(000041,'2019004',5,12,000037,100.00,NULL,'2019-05-01','MAIO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:22','22/02/2019 - 01:40:38',9,9),(000042,'2019004',6,12,000037,100.00,NULL,'2019-06-01','JUNHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000043,'2019004',7,12,000037,100.00,NULL,'2019-07-01','JULHO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000044,'2019004',8,12,000037,100.00,NULL,'2019-08-01','AGOSTO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000045,'2019004',9,12,000037,100.00,NULL,'2019-09-01','SETEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000046,'2019004',10,12,000037,100.00,NULL,'2019-10-01','OUTUBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000047,'2019004',11,12,000037,100.00,NULL,'2019-11-01','NOVEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000048,'2019004',12,12,000037,100.00,NULL,'2019-12-01','DEZEMBRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'21/01/2019 - 12:14:23','22/02/2019 - 01:40:38',9,9),(000064,'2019005',1,12,000064,100.00,NULL,'2019-01-01','JANEIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'22/02/2019 - 02:03:35',NULL,9,NULL),(000065,'2019005',2,12,000064,100.00,NULL,'2019-02-01','FEVEREIRO',NULL,NULL,NULL,NULL,NULL,2019,NULL,NULL,'22/02/2019 - 02:03:35',NULL,9,NULL);
/*!40000 ALTER TABLE `tuitions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `updates`
--

DROP TABLE IF EXISTS `updates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `updates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cli_id` varchar(45) DEFAULT NULL,
  `op` varchar(45) DEFAULT NULL,
  `command` longtext,
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `updates`
--

LOCK TABLES `updates` WRITE;
/*!40000 ALTER TABLE `updates` DISABLE KEYS */;
/*!40000 ALTER TABLE `updates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'agility'
--

--
-- Dumping routines for database 'agility'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-25  3:16:33
