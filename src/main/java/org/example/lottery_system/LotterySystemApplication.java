package org.example.lottery_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LotterySystemApplication {

    public static void main(String[] args) {
        System.out.println("Starting application...");
        SpringApplication.run(LotterySystemApplication.class, args);
    }
}
