package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.User;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Test
	public void login_error_Test() throws Exception {
		List<Object[]> params = new ArrayList<>();
		params.add(new Object[] { "root", "hoge", "パスワード誤り" });
		params.add(new Object[] { "root", "", "パスワード空白" });
		params.add(new Object[] { "hoge", "password", "存在しないユーザー" });
		params.add(new Object[] { "", "password", "ユーザー名空白" });
		params.add(new Object[] { "", "", "ユーザー名&パスワード空白" });

		for (Object[] p : params) {
			String username = (String) p[0];
			String password = (String) p[1];

			User user = new User();
			user.setUsername(username);
			user.setPassword(password);

			// 呼び出し
			mockMvc.perform((MockMvcRequestBuilders.post("/login"))
					// csrfトークンをsetする
					.with(SecurityMockMvcRequestPostProcessors.csrf()).content(new ObjectMapper().writeValueAsString(user)))
					.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn();
		}
	}

	// todo:作成中
	// 
	// @Test
	// public void private_page_access_ng_Test() throws Exception {
	// 	List<Object[]> params = new ArrayList<>();
	// 	params.add(new Object[] { null, "token null" });
	// 	params.add(new Object[] { "", "token 空白" });
	// 	params.add(new Object[] { "hogehoge", "token 不正" });

	// 	for (Object[] p : params) {
	// 		String token = (String) p[0];

	// 		// private page呼び出し
	// 		MvcResult result2 = mockMvc
	// 				.perform((MockMvcRequestBuilders.get("/private").header(SecurityConstants.HEADER_STRING, token))
	// 						.with(SecurityMockMvcRequestPostProcessors.csrf()))
	// 				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

	// 		// body検証
	// 		String content = result2.getResponse().getContentAsString();
	// 		Assert.assertEquals("this is private page!", content);
	// 	}
	// }

	@Test
	public void private_page_access_ok_Test() throws Exception {
		List<Object[]> params = new ArrayList<>();
		params.add(new Object[] { "root", "password" });
		params.add(new Object[] { "user", "password" });
		params.add(new Object[] { "guest", "password" });

		for (Object[] p : params) {
			String username = (String) p[0];
			String password = (String) p[1];

			String token = getTokenString(username, password);

			// private page呼び出し
			MvcResult result2 = mockMvc
					.perform((MockMvcRequestBuilders.get("/private").header(SecurityConstants.HEADER_STRING, token))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

			// body検証
			String content = result2.getResponse().getContentAsString();
			Assert.assertEquals("this is private page!", content);
		}
	}

	private String getTokenString(String username, String password) throws Exception {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		// 呼び出し
		MvcResult result = mockMvc.perform((MockMvcRequestBuilders.post("/login"))
				// csrfトークンをsetする
				.with(SecurityMockMvcRequestPostProcessors.csrf()).content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		// token取得
		return result.getResponse().getHeader(SecurityConstants.HEADER_STRING);
	}
}
