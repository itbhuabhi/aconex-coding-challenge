#!/bin/bash

(
	USAGE_TEXT='\n
USAGE: . start.sh [numbersFilePaths...]\n
\n
where numbersFilePaths is the list of file paths, separated by whitespace, of files containing phone numbers to be converted to matching words combinations.\n
	\tIf not provided the application will ask interactively to provide the same.\n
\n	
Also, the default behavior can be overridden by running the below command prior to the execution of the script\n
export JAVA_OPTS="[options]"\n
\n	
	\twhere options is a space delimited list of JAVA options and include\n
\n	 
	 \t-Ddictionary.path=<dictionaryPath>\n
		\t\tFile path of a dictionary containing words which would be used to convert a number to matching words combination\n
		\t\tIf not provided defaults to an internal dictionary containing around 3000 common words\n
	\t-Dunchangedigits.list=<unchangedDigitsList>\n
		\t\tComma separated list of no. of digits which can remain as-is if no matching word is found for them. Defaults to 1\n
		\t\tEx-> if you want 2 consecutive digits unchanged, but no single digit remaining unchanged in isolation, then the option should be -Dunchangedigits.list=2\n
		\t\tBut if you want maximum of 2 consecutive digits unchanged, then the option should be  -Dunchangedigits.list=1,2\n
	\t-Dconcatenate.delimter=<delimiterchar>\n
		\t\tDelimiter character used to concatenate different words. Default to -\n
\n		
		\t\tEx-> export JAVA_OPTS="-Dunchangedigits.list=1,2,3,4,5 -Dconcatenate.delimtter=~"\n'
		
		
	CLASSPATH=../target/number-converter-1.0-SNAPSHOT.jar
	MAIN_CLASS=com.aconex.challenge.numbertowords.Main
	if [ "$#" -eq 0 ]
	then	
		echo -e 'Enter USE or U for Usage, SAMPLE or S to run the application with sample data.\nElse give list of file paths, separated by whitespace, of files  containing phone numbers.'
		read -p "	" user_input
		case $user_input in 
			U|USE) 
				echo -e $USAGE_TEXT
				exit 0;;
			S|SAMPLE)
				COMMAND_ARGS=USE_SAMPLE;;
			*[!\ \t]*)  	
				COMMAND_ARGS=$user_input;;
			*) 
				echo 'No input provided. Terminating the application.'
				exit 0;;
		esac
	else
		COMMAND_ARGS="$@"
	fi	
	java -cp $CLASSPATH $JAVA_OPTS $MAIN_CLASS $COMMAND_ARGS
)
