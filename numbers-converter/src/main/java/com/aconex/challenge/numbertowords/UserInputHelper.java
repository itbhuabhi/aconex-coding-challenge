package com.aconex.challenge.numbertowords;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.aconex.challenge.numbertowords.util.CollectionsUtil;
import com.aconex.challenge.numbertowords.util.IOUtils;
import com.aconex.challenge.numbertowords.util.MessagesUtil;

public class UserInputHelper {

	private static final String NO_ARGS_MESSAGE_KEY = "missing.commanline.args.message";
	private static final String NO_INPUT_MESSAGE_KEY = "no.input.message";
	private static final String USAGE_MESSAGE_KEY = "application.usage.message";
	private static final String INVALID_FILE_PATH_MESSAGE_KEY = "invalid.file.path";

	private static final String SYS_PROP_DICT_PATH = "dictionary.path";
	List<String> phoneNumbersFilePaths;
	private String dictionaryPath;

	public static void main(String[] args) {
		UserInputHelper helper = new UserInputHelper();
		helper.parseInputInteractively(args);
	}

	public void parseInputInteractively(String[] commandLineArgs) {
		dictionaryPath = System.getProperty(SYS_PROP_DICT_PATH);
		if (commandLineArgs.length == 0) {
			System.out.println(MessagesUtil.getString(NO_ARGS_MESSAGE_KEY));
			Scanner stdInputScanner = new Scanner(System.in);
			String userInput = stdInputScanner.nextLine().trim().toUpperCase();
			stdInputScanner.close();
			switch (userInput) {
				case "" :
					System.out.println(MessagesUtil.getString(NO_INPUT_MESSAGE_KEY));
					System.exit(0);
				case "U" :
				case "USE" :
					System.out.println(MessagesUtil.getString(USAGE_MESSAGE_KEY));
					System.exit(0);
				case "S" :
				case "SAMPLE" :
					break;// DO nothing. The dictionary and phone number files would be loaded from the resources
				default :
					phoneNumbersFilePaths = Arrays.asList(userInput.split("\\s+"));
			}
		} else {
			phoneNumbersFilePaths =  Arrays.asList(commandLineArgs);
		}
		if(!areFilePathsValid()) {
			System.exit(0);
		}
	}
	
	
	public List<String> getPhoneNumbersFilePaths() {
		return phoneNumbersFilePaths;
	}

	public String getDictionaryPath() {
		return dictionaryPath;
	}

	private boolean areFilePathsValid() {
		boolean allFilePathsValid = true;
		if(!CollectionsUtil.isNullOrEmpty(phoneNumbersFilePaths)) {
			for(String phoneNumbersFilePath: phoneNumbersFilePaths) {
				if(!IOUtils.isValidFilePath(phoneNumbersFilePath)) {
					System.err.println(MessagesUtil.getString(INVALID_FILE_PATH_MESSAGE_KEY, phoneNumbersFilePaths));
					allFilePathsValid = false;
				}
			}
		};
		if(dictionaryPath != null && !IOUtils.isValidFilePath(dictionaryPath)) {
			System.err.println(MessagesUtil.getString(INVALID_FILE_PATH_MESSAGE_KEY, dictionaryPath));
			allFilePathsValid = false;
		}
		return allFilePathsValid;
	}

}
