package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Publisher;
import com.example.demo.exception.NoDataException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

  @Autowired
  NamedParameterJdbcTemplate jdbcTemplateName;

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Book> findAll() {
    final SqlParameterSource params = null;
    final StringBuilder sql = new StringBuilder()
        .append(" select                                                            ")
        .append("   b.*,                                                            ")
        .append("   a.id author_id,                                                 ")
        .append("   a.first_name author_first_name,                                 ")
        .append("   a.last_name author_last_name,                                   ")
        .append("   p.name p_name                                                   ")
        .append(" from book b                                                       ")
        .append(" left outer join writing w                                         ")
        .append("   on b.id = w.book_id                                             ")
        .append(" left outer join publisher p                                       ")
        .append("   on b.pub_id = p.id                                              ")
        .append(" left outer join author a                                          ")
        .append("   on w.author_id = a.id                                           ");

    return execute(params, sql);
  }

  private List<Book> execute(final SqlParameterSource params, final StringBuilder sql) {
    final Map<Long, Book> bookMap = new TreeMap<>();
    jdbcTemplateName.queryForList(sql.toString(), params).forEach(res -> {
      final Long key = (Long) res.get("id");

      if (!bookMap.containsKey(key)) {
        // 新規

        final Book value = new Book();
        bookMap.put(key, value);
        value.setId((Long) res.get("id"));
        value.setTitle((String) res.get("title"));
        value.setIsbn((String) res.get("isbn"));
        value.setPublicationYear((String) res.get("publication_year"));
        value.setPubId((Long) res.get("pub_id"));

        // 著者
        value.setAuthors(new ArrayList<Author>() {
          private static final long serialVersionUID = 1L;
          {
            add(new Author() {
              {
                setId((Long) res.get("author_id"));
                setFirstName((String) res.get("author_first_name"));
                setLastName((String) res.get("author_last_name"));
              }
            });
          }
        });

        if (value.getPubId() != null) {
          value.setPublisher(new Publisher() {
            {
              setId((Long) res.get("pub_id"));
              setName((String) res.get("p_name"));
            }
          });
        }

      } else {
        // 関連データ追加

        final Book value = bookMap.get(key);
        value.getAuthors().add(new Author() {
          {
            setId((Long) res.get("author_id"));
            setFirstName((String) res.get("author_first_name"));
            setLastName((String) res.get("author_last_name"));
          }
        });
      }
    });
    return new ArrayList<>(bookMap.values());
  }

  public List<Book> findAllNoRelation() {
    final SqlParameterSource params = null;
    final StringBuilder sql = new StringBuilder()
        .append("select b.* from book b                                             ");

    final List<Book> bookList = new ArrayList<>();
    jdbcTemplateName.queryForList(sql.toString(), params).forEach(res -> {
      final Book value = new Book();
      bookList.add(value);
      value.setId((Long) res.get("id"));
      value.setTitle((String) res.get("title"));
      value.setIsbn((String) res.get("isbn"));
      value.setPublicationYear((String) res.get("publication_year"));
      value.setPubId((Long) res.get("pub_id"));
    });
    return bookList;
  }

  public Book findById(Book book) throws NoDataException {
    final SqlParameterSource params = new MapSqlParameterSource()
        // ID
        .addValue("id", book.getId());

    final StringBuilder sql = new StringBuilder()
        .append(" select                                                            ")
        .append("   b.*,                                                            ")
        .append("   a.id author_id,                                                 ")
        .append("   a.first_name author_first_name,                                 ")
        .append("   a.last_name author_last_name,                                   ")
        .append("   p.name p_name                                                   ")
        .append(" from book b                                                       ")
        .append(" left outer join writing w                                         ")
        .append("   on b.id = w.book_id                                             ")
        .append(" left outer join publisher p                                       ")
        .append("   on b.pub_id = p.id                                              ")
        .append(" left outer join author a                                          ")
        .append("   on w.author_id = a.id                                           ")
        .append(" where                                                             ")
        .append("   b.id = :id                                                      ");

    List<Book> execute = execute(params, sql);
    if (execute.size() == 0) {
      throw new NoDataException();
    }
    return execute.get(0);
  }

  public Long insert(Book book) {
    SqlParameterSource params = new MapSqlParameterSource()
        // タイトル
        .addValue("title", book.getTitle())
        // isbn
        .addValue("isbn", book.getIsbn())
        // 公開年
        .addValue("publication_year", book.getPublicationYear())
        // 出版社ID
        .addValue("pub_id", book.getPubId());

    final StringBuilder sql = new StringBuilder()
        .append(" insert into book set                                               ")
        .append("   title = :title,                                                  ")
        .append("   isbn = :isbn,                                                    ")
        .append("   publication_year = :publication_year,                            ")
        .append("   pub_id = :pub_id                                                 ");
    jdbcTemplateName.update(sql.toString(), params);

    // 登録したレコードのIDを返却
    return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
  }
}
