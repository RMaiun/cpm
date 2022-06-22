drop table if exists cpm.group_role;
create table cpm.group_role
(
    group_id bigint not null,
    br_id    bigint not null,
    primary key (group_id, br_id),
    constraint group_role_group_fk foreign key (group_id) references cpm.business_group (id),
    constraint group_role_br_fk foreign key (br_id) references cpm.business_role (id)
);