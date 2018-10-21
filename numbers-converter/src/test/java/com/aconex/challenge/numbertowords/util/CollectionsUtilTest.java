package com.aconex.challenge.numbertowords.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class CollectionsUtilTest {

	@Test
	public void test_null_Collection() {
		assertTrue(CollectionsUtil.isNullOrEmpty((Collection<String>)null));
	}
	
	@Test
	public void test_Empty_Collection() {
		assertTrue(CollectionsUtil.isNullOrEmpty(new HashSet<String>()));
	}
	
	@Test
	public void test_NonEmpty_Collection() {
		Set<String> set = new HashSet<String>();
		set.add("Any random object 1");
		set.add("Any random object 2");
		assertFalse(CollectionsUtil.isNullOrEmpty(set));
	}
	
	@Test
	public void test_null_Map() {
		assertTrue(CollectionsUtil.isNullOrEmpty((Map<String,String>)null));
	}
	
	@Test
	public void test_Empty_Map() {
		assertTrue(CollectionsUtil.isNullOrEmpty(new HashMap<String,String>()));
	}
	
	@Test
	public void test_NonEmpty_Map() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("RandomKey1","RandomValue1");
		map.put("RandomKey2","RandomValue2");
		assertFalse(CollectionsUtil.isNullOrEmpty(map));
	}
	
	@Test
	public void test_anyMatch_One_Elem_Arr() {
		int[] inputArr = new int[] {1};
		assertTrue(CollectionsUtil.anyMatch(inputArr,1));
	}
	
	@Test
	public void test_anyMatch_Arr_With_Duplicates() {
		int[] inputArr = new int[] {1,2,3,3,2};
		assertTrue(CollectionsUtil.anyMatch(inputArr,2));
	}
	
	@Test
	public void test_anyMatch_Empty_Arr() {
		int[] inputArr = new int[] {};
		assertFalse(CollectionsUtil.anyMatch(inputArr,2));
	}
	
	@Test
	public void test_anyMatch_Null_Arr() {
		assertFalse(CollectionsUtil.anyMatch(null,2));
	}

}
