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
		applicationFacade.initDictionaryContainer();
		return applicationFacade;

	}

	private List<Stream<String>> getDictionaryAndNumberStreams(String... strings) {
		return Collections.singletonList(Stream.of(strings));
	}
	

	


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



	


	@Test
	public void test_For_Words_And_Numbers_With_Punctuations() {
		ApplicationFacade applicationFacade = mockApplicationFacade();
		applicationFacade.createAndPopulateDictionary(getDictionaryAndNumberStreams("BLL,!@#$%^&*()-+=~`|\\}]{[", "ME", "ALL", "L"));
		applicationFacade.initNumbersConverter();
		NumbersConverter numbersConverter = applicationFacade.getNumbersConverter();
		List<Stream<String>> numbersStreams = getDictionaryAndNumberStreams(
				"225563,!@#$%^&*()-+=~`|\\\\}]{[");
		Set<String> expectedWords = new HashSet<String>(
				Arrays.asList("2-BLL-ME", "2-ALL-ME"));
		numbersConverter.convertNumbers(numbersStreams, (numberContainer) -> {
			Set<String> actualMatchingWords = numberContainer.getTransformed();
			assertEquals(expectedWords, actualMatchingWords);
		});

	}

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



