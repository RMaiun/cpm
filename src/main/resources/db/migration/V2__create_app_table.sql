drop table if exists cpm.application;
create table cpm.application
(
    id   BIGSERIAL primary key,
    name varchar not null
);

create index app_name_idx on cpm.application (name);