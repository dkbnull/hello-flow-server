-- ============================================================
-- HelloFlow - 软件研发项目管理工具 数据库初始化脚本
-- 技术栈：MySQL 8.0+
-- 字符集：utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS `hello_flow` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `hello_flow`;

-- ============================================================
-- 系统表（sys_ 前缀）
-- ============================================================

-- -----------------------------------------------------------
-- sys_role 系统角色表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`        varchar(50)  NOT NULL                COMMENT '角色名称',
  `code`        varchar(50)  NOT NULL                COMMENT '角色编码（ADMIN/USER）',
  `description` varchar(200) DEFAULT NULL             COMMENT '描述',
  `status`      tinyint      NOT NULL DEFAULT 1       COMMENT '0-禁用 1-启用',
  `created_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 预置角色
INSERT INTO `sys_role` (`name`, `code`, `description`, `status`) VALUES
('管理员', 'ADMIN', '系统管理员，拥有所有权限', 1),
('普通用户', 'USER', '普通用户，权限由职位决定', 1);

-- -----------------------------------------------------------
-- sys_user 系统用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`    varchar(50)  NOT NULL                COMMENT '登录名',
  `password`    varchar(100) NOT NULL                COMMENT '加密密码（BCrypt）',
  `nickname`    varchar(50)  DEFAULT NULL             COMMENT '昵称',
  `email`       varchar(100) DEFAULT NULL             COMMENT '邮箱',
  `phone`       varchar(20)  DEFAULT NULL             COMMENT '手机号',
  `avatar`      varchar(255) DEFAULT NULL             COMMENT '头像URL',
  `position_id` bigint       DEFAULT NULL             COMMENT '职位ID',
  `status`      tinyint      NOT NULL DEFAULT 1       COMMENT '0-禁用 1-启用',
  `is_deleted`  tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 预置管理员（密码: admin123，实际部署时需替换为BCrypt加密值）
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `position_id`, `status`) VALUES
('admin', '$2a$10$PLACEHOLDER_BCRYPT_HASH_REPLACE_ON_DEPLOY', '管理员', NULL, 1);

-- -----------------------------------------------------------
-- sys_user_role 用户角色关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 预置管理员角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- -----------------------------------------------------------
-- sys_dict_type 字典类型表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`       varchar(100) NOT NULL                COMMENT '字典类型名称',
  `code`       varchar(100) NOT NULL                COMMENT '字典类型编码',
  `status`     tinyint      NOT NULL DEFAULT 1       COMMENT '0-禁用 1-启用',
  `remark`     varchar(200) DEFAULT NULL             COMMENT '备注',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 预置字典类型
INSERT INTO `sys_dict_type` (`name`, `code`, `remark`) VALUES
('任务类型', 'task_type', '任务类型：需求/完善/缺陷'),
('任务优先级', 'task_priority', '任务优先级'),
('任务状态', 'task_status', '任务状态流转'),
('Sprint状态', 'sprint_status', '迭代状态'),
('项目状态', 'project_status', '项目状态');

-- -----------------------------------------------------------
-- sys_dict_data 字典数据表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id`    bigint       NOT NULL                COMMENT '字典类型ID',
  `label`      varchar(100) NOT NULL                COMMENT '数据标签',
  `value`      varchar(100) NOT NULL                COMMENT '数据值',
  `sort`       int          NOT NULL DEFAULT 0       COMMENT '排序',
  `status`     tinyint      NOT NULL DEFAULT 1       COMMENT '0-禁用 1-启用',
  `remark`     varchar(200) DEFAULT NULL             COMMENT '备注',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 预置字典数据：任务类型
INSERT INTO `sys_dict_data` (`type_id`, `label`, `value`, `sort`) VALUES
(1, '需求', '1', 1),
(1, '完善', '2', 2),
(1, '缺陷', '3', 3);

-- 预置字典数据：任务优先级
INSERT INTO `sys_dict_data` (`type_id`, `label`, `value`, `sort`) VALUES
(2, '最低', '1', 1),
(2, '低', '2', 2),
(2, '中', '3', 3),
(2, '高', '4', 4),
(2, '最高', '5', 5);

-- 预置字典数据：任务状态
INSERT INTO `sys_dict_data` (`type_id`, `label`, `value`, `sort`) VALUES
(3, '未开始', '1', 1),
(3, '进行中', '2', 2),
(3, '待审查', '3', 3),
(3, '待测试', '4', 4),
(3, '已完成', '5', 5),
(3, '已关闭', '6', 6),
(3, '取消', '7', 7);

-- 预置字典数据：Sprint状态
INSERT INTO `sys_dict_data` (`type_id`, `label`, `value`, `sort`) VALUES
(4, '计划中', '1', 1),
(4, '进行中', '2', 2),
(4, '已完成', '3', 3);

-- 预置字典数据：项目状态
INSERT INTO `sys_dict_data` (`type_id`, `label`, `value`, `sort`) VALUES
(5, '归档', '0', 1),
(5, '进行中', '1', 2);


-- ============================================================
-- 业务表（hf_ 前缀）
-- ============================================================

-- -----------------------------------------------------------
-- hf_position 职位表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_position`;
CREATE TABLE `hf_position` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`        varchar(50)  NOT NULL                COMMENT '职位名称',
  `code`        varchar(50)  NOT NULL                COMMENT '职位编码（PM/DEV/QA）',
  `description` varchar(200) DEFAULT NULL             COMMENT '描述',
  `created_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- 预置职位
