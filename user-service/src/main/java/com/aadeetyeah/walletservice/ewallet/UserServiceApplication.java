package com.aadeetyeah.walletservice.ewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(UserServiceApplication.class);
    }
}

/* SQLException not connecting even with correct creds.
https://stackoverflow.com/questions/62096031/java-sql-sqlexception-access-denied-for-user-localhost-using-password-no*/
