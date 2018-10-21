package com.aconex.challenge.numbertowords.converter;

import java.util.HashSet;
import java.util.Set;

import com.aconex.challenge.numbertowords.config.Configuration;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;
import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.TransformerContainer;
import com.aconex.challenge.numbertowords.util.CollectionsUtil;
/**
 * This is the one of the core class which is fed with the dictionary and converts a number to a set of matching word combinations.
 * The algorithm uses a combination of recursive and iterative approach. It is outlined as
 * Iterate through the length  of the number string and get a prefix string and suffix string at each iteration.
 * 
 * <p>So say if the number string is 2255. At each iteration the prefix and suffix would be <br>
 * Iteration-1:: Prefix: 2, Suffix: 255<br>
 * Iteration-1:: Prefix: 22, Suffix: 55<br>
 * Iteration-1:: Prefix: 225, Suffix: 5<br>
 * Iteration-1:: Prefix: 2255, Suffix: Empty<br>
 * The algorithm will then search for an exact match of the prefix in the dictionary.
 * If there is an exact match found, make a recursive call to get all the matching combinations for the suffix string 
 * and concatenate each combination with each exact dictionary match for the prefix, separated by a delimiter.<br>
 * So in the above example for Iteration-1, say if Prefix: 2 matches word 'A', 
 * we would make a recursive call to get all the matching combinations for Suffix: 255
 * 
 * <p>We are allowed to retain digit(s) as-is even if there is no match. The possible number of consecutive digits which can be retained as-is
 * is retrieved from the Configuration. The initial requirement was to retain only 1 digit, but we can configure it to retain 1,2,3 or any number of consecutive digits as-is without any match.
 * Just the configuration element needs to list it down. So say if we want to retain 1 and 3 consecutive digits as-is even with out any match, 
 * then the configuration element "consecutive.unmatched.digits" needs to have value as 1,3. 
 * 
 * <p>If there is no exact match found for the prefix<br>
 * - Retain prefix as-is<br> 
 * 	&nbsp;&nbsp;&nbsp;- If the length of the prefix is one of the allowed number of consecutive digits to be retained as-is.<br>
 *  &nbsp;&nbsp;&nbsp;- The immediate previous prefix string was not retained as-is.<br>
 *  
 *  <p>And then make a recursive call to get all the matching combinations for the suffix and concatenate it with the prefix.
 *  
 *  <p>To the recursive call for the suffix, we would need to pass a boolean value if the prefix had exact match or not, 
 *  so that no more than the allowed digits remain as-is.
 *  Furthermore, the concatenation of matching combinations from prefix and suffix are stored as two sets. 
 *  One with exact matches i.e. all digits repalced 
 *  and other with partial matches where atleast one digit remains as-is. 
 *  This is useful when we are returning the matches for the initial number string,
 *  because we return partial matches only if there is no exact match. 
 * 
 * @author Abhishek Agarwal
 *
 */

public class NumberConverterAlgorithm implements InputTransformer<Set<String>> {
	/**
	 * The dictionary which need to be used for searching for matches.
	 */
	private Dictionary dictionary;
	
	/**
	 * Delimiter to be used to concatenate matching words, which is determined by configuration element concatenate.delimtter. Defaults to hyphen
	 */

	private final String delimiter = Configuration.getInstance().wordConcatenateDelimiter();
	
	/**
	 * No. of consecutive digits which can remain as-is without a matching word. Determined by configuration element unchangedigits.list
	 */
	
	private final int[] retainConsecutiveUnmatchedDigits = Configuration.getInstance().retainConsecutiveUnmatchedDigitsAsIs();
	
	/**
	 * To apply any massaging to the number to be transformed. 
	 * For example if the number given 2255-63, we would first need it to convert it to 225563, before searching for its matches.
	 */
	
	private InputTransformer<String> parentTransformer;
	
	/**
	 * Constructor to chain it with a parent transformer and initialize with the dictionary which is to be used.
	 * @param parentTransformer Parent transformer to apply any massaging to the number to be transformed, before the search begins. 
	 * @param dictionary Dictionary to be used for searching matching combinations of each sub-sequence of the number string.
	 */
	
