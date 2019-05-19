package br.com.arizonacrossmedia.carrental;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarrentalApplication {

    public final static String RENTAL_MESSAGE_QUEUE = "rental-message-queue";

    @Bean
    public Queue queue() {
        return new Queue(RENTAL_MESSAGE_QUEUE);
    }

    public static void main(String[] args) {
        SpringApplication.run(CarrentalApplication.class, args);
    }
}
