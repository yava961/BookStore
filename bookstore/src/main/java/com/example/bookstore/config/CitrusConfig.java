package com.example.bookstore.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CitrusConfig {

    @Value("${grpc.server.host}")
    private String grpcServerHost = "localhost";

    @Value("${grpc.server.port}")
    private int grpcServerPort = 6565;

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();
    }
}
