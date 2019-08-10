package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Authority;

import org.junit.Test;

public class CheckTests {

	@Test
	public void test01() {
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");

		String[] array=(String[])list.toArray(new String[0]);

		System.out.println(array);
	}

	@Test
	public void test02() {
		List<Authority> list = new ArrayList<>();
		list.add(new Authority(){{setAuthority("a");}});
		list.add(new Authority(){{setAuthority("b");}});
		list.add(new Authority(){{setAuthority("c");}});

		// String[] array=(String[])list.toArray(new String[0]);
		String[] array1 = list.stream().map(v -> v.getAuthority()).collect(Collectors.toList()).toArray(new String[0]);
		List<String> array = list.stream().map(v -> v.getAuthority()).collect(Collectors.toList());
		String[] arrays=(String[])array.toArray(new String[0]);

		System.out.println(array1);
		System.out.println(arrays);
	}

}
