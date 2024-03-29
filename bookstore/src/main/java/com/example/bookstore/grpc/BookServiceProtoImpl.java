package com.example.bookstore.grpc;




import book.*;
import com.example.bookstore.dto.BookDto;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GRpcService
public class BookServiceProtoImpl extends BookServiceGrpc.BookServiceImplBase {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public void addBook(AddBookRequest request, StreamObserver<BookResponse> responseObserver) {
        try {
            BookDto addedBook = bookService.addBook(bookMapper.toDto(request));
            responseObserver.onNext(toProto(addedBook));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        try {
            BookDto updatedBook = bookService.updateBook(bookMapper.toDto(request));
            responseObserver.onNext(toProto(updatedBook));
            responseObserver.onCompleted();
        } catch (BookNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            bookService.deleteBook(UUID.fromString(request.getId()));
            responseObserver.onNext(EmptyResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (BookNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        try {
            BookDto book = bookService.getBook(UUID.fromString(request.getId()));
            responseObserver.onNext(toProto(book));
            responseObserver.onCompleted();
        } catch (BookNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }


    private BookResponse toProto(BookDto bookDto) {
        return BookResponse.newBuilder()
                .setId(bookDto.getId().toString())
                .setTitle(bookDto.getTitle())
                .setAuthor(bookDto.getAuthor())
                .setIsbn(bookDto.getIsbn())
                .setQuantity(bookDto.getQuantity())
                .build();
    }

}
