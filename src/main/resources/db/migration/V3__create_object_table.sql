drop table if exists cpm.object;
create table cpm.object
(
    id     BIGSERIAL,
    code   varchar not null,
    app_id bigint  not null,
    constraint object_pk primary key (code, app_id),
    constraint object_app_fk foreign key (app_id) references cpm.application (id)
);

create index object_code_idx on cpm.object (code);