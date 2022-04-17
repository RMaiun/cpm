drop table if exists cpm.business_role;
create table cpm.business_role
(
    id        BIGSERIAL,
    code      varchar not null,
    workspace varchar default null,
    object_id bigint  not null,
    unique (code, workspace, object_id),
    constraint business_role_pk primary key (id),
    constraint business_role_object_fk foreign key (object_id) references cpm.application (id)
);

create index business_role_code_idx on cpm.business_role (code);