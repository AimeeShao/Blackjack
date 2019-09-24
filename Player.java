import java.util.ArrayList;

/**
 * This class helps create a player in blackjack which also includes the dealer.
 * A Player has a sum based on all the cards he/she has collected.
 *
 * @author Aimee Shao
 */
public class Player {

  private static final int BUST_NUM = 21;
  private static final int FACE_CARD_AMT = 10;
  private static final int KING_AS_INT = 13;
  private static final int ACE_AS_ELEVEN = 11;

  private Hand hand; // contains the player's hand
  private int aceAsElevenCount = 0; // number of aces considered as 11 in total
  private int aceAsOneCount = 0; // number of aces considered as 1 in total
  private int total = 0; // sum of cards in player's hand

  /**
   * Default constructor for the player.
   */
  Player() {
    // initialize player's hand
    hand = new Hand();
  }

  /**
   * Adds the new card to player's hand and total.
   * @param number Number of the new card the player was hit with
   * @return None.
   */
  public void hit(int number) {
    // add number to hand
    hand.add(number);

    // if ace, add as 11 to total first
    if (number == 1) {
      total += ACE_AS_ELEVEN;
      aceAsElevenCount += 1;
    }
    else if (number >= FACE_CARD_AMT && number <= KING_AS_INT) // 10,J,Q,K = 10
      total += FACE_CARD_AMT;
    else // 2-9 cards
      total += number;

    // while busted but have an aceAsEleven, change the ace's values to 1
    while (total > BUST_NUM && aceAsElevenCount > 0) {
      aceAsElevenCount -= 1;
      aceAsOneCount += 1;

      total = total - ACE_AS_ELEVEN + 1;
    }
  }

  /**
   * Returns the player's hand.
   * @return Player's hand
   */
  public ArrayList<Integer> getHand() {
    return hand.getCards();
  }

  /**
   * Returns the number of the face up card that player is first dealt.
   * @return Number of face up card
   */
  public int getFaceUpCard() {
    return hand.getFirstCard(); // face up card is first card dealt to player
  }
	
  /**
   * Returns number of aces considered as 11 in total.
   * @return aceAsElevenCount
   */
  public int getAceAsElevenCount() {
    return aceAsElevenCount;
  }

  /**
   * Returns number of aces considered as 1 in total.
   * @return aceAsOneCount
   */
  public int getAceAsOneCount() {
    return aceAsOneCount;
  }

  /**
   * Returns total sum of player's hand.
   * @return Total of player's hand
   */
  public int getTotal() {
    return total;
  }

  /**
   * Calculates the probability of player busting on next hit and
   * returns that probability.
   * @param deckForHint deck of cards used in game so far subtracting the
   *                    visible cards from each player
   * @param numCardsForHint number of cards in deckForHint
   * @return Probability of player busting on next hit
   */
  public float getHint(int[] deckForHint, int numCardsForHint) {
    // subtract player's hand from deckForHint and numCardsForHint
    for (int i = 1; i < hand.getSize(); i++) { // first card (0) accounted for
      deckForHint[hand.getCard(i) - 1] -= 1; // -1 index since A is at index 0
      numCardsForHint -= 1;
    }

    int cardsToBust = 0; // # of cards that would cause player to bust
    int numTillBust = BUST_NUM - total; // number added to total to reach 21

    // if numTillBust >= 10 (max possible value is 10), then cardsToBust is 0
    if (numTillBust < FACE_CARD_AMT) {
      // count number of cards greater than numTillBust in deckForHint
      for (int i = numTillBust; i < deckForHint.length; i++) {
        cardsToBust += deckForHint[i];
      }
    }

    return (float) cardsToBust / numCardsForHint * 100; // probability to bust
  }

  /**
   * Returns whether the player busted or not.
   * @return If player busted
   */
  public boolean getBusted() {
    return total > BUST_NUM;
  }
}
