drop table if exists cpm.business_role_relations;
create table cpm.business_role_relations
(
    role_id   bigint not null,
    parent_id bigint not null,
    constraint business_role_relations_pk primary key (role_id, parent_id),
    constraint business_role_relations_role_fk foreign key (role_id) references cpm.business_role (id),
    constraint business_role_relations_parent_fk foreign key (parent_id) references cpm.business_role (id)
);