	public NumberConverterAlgorithm(InputTransformer<String> parentTransformer, Dictionary dictionary) {
		this.parentTransformer = parentTransformer;
		this.dictionary = dictionary;
	}

	/**
	 * Initializes with the dictionary to be used for searching matching combinations.
	 * @param dictionary Dictionary to be used for searching matching combinations of each sub-sequence of the number string.
	 */
	public NumberConverterAlgorithm(Dictionary dictionary ) {
		this.dictionary = dictionary;
	}
	/**
	 * Applies any pre-massaging to be done on the number string, and then searches for all the matching combinations.
	 * @param numberContainer Transformer Container which has the number string needs to be converted, and is fed with the set of matching combinations.
	 */

	@Override
	public void transform(TransformerContainer<Set<String>> numberContainer) {
		String numberToConvert;
		if(parentTransformer != null) {
			String numberInRawForm = numberContainer.getInput();
			TransformerContainer<String> rawNumberTransformerContainer = new TransformerContainer<String>(numberInRawForm);
			parentTransformer.transform(rawNumberTransformerContainer);
			if( !CollectionsUtil.isNullOrEmpty(rawNumberTransformerContainer.getErrors())) {
				numberContainer.setErrors(rawNumberTransformerContainer.getErrors());
				return;
			}
			numberToConvert = rawNumberTransformerContainer.getTransformed();
			
		}
		else {
			numberToConvert = numberContainer.getInput();
		}
		numberContainer.setTransformed(convertNumber(numberToConvert));
		
	}
	
	/**
	 * Utility method which takes the number string whose matching combinations are to be found, 
	 * and returns the matching combinations.
	 * Lot of unit testing for this class can be covered by writing Unit tests against this method.
	 * @param numberToConvert The number string whose matching combinations are to be found
	 * @return Returns the matching combinations
	 */

	protected Set<String> convertNumber(String numberToConvert) {
		return findMatchingCombinations(numberToConvert,true).getMatches();
	}
	
	/**
	 * Internal method which is called recursively for the suffix string to find the matching combinations of a number string
	 * @param numberString Number string whose matching combinations is to be found.
	 * @param previousPrefixHasDictMatches This is true if the previous Prefix which made the recursive call had an exact dictionary match.
	 * @return Returns an {@link NumberConverterResultAccumulator} which stores the matching combinations for each recursive call.
	 */
	
	private NumberConverterResultAccumulator findMatchingCombinations(String numberString, boolean previousPrefixHasDictMatches){
		int numberStrLength = numberString.length();
		// System.out.println(Arrays.toString(retainConsecutiveUnmatchedDigits));
		NumberConverterResultAccumulator result = new NumberConverterResultAccumulator();
		for(int index =0; index< numberStrLength;index++) {
			// System.out.println(retainConsecutiveUnmatchedDigits);
			boolean prefixHasDictMatches = true;
			String numberPrefix = numberString.substring(0, index+1);
			Set<String> prefixMatches = dictionary.findMatchingWords(numberPrefix);
			if (previousPrefixHasDictMatches && CollectionsUtil.isNullOrEmpty(prefixMatches)
					&& CollectionsUtil.anyMatch(retainConsecutiveUnmatchedDigits, index + 1)) {
				prefixMatches = new HashSet<String>();
				prefixMatches.add(numberPrefix);
				prefixHasDictMatches = false;
			}
			// Go for a recursive suffix call only if 
			// 1. prefix has some match either from the dictionary or by keeping the digit(s) as-is
			// 2. And we have not reached the end of String, which is also the end of recursion in worst-case scenario
			if(!CollectionsUtil.isNullOrEmpty(prefixMatches) && index < numberStrLength-1) {
				String numberSuffix = numberString.substring(index+1);
				NumberConverterResultAccumulator suffixMatchResult = findMatchingCombinations(numberSuffix, prefixHasDictMatches);
				Set<String> suffixExactMatches = suffixMatchResult.getExactDictionaryMatches();
				Set<String> suffixPartialMatches = suffixMatchResult.getPartialDictionaryMatches();
				if(prefixHasDictMatches) {
					result.concatenatePrefixAndSuffixMatches(prefixMatches, suffixExactMatches, false);
					result.concatenatePrefixAndSuffixMatches(prefixMatches, suffixPartialMatches, true);
				}
				else {
					result.concatenatePrefixAndSuffixMatches(prefixMatches, suffixExactMatches, true);
					result.concatenatePrefixAndSuffixMatches(prefixMatches, suffixPartialMatches, true);
				}
			}
			else if(!CollectionsUtil.isNullOrEmpty(prefixMatches) && index == numberStrLength-1) {
				result.addMatches(prefixMatches, !prefixHasDictMatches);
			}
		}
		return result;
	}


