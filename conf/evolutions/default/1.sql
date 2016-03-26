DROP TABLE IF EXISTS ContainsTransversalInformation CASCADE;
CREATE TABLE IF NOT EXISTS ContainsTransversalInformation(
  id  SERIAL PRIMARY KEY
);



DROP TABLE IF EXISTS TrasversalInformation CASCADE;
CREATE TABLE IF NOT EXISTS TrasversalInformation(
  id  SERIAL PRIMARY KEY,
  information JSONB,
  containsTransversalInformation Integer REFERENCES ContainsTransversalInformation(id)
);




DROP TABLE IF EXISTS Repository CASCADE;
CREATE  TABLE  IF NOT EXISTS Repository(
  id SERIAL PRIMARY KEY,
  url varchar(255),
  usr varchar(255),
  pwd varchar(255),
  localPath varchar(255),
  subversiontype varchar(255)
);


DROP TABLE IF EXISTS RepositoryVersion CASCADE;
CREATE  TABLE  IF NOT EXISTS RepositoryVersion(
  id SERIAL PRIMARY KEY,
  localPath varchar(255),
  repositoryId Integer REFERENCES Repository(id)

);


DROP TABLE IF EXISTS File CASCADE;
CREATE  TABLE  IF NOT EXISTS File(
  id SERIAL PRIMARY KEY,
  path varchar(255),
  name varchar(255),
  size INTEGER,
  repositoryVersionId Integer REFERENCES RepositoryVersion(id)
);




DROP TABLE IF EXISTS BinaryFile CASCADE;
CREATE  TABLE  IF NOT EXISTS BinaryFile(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS TextFile CASCADE;
CREATE  TABLE  IF NOT EXISTS TextFile(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS JavaFile CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaFile(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS JavaSourceObjects CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSourceObjects(
  id  SERIAL PRIMARY KEY,
  javaFile Integer REFERENCES JavaFile(id),

)INHERITS(ContainsTransversalInformation);

DROP TABLE IF EXISTS JavaClass CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaClass(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSourceObjects);

DROP TABLE IF EXISTS JavaInterface CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaInterface(
  id  SERIAL PRIMARY KEY,
  information JSONB

)INHERITS(JavaSourceObjects);

DROP TABLE IF EXISTS JavaImport CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaImport(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSourceObjects);

DROP TABLE IF EXISTS JavaSpecificComponent CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSpecificComponent(
  id  SERIAL PRIMARY KEY,
  javaSource Integer REFERENCES JavaSourceObjects(id),
)INHERITS(ContainsTransversalInformation);


DROP TABLE IF EXISTS Fileds CASCADE;
CREATE  TABLE  IF NOT EXISTS Fileds(
  id  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSpecificComponent);



DROP TABLE IF EXISTS Methods CASCADE;
CREATE  TABLE  IF NOT EXISTS Methods(
  id  SERIAL PRIMARY KEY,
  information JSONB

)INHERITS(JavaSpecificComponent);






DROP TABLE IF EXISTS JavaDoc CASCADE;
CREATE TABLE IF NOT EXISTS JavaDoc(
  id  SERIAL PRIMARY KEY,
  doc VARCHAR(255)
)INHERITS(TrasversalInformation);
