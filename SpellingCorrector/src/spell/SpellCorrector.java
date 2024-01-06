package spell;

//import ISpellCorrector;

import java.io.File;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;


public class SpellCorrector implements ISpellCorrector
{
    //private TreeSet<String> suggestionsDist1 = new TreeSet<String>(); //first set of suggestions of distance 1
    //private TreeSet<String> suggestionsDist2 = new TreeSet<String>(); //second set of suggestions of distance 2
    //can't have these here because test driver runs methods multiple times w/out making a new SpellCorrector each time
    //so the TreeSets keep remembering alterations from previous tests
    private Trie dictionary = new Trie();

    public void useDictionary(String dictionaryFileName)
    {
        try
        {
            File myFile = new File(dictionaryFileName);
            Scanner scan = new Scanner(myFile);

            while (scan.hasNext())
            {
                dictionary.add(scan.next());
            }
            scan.close();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String suggestSimilarWord(String inputWord)
    {
        TreeSet<String> suggestionsDist1 = new TreeSet<String>(); //first set of suggestions of distance 1
        TreeSet<String> suggestionsDist2 = new TreeSet<String>(); //second set of suggestions of distance 2
        if (inputWord.equals(""))
            return null;
        String LCInputWord = inputWord.toLowerCase();
        INode notWrong = dictionary.find(LCInputWord);
        if (notWrong != null) //if the original word was in dictionary and not wrong, return that word
            return LCInputWord;
        String bestSuggestion = new String();
        int maxFreq = 0;
        deletion(LCInputWord, suggestionsDist1);
        transposition(LCInputWord, suggestionsDist1);
        alteration(LCInputWord, suggestionsDist1);
        insertion(LCInputWord, suggestionsDist1);

        /*
        Iterator itr = suggestionsDist1.iterator();
        while (itr.hasNext())
        {
            dictionary.find(itr.next());
        }
        */
        for(String suggestion : suggestionsDist1)
        {
            INode node = dictionary.find(suggestion);
            if (node != null) //if it is in dictionary
            {
                if (maxFreq < node.getValue()) //and if more frequent, then current best suggestion
                {
                    maxFreq = node.getValue(); //update max and best suggestion
                    bestSuggestion = suggestion;
                    //since it iterates through set, ties will go to alphabetical precedence
                }
            }
        }

        if (bestSuggestion.isEmpty()) //if no suggestion was found with distance 1, try distance 2
        {
            maxFreq = 0; //reset maxFreq
            for (String suggestion : suggestionsDist1)
            {
                deletion(suggestion, suggestionsDist2);
                transposition(suggestion, suggestionsDist2);
                alteration(suggestion, suggestionsDist2);
                insertion(suggestion, suggestionsDist2);
            }

            for(String suggestion : suggestionsDist2)
            {
                INode node = dictionary.find(suggestion);
                if (node != null) //if it is in dictionary
                {
                    if (maxFreq < node.getValue()) //and if more frequent, then current best suggestion
                    {
                        maxFreq = node.getValue(); //update max and best suggestion
                        bestSuggestion = suggestion;
                        //since it iterates through set, ties will go to alphabetical precedence
                    }
                }
            }
        }

        if (!bestSuggestion.isEmpty()) //if a bestSuggestion was found in dictionary
            return bestSuggestion;
        else
            return null; //no suggestion found within distance 2
    }

    public void deletion(String word, TreeSet<String> set)
    {
        if (word.length() > 1)//don't want to pass in an empty string
        {
            StringBuilder altWord = new StringBuilder(word);
            //String altWord = new String(word);
            for (int i = 0; i < word.length(); i++)
            {
                altWord.deleteCharAt(i);
                //tries to find each combination of one letter deletion of word in dictionary
               /*
                if (dictionary.find(altWord.toString()) != null) {
                    set.add(word.substring(i, word.length() - 1));
                }
                */
                //adds each combination of one letter deletion to set
                set.add(altWord.toString());
                //altWord.delete(0, altWord.length()); //reset altWord
                altWord.setLength(0);
                altWord.append(word);
            }
        }
    }

    public void transposition(String word, TreeSet<String> set)
    {
        //if (word.length() > 1) //can't transpose 1 char

        StringBuilder altWord = new StringBuilder(word);
        //only want to go to last two characters and can't transpose 1 char
        for (int i = 0; i < word.length() - 1; i++)
        {
            //StringBuilder altWord = new StringBuilder(word);
            StringBuilder reverseString = new StringBuilder(altWord.substring(i, i+2)); //ENDING IS EXCLUSIVE
            reverseString.reverse(); //switches these two letters
            altWord.replace(i, i+2, reverseString.toString()); //replaces these two letters
            set.add(altWord.toString());
            altWord.delete(0, altWord.length()); //reset altWord
            altWord.append(word);
        }

    }

    public void alteration(String word, TreeSet<String> set)
    {
        StringBuilder altWord = new StringBuilder (word);
        for (int i = 0; i < word.length(); i++)
        {
            char original = altWord.charAt(i);
            for (int j = 0; j < 26; j++) //for each letter
            {
                char letter = (char)('a' + j);
                if(letter != original) //only do this if different letter
                {
                    altWord.setCharAt(i, letter);
                    set.add(altWord.toString());
                }
            }
            altWord.setCharAt(i, original); //reset altWord
        }
    }

    public void insertion(String word, TreeSet<String> set)
    {
        StringBuilder altWord = new StringBuilder (word);
        for (int i = 0; i <= word.length(); i++) //<= otherwise won't insert at end of word
        {
            for (int j = 0; j < 26; j++) //for each letter
            {
                char letter = (char)('a' + j);
                altWord.insert(i, letter); //inserts letter into position i and increases length by 1
                set.add(altWord.toString());
                altWord.deleteCharAt(i); //reset altWord
            }

        }
    }

    public Trie getDictionary()
    {
        return dictionary;
    }
}

