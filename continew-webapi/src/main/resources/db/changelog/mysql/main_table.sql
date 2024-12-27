-- liquibase formatted sql

-- changeset charles7c:1
-- comment 初始化表结构
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `title`       varchar(30)  NOT NULL                    COMMENT '标题',
    `parent_id`   bigint(20)   NOT NULL DEFAULT 0          COMMENT '上级菜单ID',
    `type`        tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '类型（1：目录；2：菜单；3：按钮）',
    `path`        varchar(255) DEFAULT NULL                COMMENT '路由地址',
    `name`        varchar(50)  DEFAULT NULL                COMMENT '组件名称',
    `component`   varchar(255) DEFAULT NULL                COMMENT '组件路径',
    `redirect`    varchar(255) DEFAULT NULL                COMMENT '重定向地址',
    `icon`        varchar(50)  DEFAULT NULL                COMMENT '图标',
    `is_external` bit(1)       DEFAULT b'0'                COMMENT '是否外链',
    `is_cache`    bit(1)       DEFAULT b'0'                COMMENT '是否缓存',
    `is_hidden`   bit(1)       DEFAULT b'0'                COMMENT '是否隐藏',
    `permission`  varchar(100) DEFAULT NULL                COMMENT '权限标识',
    `sort`        int          NOT NULL DEFAULT 999        COMMENT '排序',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：启用；2：禁用）',
    `create_user` bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time` datetime     NOT NULL                    COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_title_parent_id`(`title`, `parent_id`),
    INDEX `idx_parent_id`(`parent_id`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `sys_dept` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `name`        varchar(30)  NOT NULL                    COMMENT '名称',
    `parent_id`   bigint(20)   NOT NULL DEFAULT 0          COMMENT '上级部门ID',
    `ancestors`   varchar(512) NOT NULL DEFAULT ''         COMMENT '祖级列表',
    `description` varchar(200) DEFAULT NULL                COMMENT '描述',
    `sort`        int          NOT NULL DEFAULT 999        COMMENT '排序',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：启用；2：禁用）',
    `is_system`   bit(1)       NOT NULL DEFAULT b'0'       COMMENT '是否为系统内置数据',
    `create_user` bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time` datetime     NOT NULL                    COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_name_parent_id`(`name`, `parent_id`),
    INDEX `idx_parent_id`(`parent_id`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE IF NOT EXISTS `sys_role` (
    `id`                  bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`                varchar(30)  NOT NULL                COMMENT '名称',
    `code`                varchar(30)  NOT NULL                COMMENT '编码',
    `data_scope`          tinyint(1)   NOT NULL DEFAULT 4      COMMENT '数据权限（1：全部数据权限；2：本部门及以下数据权限；3：本部门数据权限；4：仅本人数据权限；5：自定义数据权限）',
    `description`         varchar(200) DEFAULT NULL            COMMENT '描述',
    `sort`                int          NOT NULL DEFAULT 999    COMMENT '排序',
    `is_system`           bit(1)       NOT NULL DEFAULT b'0'   COMMENT '是否为系统内置数据',
    `menu_check_strictly` bit(1)       DEFAULT b'0'            COMMENT '菜单选择是否父子节点关联',
    `dept_check_strictly` bit(1)       DEFAULT b'0'            COMMENT '部门选择是否父子节点关联',
    `create_user`         bigint(20)   NOT NULL                COMMENT '创建人',
    `create_time`         datetime     NOT NULL                COMMENT '创建时间',
    `update_user`         bigint(20)   DEFAULT NULL            COMMENT '修改人',
    `update_time`         datetime     DEFAULT NULL            COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_name`(`name`),
    UNIQUE INDEX `uk_code`(`code`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `username`       varchar(64)  NOT NULL                    COMMENT '用户名',
    `nickname`       varchar(30)  NOT NULL                    COMMENT '昵称',
    `password`       varchar(255) DEFAULT NULL                COMMENT '密码',
    `gender`         tinyint(1)   UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别（0：未知；1：男；2：女）',
    `email`          varchar(255) DEFAULT NULL                COMMENT '邮箱',
    `phone`          varchar(255) DEFAULT NULL                COMMENT '手机号码',
    `avatar`         longtext     DEFAULT NULL                COMMENT '头像',
    `description`    varchar(200) DEFAULT NULL                COMMENT '描述',
    `status`         tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：启用；2：禁用）',
    `is_system`      bit(1)       NOT NULL DEFAULT b'0'       COMMENT '是否为系统内置数据',
    `pwd_reset_time` datetime     DEFAULT NULL                COMMENT '最后一次修改密码时间',
    `dept_id`        bigint(20)   NOT NULL                    COMMENT '部门ID',
    `create_user`    bigint(20)   DEFAULT NULL                COMMENT '创建人',
    `create_time`    datetime     NOT NULL                    COMMENT '创建时间',
    `update_user`    bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time`    datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_username`(`username`),
    UNIQUE INDEX `uk_email`(`email`),
    UNIQUE INDEX `uk_phone`(`phone`),
    INDEX `idx_dept_id`(`dept_id`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `sys_user_password_history` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint(20)   NOT NULL                COMMENT '用户ID',
    `password`    varchar(255) NOT NULL                COMMENT '密码',
    `create_time` datetime     NOT NULL                COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户历史密码表';

CREATE TABLE IF NOT EXISTS `sys_user_social` (
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `source`          varchar(255) NOT NULL                COMMENT '来源',
    `open_id`         varchar(255) NOT NULL                COMMENT '开放ID',
    `user_id`         bigint(20)   NOT NULL                COMMENT '用户ID',
    `meta_json`       text         DEFAULT NULL            COMMENT '附加信息',
    `last_login_time` datetime     DEFAULT NULL            COMMENT '最后登录时间',
    `create_time`     datetime     NOT NULL                COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_source_open_id`(`source`, `open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户社会化关联表';

CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

CREATE TABLE IF NOT EXISTS `sys_role_dept` (
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

CREATE TABLE IF NOT EXISTS `sys_option` (
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `category`      varchar(50)  NOT NULL                COMMENT '类别',
    `name`          varchar(50)  NOT NULL                COMMENT '名称',
    `code`          varchar(100) NOT NULL                COMMENT '键',
    `value`         longtext     DEFAULT NULL            COMMENT '值',
    `default_value` longtext     DEFAULT NULL            COMMENT '默认值',
    `description`   varchar(200) DEFAULT NULL            COMMENT '描述',
    `update_user`   bigint(20)   DEFAULT NULL            COMMENT '修改人',
    `update_time`   datetime     DEFAULT NULL            COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_category_code`(`category`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数表';

CREATE TABLE IF NOT EXISTS `sys_dict` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(30)  NOT NULL                COMMENT '名称',
    `code`        varchar(30)  NOT NULL                COMMENT '编码',
    `description` varchar(200) DEFAULT NULL            COMMENT '描述',
    `is_system`   bit(1)       NOT NULL DEFAULT b'0'   COMMENT '是否为系统内置数据',
    `create_user` bigint(20)   NOT NULL                COMMENT '创建人',
    `create_time` datetime     NOT NULL                COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL            COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL            COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_name`(`name`),
    UNIQUE INDEX `uk_code`(`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

CREATE TABLE IF NOT EXISTS `sys_dict_item` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `label`       varchar(30)  NOT NULL                    COMMENT '标签',
    `value`       varchar(30)  NOT NULL                    COMMENT '值',
    `color`       varchar(30)  DEFAULT NULL                COMMENT '标签颜色',
    `sort`        int          NOT NULL DEFAULT 999        COMMENT '排序',
    `description` varchar(200) DEFAULT NULL                COMMENT '描述',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：启用；2：禁用）',
    `dict_id`     bigint(20)   NOT NULL                    COMMENT '字典ID',
    `create_user` bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time` datetime     NOT NULL                    COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_value_dict_id`(`value`, `dict_id`),
    INDEX `idx_dict_id`(`dict_id`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项表';

CREATE TABLE IF NOT EXISTS `sys_log` (
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `trace_id`         varchar(255) DEFAULT NULL                COMMENT '链路ID',
    `description`      varchar(255) NOT NULL                    COMMENT '日志描述',
    `module`           varchar(50)  NOT NULL                    COMMENT '所属模块',
    `request_url`      varchar(512) NOT NULL                    COMMENT '请求URL',
    `request_method`   varchar(10)  NOT NULL                    COMMENT '请求方式',
    `request_headers`  text         DEFAULT NULL                COMMENT '请求头',
    `request_body`     text         DEFAULT NULL                COMMENT '请求体',
    `status_code`      int          NOT NULL                    COMMENT '状态码',
    `response_headers` text         DEFAULT NULL                COMMENT '响应头',
    `response_body`    mediumtext   DEFAULT NULL                COMMENT '响应体',
    `time_taken`       bigint(20)   NOT NULL                    COMMENT '耗时（ms）',
    `ip`               varchar(100) DEFAULT NULL                COMMENT 'IP',
    `address`          varchar(255) DEFAULT NULL                COMMENT 'IP归属地',
    `browser`          varchar(100) DEFAULT NULL                COMMENT '浏览器',
    `os`               varchar(100) DEFAULT NULL                COMMENT '操作系统',
    `status`           tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：成功；2：失败）',
    `error_msg`        text         DEFAULT NULL                COMMENT '错误信息',
    `create_user`      bigint(20)   DEFAULT NULL                COMMENT '创建人',
    `create_time`      datetime     NOT NULL                    COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_module`(`module`),
    INDEX `idx_ip`(`ip`),
    INDEX `idx_address`(`address`),
    INDEX `idx_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

CREATE TABLE IF NOT EXISTS `sys_message` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `title`       varchar(50)  NOT NULL                    COMMENT '标题',
    `content`     varchar(255) DEFAULT NULL                COMMENT '内容',
    `type`        tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '类型（1：系统消息）',
    `create_user` bigint(20)   DEFAULT NULL                COMMENT '创建人',
    `create_time` datetime     NOT NULL                    COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

CREATE TABLE IF NOT EXISTS `sys_message_user` (
    `message_id` bigint(20) NOT NULL              COMMENT '消息ID',
    `user_id`    bigint(11) NOT NULL              COMMENT '用户ID',
    `is_read`    bit(1)     NOT NULL DEFAULT b'0' COMMENT '是否已读',
    `read_time`  datetime   DEFAULT NULL          COMMENT '读取时间',
    PRIMARY KEY (`message_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息和用户关联表';

CREATE TABLE IF NOT EXISTS `sys_notice` (
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title`          varchar(150) NOT NULL                COMMENT '标题',
    `content`        mediumtext   NOT NULL                COMMENT '内容',
    `type`           varchar(30)  NOT NULL                COMMENT '类型',
    `effective_time` datetime     DEFAULT NULL            COMMENT '生效时间',
    `terminate_time` datetime     DEFAULT NULL            COMMENT '终止时间',
    `notice_scope`   int          NOT NULL                COMMENT '通知范围',
    `notice_users`   json         DEFAULT NULL            COMMENT '通知用户',
    `sort`           int          NOT NULL DEFAULT 999    COMMENT '排序',
    `create_user`    bigint(20)   NOT NULL                COMMENT '创建人',
    `create_time`    datetime     NOT NULL                COMMENT '创建时间',
    `update_user`    bigint(20)   DEFAULT NULL            COMMENT '修改人',
    `update_time`    datetime     DEFAULT NULL            COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

CREATE TABLE IF NOT EXISTS `sys_storage` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `name`        varchar(100) NOT NULL                    COMMENT '名称',
    `code`        varchar(30)  NOT NULL                    COMMENT '编码',
    `type`        tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '类型（1：兼容S3协议存储；2：本地存储）',
    `access_key`  varchar(255) DEFAULT NULL                COMMENT 'Access Key（访问密钥）',
    `secret_key`  varchar(255) DEFAULT NULL                COMMENT 'Secret Key（私有密钥）',
    `endpoint`    varchar(255) DEFAULT NULL                COMMENT 'Endpoint（终端节点）',
    `bucket_name` varchar(255) DEFAULT NULL                COMMENT '桶名称',
    `domain`      varchar(255) NOT NULL DEFAULT ''         COMMENT '域名',
    `description` varchar(200) DEFAULT NULL                COMMENT '描述',
    `is_default`  bit(1)       NOT NULL DEFAULT b'0'       COMMENT '是否为默认存储',
    `sort`        int          NOT NULL DEFAULT 999        COMMENT '排序',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态（1：启用；2：禁用）',
    `create_user` bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time` datetime     NOT NULL                    COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_code`(`code`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储表';

CREATE TABLE IF NOT EXISTS `sys_file` (
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `name`           varchar(255) NOT NULL                    COMMENT '名称',
    `size`           bigint(20)   NOT NULL                    COMMENT '大小（字节）',
    `url`            varchar(512) NOT NULL                    COMMENT 'URL',
    `extension`      varchar(100) DEFAULT NULL                COMMENT '扩展名',
    `thumbnail_size` bigint(20)   DEFAULT NULL                COMMENT '缩略图大小（字节)',
    `thumbnail_url`  varchar(512) DEFAULT NULL                COMMENT '缩略图URL',
    `type`           tinyint(1)   UNSIGNED NOT NULL DEFAULT 1 COMMENT '类型（1：其他；2：图片；3：文档；4：视频；5：音频）',
    `storage_id`     bigint(20)   NOT NULL                    COMMENT '存储ID',
    `create_user`    bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time`    datetime     NOT NULL                    COMMENT '创建时间',
    `update_user`    bigint(20)   NOT NULL                    COMMENT '修改人',
    `update_time`    datetime     NOT NULL                    COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX `idx_url`(`url`),
    INDEX `idx_type`(`type`),
    INDEX `idx_create_user`(`create_user`),
    INDEX `idx_update_user`(`update_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

CREATE TABLE IF NOT EXISTS `sys_client` (
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT     COMMENT 'ID',
    `client_id`      varchar(50)  NOT NULL                    COMMENT '客户端ID',
    `client_key`     varchar(255) NOT NULL                    COMMENT '客户端Key',
    `client_secret`  varchar(255) NOT NULL                    COMMENT '客户端秘钥',
    `auth_type`      json         DEFAULT NULL                COMMENT '授权类型',
    `client_type`    varchar(50)  DEFAULT NULL                COMMENT '客户端类型',
    `active_timeout` int          DEFAULT '-1'                COMMENT 'Token最低活跃频率（-1为不限制）',
    `timeout`        int          DEFAULT '2592000'           COMMENT 'Token有效期（默认30天，单位：秒）',
    `status`         tinyint(1)   UNSIGNED NOT NULL           COMMENT '状态（1：启用；2：禁用）',
    `create_user`    bigint(20)   NOT NULL                    COMMENT '创建人',
    `create_time`    datetime     NOT NULL                    COMMENT '创建时间',
    `update_user`    bigint(20)   DEFAULT NULL                COMMENT '修改人',
    `update_time`    datetime     DEFAULT NULL                COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户端管理';
