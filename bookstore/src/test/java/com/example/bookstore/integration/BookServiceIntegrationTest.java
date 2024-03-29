package com.example.bookstore.integration;

import book.*;

import com.example.bookstore.config.CitrusConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CitrusConfig.class)
class BookServiceIntegrationTest {

    private ManagedChannel grpcChannel;

    private String bookId;

    @BeforeEach
    public void setUp() {
        grpcChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        bookId = UUID.randomUUID().toString();

    }

    @Test
    void testAddBook() {

        BookServiceGrpc.BookServiceBlockingStub stub = BookServiceGrpc.newBlockingStub(grpcChannel);

        stub.addBook(AddBookRequest.newBuilder()
                .setId(bookId) // Use the generated UUID as the book ID
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("1234567890")
                .setQuantity(5)
                .build());

        assertTrue(true);
    }

    @Test
    void testDeleteBook() {

        BookServiceGrpc.BookServiceBlockingStub stub = BookServiceGrpc.newBlockingStub(grpcChannel);

        stub.deleteBook(DeleteBookRequest.newBuilder()
                .setId(bookId)
                .build());

        assertTrue(true);
    }

}



