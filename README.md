# sodata-stac


## Develop

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

```
./mvnw -Pnative native:compile
```

### Python-Gugus in VS Code

In den Settings `Python: Default Interpreter Path` den absoluten Pfad zu "graalpy" setzen. Dann einen ganzen Ordner (=Workspace?) öffnen, damit die third party libs gefunden werden.