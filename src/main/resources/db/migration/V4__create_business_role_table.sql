drop table if exists cpm.business_role;
create table cpm.business_role
(
    id        BIGSERIAL,
    code      varchar not null,
--     divisible bool default false,
--     workspace varchar default null,
    domain_id bigint  not null,
--     unique (code, workspace, domain_id),
    unique (code, domain_id),
    constraint business_role_pk primary key (id),
    constraint business_role_object_fk foreign key (domain_id) references cpm.application (id)
);

create index business_role_code_idx on cpm.business_role (code);