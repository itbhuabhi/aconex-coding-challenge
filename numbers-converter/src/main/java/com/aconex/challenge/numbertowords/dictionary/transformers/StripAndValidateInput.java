package com.aconex.challenge.numbertowords.dictionary.transformers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A transformer class which would strip the unwanted characters from the input,
 * and validate if it contains desired characters only.<br>
 * 
 * Ex- If the rule says to strip whitespaces and punctuations from the numbers and dictionary words,
 * then corresponding {@link #stripCharsPattern} is provided to do the same.<br>
 * Once the unwanted characters are removed, {@link #validCharsPattern} is used to verify if only desired characters remain
 * 
 * @author Abhishek Agarwal
 *
 */

public class StripAndValidateInput implements InputTransformer<String>{
	/**
	 * Regular Expression for the unwanted characters to be removed from the input
	 */
	private Pattern stripCharsPattern;
	/**
	 * Regular expression to to verify if only desired characters remain
	 */
	private Pattern validCharsPattern;
	//TODO: To store errors may be using enums or some better way
	private static String INVALID_INPUT_ERROR_KEY = "invalid.input";
	/**
	 * Pre-compiles the regular expressions for striping unwanted characters and verifying if only desired characters remain
	 * @param stripCharsRegex Regular Expression for the unwanted characters to be removed from the input
	 * @param validCharsRegex Regular expression to to verify if only desired characters remain
	 */
	public StripAndValidateInput(String stripCharsRegex, String validCharsRegex) {
		this.stripCharsPattern = Pattern.compile(stripCharsRegex);
		this.validCharsPattern = Pattern.compile(validCharsRegex);
	}
	
	/**
	 * Transforms or returns the errors based on the regular expressions provided
	 * @param transformerContainer which contains the input and is fed with the transformed output or the errors as the case may be.
	 */
	@Override
	public void transform(TransformerContainer<String> transformerContainer) {
		String strToBeTransformed = transformerContainer.getInput();
		String transformedStr = stripCharacters(strToBeTransformed);
		if(validate(transformedStr))
			transformerContainer.setTransformed(transformedStr);
		else {
			transformerContainer.addError(INVALID_INPUT_ERROR_KEY);
		}
	};
	
	private String stripCharacters(String str) {
		Matcher matcher = stripCharsPattern.matcher(
				str);
		String strippedStr = matcher.replaceAll("");
		return strippedStr;
	}
	private boolean validate(String input) {
		Matcher matcher = validCharsPattern.matcher(
				input);
		return matcher.find();
	}
}
