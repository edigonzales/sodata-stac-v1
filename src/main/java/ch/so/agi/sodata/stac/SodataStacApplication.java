package ch.so.agi.sodata.stac;

import java.nio.file.Paths;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
//import org.graalvm.polyglot.Context;
//import org.graalvm.polyglot.Engine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
@Configuration
public class SodataStacApplication {

	public static void main(String[] args) {
		SpringApplication.run(SodataStacApplication.class, args);
	}
	
    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    } 

    // Brauche ich gar nich als Bean? Was habe ich für Vorteile? Ich benötige
    // es nur an einer Stelle, dort kann ich den Context auch gleich wieder schliessen.
    
    // see: https://blogs.oracle.com/javamagazine/post/java-graalvm-polyglot-python-r
    // Relevant für mich?
//    @Bean
//    Engine createEngine() {
//        return Engine.newBuilder().build();
//    }
//
//    @Bean
//    Context createContext(Engine engine) {
//        String VENV_EXECUTABLE = SodataStacApplication.class.getClassLoader()
//                .getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();
//
//        return Context.newBuilder("python")
//                .allowAllAccess(true)
//                .option("python.Executable", VENV_EXECUTABLE)
//                .option("python.ForceImportSite", "true")
//                .engine(engine)
//                .build();
//    }
    
    // Anwendung ist fertig gestartet. 
    // Kubernetes: Live aber nicht ready.
    
    // Parsen des XML mit den Themenpublikation und Umwandlung nach Stac.
    // Themapublikationen (aka Datensätzen).
    @Bean
    CommandLineRunner init(ConfigService configService) {
        return args -> {            
            configService.readXml();
        };
    }
}
