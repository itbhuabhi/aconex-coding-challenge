package com.aconex.challenge.numbertowords.util;

import java.io.File;

/**
 * Uitlity class for IO Operations. 
 * Presently it has only one method but would always have potential to grow.
 * @author Abhishek Agarwal
 *
 */
public class IOUtils {
	/**
	 * Utility method to test if the given file points to a file.
	 * @param filePath Path of the file which is to be tested.
	 * @return true, if the file exists at the given file path
	 */
	public static boolean isValidFilePath(String filePath) {
		if(StringUtil.isBlankOrNull(filePath))
			return false;
		File tmpFile = new File(filePath);
		return tmpFile.isFile();
	}
	

}
