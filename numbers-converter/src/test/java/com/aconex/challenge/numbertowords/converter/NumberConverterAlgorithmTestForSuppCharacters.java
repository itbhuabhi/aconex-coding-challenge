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
 * Tests if the algorithm works fine if the words in dictionary have characters outside the Basic Multilingual Plane (BMP)
 * As such the onus is on the {@link WordToNumberConverter} to support that, but writing a simple test that it does not fall on its face when dealing with them.
 * @author Abhishek Agarwal
 *
 */

public class NumberConverterAlgorithmTestForSuppCharacters extends NumberConverterAlgorithmTestBase{

	@Before
	public void setUp() throws Exception {
		Configuration mockConfig = mock(Configuration.class);
		when(mockConfig.retainConsecutiveUnmatchedDigitsAsIs()).thenReturn(new int[] {1});
		when(mockConfig.wordConcatenateDelimiter()).thenReturn("-");
		Configuration.setInstance(mockConfig);
	}

	@Test
	public void test_dictionaryWord_with_supplementary_char() {
		//This implies that dictionary has only one word C𐐀LL and character 𐐀  maps to digit 2
		Dictionary dictionary = mockDictionary("2255->C𐐀LL");

		NumberConverterAlgorithm converter = getAlgoInstance(dictionary);

		String inputNumber = "2255";
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("C𐐀LL"));
		Set<String> actualMatchingWords = converter.convertNumber(inputNumber);
		assertEquals(expectedWords, actualMatchingWords);
	}

	@Override
	protected NumberConverterAlgorithm getAlgoInstance(Dictionary dictionary) {
		return new NumberConverterAlgorithm(dictionary);
	}

}
