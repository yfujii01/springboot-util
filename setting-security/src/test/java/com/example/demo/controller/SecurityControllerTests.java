package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityControllerTests extends AbstractControllerTests {

  /** ログインエラーテスト */
  @Test
  public void login_error_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"root", "hoge", "パスワード誤り"});
    params.add(new Object[] {"root", "", "パスワード空白"});
    params.add(new Object[] {"hoge", "password", "存在しないユーザー"});
    params.add(new Object[] {"", "password", "ユーザー名空白"});
    params.add(new Object[] {"", "", "ユーザー名&パスワード空白"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      User user = new User();
      user.setUsername(username);
      user.setPassword(password);

      // 呼び出し
      mockMvc
          .perform(
              (MockMvcRequestBuilders.post("/login"))
                  // csrfトークンをsetする
                  .with(SecurityMockMvcRequestPostProcessors.csrf())
                  .content(new ObjectMapper().writeValueAsString(user)))
          .andExpect(MockMvcResultMatchers.status().isUnauthorized())
          .andReturn();
    }
  }

  /** プライペートページ token不正によりアクセス失敗するケース */
  @Test
  public void private_page_access_ng_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"", "token 空白"});
    params.add(new Object[] {"hogehoge", "token 不正"});

    for (Object[] p : params) {
      String token = (String) p[0];

      // private page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/private")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isForbidden())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("", content);
    }
  }

  /** トークン作成用の鍵が異なるケース */
  @Test
  public void token_key_error_and_throw_SignatureException_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(
        new Object[] {
          "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNTY1NDQ1MTIzLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdfQ.nPUK5QTXMQPRycG98JNs7QeCog3V93hKXuX-R99nt7Yu0joz89Wa_aumnmWws_Ipbipq2sJW7m6Mj_45YsmCig",
          "JWTのtoken作成用のパスワード鍵が異なる"
        });

    for (Object[] p : params) {
      String token = (String) p[0];

      // private page呼び出し
      try {
        mockMvc
            .perform(
                (MockMvcRequestBuilders.get("/private")
                        .header(SecurityConstants.HEADER_STRING, token))
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(MockMvcResultMatchers.status().isForbidden())
            .andReturn();
      } catch (SignatureException e) {
        continue;
      }
      Assert.fail("例外発生しなかったためNG");
    }
  }

  /** プライベートページにアクセスできるケース */
  @Test
  public void private_page_access_ok_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"root", "password"});
    params.add(new Object[] {"user", "password"});
    params.add(new Object[] {"guest", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // private page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/private")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("this is private page!", content);
    }
  }

  /** admin専用ページにアクセスできるケース */
  @Test
  public void admin_page_access_ok_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"root", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/adminonly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("this is admin only page!", content);
    }
  }

  /** admin専用ページにアクセスできないケース */
  @Test
  public void admin_page_access_ng_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"user", "password"});
    params.add(new Object[] {"guest", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/adminonly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isForbidden())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("", content);
    }
  }

  /** user and admin専用ページにアクセスできるケース */
  @Test
  public void user_and_admin_page_access_ok_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"user", "password"});
    params.add(new Object[] {"root", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/userandadminonly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("this is user and admin only page!", content);
    }
  }

  /** user and admin専用ページにアクセスできないケース */
  @Test
  public void user_and_admin_page_access_ng_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"guest", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/userandadminonly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isForbidden())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("", content);
    }
  }

  /** user専用ページにアクセスできるケース */
  @Test
  public void user_page_access_ok_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"user", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/useronly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("this is user only page!", content);
    }
  }

  /** user専用ページにアクセスできないケース */
  @Test
  public void user_page_access_ng_Test() throws Exception {
    List<Object[]> params = new ArrayList<>();
    params.add(new Object[] {"root", "password"});
    params.add(new Object[] {"guest", "password"});

    for (Object[] p : params) {
      String username = (String) p[0];
      String password = (String) p[1];

      String token = getTokenString(username, password);

      // page呼び出し
      MvcResult resule =
          mockMvc
              .perform(
                  (MockMvcRequestBuilders.get("/useronly")
                          .header(SecurityConstants.HEADER_STRING, token))
                      .with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(MockMvcResultMatchers.status().isForbidden())
              .andReturn();

      // body検証
      String content = resule.getResponse().getContentAsString();
      Assert.assertEquals("", content);
    }
  }
}
