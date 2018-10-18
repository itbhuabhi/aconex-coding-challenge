package com.aconex.challenge.numbertowords.converter;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.aconex.challenge.numbertowords.dictionary.Dictionary;
import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;

public abstract class NumberConverterAlgorithmTestBase {

	
	/*
	 * Mocks the {@link Dictionary} object 
	 * @param numberToWordsArr Each string is a number mapping to possible words based on a chosen number encoding, and the given words are present in the list of dictionary words provided.
	 *  Ex-> If an input is 2255->CALL,BALL This means 2255 would encode to CALL and BALL, and the user dictionary has words CALL and BALL in it.
	 * @return
	 */


	protected Dictionary mockDictionary(String... numberToWordsArr) {
		Dictionary dictionary = mock(Dictionary.class);
		Arrays.stream(numberToWordsArr).forEach((numberToWords)-> {
			String[] parts = numberToWords.split("->");
			when(dictionary.findMatchingWords(parts[0])).thenReturn(new HashSet<String>(Arrays.asList(parts[1].split(","))));
		});
		return dictionary;
	}
	
	protected abstract InputTransformer<Set<String>> getAlgoInstance(
			Dictionary dictionary);

}
