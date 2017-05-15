package hangman;

import java.io.CharArrayReader;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.Scanner;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;

/**
 * Created by jcdunnMac on 5/11/17.
 */



public class Main {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        int numGuess = Integer.parseInt(args[2]);

        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("Welcome to Evil Hangman");
        System.out.println("Your evil word length is:" + args[1]);
        System.out.println("You will have " + numGuess + " guesses");
        System.out.println("Game Starting...");

        System.out.println();
        System.out.println("--------------------------------------------------");

        System.out.println();
        System.out.println();

        EvilHangmanGame evilGame = new EvilHangmanGame();
        evilGame.startGame(new File(args[0]), Integer.parseInt(args[1]));

        int guessCount = 0;

        while(evilGame.getNumGuess() < numGuess)
        {
            System.out.println("Number of remaining Guesses: " + (Integer.parseInt(args[2]) - evilGame.getNumGuess()));
            System.out.println("Letters guessed: " + evilGame.getGuessString());


            System.out.println("Enter a letter guess: ");
            String g = reader.nextLine(); // Scans the next token of the input as an int.
            boolean input_error = false;
            if(g == "")
                input_error = true;
            else
                if(g.length() != 1)
                    input_error = true;
                else
                    if(!Character.isAlphabetic(g.charAt(0)))
                        input_error = true;


            if(!input_error) {
                System.out.println("You guessed: " + g);

                try {
                    Set<String> printSet = evilGame.makeGuess(g.charAt(0));

                    System.out.println();
                    System.out.println("--------------------------------------------------");
                } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                    System.out.println("You allready guessed " + g);
                }
                System.out.println("Word: \t" + evilGame.getGuessTemplate());
                if (evilGame.isWinner())
                    break;
            }
            else
            {
                System.out.println();
                System.out.println("User Input Error");
                System.out.println();
                System.out.println("--------------------------------------------------");
            }
        }
        if(evilGame.getNumGuess() == numGuess)
        {
            System.out.println("YOU LOSE!");
            Set<String> total = evilGame.getCurrentPartition();
            Iterator<String> it = total.iterator();
            System.out.println("The word was " + it.next());

        }
        else
        {
            System.out.println("You win?");
        }
    }
}
