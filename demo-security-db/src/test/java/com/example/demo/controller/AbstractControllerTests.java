package com.example.demo.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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

  // @Autowired protected TestInitializer testInitializer;

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

    // testInitializer.initIncrement();
  }
}
