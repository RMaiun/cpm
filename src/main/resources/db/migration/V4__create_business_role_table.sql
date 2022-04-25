drop table if exists cpm.business_role;
create table cpm.business_role
(
    id        BIGSERIAL,
    domain_id bigint  not null,
    role_type varchar not null,
    unique (domain_id, role_type),
    constraint business_role_pk primary key (id),
    constraint business_role_object_fk foreign key (domain_id) references cpm.application (id)
);