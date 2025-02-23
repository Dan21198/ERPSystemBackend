package osu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Rpr1Application {

    public static void main(String[] args) {
        SpringApplication.run(Rpr1Application.class, args);
    }

}
