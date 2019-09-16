import java.io.*;
import java.util.*;
import java.lang.Math;


public class Deck
{
  private int size;
  private List<Card> cards;

  // create full deck and shuffle
  public Deck(String[] suits, int[] ranks)
  {
		cards = new ArrayList<Card>();
		for (int j = 0; j < ranks.length; j++) {
			for (String suitString : suits) {
				cards.add(new Card(ranks[j], suitString));
			}
		}
		size = cards.size();
		shuffle();
  }
  
  // create empty deck
  public Deck()
  {
		cards = new ArrayList<Card>();
		size = 0;
  }
  
  public void shuffle()
  {
		for (int k = cards.size() - 1; k > 0; k--) {
			int howMany = k + 1;
			int start = 0;
			int randPos = (int) (Math.random() * howMany) + start;
			Card temp = cards.get(k);
			cards.set(k, cards.get(randPos));
			cards.set(randPos, temp);
		}
		size = cards.size();  
  }
  
  public Card deal()
  {
		if (isEmpty()) {
			return null;
		}
		size--;
		Card c = cards.get(size);
		return c;  
  }

  public Card peek(int depth) {
	  if (depth > size - 1) {
		  throw new RuntimeException("peek() - invalid depth " + depth);
	  }
	  Card c = cards.get(size - depth - 1);
	  return c;
  }
  
  public void add(Card c)
  {
	  cards.add(c);
	  size++;
  }
  
  public void addToEnd(Card c)
  {
	  cards.add(size, c);
	  size++;
  }

  public int size()
  {
    return size;
  }
  
  public boolean isEmpty()
  {
	return size == 0;
  }
  
  public String toString()
  {
		String rtn = "size = " + size + "\nUndealt cards: \n";

		for (int k = size - 1; k >= 0; k--) {
			rtn = rtn + cards.get(k);
			if (k != 0) {
				rtn = rtn + ", ";
			}
			if ((size - k) % 2 == 0) {
				// Insert carriage returns so entire deck is visible on console.
				rtn = rtn + "\n";
			}
		}

		rtn = rtn + "\n";
		return rtn;
  }
}
