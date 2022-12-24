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
* -> https://stackoverflow.com/questions/53321302/maven-intellij-error-unable-to-import-maven-project-see-logs-for-details
* 2. Spring initializer created project with spring 3 which by default uses java 17 so downgrade spring boot version in pom.xml
* -> https://stackoverflow.com/questions/73679679/java-cannot-access-org-springframework-boot-springapplication-bad-class-file
* -> https://stackoverflow.com/questions/74648576/java-class-file-has-wrong-version-61-0-should-be-55-0
* * 3. jakarta persistence not working
* ->https://stackoverflow.com/questions/56203811/package-javax-persistence-does-not-exist-using-spring-data-jpa-and-intellij#comment130793963_56204159*/
