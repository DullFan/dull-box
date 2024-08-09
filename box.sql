-- ----------------------------
-- 用户信息表
-- ----------------------------
drop table if exists sys_user;
CREATE TABLE `sys_user`
(
    `user_id`      bigint      NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_name`    varchar(30) NOT NULL COMMENT '用户账号',
    `nick_name`    varchar(30) NOT NULL COMMENT '用户昵称',
    `email`        varchar(50)  DEFAULT '' COMMENT '用户邮箱',
    `phone_number` varchar(11)  DEFAULT '' COMMENT '手机号码',
    `sex`          char(1)      DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`       varchar(100) DEFAULT '' COMMENT '头像地址',
    `password`     varchar(100) DEFAULT '' COMMENT '密码',
    `age`          tinyint unsigned DEFAULT '0' COMMENT '年龄',
    `introduction` varchar(500) DEFAULT NULL COMMENT '简介',
    `status`       char(1)      DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `del_flag`     char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `login_ip`     varchar(128) DEFAULT '' COMMENT '最后登录IP',
    `login_date`   datetime     DEFAULT NULL COMMENT '最后登录时间',
    `create_by`    varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uni_user_name` (`user_name`) USING BTREE COMMENT '用户名称'
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';


-- ----------------------------
-- 参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config
(
    config_id    int(5) not null auto_increment comment '参数主键',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键名',
    config_value varchar(500) default '' comment '参数键值',
    config_type  char(1)      default 'N' comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config
values (1, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启验证码功能（true开启，false关闭）');
insert into sys_config
values (2, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', sysdate(), '', null,
        '是否开启注册用户功能（true开启，false关闭）');


SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for box_file
-- ----------------------------
DROP TABLE IF EXISTS `box_file`;

CREATE TABLE `box_file`
(
    `file_id`                   bigint                                                 NOT NULL COMMENT '文件id',
    `filename`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名称',
    `real_path`                 varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件物理路径',
    `file_size`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件实际大小',
    `file_size_desc`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件大小展示字符',
    `file_suffix`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件后缀',
    `file_preview_content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件预览的响应头Content-Type的值',
    `identifier`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件唯一标识',
    `create_user`               bigint                                                 NOT NULL COMMENT '创建人',
    `create_time`               datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='物理文件信息表';

-- ----------------------------
-- Table structure for box_share
-- ----------------------------
DROP TABLE IF EXISTS `box_share`;
CREATE TABLE `box_share`
(
    `share_id`       bigint(0) NOT NULL COMMENT '分享id',
    `share_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享名称',
    `share_type`     tinyint(1) NOT NULL DEFAULT 0 COMMENT '分享类型（0 有提取码）',
    `share_day_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '分享类型（0 永久有效；1 7天有效；2 30天有效）',
    `share_day`      tinyint(1) NOT NULL DEFAULT 0 COMMENT '分享有效天数（永久有效为0）',
    `share_end_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '分享结束时间',
    `share_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享链接地址',
    `share_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享提取码',
    `share_status`   tinyint(1) NOT NULL DEFAULT 0 COMMENT '分享状态（0 正常；1 有文件被删除）',
    `create_user`    bigint(0) NOT NULL COMMENT '分享创建人',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    PRIMARY KEY (`share_id`) USING BTREE,
    UNIQUE INDEX `uk_create_user_time` (`create_user`, `create_time`) USING BTREE COMMENT '创建人、创建时间唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户分享表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for box_share_file
-- ----------------------------
DROP TABLE IF EXISTS `box_share_file`;
CREATE TABLE `box_share_file`
(
    `id`          bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `share_id`    bigint(0) NOT NULL COMMENT '分享id',
    `file_id`     bigint(0) NOT NULL COMMENT '文件记录ID',
    `create_user` bigint(0) NOT NULL COMMENT '分享创建人',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_share_id_file_id` (`share_id`, `file_id`) USING BTREE COMMENT '分享ID、文件ID联合唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户分享文件表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for box_user_file
-- ----------------------------
DROP TABLE IF EXISTS `box_user_file`;
CREATE TABLE `box_user_file`
(
    `file_id`        bigint(20) NOT NULL COMMENT '文件记录ID',
    `user_id`        bigint(20) NOT NULL COMMENT '用户ID',
    `parent_id`      bigint(20) NOT NULL COMMENT '上级文件夹ID,顶级文件夹为0',
    `real_file_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '真实文件id',
    `filename`       varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名',
    `folder_flag`    tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是文件夹 （0 否 1 是）',
    `file_size_desc` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '--' COMMENT '文件大小展示字符',
    `file_type`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv）',
    `del_flag`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识（0 否 1 是）',
    `create_user`    bigint(20) NOT NULL COMMENT '创建人',
    `create_time`    datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user`    bigint(20) NOT NULL COMMENT '更新人',
    `update_time`    datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`file_id`) USING BTREE,
    KEY              `index_file_list` (`user_id`, `del_flag`, `parent_id`, `file_type`, `file_id`, `filename`, `folder_flag`,
        `file_size_desc`, `create_time`, `update_time`) USING BTREE COMMENT '查询文件列表索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='用户文件信息表';

-- ----------------------------
-- Table structure for box_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `box_user_search_history`;
CREATE TABLE `box_user_search_history`
(
    `id`             bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`        bigint(0) NOT NULL COMMENT '用户id',
    `search_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '搜索文案',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    `update_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_id_search_content_update_time` (`user_id`, `search_content`, `update_time`) USING BTREE COMMENT '用户id、搜索内容和更新时间唯一索引',
    UNIQUE INDEX `uk_user_id_search_content` (`user_id`, `search_content`) USING BTREE COMMENT '用户id和搜索内容唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户搜索历史表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for box_file_chunk
-- ----------------------------

DROP TABLE IF EXISTS `box_file_chunk`;
CREATE TABLE `box_file_chunk`
(
    `id`              bigint                           NOT NULL AUTO_INCREMENT COMMENT '主键',
    `identifier`      varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件唯一标识',
    `real_path`       varchar(700) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分片真实的存储路径',
    `chunk_number`    int                              NOT NULL DEFAULT '0' COMMENT '分片编号',
    `expiration_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
    `create_user`     bigint                           NOT NULL COMMENT '创建人',
    `create_time`     datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_identifier_chunk_number_create_user` (`identifier`, `chunk_number`, `create_user`) USING BTREE COMMENT '文件唯一标识、分片编号和用户ID的唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 101
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='文件分片信息表';

-- ----------------------------
-- Table structure for box_error_log
-- ----------------------------

DROP TABLE IF EXISTS `box_error_log`;
CREATE TABLE `box_error_log`
(
    `id`          bigint                           NOT NULL AUTO_INCREMENT COMMENT '主键',
    `log_content` varchar(900) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日志内容',
    `log_status`  tinyint                                   DEFAULT '0' COMMENT '日志状态：0 未处理 1 已处理',
    `create_user` bigint                           NOT NULL COMMENT '创建人',
    `create_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` bigint                           NOT NULL COMMENT '更新人',
    `update_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='错误日志表';

SET
FOREIGN_KEY_CHECKS = 1;