	/**
	 * Utility class which stores the matching combinations for each recursive call for the suffix string.
	 */
	private class NumberConverterResultAccumulator {

		/**
		 * Set of exact matches i.e. the match does not contain any digit.
		 */
		private Set<String> exactDictionaryMatches;
		/**
		 * Set of partial matches. i.e. each match in the set has atleast one digit unchanged.
		 */
		private Set<String> partialDictionaryMatches;

		private NumberConverterResultAccumulator() {
			exactDictionaryMatches = new HashSet<String>();
			partialDictionaryMatches = new HashSet<String>();
		}

		/**
		 * This method would expect two sets prefixMatches and suffixMatches. 
		 * The matches from two sets are concatenated separated by a delimiter.
		 * Each set will have either all Partial Matches or all Complete Matches, but if any of the
		 * set is of partial matches then the concatenated strings would go in the partialDictionaryMatches
		 * 
		 * @param prefixMatches Set of matching replacement words for the prefix of a number
		 * @param suffixMatches Set of matching replacement words for the suffix of a number
		 * @param hasPartialMatches If either of the set has partial dictionary matches, this value has to be passed as true
		 */
		private void concatenatePrefixAndSuffixMatches(Set<String> prefixMatches, Set<String> suffixMatches, boolean hasPartialMatches) {
			if (CollectionsUtil.isNullOrEmpty(prefixMatches) || CollectionsUtil.isNullOrEmpty(suffixMatches))
				return;
			for (String prefixMatch : prefixMatches) {
				for (String suffixMatch : suffixMatches) {
					StringBuilder concatenatedString = new StringBuilder();
					concatenatedString.append(prefixMatch).append(delimiter).append(suffixMatch);
					if (hasPartialMatches) {
						partialDictionaryMatches.add(concatenatedString.toString());
					} else {
						exactDictionaryMatches.add(concatenatedString.toString());
					}
				}
			}

		}

		/**
		 * This is a method to store the matches as is.
		 * @param matches Matches to be stored.
		 * @param hasPartialMatches If either of the set has partial dictionary matches, this value has to be passed as true
		 */
		private void addMatches(Set<String> matches, boolean hasPartialMatches) {
			if (!exactDictionaryMatches.isEmpty() && hasPartialMatches)
				return;
			if (CollectionsUtil.isNullOrEmpty(matches) || CollectionsUtil.isNullOrEmpty(matches))
				return;
			if (hasPartialMatches) {
				partialDictionaryMatches.addAll(matches);
			} else {
				exactDictionaryMatches.addAll(matches);
			}

		}

		/**
		 * 
		 * @return Set of Exact dictionary matches
		 */
		private Set<String> getExactDictionaryMatches() {
			return exactDictionaryMatches;
		}

		/**
		 * 
		 * @return Set of partial dictionary matches
		 */
		private Set<String> getPartialDictionaryMatches() {
			return partialDictionaryMatches;
		}
		
		/**
		 * @return Returns set of matches. Only if there is no exact match, the set of partial matches is returned, else the set of exact matches is returned.
		 */

		private Set<String> getMatches() {
			if (exactDictionaryMatches.size() > 0)
				return exactDictionaryMatches;
			else
				return partialDictionaryMatches;
		}

	}
}
