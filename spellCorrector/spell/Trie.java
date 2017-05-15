package spell;

/**
 * Created by jcdunnMac on 5/9/17.
 */


public class Trie implements ITrie {

    int wordCount;
    int nodeCount;
    Node rootNode;
    String trieString;

    public Trie()
    {
        wordCount = 0;
        nodeCount = 1;
        rootNode = new Node();
    }
    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     */
    public void add(String word)
    {
        addRec(word, rootNode);
    }
    void addRec(String word, Node node)
    {
        if(word.length() == 0) // base case
        {
            node.incrValue();
            if(node.getValue() == 1)
                wordCount++; // first occurance of word in Trie
        }
        else
        {
            char c = word.charAt(0);
            if(c < 97) // convert capital to lower case
            {
                c += 32;
            }
            int index = c - 97; // convert char to index, 'a' -> 0
            if(node.alphArray[index] == null) // no node made at that index
            {
                node.alphArray[index] = new Node(); // make a new node
                nodeCount ++;
            }
            addRec(word.substring(1), node.alphArray[index]);
        }
    }


    /**
     * Searches the trie for the specified word
     */
    public INode find(String word)
    {
        return findRec(word, rootNode);
    }

    INode findRec(String word, Node node)
    {
        char c = word.charAt(0);
        if(c < 97) // convert capital to lower case
        {
            c += 32;
        }
        int index = c - 97; // convert char to index, 'a' -> 0
        if (node.alphArray[index] == null) {

            //System.out.println("INDEX NOT ALLOCATED: " + c);
            return null;
        }
        else if(word.length() == 1 && (node.alphArray[index].getValue() != 0)) {
            //System.out.println("AT END OF WORD");
            return node.alphArray[index];
        }
        else if(word.length() == 1 && (node.alphArray[index].getValue() == 0)) {
            //System.out.println("NOT A WORD");
            return null;
        }
        else
        {
            //System.out.println("TRY NEXT INDEX");
            return findRec(word.substring(1), node.alphArray[index]);
        }
    }


    /*
     * Returns the number of unique words in the trie
     */
    public int getWordCount()
    {
        return wordCount;
    }

    /*
     * Returns the number of nodes in the trie
     */
    public int getNodeCount()
    {
        return nodeCount;
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     */
    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringRec(rootNode, word, output);
        return output.toString();
    }

    private void toStringRec(Node n, StringBuilder word, StringBuilder output) {
        if(n == null)
            return;

        if(n.getValue() > 0)
            output.append(word.toString() + "\n");

        for(int i = 0; i < 26; ++i) {
            word.append((char)(97 + i));
            toStringRec(n.alphArray[i], word, output);
            word.setLength(word.length() - 1);
        }
    }

    @Override
    public int hashCode()
    {
        return nodeCount * wordCount;
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

		Trie other = (Trie)o;
        // do some beef

        return equalsRec(rootNode, other.rootNode);

    }

    boolean equalsRec(Node node1, Node node2)
    {
        for (int i = 0; i < 26; i++) {
            if(node1.alphArray[i] == null && node2.alphArray[i] == null) {
                continue;
            }
            else if (node1.alphArray[i] == null || node2.alphArray[i] == null) {
                return false;
            }
            else if(node1.alphArray[i].getValue() != node1.alphArray[i].getValue()) {
                return false;
            }
            if(!equalsRec(node1.alphArray[i], node2.alphArray[i])) {
                return false;
            }
        }
        return true;
    }

    public class Node implements INode
    {
        /**
         * Returns the frequency count for the word represented by the node
         */

        int nodeValue;
        public Node[] alphArray;
        public Node(){
            nodeValue = 0;
            alphArray = new Node[26];
            for(int i = 0; i < 26; i++)
                alphArray[i] = null;
        }
        public int getValue()
        {
            return nodeValue;
        }
        public void incrValue()
        {
            nodeValue++;
        }
    }
}
