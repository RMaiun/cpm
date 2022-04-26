drop table if exists cpm.domain_to_domain;
create table cpm.domain_to_domain
(
    id        bigserial primary key,
    domain_id bigint not null,
    parent_id bigint default null,
    unique (domain_id, parent_id),
    constraint group_relation_self_fk foreign key (domain_id) references cpm.business_group (id),
    constraint group_relation_parent_fk foreign key (parent_id) references cpm.business_group (id)
);