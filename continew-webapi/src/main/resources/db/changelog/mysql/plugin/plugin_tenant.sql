-- liquibase formatted sql

-- changeset 小熊:1
-- comment 初始化表结构
-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
`id` bigint NOT NULL COMMENT 'ID',
`name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户名称',
`user_id` bigint DEFAULT NULL COMMENT '租户对应的用户',
`domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '绑定的域名',
`package_id` bigint NOT NULL COMMENT '租户套餐编号',
`status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态（1：启用；2：禁用）',
`expire_time` datetime DEFAULT NULL COMMENT '租户过期时间',
`create_user` bigint NOT NULL COMMENT '创建人',
`create_time` datetime NOT NULL COMMENT '创建时间',
`update_user` bigint DEFAULT NULL COMMENT '修改人',
`update_time` datetime DEFAULT NULL COMMENT '修改时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户表';

-- ----------------------------
-- Table structure for sys_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package` (
 `id` bigint NOT NULL COMMENT 'ID',
 `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐名称',
 `menu_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的菜单ids',
 `menu_check_strictly` bit(1) DEFAULT b'0' COMMENT '菜单选择是否父子节点关联',
 `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态（1：启用；2：禁用）',
 `create_user` bigint NOT NULL COMMENT '创建人',
 `create_time` datetime NOT NULL COMMENT '创建时间',
 `update_user` bigint DEFAULT NULL COMMENT '修改人',
 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户套餐表';

-- changeset 小熊:2
-- comment 添加租户列和索引
ALTER TABLE sys_app ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_dept ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_file ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_log ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_menu ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_message ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_message_user ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_notice ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_role ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE sys_user ADD COLUMN tenant_id BIGINT NOT NULL DEFAULT 0;

-- changeset 小熊:3
-- comment 唯一索引变更
ALTER TABLE sys_app DROP INDEX `uk_access_key`;
ALTER TABLE sys_app ADD UNIQUE INDEX `uk_access_key` (`access_key`, `tenant_id`);
ALTER TABLE sys_dept DROP INDEX `uk_name_parent_id`;
ALTER TABLE sys_dept ADD UNIQUE INDEX `uk_name_parent_id` (`name`, `parent_id`, `tenant_id`);
ALTER TABLE sys_menu DROP INDEX `uk_title_parent_id`;
ALTER TABLE sys_menu ADD UNIQUE INDEX `uk_title_parent_id` (`title`, `parent_id`, `tenant_id`);
ALTER TABLE sys_role DROP INDEX `uk_name`;
ALTER TABLE sys_role ADD UNIQUE INDEX `uk_name` (`name`, `tenant_id`);
ALTER TABLE sys_role DROP INDEX `uk_code`;
ALTER TABLE sys_role ADD UNIQUE INDEX `uk_code` (`code`, `tenant_id`);
ALTER TABLE sys_user DROP INDEX `uk_username`;
ALTER TABLE sys_user ADD UNIQUE INDEX `uk_username` (`username`, `tenant_id`);
ALTER TABLE sys_user DROP INDEX `uk_email`;
ALTER TABLE sys_user ADD UNIQUE INDEX `uk_email` (`email`, `tenant_id`);
ALTER TABLE sys_user DROP INDEX `uk_phone`;
ALTER TABLE sys_user ADD UNIQUE INDEX `uk_phone` (`phone`, `tenant_id`);

-- changeset 小熊:4
-- comment 菜单录入
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10015, '租户套餐管理', 7000, 2, '/open/tenantPackage', 'TenantPackage', 'open/tenantPackage/index', NULL, 'storage', b'0', b'0', b'0', NULL, 1, 1, 1, now(), 1, now(), 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10016, '列表', 10015, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenantPackage:list', 1, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10017, '详情', 10015, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenantPackage:detail', 2, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10018, '新增', 10015, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenantPackage:add', 3, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10019, '修改', 10015, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenantPackage:update', 4, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10020, '删除', 10015, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenantPackage:delete', 5, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10022, '租户管理', 7000, 2, '/open/tenant', 'Tenant', 'open/tenant/index', NULL, 'user-group', b'0', b'0', b'0', NULL, 1, 1, 1, now(), 1, now(), 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10023, '列表', 10022, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenant:list', 1, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10024, '详情', 10022, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenant:detail', 2, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10025, '新增', 10022, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenant:add', 3, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10026, '修改', 10022, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenant:update', 4, 1, 1, now(), NULL, NULL, 0);
INSERT INTO `continew_admin`.`sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `tenant_id`) VALUES (10027, '删除', 10022, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'tenant:tenant:delete', 5, 1, 1, now(), NULL, NULL, 0);