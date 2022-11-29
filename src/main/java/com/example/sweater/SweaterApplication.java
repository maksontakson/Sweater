package com.example.sweater;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SweaterApplication {
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(SweaterApplication.class, args);
//        displayAllBeans();
    }
//    public static void displayAllBeans() {
//        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
//        for(String beanName : allBeanNames) {
//            System.out.println(beanName);
//        }
//    }
}
