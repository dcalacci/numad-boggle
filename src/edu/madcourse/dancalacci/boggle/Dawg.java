package edu.madcourse.dancalacci.boggle;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Arrays;

public class Dawg {
	
	private static final int CHILD_BIT_SHIFT = 5;
	private static final int CHILD_INDEX_BIT_MASK = 0X003FFFE0;
	private static final int LETTER_BIT_MASK = 0X0000001F;
	private static final int END_OF_WORD_BIT_MASK = 0X00800000;
	private static final int END_OF_LIST_BIT_MASK = 0X00400000;
	private static final int INPUT_SIZE_LIMIT = 100;
	private static final char LOWER_IT = 32;

	private int numberOfNodes;

	private int[] theDawgArray;
	
	public Dawg() throws Exception {

	    //DataInputStream dawgDataFile = new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream("Traditional_Dawg_For_Word-List.dat")));
	    DataInputStream dawgDataFile = new DataInputStream(new BufferedInputStream(new FileInputStream("Traditional_Dawg_For_Word-List.dat")));
	    numberOfNodes = endianConversion(dawgDataFile.readInt());
		theDawgArray = new int[numberOfNodes];
		
		for (int x = 0; x < numberOfNodes; x++) {
			theDawgArray[x] = endianConversion(dawgDataFile.readInt());
		}
		dawgDataFile.close();
	}

	private int endianConversion(int thisInteger) {
		return ((thisInteger & 0x000000ff) << 24) + ((thisInteger & 0x0000ff00) << 8) + ((thisInteger & 0x00ff0000) >>> 8) + ((thisInteger & 0xff000000) >>> 24);
	}
	
	// These methods are used to extract information from the "theDawgArray" nodes.
	private char nodeLetter(int index) {
		return (char)((theDawgArray[index]&LETTER_BIT_MASK) + 'A');
	}
	private boolean nodeEndOfWord(int index) {
		return ((theDawgArray[index]&END_OF_WORD_BIT_MASK) != 0);
	}
	private int nodeNext(int index) {
		return ((theDawgArray[index]&END_OF_LIST_BIT_MASK) == 0)? (index + 1): 0;
	}
	private int nodeChild(int index) {
		return ((theDawgArray[index]&CHILD_INDEX_BIT_MASK)>>>CHILD_BIT_SHIFT);
	}

	private String searchForStringRecurse(String thisString, int position, int thisIndex, boolean[] result) {
		int currentIndex = thisIndex;
		char currentChar = thisString.charAt(position);
		String addThisMessage = new String("----------------------------------------\n");
		addThisMessage += "Seek |" + currentChar + "| in position |" + position + "|.\n";
		String returnHolder;
		while ( currentIndex != 0 ) {
			addThisMessage += "Node|" + currentIndex + "| Letter|" + nodeLetter(currentIndex) + "| "; 
			if ( currentChar > nodeLetter(currentIndex) ) {
				currentIndex = nodeNext(currentIndex);
				addThisMessage += "- Letter too small.\n";
			}
			else if ( currentChar < nodeLetter(currentIndex) ) {
				result[0]= false; 
				return (addThisMessage + "- Letter too big\n\nWord Not Found\n");
			}
			else if ( thisString.length() == (position + 1) ) {
				addThisMessage += "= Letter match.\n";
				if ( nodeEndOfWord(currentIndex) ) {
					result[0] = true;
					return (addThisMessage + "\nWord Found.\n");
				}
				else {
					result[0] = false;
					return (addThisMessage + "\nWord Not Found.\n");
				}
			}
			else {
				addThisMessage += "= Letter match.\n";
				returnHolder = searchForStringRecurse(thisString, position + 1, nodeChild(currentIndex), result);
				addThisMessage += returnHolder;
				return addThisMessage;
			}
		}
		result[0] = false;
		return (addThisMessage + "Reached end of list.\n\nWord Not Found\n");
	}

	public String searchForString(String toSearchFor) {
		boolean[] found = new boolean[1];
		String holder;
		String upperString = toSearchFor.toUpperCase();
		String traversalResult = new String("Searching for:  |" + upperString + "| - ");
		found[0] = false;
		holder = searchForStringRecurse(upperString, 0, (upperString.charAt(0) - 'A' + 1), found);
		if ( found[0] ) traversalResult += "Word Found.\n";
		else traversalResult += "Word Not Found.\n";
		traversalResult += holder;
		return traversalResult;
	}
	
	private String searchForPatternRecurse(String emptyPattern, int position, int thisIndex, char[] thePattern, int[] tally) {
		int currentIndex = thisIndex;
		String addThisMessage = "";
		String returnHolder;
		char currentChar = emptyPattern.charAt(position);
		while ( currentIndex != 0 ) {
			if ( currentChar == '?') {
				thePattern[position] = nodeLetter(currentIndex);
				thePattern[position] += LOWER_IT;
				if ( (position == (emptyPattern.length() - 1)) ) {
					if ( nodeEndOfWord(currentIndex) ) {
						tally[0] += 1;
						addThisMessage += "|" + tally[0] + "| - " + new String(thePattern, 0, position + 1) + "\n";
					}
				}
				else {
					returnHolder = searchForPatternRecurse(emptyPattern, position + 1, nodeChild(currentIndex), thePattern, tally);
					addThisMessage += returnHolder;
				}
				currentIndex = nodeNext(currentIndex);
			}
			else if ( currentChar > nodeLetter(currentIndex) ) {
				currentIndex = nodeNext(currentIndex);
			}
			else if ( currentChar < nodeLetter(currentIndex) ) {
				break;
			}
			else if ( (position == (emptyPattern.length() - 1)) ) {
				if ( nodeEndOfWord(currentIndex) ) {
					thePattern[position] = nodeLetter(currentIndex);
					tally[0] += 1;
					addThisMessage = "|" + tally[0] + "| - " + new String(thePattern, 0, position + 1) + "\n";
					return addThisMessage;
				}
				break;
			}
			else {
				thePattern[position] = nodeLetter(currentIndex);
				addThisMessage = searchForPatternRecurse(emptyPattern, position + 1, nodeChild(currentIndex), thePattern, tally);
				return addThisMessage;
			}
		}
		return addThisMessage;
	}
	
