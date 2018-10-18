package com.aconex.challenge.numbertowords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.converter.NumbersConverter;
import com.aconex.challenge.numbertowords.util.CollectionsUtil;
import com.aconex.challenge.numbertowords.util.MessagesUtil;
import com.aconex.challenge.numbertowords.util.StringUtil;



public class Main {
	
	private static final String SAMPLE_DICTIONARY_RESOURCE_PATH = "samples/commonWords.txt";
	private static final String SAMPLE_NUMBERS_RESOURCE_PATH = "samples/samplePhoneNumbers.txt";

	private static final String NO_MATCH_MESSAGE_KEY = "number.none.match";
	private static final String MATCH_MESSAGE_KEY = "number.matches";
	private static final String INVALID_INPUT_MESSAGE_KEY = "invlaid.number";
	
	
	private ApplicationFacade applicationFacade;

	public Main() {
		applicationFacade = new ApplicationFacade();
	}


	public static void main(String[] strArray) throws IOException {
		
		Main main = new Main();
		
		UserInputHelper userInputHelper = new UserInputHelper();
		userInputHelper.parseInputInteractively(strArray);
		
		String usersDictionaryPath = userInputHelper.getDictionaryPath();
		List<String> usersNumbersFilesPath = userInputHelper.getPhoneNumbersFilePaths();
		
		List<Stream<String>> dictionarySources = new ArrayList<Stream<String>>();
		List<Stream<String>> numbersStreams = new ArrayList<Stream<String>>();
		
		//If no external dictionary is provided, then we would use an Internal sample dictionary
		if(StringUtil.isBlankOrNull(usersDictionaryPath)) {
			Stream<String> sampleDictionarySource = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(SAMPLE_DICTIONARY_RESOURCE_PATH))).lines();
			dictionarySources.add(sampleDictionarySource);
		}
		else {
			dictionarySources.add(Files.lines(Paths.get(usersDictionaryPath)));
		}
		
		if(CollectionsUtil.isNullOrEmpty(usersNumbersFilesPath)) {
			Stream<String> sampleNumbersStream = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(SAMPLE_NUMBERS_RESOURCE_PATH))).lines();
			numbersStreams.add(sampleNumbersStream);
		}
		else {
			
			for(String filePath : usersNumbersFilesPath) {
				numbersStreams.add(Files.lines(Paths.get(filePath)));
			}
		}
		
		convert(dictionarySources, numbersStreams);
		
		


		

	}
	
	private static void convert(List<Stream<String>> dictionarySources, List<Stream<String>> numbersStreams) {
		ApplicationFacade applicationFacade = new ApplicationFacade();
		applicationFacade.createAndPopulateDictionary(dictionarySources);
		applicationFacade.initNumbersConverter();
		NumbersConverter converter = applicationFacade.getNumbersConverter();
		converter.convertNumbers(numbersStreams, 
				(numStringContainer) -> {
					List<String> errors = numStringContainer.getErrors();
					if (CollectionsUtil.isNullOrEmpty(errors)) {
						Set<String> matchedWords = numStringContainer.getTransformed();
						if (CollectionsUtil.isNullOrEmpty(matchedWords)) {
							System.out.println(MessagesUtil.getString(NO_MATCH_MESSAGE_KEY,numStringContainer.getInput()));
						} else {
							System.out.println(MessagesUtil.getString(MATCH_MESSAGE_KEY,numStringContainer.getInput(),matchedWords));
							//System.out.println(numStringContainer.getInput() + " matches:: " + matchedWords);
						}
					} else {
						System.err.println(MessagesUtil.getString(INVALID_INPUT_MESSAGE_KEY,numStringContainer.getInput()));

					}
				});
	}
}
