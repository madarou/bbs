/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50633
Source Host           : localhost:3306
Source Database       : bbs

Target Server Type    : MYSQL
Target Server Version : 50633
File Encoding         : 65001

Date: 2016-10-28 17:39:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `job`
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` varchar(11) NOT NULL,
  `source` varchar(30) NOT NULL DEFAULT '',
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `time` datetime DEFAULT NULL,
  `jobType` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of job
-- ----------------------------

-- ----------------------------
-- Table structure for `single`
-- ----------------------------
DROP TABLE IF EXISTS `single`;
CREATE TABLE `single` (
  `id` varchar(11) NOT NULL,
  `source` varchar(30) NOT NULL DEFAULT '',
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `pic_tag` varchar(30) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of single
-- ----------------------------

-- ----------------------------
-- Table structure for `single_pic`
-- ----------------------------
DROP TABLE IF EXISTS `single_pic`;
CREATE TABLE `single_pic` (
  `id` varchar(11) NOT NULL,
  `source` varchar(30) NOT NULL DEFAULT '',
  `pic_id` int(11) DEFAULT NULL,
  `content` blob,
  PRIMARY KEY (`id`,`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of single_pic
-- ----------------------------