	public String searchForPattern(String thisPattern) {
		int[] counter = new int[1];
		String holder = "";
		String upperString = thisPattern.toUpperCase();
		String traversalResult = new String("Pattern:  |" + upperString + "| - ");
		char[] runningPattern = new char[upperString.length()];
		counter[0] = 0;
		if ( upperString.charAt(0) != '?' ) holder += searchForPatternRecurse(upperString, 0, (upperString.charAt(0) - '@'), runningPattern, counter);
		else holder += searchForPatternRecurse(upperString, 0, 1, runningPattern, counter);
		traversalResult += counter[0] + " Words fit:\n\n";
		traversalResult += holder;
		return traversalResult;
	}
	
	// This method is the core program component.  It requires that "unusedChars" be in alphabetical order because the traditional "Dawg" is a list based structure.
	private String anagramRecurse(int currentIndex, char[] toyWithMe, int fillThisPosition, char[] unusedChars, int sizeOfBank, int[] forTheCounter, boolean onWildcard){
		char previousChar = '\0';
		char currentChar;
		int tempIndex = nodeChild(currentIndex);
		String holder;
		String wordAccumulator = "";
		
		toyWithMe[fillThisPosition] = nodeLetter(currentIndex);
		if ( onWildcard ) toyWithMe[fillThisPosition] += LOWER_IT;
		if ( nodeEndOfWord(currentIndex) ) {
			forTheCounter[0] += 1;
			wordAccumulator = new String(toyWithMe, 0, fillThisPosition + 1);
			wordAccumulator = "|" + forTheCounter[0] + "| - " + wordAccumulator + "";
			if ( sizeOfBank == 0 ) wordAccumulator += " ********->\n";
			else wordAccumulator +="\n";
		}
		
		if ( (sizeOfBank > 0) && (tempIndex != 0) ) {
			for ( int x = 0; x < sizeOfBank; x++ ) {
				currentChar = unusedChars[x];
				if ( currentChar == previousChar ) continue;
				do {
					if ( currentChar == nodeLetter(tempIndex) || currentChar == '?') {
						removeCharFromArray(unusedChars, x, sizeOfBank);
						holder = anagramRecurse(tempIndex, toyWithMe, fillThisPosition + 1, unusedChars, sizeOfBank - 1, forTheCounter, (currentChar == '?')? true: false);
						wordAccumulator += holder;
						insertCharIntoArray(unusedChars, x, currentChar, sizeOfBank);
						if ( currentChar != '?' ) {
							tempIndex = nodeNext(tempIndex);
							break;
						}
					}
					else if ( currentChar < nodeLetter(tempIndex) ) break;
				} while ( (tempIndex = nodeNext(tempIndex)) != 0 );
				if ( currentChar == '?' ) tempIndex = nodeChild(currentIndex);
				if ( tempIndex == 0 ) break;
				previousChar = currentChar;
			}
		}
		return wordAccumulator;
	}
	
	// The "toScrambleUp" String may contain '?' wildcards, so indicate these wildcards as lower case letters.
	public String anagram(String toScrambleUp) {
		String upperString = toScrambleUp.toUpperCase();
		int numberOfLetters = upperString.length();
		char[] inputCharArray = new char[INPUT_SIZE_LIMIT];
		char[] theWordSoFar = new char[INPUT_SIZE_LIMIT];
		upperString.getChars(0, numberOfLetters, inputCharArray, 0);
		Arrays.sort(inputCharArray,0,numberOfLetters);
		String traversalResult = "";
		String holder;
		
		char previousChar = '\0';
		char currentChar;
		int[] forTheCount = new int[1];
		forTheCount[0] = 0;
		
		for ( int x = 0; x < numberOfLetters; x++){
			currentChar = inputCharArray[x];
			if ( currentChar == previousChar ) continue;
			removeCharFromArray(inputCharArray, x, numberOfLetters);
			if ( currentChar != '?' ) {
				holder = "-----------------------------\n";
				holder += anagramRecurse(currentChar - '@', theWordSoFar, 0, inputCharArray, numberOfLetters - 1, forTheCount, false);
				traversalResult += holder;
			}
			else {
				for ( int y = 'A'; y <= 'Z'; y++  ) {
					holder = "-----------------------------\n";
					holder += anagramRecurse(y - '@', theWordSoFar, 0, inputCharArray, numberOfLetters - 1, forTheCount, true);
					traversalResult += holder;
				}
			}
			insertCharIntoArray(inputCharArray, x, currentChar, numberOfLetters);
			previousChar = currentChar;
		}
		traversalResult = "Anagramming this:  |" + upperString + "|\nResults in |" + forTheCount[0] + "| words.\n" + traversalResult;
		return traversalResult;
	}
	
	private void removeCharFromArray(char[] thisArray, int thisPosition, int size) {
		System.arraycopy(thisArray, thisPosition + 1, thisArray, thisPosition, (size - thisPosition - 1));
	}
	
	private void insertCharIntoArray(char[] thisArray, int thisPosition, char thisChar, int size) {
		System.arraycopy(thisArray, thisPosition, thisArray, thisPosition + 1, (size - thisPosition - 1));
		thisArray[thisPosition] = thisChar;
	}
}