package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;

import java.util.UUID;

public interface BookService {
    BookDto addBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto);

    void deleteBook(UUID id);

    BookDto getBook(UUID id);


}
