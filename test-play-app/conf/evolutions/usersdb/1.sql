# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table users (
  user_id                   varchar(255) not null,
  email                     varchar(255),
  temp                      varchar(255),
  constraint pk_users primary key (user_id))
;

create sequence users_seq;




# --- !Downs

drop table if exists users cascade;

drop sequence if exists users_seq;

