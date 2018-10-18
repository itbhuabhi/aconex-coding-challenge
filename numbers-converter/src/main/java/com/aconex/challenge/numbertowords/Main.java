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

import com.aconex.challenge.numbertowords.converter.NumberConverterAlgorithm;
import com.aconex.challenge.numbertowords.converter.NumbersConverter;
import com.aconex.challenge.numbertowords.dictionary.Dictionary;
import com.aconex.challenge.numbertowords.dictionary.DictionaryFactory;
import com.aconex.challenge.numbertowords.dictionary.NumbersEncodingParser;
import com.aconex.challenge.numbertowords.dictionary.WordToNumberConverter;
import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.StripAndValidateInput;
import com.aconex.challenge.numbertowords.dictionary.transformers.TransformerContainer;
import com.aconex.challenge.numbertowords.dictionary.transformers.UpperCaseTransformer;
import com.aconex.challenge.numbertowords.util.CollectionsUtil;
import com.aconex.challenge.numbertowords.util.MessagesUtil;
import com.aconex.challenge.numbertowords.util.StringUtil;

/**
 * This is the starting point of the application. The flow of the application can be summarized as.<br>
 * There are two main inputs given to the application by the user. File(s) containing different phone numbers which are to be translated.
 * And a dictionary file which contains the different words which are to be used to convert the number into word combination(s).
 * The class {@link UserInputHelper} is used to interact with the user using command line, and receive the file paths for the dictionary words and phone numbers.
 * 
 * <p>When numbers are converted to a word, each character of the word maps uniquely to a digit. 
 * This number encoding is stored in an internal configuration file and is parsed by {@link NumbersEncodingParser}, 
 * which converts it into a map of characters to digits. 
 * 
 * <p>Furthermore, the words are stored in the {@link Dictionary} but in numbers form.
 * Simple reason being each word uniquely maps to a number, but not vica-versa, which makes searching of sub-sequence of number less complex.
 * So we would store the number form of a user dictionary word as the key and the set of words it maps to as the value in the {@link Dictionary} 
 * 
 * <p>But even before the word given in user dictionary can be converted to number, we need to take care of stripping punctuations and whitespaces.
 * And convert it to uppercase. And validate if the transformed input is valid or not.
 * 
 * <p>Similarly before finding matches for a number we need to do a similar thing i.e. get rid of punctuations and whitespaces and validate it.
 *    
 * <p>So this suggests that both user dictionary words and phone numbers involve series of transformation.
 * 
 * 
 * <p>To support these transformations there is an interface {@link InputTransformer} which defines a common contract. 
 * And allows the transformations to be chained together using a {@link TransformerContainer}.
 * It is passed around from one transformer to another and contains the input, and is fed with the transformed output and error, if any.
 * 
 * <p>The chaining of transformers makes it easy if some of the business rules related to transformation are changed
 * Or the application needs to support other language, or some other number encoding.
 * The various transformers used in the application are as
 * For dictionary words
 * <ul>
 * 	<li>Strip punctuations and whitespaces and validate after that. ({@link StripAndValidateInput})</li>
 * 	<li>Convert it to uppercase ({@link UpperCaseTransformer})</li>
 * 	<li>Transform it to a number using the number encoding ({@link WordToNumberConverter})</li>
 * </ul>
 * 
 * <p>For phone numbers
 * <ul>
 * 	<li>Strip punctuations and whitespaces and validate after that ({@link StripAndValidateInput})</li>
 *	<li>Convert number into matching word combinations ({@link NumberConverterAlgorithm})</li>
 * </ul>
 * 
 * <p>So, once we have the stream of dictionary words the abstract dictionary factory {@link DictionaryFactory}
 * creates the dictionary, transforms each word to a number and stores it in the dictionary.
 * The actual instantiation of the {@link Dictionary} and the data structure used for {@link Dictionary} is left to
 * the concrete implementation of the {@link DictionaryFactory}. Presently, I have chosen a dictionary 
 * which relies on hash set as an internal datastructure, but it can some other structure or a NoSQL DB if the input dictionary 
 * is too huge.
 * 
 * Once the dictionary is created the class {@link NumbersConverter} iterates through each phone number and 
 * passes it to {@link NumberConverterAlgorithm} which finds the mapping word combinations.
 * 
 * The brief description of the algorithm is 
 * <ul>
 * 	<li>Iterate through each prefix one by one and search for matching word(s). 
 * 		If matching word(s) can be found use them, 
 * 		else see if the business rules allow the prefix digits to remain as-is</li>
 * 	<li>Make a recursive call to get all combinations of the remaining suffix.</li>
 * 	<li>Concatenate the matching combinations of the prefix and the suffix.</li>
 * </ul>
 * 
 * As matching combinations are found for each number, the class {@link NumbersConverter} sends it back 
 * to this class by a call back handler, which in turns displays the output on console to the user.
 * 
 * Lastly the class {@link ApplicationFacade} as the name suggests brings all these pieces together.
 * 
 * @author Abhishek Agarwal
 *
 */

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
