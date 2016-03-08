# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table REPO_INFO (
  id                        bigint auto_increment not null,
  parent                    varchar(255),
  file_name                 varchar(255),
  nom                       integer,
  size                      integer,
  wc                        integer,
  repository_id             bigint,
  constraint pk_REPO_INFO primary key (id))
;

create table REPOSITORY (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  uri                       varchar(255),
  pwd                       varchar(255),
  usr_id                    bigint,
  type                      varchar(255),
  constraint pk_REPOSITORY primary key (id))
;

create table REPOSITORY_VERSION (
  id                        bigint auto_increment not null,
  hss                       varchar(255),
  repository_id             bigint,
  constraint pk_REPOSITORY_VERSION primary key (id))
;

create table USER (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint uq_USER_name unique (name),
  constraint pk_USER primary key (id))
;

alter table REPO_INFO add constraint fk_REPO_INFO_repository_1 foreign key (repository_id) references REPOSITORY_VERSION (id) on delete restrict on update restrict;
create index ix_REPO_INFO_repository_1 on REPO_INFO (repository_id);
alter table REPOSITORY add constraint fk_REPOSITORY_usr_2 foreign key (usr_id) references USER (id) on delete restrict on update restrict;
create index ix_REPOSITORY_usr_2 on REPOSITORY (usr_id);
alter table REPOSITORY_VERSION add constraint fk_REPOSITORY_VERSION_repository_3 foreign key (repository_id) references REPOSITORY (id) on delete restrict on update restrict;
create index ix_REPOSITORY_VERSION_repository_3 on REPOSITORY_VERSION (repository_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table REPO_INFO;

drop table REPOSITORY;

drop table REPOSITORY_VERSION;

drop table USER;

SET FOREIGN_KEY_CHECKS=1;

