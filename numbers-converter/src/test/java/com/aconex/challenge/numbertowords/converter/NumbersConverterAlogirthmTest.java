package com.aconex.challenge.numbertowords.converter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aconex.challenge.numbertowords.config.Configuration;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;

/*
 * It is verifying if the core algorithm is working fine for different use cases.<br>
 * To test the algorithm we mock the {@link Dictionary dictionary} which is provided as an input to the dictionary.<br>
 * While mocking the dictionary this class assumes default number encoding which is <br>
 * 
 * A,B,C -> 2	<br>
 * D,E,F -> 3	<br>
 * G,H,I -> 4 	<br>
 * J,K,L -> 5 	<br>
 * M,N,O -> 6 	<br>
 * P,Q,R,S -> 7 <br>
 * T,U,V -> 8	<br>
 * W,X,Y,Z -> 9	<br><br>
 * 
 * Based on the above encoding, the number 2255 could map to a word like CALL, BALL, AALL, AAJJ and other permutations
 * and we can chose to include any of these combinations in the dictionary
 * 
 * @author Abhishek Agarwal
 *
 */



public class NumbersConverterAlogirthmTest extends NumberConverterAlgorithmTestBase{
	
	
	/*
	 * Since the dictionary gets the delimitter and the max number of digits which can be retained as-is from the Configuration so we would need to mock the Configuration
	 * The Unit tests in this class allows only 1 digit to be retained as-is which is the default as well.
	 */

	@Before
	public void setUp() throws Exception {
		Configuration mockConfig = mock(Configuration.class);
		when(mockConfig.retainConsecutiveUnmatchedDigitsAsIs()).thenReturn(new int[] {1});
		when(mockConfig.wordConcatenateDelimiter()).thenReturn("-");
		Configuration.setInstance(mockConfig);
	}
	
	
	


	//This is the very basic test which would help to create initial design and help build further complicated test cases
	@Test
	public void test_successfulNumberMapping_to_exactlyOneWord() {
		//This implies that dictionary has only one word CALL
		Dictionary dictionary = mockDictionary("2255->CALL");

		NumberConverterAlgorithm converter = (NumberConverterAlgorithm)getAlgoInstance(dictionary);

		String inputNumber = "2255";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	//This test case would take care of numbers where substrings of the input number matches exactly one dictionary word 
	//i.e. if a part of word matches it would create dictionay such that no subsequence of the sequence matches
	//Ex-> 2255.63 maps to CALL-ME. Words in the dictionary are CALL and ME and no other part of the number matches a word in dictionary


	@Test
	public void test_successfulNumberMapping_matching_oneWord() {
		//This implies that dictionary has words CALL and ME
		Dictionary dictionary = mockDictionary("2255->CALL","63->ME");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "225563";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	//Now the dictionary have words like CALL,BALL and ME where CALL and BALL both maps to 2255
	@Test
	public void test_successfulNumberMapping_matching_multipleWords() {
		//This implies that dictionary has words CALL , BALL and ME
		Dictionary dictionary = mockDictionary("2255->CALL,BALL","63->ME");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "225563";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL-ME","BALL-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	//Now the dictionary have words like CALL,BALL and ME where CALL and BALL both maps to 2255
	//And furthermore there are words like B and ALL. So 2255 would have exact matches CALL, BALL, B-ALL
	@Test
	public void test_successfulNumberMapping_with_overlapping() {
		//This implies that dictionary has word CALL , BALL , ME , B and ALL
		Dictionary dictionary = mockDictionary("2255->CALL,BALL","63->ME","2->B","255->ALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "225563";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL-ME","BALL-ME","B-ALL-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}
	//Dictionary has ALL and ME so for input number 225563 there would be one unchanged digit, 
	//because no consecutive digits along with the first digit '2' maps to a word
	@Test
	public void test_successfulNumberMapping_with_oneDigitUnchanged() {
		//This implies that dictionary has words ME and ALL
		Dictionary dictionary = mockDictionary("63->ME","255->ALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "225563";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("2-ALL-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	//This test asserts that partial matches(some digits unchanged) are only returned when there is no exact match(all digits changed)
	//Dictionary has BALL, ALL and ME so for input number 225563 there would be one unchanged digit
	// So now for input 225563 it should not return 2-ALL-ME because it has an exact match as BALL-ME
	@Test
	public void test_successfulNumberMapping_returns_exactMatch_Only() {
		//This implies that dictionary has words ME and ALL
		Dictionary dictionary = mockDictionary("63->ME","255->ALL", "2255->BALL");

		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "225563";
		Set<String> expectedWords = new HashSet<String>(
				Arrays.asList("BALL-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}
	
	
	//Tests for scenarios where total no. of digits unchanged is more than 1, but these digits can not be consecutive
	//Dictionary has ALL and ME so for input number 2255263 there would be 2 unchanged digits which are not consecutive
	@Test
	public void test_successfulNumberMapping_with_multiDigitUnchanged() {

		//This implies that dictionary has words ME and ALL
		Dictionary dictionary = mockDictionary("63->ME","255->ALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "2255263";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("2-ALL-2-ME"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}
	// Tests for scenarios where there are subsequences in the number which map to exact dictionary words, 
	// but to get a matching combination more than one consecutive digits need to remain un-changed, hence should not return any match. 
	// For an input number like 222552263 with dictionary words being ME and ALL, it could map to 22-ALL-22-ME, 
	// But since it violates the rule of consecutive digits unchanged, so eventually no match should be returned.
	// 
	@Test
	public void test_unsuccessfulMatch() {
		
		//This implies that dictionary has words ME and ALL
		Dictionary dictionary = mockDictionary("63->ME","255->ALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);
		String inputNumber = "222552263";
		Set<String> expectedWords = new HashSet<String>();
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}



	protected Dictionary mockDictionary(String... numberToWordsArr) {
		Dictionary dictionary = mock(Dictionary.class);
		Arrays.stream(numberToWordsArr).forEach((numberToWords)-> {
			String[] parts = numberToWords.split("->");
			when(dictionary.findMatchingWords(parts[0])).thenReturn(new HashSet<String>(Arrays.asList(parts[1].split(","))));
		});
		return dictionary;
	}

	@Override
	protected NumberConverterAlgorithm getAlgoInstance(Dictionary dictionary) {
		return new NumberConverterAlgorithm(dictionary);
	}

}
