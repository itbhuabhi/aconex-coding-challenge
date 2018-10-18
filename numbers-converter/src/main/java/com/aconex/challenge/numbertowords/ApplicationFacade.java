package com.aconex.challenge.numbertowords;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.config.Configuration;
import com.aconex.challenge.numbertowords.converter.NumberConverterAlgorithm;
import com.aconex.challenge.numbertowords.converter.NumbersConverter;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;
import com.aconex.challenge.numbertowords.dictionary.DictionaryFactory;
import com.aconex.challenge.numbertowords.dictionary.HashBasedDictionaryFactory;
import com.aconex.challenge.numbertowords.dictionary.NumbersEncodingParser;
import com.aconex.challenge.numbertowords.dictionary.WordToNumberConverter;
import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.StripAndValidateInput;
import com.aconex.challenge.numbertowords.dictionary.transformers.UpperCaseTransformer;

/**
 * Application Facade which becomes the integration point of the application.
 * @author Abhishek Agarwal
 *
 */
public class ApplicationFacade {
	
	private DictionaryFactory dictionaryFactory;
	
	private NumbersConverter numbersConverter;
	
	/**
	 * Constructs the application facade which in turn eagerly initializes the dictionary factory with the {@link InputTransformer transformers} it would need
	 * while creating the dictionary.
	 */
	public ApplicationFacade() {
		initDictionaryFactory();
	}


	/**
	 * Returns the number converter which takes care of converting each element of the numbers stream.
	 * @return the number converter which takes care of converting each element of the numbers stream
	 */
	public NumbersConverter getNumbersConverter() {
		return numbersConverter;
	}

	/**
	 * 
	 * Instantiates the dictionary factory and other related beans.
	 */
	//The reason its access level is protected, because if ApplicationFacade is mocked then it can be explicitly called.
	protected void initDictionaryFactory() {
		Map<String, String> numbersEncodingMap = getNumbersEncodingMap();
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String dictValidRegex = Configuration.getInstance().dictValidRegex();
		InputTransformer<String> wordToNumConverter = new WordToNumberConverter(new UpperCaseTransformer(new StripAndValidateInput(stripCharactersRegex, dictValidRegex)), numbersEncodingMap);
		
		dictionaryFactory = new HashBasedDictionaryFactory(wordToNumConverter);
	}
	
	/**
	 * Returns Number encoding map.
	 * @return Number Encoding Map
	 */
	// Protected access so that it can be mocked for Unit testing. 
	// For Unit testing we can not rely on Configuration files.
	protected Map<String, String> getNumbersEncodingMap() {
		NumbersEncodingParser numbersEndcodingParser = new NumbersEncodingParser();
		return numbersEndcodingParser.getCharToDigitEncoding();
	}

	/**
	 * Creates and populates the dictionary. 
	 * @param dictionaryStreams List of dictionaryStreams
	 */
	public void createAndPopulateDictionary(List<Stream<String>> dictionaryStreams) {
		dictionaryFactory.createAndPopulateDictionary(dictionaryStreams);

	}
	
	/**
	 * Initializes the number converter algorithm with different transformers and the dictionary.
	 * Since the algorithm relies on dictionary, which is passed to it as constructor.
	 * This can only be called once the dictionary is populated.
	 */
	protected void initNumbersConverter() {
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String numberValidRegex = Configuration.getInstance().numberValidRegex();
		Dictionary dictionary = dictionaryFactory.getDictionary();
		InputTransformer<Set<String>> numberConverterAlgorithm = new NumberConverterAlgorithm(new StripAndValidateInput(stripCharactersRegex,numberValidRegex),dictionary);

		numbersConverter = new NumbersConverter(numberConverterAlgorithm);
		
	}
}
