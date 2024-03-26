package com.example.bookstore.grpc;




import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@GRpcService
public class BookService extends BookServiceGrpc.BookServiceImplBase {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public void getBook(BookId request, StreamObserver<Book> responseObserver) {
        Optional<Book> optionalBook = bookRepository.findById(UUID.fromString(request.getId()))
                .map(bookMapper::toDto);
        if (optionalBook.isPresent()) {
            responseObserver.onNext(optionalBook.get());
        } else {
            responseObserver.onError(new Exception("Book not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(Book request, StreamObserver<Book> responseObserver) {
        BookDto bookDto = bookMapper.toDto(request);
        Book savedBook = bookRepository.save(bookMapper.toEntity(bookDto));
        responseObserver.onNext(bookMapper.toDto(savedBook));
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(Book request, StreamObserver<Book> responseObserver) {
        UUID bookId = UUID.fromString(request.getId());
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            BookDto updatedBookDto = bookMapper.toDto(request);
            Book updatedBook = bookMapper.toEntity(updatedBookDto);
            updatedBook.setId(bookId);
            bookRepository.save(updatedBook);
            responseObserver.onNext(request);
        } else {
            responseObserver.onError(new Exception("Book not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(BookId request, StreamObserver<Book> responseObserver) {
        UUID bookId = UUID.fromString(request.getId());
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            bookRepository.deleteById(bookId);
            responseObserver.onNext(Book.newBuilder().setId(request.getId()).build());
        } else {
            responseObserver.onError(new Exception("Book not found"));
        }
        responseObserver.onCompleted();
    }

    // Additional method for listing all books
    public void listBooks(com.example.bookstore.grpc.Empty request, StreamObserver<Book> responseObserver) {
        List<Book> books = bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
        books.forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
