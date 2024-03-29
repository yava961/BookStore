package com.example.bookstore.mapper;




import book.AddBookRequest;
import book.UpdateBookRequest;
import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Book;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto bookDto);

    BookDto toDto(AddBookRequest request);

    BookDto toDto(UpdateBookRequest request);

    BookDto toDto(Book book);
    default UUID map(String value) {
        return UUID.fromString(value); // Convert String to UUID
    }
}