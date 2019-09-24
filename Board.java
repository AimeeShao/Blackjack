import java.util.Random;
import java.util.ArrayList;

/**
 * Board is the playing field of Blackjack. It contains the dealer, the players,
 * and the deck of cards.
 *
 * @author Aimee Shao
 */
public class Board
{
  // Used in the deck
  private static final int NUM_OF_NUMBERS = 13;
  private static final int NUM_OF_SUITS = 4;

  // Minimum for dealer to stay
  private static final int DEALER_MINIMUM = 17;

  // String for dealing
  private static final String DEALING_STR = "Dealing... ";
  private static final String DONE_DEALING_STR = "done.\n";

  // Strings for active player
  // Starting new player's turn
  private static final String PLAYER_TURN_STR = "It is now Player %d's turn.\n";
  private static final String CONFIRM_PLAYER_STR = String.format("%s"
      , "Please press enter to confirm the player.");
	
  // Hitting, busting, and hint
  private static final String PLAYER_HIT_STR = "\nYou got a %s!\n";
  private static final String PLAYER_BUSTED_STR = "You busted.";
  private static final String PLAYER_HINT_STR = String.format("\n%s%s\n\n"
      , "You have a %.2f%% chance of busting on your "
      , "next hit based on what you know.");

  // Hand and sum
  private static final String PLAYER_HAND_STR = "Your hand contains ";
  private static final String PLAYER_TOTAL_STR = "This sums up to be %d";
  private static final String PLAYER_ACE_STR = " with %d ace(s) counted as ";
  private static final String PLAYER_ACE_ELEVEN_STR = "11";
  private static final String PLAYER_ACE_ONE_STR = "1";
  private static final String PLAYER_TOTAL_AND_STR = " and";
  private static final String COMMA_STR = ", ";
  private static final String PERIOD_STR = ".";

  // Strings for printing board
  private static final String DEALER_CARD_STR = "Dealer is showing a %s.\n";
  private static final String OTHERS_CARD_STR = "Player %d is showing a %s.\n";

  // Strings for end game results
  private static final String DEALER_TOTAL_STR = String.format("%s\n\n"
      , "Dealer has a total of %d.");
  private static final String PLAYER_STR = String.format("%s"
      , "Player %d has a total of %d, so he ");
  private static final String WINS_STR = "wins!";
  private static final String WINS_BUST_STR = "wins due to the dealer busting!";
  private static final String LOSES_STR = "loses.";
  private static final String LOSES_BUST_STR = "loses due to busting.";
  private static final String TIES_STR = "ties with the dealer.";

  private int[] deck; // count of each card number 1-13 in deck
  private Player[] players; // array of the players; dealer is players[0]
  private int activePlayer; // player whose turn it is
  private int[] faceUpCards; // one card from each player visible to everyone

  // variables to keep track of deck
  private int cardsInDeck = 0; // number of cards in the deck
  private int refillCount = 0; // number of times we added a deck

  private Random rand; // random generator used in dealing cards

  /**
   * This is the constructor for the Board, initializing variables
   * @param numPlayers Number of players besides the dealer.
   * @return None.
   */
  Board(int numPlayers) {
		
    // Initialize deck which has 13 numbers
    deck = new int[NUM_OF_NUMBERS];

    // Create all players including dealer
    players = new Player[numPlayers + 1]; // Add 1 because dealer is players[0]
    for(int p = 0; p < players.length; p++) {
      players[p] = new Player();
    }

    // Initialize the faceUpCards array
    faceUpCards = new int[numPlayers + 1];

    rand = new Random();
  }

  /**
   * Sets up the board. It initializes the dealer and players and deals them
   * their initial 2 cards - one face up, one face down.
   * @return None.
   */
  public void setup() {
    // refill deck
    refillDeck();

    System.out.print(DEALING_STR);

    // deal each player at the start of the game
    for (int p = 0; p < players.length; p++) {
      // Add face up card from Player p to the array and add to Player p's hand
      int faceUpCard = dealCard();
      faceUpCards[p] = faceUpCard;
      players[p].hit(faceUpCard);

      // Deal second initial card - the face down card
      players[p].hit(dealCard());
    }

    // Player 1 starts, until Player numPlayers. Then dealer's turn.
    activePlayer = 1;

    System.out.print(DONE_DEALING_STR);
  }

  /**
   * Deal a random card/number from the deck to a player.
   * @return Number of the dealt card.
   */
  public int dealCard() {
    // if we ran out of cards, first refill the deck
    if (cardsInDeck == 0)
      refillDeck();

    // Get a random number [1-13]
    int number = rand.nextInt(NUM_OF_NUMBERS) + 1;

    // Loop to ensure random but also available in deck
    while (deck[number-1] == 0) {
      number = rand.nextInt(NUM_OF_NUMBERS) + 1;
    }

    // subtract 1 for the card we took out
    deck[number-1] -= 1;
    cardsInDeck -= 1;

    return number;
  }

  /**
   * Hits the player, adding a card to the player's total. Prints the card the
   * player was dealt and if player busted.
   * @return whether player busted or not.
   */
  public boolean hitPlayer() {
    // hit player - deal card to player
    int number = dealCard();
    System.out.printf(PLAYER_HIT_STR, convertIntToCard(number));
    players[activePlayer].hit(number);

    // check if player busted
    boolean bust = players[activePlayer].getBusted();
    if (bust)
			System.out.println(PLAYER_BUSTED_STR);

    return bust;
  }

  /**
   * Changes the active player to the next player. This occurs when a player
   * busts or stays.
   * @return activePlayer - the next player's turn
   */
  public int nextPlayer() {
    return ++activePlayer;
  }

