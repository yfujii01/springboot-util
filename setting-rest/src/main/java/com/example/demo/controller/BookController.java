package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> findAll() throws JsonProcessingException {
        return bookRepository.findAll();
    }

    @GetMapping("{id}")
    public Book findById(@PathVariable("id") Long id) throws Exception {
        Book selectBook = new Book();
        selectBook.setId(id);
        return bookRepository.findById(selectBook);
    }

    @GetMapping("norel")
    public List<Book> findAllNorel() throws Exception {
        return bookRepository.findAllNoRelation();
    }

    @PostMapping()
    public Long insert(@Validated @RequestBody Book book) throws Exception {
        return bookRepository.insert(book);
    }

    @PutMapping("{id}")
    public Book update(@PathVariable("id") Long id, @Validated @RequestBody Book book) throws Exception {
        book.setId(id);
        return bookRepository.update(book);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) throws Exception {
        Book selectBook = new Book();
        selectBook.setId(id);
        bookRepository.delete(selectBook);
    }
}