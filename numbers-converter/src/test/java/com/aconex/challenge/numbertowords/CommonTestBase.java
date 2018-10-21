package com.aconex.challenge.numbertowords;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.aconex.challenge.numbertowords.config.Configuration;

public class CommonTestBase {

	protected Map<String, String> defaultNumberEncodingMap() {
		Map<String, String> map = new HashMap<String, String>();

		parseAndAddToMap("A,B,C->2", map);
		parseAndAddToMap("D,E,F->3", map);
		parseAndAddToMap("G,H,I->4", map);
		parseAndAddToMap("J,K,L->5", map);
		parseAndAddToMap("M,N,O->6", map);
		parseAndAddToMap("P,Q,R,S->7", map);
		parseAndAddToMap("T,U,V->8", map);
		parseAndAddToMap("W,X,Y,Z->9", map);
		return map;
	}

	protected Map<String, String> supplementaryCharsMap() {
		Map<String, String> map = new HashMap<String, String>();

		parseAndAddToMap("𐐀,B,C->2", map);
		parseAndAddToMap("D,E,F->3", map);
		parseAndAddToMap("G,H,I->4", map);
		parseAndAddToMap("J,K,L->5", map);
		parseAndAddToMap("M,N,O->6", map);
		parseAndAddToMap("P,Q,R,S->7", map);
		parseAndAddToMap("T,U,V->8", map);
		parseAndAddToMap("W,X,Y,Z->9", map);
		return map;
	}

	protected void mockConfigurationWithDefaults() {
		Configuration mockConfig = mock(Configuration.class);
		when(mockConfig.retainConsecutiveUnmatchedDigitsAsIs())
				.thenReturn(new int[]{1});
		when(mockConfig.wordConcatenateDelimiter()).thenReturn("-");
		when(mockConfig.stripCharactersRegex()).thenReturn("[\\p{Punct}\\s]");
		when(mockConfig.dictValidRegex()).thenReturn("^[A-Za-z]{1,}$");
		when(mockConfig.numberValidRegex()).thenReturn("^[0-9]{1,}$");

		Configuration.setInstance(mockConfig);
	}

	private void parseAndAddToMap(String input, Map<String, String> map) {
		String[] splitArr1 = input.split("->");
		// TODO Validate
		String[] alphabets = splitArr1[0].split(",");
		String digit = splitArr1[1];
		for (String alphabet : alphabets) {
			map.put(alphabet, digit);
		}
	}
}

