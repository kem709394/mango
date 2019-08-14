-- noinspection SpellCheckingInspectionForFile

-- 创建系统表
CREATE TABLE IF NOT EXISTS `sys_resource` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `code` VARCHAR(45) NULL COMMENT '资源代码（不可重复）',
  `name` VARCHAR(45) NULL COMMENT '资源名称',
  `path` VARCHAR(255) NULL COMMENT '访问路径',
  `method` VARCHAR(20) NULL COMMENT '请求方法',
  `map_class` VARCHAR(45) NULL COMMENT '映射类型',
  `attributes` JSON NULL COMMENT '可选属性',
  `note` TINYTEXT NULL COMMENT '资源说明',
  `priority` INT NULL COMMENT '优先级别',
  `ext_fields` JSON NULL COMMENT '扩展字段',
  `state` VARCHAR(10) NULL COMMENT '资源状态',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB
COMMENT = '系统资源';

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `avatar` VARCHAR(255) NULL COMMENT '用户头像url',
  `nick_name` VARCHAR(45) NULL COMMENT '用户昵称',
  `user_name` VARCHAR(45) NULL COMMENT '用户账号（不可重复）',
  `password` VARCHAR(255) NULL COMMENT '用户密码（加密后密文）',
  `note` TINYTEXT NULL COMMENT '用户说明',
  `privilege` JSON NULL COMMENT '用户特权（保留字段）',
  `salt` VARCHAR(20) NULL COMMENT '密码加密盐值',
  `state` VARCHAR(10) NULL COMMENT '用户状态',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC))
ENGINE = InnoDB
COMMENT = '系统用户';

CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` VARCHAR(45) NULL COMMENT '角色名称',
  `code` VARCHAR(45) NULL COMMENT '角色代码（不可重复）',
  `note` TINYTEXT NULL COMMENT '角色说明',
  `ext_fields` JSON NULL COMMENT '扩展字段',
  `state` VARCHAR(10) NULL COMMENT '角色状态',
  `priority` INT NULL COMMENT '优先级别',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB
COMMENT = '系统角色';

CREATE TABLE IF NOT EXISTS `sys_user_has_sys_role` (
  `sys_user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `sys_role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`sys_user_id`, `sys_role_id`),
  INDEX `fk_sys_user_has_sys_role_sys_role1_idx` (`sys_role_id` ASC),
  INDEX `fk_sys_user_has_sys_role_sys_user1_idx` (`sys_user_id` ASC),
  CONSTRAINT `fk_sys_user_has_sys_role_sys_user1`
    FOREIGN KEY (`sys_user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_user_has_sys_role_sys_role1`
    FOREIGN KEY (`sys_role_id`)
    REFERENCES `sys_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户拥有角色';

CREATE TABLE IF NOT EXISTS `sys_dictionary` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `name` VARCHAR(45) NULL COMMENT '数据名称',
  `code` VARCHAR(45) NULL COMMENT '数据代码（不可重复）',
  `type` VARCHAR(20) NULL COMMENT '数据类型',
  `options` JSON NULL COMMENT '数据选项',
  `note` TINYTEXT NULL COMMENT '数据说明',
  `priority` INT NULL COMMENT '优先级别',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户 ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB
COMMENT = '数据字典';

CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '参数 ID',
  `name` VARCHAR(45) NULL,
  `code` VARCHAR(45) NULL COMMENT '参数代码（不可重复）',
  `content` JSON NULL COMMENT '参数内容',
  `note` TINYTEXT NULL COMMENT '参数说明',
  `priority` INT NULL COMMENT '优先级别',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB
COMMENT = '系统参数';

CREATE TABLE IF NOT EXISTS `sys_file` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `uuid` VARCHAR(45) NULL COMMENT '文件UUID（不可重复）',
  `name` VARCHAR(255) NULL COMMENT '文件名称',
  `type` VARCHAR(45) NULL COMMENT '文件类型',
  `size` BIGINT(20) NULL COMMENT '文件大小',
  `url` VARCHAR(255) NULL COMMENT '访问地址',
  `store` VARCHAR(10) NULL COMMENT '存储方式',
  `ext_fields` JSON NULL COMMENT '扩展字段',
  `permission` JSON NULL COMMENT '访问权限（保留字段）',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `is_synced` TINYINT(1) NULL COMMENT '是否已同步（文件删除异步操作）',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC))
ENGINE = InnoDB
COMMENT = '上传文件';

