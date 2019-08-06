package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;

import com.example.demo.entity.Book;
import com.example.demo.exception.NoDataException;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public String findAll() throws JsonProcessingException {
        List<Book> booklist = bookRepository.findAll();
        return new ObjectMapper().writeValueAsString(booklist);
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") Long id) throws JsonProcessingException, NoDataException {
        Book selectBook = new Book();
        selectBook.setId(id);
        Book resultBook = bookRepository.findById(selectBook);
        return new ObjectMapper().writeValueAsString(resultBook);
    }

    @GetMapping("norel")
    public String findAllNorel() throws JsonProcessingException {
        List<Book> booklist = bookRepository.findAllNoRelation();
        return new ObjectMapper().writeValueAsString(booklist);
    }

    @PostMapping()
    public Long insert(@Validated @RequestBody Book book) throws Exception {
        return bookRepository.insert(book);
    }

    // @PutMapping("{id}")
    // public void update(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
    //     HogeModel hoge = objectMapper.readValue(request.getInputStream(), HogeModel.class);
    //     SqlParameterSource params = new MapSqlParameterSource().addValue("name", hoge.name).addValue("id", id);
    //     jdbcTemplateName.update("UPDATE HOGE SET name = :name WHERE id = :id", params);
    // }

    // @DeleteMapping("{id}")
    // public void delete(@PathVariable("id") int id) throws Exception {
    //     SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
    //     jdbcTemplateName.update("DELETE FROM HOGE WHERE id = :id", params);
    // }
}