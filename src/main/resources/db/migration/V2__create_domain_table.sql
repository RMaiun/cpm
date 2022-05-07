drop table if exists cpm.domain;
create table cpm.domain
(
    id     BIGSERIAL primary key,
    code   varchar not null,
    app_id bigint  not null,
    constraint domain_app_fk foreign key (app_id) references cpm.application (id)
);

create index domain_code_idx on cpm.domain (code);