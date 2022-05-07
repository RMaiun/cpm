drop table if exists cpm.domain_to_domain;
create table cpm.domain_to_domain
(
    id        bigserial primary key,
    domain_id bigint not null,
    parent_id bigint default null,
    unique (domain_id, parent_id),
    constraint domain_to_domain_self_fk foreign key (domain_id) references cpm.domain (id),
    constraint domain_to_domain_parent_fk foreign key (parent_id) references cpm.domain (id)
);