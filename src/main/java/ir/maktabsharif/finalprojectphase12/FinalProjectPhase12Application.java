package ir.maktabsharif.finalprojectphase12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "ir.maktabsharif.finalprojectphase12")
public class FinalProjectPhase12Application {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectPhase12Application.class, args);
    }

}
