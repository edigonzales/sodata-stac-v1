package ch.so.agi.sodata.stac;

import java.nio.file.Paths;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class SodataStacApplication {

	public static void main(String[] args) {
		SpringApplication.run(SodataStacApplication.class, args);
	}

    // see: https://blogs.oracle.com/javamagazine/post/java-graalvm-polyglot-python-r
    // Relevant für mich?
    @Bean
    Engine createEngine() {
        return Engine.newBuilder().build();
    }

    @Bean
    Context createContext(Engine engine) {
        String VENV_EXECUTABLE = SodataStacApplication.class.getClassLoader()
                .getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();

        return Context.newBuilder("python").allowAllAccess(true).option("python.Executable", VENV_EXECUTABLE)
                .option("python.ForceImportSite", "true")
                .engine(engine)
                .build();
    }
    
    // Anwendung ist fertig gestartet. 
    // Kubernetes: Live aber nicht ready.
    // Importieren der Konfiguration. D.h. der XML-Datei mit den vorhandenen
    // Themapublikationen (aka Datensätzen).
    @Bean
    CommandLineRunner init(ConfigService configService) {
        return args -> {
            configService.readXml();
            
            // Testeshalber stac hier.  
//            StacService stacService = new StacService();
//            stacService.foo();

            
            //System.out.println(configService.getThemePublicationList().size());
        };
    }
}
