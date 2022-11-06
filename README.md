# sodata-stac

## Bemerkungen
- Quick and Dirty die Model-Klassen copy/pastet und von die Validierungsannotationen entfernt. Es gab Probleme wegen Saxon, was keinen direkten Zusammehang hat aber als Dependency in der Lib dabei ist. Vielleicht reicht auch exclude aus?

- Notfalls alle Objekte bereits mit richtiger URL schreiben. Dann muss man nicht mehr normalizen, was die Request macht (??)

- Achtung: Devtools im Native Image?

## Develop

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

```
CONFIG_FILE=$PWD/datasearch.xml STAC_DIR=/Users/stefan/tmp/staccreator/ ROOT_HREF=http://localhost:8080/stac/ ./mvnw -Pnative native:compile
```

### Python-Gugus in VS Code

In den Settings `Python: Default Interpreter Path` den absoluten Pfad zu "graalpy" setzen. Dann einen ganzen Ordner (=Workspace?) öffnen, damit die third party libs gefunden werden.