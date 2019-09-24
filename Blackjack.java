import java.util.Scanner;

/**
 * This is the game of Blackjack. It contains	a board which contains the
 * card deck and the players.
 *
 * @author Aimee Shao
 */
public class Blackjack {

  // Strings for argument
  private static final String USAGE_STR = String.format("\n%s\n%s\n%s\n%s\n\n"
    , "Usage: java Blackjack [numPlayers]" 
    ,	"  numPlayers: equal to the number of players aside from the dealer"
    , "    -- optional: default is 1 player"
    , "    -- must be an integer");	
  private static final String ERROR_TOO_MANY_ARGS = String.format("%s\n"
    , "Error: Too many arguments");
  private static final String ERROR_NOT_INT = String.format("%s\n"
    , "Error: Inputted argument needs to be an integer");

  // Strings during interactive loop
  private static final String CLEAR_CONSOLE_STR = "\033[H\033[2J";
  private static final String NEXT_PLAYER_STR	= String.format("\n%s"
    , "Please press enter to switch to next player.");
  private static final String HIT_STAY_STR = String.format("%s"
    , "Would you like to `hit`, `stay`, or receive a `hint`?");
  private static final String INPUT_INVALID_STR = String.format("%s\n%s\n"
    , "\"%s\" is not accepted."
    , "Please enter hit, stay, or hint.");

  // String comparisons with user input
  private static final String HIT_STR = "hit";
  private static final String STAY_STR = "stay";
  private static final String HINT_STR = "hint";

  /**
   * This method is used to start the Blackjack program and is the interactive
   * interface that interacts with the user/players.
   * @param args Command line arguments
   * @return Nothing.
   */
  public static void main (String[] args) {

    int numPlayers = 1; // number of players besides Dealer, default to 1
    Board board; // Board containing the deck and players
    Scanner in = new Scanner(System.in); // Used to read input from user

    // numPlayers is optional argument
    // check if inputted too many arguments
    if (args.length > 1) {
      System.err.print(ERROR_TOO_MANY_ARGS);
      System.err.print(USAGE_STR);
      System.exit(0);
    }

    // Check if inputted argument has correct format (an integer)
    if (args.length == 1) {
      try {
        numPlayers = Integer.parseInt(args[0]);
      } catch (Exception e) {
        System.err.print(ERROR_NOT_INT);
        System.err.print(USAGE_STR);
        System.exit(0);
      }
    }

    // Create board and set it up
    board = new Board(numPlayers);
    board.setup();

    // interactive loop till game ends
    while(true) {
      // clear console so next player can't see previous player's hand
      System.out.print(CLEAR_CONSOLE_STR);

      // Set up console for active player
      board.printBoard();
      board.printActivePlayer();
      in.nextLine(); // active player needs to confirm presence

      // Show player's hand and current total
      board.printActiveHand();
      board.printActiveTotal();

      // Prompt to hit or stay 
      System.out.println(HIT_STAY_STR);
      String input = in.nextLine();

      // Reprompt until "stay" or bust
      while (!input.equalsIgnoreCase(STAY_STR)) {
        // hit player
        if (input.equalsIgnoreCase(HIT_STR)) {
        	final boolean bust = board.hitPlayer();
          board.printActiveHand();
          board.printActiveTotal();

          // check if busted
          if (bust) {
            System.out.println(NEXT_PLAYER_STR);
            in.nextLine(); // Wait for player's confirmation to move on
            break;
          }
        } else if (input.equals(HINT_STR)) { // player wants hint
          board.printActiveHint();
        }	else { // input is none of the above, so print invalid input message  
          System.out.printf(INPUT_INVALID_STR, input);
        }

        // Reprompt
        System.out.println(HIT_STAY_STR);
        input = in.nextLine();
      }
			
      // move on to next player and check if all players went
      if (board.nextPlayer() == numPlayers + 1) {
        // clear screen and print end results
        System.out.print(CLEAR_CONSOLE_STR);
        board.gameEnd();
      }
    }
  }
}
