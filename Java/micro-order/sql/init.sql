CREATE DATABASE IF NOT EXISTS `order`
  CHARACTER SET = 'utf8mb4'
  COLLATE = 'utf8mb4_unicode_ci';

USE `order`;

CREATE TABLE IF NOT EXISTS `order_info` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `order_id` VARCHAR(255) UNIQUE NOT NULL COMMENT '订单ID',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
  `item_id` VARCHAR(32) COMMENT '商品ID',
  `count` INT COMMENT '数量',
  `status` TINYINT DEFAULT 0 COMMENT '状态,0是新创建，1是完成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   INDEX idx_user_status (user_id, status),
   INDEX idx_status_time (status, create_time)
);

CREATE TABLE IF NOT EXISTS `order_stats`  (
 `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
 `stat_time` DATETIME COMMENT '统计时间（5分钟粒度）',
 `new_count` INT DEFAULT 0 COMMENT '新订单数',
 `completed_count` INT DEFAULT 0 COMMENT '成功订单数',
 `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
 `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `alarm_info`  (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `system_name` varchar(50) COMMENT '分系统名称',
  `log`  text  COMMENT '日志内容',
  `status` TINYINT DEFAULT 0 COMMENT 'lark发送状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_system (system_name),
  INDEX idx_log_prefix (log(255))
);

CREATE TABLE `local_transaction_message` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单id',
  `topic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '主题',
  `message_body` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;