package com.aconex.challenge.numbertowords.dictionary.transformers;

/**
 * Interface to define a contract for the different transformation implementations. Some of the existing usage of implementations are
 * <ul>
 * 	<li>Strip whitespace and punctuations from numbers and words.</li>
 * 	<li> Convert dictionary words to upper case.</li>
 * 	<li> Convert dictionary word to a corresponding number, based on the number encoding, to be stored in a dictionary.</li>
 * </ul>
 * Since the implementation have a common contract so they can be chained.<br>
 * 
 * Ex- Dictionary word first transformed to strip unwanted characters. The output of which is then chained to Upper case, which in turn is transformed to a number to be stored in the dictionary.
 * 
 * @param <T> This is the type of the result which comes out of transformation.
 * 
 * @author Abhishek Agarwal
 * @see StripAndValidateInput
 * @see UpperCaseTransformer
 * @see TransformerContainer
 */
public interface InputTransformer<T> {
	/**
	 * Method whose implementation would transform and validate(if required) a given input
	 * @param transformerContainer Container which encapsulates the input to be transformed and 
	 * and is fed with the transformed output or the errors as the case may be.
	 */
	public void transform(TransformerContainer<T> transformerContainer);

}
