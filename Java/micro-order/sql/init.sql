CREATE DATABASE IF NOT EXISTS `order`
  CHARACTER SET = 'utf8mb4'
  COLLATE = 'utf8mb4_unicode_ci';

USE `order`;

CREATE TABLE IF NOT EXISTS `order_info` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `order_id` VARCHAR(32) UNIQUE NOT NULL COMMENT '订单ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `item_id` VARCHAR(32) COMMENT '商品ID',
  `count` INT COMMENT '数量',
  `status` TIYINT DEFAULT 0 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   INDEX idx_user_status (user_id, status),
   INDEX idx_status_time (status, create_time)
);

CREATE TABLE IF NOT EXISTS `order_stats`  (
 `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
 `stat_time` DATETIME COMMENT '统计时间（5分钟粒度）',
 `new_count` INT DEFAULT 0 COMMENT '新订单数',
 `completed_count` INT DEFAULT 0 COMMENT '成功订单数'
);

CREATE TABLE IF NOT EXISTS `alarm_info`  (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `system_name` varchar(50) COMMENT '分系统名称',
  `log`  text  COMMENT '日志内容',
  `status` TIYINT DEFAULT 0 COMMENT 'lark发送状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_system (system_name)
);