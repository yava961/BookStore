package com.example.bookstore.config;


import com.example.bookstore.mapper.BookMapperImpl;
import com.example.bookstore.mapper.BookMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public BookMapper bookMapper(){
        return new BookMapperImpl();

    }

}
