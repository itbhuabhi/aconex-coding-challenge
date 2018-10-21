package com.aconex.challenge.numbertowords.dictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.aconex.challenge.numbertowords.util.StringUtil;
/**
 * This is a utility class which by default parses an internal file containing the number encoding
 * and stores the encoding as a map, which can be retrieved later.<br><br>
 * It has two constructors the no-arg one would parse an internal file which has the default encoding.
 * At the same time it has another constructor can take a {@link InputStreamReader} for a different set of encoding.<br>
 * It expects each line of the reader having a set of characters mapping to a number.
 * Characters are separated from each other by a comma, and the set of characters and numbers
 * delimitted by an an arrow.<br>
 * An example of a line of the {@link InputStreamReader} is as below<br>
 * A,B,C -&gt; 2<br>
 * <br>
 * Comments can be added by having # character at the beginning of the line<br>
 * And the default encoding is<br>
 * A,B,C -&gt; 2	<br>
 * D,E,F -&gt; 3	<br>
 * G,H,I -&gt; 4 	<br>
 * J,K,L -&gt; 5 	<br>
 * M,N,O -&gt; 6 	<br>
 * P,Q,R,S -&gt; 7 <br>
 * T,U,V -&gt; 8	<br>
 * W,X,Y,Z -&gt; 9	<br><br>
 * 
 * 
 * 
 * @author aagarwal
 *
 */
public class NumbersEncodingParser {
	
	private static Logger LOGGER = Logger.getLogger(NumbersEncodingParser.class.toString());
	
	private static final String COMMENT_START_CHAR_SEQUENCE = "#";
	private static final String CHARS_DIGIT_DELIMITER = "->";
	private static final String CHARS_DELIMITER = ",";
	private static final String NUMBERS_ENCODING_FILE_NAME = "numbersEncoding.properties";
	
	private Map<String,String> charToDigitEncodingMap;

	/**
	 * No-arg constructor which would parse an internal encoding file and create the mapping.<br>
	 * Parsing is done as a part of the constructor so that when the method {@link #getCharToDigitEncoding()}
	 * the encoding map is available
	 */
	public NumbersEncodingParser() {
		parse(new InputStreamReader(ClassLoader.getSystemResourceAsStream(NUMBERS_ENCODING_FILE_NAME)));
	}
	/**
	 * Creates an encoding map from an external mapping source
	 * @param numbersEncodingReader Numbers Encoding Reader whose each line contains mapping from characters to a number
	 */
	public NumbersEncodingParser(InputStreamReader numbersEncodingReader) {
		parse(numbersEncodingReader);
	}
	/**
	 * Returns the encoding map such that each character has its own key
	 * @return Encoding map such that each character has its own key
	 */
	
	public Map<String,String> getCharToDigitEncoding() {
		return Collections.unmodifiableMap(charToDigitEncodingMap);
	}

	//TODO : Presently it does not have validation, but they need to be added preceded by their corresponding unit tests
	// to support external encoding in a more robust manner
	// The current set of Unit tests would ensure that existing functionality is not broken.
	private void parse(InputStreamReader reader) {
		charToDigitEncodingMap = new HashMap<String, String>();
		try (Stream<String> stream = new BufferedReader(reader).lines()) {
			stream.forEach((line) -> {
				if (StringUtil.isBlankOrNull(line)) {
					return;
				}
				line = line.trim();
				if (line.startsWith(COMMENT_START_CHAR_SEQUENCE)) {
					return;
				}
				String[] alphabetDigitSplitArr = line.split(CHARS_DIGIT_DELIMITER);
				String[] alphabets = alphabetDigitSplitArr[0].split(CHARS_DELIMITER);
				String digit = alphabetDigitSplitArr[1].trim();
				for (String alphabet : alphabets) {
					charToDigitEncodingMap.put(alphabet.trim(), digit);
				}
			});
		}
		if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "Number encoding map being used: {0}", charToDigitEncodingMap );
}
	}

}
