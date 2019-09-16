
public class Game {
	private int numberOfPlayers;
	private int currentPlayer;
	private Rules rules;
	private Deck gameDeck;			// initial deck with all cards
	private Deck openDeck;			// deck on the table
	private Deck playerDecks[];
	String previousMove = "";
	String nextMove = "";
	
	private static final String[] suits = 
		{ "HEARTS", "DIAMONDS", "CLUBS", "SPADES" };
	
	private static final int[] points = 
		{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
	
	public Game(int players) {
		numberOfPlayers = players;
		currentPlayer = 1;
		gameDeck = new Deck(suits, points);
		System.out.println("number of players: " + numberOfPlayers);
		playerDecks = new Deck[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			playerDecks[i] = new Deck();
		}
		openDeck = new Deck();
		
		// take all cards from gameDeck and give to player Decks
		int j = 0;
		while (gameDeck.size() > 0) {
			Card c = gameDeck.deal();
			playerDecks[j].add(c);
			j = (j + 1) % numberOfPlayers;
		}
		
		// create the rules of the game
		rules = new Rules();
	}
	
	public int players() {
		return numberOfPlayers;
	}
	
	public void setNextPlayerTurn() {
		if ((previousMove != "getFigure") ||
			(previousMove == "getFigure" && nextMove == "getFigure")) {
			currentPlayer = (currentPlayer % numberOfPlayers) + 1;
		}
	}
	
	public int playerSelected() {
		return currentPlayer;
	}
	
	public Deck playerDeck(int player) {
		return playerDecks[player-1];
	}
	
	public Deck openDeck() {
		return openDeck;
	}
	
	public String getNextMove() {
		previousMove = nextMove;	// keep copy of previous move
									// to set currentPlayer
		nextMove = rules.getNextMove(openDeck);
		return nextMove;
	}
	
	public boolean canSlap() {
		return rules.canSlap(openDeck);
	}
	
	public String toString() {
		String rtn = "";
		for (int i = 0; i < numberOfPlayers; i++) {
			rtn += "Player Deck:" + "\n";
			rtn += playerDecks[i].toString();
		}

		rtn += "Open Deck:" + "\n";
		rtn += openDeck.toString();

		return rtn;
	}	
}
