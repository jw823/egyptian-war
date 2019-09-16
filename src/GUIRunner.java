
public class GUIRunner {
	public static void main(String[] args) {
		int numberOfPlayers = 2;
		Game board = new Game(numberOfPlayers);
		EgyptianWarGUI gui = new EgyptianWarGUI(board);
		gui.displayGame();
	}
}
