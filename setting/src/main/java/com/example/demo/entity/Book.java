package com.example.demo.entity;

import java.util.List;
import lombok.Data;

@Data
public class Book {
    private Long id;
    private String title;
    private String isbn;
    private String publicationYear;
    private Long pubId;
    private Publisher publisher;
    private List<Writing> writings;
}
