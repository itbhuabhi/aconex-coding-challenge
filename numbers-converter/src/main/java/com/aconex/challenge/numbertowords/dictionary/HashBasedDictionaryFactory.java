package com.aconex.challenge.numbertowords.dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
/**
 * An implementation of {@link DictionaryFactory} which relies on hash based data structure for the {@link Dictionary}
 * @author Abhishek Agarwal
 *
 */

public class HashBasedDictionaryFactory extends DictionaryFactory {

	public HashBasedDictionaryFactory(InputTransformer<String> wordToNumberConverter) {
		super(wordToNumberConverter);
	}

	/**
	 * Implementation of the factory method to instantiate a concrete dictionary class
	 * @return Returns an empty Hash Based dictionary object
	 */
	@Override
	public Dictionary createDictionary() {
		return new HashBasedDictionary();
	}
	
	/**
	 * An inner class which is a concrete implementation of {@link Dictionary} relying on hash based data structure
	 */
	private class HashBasedDictionary implements Dictionary {
			private Map<String, Set<String>> map;
	
			public HashBasedDictionary() {
				map = new HashMap<String, Set<String>>();
			}
	
			@Override
			public void insert(String matchingNumber, String word) {
				Set<String> matchingWords;
				if (map.containsKey(matchingNumber)) {
					matchingWords = map.get(matchingNumber);
				} else {
					matchingWords = new HashSet<String>();
					map.put(matchingNumber, matchingWords);
				}
				matchingWords.add(word);
			}
	
			@Override
			public Set<String> findMatchingWords(String number) {
				return map.get(number);
	
			}
	
	}

}
