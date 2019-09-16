
public class Rules {
	
	enum Move {
		PLAY, 		// players take turn playing
		GETFIGURE,  // one player put a figure, next put x cards or figure
		TAKEPILE 	// player who slap or put figure can take the pile
	};
	
	String moveName[] = {"play", "getFigure", "takePile"};
	
	private int howMany;
	private Move nextMove;
	private boolean inWaitFigure = false;
	private boolean nextPlayer = false;
	
	public boolean canSlap(Deck deck) {
		boolean ret = false;
		Card c0 = null, c1 = null, c2 = null;
		if (deck.size() >= 1) {
			c0 = deck.peek(0);   // top card
		}
		if (deck.size() >= 2) {
			c1 = deck.peek(1);   // next card
		}
		if (deck.size() >= 3) {
			c2 = deck.peek(2);   // next card after
		}
		
		// can slap if next card and sandwich rules
		if ((deck.size() >= 2 && c0.getRank() == c1.getRank()) || 
			(deck.size() >= 3 && c0.getRank() == c2.getRank() )) { 
			ret = true;
		}		
		
		return ret;
	}
	
	public String getNextMove(Deck deck) {
		Card c0 = null;
		if (deck.size() >= 1) {
			c0 = deck.peek(0);   // top card
		}
		if (inWaitFigure) {
			if (c0.isFigure()) {
				nextPlayer = true;
				inWaitFigure = false;
				nextMove = Move.GETFIGURE;
			} else {
				howMany--;
				nextPlayer = false;
				if (howMany == 0) {
					inWaitFigure = false;
					nextMove = Move.TAKEPILE;
				}
			}
		}		
		else if (deck.size() >= 1) {
			// figures special checks
			if (c0.getRank() == "ace") {
				nextMove = Move.GETFIGURE;
				inWaitFigure = true;
				howMany = 4;
			}
			else if (c0.getRank() == "king") {
				nextMove = Move.GETFIGURE;
				inWaitFigure = true;
				howMany = 3;
			}
			else if (c0.getRank() == "queen") {
				nextMove = Move.GETFIGURE;
				inWaitFigure = true;
				howMany = 2;
			}
			else if (c0.getRank() == "jack") {
				nextMove = Move.GETFIGURE;
				inWaitFigure = true;
				howMany = 1;
			}
		}
		else {
			// default move is continue to play
			nextMove = Move.PLAY;
		}
		
		return moveName[nextMove.ordinal()];
	}
	
	public boolean getFigureMoveToNextPlayer() {
		boolean ret = false;
		if (inWaitFigure && nextPlayer) {
			ret = true;
		}
		return ret;
	}
}
