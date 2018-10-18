package com.aconex.challenge.numbertowords.converter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.aconex.challenge.numbertowords.config.Configuration;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;

/*
 * Tests for numbers which gets a match by retaining more than 1 consecutive digits as-is
 * Similar to {@link NumbersConverterAlogirthmTest}, mocking of dictionary assumes default number encoding. 
 * @author aagarwal
 *
 */

public class NumConverterAlgoTestForMultiUnchangedDigits extends NumberConverterAlgorithmTestBase{

	/*
	 * Tests for a number which would result in 2 consecutive digits unchanged.
	 * The configuration has to be mocked to change from default of 1.
	 * So for a number 22551225512 with dictionary having only word CALL(2255), it would result in word CALL-63-CALL
	 */
	@Test
	public void test_successfulNumberMapping_with_twoDigitUnchanged() {
		mockConfiguration(new int[]{2});
		//This implies that dictionary has only word CALL
		Dictionary dictionary = mockDictionary("2255->CALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "2255632255";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL-63-CALL"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	/*
	 * Tests for a number which would have one digit unchanged in isolation and another sequence which would have 2 digit in isolation.
	 * But the configuration is set such that it can not have any single unchanged digit in islolation, so eventually there should be no match.
	 * So for a number 22551225512 with dictionary having only word CALL(2255), it would result in word CALL-1-CALL-12,
	 * But since one digit in isolation is not allowed hence there should not be any match.
	 */


	@Test
	public void test_unsuccessfulNumberMapping_with_twoDigitUnchanged() {
		mockConfiguration(new int[]{2});
		//This implies that dictionary has only word CALL
		Dictionary dictionary = mockDictionary("2255->CALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "22551225512";
		Set<String> expectedWords = new HashSet<String>();
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	/*
	 * Tests for a number which would have one digit unchanged in isolation and another sequence which would have 2 digit in isolation.
	 * And the configuration is set, such that it allows 1 and 2 consecutive digits to remain as-is
	 * So for a number 22551225512 with dictionary having only word CALL(2255), it would result in a succesful match of word CALL-1-CALL-12,
	 */

	

	@Test
	public void test_successfulNumberMapping_with_MaxTwoDigitUnchanged() {
		mockConfiguration(new int[]{1, 2});

		//This implies that dictionary has words CALL and A
		Dictionary dictionary = mockDictionary("2255->CALL");
		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "22551225512";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("CALL-1-CALL-12"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	private void mockConfiguration(int[] unchangedDigitsArr) {
		Configuration mockConfig = mock(Configuration.class);
		when(mockConfig.retainConsecutiveUnmatchedDigitsAsIs())
				.thenReturn(unchangedDigitsArr);
		when(mockConfig.wordConcatenateDelimitter()).thenReturn("-");
		Configuration.setInstance(mockConfig);
	}


	@Override
	protected NumberConverterAlgorithm getAlgoInstance(Dictionary dictionary) {
		return new NumberConverterAlgorithm(dictionary);
	}

}
