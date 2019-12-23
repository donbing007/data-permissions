-- database name '`xplat_data_permissions`'

create table if not exists data_scope
(
  id     bigint auto_increment primary key,
  entity varchar(255) not null comment '实体描述信息.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists data_scope_conditions
(
  id            bigint auto_increment primary key,
  data_scope_id bigint       not null comment '数据权限定义标识.',
  field         varchar(255) not null comment '字段描述信息,一般为字段名称.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists data_scope_sub_condition
(
  id            bigint auto_increment primary key,
  conditions_id bigint                not null comment '主条件标识.',
  value_type bigint                not null comment '值的数据类型.',
  entity        varchar(255)          not null comment '实体信息,冗余.',
  field         varchar(255)          not null comment '字段描述信息,一般为字段名称.冗余.',
  operation     varchar(255)          not null comment '条件操作符',
  `index`       smallint(6) default 0 not null comment '当前条件在主条件中处于的位置,从0开始.',
  `value`         varchar(255)          not null comment '操作目标数据.',
  link          tinyint     default 0 not null comment '和上一子条件的连接方式,0表示 and,1表示 or.',
  role          varchar(255)          not null comment '角色,冗余.',
  tenant        varchar(255)          not null comment '租户,冗余.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists field_scope
(
  id     bigint auto_increment primary key,
  role   varchar(255)             not null comment '权限属于的角色.冗余.',
  tenant varchar(255)             not null comment '权限属于的租户.冗余.',
  entity varchar(255)             not null comment '实体对象描述',
  field  varchar(255) default '*' not null comment '字段描述,一般是字段名称.默认为"*",表示所有范围.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists role
(
  id               bigint auto_increment primary key,
  role_external_id varchar(255) not null comment '角色外部定义标识.',
  tenant_id        varchar(255) not null comment '角色属于的租户标识.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists role_permissions
(
  id         bigint auto_increment primary key,
  role_id    bigint            not null comment '角色标识',
  scope_id   bigint  default 0 null comment '根据 type 不同指向不同的权限范围 column 或者 data.',
  scope_type tinyint default 0 not null comment '范围类型.是字段,还是数据范围.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index data_scope_sub_condition_entity_role_tenant_index on xplat_data_permissions.data_scope_sub_condition (entity, role, tenant);
create index field_scope_entity_role_tenant_index on xplat_data_permissions.field_scope (entity, role, tenant);