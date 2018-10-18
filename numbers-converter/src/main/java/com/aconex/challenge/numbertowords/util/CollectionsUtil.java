package com.aconex.challenge.numbertowords.util;

import java.util.Collection;
import java.util.Map;
/**
 * A utility class for Collections. This has some of the methods which are not there in the Java SDK
 * Very small subset of https://commons.apache.org/proper/commons-collections/
 *
 */

public class CollectionsUtil {
	/**
	 *
	 * @param collection The collection which needs to be tested.
	 * @return A boolean value. True if the passed collection is null or empty
	 */
	public static boolean isNullOrEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 *
	 * @param map The map which needs to be tested.
	 * @return A boolean value. True if the passed map is null or empty
	 */

	public static boolean isNullOrEmpty( final Map< ?, ? > map ) {
	    return map == null || map.isEmpty();
	}

	/**
	 * A utility method which checks if a given number is present in an array or not.
	 * @param numArray The array which needs to be scanned.
	 * @param num The number which is being searched in the array.
	 * @return true if a match of the number is found in the array.
	 */
	public static boolean anyMatch(int[] numArray, int num) {
		if(numArray == null || numArray.length == 0)
			return false;
		if(numArray.length == 1) {
			return num == numArray[0];
		}
		else {
			for(int numI: numArray) {
				if(num == numI)
					return true;
			}
		}
		return false;
	}
	
	

}
