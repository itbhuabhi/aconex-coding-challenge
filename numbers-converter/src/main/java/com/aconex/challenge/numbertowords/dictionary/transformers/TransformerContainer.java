package com.aconex.challenge.numbertowords.dictionary.transformers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 
 * It is a generic container class of the below different transformations that happen now in converting numbers to a word combination.
 * <ul>
 * <li>Presently whitespaces and punctuations are stripped from phone numbers.</li>
 * <li>Similarly, whitespaces and punctuations are strpped from input dictionary words, and converted to upper case.</li>
 * <li>Then the above dictionary word is transformed to a number to be stored in the {@link com.aconex.challenge.numbertowords.dictionary.Dictionary Dictionary}</li>
 * <li>And then eventually the above dictionary word is transformed to a possible combination of words 
 * </ul>
 * To elaborate, we have phone numbers and dictionary words coming as input in a raw form
 * and we need to apply business rules to them before they can actually be used.<br>
 * 
 * Like in phone numbers and dictionary words we need to strip of the punctuations and whitespaces.<br>
 * Ex- If an {@link #input} phone number is 434-508-305, it first needs to be {@link #transformed} to 434508305<br>
 * Similarly an {@link #input} dictionary word like "won't" needs to be {@link #transformed} to "wont"
 * before it is added to the {@link com.aconex.challenge.numbertowords.dictionary.Dictionary Dictionary}<br>
 * 
 * Furthermore if the input is invalid, we can instead store the {@link #errors}<br>
 * 
 * This class can also be used to store the output of finding the matching word combinations. 
 * Ex- An {@link #input} number like 2255.63, based on a particular dictionary
 * is {@link #transformed} to a {@link java.util.Set} of words::  CALL-ME and BALL-ME 
 * 
 * 
 * @param <T> This is the type of the {@link #transformed} element
 * @author aagarwal
 */

public class TransformerContainer<T> {
	/**
	 * Input string which needs to be transformed
	 */
	private String input;
	/**
	 * Transformed output of the input string
	 */
	private T transformed;
	/**
	 * List of errors if the input string is invalid
	 */
	private List<String> errors;
	
	/**
	 * Constructs an inital empty object for an input which is being transformed
	 * @param input Input string which needs to be transformed
	 */

	public TransformerContainer(String input) {
		this.input = input;
	}
	
	/**
	 * 
	 * @return input string which needs to be tranfored 
	 */
	
	public String getInput() {
		return input;
	}
	
	/**
	 * 
	 * @return transformed output of the input String
	 */
	
	public T getTransformed() {
		return transformed;
	}
	
	/**
	 * 
	 * @param transformed output of the Input string
	 */

	public void setTransformed(T transformed) {
		this.transformed = transformed;
	}
	
	/**
	 * 
	 * @param errors List of errors if the input is invalid
	 */

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	/**
	 * 
	 * @return List of errors if the input is invalid
	 */
	
	public List<String> getErrors() {
		if (errors == null)
			return null;
		return Collections.unmodifiableList(errors);
	}
	
	/**
	 * 
	 * @param error Add each error one be one if the input is invalid
	 */
	
	public void addError(String error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		errors.add(error);
	}
	
	/**
	 * Utility method which would set {@link #transformed} as {@link #input} so that the same object can be piped to a different transformer.<br>
	 * Chose to flip the input rather than creating a new container object so that the errors are not lost. And the input of the previous transform might not be of any use. 
	 * 
	 * @throws IllegalAccessException If the {@link #transformed} object is not of type String
	 */
	public void flipInput() throws IllegalAccessException {
		if(this.transformed instanceof String ) {
			this.input = (String) this.transformed;
			this.transformed = null;
		}
		else
			throw new IllegalAccessException("Transformed type is not same as type of input");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TransformerContainer:: ");
		sb.append("input: ").append(input);
		sb.append(", transformed: ").append(transformed);
		sb.append(", errors= ").append(errors);
		return "TransformerContainer [input=" + input + ", transformed=" + transformed + ", errors=" + errors + "]";
	}
	
	
	
}
