package com.aconex.challenge.numbertowords.util;

/**
 * A utility class for String objects. More string related utility methods can be added to this class.
 *
 * @author Abhishek Agarwal
 *
 */
public class StringUtil {
	/**
	 * Tests if the passed string is null or after trimming whitespaces is blank.
	 * @param str The string to be tested.
	 * @return true, if the passed string is null or after trimming whitespaces it is blank.
	 */
	public static boolean isBlankOrNull(String str) {
		if(str == null || str.trim().length() ==0) {
			return true;
		}
		return false;
	}
}
