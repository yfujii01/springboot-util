package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.demo.entity.Book;
import com.example.demo.entity.Writing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

  @Autowired
  NamedParameterJdbcTemplate jdbcTemplateName;

  public List<Book> findAll() {
    SqlParameterSource paramSource = null;
    List<Map<String, Object>> resList = jdbcTemplateName
        .queryForList("select b.*,w.author_id from book b left outer join writing w on b.id = w.book_id", paramSource);
    Map<Long, Book> bookMap = new TreeMap<>();
    resList.forEach(res -> {
      Long key = (Long) res.get("id");

      // IDのダブリがないか確認する
      if (!bookMap.containsKey(key)) {
        // 新規

        Book value = new Book();
        bookMap.put(key, value);
        value.setId((Long) res.get("id"));
        value.setTitle((String) res.get("title"));
        value.setIsbn((String) res.get("isbn"));
        value.setPublicationYear((String) res.get("publication_year"));
        value.setPubId((Long) res.get("pub_id"));
        value.setWritings(new ArrayList<Writing>() {
          private static final long serialVersionUID = 1L;
          {
            add(new Writing() {
              {
                setBookId((Long) res.get("id"));
                setAuthorId((Long) res.get("author_id"));
              }
            });
          }
        });

      } else {
        // 関連データ追加

        Book value = bookMap.get(key);
        value.getWritings().add(new Writing() {
          {
            setBookId((Long) res.get("id"));
            setAuthorId((Long) res.get("author_id"));
          }
        });
      }
    });
    return new ArrayList<>(bookMap.values());
  }
}
