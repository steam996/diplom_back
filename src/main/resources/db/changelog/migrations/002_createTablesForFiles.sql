--liquibase formatted sql

--changeset Andros:2

create table files(
                      id bigserial,
                      name varchar(50) not null,
                      date_of_change date,
                      size bigint,
                      user_id bigint,
                      primary key (id),
                      foreign key (user_id) references users(id)
);


-- rollback ;


