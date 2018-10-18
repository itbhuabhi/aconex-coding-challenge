package com.aconex.challenge.numbertowords.config;

import java.util.Arrays;
import java.util.Properties;

public class Configuration {
	private static final String CONFIG_FILE_PATH = "configuration/application.config";
	private static final String WORDS_CONCATENATE_DELIMITTER_KEY = "concatenate.delimtter";
	private static final String RETAIN_CONS_UNMATCHED_DIGITS_ASIS_KEY = "unchangedigits.list";
	
	private static final String STRIP_CHARS_REGEX_KEY = "strip.chars.regex";
	private static final String DICT_WORD_VALID_REGEX_KEY = "dict.word.valid.regex";
	private static final String NUM_VALID_REGEX_KEY = "num.valid.regex";

    private static Configuration instance;
	private String concatenateDelimitter;
	private int[] retainConsecutiveUnmatchedCharsAsIs;
	
	private Properties configFileProperties;
	
    /**
     * This method is addded so that it can be mocked and then used by the application. Another option could have been to use Power Mockito
     * @param config Mocked config object
     */
    public static void setInstance(Configuration config) {
        synchronized (Configuration.class) {
        	instance = config;
        }
      }
    
      public static Configuration getInstance() {
        synchronized (Configuration.class) {
          if (instance == null) {
        	  instance = new Configuration();
          }
        }
        return instance;
      }
	
	private Configuration() {
		configFileProperties = new java.util.Properties();
		try {
			configFileProperties.load(Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String wordConcatenateDelimitter() {
		if(concatenateDelimitter != null)
			return concatenateDelimitter;
		concatenateDelimitter = getFromSystemPropertyFirst(WORDS_CONCATENATE_DELIMITTER_KEY, "-");
		return concatenateDelimitter;
	}
	
	public int[] retainConsecutiveUnmatchedDigitsAsIs() {
		if(retainConsecutiveUnmatchedCharsAsIs!=null)
			return retainConsecutiveUnmatchedCharsAsIs;
		String str = getFromSystemPropertyFirst(RETAIN_CONS_UNMATCHED_DIGITS_ASIS_KEY,"1");
		retainConsecutiveUnmatchedCharsAsIs = Arrays.stream(str.split(","))
		        .mapToInt(Integer::valueOf)
		        .toArray();
		System.out.println(Arrays.toString(retainConsecutiveUnmatchedCharsAsIs));
		return retainConsecutiveUnmatchedCharsAsIs;
	}

	private String getFromSystemPropertyFirst(String key, String defaultValue) {
		String valueFromSysProperty = System.getProperty(key);
		if(valueFromSysProperty != null)
			return valueFromSysProperty;
		else
			return configFileProperties.getProperty(key,defaultValue);
	}
	
	public String stripCharactersRegex() {
		return configFileProperties.getProperty(STRIP_CHARS_REGEX_KEY,"[\\p{Punct}\\s]");
	}
	
	public String dictValidRegex() {
		return configFileProperties.getProperty(DICT_WORD_VALID_REGEX_KEY,"^[A-Za-z]{1,}$");
	}
	
	public String numberValidRegex() {
		return configFileProperties.getProperty(NUM_VALID_REGEX_KEY,"^[0-9]{1,}$");
	}
}