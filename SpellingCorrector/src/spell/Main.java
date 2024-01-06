package spell;

import java.io.IOException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {

	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws IOException {

		String dictionaryFileName = args[0];
		String inputWord = args[1];

		/**
		 * Create an instance of your corrector here
		 */
		ISpellCorrector corrector = new SpellCorrector(); //changed from = null

		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		if (suggestion == null) {
			suggestion = "No similar word found";
		}

		System.out.println("Suggestion is: " + suggestion);
		/*
		ISpellCorrector corrector2 = new SpellingCorrector();
		corrector2.useDictionary(dictionaryFileName);
		if (corrector.)



		Trie t1 = new Trie();
		Trie t2 = new Trie();

		t1.add("tool");
		t1.add("tool");
		t1.add("a");
		t2.add("tool");

		System.out.println(t1.toString());

		if (t1.find("too") != null)
			System.out.println("oops");
		if(t1.equals(t2))
			System.out.println("Dictionaries are: equal");
		else
			System.out.println("Dictionaries are: not equal");
		*/
	}

}
