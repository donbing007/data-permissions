-- database name '`xplat_data_permissions`'

create table field_scope(
    id      bigint auto_increment primary key,
    entity  varchar(255)             not null comment '实体对象描述',
    `field` varchar(255) default '*' not null comment '字段描述,一般是字段名称.默认为"*",表示所有范围.'
); -- ' 字段范围.'

create table data_scope
(
    id     bigint auto_increment primary key,
    entity varchar(255) not null comment '实体描述信息.'
); -- ' 数据范围.'

create table data_scope_conditions
(
    id            bigint auto_increment primary key,
    data_scope_id bigint       not null comment '数据权限定义标识.',
    `field`       varchar(255) not null comment '字段描述信息,一般为字段名称.'
); -- ' 数据范围条件.'

create table data_scope_sub_condition
(
    id            bigint auto_increment primary key,
    conditions_id bigint             not null comment '主条件标识.',
    value_type_id bigint             not null comment '值的数据类型.',
    `field`       varchar(255) not null comment '字段描述信息,一般为字段名称.',
    operation      varchar(255)       not null comment '条件操作符',
    `index`       smallint default 0 not null comment '当前条件在主条件中处于的位置,从0开始.',
    value         varchar(255)       not null comment '操作目标数据.',
    link          tinyint  default 0 not null comment '和上一子条件的连接方式,0表示 and,1表示 or.'
); -- '数据范围子条件.其一定属于某个 conditions.'


create table data_scope_value_type
(
    id    bigint auto_increment primary key,
    name  varchar(255)                not null comment '数据类型名称.',
    descr varchar(255) default 'Null' null
); -- '数据范围条件中的操作值数据类型字典.'

create table role
(
    id               bigint auto_increment primary key,
    role_external_id varchar(255) not null comment '角色外部定义标识.',
    tenant_id        varchar(255) not null comment '角色属于的租户标识.'
); -- '角色信息.'

create table role_permissions
(
    id         bigint auto_increment primary key,
    role_id    bigint            not null comment '角色标识',
    scope_id   bigint  default 0 comment '根据 type 不同指向不同的权限范围 column 或者 data.',
    scope_type tinyint default 0 not null comment '范围类型.是字段,还是数据范围.'
); -- '角色权限定义.'

insert into data_scope_value_type (id, `name`, descr)
values (1, 'INTEGER', '数值整形'),
       (2, 'STRING', '字符串'),
       (3, 'FLOAT', '数值浮点'),
       (4, 'DATATIME', '日期时间');