DROP TABLE IF EXISTS ContainsTransverseInformation CASCADE;
CREATE TABLE IF NOT EXISTS ContainsTransverseInformation(
  id_CTI  SERIAL PRIMARY KEY
);



DROP TABLE IF EXISTS TransverseInformation CASCADE;
CREATE TABLE IF NOT EXISTS TransverseInformation(
  id_TI  SERIAL PRIMARY KEY,
  information JSONB,
  containsTransverseInformation Integer REFERENCES ContainsTransverseInformation(id_CTI)
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
  id_F SERIAL PRIMARY KEY,
  path varchar(255),
  name varchar(255),
  size INTEGER,
  repositoryVersionId Integer REFERENCES RepositoryVersion(id)
);




DROP TABLE IF EXISTS BinaryFile CASCADE;
CREATE  TABLE  IF NOT EXISTS BinaryFile(
  id_BF  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS TextFile CASCADE;
CREATE  TABLE  IF NOT EXISTS TextFile(
  id_TF  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS JavaFile CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaFile(
  id_JF  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(File);

DROP TABLE IF EXISTS JavaSourceObject CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSourceObject(
  id_JSO  SERIAL PRIMARY KEY,
  javaFile Integer REFERENCES JavaFile(id_JF)

)INHERITS(ContainsTransverseInformation);

DROP TABLE IF EXISTS JavaClass CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaClass(
  id_JC  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaInterface CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaInterface(
  id_JI  SERIAL PRIMARY KEY,
  information JSONB

)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaImport CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaImport(
  id_JI  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSourceObject);

DROP TABLE IF EXISTS JavaSpecificComponent CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaSpecificComponent(
  id_JSP  SERIAL PRIMARY KEY,
  javaSource Integer REFERENCES JavaSourceObject(id_JSO)
)INHERITS(ContainsTransverseInformation);


DROP TABLE IF EXISTS JavaField CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaField(
  id_F  SERIAL PRIMARY KEY,
  information JSONB
)INHERITS(JavaSpecificComponent);



DROP TABLE IF EXISTS JavaMethod CASCADE;
CREATE  TABLE  IF NOT EXISTS JavaMethod(
  id_M  SERIAL PRIMARY KEY,
  information JSONB

)INHERITS(JavaSpecificComponent);



DROP TABLE IF EXISTS JavaDoc CASCADE;
CREATE TABLE IF NOT EXISTS JavaDoc(
  id_JD  SERIAL PRIMARY KEY
  )INHERITS(TransverseInformation);
