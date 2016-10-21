-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: mobile
-- ------------------------------------------------------
-- Server version	5.7.12-log

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
-- Table structure for table `chat_record`
--

DROP TABLE IF EXISTS `chat_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `format` varchar(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `from_userid` int(11) DEFAULT NULL,
  `to_userid` int(11) DEFAULT NULL,
  `content` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=248 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_record`
--

LOCK TABLES `chat_record` WRITE;
/*!40000 ALTER TABLE `chat_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jo_user`
--

DROP TABLE IF EXISTS `jo_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `nickname` varchar(45) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `password` char(32) NOT NULL COMMENT '密码',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `reset_key` char(32) DEFAULT NULL COMMENT '重置密码KEY',
  `reset_pwd` varchar(10) DEFAULT NULL COMMENT '重置密码VALUE',
  `error_time` datetime DEFAULT NULL COMMENT '登录错误时间',
  `error_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录错误次数',
  `error_ip` varchar(50) DEFAULT NULL COMMENT '登录错误IP',
  `activation` tinyint(1) NOT NULL DEFAULT '1' COMMENT '激活状态',
  `activation_code` char(32) DEFAULT NULL COMMENT '激活码',
  `is_rejective` tinyint(1) unsigned DEFAULT '0',
  `is_admin` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jo_user`
--

LOCK TABLES `jo_user` WRITE;
/*!40000 ALTER TABLE `jo_user` DISABLE KEYS */;
INSERT INTO `jo_user` VALUES (1,'admin','admin',NULL,'5f4dcc3b5aa765d61d8327deb882cf99','2011-01-03 00:00:00','127.0.0.1','2016-08-16 13:56:48','0:0:0:0:0:0:0:1',151,NULL,NULL,NULL,0,NULL,1,NULL,0,1),(2,'adadsada','QQQ','aaa@qaq.com','202cb962ac59075b964b07152d234b70','2016-08-12 09:58:49','0:0:0:0:0:0:0:1','2016-08-12 09:58:49','0:0:0:0:0:0:0:1',0,NULL,NULL,NULL,0,NULL,0,'f990850393b1465ab04230ee6b2c79a0',0,0),(3,'aaa','哈哈哈',NULL,'202cb962ac59075b964b07152d234b70','2016-08-12 09:58:49','0:0:0:0:0:0:0:1','2016-08-12 09:58:49','0:0:0:0:0:0:0:1',0,NULL,NULL,NULL,0,NULL,1,NULL,1,0);
/*!40000 ALTER TABLE `jo_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'mobile'
--

--
-- Dumping routines for database 'mobile'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-21 13:20:27
