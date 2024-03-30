package com.example.bookstore.integration;

import book.*;
import com.example.bookstore.BookstoreApplication;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookServiceIntegrationTest {

    private static ConfigurableApplicationContext context;
    private static ManagedChannel channel;
    private static BookServiceGrpc.BookServiceBlockingStub stub;
    private static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    static void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();

        context = SpringApplication.run(BookstoreApplication.class,
                "--grpc.server.port=" + 6565,
                "--spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                "--spring.datasource.username=" + postgresContainer.getUsername(),
                "--spring.datasource.password=" + postgresContainer.getPassword());

        channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        stub = BookServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    static void tearDown() {
        context.close();

        channel.shutdown();

        postgresContainer.stop();
    }

    @Test
    void testAddBook() {
        String bookId = UUID.randomUUID().toString();
        AddBookRequest request = AddBookRequest.newBuilder()
                .setId(bookId)
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("1234567890")
                .setQuantity(5)
                .build();

        BookResponse response = stub.addBook(request);

        assertNotNull(response.getId());
    }

    @Test
    void testDeleteBook() {
        String bookId = UUID.randomUUID().toString();

        AddBookRequest addRequest = AddBookRequest.newBuilder()
                .setId(bookId)
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("1234567890")
                .setQuantity(5)
                .build();

        BookResponse addResponse = stub.addBook(addRequest);
        assertNotNull(addResponse.getId(), "Book was not added successfully");

        DeleteBookRequest deleteRequest = DeleteBookRequest.newBuilder()
                .setId(bookId)
                .build();

        EmptyResponse deleteResponse = stub.deleteBook(deleteRequest);

        assertNotNull(deleteResponse, "Delete response is null");

    }

}
