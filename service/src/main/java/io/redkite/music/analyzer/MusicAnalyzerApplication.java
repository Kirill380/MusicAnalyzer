package io.redkite.music.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class MusicAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicAnalyzerApplication.class, args);
	}
}
