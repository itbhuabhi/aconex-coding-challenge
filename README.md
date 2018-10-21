# Aconex Coding Challenge
## Number converter application or 1800-FLOWERS problem
Basically this application helps to replace some or all of the digits of a phone number, with words from a given dictionary so that it is easier to remember.

#### Reason I chose this problem
The reason for me chosing this problem is that after reading both the problems couple of times, this problem statement was more clear to me. And in absolute terms I found this problem challenging enough.

#### Pre-requisite
Java 8

#### Usage
##### How to run the application
Download the executable [jar](numbers-converter/target/number-converter-1.0-SNAPSHOT.jar)
From command line or Unix shell as the case may be, run the below command, executing from the directory where the above jar is saved.
`java -jar number-converter-1.0-SNAPSHOT.jar`

Once run, from there on, the application is interactive to guide the user.

##### How to build the jar again, run Unit tests, generate Javadocs</b>

Clone the [numbers-converter](https://github.com/itbhuabhi/aconex-coding-challenge) repository
And from numbers-converter directory run<br>
`mvn clean install`


#### How the application is/can be enhanced
* The requirement allows to retain only 1 consecutive digit. But I observed it becomes a limitation for lot of numbers, more so for numbers with "0" as it does not have any character match. The program through system property or configuration allows any number of digits to remain as-is, when no matching combination is found for them.

* The rules related to stripping of characters can be easily changed by configuration.

* Since the number encoding is read from a properties file, so it can be easily changed to something else.
Moresoover, it does not have to be limited to English language. With some configuration changes and may be change to the chaining of transformers it can support other English like languages. The reason I said English like language because not sure if something like this is possible with my native language Hindi. And if it is it would be much more complex.

* Furthermore, it can support other English like languages which have supplementary characters.

* The application code can be easily modified to use some other implementation of Dictionary. Like presently Dictionary uses hash based datastructure, but it can be modified to use some thing like tries. Or even make it persistent to say some NoSQL if the dictionary is very huge.



#### Application Flow explaining the programming logic and usage of different classes.

Please note similar explaination is given in the Javadocs of the com.aconex.challenge.numbertowords.Main class. 

The starting point of the application is `com.aconex.challenge.numbertowords.Main`. The flow of the application can be summarized as.
There are two main inputs given to the application by the user. File(s) containing different phone numbers which are to be translated. And a dictionary file which contains the different words which are to be used to convert the number into word combination(s). The class `UserInputHelper` is used to interact with the user using command line, and receives the file paths for the dictionary words and phone numbers.

When numbers are converted to a word, each character of the word maps uniquely to a digit. This number encoding is stored in an internal configuration file and is parsed by `NumbersEncodingParser`, which converts it into a map of characters to digits.

Furthermore, the words are stored in the `Dictionary` object but in numbers form. Simple reason being each word uniquely maps to a number, not the other way round. This makes the searching of sub-sequence of number less complex. So we would store the number form of a user dictionary word as the key and the set of words it maps to as the value in the `Dictionary` object.

But even before the word given in user dictionary can be converted to number, we need to take care of stripping punctuations and whitespaces. And convert it to uppercase. And validate if the transformed input is valid or not.

Similarly before finding matches for a number we need to do a similar thing i.e. get rid of punctuations and whitespaces and validate it.

So this suggests that both user dictionary words and phone numbers involve series of transformation.

To support these transformations there is an interface `InputTransformer` which defines a common contract and all transformation classes implment this interface. This allows the transformations to be chained together.

The chaining of transformers makes it easy if some of the business rules related to transformation are changed Or the application needs to support other language, or some other number encoding. The various transformers used in the application are as.

For dictionary words::

* Strip punctuations and whitespaces and validate after that. (`StripAndValidateInput`)
* Convert it to uppercase (`UpperCaseTransformer`)
* Transform it to a number using the number encoding (`WordToNumberConverter`)

For phone numbers::
* Strip punctuations and whitespaces and validate after that (`StripAndValidateInput`)
* Convert number into matching word combinations (`NumberConverterAlgorithm`)

So, once we have the stream of dictionary words the abstract dictionary factory `DictionaryFactory` creates the dictionary, transforms each word to a number and stores it in the dictionary. The actual instantiation of the `Dictionary` and the data structure used for Dictionary is left to the concrete implementation of the `DictionaryFactory`. Presently, I have chosen a dictionary which relies on hash set as an internal datastructure, but it can some other structure or a NoSQL DB if the input dictionary is too huge. Once the dictionary is created the class` NumbersConverter` iterates through each phone number and passes it to `NumberConverterAlgorithm` which finds the mapping word combinations. 

The brief description of the algorithm converting number to word combinations is

Iterate through each prefix one by one and search for matching word(s). If matching word(s) can be found use them, else see if the business rules allow the prefix digits to remain as-is
Make a recursive call to get all combinations of the remaining suffix.
Concatenate the matching combinations of the prefix and the suffix.

As matching combinations are found for each number, the class `NumbersConverter` sends it back to the `Main` class by a call back handler, which in turns displays the output on console to the user. Lastly the class `ApplicationFacade` as the name suggests brings all these pieces together.





