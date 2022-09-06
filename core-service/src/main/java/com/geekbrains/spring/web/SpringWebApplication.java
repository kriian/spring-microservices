package com.geekbrains.spring.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringWebApplication {
	// Домашнее задание:
	// Реализовать перехватчик методов удаления и
	// *Вывести пользователя, который залогинился



	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}

}
