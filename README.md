# Aconex Coding Challenge
## Number converter application or 1800-FLOWERS problem
Basically this application helps to replace some or all of the digits of a phone number, with words from a given dictionary so that it is easier to remember.

#### Reason I chose this problem
The reason for me chosing this problem is that after reading both the problems couple of times, this problem statement was more clear to me. And in absolute terms I found this problem challenging enough.

#### Pre-requisite
Java 8

#### Usage
##### How to run the application
Clone the repository using below command\
`git clone https://github.com/itbhuabhi/aconex-coding-challenge`

The 'numbers-converter' directory inside 'aconex-coding-challenge' forms the root of the application, so please navigate to it first.\
`cd aconex-coding-challenge/numbers-converter`


Then on a Unix like Platform, navigate to the 'bin' directory, which is directly under the root\
`cd bin`

And then execute the shell script\
`. start.sh`

The script is made interactive to respond to User's inputs and guide User through different options.\
\
Alternatively, the program can be run directly by executing the JAR from 'target' directory, which also lies directly under the root.\
`java -jar number-converter-1.0-SNAPSHOT.jar`

Similar to execution via shell script, the application runs in interactive mode here as well.


##### How to build the jar again, run Unit tests, generate Javadocs

After cloning the repository, from root(numbers-converter directory) run the below command.\
`mvn clean install`

This generates the newly created jar, Unit test reports and Javadocs inside the target directory.\
Javadocs specifically can be found at:
`<root>/target/apidocs/index.html`


#### How the application is/can be enhanced
* The initial requirement was to restrict the application to retain only 1 consecutive digit in case of unmatched digit. But it is observed that it becomes a limitation for lot of numbers, more so for numbers with "0" as it does not have any character match. The program through configuration allows to change the number of digits which can remain as-is, in case of no matching combination for a subsequence of the phone number.

* The definition on which set of characters to be stripped from the phone numbers and dictionary words is easily configurable.

* The default number encoding can be easily changed. Moresoover, it does not have to be limited to English language. With some configuration changes and the chaining of transformation classes, it can support other English like languages. The reason I said English like language because not sure if something like this is possible with my native language Hindi. And if it is it would be much more complex.

* Furthermore, it can support other English like languages which can have supplementary characters.

* The application code can be easily modified to use some other implementation of Dictionary. Like presently Dictionary uses hash based datastructure, but it can be modified to use some thing like tries. Or even make it persistent to say some NoSQL DB if the dictionary is very huge.



#### Application Flow explaining the programming logic and usage of different classes.

Please note similar explaination is given in the Javadocs of the start class - `com.aconex.challenge.numbertowords.Main`. The Javadocs allows easy navigation to other classes. 

<p>The flow of the application can be summarized as.\
There are two main inputs given to the application by the user. File(s) containing different phone numbers which are to be translated. And a dictionary file which contains the different words which are to be used to convert the number into word combination(s). The class `UserInputHelper` is used to interact with the user using command line, and receives the file paths for the dictionary words and phone numbers.

<p>When numbers are converted to a word, each character of the word maps uniquely to a digit. This number encoding is stored in an internal configuration file and is parsed by `NumbersEncodingParser`, which converts it into a map of characters to digits.

<p>Furthermore, the words are stored in the `Dictionary` object but in numbers form. Simple reason being each word uniquely maps to a number, not the other way round. This makes the searching of sub-sequence of number less complex. So we would store the number form of a user dictionary word as the key and the set of words it maps to as the value in the `Dictionary` object.

<p>But even before the word given in user dictionary can be converted to number, we need to take care of stripping punctuations and whitespaces. And then convert it to uppercase. And lastly validate if the transformed input is valid or not.

<p>Similarly before finding matches for a number we need to do a similar thing i.e. get rid of punctuations and whitespaces and validate it.

<p>So this suggests that both user dictionary words and phone numbers involve series of transformation.

<p>To support these transformations there is an interface `InputTransformer` which defines a common contract and all transformation classes implment this interface. This allows a transformation to be decorated with another transformation.

<p>This chaining of transformers makes it easy if some of the business rules related to transformation are changed. Or the application needs to support other language, or some other number encoding. The various transformers used in the application are as.<br>

For dictionary words::

* Strip punctuations and whitespaces and validate after that. (`StripAndValidateInput`)
* Convert it to uppercase (`UpperCaseTransformer`)
* Transform it to a number using the number encoding. (`WordToNumberConverter`)

For phone numbers::

* Strip punctuations and whitespaces and validate after that (`StripAndValidateInput`)
* Convert number into matching word combinations which is the eventual outcome. (`NumberConverterAlgorithm`)

So, once we have the stream of dictionary words the abstract dictionary factory `DictionaryFactory` creates the dictionary, transforms each word to a number and stores it in the dictionary. The actual instantiation of the `Dictionary` and the data structure used for Dictionary is left to the concrete implementation of the `DictionaryFactory`. Presently, the dictionary implementation chosen relies on hash set as its internal datastructure, but it can be some other structure liek Tries, or a NoSQL DB if the input dictionary is too huge. Once the dictionary is created the class `NumbersConverter` iterates through each phone number and passes it to `NumberConverterAlgorithm` which finds the mapping word combinations. 

`NumberConverterAlgorithm` forms the core of the application and the algorithm used to convert a number to word combinations can be briefly described as

Iterate through each prefix one by one and search for matching word(s). If matching word(s) can be found use them, else see if the business rules allow the prefix digits to remain as-is
Make a recursive call to get all combinations of the remaining suffix if there was a match found for prefix either in the dictionary or retaining the prefix.
Concatenate the matching combinations of the prefix and the suffix.

As matching combinations are found for each number, the class `NumbersConverter` sends it back to the `Main` class by a call back handler, which in turns displays the output on console to the user. Lastly the class `ApplicationFacade` as the name suggests brings all these pieces together.
