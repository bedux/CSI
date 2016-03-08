# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ComponentInfo (
  id                        bigint auto_increment not null,
  parent                    varchar(255),
  file_name                 varchar(255),
  nom                       integer,
  size                      integer,
  wc                        integer,
  repository_id             bigint,
  constraint pk_ComponentInfo primary key (id))
;

create table Repository (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  uri                       varchar(255),
  pwd                       varchar(255),
  usr_id                    bigint,
  type                      varchar(255),
  constraint pk_Repository primary key (id))
;

create table RepositoryVersion (
  id                        bigint auto_increment not null,
  hss                       varchar(255),
  num_of_file               integer,
  mama                      varchar(255),
  json                      varchar(255),
  repository_id             bigint,
  constraint pk_RepositoryVersion primary key (id))
;

create table User (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint uq_User_name unique (name),
  constraint pk_User primary key (id))
;

alter table ComponentInfo add constraint fk_ComponentInfo_repository_1 foreign key (repository_id) references RepositoryVersion (id) on delete restrict on update restrict;
create index ix_ComponentInfo_repository_1 on ComponentInfo (repository_id);
alter table Repository add constraint fk_Repository_usr_2 foreign key (usr_id) references User (id) on delete restrict on update restrict;
create index ix_Repository_usr_2 on Repository (usr_id);
alter table RepositoryVersion add constraint fk_RepositoryVersion_repository_3 foreign key (repository_id) references Repository (id) on delete restrict on update restrict;
create index ix_RepositoryVersion_repository_3 on RepositoryVersion (repository_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table ComponentInfo;

drop table Repository;

drop table RepositoryVersion;

drop table User;

SET FOREIGN_KEY_CHECKS=1;

