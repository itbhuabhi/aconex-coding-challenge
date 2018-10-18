package com.aconex.challenge.numbertowords;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.config.Configuration;
import com.aconex.challenge.numbertowords.converter.NumberConverterAlgorithm;
import com.aconex.challenge.numbertowords.converter.NumbersConverter;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;
import com.aconex.challenge.numbertowords.dictionary.DictionaryContainer;
import com.aconex.challenge.numbertowords.dictionary.HashBasedDictionaryContainer;
import com.aconex.challenge.numbertowords.dictionary.NumbersEncodingParser;
import com.aconex.challenge.numbertowords.dictionary.WordToNumberConverter;
import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.StripAndValidateInput;
import com.aconex.challenge.numbertowords.dictionary.transformers.UpperCaseTransformer;

public class ApplicationFacade {
	
	private DictionaryContainer dictionaryContainer;
	
	private NumbersConverter numbersConverter;
	
	public ApplicationFacade() {
		initDictionaryContainer();
	}

	public DictionaryContainer getDictionaryContainer() {
		return dictionaryContainer;
	}

	public NumbersConverter getNumbersConverter() {
		return numbersConverter;
	}

	
	

	protected void initDictionaryContainer() {
		Map<String, String> numbersEncodingMap = getNumbersEncodingMap();
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String dictValidRegex = Configuration.getInstance().dictValidRegex();
		InputTransformer<String> wordToNumConverter = new WordToNumberConverter(new UpperCaseTransformer(new StripAndValidateInput(stripCharactersRegex, dictValidRegex)), numbersEncodingMap);
		
		dictionaryContainer = new HashBasedDictionaryContainer(wordToNumConverter);
	}
	
	protected Map<String, String> getNumbersEncodingMap() {
		NumbersEncodingParser numbersEndcodingParser = new NumbersEncodingParser();
		return numbersEndcodingParser.getCharToDigitEncoding();
	}

	public void createAndPopulateDictionary(List<Stream<String>> dictionarySources) {
		dictionaryContainer.createAndPopulateDictionary(dictionarySources);

	}
	
	protected void initNumbersConverter() {
		String stripCharactersRegex = Configuration.getInstance().stripCharactersRegex();
		String numberValidRegex = Configuration.getInstance().numberValidRegex();
		Dictionary dictionary = dictionaryContainer.getDictionary();
		InputTransformer<Set<String>> numberConverterAlgorithm = new NumberConverterAlgorithm(new StripAndValidateInput(stripCharactersRegex,numberValidRegex),dictionary);

		numbersConverter = new NumbersConverter(numberConverterAlgorithm);
		
	}
	
	
	
	





}
