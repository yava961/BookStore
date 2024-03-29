package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookServiceImpl  implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto addBook(BookDto bookDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(bookDto)));
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        return bookMapper.toDto(bookRepository.findById(bookDto.getId())
                .map(existingBook -> {
                    existingBook.setTitle(bookDto.getTitle());
                    existingBook.setAuthor(bookDto.getAuthor());
                    existingBook.setIsbn(bookDto.getIsbn());
                    existingBook.setQuantity(bookDto.getQuantity());
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookDto.getId())));
    }

    @Override
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto getBook(UUID id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id)));
    }

}
