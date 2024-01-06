package spell;

public class Trie implements ITrie
{
    private Node root = new Node();
    private int nodeCount = 1; //this needs to be initialized to 1 if includes root
    private int wordCount = 0;
    private int charCntr = 0; //use this to parse string into chars for add method

    public void add(String word)
    {
        String LCWord = word.toLowerCase();
        //MAYBE check if empty string?
        insertChar(LCWord, root);
    }

    public void insertChar(String word, Node node)
    {
        //word = word.toLowerCase();
        if (Character.isLetter(word.charAt(charCntr))) //may want to use delimiter
        {
            //int charCntr = 0; //use this to parse string into chars
            int index = word.charAt(charCntr) - 'a'; //this will convert letter to appropriate int index
            //.substring(1) grabs 1 on in string
            if (node.getChild(index) != null) //if there is already a node here
            {
                if (charCntr < word.length() - 1) //if it isn't the end of the string
                {
                    charCntr++; //move on to next char
                    insertChar(word, node.getChild(index)); //recurse down into next node
                }
                else //this is the final node
                {
                    if (node.getChild(index).getValue() == 0) //say added "pi" after "pie"--node exists, but hasn't been final node yet
                    {
                        wordCount++;
                    }
                    node.getChild(index).incrementValue(); //DOES THIS WORK?
                    charCntr = 0; //reset counter for next string
                }
            }
            else //if there is not a node here yet
            {
                if (charCntr < word.length() - 1) //if it isn't the end of the string
                {
                    charCntr++; //move on to next char
                    node.createNode(index); //create a node for this character
                    nodeCount++;
                    insertChar(word, node.getChild(index)); //recurse down into next node
                }
                else //this is the final node
                {
                    node.createNode(index); //create a node for this character
                    nodeCount++;
                    node.getChild(index).incrementValue(); //DOES THIS WORK? Word is complete
                    wordCount++; //this is a new unique word
                    charCntr = 0; //reset counter for next string
                }
            }
        }
        //if it isn't a letter, will just abandon the word and recurse back up
    }

    public INode find(String word) //do I need the ITrie.?
    {
        word = word.toLowerCase();
        //MAYBE check if empty string?
        return nextNode(word, root);
    }

    public INode nextNode(String word, Node node)
    {
        //word.toLowerCase();
        if (Character.isLetter(word.charAt(charCntr))) //may want to use delimiter
        {
            //int charCntr = 0; //use this to parse string into chars
            int index = word.charAt(charCntr) - 'a'; //this will convert letter to appropriate int index
            if (node.getChild(index) != null) //if there is already a node here
            {
                if (charCntr < word.length() - 1) //if it isn't the end of the string
                {
                    charCntr++; //move on to next char
                    return nextNode(word, node.getChild(index)); //recurse down into next node
                }
                else //this is the final node
                {
                    charCntr = 0; //reset counter for next string
                    if (node.getChild(index).getValue() > 0)
                        return node.getChild(index); //found it
                    else
                        return null; //was just a prefix
                }
            }
            else //this node does not exist, so word is not found
            {
                charCntr = 0; //reset counter for next string
                return null;
            }
        }
        //if it isn't a letter, will just return null
        else
        {
            return null;
        }
    }

    public Node getRoot()
    {
        return root;
    }

    public int getWordCount()
    {
        return wordCount;
    }

    public int getNodeCount()
    {
        return nodeCount;
    }

    @Override
    public String toString()
    {
        StringBuilder word = new StringBuilder(); //this is used to construct current word
        StringBuilder dictionary = new StringBuilder(); //this is used to output all words in Trie

        for (int i = 0; i < 26; i++) //26 is the number of letters or nodes in a node
        {
            if (root.getChild(i) != null)
            {
                char letter = (char)(i + 'a'); //this turns index back into a char
                word.append(letter);
                wordBuilder(word, dictionary, root.getChild(i));
            }
            //don't do anything if it is null
        }
        return dictionary.toString();
    }

    public void wordBuilder(StringBuilder word, StringBuilder dictionary, Node node)
    {
        if (node.getValue() > 0)
        {
            dictionary.append(word + "\n"); //complete word
        }
        for (int i = 0; i < 26; i++) //26 is the number of letters or nodes in a node
        {
            if (node.getChild(i) != null)
            {
                char letter = (char)(i + 'a'); //this turns index back into a char
                word.append(letter);
                wordBuilder(word, dictionary, node.getChild(i));
            }
            //don't do anything if it is null
        }
        word.setLength(word.length() - 1); //delete this character as move back up recursion
        //word will always have at least one char available to delete at this point in recursion
    }

    @Override
    public int hashCode()
    {
        for (int i = 0; i < 26; i++)
        {
            if (root.getChild(i) != null)
            {
                return ((nodeCount << 16) * (wordCount << 8)) + i;
            }
        }
        return (nodeCount + wordCount);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        Trie other = (Trie)o; //cast as Trie
        //Trie other = ((Trie) o);

        if (other.getNodeCount() != nodeCount)
            return false;

        if (other.getWordCount() != wordCount)
            return false;

        for (int i = 0; i < 26; i++) //compares root arrays
        {
            if ((other.getRoot().getChild(i) == null) && (root.getChild(i) != null))
                return false;
            else if ((root.getChild(i) == null) && (other.getRoot().getChild(i) != null))
                return false;
        }

        //boolean traversalFlag = true;
        for (int i = 0; i < 26; i++) //now know that arrays are not null at same positions
        {
            if (root.getChild(i) != null) //then recurse down the nodes
            {
                //if these children are not equal
                if (!equalsTraversal(other.getRoot().getChild(i), root.getChild(i)))
                {
                    return false;
                }
                //otherwise move on
            }
        }
        return true; //at this point every node is equal
    }

    public boolean equalsTraversal(Node n1, Node n2)
    {
        if (n1.getValue() != n2.getValue())
            return false;
        for (int i = 0; i < 26; i++) //compares node arrays
        {
            if ((n1.getChild(i) == null) && (n2.getChild(i) != null))
                return false;
            else if ((n2.getChild(i) == null) && (n1.getChild(i) != null))
                return false;
        }
        for (int i = 0; i < 26; i++) //now know that arrays are not null at same positions
        {
            if (n1.getChild(i) != null) //then recurse down the nodes
            {
                //if these children are not equal
                if (!equalsTraversal(n1.getChild(i), n2.getChild(i)))
                {
                    return false;
                }
                //otherwise move on
            }
        }
        return true; //at this point every node is equal
    }
}

