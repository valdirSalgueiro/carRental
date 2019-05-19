package br.com.arizonacrossmedia.carrental;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarrentalApplication {

    @Bean
    public Queue hello() {
        return new Queue("rentalQueue");
    }

    public static void main(String[] args) {
        SpringApplication.run(CarrentalApplication.class, args);
    }
}
