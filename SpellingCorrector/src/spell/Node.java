package spell;

public class Node implements INode
{
    private int freq = 0; //this is the frequency in which the node is used
    private Node[] children = new Node[26]; //for 26 letters in alphabet

    public int getValue()
    {
        return freq;
    }

    public void incrementValue()
    {
        freq++; //MAY NOT NEED THIS FUNCTION
    }

    public Node[] getChildren()
    {
        return children;
    }

    public Node getChild(int index) //should I optimize it and have it accessible?
    {
        return children[index];
    }

    public void createNode(int index)
    {
        children[index] = new Node(); //creates new Node at position symbolizing that letter
    }
}
