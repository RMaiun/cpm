drop table if exists cpm.user_group;
create table cpm.user_group
(
    uid      varchar not null,
    group_id bigint  not null,
    primary key (uid, group_id),
    constraint group_app_fk foreign key (group_id) references cpm.business_group (id)
);