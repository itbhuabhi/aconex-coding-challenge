package com.aconex.challenge.numbertowords.dictionary;

import java.util.Set;


/**
 * This is an interface which defines the contract for storing dictionary words and their number transformations
 * and retrieving them.
 * 
 * Every word would uniquely map to a number based on the alphabet to digit mapping.
 * So the dictionary will accept the word and the number form of the word and store it. 
 * And later for any number the dictionary can be searched to find matching words.
 * <p>Ex- Words like CALL, BALL and ME would translate to 2255,2255 and 63 respectively, 
 * based on the below number encoding<br>
 * A,B,C -&gt; 2	<br>
 * D,E,F -&gt; 3	<br>
 * G,H,I -&gt; 4 	<br>
 * J,K,L -&gt; 5 	<br>
 * M,N,O -&gt; 6 	<br>
 * P,Q,R,S -&gt; 7 <br>
 * T,U,V -&gt; 8	<br>
 * W,X,Y,Z -&gt; 9	<br><br>
 * 
 * The word is stored as translated number in the dictionary as the key. 
 * And the word itself is stored as one of the value of the mapped number. 
 * This is for the simple reason that word maps uniquely to a number while the other way round is not true.
 * So in this example number 2255 maps to values CALL and BALL
 * While 63 maps to ME.
 * Later say the dictionary encounters word which also maps to 63. 
 * Then the word OF would be added to the existing set of values of 63
 * Later when we are say searching for 2255 we can get all the possible actual dictionary words for it.
 * In the example given above for number 2255 the set of returned words would be CALL and BALL
 * 
 * @author Abhishek Agarwal
 *
 */

public interface Dictionary {
	/**
	 * Adds the uniquely mappedNumber to the dictionary and would maintain the corresponding set of words
	 * which mapped to this number as its value. 
	 * @param mappedNumber Uniquely mappedNumber to a word. Since this is the key so it becomes the first argument
	 * @param word The corresponding word
	 */
	void insert(String mappedNumber, String word);

	/**
	 * Returns the set of words mapping to a number. So in the example given {@link Dictionary above}
	 * If the argument is 2255 then the returned would value would be set BALL,CALL<br>
	 * If there are no matching words the implementation class can chose to return null or an empty set
	 * 
	 * @param number The number for which mapping words has to be found 
	 * @return The set of words which got converted to this number
	 */
	Set<String> findMatchingWords(String number);
	
}
