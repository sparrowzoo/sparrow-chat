alter table t_user
add category int default 0 not null comment '用户类别' after user_id,
add english_name varchar(64) default '' not null after category;
