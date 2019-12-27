import java.util.*;

/**
 * HangmanManager class picks the possible answer for the Hangman Game.
 * The answer is only deduced by the most occured set of pattern and the 
 * set of pattern changes each time a letter is picked. Once there is no more
 * pattern to pick between, first one in the set is the answer.
 * 
 * @author Blezyl Santos
 * @version 1.24 (Jan 24 2019)
 */

public class HangmanManager{
   private int length;                          // length of word
   private int maxGuesses;                      
   private Set<String> possibleWords;           // also the library
   private int Guesses;                         // wrong guesses made
   private SortedSet<Character> guessedLetters;
   private String pattern;                            
   
   /** 
    * Accepts the dictionary, the length of the word, and the max.
    * takes out all words in the dictionary that is either more or less than 
    * the given length and stores it in the possibleWords. Initializes guesses and guessedLetters
    * creates the initial pattern
    *
    * @param Dictionary the list of words in the dictionary
    * @param length the length of the word that the user wants to guess
    * @param max max guesses the user can have
    * @throws IllegalArgumentException if the length is 0 annd if max is not higher than 0
    */
   public HangmanManager(List<String> Dictionary, int length, int max){
      if (length < 1 || max < 0 ){
         throw new IllegalArgumentException();
      } else {
         this.length = length;
         maxGuesses = max;
      }
      this.possibleWords = new TreeSet<String>();
      for (String word: Dictionary){
         if (word.length() == this.length){
            possibleWords.add(word);
         }
      }
      Guesses = 0;
      guessedLetters = new TreeSet<Character>();
      pattern = "";
      for (int i = 0; i< length; i++){
         this.pattern = pattern + "- ";
      }
   }
   /**
    * Returns all the possible words that the computer is considering as answers
    * 
    * @returns possibleWords the possible words remaining in the set
    */
   public Set<String> words(){
      return this.possibleWords;
   }
   /** 
    * Returns the guesses left that the user has left
    * 
    * @returns the max guess the user had minus the wrong guesses
    */
   public int guessesLeft(){
      return maxGuesses - Guesses;
   }
   /** 
    * Returns the guessed letters of the use in alphabetic order
    * 
    * @returns guessedLetters all letters the user has guessed
    */
   public SortedSet<Character> guesses(){
      return guessedLetters;
   }
   
   /** 
    * Returns the current highest pattern 
    * 
    * @returns pattern the pattern the computer is considering
    * @throws IllegalArgumentException if possibleWords is empty
    */
   public String pattern(){
      if (possibleWords.isEmpty() == true){
         throw new IllegalStateException();
      } else { return pattern; }
   }
   /** 
    * Accepts the Guess of the user. All possible words are checked
    * and made into a new pattern either containing the guess or not.
    * Then the computer picks the highest occured pattern. if the pattern 
    * contains the picked letter the user is right.
    * 
    * @param Guess the guess of the user
    * @returns rightGuesses the amount of times the guess appear in the pattern
    * @throws IllegalStateException if guesses left is less than 0 or the possible words is empty
    * @throws IllegalArgumantException if the list of words is not empty and the guessed letters already contains the Guess
    */
   public int record (char Guess){
      int rightGuess = 0;
      if (guessesLeft()<1 || possibleWords.isEmpty()) throw new IllegalStateException();
      else if (!(possibleWords.isEmpty()) && guessedLetters.contains(Guess)) throw new IllegalArgumentException();
      guessedLetters.add(Guess);
      // adding pattern into map and the corresponding words
      Map<String, Set<String>> AnsMap = new TreeMap<String, Set<String>>();
      for (String word: possibleWords){
         String patt = patternMaker(word, Guess);
         if (!AnsMap.containsKey(patt)) AnsMap.put(patt, new TreeSet<String>());
         AnsMap.get(patt).add(word);
      }
      this.pattern = mostOccur(AnsMap);
      // how many times the guess is in the patter
      for (int i=0; i<pattern.length();i++){
         if (pattern.charAt(i) == Guess){
            rightGuess++;
         }
      }      
      possibleWords = AnsMap.get(pattern);
      if (rightGuess==0) Guesses ++;
      
      return rightGuess;
   }
   /** 
    * Accepts a word and the guess of the user. Creates a pattern of the word consisteing of dashes 
    * as letters not guessed yet, the character the user picks, or the letter from previous correct guesses
    * that is part of the answer. Returns the final pattern of the word.
    * 
    * @param word the word from possibleWords 
    * @param guess the guess of the user
    * @return tempPattern the pattern for the specific word passed through in the parameter 
    */
   public String patternMaker(String word, char guess){
      String tempPattern = "";
      for (int i=0; i<word.length();i++){
         if (pattern.charAt(2*i) == '-'){
            if (word.charAt(i) == guess){
               tempPattern = tempPattern + guess + " ";
            } else {
               tempPattern = tempPattern + "- ";
            }
         } else {
            tempPattern = tempPattern + pattern.charAt(2*i) + " ";
         }
      }
      return tempPattern;
   }
   /** 
    * Accepts a map and finds the pattern that has the most words in it
    * 
    * @param theMap the map that the methos goes through and check
    * @returns largestP the pattern that contains the most words
    */
   public String mostOccur(Map<String, Set<String>> theMap){
      String largestP = "";
      int largest = 0;
      for (String word: theMap.keySet()){
         if (theMap.get(word).size() > largest){
            largestP = word;
            largest = theMap.get(word).size();
         }
      } 
      return largestP;
   }
}