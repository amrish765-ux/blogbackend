package com.blogapplication;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SpringBootApplication
@EnableTransactionManagement
public class BlogapplicaitonApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogapplicaitonApplication.class, args);

	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
