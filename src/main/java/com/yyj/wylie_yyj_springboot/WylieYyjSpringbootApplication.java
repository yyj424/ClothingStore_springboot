package com.yyj.wylie_yyj_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WylieYyjSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WylieYyjSpringbootApplication.class, args);
    }

}
