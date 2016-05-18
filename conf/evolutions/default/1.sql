CREATE SEQUENCE id_counter;


DROP TABLE IF EXISTS Repository CASCADE;
CREATE  TABLE  IF NOT EXISTS Repository(
  id SERIAL PRIMARY KEY UNIQUE,
  url varchar(255),
  usr varchar(255),
  pwd varchar(255),
  localPath varchar(255),
  subversiontype varchar(255)
);


DROP TABLE IF EXISTS RepositoryVersion CASCADE;
CREATE  TABLE  IF NOT EXISTS RepositoryVersion(
  id SERIAL PRIMARY KEY UNIQUE,
  localPath varchar(255),
  repositoryId Integer REFERENCES Repository(id)

);


CREATE TABLE "public"."RepositoryRender" (
  "id" serial,
  "repositoryVersion" Integer,
  "localPath" text,
  PRIMARY KEY ("id"),
  CONSTRAINT "repositoryVersion" FOREIGN KEY ("repositoryVersion") REFERENCES "public"."repositoryversion"("id")
);




DROP TABLE IF EXISTS ContainsTransverseInformation CASCADE;
CREATE TABLE IF NOT EXISTS ContainsTransverseInformation(
  id  SERIAL PRIMARY KEY UNIQUE
);



DROP TABLE IF EXISTS TransverseInformation CASCADE;
CREATE TABLE IF NOT EXISTS TransverseInformation(
  id  SERIAL PRIMARY KEY UNIQUE,
  containsTransverseInformation Integer REFERENCES ContainsTransverseInformation(id)
);


DROP VIEW methodCountView;
CREATE VIEW methodCountView AS
  select methodName AS methodName,count(*) AS number,row_number() OVER() AS id
  from method
    join method_discussion
      on method.id = method_discussion.idm
  group by methodName;


DROP VIEW importCountView;
CREATE VIEW importCountView AS
  select package AS package,count(*) AS number,row_number() OVER() AS id
  from import
    join import_discussion
      on import.id = import_discussion.idi
  group by package;


DROP TABLE IF EXISTS File CASCADE;
CREATE  TABLE  IF NOT EXISTS File(
  id SERIAL PRIMARY KEY UNIQUE,
  path varchar(255),
  name varchar(255),
  size INTEGER,
  repositoryVersionId Integer REFERENCES RepositoryVersion(id)
);




DROP TABLE IF EXISTS BinaryFile CASCADE;
CREATE  TABLE  IF NOT EXISTS BinaryFile(
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS TextFile CASCADE;
CREATE  TABLE  IF NOT EXISTS TextFile(
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS JavaFile CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaFile(
  information JSONB
)INHERITS(File);


DROP TABLE IF EXISTS JavaSourceObject CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSourceObject(
  javaFile Integer REFERENCES File(id)

)INHERITS(ContainsTransverseInformation);

DROP TABLE IF EXISTS JavaPackage CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaPackage(
  information JSONB
)INHERITS(JavaSourceObject);


DROP TABLE IF EXISTS JavaClass CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaClass(
  information JSONB
)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaEnum CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaEnum(
  information JSONB
)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaInterface CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaInterface(
  information JSONB

)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaImport CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaImport(
  information JSONB
)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaSpecificComponent CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSpecificComponent(
  javaSource Integer REFERENCES ContainsTransverseInformation(id)
)INHERITS(ContainsTransverseInformation);


DROP TABLE IF EXISTS JavaField CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaField(
  information JSONB
)INHERITS(JavaSpecificComponent);



DROP TABLE IF EXISTS JavaMethod CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaMethod(
  information JSONB
)INHERITS(JavaSpecificComponent);



DROP TABLE IF EXISTS JavaDoc CASCADE;
CREATE TABLE IF NOT EXISTS JavaDoc(
  information JSONB
)INHERITS(TransverseInformation);
