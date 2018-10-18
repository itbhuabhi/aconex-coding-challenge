package com.aconex.challenge.numbertowords.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagesUtil {
	// property file is: package/name/messages.properties
	private static final String BUNDLE_NAME = "messages.messages";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	private MessagesUtil() {
	}
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	public static String getString(String key, Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static void main(String[] args) {
		System.out.println(MessagesUtil.getString("application.usage.message"));
	}
}
