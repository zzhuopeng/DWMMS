/*
Navicat MySQL Data Transfer

Source Server         : Win7MySQL
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : drivingbehaviourwarning

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2019-06-11 17:44:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for driver_info
-- ----------------------------
DROP TABLE IF EXISTS `driver_info`;
CREATE TABLE `driver_info` (
  `id` varchar(40) NOT NULL,
  `account` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  `car_type` varchar(100) DEFAULT NULL,
  `profession` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of driver_info
-- ----------------------------
INSERT INTO `driver_info` VALUES ('4028816e6b3bc9dc016b3bc9df1a0000', 'car1', '1', '小明', '18883870089', '出租车', '出租车司机');
INSERT INTO `driver_info` VALUES ('4028816e6b3c5cc2016b3c5cc4960000', 'car1', '1', '小明', '18883870089', '出租车', '出租车司机');

-- ----------------------------
-- Table structure for warning_info
-- ----------------------------
DROP TABLE IF EXISTS `warning_info`;
CREATE TABLE `warning_info` (
  `id` varchar(40) NOT NULL,
  `driver_id` varchar(40) NOT NULL,
  `time` varchar(100) DEFAULT NULL,
  `grade` varchar(2) DEFAULT NULL,
  `head` varchar(2) DEFAULT NULL,
  `fatigue` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `warning_info_driver_info_id_fk` (`driver_id`),
  CONSTRAINT `warning_info_driver_info_id_fk` FOREIGN KEY (`driver_id`) REFERENCES `driver_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warning_info
-- ----------------------------
