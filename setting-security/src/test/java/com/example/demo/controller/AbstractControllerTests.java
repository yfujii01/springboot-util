package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // テストデータをDBに保存しない
@Ignore
public abstract class AbstractControllerTests {

  /** コントローラーテストを呼び出すためのmock */
  protected MockMvc mockMvc;

  /** ロガー */
  protected static final Logger logger = LoggerFactory.getLogger(AbstractControllerTests.class);

  @Autowired private WebApplicationContext context;

  /** mockを作成する */
  @Before
  public void setup() {
    mockMvc =
        MockMvcBuilders
            // web context 有効化
            .webAppContextSetup(context)
            // spring securityを有効化
            .apply(SecurityMockMvcConfigurers.springSecurity())
            // 作成
            .build();
  }

  /**
   * tokenを発行する
   *
   * @param username ユーザー名
   * @param password パスワード
   * @return token
   * @throws Exception
   */
  protected String getTokenString(String username, String password) throws Exception {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);

    // 呼び出し
    MvcResult result =
        mockMvc
            .perform(
                (MockMvcRequestBuilders.post("/login"))
                    // csrfトークンをsetする
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(new ObjectMapper().writeValueAsString(user)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    // token取得
    return result.getResponse().getHeader(SecurityConstants.HEADER_STRING);
  }
}
