package web_diggers.abc_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecretConfigProperties.class)
public class AbcBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(AbcBackendApplication.class, args);
	}

}
