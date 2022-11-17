package pl.edu.pjwstk.ARC2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Arc2Application {

	public static void main(String[] args) {
		SpringApplication.run(Arc2Application.class, args);
	}

}