  /**
   * Actions done at end of game. Dealer deals himself according to rules and
   * determines the results of the game/each player.
   * @return None.
   */
  public void gameEnd() {
    // Rules of blackjack dealer
    // Dealer must hit if his sum is less than 17
    while (players[0].getTotal() < DEALER_MINIMUM) {
      players[0].hit(dealCard());
    }
		
    // Print result of dealer
    int dealerSum = players[0].getTotal();
    System.out.printf(DEALER_TOTAL_STR, dealerSum);

    // Print out results of each player
    for (int p = 1; p < players.length; p++) {
      int playerSum = players[p].getTotal();

      System.out.printf(PLAYER_STR, p, playerSum);

      if (players[p].getBusted()) // players loses if bust
        System.out.println(LOSES_BUST_STR);
      else if (players[0].getBusted()) // player wins if no bust but dealer did
        System.out.println(WINS_BUST_STR);
      else if (playerSum > dealerSum) // player sum > dealer sum
        System.out.println(WINS_STR);
      else if (playerSum == dealerSum) // player ties with dealer
        System.out.println(TIES_STR);
      else // dealer sum > player sum
        System.out.println(LOSES_STR);
    }

    System.out.println(); // extra new line
    System.exit(0); // end game/program
	}

  /**
   * Prints dealer and all players' visible card.
   * @return None.
   */
  public void printBoard() {
    // print face up cards aka the board visible to everyone
    for (int p = 0; p < players.length; p++) {
      String faceUpCard = convertIntToCard(faceUpCards[p]);

      // dealer
      if (p == 0)
        System.out.printf(DEALER_CARD_STR, faceUpCard);
      else // players
        System.out.printf(OTHERS_CARD_STR, p, faceUpCard);
    }
		
    // extra new line
    System.out.println();	
  }

  /**
   * Prints to console which player's turn it is.
   * @return None.
   */
  public void printActivePlayer() {
    System.out.printf(PLAYER_TURN_STR, activePlayer);
    System.out.println(CONFIRM_PLAYER_STR);
  }
	
  /**
   * Prints to console the active player's hand (list of cards).
   * @return None.
   */
  public void printActiveHand() {
    // Get hand from the player
    ArrayList<Integer> hand = players[activePlayer].getHand();

    // Print initial statement
    System.out.print(PLAYER_HAND_STR);

    // loop through hand and print the cards
    for (int i = 0; i < hand.size(); i++) {
      String card = convertIntToCard(hand.get(i));
      System.out.print(card);

      // print "." instead of ", " after last card
      if (i == hand.size() - 1)
        System.out.println(PERIOD_STR);
      else
        System.out.print(COMMA_STR);
    }
  }

  /**
   * Prints active player's total.
   * @return None.
   */
  public void printActiveTotal() {
    int aceAsEleven = players[activePlayer].getAceAsElevenCount();
    int aceAsOne = players[activePlayer].getAceAsOneCount();

    // Print beginning of string
    System.out.printf(PLAYER_TOTAL_STR, players[activePlayer].getTotal());

    // Print aces strings if player has aces
    if (aceAsEleven > 0) {
      System.out.printf(PLAYER_ACE_STR, aceAsEleven);
      System.out.print(PLAYER_ACE_ELEVEN_STR);
    }

    if (aceAsEleven > 0 && aceAsOne > 0)
      System.out.print(PLAYER_TOTAL_AND_STR);
    
    if(aceAsOne > 0)
    {
      System.out.printf(PLAYER_ACE_STR, aceAsOne);
      System.out.print(PLAYER_ACE_ONE_STR);
    }

    System.out.println(PERIOD_STR);
  }

  /**
   * Prints active player's probability of busting when he wants help.
   * @return None.
   */
  public void printActiveHint() {
    // create the deck for hint containing all decks used in the game so far
    int[] deckForHint = new int[NUM_OF_NUMBERS];
    // total number of cards in the game so far - number of visible cards
    int numCardsForHint = NUM_OF_SUITS * NUM_OF_NUMBERS * refillCount -
      faceUpCards.length;

    for (int i = 0; i < deckForHint.length; i++) {
      deckForHint[i] = NUM_OF_SUITS * refillCount;
    }

    // subtract each player's visible card from the deckForHint
    for (int i = 0; i < faceUpCards.length; i++) {
      int card = faceUpCards[i];
      deckForHint[card - 1] -= 1; // -1 in index because 1 is at index 0 in deck
    }
		
    System.out.printf(PLAYER_HINT_STR, 
        players[activePlayer].getHint(deckForHint, numCardsForHint));
  }

  /**
   * Helper method that refills the deck so the deck has 4 cards
   * for each of the 13 numbers.
   * @return None.
   */
  private void refillDeck() {
    // loop through deck and refill so each number has 4 cards.
    for (int i = 0; i < deck.length; i++) {
      deck[i] = NUM_OF_SUITS;
    }

    // set variables accordingly
    cardsInDeck = NUM_OF_NUMBERS * NUM_OF_SUITS;
    refillCount += 1;
  }

  /**
   * Helper method that converts an integer to a string representing the card.
   * @param card The integer to convert
   * @return The string representing the card
   */
  private String convertIntToCard(int card) {
    String faceUpCard = "";

    // convert the integer stored into cards (A,2-10,J,Q,K)
    switch (card) {
      case 1:
        faceUpCard = "A"; 
        break; 
      case 11:
        faceUpCard = "J";
        break;
      case 12:
        faceUpCard = "Q";
        break;
      case 13:
        faceUpCard = "K";
        break;
      default: // 2-10
        faceUpCard += card;
    }

    return faceUpCard;
  }
}
