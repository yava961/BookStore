package com.example.bookstore.unit;



import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {
        BookDto bookDto = new BookDto(UUID.randomUUID(), "Test Book", "Test Author", "1234567890", 5);
        Book expectedBook = new Book(); // Mock the book entity returned by repository.save()
        when(bookMapper.toEntity(bookDto)).thenReturn(expectedBook);
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook); // Ensure repository.save() returns the expected book
        when(bookMapper.toDto(expectedBook)).thenReturn(bookDto); // Ensure mapper returns the expected DTO

        BookDto result = bookService.addBook(bookDto);

        assertEquals(bookDto.getTitle(), result.getTitle());
        assertEquals(bookDto.getAuthor(), result.getAuthor());
    }

    @Test
    void testGetBookById() throws BookNotFoundException {
        UUID bookId = UUID.randomUUID();
        BookDto expectedBookDto = new BookDto(bookId, "Test Book", "Test Author", "1234567890", 5);
        Book expectedBook = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);

        BookDto result = bookService.getBook(bookId);

        assertEquals(expectedBookDto.getTitle(), result.getTitle());
        assertEquals(expectedBookDto.getAuthor(), result.getAuthor());
    }



    @Test
    void testUpdateBook_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        BookDto bookDto = new BookDto(nonExistentId, "Non-existent Book", "Non-existent Author", "0987654321", 1);
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookDto));
        verify(bookRepository, times(1)).findById(nonExistentId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void testDeleteBook() {

        UUID bookIdToDelete = UUID.randomUUID();

        assertDoesNotThrow(() -> bookService.deleteBook(bookIdToDelete));

        verify(bookRepository, times(1)).deleteById(bookIdToDelete);
    }
}
