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

  /** 全件検索 */
  public List<Book> findAll() {
    // パラメータ設定
    final SqlParameterSource params = null;
    final StringBuilder sql = new StringBuilder();
    sql.append(" select");
    sql.append("   b.*,");
    sql.append("   a.id author_id,");
    sql.append("   a.first_name author_first_name,");
    sql.append("   a.last_name author_last_name,");
    sql.append("   p.name p_name");
    sql.append(" from book b");
    sql.append(" left outer join writing w");
    sql.append("   on b.id = w.book_id");
    sql.append(" left outer join publisher p");
    sql.append("   on b.pub_id = p.id");
    sql.append(" left outer join author a");
    sql.append("   on w.author_id = a.id");
    sql.append(" order by");
    sql.append("   b.id desc,");
    sql.append("   a.id");

    // SQL発行
    return execute(params, sql);
  }

  /** 全件検索(関連なし) */
  public List<Book> findAllNoRelation() {
    // パラメータ設定
    final SqlParameterSource params = null;
    final StringBuilder sql = new StringBuilder();
    sql.append("select b.* from book b");

    // SQL発行
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

  /** ID指定で検索 */
  public Book findById(Book book) throws NoDataException {
    // パラメータ設定
    final SqlParameterSource params = new MapSqlParameterSource()
        // ID
        .addValue("id", book.getId());

    final StringBuilder sql = new StringBuilder();
    sql.append(" select");
    sql.append("   b.*,");
    sql.append("   a.id author_id,");
    sql.append("   a.first_name author_first_name,");
    sql.append("   a.last_name author_last_name,");
    sql.append("   p.name p_name");
    sql.append(" from book b");
    sql.append(" left outer join writing w");
    sql.append("   on b.id = w.book_id");
    sql.append(" left outer join publisher p");
    sql.append("   on b.pub_id = p.id");
    sql.append(" left outer join author a");
    sql.append("   on w.author_id = a.id");
    sql.append(" where");
    sql.append("   b.id = :id");

    // SQL発行
    List<Book> execute = execute(params, sql);
    if (execute.size() == 0) {
      throw new NoDataException("Data Not Found!");
    }
    return execute.get(0);
  }

  /** 登録 */
  public Long insert(Book book) {
    // パラメータ設定
    SqlParameterSource params = new MapSqlParameterSource()
        // タイトル
        .addValue("title", book.getTitle())
        // isbn
        .addValue("isbn", book.getIsbn())
        // 公開年
        .addValue("publication_year", book.getPublicationYear())
        // 出版社ID
        .addValue("pub_id", book.getPubId());

    final StringBuilder sql = new StringBuilder();
    sql.append(" insert into book set");
    sql.append("   title = :title,");
    sql.append("   isbn = :isbn,");
    sql.append("   publication_year = :publication_year,");
    sql.append("   pub_id = :pub_id");
    jdbcTemplateName.update(sql.toString(), params);

    // 登録したレコードのIDを返却
    return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
  }

  /**
   * 修正
   * 
   * @throws NoDataException
   */
  public Book update(Book book) throws NoDataException {
    // パラメータ設定
    SqlParameterSource params = new MapSqlParameterSource()
        // ID
        .addValue("id", book.getId())
        // タイトル
        .addValue("title", book.getTitle())
        // isbn
        .addValue("isbn", book.getIsbn())
        // 公開年
        .addValue("publication_year", book.getPublicationYear())
        // 出版社ID
        .addValue("pub_id", book.getPubId());

    // SQL発行
    final StringBuilder sql = new StringBuilder();
    sql.append(" update book set");
    sql.append("   title = :title,");
    sql.append("   isbn = :isbn,");
    sql.append("   publication_year = :publication_year");
    sql.append(" where");
    sql.append("   pub_id = :pub_id");
    jdbcTemplateName.update(sql.toString(), params);

    // 返却
    return this.findById(book);
  }

  /** 削除 */
  public void delete(Book book) {
    // パラメータ設定
    final SqlParameterSource params = new MapSqlParameterSource()
        // ID
        .addValue("id", book.getId());

    final StringBuilder sql = new StringBuilder();
    sql.append(" delete from book");
    sql.append(" where");
    sql.append("   id = :id");

    // SQL発行
    jdbcTemplateName.update(sql.toString(), params);
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

        // 著者情報
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

        // 出版社情報
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
        // 著者情報(2件目以降)
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
}
