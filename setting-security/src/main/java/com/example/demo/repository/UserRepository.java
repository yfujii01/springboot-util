package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.demo.entity.User;
import com.example.demo.entity.Authority;
import com.example.demo.exception.NoDataException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @Autowired
  NamedParameterJdbcTemplate jdbcTemplateName;

  @Autowired
  JdbcTemplate jdbcTemplate;

  /** 全件検索 */
  public List<User> findAll() {
    // パラメータ設定
    final SqlParameterSource params = null;

    // SQL
    final StringBuilder sql = new StringBuilder();
    sql.append(" select");
    sql.append("   u.*,");
    sql.append("   a.username authority_username,");
    sql.append("   a.authority authority");
    sql.append(" from users u");
    sql.append(" left outer join authorities a");
    sql.append("   on u.username = a.username");
    sql.append(" order by");
    sql.append("   u.username,");
    sql.append("   a.authority");

    // SQL発行
    return execute(params, sql);
  }

  /** ID指定で検索 */
  public User findById(User user) throws NoDataException {
    // パラメータ設定
    final SqlParameterSource params = new MapSqlParameterSource()
        // username
        .addValue("username", user.getUsername());

    // SQL
    final StringBuilder sql = new StringBuilder();
    sql.append(" select");
    sql.append("   u.*,");
    sql.append("   a.username authority_username,");
    sql.append("   a.authority authority");
    sql.append(" from users u");
    sql.append(" left outer join authorities a");
    sql.append("   on u.username = a.username");
    sql.append(" where");
    sql.append("   u.username = :username");
    sql.append(" order by");
    sql.append("   a.authority");

    // SQL発行
    List<User> execute = execute(params, sql);
    if (execute.size() == 0) {
      throw new NoDataException("Data Not Found!");
    }
    return execute.get(0);
  }

  /** 登録 */
  public void insert(User user) {
    // パラメータ設定
    SqlParameterSource params = new MapSqlParameterSource()
        // username
        .addValue("username", user.getUsername())
        // password
        .addValue("password", user.getPassword())
        // 有効状態
        .addValue("enabled", user.getEnabled());

    // SQL
    final StringBuilder sql = new StringBuilder();
    sql.append(" insert into users set");
    sql.append("   username = :username,");
    sql.append("   password = :password,");
    sql.append("   enabled = :enabled");
    jdbcTemplateName.update(sql.toString(), params);

    // 権限追加
    insertAuthority(user);
  }

  /** authorty登録 */
  private void insertAuthority(User user) {
    user.getAuthorities().forEach(authorty -> {
      // パラメータ設定
      SqlParameterSource params = new MapSqlParameterSource()
          // username
          .addValue("username", user.getUsername())
          // 権限
          .addValue("authority", authorty.getAuthority());

      // SQL
      final StringBuilder sql = new StringBuilder();
      sql.append(" insert into authorities set");
      sql.append("   username = :username,");
      sql.append("   authority = :authority");
      jdbcTemplateName.update(sql.toString(), params);
    });
  }

  /**
   * 修正
   * 
   * @throws NoDataException
   */
  public User update(User user) throws NoDataException {
    // パラメータ設定
    SqlParameterSource params = new MapSqlParameterSource()
        // username
        .addValue("username", user.getUsername())
        // password
        .addValue("password", user.getPassword())
        // 有効状態
        .addValue("enabled", user.getEnabled());

    // SQL発行
    final StringBuilder sql = new StringBuilder();
    sql.append(" update users set");
    sql.append("   password = :password,");
    sql.append("   enabled = :enabled");
    sql.append(" where");
    sql.append("   username = :username");
    jdbcTemplateName.update(sql.toString(), params);

    // 返却
    return this.findById(user);
  }

  /** 削除 */
  public void delete(User user) {
    // パラメータ設定
    final SqlParameterSource params = new MapSqlParameterSource()
        // username
        .addValue("username", user.getUsername());

    // SQL
    final StringBuilder sql = new StringBuilder();
    sql.append(" delete from users");
    sql.append(" where");
    sql.append("   username = :username");

    // SQL発行
    jdbcTemplateName.update(sql.toString(), params);
  }

  private List<User> execute(final SqlParameterSource params, final StringBuilder sql) {
    final Map<String, User> userMap = new TreeMap<>();
    jdbcTemplateName.queryForList(sql.toString(), params).forEach(res -> {
      final String key = (String) res.get("username");

      if (!userMap.containsKey(key)) {
        // 新規

        final User value = new User();
        userMap.put(key, value);
        value.setUsername((String) res.get("username"));
        value.setPassword((String) res.get("password"));
        value.setEnabled((Boolean) res.get("enabled"));

        // 著者情報
        if (res.get("authority_username") != null) {
          value.setAuthorities(new ArrayList<Authority>() {
            private static final long serialVersionUID = 1L;
            {
              add(new Authority() {
                {
                  setUsername((String) res.get("authority_username"));
                  setAuthority((String) res.get("authority"));
                }
              });
            }
          });
        }
      } else {
        // 関連データ追加

        final User value = userMap.get(key);
        // 著者情報(2件目以降)
        if (res.get("authority_username") != null) {
          value.getAuthorities().add(new Authority() {
            {
              setUsername((String) res.get("authority_username"));
              setAuthority((String) res.get("authority"));
            }
          });
        }
      }
    });
    return new ArrayList<>(userMap.values());
  }
}
