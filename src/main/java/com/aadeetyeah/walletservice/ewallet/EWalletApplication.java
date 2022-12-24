package com.aadeetyeah.walletservice.ewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWalletApplication.class, args);
	}

}

/* Issues faced
* 1. while importing this project, Solution: Change to latest maven version
* -> https://stackoverflow.com/questions/53321302/maven-intellij-error-unable-to-import-maven-project-see-logs-for-details*/