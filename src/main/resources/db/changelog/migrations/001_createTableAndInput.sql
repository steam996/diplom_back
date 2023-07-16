--liquibase formatted sql

--changeset Andros:1
CREATE table users (
                       id bigserial,
                       login varchar(50) not null unique ,
                       password varchar(255) not null ,
                       primary key (id)
);

create table roles(
                      id serial,
                      name varchar(50) not null ,
                      primary key (id)
);

create table users_roles(
                            user_id bigint not null ,
                            role_id int not null,
                            primary key (user_id, role_id),
                            foreign key (user_id) references users(id),
                            foreign key (role_id) references roles(id)
);

insert into roles(name) values ('ROLE_USER'), ('ROLE_ADMIN');

insert into users(login, password)
VALUES ('user', '$2a$12$at6dZEqzMTjaTtPmUprdbekj9xs04u/Yp9TjTKDurdF8HN/Q7jIYq'),
       ('admin', '$2a$12$at6dZEqzMTjaTtPmUprdbekj9xs04u/Yp9TjTKDurdF8HN/Q7jIYq');

insert into users_roles(user_id, role_id) VALUES (1, 1), (2,2);
-- rollback ;


