



CREATE SEQUENCE repository_id_seq
START WITH 600000
INCREMENT BY 1
  NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE discussion (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  url text
);



CREATE TABLE import (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  package text
);

CREATE TABLE import_discussion (
  idd integer NOT NULL,
  idi integer NOT NULL,
  id integer DEFAULT nextval('repository_id_seq'::regclass)
);


CREATE TABLE method (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  methodname text,
  params integer
);


CREATE TABLE method_discussion (
  idm integer NOT NULL,
  idd integer NOT NULL,
  id integer DEFAULT nextval('repository_id_seq'::regclass)
);


-- ######################################################################


CREATE TABLE repository (
  id integer NOT NULL,
  url character varying(255),
  usr character varying(255),
  pwd character varying(255),
  localpath character varying(255),
  subversiontype character varying(255)
);





CREATE TABLE repository_render (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  repositoryversion integer,
  localpath text,
  metrictype text,
  repository integer
);


CREATE TABLE binary_file (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  repo_version integer,
  name text,
  size integer
);







CREATE TABLE import_file (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  idf integer,
  idi integer
);



CREATE TABLE java_class (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  java_file integer,
  name text
);





CREATE TABLE java_doc (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  java_file integer,
  name text
);



CREATE TABLE java_field (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  name text,
  java_file integer
);



CREATE TABLE java_file (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  path text,
  name text,
  repo_version integer,
  size integer,
  nline integer
);


CREATE TABLE java_interface (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  java_file integer,
  name text
);





CREATE TABLE java_method (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  java_file integer,
  name text
);



CREATE TABLE java_package (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  java_file integer,
  name text
);






CREATE TABLE method_file (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  idf integer,
  idm integer
);


CREATE TABLE repositoryversion (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  localpath character varying(255),
  repository integer
);


CREATE TABLE text_file (
  id integer DEFAULT nextval('repository_id_seq'::regclass) NOT NULL,
  repo_version integer,
  name text,
  size integer
);


