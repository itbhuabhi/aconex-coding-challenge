package com.aconex.challenge.numbertowords.dictionary;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aconex.challenge.numbertowords.CommonTestBase;

/*
 * The word given by a user dictionary is converted to a number based on a number encoding, 
 * before storing the number and its word representation(s) in the {@link com.aconex.challenge.numbertowords.dictionary.Dictionary Dictionary}
 * The class {@link WordToNumberConverter} does this transformation, so this class makes sure all legal words are correctly transformed.
 * This class also tests for characters outside the Basic Multilingual Plane (BMP), which has to be supported by the number encoding as well.
 * @author Abhishek Agarwal
 *
 */

public class WordToNumberConverterTest extends CommonTestBase {


	/*
	 * Basic test for a simple word for default number encoding.
	 */
	@Test
	public void test_Simple_Word() {
		WordToNumberConverter wordToNumConverter = new WordToNumberConverter(defaultNumberEncodingMap());

		String numRepresentationOfWord = wordToNumConverter.convertWordToNumber("ABHISHEK");
		String expected = "22447435";
		assertEquals(expected, numRepresentationOfWord);

	}

	/*
	 * Makes sure no character is missed.
	 */
	@Test
	public void test_Word_With_All_English_Alpbhabets() {
		WordToNumberConverter wordToNumConverter = new WordToNumberConverter(defaultNumberEncodingMap() );
		String numRepresentationOfWord = wordToNumConverter.convertWordToNumber("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String expected = "22233344455566677778889999";
		assertEquals(expected, numRepresentationOfWord);
	}



	/*
	 * Tests for a character not supported by the number encoding.
	 */
	@Test(expected = NullPointerException.class)
	public void test_Word_With_Some_Characters_With_No_Mapping() {
		WordToNumberConverter wordToNumConverter = new WordToNumberConverter(defaultNumberEncodingMap());
		wordToNumConverter.convertWordToNumber("ABCeF");
	}
	
	/*
	 * Tests for a supplementary character supported by the number encoding.
	 */

	@Test
	public void test_Word_With_Supplementary_Character() {
		WordToNumberConverter wordToNumConverter = new WordToNumberConverter(supplementaryCharsMap());
		String numRepresentationOfWord = wordToNumConverter.convertWordToNumber("C𐐀LL");
		String expected = "2255";
		assertEquals(expected, numRepresentationOfWord);
	}


}
