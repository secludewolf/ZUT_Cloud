/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : cloud_test

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 10/05/2021 19:52:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `status` int(255) NOT NULL COMMENT '状态',
  `level` int(255) NOT NULL COMMENT '等级',
  `createTime` bigint(20) NOT NULL COMMENT '创建时间',
  `changeTime` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `size` bigint(20) NOT NULL COMMENT '文件大小',
  `status` int(255) NOT NULL COMMENT '文件状态',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',
  `quoteNumber` bigint(20) NOT NULL COMMENT '引用数目',
  `createTime` bigint(20) NOT NULL COMMENT '创建时间',
  `changeTime` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for filereport
-- ----------------------------
DROP TABLE IF EXISTS `filereport`;
CREATE TABLE `filereport`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `fileId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `memberId` int(11) NULL DEFAULT NULL,
  `memberType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `requestIp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `actionUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `actionMethod` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `optionType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `optionDesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `optionModel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `classPath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `requestMethod` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parameter` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `status` int(255) NULL DEFAULT NULL,
  `startTime` bigint(20) NULL DEFAULT NULL,
  `finishTime` bigint(20) NULL DEFAULT NULL,
  `consumingTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3052 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for share
-- ----------------------------
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userId` int(255) NULL DEFAULT NULL,
  `repoId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(255) NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  `validTime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sharereport
-- ----------------------------
DROP TABLE IF EXISTS `sharereport`;
CREATE TABLE `sharereport`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `shareId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `repoId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '仓库ID',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `status` int(255) NOT NULL COMMENT '状态',
  `level` int(255) NOT NULL COMMENT '等级',
  `createTime` bigint(20) NOT NULL COMMENT '创建时间',
  `changeTime` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for userfile
-- ----------------------------
DROP TABLE IF EXISTS `userfile`;
CREATE TABLE `userfile`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `userId` int(255) NULL DEFAULT NULL,
  `fileId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
