package com.aconex.challenge.numbertowords.converter;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.dictionary.transformers.InputTransformer;
import com.aconex.challenge.numbertowords.dictionary.transformers.TransformerContainer;

/**
 * It accepts multiples streams of numbers and sequentially converts each number to a word combination. Presently processing of numbers is done sequentially
 * but can be extended to do it parallel in a multi-threaded environment.
 *
 * @author Abhishek Agarwal
 *
 */
public class NumbersConverter {
	/**
	 * The core algorithm which transforms a number to a word combinations. This is decorated with a parent transformer which would convert the number so that the
	 * resulting string is left with only numbers.
	 */
	private InputTransformer<Set<String>> numberConverterAlgo;

	/**
	 * Initializes with the Number Converter Algorithm object which is used to
	 * find matching word combinations for each number string
	 *
	 * @param numberConverterAlgo The core algorithm which transforms a number to a word combinations.
	 */
	public NumbersConverter(InputTransformer<Set<String>> numberConverterAlgo) {
		this.numberConverterAlgo = numberConverterAlgo;
	}


	/**
	 *
	 * @param numberStreams A List of number streams where each element of the stream is a number whose mapping word combinations is to be found.
	 * @param consumer Call back handler which processes the result. The call back handler can chose to display the results on the UI,
	 *  persist it, or do some further processing on the results. streams.
	 */
	public void convertNumbers(List<Stream<String>> numberStreams,
			Consumer<TransformerContainer<Set<String>>> consumer) {
		numberStreams.stream()
				.forEach((input) -> input.forEach((numberString) -> {
					TransformerContainer<Set<String>> numberContainer = new TransformerContainer<Set<String>>(
							numberString);
				numberConverterAlgo.transform(numberContainer);
				consumer.accept(numberContainer);
				})
		);
	}
}
