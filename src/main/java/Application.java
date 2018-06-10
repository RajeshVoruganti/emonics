

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import storage.StorageProperties;

@EnableAutoConfiguration
@EnableJpaRepositories 
@EnableJpaAuditing
@ComponentScan(basePackages = {"storage","storage.controller","storage.model"
		,"service","repository","model","exception","schedule"})
@SpringBootApplication(scanBasePackages = {"storage","storage.controller","storage.model"
		,"service","repository","model","exception","schedule"})
@EntityScan("model")
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
