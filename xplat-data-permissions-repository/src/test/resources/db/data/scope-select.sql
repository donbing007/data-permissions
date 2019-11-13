insert into role(id, role_external_id, tenant_id) values (1, 'r1', 't1');

-- 两个 column 权限
insert into role_permissions(id, role_id, scope_id, scope_type) values (1, 1, 1, 0);
insert into role_permissions(id, role_id, scope_id, scope_type) values (2, 1, 2, 0);
insert into field_scope(id, entity, field) values (1, 't1', 'c1');
insert into field_scope(id, entity, field) values (2, 't1', 'c2');
insert into field_scope(id, entity, field) values (3, 't2', 'c2');

-- 两个 data 权限
insert into role_permissions(id, role_id, scope_id, scope_type) values (3, 1, 1, 1);
insert into role_permissions(id, role_id, scope_id, scope_type) values (4, 1, 2, 1);
insert into data_scope(id, entity) values (1, 't1');
insert into data_scope_conditions(id, data_scope_id, field) values (1,1,'c1');
insert into data_scope_sub_condition(id, conditions_id, field, value_type_id, operation, `index`, `value`,link) values (1,1,'c1',0,'>',0,'100',0);
insert into data_scope_sub_condition(id, conditions_id, field, value_type_id, operation, `index`, `value`,link) values (2,1,'c1',0,'=',0,'100',0);

insert into data_scope(id, entity) values (2, 't2');
insert into data_scope_conditions(id, data_scope_id, field) values (2,2,'c1');
insert into data_scope_sub_condition(id, conditions_id, field, value_type_id, operation, `index`, `value`,link) values (3,2,'c1',0,'<=',0,'100',0);