INSERT INTO `hf_position` (`name`, `code`, `description`) VALUES
('项目经理', 'PM', '统筹项目，管理需求和进度'),
('开发工程师', 'DEV', '执行开发任务，可在项目中担任开发主责'),
('测试工程师', 'QA', '测试任务，可在项目中担任测试主责');

-- -----------------------------------------------------------
-- hf_project 项目表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_project`;
CREATE TABLE `hf_project` (
  `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code`         varchar(50)  NOT NULL                COMMENT '项目编码',
  `name`         varchar(100) NOT NULL                COMMENT '项目名称',
  `description`  text         DEFAULT NULL             COMMENT '项目描述',
  `pm_id`        bigint       DEFAULT NULL             COMMENT '项目经理ID',
  `dev_lead_id`  bigint       DEFAULT NULL             COMMENT '开发主责ID（开发工程师）',
  `test_lead_id` bigint       DEFAULT NULL             COMMENT '测试主责ID（测试工程师）',
  `status`       tinyint      NOT NULL DEFAULT 1       COMMENT '0-归档 1-进行中',
  `is_deleted`   tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_by`   bigint       DEFAULT NULL             COMMENT '创建者ID',
  `created_at`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_pm_id` (`pm_id`),
  KEY `idx_dev_lead_id` (`dev_lead_id`),
  KEY `idx_test_lead_id` (`test_lead_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- -----------------------------------------------------------
-- hf_project_member 项目成员表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_project_member`;
CREATE TABLE `hf_project_member` (
  `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint   NOT NULL                COMMENT '项目ID',
  `user_id`    bigint   NOT NULL                COMMENT '用户ID',
  `joined_at`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `is_deleted` tinyint  NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_user` (`project_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成员表';

-- -----------------------------------------------------------
-- hf_sprint 迭代表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_sprint`;
CREATE TABLE `hf_sprint` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint       NOT NULL                COMMENT '项目ID',
  `name`       varchar(100) NOT NULL                COMMENT 'Sprint名称',
  `goal`       varchar(500) DEFAULT NULL             COMMENT 'Sprint目标',
  `status`     tinyint      NOT NULL DEFAULT 1       COMMENT '1-计划中 2-进行中 3-已完成',
  `start_date` date         DEFAULT NULL             COMMENT '开始日期',
  `end_date`   date         DEFAULT NULL             COMMENT '结束日期',
  `is_deleted` tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_by` bigint       DEFAULT NULL             COMMENT '创建者ID',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='迭代表';

