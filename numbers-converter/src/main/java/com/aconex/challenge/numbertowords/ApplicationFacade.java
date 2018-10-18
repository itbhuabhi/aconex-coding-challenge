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


public class ApplicationFacade {
	
	private DictionaryFactory dictionaryFactory;
	
	private NumbersConverter numbersConverter;
	
	public ApplicationFacade() {
		initDictionaryFactory();
	}


	public NumbersConverter getNumbersConverter() {
		return numbersConverter;
	}

	
	

	protected void initDictionaryFactory() {
		Map<String, String> numbersEncodingMap = getNumbersEncodingMap();
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String dictValidRegex = Configuration.getInstance().dictValidRegex();
		InputTransformer<String> wordToNumConverter = new WordToNumberConverter(new UpperCaseTransformer(new StripAndValidateInput(stripCharactersRegex, dictValidRegex)), numbersEncodingMap);
		
		dictionaryFactory = new HashBasedDictionaryFactory(wordToNumConverter);
	}
	
	protected Map<String, String> getNumbersEncodingMap() {
		NumbersEncodingParser numbersEndcodingParser = new NumbersEncodingParser();
		return numbersEndcodingParser.getCharToDigitEncoding();
	}

	public void createAndPopulateDictionary(List<Stream<String>> dictionarySources) {
		dictionaryFactory.createAndPopulateDictionary(dictionarySources);

	}
	
	protected void initNumbersConverter() {
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String numberValidRegex = Configuration.getInstance().numberValidRegex();
		Dictionary dictionary = dictionaryFactory.getDictionary();
		InputTransformer<Set<String>> numberConverterAlgorithm = new NumberConverterAlgorithm(new StripAndValidateInput(stripCharactersRegex,numberValidRegex),dictionary);

		numbersConverter = new NumbersConverter(numberConverterAlgorithm);
		
	}
}
