import java.io.*;
import java.util.*;

public class Card
{
    enum Suit{
        HEARTS, DIAMONDS, CLUBS, SPADES
    };
    
    enum Rank{
    	// ACE has value 0, getPointValue() adds one
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING
    };
    
    String faces[] = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

    private Rank myRank;
    private Suit mySuit;

    public Card (int rank, String suit)
    {
      myRank = Rank.values()[rank - 1];
      mySuit = Suit.valueOf(suit);
    }

    public String getRank()
    {
      return faces[myRank.ordinal()];
    }

    public int getPointValue()
    {
      return myRank.ordinal() + 1;
    }
    
    public String getSuit()
    {
      return mySuit.name();
    }

    public String toString()
    {
      return faces[myRank.ordinal()] +  " of " + mySuit.toString();
    }
    
    public boolean isFigure()
    {
    	boolean ret = false;
    	if (getRank() == "jack" || getRank() == "queen" || 
    		getRank() == "king" || getRank() == "ace") {
    		ret = true;
    	}
    	return ret;    	
    }
}
