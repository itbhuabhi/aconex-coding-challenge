package com.aconex.challenge.numbertowords;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.aconex.challenge.numbertowords.converter.NumbersConverter;

public class ApplicationIntegrationTest extends CommonTestBase {

	
	@Before
	public void setUp() throws Exception {
		mockConfigurationWithDefaults();
	}


	private ApplicationFacade mockApplicationFacade() {
		ApplicationFacade applicationFacade = mock(ApplicationFacade.class,
				Mockito.CALLS_REAL_METHODS);
		when(applicationFacade.getNumbersEncodingMap())
				.thenReturn(defaultNumberEncodingMap());
		applicationFacade.initDictionaryFactory();
		return applicationFacade;

	}

	private List<Stream<String>> getDictionaryAndNumberStreams(String... strings) {
		if(strings == null)
			return Collections.singletonList(Stream.empty());
		return Collections.singletonList(Stream.of(strings));
	}
	
	// Tests for empty dictionary
	@Test(expected = IllegalArgumentException.class)
	public void test_For_Empty_Dictionary() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams(null));
	}


	// Tests for dictionary sources such that they don't have even a single legal word
	@Test(expected = IllegalArgumentException.class)
	public void test_For_Dictionary_With_No_Legal_Words() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("123", "1234!@"));
	}
	

	//No noise in the numbers and the words and words are in upper case.

	@Test
	public void test_For_Words_And_Numbers_With_No_Massaging() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("BLL", "ME", "ALL", "L"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams("225563");
		Set<String> expectedWords = new HashSet<String>(Arrays.asList("2-BLL-ME", "2-ALL-ME"));
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			Set<String> actualMatchingWords = numberContainer.getTransformed();
			assertEquals(expectedWords, actualMatchingWords);
		});
		
	}



	

	//Words and numbers after removing the noise are valid 

	@Test
	public void test_For_Words_And_Numbers_With_Punctuations_And_Whitespaces() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("BLL,!@#$%^&*()-+=~`|\\}]{[", "M E", "A	L L", "L"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams(
				"225 56	3,!@#$%^&*()-+=~`|\\\\}]{[");
		Set<String> expectedWords = new HashSet<String>(
				Arrays.asList("2-BLL-ME", "2-ALL-ME"));
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			Set<String> actualMatchingWords = numberContainer.getTransformed();
			assertEquals(expectedWords, actualMatchingWords);
		});

	}

	/**
	 * Tested for words with lower case characters. Retained the punctuations as well.
	 */
	@Test
	public void test_For_Words_With_Lower_Case_Characters() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("blL,!@#$%^&*()-+=~`|\\}]{[", "mE", "ALl", "l"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams("225563,!@#$%^&*()-+=~`|\\\\}]{[");
		Set<String> expectedWords = new HashSet<String>(
				Arrays.asList("2-BLL-ME", "2-ALL-ME"));
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			Set<String> actualMatchingWords = numberContainer.getTransformed();
			assertEquals(expectedWords, actualMatchingWords);
		});

	}

	/**
	 * If there are words with invalid characters they should be simply ignored
	 */
	@Test
	public void test_For_Words_With_Invalid_Characters() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("0123","BLL", "ME", "ALL", "L"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams("225563");
		Set<String> expectedWords = new HashSet<String>(
				Arrays.asList("2-BLL-ME", "2-ALL-ME"));
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			Set<String> actualMatchingWords = numberContainer.getTransformed();
			assertEquals(expectedWords, actualMatchingWords);
		});

	}
	
	/**
	 * If there are numbers with invalid characters the response should contain errors
	 */
	@Test
	public void test_For_Numbers_With_Invalid_Characters() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("0123","BLL", "ME", "ALL", "L"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams("A225563");
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			assertFalse(numberContainer.getErrors().isEmpty());
		});

	}
	
}