CREATE TABLE IF NOT EXISTS `sys_login_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `user_name` VARCHAR(45) NULL COMMENT '登录账号',
  `ip_address` VARCHAR(100) NULL COMMENT '访问IP地址',
  `device` JSON NULL COMMENT '设备信息',
  `note` TINYTEXT NULL COMMENT '日志说明',
  `is_succeed` TINYINT(1) NULL COMMENT '是否成功',
  `login_time` DATETIME NULL COMMENT '登录时间',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '登录日志';

CREATE TABLE IF NOT EXISTS `sys_operate_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '操作日志ID',
  `path` VARCHAR(100) NULL COMMENT '访问路径',
  `method` VARCHAR(20) NULL COMMENT '请问方法',
  `parameters` JSON NULL COMMENT '请求参数',
  `ip_address` VARCHAR(100) NULL COMMENT '访问IP地址',
  `device` JSON NULL COMMENT '设备信息',
  `is_agreed` TINYINT(1) NULL COMMENT '是否通过鉴权',
  `response` JSON NULL COMMENT '返回内容',
  `sys_user_id` BIGINT(20) NOT NULL COMMENT '操作者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`),
  INDEX `fk_sys_operate_log_sys_user1_idx` (`sys_user_id` ASC),
  CONSTRAINT `fk_sys_operate_log_sys_user1`
    FOREIGN KEY (`sys_user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '操作日志';

CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `type` VARCHAR(20) NULL COMMENT '消息类型',
  `title` VARCHAR(255) NULL COMMENT '消息标题',
  `content` LONGTEXT NULL COMMENT '消息正文',
  `note` TINYTEXT NULL COMMENT '消息说明',
  `ext_fields` JSON NULL COMMENT '扩展字段',
  `sys_user_id` BIGINT(20) NOT NULL COMMENT '接收人的用户ID',
  `state` VARCHAR(10) NULL COMMENT '消息状态',
  `read_time` DATETIME NULL COMMENT '已读时间',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  INDEX `fk_sys_message_sys_user1_idx` (`sys_user_id` ASC),
  CONSTRAINT `fk_sys_message_sys_user1`
    FOREIGN KEY (`sys_user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '系统消息';

CREATE TABLE IF NOT EXISTS `sys_menu_function` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `pid` BIGINT(20) NULL COMMENT '父级菜单ID',
  `code` VARCHAR(45) NULL COMMENT '菜单代码（不可重复）',
  `name` VARCHAR(45) NULL COMMENT '菜单名称',
  `type` VARCHAR(20) NULL COMMENT '菜单类型',
  `note` TINYTEXT NULL COMMENT '菜单说明',
  `ext_fields` JSON NULL COMMENT '扩展字段',
  `state` VARCHAR(10) NULL COMMENT '菜单状态',
  `priority` INT NULL COMMENT '优先级别',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB
COMMENT = '菜单功能';

CREATE TABLE IF NOT EXISTS `sys_error_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '异常日志ID',
  `path` VARCHAR(100) NULL COMMENT '访问路径',
  `method` VARCHAR(20) NULL COMMENT '请求方法',
  `parameters` JSON NULL COMMENT '请求参数',
  `ip_address` VARCHAR(100) NULL COMMENT '访问IP地址',
  `device` JSON NULL COMMENT '设备信息',
  `content` LONGTEXT NULL COMMENT '异常信息',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '异常日志';

CREATE TABLE IF NOT EXISTS `sys_role_has_sys_menu_function` (
  `sys_role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `sys_menu_function_id` BIGINT(20) NOT NULL COMMENT '菜单ID',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`sys_role_id`, `sys_menu_function_id`),
  INDEX `fk_sys_role_has_sys_menu_function_sys_menu_function1_idx` (`sys_menu_function_id` ASC),
  INDEX `fk_sys_role_has_sys_menu_function_sys_role1_idx` (`sys_role_id` ASC),
  CONSTRAINT `fk_sys_role_has_sys_menu_function_sys_role1`
    FOREIGN KEY (`sys_role_id`)
    REFERENCES `sys_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_role_has_sys_menu_function_sys_menu_function1`
    FOREIGN KEY (`sys_menu_function_id`)
    REFERENCES `sys_menu_function` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '角色拥有菜单';

