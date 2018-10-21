package com.aconex.challenge.numbertowords.util;

import static org.junit.Assert.*;


import org.junit.Test;

public class StringUtilTest {

	@Test
	public void test_null_String() {
		assertTrue(StringUtil.isBlankOrNull(null));
	}
	
	@Test
	public void test_Empty_String() {
		assertTrue(StringUtil.isBlankOrNull(""));
	}
	
	@Test
	public void test_String_With_Whitespaces() {
		assertTrue(StringUtil.isBlankOrNull(" 	"));
	}
	
	@Test
	public void test_NonEmpty_String() {
		assertFalse(StringUtil.isBlankOrNull(" Random String "));

	}

}
