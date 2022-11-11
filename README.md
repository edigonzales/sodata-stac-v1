# sodata-stac

## Bemerkungen
- Quick and Dirty die Model-Klassen copy/pastet und von die Validierungsannotationen entfernt. Es gab Probleme wegen Saxon, was keinen direkten Zusammehang hat aber als Dependency in der Lib dabei ist. Vielleicht reicht auch exclude aus?

- Notfalls alle Objekte bereits mit richtiger URL schreiben. Dann muss man nicht mehr normalizen, was die Request macht (??)

- Achtung: Devtools im Native Image?

## Develop

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Build

```
VENV_EXE_PATH=./venv/bin/graalpy CONFIG_FILE=$PWD/datasearch.xml STAC_DIR=/tmp/ ROOT_HREF=http://localhost:8080/stac/ ./mvnw package
```

### Native Image

Das Native Image kann mit Spring Boot momentan nicht erstellt werden: https://github.com/oracle/graal/issues/4473

```
CONFIG_FILE=$PWD/datasearch.xml STAC_DIR=/Users/stefan/tmp/staccreator/ ROOT_HREF=http://localhost:8080/stac/ ./mvnw -Pnative native:compile
```

## Run

```
CONFIG_FILE=$PWD/datasearch.xml STAC_DIR=/Users/stefan/tmp/staccreator/ ROOT_HREF=http://localhost:8080/stac/ java -jar target/sodata-stac-0.0.1-SNAPSHOT.jar
```

### Python-Gugus in VS Code

In den Settings `Python: Default Interpreter Path` den absoluten Pfad zu "graalpy" setzen. Dann einen ganzen Ordner (=Workspace?) öffnen, damit die third party libs gefunden werden.
