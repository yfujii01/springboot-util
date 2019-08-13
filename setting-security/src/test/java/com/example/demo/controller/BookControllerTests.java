package com.example.demo.controller;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Publisher;
import com.example.demo.repository.BookRepository;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTests extends AbstractControllerTests {
  String token;

  /** mockを作成する */
  @Before
  public void setup() {
    super.setup();

    String username = "root";
    String password = "password";

    try {
      token = getTokenString(username, password);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void findAll() throws Exception {
    // public呼び出し
    MvcResult result =
        mockMvc
            .perform(
                (MockMvcRequestBuilders //
                    .get("/books") // url
                    .header(SecurityConstants.HEADER_STRING, token) // header
                ))
            // 返却されるhttp status code
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    // body内容の確認
    String body = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    List<Book> books = mapper.readValue(body, new TypeReference<List<Book>>() {});

    Assert.assertEquals(11, books.size());
  }

  @Test
  public void findById() throws Exception {
    // public呼び出し
    MvcResult result =
        mockMvc
            .perform(
                (MockMvcRequestBuilders //
                    .get("/books/1") // url
                    .header(SecurityConstants.HEADER_STRING, token) // header
                ))
            // 返却されるhttp status code
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    // body内容の確認
    String body = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    Book book = mapper.readValue(body, Book.class);

    Book expected = new Book();
    expected.setId(new Long(1));
    expected.setTitle("ゆめをかなえるぞう");
    expected.setIsbn("651324212");
    expected.setPublicationYear("2017");
    expected.setPubId(new Long(1));
    expected.setPublisher(
        new Publisher() {
          {
            setId(new Long(1));
            setName("アイカム");
          }
        });
    expected.setAuthors(
        new ArrayList<Author>() {
          private static final long serialVersionUID = 1L;

          {
            add(
                new Author() {
                  {
                    setId(new Long(1));
                    setLastName("さとう");
                    setFirstName("たける");
                  }
                });
            add(
                new Author() {
                  {
                    setId(new Long(2));
                    setLastName("たなか");
                    setFirstName("ごろう");
                  }
                });
          }
        });

    Assert.assertEquals(expected.toString(), book.toString());
  }

  @Autowired BookRepository bookRepository;

  @Test
  public void insert() throws Exception {

    // テスト実行前のbook
    List<Book> beforeBooks = bookRepository.findAllNoRelation();

    Book insertBook =
        new Book() {
          {
            setTitle("title");
            setIsbn("isbn");
            setPublicationYear("2019");
          }
        };

    // public呼び出し
    MvcResult result =
        mockMvc
            .perform(
                (MockMvcRequestBuilders //
                        .post("/books") // url
                        .header(SecurityConstants.HEADER_STRING, token) // header
                    )
                    .contentType(MediaType.APPLICATION_JSON) // ContentTypeの設定
                    .content(new ObjectMapper().writeValueAsString(insertBook)) // パラメータJsonの設定
                )
            .andExpect(MockMvcResultMatchers.status().isOk()) // 返却されるhttp status code
            .andReturn();

    // body内容の確認
    String body = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    Long id = mapper.readValue(body, Long.class);

    // テスト実行後のbook
    List<Book> afterBooks = bookRepository.findAllNoRelation();

    Assert.assertEquals(beforeBooks.size() + 1, afterBooks.size());
    Assert.assertEquals(beforeBooks.size() + 1, id.intValue());
  }
}
