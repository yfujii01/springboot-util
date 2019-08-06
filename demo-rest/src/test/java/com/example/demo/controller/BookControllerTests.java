package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTests {

  /** コントローラーテストを呼び出すためのmock */
  protected MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  /** mockを作成する */
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
        // web context 有効化
        .webAppContextSetup(context)
        // 作成
        .build();
  }

  @Test
  public void findAll() throws Exception {
    // public呼び出し
    MvcResult result = mockMvc.perform((MockMvcRequestBuilders.get("/books")))
        // 返却されるhttp status code
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    // body内容の確認
    String body = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    List<Book> books = mapper.readValue(body, new TypeReference<List<Book>>() {
    });

    Assert.assertEquals(11, books.size());
  }

}
