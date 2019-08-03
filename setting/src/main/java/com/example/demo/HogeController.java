package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
@RequestMapping("/hoge")
public class HogeController {

  @Autowired
  NamedParameterJdbcTemplate jdbcTemplateName;

  ObjectMapper objectMapper = new ObjectMapper();

  @Data
  static class HogeModel {
    private int id;
    private String name;
  }

  @GetMapping()
  public String select() throws Exception {
    List<HogeModel> hoges = jdbcTemplateName.query("select * from hoge", new RowMapper<HogeModel>() {
      public HogeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        HogeModel hoge = new HogeModel();
        hoge.setId(rs.getInt("id"));
        hoge.setName(rs.getString("name"));
        return hoge;
      }
    });

    // JSONに変換して返却
    return objectMapper.writeValueAsString(hoges);
  }

  @GetMapping("{id}")
  public String selectById(@PathVariable("id") String id) throws Exception {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
    List<HogeModel> hoges = jdbcTemplateName.query("select * from hoge where id = :id", params,
        new RowMapper<HogeModel>() {
          public HogeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            HogeModel hoge = new HogeModel();
            hoge.setId(rs.getInt("id"));
            hoge.setName(rs.getString("name"));
            return hoge;
          }
        });

    // JSONに変換して返却
    return objectMapper.writeValueAsString(hoges);
  }

  @PostMapping()
  public void insert(HttpServletRequest request) throws Exception {
    HogeModel hoge = objectMapper.readValue(request.getInputStream(), HogeModel.class);
    SqlParameterSource params = new MapSqlParameterSource().addValue("name", hoge.getName());
    jdbcTemplateName.update("INSERT INTO HOGE SET name = :name", params);
  }

  @PutMapping("{id}")
  public void update(@PathVariable("id") int id,HttpServletRequest request) throws Exception {
    HogeModel hoge = objectMapper.readValue(request.getInputStream(), HogeModel.class);
    SqlParameterSource params = new MapSqlParameterSource().addValue("name", hoge.name).addValue("id", id);
    jdbcTemplateName.update("UPDATE HOGE SET name = :name WHERE id = :id", params);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable("id") int id) throws Exception {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
    jdbcTemplateName.update("DELETE FROM HOGE WHERE id = :id", params);
  }
}