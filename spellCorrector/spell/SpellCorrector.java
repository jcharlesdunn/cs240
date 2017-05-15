package spell;

import java.util.Scanner;
import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by jcdunnMac on 5/9/17.
 */

public class SpellCorrector implements ISpellCorrector {


    public Trie trieDict;
    public SpellCorrector()
    {
        trieDict = new Trie();
    }

    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     */
    public void useDictionary(String dictionaryFileName) throws IOException
    {
        Scanner myscan = null;
        // delimiters for this lab?
        myscan = new Scanner(new File(dictionaryFileName)).useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");

        // populate Trie with data from dictionaryFileName
        while(myscan.hasNext()) {
            String temp = myscan.nextLine();
            trieDict.add(temp);
        }
        myscan.close();

        //System.out.println("Word count: " + trieDict.getWordCount());
        //System.out.println("Node count: " + trieDict.getNodeCount() + "\n");
    }


    /**
     * Suggest a word from the dictionary that most closely matches
     */
    public String suggestSimilarWord(String inputWord)
    {
        if(trieDict.find(inputWord) == null)
        {
            //System.out.println("WORD NOT FOUND");
            Set<String> dist1Set = new HashSet<String>();
            Set<String> dist2Set = new HashSet<String>();
            Set<String> suggestSet = new HashSet<String>();

            // Deletion Distance 1
            //System.out.println("DeletionDistance1");
            dist1Set.addAll(deleteDist(inputWord));

            //Transposition Distance 1
            //System.out.println("TranspositionDistance1");
            dist1Set.addAll(transDist(inputWord));

            // Alteration Distance 1
            //System.out.println("AlterationDistance1");
            dist1Set.addAll(altDist(inputWord));


            // Insertion Distance 1
            //System.out.println("InsertionDistance1");
            dist1Set.addAll(insertDist(inputWord));

            // filter distance 1 set
            for(String s : dist1Set)
            {
                if(trieDict.find(s) != null)
                    suggestSet.add(s);
            }



            // if its empty, do distance 2
            if(suggestSet.isEmpty())
            {
                for(String s : dist1Set)
                {
                    // Deletion Distance 2
                    //System.out.println("DeletionDistance2");
                    dist2Set.addAll(deleteDist(s));

                    //Transposition Distance 2
                    //System.out.println("TranspositionDistance2");
                    dist2Set.addAll(transDist(s));

                    // Alteration Distance 2
                    //System.out.println("AlterationDistance2");
                    dist2Set.addAll(altDist(s));


                    // Insertion Distance 2
                    //System.out.println("InsertionDistance2");
                    dist2Set.addAll(insertDist(s));
                }

                // filter distance 1 set
                for(String s : dist2Set)
                {
                    if(trieDict.find(s) != null)
                        suggestSet.add(s);
                }
            }

            if(suggestSet.isEmpty())
            {
                return null;
            }
            else
            {
                //System.out.println("SUGGEST SET");
                //for (String s : suggestSet) {
                //    System.out.println(s);
                //}

                // take suggest set and find most common word
                int highVal = 0;
                String highString = "";
                for (String s : suggestSet) {
                    int currVal = trieDict.find(s).getValue();
                    if (currVal == highVal) {
                        if (highString.compareTo(s) > 0) {
                            highString = s;
                        }
                    }
                    if (currVal > highVal) {
                        highVal = currVal;
                        highString = s;
                    }
                }
                //System.out.println("\nSUGGESTED WORD: " + highString);
                for(int i = 0; i < highString.length(); i++)
                {
                    char[] c = highString.toCharArray();
                    if(c[i] < 97) // convert capital to lower case
                    {
                        c[i] += 32;
                    }

                    highString = new String(c);
                }
                return highString;

            }
        }
        else
        {
            // Convert word to lower case and return it
            for(int i = 0; i < inputWord.length(); i++)
            {
                char[] c = inputWord.toCharArray();
                if(c[i] < 97) // convert capital to lower case
                {
                    c[i] += 32;
                }

                return new String(c);
            }
        }





        //System.out.println("\nPRINT ALL WORDS");
        //System.out.println(trieDict.toString());
        return null;
    }

    Set<String> deleteDist(String word)
    {
        Set<String> deleteSet = new HashSet<String>();
        if(word.length() > 1) {
            for (int i = 0; i < word.length(); i++) {
                StringBuilder sb = new StringBuilder(word);
                sb.deleteCharAt(i);
                //System.out.println(sb.toString());
                deleteSet.add(sb.toString());
            }
        }
        return deleteSet;
    }

    Set<String> transDist(String word)
    {
        Set<String> tranSet = new HashSet<String>();
        for(int i = 0; i < word.length() - 1; i++)
        {
            char[] c = word.toCharArray();

            // Replace with a "swap" function, if desired:
            char temp = c[i];
            c[i] = c[i+1];
            c[i+1] = temp;

            String trans = new String(c);
            tranSet.add(trans);
            //System.out.println(trans);
        }
        return tranSet;
    }

    Set<String> altDist(String word)
    {
        Set<String> altSet = new HashSet<String>();
        for(int i = 0; i < word.length(); i++)
        {
            char[] c = word.toCharArray();
            for(int j = 0; j < 26; j++) {
                c[i] = (char) (j + 'a');
                String alt = new String(c);
                //System.out.println(alt);
               altSet.add(alt);
            }

        }
        return altSet;
    }

    Set<String> insertDist(String word)
    {
        Set<String> insertSet = new HashSet<String>();
        for(int i = 0; i <= word.length(); i++)
        {
            for(int j = 0; j < 26; j++) {
                char c = (char) (j + 'a');
                String ins = new StringBuilder(word).insert(i, c).toString();
                //System.out.println(ins);
                insertSet.add(ins);
            }

        }
        return insertSet;
    }




}