CREATE TABLE IF NOT EXISTS `sys_role_has_sys_resource` (
  `sys_role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `sys_resource_id` BIGINT(20) NOT NULL COMMENT '资源ID',
  `filter` JSON NULL COMMENT '过滤器（暂时只能过滤资源返回的字段）',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`sys_role_id`, `sys_resource_id`),
  INDEX `fk_sys_role_has_sys_resource_sys_resource1_idx` (`sys_resource_id` ASC),
  INDEX `fk_sys_role_has_sys_resource_sys_role1_idx` (`sys_role_id` ASC),
  CONSTRAINT `fk_sys_role_has_sys_resource_sys_role1`
    FOREIGN KEY (`sys_role_id`)
    REFERENCES `sys_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sys_role_has_sys_resource_sys_resource1`
    FOREIGN KEY (`sys_resource_id`)
    REFERENCES `sys_resource` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '角色拥有资源';

CREATE TABLE IF NOT EXISTS `sys_user_token` (
  `sys_user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `token` VARCHAR(45) NULL COMMENT '授权证书',
  `expire_time` DATETIME NULL COMMENT '过期时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  INDEX `fk_sys_user_token_sys_user1_idx` (`sys_user_id` ASC),
  PRIMARY KEY (`sys_user_id`),
  CONSTRAINT `fk_sys_user_token_sys_user1`
    FOREIGN KEY (`sys_user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户登录授权';

CREATE TABLE IF NOT EXISTS `sys_captcha` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '验证码ID',
  `session` VARCHAR(45) NULL COMMENT '请求session标识',
  `code` VARCHAR(6) NULL COMMENT '验证码',
  `expire_time` DATETIME NULL COMMENT '过期时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '验证码';

CREATE TABLE IF NOT EXISTS `sys_user_ext` (
  `sys_user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `email` VARCHAR(100) NULL COMMENT '邮箱地址',
  `mobile` VARCHAR(20) NULL COMMENT '电话号码',
  PRIMARY KEY (`sys_user_id`),
  CONSTRAINT `fk_sys_user_ext_sys_user1`
    FOREIGN KEY (`sys_user_id`)
    REFERENCES `sys_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户扩展字段';

CREATE TABLE IF NOT EXISTS `sys_task` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `name` VARCHAR(45) NULL COMMENT '任务名称',
  `bean` VARCHAR(45) NULL COMMENT '对应的Spring bean',
  `params` JSON NULL COMMENT '执行参数',
  `cron` VARCHAR(100) NULL COMMENT 'cron表达式',
  `note` TINYTEXT NULL COMMENT '任务说明',
  `state` VARCHAR(10) NULL COMMENT '任务状态',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户 ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `modifier_id` BIGINT(20) NULL COMMENT '最后修改者的用户ID',
  `modify_time` DATETIME NULL COMMENT '最后修改时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '定时任务';

CREATE TABLE IF NOT EXISTS `sys_task_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `sys_task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
  `bean` VARCHAR(45) NULL COMMENT '对应的Spring bean',
  `params` JSON NULL COMMENT '执行参数',
  `note` TINYTEXT NULL COMMENT '任务说明',
  `is_succeed` TINYINT(1) NULL COMMENT '是否成功',
  `message` LONGTEXT NULL COMMENT '异常信息',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `create_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `fk_sys_task_log_sys_task1_idx` (`sys_task_id` ASC),
  CONSTRAINT `fk_sys_task_log_sys_task1`
    FOREIGN KEY (`sys_task_id`)
    REFERENCES `sys_task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '任务日志';

CREATE TABLE IF NOT EXISTS `sys_mail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '邮件ID',
  `subject` VARCHAR(200) NULL COMMENT '邮件主题',
  `content` LONGTEXT NULL COMMENT '邮件正文',
  `from_site` VARCHAR(45) NULL COMMENT '发送地址',
  `to_site` JSON NULL COMMENT '接收地址',
  `cc_site` JSON NULL COMMENT '抄送地址',
  `attachment` JSON NULL COMMENT '附件',
  `inline` JSON NULL COMMENT '嵌入图片',
  `counter` INT NULL COMMENT '发送失败计数器',
  `state` VARCHAR(10) NULL COMMENT '邮件状态',
  `send_time` DATETIME NULL COMMENT '发送时间',
  `message` LONGTEXT NULL COMMENT '异常信息',
  `is_deleted` TINYINT(1) NULL COMMENT '是否已删除',
  `creator_id` BIGINT(20) NULL COMMENT '创建者的用户ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `remark` VARCHAR(255) NULL COMMENT '备注（保留字段）',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '系统邮件';

-- 初始化数据
INSERT INTO `sys_config`(`id`, `name`, `code`, `content`, `note`, `priority`, `modifier_id`, `modify_time`, `remark`) VALUES (1, '存储设置', 'STORE_CONFIG', '{\"mode\": \"local\", \"local\": {\"domain\": \"http://localhost:8080\", \"pathPrefix\": \"test\", \"storeDirectory\": \"E://upload\"}}', '设置文件存储方式及相关参数', 1, 1, '2019-08-02 17:45:40', '');
INSERT INTO `sys_config`(`id`, `name`, `code`, `content`, `note`, `priority`, `modifier_id`, `modify_time`, `remark`) VALUES (2, '邮件设置', 'MAIL_CONFIG', '{\"host\": \"smtp.qq.com\", \"port\": \"25\", \"sender\": \"1558316297@qq.com\", \"password\": \"ziegxwwsehixibfd\", \"username\": \"1558316297@qq.com\"}', NULL, 1, 1, '2019-08-03 11:53:59', NULL);
INSERT INTO `sys_config`(`id`, `name`, `code`, `content`, `note`, `priority`, `modifier_id`, `modify_time`, `remark`) VALUES (3, '日志设置', 'LOG_CONFIG', '{\"task\": true, \"error\": true, \"login\": true, \"operate\": false}', NULL, 1, 1, '2019-08-03 16:10:54', NULL);
INSERT INTO `sys_config`(`id`, `name`, `code`, `content`, `note`, `priority`, `modifier_id`, `modify_time`, `remark`) VALUES (4, 'Druid监控设置', 'DRUID_CONFIG', '{\"password\": \"123456\", \"username\": \"admin\", \"index_url\": \"http://localhost:8080/druid/index.html\"}', NULL, 1, 1, '2019-08-02 10:52:03', NULL);

INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, '字典类型', 'DICT_TYPE', '1', '[{\"code\": \"1\", \"name\": \"系统\"}, {\"code\": \"2\", \"name\": \"业务\"}]', '', 0, 0, 1, '2019-07-13 11:39:00', 1, '2019-07-13 16:26:00', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, '使用状态', 'USE_STATE', '1', '[{\"code\": \"0\", \"name\": \"禁用\"}, {\"code\": \"1\", \"name\": \"可用\"}]', '', 0, 0, 1, '2019-07-13 12:32:52', 1, '2019-07-13 17:46:56', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (3, '请求方法', 'REQUEST_METHOD', '1', '[{\"code\": \"GET\", \"name\": \"GET\"}, {\"code\": \"POST\", \"name\": \"POST\"}, {\"code\": \"PUT\", \"name\": \"PUT\"}, {\"code\": \"DELETE\", \"name\": \"DELETE\"}]', '', 0, 0, 1, '2019-07-13 12:34:26', 1, '2019-07-13 17:48:06', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (4, '菜单类型', 'MENU_TYPE', '1', '[{\"code\": \"0\", \"name\": \"集合\"}, {\"code\": \"1\", \"name\": \"菜单\"}, {\"code\": \"2\", \"name\": \"按钮\"}]', '', 0, 0, 1, '2019-07-13 12:35:31', 1, '2019-08-03 14:42:25', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (5, '消息类型', 'MESSAGE_TYPE', '1', '[{\"code\": \"1\", \"name\": \"系统\"}, {\"code\": \"2\", \"name\": \"业务\"}]', '', 0, 0, 1, '2019-07-13 12:37:27', 1, '2019-08-03 09:39:57', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (6, '消息状态', 'MESSAGE_STATE', '1', '[{\"code\": \"0\", \"name\": \"未读\"}, {\"code\": \"1\", \"name\": \"已读\"}, {\"code\": \"2\", \"name\": \"删除\"}]', '', 0, 0, 1, '2019-07-30 11:58:06', 1, '2019-07-30 12:02:40', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (7, '任务状态', 'TASK_STATE', '1', '[{\"code\": \"0\", \"name\": \"停止\"}, {\"code\": \"1\", \"name\": \"运行\"}]', '', 0, 0, 1, '2019-08-01 10:20:45', 1, '2019-08-01 13:10:42', NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (8, '存储方式', 'STORE_MODE', '1', '[{\"code\": \"local\", \"name\": \"本地存储\"}, {\"code\": \"aliyun\", \"name\": \"阿里云\"}, {\"code\": \"qcloud\", \"name\": \"腾讯云\"}]', '', 0, 0, 1, '2019-08-03 09:36:03', NULL, NULL, NULL);
INSERT INTO `sys_dictionary`(`id`, `name`, `code`, `type`, `options`, `note`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (9, '邮件状态', 'MAIL_STATE', '1', '[{\"code\": \"0\", \"name\": \"等待发送\"}, {\"code\": \"1\", \"name\": \"发送成功\"}, {\"code\": \"2\", \"name\": \"撤销发送\"}, {\"code\": \"-1\", \"name\": \"发送失败\"}]', '', 0, 0, 1, '2019-08-03 11:37:16', 1, '2019-08-03 12:48:47', NULL);

INSERT INTO `sys_user`(`id`, `avatar`, `nick_name`, `user_name`, `password`, `note`, `privilege`, `salt`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, 'http://q1.qlogo.cn/g?b=qq&nk=269237955&s=100&t=1565083447', 'root', 'root', '6430e688e153a719102793b68876b15c1fbae338394292396ea7e53ca2c88fe3', NULL, '{}', '111111', '1', 0, 0, '2019-07-06 15:02:17', 1, '2019-08-02 15:07:20', NULL);
INSERT INTO `sys_user`(`id`, `avatar`, `nick_name`, `user_name`, `password`, `note`, `privilege`, `salt`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, 'http://q1.qlogo.cn/g?b=qq&nk=269237955&s=100&t=1565083447', 'admin', 'admin', '4a49c8935bd3ca437b577262d5566fcf2cce725af1c6c892d4e3570722e1487e', '11', '{}', 'ELeGAKRKnc6S4DobMVxn', '1', 0, 1, '2019-07-18 11:10:30', 1, '2019-07-18 12:14:17', NULL);

INSERT INTO `sys_role`(`id`, `name`, `code`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, '超级管理员', 'ROLE_ROOT', NULL, '{}', '1', 1, 0, 1, '2019-07-07 10:47:27', NULL, NULL, NULL);
INSERT INTO `sys_role`(`id`, `name`, `code`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, '管理员', 'ROLE_ADMIN', '', '{}', '1', 2, 0, 1, '2019-07-13 18:31:01', 1, '2019-07-31 10:12:26', NULL);

INSERT INTO `sys_user_has_sys_role`(`sys_user_id`, `sys_role_id`, `remark`) VALUES (1, 1, NULL);
INSERT INTO `sys_user_has_sys_role`(`sys_user_id`, `sys_role_id`, `remark`) VALUES (2, 2, NULL);


INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, 'sys:user:find', '系统用户-查询', '/v1/sys/user', 'GET', 'com.mangogo.sys.entity.SysUser', '[\"userName\", \"password\"]', '', 0, '{}', '1', 0, 1, '2019-07-13 18:03:27', 1, '2019-08-06 11:27:20', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, 'sys:user:detail', '系统用户-详情', '/v1/sys/user/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-07-17 13:56:19', 1, '2019-08-04 10:19:41', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (3, 'sys:user:create', '系统用户-添加', '/v1/sys/user', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-07-17 13:57:30', 1, '2019-08-04 10:19:45', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (4, 'sys:user:update', '系统用户-修改', '/v1/sys/user/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-07-17 13:58:09', 1, '2019-08-04 10:19:48', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (5, 'sys:user:delete', '系统用户-删除', '/v1/sys/user', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-07-17 13:58:47', 1, '2019-08-04 10:19:52', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (6, 'sys:user:list', '系统用户-列表', '/v1/sys/user/list', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 14:26:44', 1, '2019-08-04 10:19:58', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (7, 'sys:role:find', '用户角色-查询', '/v1/sys/role', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:50:18', 1, '2019-08-04 10:20:11', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (8, 'sys:role:detail', '用户角色-详情', '/v1/sys/role/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:56:23', 1, '2019-08-04 10:20:15', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (9, 'sys:role:create', '用户角色-添加', '/v1/sys/role', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:56:42', 1, '2019-08-04 10:20:19', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (10, 'sys:role:update', '用户角色-修改', '/v1/sys/role/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:56:56', 1, '2019-08-04 10:20:23', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (11, 'sys:role:delete', '用户角色-删除', '/v1/sys/role', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:57:08', 1, '2019-08-04 10:20:27', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (12, 'sys:role:list', '用户角色-列表', '/v1/sys/role/list', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:57:26', 1, '2019-08-04 10:20:32', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (13, 'sys:role:menu_function:detail', '用户角色-拥有菜单', '/v1/sys/role/{id}/menu_function', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:57:39', 1, '2019-08-04 10:20:37', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (14, 'sys:role:menu_function:update', '用户角色-更新菜单', '/v1/sys/role/{id}/menu_function', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:57:54', 1, '2019-08-04 10:20:41', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (15, 'sys:role:resource:detail', '用户角色-拥有资源', '/v1/sys/role/{id}/resource', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:58:08', 1, '2019-08-04 10:20:48', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (16, 'sys:role:resource:update', '用户角色-更新资源', '/v1/sys/role/{id}/resource', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 16:58:23', 1, '2019-08-04 10:20:55', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (17, 'sys:resource:find', '系统资源-查询', '/v1/sys/resource', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:22:19', 1, '2019-08-04 10:21:03', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (18, 'sys:resource:detail', '系统资源-详情', '/v1/sys/resource/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:22:32', 1, '2019-08-04 10:21:07', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (19, 'sys:resource:create', '系统资源-添加', '/v1/sys/resource', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:22:44', 1, '2019-08-04 10:21:11', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (20, 'sys:resource:update', '系统资源-修改', '/v1/sys/resource/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:22:58', 1, '2019-08-04 10:21:15', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (21, 'sys:resource:delete', '系统资源-删除', '/v1/sys/resource', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:23:14', 1, '2019-08-04 10:21:19', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (22, 'sys:resource:list', '系统资源-列表', '/v1/sys/resource/list', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:23:31', 1, '2019-08-04 10:21:23', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (23, 'sys:menu_function:find', '菜单功能-查询', '/v1/sys/menu_function', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:16', 1, '2019-08-04 10:22:05', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (24, 'sys:menu_function:detail', '菜单功能-详情', '/v1/sys/menu_function/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:20', 1, '2019-08-04 10:22:10', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (25, 'sys:menu_function:create', '菜单功能-添加', '/v1/sys/menu_function', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:23', 1, '2019-08-04 10:22:14', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (26, 'sys:menu_function:update', '菜单功能-修改', '/v1/sys/menu_function/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:26', 1, '2019-08-04 10:22:19', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (27, 'sys:menu_function:delete', '菜单功能-删除', '/v1/sys/menu_function/{id}', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:36', 1, '2019-08-04 10:22:22', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (28, 'sys:menu_function:list', '菜单功能-列表', '/v1/sys/menu_function/list', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:40', 1, '2019-08-04 10:22:26', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (29, 'sys:menu_function:tree', '菜单功能-树形结构', '/v1/sys/menu_function/tree', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:41:43', 1, '2019-08-04 10:22:30', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (30, 'sys:config:find', '系统参数-查询', '/v1/sys/config', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:48:56', 1, '2019-08-04 10:22:37', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (31, 'sys:config:detail', '系统参数-详情', '/v1/sys/config/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:49:12', 1, '2019-08-04 10:22:42', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (32, 'sys:config:update', '系统参数-修改', '/v1/sys/config/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:49:25', 1, '2019-08-04 10:22:46', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (33, 'sys:config:content:detail', '系统参数-内容数据', '/v1/sys/config/content', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:49:43', 1, '2019-08-04 10:22:50', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (34, 'sys:config:content:update', '系统参数-更新内容', '/v1/sys/config/content', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:49:56', 1, '2019-08-04 10:22:55', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (35, 'sys:dictionary:find', '数据字典-查询', '/v1/sys/dictionary', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:58:45', 1, '2019-08-04 10:23:05', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (36, 'sys:dictionary:detail', '数据字典-详情', '/v1/sys/dictionary/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:58:57', 1, '2019-08-04 10:23:09', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (37, 'sys:dictionary:create', '数据字典-添加', '/v1/sys/dictionary', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:59:08', 1, '2019-08-04 10:23:13', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (38, 'sys:dictionary:update', '数据字典-修改', '/v1/sys/dictionary/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:59:17', 1, '2019-08-04 10:23:16', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (39, 'sys:dictionary:delete', '数据字典-删除', '/v1/sys/dictionary', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:59:29', 1, '2019-08-04 10:23:19', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (40, 'sys:dictionary:list', '数据字典-列表', '/v1/sys/dictionary', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 17:59:43', 1, '2019-08-04 10:23:23', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (41, 'sys:file:find', '用户文件-查询', '/v1/sys/file', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:08:20', 1, '2019-08-04 10:24:36', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (42, 'sys:file:detail', '用户文件-详情', '/v1/sys/file/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:08:32', 1, '2019-08-04 10:24:41', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (43, 'sys:file:upload', '用户文件-上传', '/v1/sys/file', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:08:46', 1, '2019-08-04 10:24:46', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (44, 'sys:file:delete', '用户文件-删除', '/v1/sys/file', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:09:01', 1, '2019-08-04 10:24:30', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (45, 'sys:task:find', '定时任务-查询', '/v1/sys/task', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:16:56', 1, '2019-08-04 10:24:55', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (46, 'sys:task:detail', '定时任务-详情', '/v1/sys/task/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:17:11', 1, '2019-08-04 10:24:59', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (47, 'sys:task:create', '定时任务-添加', '/v1/sys/task', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:17:21', 1, '2019-08-04 10:25:03', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (48, 'sys:task:update', '定时任务-修改', '/v1/sys/task/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:17:34', 1, '2019-08-04 10:25:06', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (49, 'sys:task:delete', '定时任务-删除', '/v1/sys/task/{id}', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:17:48', 1, '2019-08-04 10:25:10', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (50, 'sys:task:list', '定时任务-列表', '/v1/sys/task', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:17:59', 1, '2019-08-04 10:25:13', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (51, 'sys:task:operate', '定时任务-操作', '/v1/sys/task/{id}', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:18:54', 1, '2019-08-04 10:25:19', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (52, 'sys:message:find', '系统消息-查询', '/v1/sys/message', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:41:55', 1, '2019-08-04 10:25:26', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (53, 'sys:message:detail', '系统消息-详情', '/v1/sys/message/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:42:04', 1, '2019-08-04 10:25:30', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (54, 'sys:message:create', '系统消息-创建', '/v1/sys/message', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:42:16', 1, '2019-08-04 10:25:34', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (55, 'sys:message:update', '系统消息-修改', '/v1/sys/message/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:42:26', 1, '2019-08-04 10:25:37', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (56, 'sys:message:delete', '系统消息-删除', '/v1/sys/message', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-03 18:42:36', 1, '2019-08-04 10:25:41', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (57, 'sys:mail:find', '邮件消息-查询', '/v1/sys/mail', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 09:54:27', 1, '2019-08-04 10:25:59', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (58, 'sys:mail:detail', '邮件消息-查询', '/v1/sys/mail/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 09:55:03', 1, '2019-08-04 10:26:08', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (59, 'sys:mail:create', '邮件消息-添加', '/v1/sys/mail', 'POST', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 09:55:13', 1, '2019-08-04 10:26:16', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (60, 'sys:mail:update', '邮件消息-修改', '/v1/sys/mail/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 09:55:24', 1, '2019-08-04 10:26:22', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (61, 'sys:mail:delete', '邮件消息-删除', '/v1/sys/mail', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 09:55:36', 1, '2019-08-04 10:26:29', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (62, 'sys:error_log:find', '异常日志-查询', '/v1/sys/error_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:13:35', 1, '2019-08-04 10:16:32', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (63, 'sys:error_log:detail', '异常日志-详情', '/v1/sys/error_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:13:48', 1, '2019-08-04 10:16:47', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (64, 'sys:error_log:delete', '异常日志-删除', '/v1/sys/error_log', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:13:59', 1, '2019-08-04 10:16:58', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (65, 'sys:login_log:find', '登录日志-查询', '/v1/sys/login_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:14:12', 1, '2019-08-04 10:17:37', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (66, 'sys:login_log:detail', '登录日志-详情', '/v1/sys/login_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:14:24', 1, '2019-08-04 10:17:47', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (67, 'sys:login_log:delete', '登录日志-删除', '/v1/sys/login_log', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:14:40', 1, '2019-08-04 10:17:55', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (68, 'sys:operate_log:find', '操作日志-查询', '/v1/sys/operate_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:14:55', 1, '2019-08-04 10:18:14', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (69, 'sys:operate_log:detail', '操作日志-详情', '/v1/sys/operate_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:15:05', 1, '2019-08-04 10:18:24', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (70, 'sys:operate_log:delete', '操作日志-删除', '/v1/sys/operate_log', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:15:15', 1, '2019-08-04 10:18:32', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (71, 'sys:task_log:find', '任务日志-查询', '/v1/sys/task_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:15:31', 1, '2019-08-04 10:18:50', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (72, 'sys:task_log:detail', '任务日志-详情', '/v1/sys/task_log', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:15:41', 1, '2019-08-04 10:19:01', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (73, 'sys:task_log:delete', '任务日志-删除', '/v1/sys/task_log', 'DELETE', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-04 10:16:00', 1, '2019-08-04 10:19:12', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (74, 'sys:self:info:detail', '当前用户-用户资料', '/v1/self/info', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:12:57', 1, '2019-08-05 10:15:52', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (75, 'sys:self:info:update', '当前用户-更新资料', '/v1/self/info', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:13:15', 1, '2019-08-05 10:16:15', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (76, 'sys:self:message:count', '当前用户-消息数量', '/v1/self/message/count', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:13:33', 1, '2019-08-05 10:16:56', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (77, 'sys:self:message:list', '当前用户-消息列表', '/v1/self/message/list', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:13:43', 1, '2019-08-05 10:17:15', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (78, 'sys:self:message:detail', '当前用户-消息详情', '/v1/self/message/{id}', 'GET', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:13:56', 1, '2019-08-05 10:17:47', NULL);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `path`, `method`, `map_class`, `attributes`, `note`, `priority`, `ext_fields`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (79, 'sys:self:message:update', '当前用户-更新消息', '/v1/self/message/{id}', 'PUT', NULL, '[]', '', 0, '{}', '1', 0, 1, '2019-08-05 10:15:05', 1, '2019-08-05 10:18:00', NULL);

INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 1, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 2, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 3, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 4, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 5, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 6, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 7, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 8, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 9, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 10, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 11, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 12, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 13, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 14, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 15, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 16, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 17, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 18, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 19, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 20, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 21, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 22, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 23, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 24, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 25, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 26, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 27, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 28, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 29, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 30, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 31, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 32, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 33, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 34, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 35, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 36, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 37, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 38, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 39, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 40, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 41, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 42, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 43, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 44, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 45, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 46, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 47, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 48, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 49, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 50, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 51, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 52, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 53, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 54, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 55, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 56, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 57, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 58, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 59, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 60, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 61, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 62, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 63, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 64, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 65, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 66, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 67, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 68, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 69, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 70, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 71, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 72, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 73, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 74, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 75, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 76, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 77, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 78, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (1, 79, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 1, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 2, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 6, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 7, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 8, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 12, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 13, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 15, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 17, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 18, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 22, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 23, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 24, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 28, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 29, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 30, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 31, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 33, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 35, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 36, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 40, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 41, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 42, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 43, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 45, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 46, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 50, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 52, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 53, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 54, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 55, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 56, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 57, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 58, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 59, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 60, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 61, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 62, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 63, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 64, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 65, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 66, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 67, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 68, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 69, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 70, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 71, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 72, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 73, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 74, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 75, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 76, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 77, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 78, '{\"fields\": []}', NULL);
INSERT INTO `sys_role_has_sys_resource`(`sys_role_id`, `sys_resource_id`, `filter`, `remark`) VALUES (2, 79, '{\"fields\": []}', NULL);

INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, 0, 'sys_part1', '权限管理', '1', '', '{}', '1', 1, 0, 1, '2019-07-16 16:33:28', 1, '2019-08-03 14:46:46', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, 0, 'sys_part2', '系统设置', '1', '', '{}', '1', 2, 0, 1, '2019-07-16 16:44:10', 1, '2019-08-03 14:46:53', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (3, 0, 'sys_part3', '消息管理', '1', '', '{}', '1', 3, 0, 1, '2019-07-16 16:44:11', 1, '2019-08-03 14:47:05', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (4, 1, 'sys_user', '用户管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-16 18:20:48', 1, '2019-07-25 16:31:59', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (5, 1, 'sys_role', '角色管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-16 18:21:22', 1, '2019-07-25 16:32:08', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (6, 0, 'sys_part4', '日志管理', '1', '', '{}', '1', 4, 0, 1, '2019-07-17 09:46:12', 1, '2019-08-03 14:47:10', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (7, 2, 'sys_config', '参数管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:46:29', 1, '2019-07-25 16:32:31', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (8, 2, 'sys_dictionary', '字典管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:46:41', 1, '2019-07-25 16:32:42', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (9, 2, 'sys_menu_function', '菜单管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:46:53', 1, '2019-07-25 16:32:52', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (10, 2, 'sys_resource', '资源管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:47:03', 1, '2019-07-25 16:33:03', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (11, 3, 'sys_message', '系统消息', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:47:21', 1, '2019-07-25 16:49:06', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (12, 6, 'sys_login_log', '登录日志', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:47:41', 1, '2019-07-25 16:49:26', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (13, 6, 'sys_operate_log', '操作日志', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:47:55', 1, '2019-07-25 16:49:37', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (14, 6, 'sys_error_log', '异常日志', '1', '', '{}', '1', 0, 0, 1, '2019-07-17 09:48:06', 1, '2019-07-25 16:49:48', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (15, 3, 'sys_mail', '邮件消息', '1', '', '{}', '1', 0, 0, 1, '2019-07-25 16:29:12', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (16, 2, 'sys_file', '文件管理', '1', '', '{}', '1', 0, 0, 1, '2019-07-25 16:33:42', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (17, 0, 'monitor', '系统监控', '1', '', '{}', '1', 5, 0, 1, '2019-07-25 16:47:32', 1, '2019-08-03 14:47:15', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (18, 0, 'document', '系统文档', '1', '', '{}', '1', 6, 0, 1, '2019-07-25 16:47:49', 1, '2019-08-03 14:47:36', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (19, 17, 'mon_druid', 'SQL监控', '1', '', '{}', '1', 0, 0, 1, '2019-07-25 16:48:24', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (20, 18, 'doc_swagger', '接口文档', '1', '', '{}', '1', 0, 0, 1, '2019-07-25 16:48:41', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (21, 4, 'sys_user_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-07-25 18:08:18', 1, '2019-07-25 18:09:10', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (22, 4, 'sys_user_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-07-25 18:08:36', 1, '2019-07-25 18:14:00', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (23, 4, 'sys_user_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-07-25 18:14:33', 1, '2019-07-25 18:15:04', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (24, 4, 'sys_user_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-07-25 18:14:53', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (25, 2, 'sys_task', '定时任务', '1', '', '{}', '1', 0, 0, 1, '2019-08-01 09:24:14', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (26, 6, 'sys_task_log', '任务日志', '1', '', '{}', '1', 0, 0, 1, '2019-08-01 17:14:55', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (27, 0, 'base', '基础功能', '0', '', '{}', '1', 0, 0, 1, '2019-08-03 14:43:57', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (28, 27, 'lang', '语言切换', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:51:59', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (29, 27, 'message', '消息中心', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:52:31', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (30, 27, 'setting', '个人设置', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:52:56', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (31, 5, 'sys_role_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:54:19', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (32, 5, 'sys_role_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:54:52', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (33, 5, 'sys_role_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:55:25', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (34, 5, 'sys_role_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:55:49', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (35, 5, 'sys_role_menu', '菜单', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:56:15', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (36, 5, 'sys_role_resource', '资源', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:56:33', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (37, 7, 'sys_config_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:57:30', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (38, 7, 'sys_config_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:57:56', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (39, 8, 'sys_dictionary_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:58:55', 1, '2019-08-03 14:59:56', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (40, 8, 'sys_dictionary_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:59:11', 1, '2019-08-03 15:00:13', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (41, 8, 'sys_dictionary_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:59:25', 1, '2019-08-03 15:00:20', NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (42, 8, 'sys_dictionary_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 14:59:46', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (43, 9, 'sys_menu_function_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:01:08', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (44, 9, 'sys_menu_function_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:01:30', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (45, 9, 'sys_menu_function_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:01:48', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (46, 9, 'sys_menu_function_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:02:09', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (47, 10, 'sys_resource_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:02:50', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (48, 10, 'sys_resource_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:03:10', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (49, 10, 'sys_resource_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:03:26', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (50, 10, 'sys_resource_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:03:39', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (51, 16, 'sys_file_config', '设置', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:05:41', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (52, 16, 'sys_file_upload', '上传', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:06:32', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (53, 16, 'sys_file_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:06:44', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (54, 16, 'sys_file_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:07:01', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (55, 25, 'sys_task_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:07:53', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (56, 25, 'sys_task_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:08:07', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (57, 25, 'sys_task_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:08:23', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (58, 25, 'sys_task_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:08:44', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (59, 25, 'sys_task_execute', '执行', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:10:28', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (60, 25, 'sys_task_start', '启动', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:10:56', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (61, 25, 'sys_task_stop', '停止', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:11:22', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (62, 25, 'sys_task_log_page', '日志', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:14:44', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (63, 11, 'sys_message_create', '添加', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:15:40', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (64, 11, 'sys_message_update', '修改', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:15:52', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (65, 11, 'sys_message_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:16:08', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (66, 11, 'sys_message_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:16:22', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (67, 15, 'sys_mail_config', '设置', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:17:16', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (68, 15, 'sys_mail_send', '发送', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:17:29', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (69, 15, 'sys_mail_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:17:42', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (70, 15, 'sys_mail_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:17:56', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (71, 15, 'sys_mail_cancel', '撤销', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:18:21', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (72, 15, 'sys_mail_retry', '重发', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:19:03', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (73, 12, 'sys_login_log_open', '开启', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:20:34', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (74, 12, 'sys_login_log_close', '关闭', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:20:50', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (75, 12, 'sys_login_log_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:21:07', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (76, 12, 'sys_login_log_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:21:21', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (77, 13, 'sys_operate_log_open', '开启', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:21:49', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (78, 13, 'sys_operate_log_close', '关闭', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:22:07', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (79, 13, 'sys_operate_log_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:22:55', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (80, 13, 'sys_operate_log_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:23:09', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (81, 14, 'sys_error_log_open', '开启', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:23:39', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (82, 14, 'sys_error_log_close', '关闭', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:23:53', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (83, 14, 'sys_error_log_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:24:13', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (84, 14, 'sys_error_log_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:24:29', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (85, 26, 'sys_task_log_open', '开启', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:24:59', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (86, 26, 'sys_task_log_close', '关闭', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:25:13', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (87, 26, 'sys_task_log_delete', '删除', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:25:25', NULL, NULL, NULL);
INSERT INTO `sys_menu_function`(`id`, `pid`, `code`, `name`, `type`, `note`, `ext_fields`, `state`, `priority`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (88, 26, 'sys_task_log_detail', '详情', '2', '', '{}', '1', 0, 0, 1, '2019-08-03 15:25:41', NULL, NULL, NULL);

INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 1, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 2, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 3, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 4, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 5, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 6, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 7, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 8, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 9, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 10, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 11, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 12, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 13, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 14, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 15, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 16, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 17, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 18, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 19, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 20, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 21, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 22, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 23, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 24, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 25, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 26, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 27, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 28, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 29, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 30, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 31, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 32, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 33, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 34, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 35, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 36, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 37, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 38, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 39, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 40, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 41, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 42, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 43, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 44, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 45, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 46, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 47, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 48, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 49, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 50, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 51, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 52, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 53, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 54, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 55, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 56, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 57, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 58, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 59, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 60, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 61, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 62, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 63, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 64, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 65, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 66, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 67, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 68, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 69, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 70, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 71, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 72, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 73, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 74, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 75, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 76, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 77, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 78, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 79, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 80, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 81, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 82, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 83, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 84, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 85, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 86, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 87, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (1, 88, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 1, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 2, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 3, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 4, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 5, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 6, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 7, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 8, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 9, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 10, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 11, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 12, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 13, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 14, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 15, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 16, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 17, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 18, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 19, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 20, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 21, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 22, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 23, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 24, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 25, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 26, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 27, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 28, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 29, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 30, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 31, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 32, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 33, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 34, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 35, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 36, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 37, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 38, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 39, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 40, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 41, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 42, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 43, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 44, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 45, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 46, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 47, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 48, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 49, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 50, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 51, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 52, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 53, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 54, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 55, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 56, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 57, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 58, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 59, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 60, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 61, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 62, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 63, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 64, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 65, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 66, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 67, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 68, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 69, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 70, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 71, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 72, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 73, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 74, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 75, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 76, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 77, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 78, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 79, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 80, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 81, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 82, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 83, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 84, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 85, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 86, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 87, NULL);
INSERT INTO `sys_role_has_sys_menu_function`(`sys_role_id`, `sys_menu_function_id`, `remark`) VALUES (2, 88, NULL);

INSERT INTO `sys_task`(`id`, `name`, `bean`, `params`, `cron`, `note`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (1, '测试任务', 'testJob', '{}', '0/1 * * * * ? ', '控制台打印test...', '0', 0, 1, '2019-08-01 11:43:33', 1, '2019-08-01 13:42:31', NULL);
INSERT INTO `sys_task`(`id`, `name`, `bean`, `params`, `cron`, `note`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (2, '邮件发送任务', 'mailJob', '{}', '0/30 * * * * ?', '发送未发送或发送失败的邮件', '0', 0, 1, '2019-08-03 11:55:30', NULL, NULL, NULL);
INSERT INTO `sys_task`(`id`, `name`, `bean`, `params`, `cron`, `note`, `state`, `is_deleted`, `creator_id`, `create_time`, `modifier_id`, `modify_time`, `remark`) VALUES (3, '文件同步任务', 'fileJob', '{}', '0/30 * * * * ?', '删除未进行物理删除的文件', '0', 0, 1, '2019-08-03 11:55:30', NULL, NULL, NULL);

-- 创建Quartz表
CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(190) NOT NULL,
JOB_GROUP VARCHAR(190) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
JOB_NAME VARCHAR(190) NOT NULL,
JOB_GROUP VARCHAR(190) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(190) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(190) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(190) NOT NULL,
TRIGGER_GROUP VARCHAR(190) NOT NULL,
INSTANCE_NAME VARCHAR(190) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(190) NULL,
JOB_GROUP VARCHAR(190) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(190) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
