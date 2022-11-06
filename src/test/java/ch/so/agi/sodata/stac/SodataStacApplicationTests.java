package ch.so.agi.sodata.stac;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// pystac öffnet beim normalisieren der href das File via Url. 
// Aus diesem Grund muss man den Port kennen. 
// Sowieso alles noch unschön.
@SpringBootTest(
        webEnvironment = WebEnvironment.DEFINED_PORT,
        properties = {
          "server.port=8080",
          "management.server.port=9090"
        })
//@SpringBootTest
class SodataStacApplicationTests {

	@Test
	void contextLoads() {
	}

}
