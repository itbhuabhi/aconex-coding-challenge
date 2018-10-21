package com.aconex.challenge.numbertowords.dictionary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.TransformerContainer;
import com.aconex.challenge.numbertowords.util.CollectionsUtil;

/**
 * Abstract class which parses each word of every dictionary source, applies validation and transformation on each word, 
 * then converts that word to a number and stores that the number representation of the word and the word itself in the dictionary.
 * 
 * <p>The creation of the {@link Dictionary} and the type of dictionary is left to the implementation class.
 * This allows to easily plug different types of Dictionary implementations. Like the data structure for the dictionary can be hash based or tries.
 * Or if the dictionary is too large it can be made persistent to some NoSQL implementation like Infinispan
 * 
 * @author Abhishek Agarwal
 * @see HashBasedDictionaryFactory
 *
 */
public abstract class DictionaryFactory {
	private static final Logger LOGGER = Logger.getLogger(DictionaryFactory.class.getName());

	private Dictionary dictionary;
	
	
	/**
	 * Word converter which would do any pre-processing, if decorated with other transformer, on each dictionary word before converting it to a number based on the number encoding. 
	 */

	private InputTransformer<String> wordConverter;
	
	/**
	 * Constructs a container object which would be later used to create and populate the dictionary
	 * @param wordConverter Word converter which would do any pre-processing on each dictionary word before converting it to a number based on the number encoding.
	 */

	public DictionaryFactory(InputTransformer<String> wordConverter) {
		this.wordConverter = wordConverter;

	}

	public Dictionary getDictionary() {
		return dictionary;
	}
	/**
	 * Iterates through each dictionary source and then each element of the dictionary, and then transforms it to a number, and the combination of word and number are stored in the dictionary.
	 * Presently this is single threaded but it can be overridden to make it multi-threaded and at the same time the dictionary implementaion has to be made thread safe. 
	 * @param dictionarySources List of dictionary sources where each element of every source is dictionary word
	 */

	public void createAndPopulateDictionary(List<Stream<String>> dictionarySources) {
		dictionary = createDictionary();
		dictionarySources.forEach((dictionarySource) -> {
			populateDictionaryFromSingleSource(dictionarySource);
		});
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.log(Level.FINER, "Dictionary succesfully created and populated");
		}

	}

	/**
	 * Internal method which processes individual dictionary source
	 * @param dictionarySource
	 */
	private void populateDictionaryFromSingleSource(Stream<String> dictionarySource) {
		
		dictionarySource.forEach((word) -> {
			TransformerContainer<String> wordToNumTransformerContainer = new TransformerContainer<String>(word);
			wordConverter.transform(wordToNumTransformerContainer);
			if (!CollectionsUtil.isNullOrEmpty(wordToNumTransformerContainer.getErrors()) ) {
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.log(Level.FINE, "Invalid dictionary word: {0}. Skipping it", word);
				}
			} else {
				String wordToBeAdded = wordToNumTransformerContainer.getInput();
				String matchingNumber = wordToNumTransformerContainer.getTransformed();
				dictionary.insert(matchingNumber, wordToBeAdded );
			}
		});

	}

	/**
	 * Abstract factory method to create the dictionary, based on the implementation used in the Subclass
	 * @return Returns the empty dictionary object to be populated by {@link #createAndPopulateDictionary} 
	 */
	protected abstract Dictionary createDictionary();

	

}
