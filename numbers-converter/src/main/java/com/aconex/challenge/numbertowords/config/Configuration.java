package com.aconex.challenge.numbertowords.config;

import java.util.Arrays;
import java.util.Properties;

import com.aconex.challenge.numbertowords.util.StringUtil;

/**
 * Loads default configurations from application config file.
 * Some of the configuration options are passed as System property which takes precedence over 
 * what is there in the config file.
 * @author Abhishek Agarwal
 *
 */
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
     * 
     * This method is added so that it can be mocked and then used by the application. Another option could have been to use Power Mockito
     * @param config Mocked config object
     */
    public static void setInstance(Configuration config) {
        synchronized (Configuration.class) {
        	instance = config;
        }
      }
    
    /**
     * Returns singleton instance of Configuration
     * @return returns singleton instance of Configuration
     */
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
	
	/**
	 * Default Character string to be used to concatenate two different dictionary words used for the same number. 
	 * @return Returns character string to concatenate two different words used for the same number. Defaults to -
	 */
	public String wordConcatenateDelimitter() {
		if(concatenateDelimitter != null)
			return concatenateDelimitter;
		concatenateDelimitter = getFromSystemPropertyFirst(WORDS_CONCATENATE_DELIMITTER_KEY, "-");
		return concatenateDelimitter;
	}
	
	/**
	 * Returns an array of number of digits which can consecutively remain as-is.
	 * The user can override the configuration by passing it as system property. 
	 * If invalid input provided it defaults to 1.
	 * @return an array of number of digits which can consecutively remain as-is
	 */
	public int[] retainConsecutiveUnmatchedDigitsAsIs() {
		if(retainConsecutiveUnmatchedCharsAsIs!=null)
			return retainConsecutiveUnmatchedCharsAsIs;
		String str = getFromSystemPropertyFirst(RETAIN_CONS_UNMATCHED_DIGITS_ASIS_KEY,"1");
		try {
		retainConsecutiveUnmatchedCharsAsIs = Arrays.stream(str.split("\\s*,\\s*"))
				.map((str1)-> str1.trim())
		        .mapToInt(Integer::valueOf)
		        .toArray();
		}
		//If invalid user input default to 1
		catch(NumberFormatException nfe) {
			retainConsecutiveUnmatchedCharsAsIs = new int[] {1};
		}
		System.out.println(Arrays.toString(retainConsecutiveUnmatchedCharsAsIs));
		return retainConsecutiveUnmatchedCharsAsIs;
	}

	private String getFromSystemPropertyFirst(String key, String defaultValue) {
		String valueFromSysProperty = System.getProperty(key);
		if(!StringUtil.isBlankOrNull(valueFromSysProperty))
			return valueFromSysProperty;
		else
			return configFileProperties.getProperty(key,defaultValue);
	}
	
	/**
	 * Regular expression to remove characters from numbers and dictionary words. 
	 * Defaults to stripping punctuations and whitespaces from numbers and dictionary words.
	 * @return Regular expression to remove characters from numbers and dictionary words.
	 */
	public String stripCharactersRegex() {
		return configFileProperties.getProperty(STRIP_CHARS_REGEX_KEY,"[\\p{Punct}\\s]");
	}
	/**
	 * Regular expression to validate dictionary words after stripping of unwanted characters.
	 * Defaults to matching English letter alphabets only.
	 * @return Regular expression to validate dictionary words after stripping of unwanted characters. 
	 */
	public String dictValidRegex() {
		return configFileProperties.getProperty(DICT_WORD_VALID_REGEX_KEY,"^[A-Za-z]{1,}$");
	}

	/**
	 * Regular expression to validate numbers after stripping of unwanted characters.
	 * Defaults to matching numeric values.
	 * @return Regular expression to validate numbers after stripping of unwanted characters. 
	 */
	public String numberValidRegex() {
		return configFileProperties.getProperty(NUM_VALID_REGEX_KEY,"^[0-9]{1,}$");
	}
}
