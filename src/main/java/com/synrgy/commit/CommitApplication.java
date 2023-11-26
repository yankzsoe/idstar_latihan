package com.synrgy.commit;

import com.synrgy.commit.controller.fileupload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class CommitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommitApplication.class, args);
	}

}