-- -----------------------------------------------------------
-- hf_task 任务表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_task`;
CREATE TABLE `hf_task` (
  `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id`       bigint       NOT NULL                COMMENT '项目ID',
  `task_code`        varchar(100) NOT NULL                COMMENT '任务编码（项目编码-序号）',
  `task_seq`         int          NOT NULL                COMMENT '任务序号（项目内自增）',
  `sprint_id`        bigint       DEFAULT NULL             COMMENT '所属Sprint（可选）',
  `parent_id`        bigint       DEFAULT NULL             COMMENT '父任务ID，NULL表示顶级任务',
  `type`             tinyint      NOT NULL DEFAULT 1       COMMENT '1-需求 2-完善 3-缺陷',
  `title`            varchar(200) NOT NULL                COMMENT '标题',
  `description`      longtext     DEFAULT NULL             COMMENT '描述（富文本）',
  `status`           tinyint      NOT NULL DEFAULT 1       COMMENT '1-未开始 2-进行中 3-待审查 4-待测试 5-已完成 6-已关闭 7-取消',
  `priority`         tinyint      NOT NULL DEFAULT 3       COMMENT '1-最低 2-低 3-中 4-高 5-最高',
  `assignee_id`      bigint       DEFAULT NULL             COMMENT '当前指派人ID',
  `reporter_id`      bigint       DEFAULT NULL             COMMENT '创建者ID',
  `developer_id`     bigint       DEFAULT NULL             COMMENT '开发工程师ID（原始指派）',
  `tester_id`        bigint       DEFAULT NULL             COMMENT '测试工程师ID（原始指派）',
  `due_date`         date         DEFAULT NULL             COMMENT '到期时间',
  `plan_start_date`  date         DEFAULT NULL             COMMENT '计划开始日期',
  `plan_end_date`    date         DEFAULT NULL             COMMENT '计划结束日期',
  `actual_start_date` date        DEFAULT NULL             COMMENT '实际开始日期',
  `actual_end_date`  date         DEFAULT NULL             COMMENT '实际完成日期',
  `close_date`       datetime     DEFAULT NULL             COMMENT '关闭日期',
  `cancel_reason`    varchar(500) DEFAULT NULL             COMMENT '取消原因',
  `delay_reason`     varchar(500) DEFAULT NULL             COMMENT '延期原因',
  `is_delayed`       tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（延期标记）',
  `is_deleted`       tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_by`       bigint       DEFAULT NULL             COMMENT '创建人ID',
  `updated_by`       bigint       DEFAULT NULL             COMMENT '更新人ID',
  `created_at`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project_status` (`project_id`, `status`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_due_date` (`due_date`),
  KEY `idx_sprint_id` (`sprint_id`),
  KEY `idx_developer_id` (`developer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- -----------------------------------------------------------
-- hf_task_relation 任务关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_task_relation`;
CREATE TABLE `hf_task_relation` (
  `id`              bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id`         bigint NOT NULL COMMENT '任务ID',
  `related_task_id` bigint NOT NULL COMMENT '关联任务ID',
  `relation_type`   tinyint NOT NULL DEFAULT 1 COMMENT '1-关联 2-依赖 3-重复',
  `created_at`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_relation` (`task_id`, `related_task_id`, `relation_type`),
  KEY `idx_related_task_id` (`related_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务关联表';

-- -----------------------------------------------------------
-- hf_task_history 任务操作历史表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_task_history`;
CREATE TABLE `hf_task_history` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id`    bigint       NOT NULL                COMMENT '任务ID',
  `user_id`    bigint       NOT NULL                COMMENT '操作人ID',
  `action`     varchar(50)  NOT NULL                COMMENT '操作类型（CREATE/ASSIGN/STATUS_CHANGE/PRIORITY_CHANGE等）',
  `field`      varchar(50)  DEFAULT NULL             COMMENT '变更字段',
  `old_value`  varchar(500) DEFAULT NULL             COMMENT '旧值',
  `new_value`  varchar(500) DEFAULT NULL             COMMENT '新值',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_created` (`task_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务操作历史表';

-- -----------------------------------------------------------
-- hf_comment 评论表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_comment`;
CREATE TABLE `hf_comment` (
  `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id`    bigint   NOT NULL                COMMENT '任务ID',
  `user_id`    bigint   NOT NULL                COMMENT '评论人ID',
  `content`    longtext NOT NULL                COMMENT '评论内容（富文本）',
  `is_deleted` tinyint  NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- -----------------------------------------------------------
-- hf_notification 通知表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_notification`;
CREATE TABLE `hf_notification` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`    bigint       NOT NULL                COMMENT '接收人ID',
  `title`      varchar(200) NOT NULL                COMMENT '通知标题',
  `content`    text         DEFAULT NULL             COMMENT '通知内容',
  `type`       tinyint      NOT NULL                COMMENT '1-任务分配 2-状态变更 3-重新打开 4-评论 5-系统通知',
  `related_id` bigint       DEFAULT NULL             COMMENT '关联业务ID（任务ID等）',
  `is_read`    tinyint      NOT NULL DEFAULT 0       COMMENT '0-未读 1-已读',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- -----------------------------------------------------------
-- hf_user_notification_setting 用户通知设置表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_user_notification_setting`;
CREATE TABLE `hf_user_notification_setting` (
  `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`      bigint   NOT NULL                COMMENT '用户ID',
  `email_enabled` tinyint NOT NULL DEFAULT 0       COMMENT '0-关闭 1-开启',
  `created_at`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知设置表';

-- -----------------------------------------------------------
-- hf_filter 过滤器表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hf_filter`;
CREATE TABLE `hf_filter` (
  `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`    bigint       NOT NULL                COMMENT '所属用户ID',
  `name`       varchar(100) NOT NULL                COMMENT '过滤器名称',
  `conditions` json         DEFAULT NULL             COMMENT '查询条件（JSON格式）',
  `is_default` tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是',
  `is_deleted` tinyint      NOT NULL DEFAULT 0       COMMENT '0-否 1-是（逻辑删除）',
  `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='过滤器表';
