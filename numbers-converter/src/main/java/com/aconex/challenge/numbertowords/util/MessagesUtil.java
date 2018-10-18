package com.aconex.challenge.numbertowords.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class to convert message keys in combination with paramters to messages from a properties file
 * @author Abhishek Agarwal
 *
 */
public class MessagesUtil {
	// property file is: package/name/messages.properties
	private static final String BUNDLE_NAME = "messages.messages";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	private MessagesUtil() {
	}
	/**
	 * Gets the message for the message key
	 * @param key The key in the messages properties file
	 * @return The message associated with the key
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	/**
	 * 
	 * @param key The key in the messages properties file
	 * @param params Value of the placeholders in the messages
	 * @return Returns the message
	 */
	public static String getString(String key, Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return key;
		}
	}

}
