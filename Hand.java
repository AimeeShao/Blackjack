import java.util.ArrayList;

/**
 * This class serves as the hand of a player. It contains the cards in a
 * player's hand.
 *
 * @author Aimee Shao
 */
public class Hand {

  private ArrayList<Integer> cards; // the cards in the hand

  /**
   * Default constructor for hand, initializing list of cards.
   */
  Hand() {
    cards = new ArrayList<>();
  }

  /**
   * Adds a card to the hand.
   * @param number Number of the card to add to the hand.
   * @return None.
   */
  public void add(int number) {
    cards.add(number);
  }

  /**
   * Returns the cards in hand.
   * @return List of cards.
   */
  public ArrayList<Integer> getCards() {
    return cards;
  }

  /**
   * Return the first card which is the face up card.
   * @return Number of face up card.
   */
  public int getFirstCard() {
    return cards.get(0);
  }

  /**
   * Returns the number of cards in hand.
   * @return Size of hand.
   */
  public int getSize() {
    return cards.size();
  }

  /**
   * Returns the card at a specific index.
   * @param pos Index of card to get.
   * @return Card number.
   */
  public int getCard(int pos) {
    return cards.get(pos);
  }
}
