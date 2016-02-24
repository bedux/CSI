# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table REPO (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  uri                       varchar(255),
  pwd                       varchar(255),
  usr_id                    bigint,
  type                      varchar(255),
  constraint pk_REPO primary key (id))
;

create table REPO_VERSION (
  id                        bigint auto_increment not null,
  object_id                 varchar(255),
  constraint pk_REPO_VERSION primary key (id))
;

create table USER (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint uq_USER_name unique (name),
  constraint pk_USER primary key (id))
;

alter table REPO add constraint fk_REPO_usr_1 foreign key (usr_id) references USER (id) on delete restrict on update restrict;
create index ix_REPO_usr_1 on REPO (usr_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table REPO;

drop table REPO_VERSION;

drop table USER;

SET FOREIGN_KEY_CHECKS=1;

