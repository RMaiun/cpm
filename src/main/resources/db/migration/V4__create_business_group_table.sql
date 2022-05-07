drop table if exists cpm.business_group;
create table cpm.business_group
(
    id        bigserial primary key,
    code      varchar not null,
    app_id bigint  not null,
    constraint group_domain_fk foreign key (app_id) references cpm.application (id)
);