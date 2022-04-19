drop table if exists cpm.business_group;
create table cpm.business_group
(
    id        bigserial primary key,
    code      varchar not null,
    domain_id bigint  not null,
    constraint group_domain_fk foreign key (domain_id) references cpm.domain (id)
);