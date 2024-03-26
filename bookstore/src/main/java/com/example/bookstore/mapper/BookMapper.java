package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Book;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto bookDto);

    BookDto toDto(Book book);
}
