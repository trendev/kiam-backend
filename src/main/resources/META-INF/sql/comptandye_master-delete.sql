-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: localhost    Database: comptandye_master
-- ------------------------------------------------------
-- Server version	5.7.19

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

DROP TABLE IF EXISTS `ADDRESS`;
DROP TABLE IF EXISTS `BILL`;
DROP TABLE IF EXISTS `BILL_PAYMENT`;
DROP TABLE IF EXISTS `BILL_PURCHASEDOFFERING`;
DROP TABLE IF EXISTS `BUSINESS`;
DROP TABLE IF EXISTS `Bill_COMMENTS`;
DROP TABLE IF EXISTS `CATEGORY`;
DROP TABLE IF EXISTS `CLIENT`;
DROP TABLE IF EXISTS `CLIENT_CATEGORY`;
DROP TABLE IF EXISTS `COLLECTIVE_GROUP`;
DROP TABLE IF EXISTS `CUSTOMER_DETAILS`;
DROP TABLE IF EXISTS `CustomerDetails_COMMENTS`;
DROP TABLE IF EXISTS `EXPENSE`;
DROP TABLE IF EXISTS `EXPENSEITEM`;
DROP TABLE IF EXISTS `EXPENSE_BUSINESS`;
DROP TABLE IF EXISTS `EXPENSE_EXPENSEITEM`;
DROP TABLE IF EXISTS `EXPENSE_PAYMENT`;
DROP TABLE IF EXISTS `Expense_CATEGORIES`;
DROP TABLE IF EXISTS `OFFERING`;
DROP TABLE IF EXISTS `OFFERING_BUSINESS`;
DROP TABLE IF EXISTS `NOTIFICATION`;
DROP TABLE IF EXISTS `PACK_CONTENT`;
DROP TABLE IF EXISTS `PAYMENT`;
DROP TABLE IF EXISTS `PAYMENTMODE`;
DROP TABLE IF EXISTS `PRODUCT`;
DROP TABLE IF EXISTS `PRODUCTRECORD`;
DROP TABLE IF EXISTS `PRODUCTREFERENCE`;
DROP TABLE IF EXISTS `PURCHASEDOFFERING`;
DROP TABLE IF EXISTS `PURCHASEDOFFERING_BUSINESS`;
DROP TABLE IF EXISTS `Product_COMMENTS`;
DROP TABLE IF EXISTS `RETURNED_ITEM`;
DROP TABLE IF EXISTS `SEQUENCE`;
DROP TABLE IF EXISTS `SOCIAL_NETWORK_ACCOUNT`;
DROP TABLE IF EXISTS `SOLD_ITEM`;
DROP TABLE IF EXISTS `USER_ACCOUNT`;
DROP TABLE IF EXISTS `USER_ACCOUNT_BUSINESS`;
DROP TABLE IF EXISTS `USER_ACCOUNT_PAYMENTMODE`;
DROP TABLE IF EXISTS `USER_ACCOUNT_USER_ACCOUNT`;
DROP TABLE IF EXISTS `USER_ACCOUNT_USER_GROUP`;
DROP TABLE IF EXISTS `USER_GROUP`;
DROP TABLE IF EXISTS `VATRATES`;
DROP TABLE IF EXISTS `VatRates_RATES`;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
