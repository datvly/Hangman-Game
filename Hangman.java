import java.util.Scanner;

// A program that plays the game of hangman using while loops, parameters,
// and string methods from recent topics.
public class Hangman {

    // Change this if you want to change the number of wrong guesses allowed
    public static final int MAX_WRONG_GUESSES = 5;

    // Playing the entire game of hangman with the phrase bank.
    public static void main(String[] args) {
        intro();
        PhraseBank phrases = buildPhraseBank(args);
        Scanner keyboard = new Scanner(System.in);
        wholeGame(phrases, keyboard);
        keyboard.close();
    }


    // Check if the guesses matches the secret word
    // We stored the guessedword in the result String
    public static String mysteriousWord(String secretWord, String guessedLetters) {
        String result = "";
        int lengthOfWord = secretWord.length();
        for (int i = 0; i < lengthOfWord; i++) {
            String letter = secretWord.substring(i, i + 1); //pull out the letter we want
            if (guessedLetters.contains(letter)) { // check if the letter is in the guessed string
                result += letter;
            } else if (letter.equals("_")) {
                result += "_";
            } else {
                result += "*";
            }
        }
        return result;
    }


    // Update the alphabet via printing the only necessary letters
    // and using string concatenation.
    public static String updateAlphabet(String alphabet, String fixedInput) {
        int pulloutLetter = alphabet.indexOf(fixedInput);   //Get the index from userinput
        String updateAlphabet = alphabet.substring(0, pulloutLetter);
        updateAlphabet += alphabet.substring(pulloutLetter + 1);
        return updateAlphabet;
    }


    //Print out the not guessed letters
    public static void notGuessed(String alphabet) {
        System.out.println("The letters you have not guessed yet are: ");
        for (int i = 0; i < alphabet.length() - 1; i++) {
            System.out.print(alphabet.charAt(i) + "--");
        }
        System.out.println(alphabet.charAt(alphabet.length() - 1));
        System.out.println();
    }


    // Gets the input from the player.
    // We will use this input in other methods.
    public static String userInput(Scanner keyboard) {
        System.out.print("Enter your next guess: ");
        String userInput = keyboard.nextLine();
        System.out.println();
        return userInput;
    }


    // Changes the input from the user to automatically
    // capitalize if they input a lowercase letter.
    public static String fixedInput(String userInput) {
        String fixedInput = userInput.substring(0, 1);
        fixedInput = fixedInput.toUpperCase();
        return fixedInput;
    }


    // Checking if the input from the player is valid
    // invalid input are such as !, *, _, and etc.
    // If the player try to enter the same letter twice,
    // it will also bring up as invalid guess.
    public static String invalidInput(Scanner keyboard, String alphabet) {
        String result = userInput(keyboard);
        String altResult = fixedInput(result);
        while (!(alphabet.contains(altResult))) {
            System.out.println(result + " is not a valid guess.");
            notGuessed(alphabet); // Print the amount of letters that are left
            result = userInput(keyboard);   //This will prompt the player to enter a guess again.
            altResult = fixedInput(result);
        }
        return altResult;
    }


    // Print out the line "You guessed" after user input
    public static void youGuessedBlank(String fixedInput) {
        System.out.println("You guessed " + fixedInput + ".");
        System.out.println();
    }


    // Deciding what to do when word guess is wrong or correct
    // It will display the amount of wrong guesses.
    public static int wrongOrCorrect(String mysteriousWord, String fixedInput, int wrongTracker) {
        if (!mysteriousWord.contains(fixedInput)) {
            youGuessedBlank(fixedInput);
            System.out.println("That is not present in the secret phrase.");
            wrongTracker++;
        } else {
            youGuessedBlank(fixedInput);
            System.out.println("That is present in the secret phrase.");
        }
        System.out.println();
        System.out.println("Number of wrong guesses so far: " + wrongTracker);
        return wrongTracker;
    }


    // Deciding to play the game again or not depending
    // on the input from the user.
    public static void wholeGame(PhraseBank phrases, Scanner keyboard) {
        boolean playAgain = true;
        while (playAgain) {
            playRound(phrases, keyboard);
            System.out.println("Do you want to play again?");
            System.out.print("Enter 'Y' or 'y' to play again: ");
            String endGame = keyboard.nextLine().toLowerCase();   //Lowercase if the player enter a capital y
            playAgain = endGame.equals("y");
        }
    }


    // The start of the game where the topic is displayed
    // and the secret word is created to be used in another method.
    public static String getTopicAndPhrase(PhraseBank phrases) {
        String topic = phrases.getTopic();
        System.out.println();
        System.out.println("I am thinking of a " + topic + " ...");
        System.out.println();
        return phrases.getNextPhrase();
    }


    // The core of it all as it plays each round
    public static void playRound(PhraseBank phrases, Scanner keyboard) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String secretWord = getTopicAndPhrase(phrases);
        String guessedLetters = "";
        int wrongTracker = 0;  //cumulative sum of the amount of wrong guesses.
        boolean win = win(secretWord, guessedLetters);
        while (!win && wrongTracker != MAX_WRONG_GUESSES) {   //the game will continue playing if these conditions are not met
            System.out.println("The current phrase is " + mysteriousWord(secretWord, guessedLetters));
            System.out.println();
            notGuessed(alphabet);
            String input = invalidInput(keyboard, alphabet);
            wrongTracker = wrongOrCorrect(secretWord, input, wrongTracker);
            alphabet = updateAlphabet(alphabet, input);
            guessedLetters += input; // add letter to the guessed word
            win = win(secretWord, guessedLetters);
        }
        ending(secretWord, wrongTracker);
    }


    // Calculating the winning conditions that will be used in other methods.
    public static boolean win(String secretWord, String guessedLetters) {
        String winCondition = mysteriousWord(secretWord, guessedLetters);
        return winCondition.equals(secretWord);
    }



    // Prints out the ending statements with stating the player
    // or lost and also revealing the secret word.
    public static void ending(String secretWord, int wrongTracker) {
        secretWord = secretWord.toUpperCase();
        if (wrongTracker == MAX_WRONG_GUESSES) {
            System.out.println("You lose. The secret phrase was " + secretWord);
        } else {
            System.out.println("The phrase is " + secretWord + ".");
            System.out.println("You win!!");
        }
    }


    // Build the PhraseBank.
    // If args is empty or null, build the default phrase bank.
    // If args is not null and has a length greater than 0
    // then the first elements is assumed to be the name of the
    // file to build the PhraseBank from.
    public static PhraseBank buildPhraseBank(String[] args) {
        PhraseBank result;
        if (args == null || args.length == 0
                || args[0] == null || args[0].length() == 0) {
            result = new PhraseBank();
            /* CS312 students, uncomment the following line if you
             * would like less predictable behavior from the PhraseBank
             * during testing. NOTE, this line MUST be commented out
             * in the version of the program you turn in
             * or your will fail all tests.
             */
            // result = new PhraseBank("HangmanMovies.txt", true); // MUST be commented out in version you submit.
        } else {
            result = new PhraseBank(args[0]);
        }
        return result;
    }

    // Print the intro to the program.
    public static void intro() {
        System.out.println("This program plays the game of hangman.");
        System.out.println();
        System.out.println("The computer will pick a random phrase.");
        System.out.println("Enter letters for your guess.");
        System.out.println("After 5 wrong guesses you lose.");
    }


}
