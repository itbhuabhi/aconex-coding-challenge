/**
 * 
 */
package com.aconex.challenge.numbertowords.dictionary.transformers;

import com.aconex.challenge.numbertowords.util.CollectionsUtil;

/**
 * Decorator class which transforms an input string to upper case
 * If there is a {@link parentTransformer} provided then first it would use that to do the first level of transformation. 
 * And the transformed output of the parent would be converted to upper case
 * 
 * @author Abhishek Agarwal
 *
 */
public class UpperCaseTransformer implements InputTransformer<String> {
	
	/**
	 * Parent Transformer, whose transformed output is converted to Upper case
	 */
	
	private InputTransformer<String> parentTransformer;
	
	/**
	 * No-arg constructor, so that it can be used in isolation as well to convert a string to upper case
	 */
	
	public UpperCaseTransformer() {
		
	}
	/**
	 * Constructs with a parent transformer whose output is piped to this class
	 * @param parentTransformer Parent Transformer, whose transformed output is converted to Upper case
	 */
	
	public UpperCaseTransformer(InputTransformer<String> parentTransformer) {
		this.parentTransformer = parentTransformer;
	}


	@Override
	public void transform(TransformerContainer<String> transformerContainer) {
		String strToBeConvertedToUpperCase = transformerContainer.getInput();
		if(parentTransformer == null) {
			transformerContainer.setTransformed(strToBeConvertedToUpperCase.toUpperCase());
		}
		//If there is a parentTransformer then first the transformation as per the parent transformer needs to be applied and then convert it to upper case 
		else {
			parentTransformer.transform(transformerContainer);
			//Only if there are no errors reported from the parent, then we need to convert to uppercase
			if(CollectionsUtil.isNullOrEmpty(transformerContainer.getErrors())) {
				strToBeConvertedToUpperCase = transformerContainer.getTransformed();
				transformerContainer.setTransformed(strToBeConvertedToUpperCase.toUpperCase());
			}
		}
	}

}
