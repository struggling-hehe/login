package com.clear.loginjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author jiexipeng
 */

@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class})
public class LoginJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginJwtApplication.class, args);
    }

}
