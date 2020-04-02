package com.solan.localfdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(value = "com.solan.jbase.aspect")
public class LocalFdfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalFdfsApplication.class, args);
	}

}
