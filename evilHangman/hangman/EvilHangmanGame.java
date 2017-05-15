package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by jcdunnMac on 5/11/17.
 */

public class EvilHangmanGame implements IEvilHangmanGame{


    Set<String> currentPartition;
    int wordLength;
    int maxGuess;
    int numGuess;
    byte[] guessArray;
    char evilTemplate[];

    public EvilHangmanGame(){}

    int wordContainsGuess(String t, char g)
    {
        int cnt = 0;
        for(int i = 0; i < t.length(); i++)
            if(t.charAt(i) == g)
                cnt++;
        return cnt;
    }



    public String getGuessString()
    {
        char c = 'a';
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < guessArray.length; i++)
        {
            if(guessArray[i] == 1)
            {
                sb.append(c);
                sb.append(" ");
            }
            c++;
        }
        return sb.toString();
    }

    public String getGuessTemplate()
    {
        return new String(evilTemplate);
    }


    public byte[] getGuessArray()
    {
        return guessArray;
    }
    public int getNumGuess()
    {
        return numGuess;
    }

    public void incrNumGuess()
    {
        numGuess++;
    }

    public boolean isWinner()
    {
        for(int i = 0; i < evilTemplate.length; i++)
            if(!Character.isAlphabetic(evilTemplate[i]))
                return false;
        return true;
    }

    public Set<String> getCurrentPartition()
    {
        return currentPartition;
    }

    int getNumSpaces(String t)
    {
        int cnt = 0;
        for(int i = 0; i < t.length(); i++)
            if(t.charAt(i) == '-')
                cnt++;
        return cnt;
    }











    @SuppressWarnings("serial")
    public static class GuessAlreadyMadeException extends Exception
    {

    }

    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     *	This method should set up everything required to play the game,
     *	but should not actually play the game. (ie. There should not be
     *	a loop to prompt for input from the user.)
     */
    public void startGame(File dictionary, int wordLength)
    {
        Scanner myscan = null;
        try {
            myscan = new Scanner(dictionary).useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
        } catch (FileNotFoundException e) {
            System.err.println("USAGE: java hangman.main 'filename'.txt numLetters numGuesses");
            e.printStackTrace(System.out);
        }

        Set<String> startSet = new HashSet<String>();
        this.wordLength = wordLength;
        this.numGuess = 0;

        guessArray = new byte[26];
        evilTemplate = new char[wordLength];
        for(int i = 0; i < wordLength; i++)
        {
            evilTemplate[i] = '-';
        }
        for(byte b : guessArray)
        {
            b = 0;
        }

        while(myscan.hasNextLine())
        {
            String s = myscan.nextLine();
            if(s.length() == wordLength)
            {
                if(s.equals("irresponsibilities")) // i r e s p o n b l t     ||   i t e r v e s n o
                    System.out.println("WJKLHEWJKGDJKSBFSBDFBDSHFBAJKLDFNBKJFGBJKLGBGGBJKLFNjksdfjkbfjkbfhfgkbhklfbsjkfbjk");
                startSet.add(s);
            }
        }
        currentPartition = new HashSet<String>();
        currentPartition.addAll((startSet));
    }


    /**
     * Make a guess in the current game.
     */
    public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException
    {
        if(guess < 97) // convert capital to lower case
        {
            guess += 32;
        }
        guess -= 'a';
        if(guessArray[guess] == 1)
            throw new IEvilHangmanGame.GuessAlreadyMadeException();

        guessArray[guess] = 1;
        // make partitions

        guess += 'a'; // back to char : o

        List<String> templates = new ArrayList<String>();
        HashMap<String,Set<String>> partitionMap;
        partitionMap = new HashMap<String,Set<String>>();
        partitionMap.put(new String(evilTemplate), new HashSet<String>());

        templates.add(new String(evilTemplate));

        char temp[] = new char[wordLength];

        for(String s : currentPartition)
        {
            boolean isNewTemp = false;
            for(int i = 0; i < wordLength; i++)
                temp[i] = evilTemplate[i];
            for(int i = 0; i < s.length(); i++)
            {
                if(!Character.isAlphabetic(temp[i])) // if the template is non alpha
                {
                    if(s.charAt(i) == guess)
                    {
                        // set template
                        temp[i] = guess;

                        // set bool
                        isNewTemp = true;
                    }
                }
            }
            // if the bool is true, add word to set in map
            if(isNewTemp)
            {
                String t = new String(temp);
                if(!partitionMap.containsKey(t))
                {
                    Set<String> tempSet = new HashSet<String >();
                    tempSet.add(s);
                    templates.add(t);
                    partitionMap.put(t,tempSet);
                }
                else
                {
                    partitionMap.get(t).add(s);
                }
            }
            else // add it outer set
            {
                partitionMap.get(new String(evilTemplate)).add(s);
            }
        }


        for(String s : templates)         //********************************************************
            System.out.println(s + " contains: " + partitionMap.get(s).size() + " elements");

        // Choose    the largest of these word groups to replace L
        int maxSize = 0;
        String maxKey = new String();
        Map<String, Set<String>> tempMap = new HashMap<String, Set<String>>();
        for(String s : templates)
        {
            if (partitionMap.get(s).size() > maxSize)
            {
                tempMap.clear();
                maxSize = partitionMap.get(s).size();
                maxKey = s;
                tempMap.put(s,partitionMap.get(s));
            }
            else if((partitionMap.get(s).size() != 0) && (partitionMap.get(s).size() == maxSize))
            {
                tempMap.put(s,partitionMap.get(s));
            }
        }
        if(tempMap.size() == 1) {
            //System.out.println("SIZE OF NEW SET: " + partitionMap.get(maxKey).size());
            for(int i = 0; i < wordLength; i++)
                evilTemplate[i] = maxKey.charAt(i);
            currentPartition.clear();
            currentPartition.addAll(partitionMap.get(maxKey));
            if(wordContainsGuess(maxKey,guess) == 0)
            {
                System.out.println("Sorry, there are no " + guess + "'s");
                incrNumGuess();
            }
            else
            {
                System.out.println("Yes, there is " + wordContainsGuess(maxKey,guess) + " " + guess);
            }
            return partitionMap.get(maxKey);
        }
        else
        {


            // if there are two of the same,
            // now select the set to return based off of follwing criteria

            // 1. Choose the group in which the letter does not appear at all.

            for (String s : tempMap.keySet()) {
                boolean letterAppears = false;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == guess)
                        letterAppears = true;
                }
                if (!letterAppears)
                {
                    //System.out.println("SIZE OF NEW SET: " + partitionMap.get(s).size());
                    for(int i = 0; i < wordLength; i++)
                        evilTemplate[i] = s.charAt(i);
                    currentPartition.clear();
                    currentPartition.addAll(partitionMap.get(s));
                    if(wordContainsGuess(maxKey,guess) == 0)
                    {
                        System.out.println("Sorry, there are no " + guess + "'s");
                        incrNumGuess();
                    }
                    else
                    {
                        System.out.println("Yes, there is " + wordContainsGuess(maxKey,guess) + " " + guess);
                    }
                    return partitionMap.get(s);
                }
            }

            // 2. If each group has the guessed letter, choose the one with the fewest letters.
            int maxSpaces = 0;
            boolean isUnique = true;

            for (String s : tempMap.keySet()) {
                int cnt = 0;
                for(int i = 0; i < s.length(); i++)
                    if(s.charAt(i) == '-')
                        cnt++;
                if(cnt > maxSpaces)
                {
                    maxSpaces = cnt;
                    maxKey = s;
                }
                else if(cnt == maxSpaces)
                {
                    isUnique = false;
                }
            }
            if(isUnique)
            {
                //System.out.println("SIZE OF NEW SET: " + partitionMap.get(maxKey).size());
                for(int i = 0; i < wordLength; i++)
                    evilTemplate[i] = maxKey.charAt(i);
                currentPartition.clear();
                currentPartition.addAll(partitionMap.get(maxKey));
                if(wordContainsGuess(maxKey,guess) == 0)
                {
                    System.out.println("Sorry, there are no " + guess + "'s");
                    incrNumGuess();
                }
                else
                {
                    System.out.println("Yes, there is " + wordContainsGuess(maxKey,guess) + " " + guess);
                }
                return partitionMap.get(maxKey);
            }

            // 3. If this still has not resolved the issue, choose the one with the rightmost guessed letter.
            // 4. If there is still more than one group, choose the one with the next
            // rightmost letter. Repeat this step (step 4) until a group is chosen.

            boolean setSelected = false;
            isUnique = true;

            while(!setSelected)
            {
                for(int j = wordLength - 1; j >= 0; j--)
                {
                    int numUnique = 0;
                    for(String s : tempMap.keySet())
                    {
                        if(getNumSpaces(s) == maxSpaces)
                        {
                            if(s.charAt(j) == guess)
                            {
                                numUnique++;
                                maxKey = s;
                            }
                        }
                    }
                    if(numUnique == 1)
                    {
                        //System.out.println("SIZE OF NEW SET: " + partitionMap.get(maxKey).size());
                        for(int i = 0; i < wordLength; i++)
                            evilTemplate[i] = maxKey.charAt(i);
                        currentPartition.clear();
                        currentPartition.addAll(tempMap.get(maxKey));
                        if(wordContainsGuess(maxKey,guess) == 0)
                        {
                            System.out.println("Sorry, there are no " + guess + "'s");
                            incrNumGuess();
                        }
                        else
                        {
                            System.out.println("Yes, there is " + wordContainsGuess(maxKey,guess) + " " + guess);
                        }
                        return partitionMap.get(maxKey);
                    }
                }
            }
        }
        return null;
    }


}
