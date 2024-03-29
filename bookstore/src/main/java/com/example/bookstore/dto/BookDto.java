package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
}
