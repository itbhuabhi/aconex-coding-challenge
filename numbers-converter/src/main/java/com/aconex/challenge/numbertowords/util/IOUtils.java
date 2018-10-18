package com.aconex.challenge.numbertowords.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class IOUtils {
	public static boolean isValidFilePath(String filePath) {
		if(StringUtil.isBlankOrNull(filePath))
			return false;
		File tmpFile = new File(filePath);
		return tmpFile.isFile();
	}
	
	public static BufferedReader getBufferedReaderForFile(String filePath, boolean isSystemResource) throws FileNotFoundException {
		if(isSystemResource)
			return new BufferedReader(new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filePath))));
		else
			return new BufferedReader(new FileReader(filePath)); 
	}

}
