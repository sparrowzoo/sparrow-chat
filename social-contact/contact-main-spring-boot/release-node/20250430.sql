alter table t_session
add sender_name varchar(64) default '' not null after user_id,
add sender_nick_name varchar(64) default '' not null after sender_name,
add receiver_name varchar(64) default '' not null after sender_nick_name,
add receiver_nick_name varchar(64) default '' not null after receiver_name,
add group_name varchar(64) default '' not null after receiver_nick_name;

