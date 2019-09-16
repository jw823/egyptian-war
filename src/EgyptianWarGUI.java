import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * This class provides a GUI for the Egyptian War game
 */
public class EgyptianWarGUI extends JFrame implements ActionListener
{
    /** Height of the game frame. */
    private static final int DEFAULT_HEIGHT = 500;
    /** Width of the game frame. */
    private static final int DEFAULT_WIDTH = 800;
    /** Width of a card. */
    private static final int CARD_WIDTH = 73;
    /** Height of a card. */
    private static final int CARD_HEIGHT = 97;
    /** Row (y coord) of the upper left corner of the first card. */
    private static final int LAYOUT_CENTER_COL = 200;
    /** Column (x coord) of the upper left corner of the first card. */
    private static final int LAYOUT_CENTER_ROW = 220;
    /** Distances from center for player piles **/
    private static final int LAYOUT_OFFSET_X = 200;
    private static final int LAYOUT_OFFSET_Y = 150;
    /** Distance between the upper left x coords of
     *  two horizonally adjacent cards. */
    private static final int LAYOUT_WIDTH_INC = 100;
    /** Distance between the upper left y coords of
     *  two vertically adjacent cards. */
    private static final int LAYOUT_HEIGHT_INC = 125;
    /** y coord of the "Replace" button. */
    private static final int BUTTON_TOP = 30;
    /** x coord of the "Replace" button. */
    private static final int BUTTON_LEFT = 570;
    /** Distance between the tops of the "Replace" and "Restart" buttons. */
    private static final int BUTTON_HEIGHT_INC = 50;
    /** y coord of the "n undealt cards remain" label. */
    private static final int LABEL_TOP = 160;
    /** x coord of the "n undealt cards remain" label. */
    private static final int LABEL_LEFT = 540;
    /** Distance between the tops of the "n undealt cards" and
     *  the "You lose/win" labels. */
    private static final int LABEL_HEIGHT_INC = 35;

    /** The board (Board subclass). */
    private Game game;

    /** The main panel containing the game components. */
    private JPanel panel;
    /** The Replace button. */
    private JButton replaceButton;
    /** The Restart button. */
    private JButton restartButton;
    /** The "number of undealt cards remain" message. */
    private JLabel statusMsg;
    /** The "you've won n out of m games" message. */
    private JLabel totalsMsg;
    /** The open deck cards. */
    private JLabel[] openDeckCards;
    /** The player deck cards. */
    private JLabel[] playerDeckCards;
    /** The player pile label. */
    private JLabel[] playerId;
    /** The win message. */
    private JLabel winMsg;
    /** The loss message. */
    private JLabel lossMsg;
    /** The coordinates of the player piles displays. */
    private Point[] pileCoords;
    /** Keep track of the next move */
    private String move;

    /** kth element is true iff the user has selected card #k. */
    private boolean[] selections;
    /** Current selected player */
    private int playerSelected;
    /** Size of open deck pile */
    private int openDeckSize;
    /** The number of games won. */
    private int totalWins;
    /** The number of games played. */
    private int totalGames;


    /**
     * Initialize the GUI.
     * @param gameBoard is a <code>Board</code> subclass.
     */
    public EgyptianWarGUI(Game gameBoard) {
        game = gameBoard;
        playerSelected = game.playerSelected();
        totalWins = 0;
        totalGames = 0;

        // Initialize pileCoords for up to 4 players + pile 0 for open deck
        pileCoords = new Point[game.players() + 1];
        int x = LAYOUT_CENTER_ROW;
        int y = LAYOUT_CENTER_COL;
        pileCoords[0] = new Point(x, y);
        pileCoords[1] = new Point(x, y + LAYOUT_OFFSET_Y);
        pileCoords[2] = new Point(x, y - LAYOUT_OFFSET_Y);
        if (game.players() >= 3) {
        	pileCoords[3] = new Point(x - LAYOUT_OFFSET_X, y);
        }
        if (game.players() == 4) {
        	pileCoords[4] = new Point(x + LAYOUT_OFFSET_X, y);
        }
        
        selections = new boolean[game.players()];
        initDisplay();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }

    /**
     * Run the game.
     */
    public void displayGame() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    /**
     * Draw the display (cards and messages).
     */
    public void repaint() {
        for (int k = 0; k < game.players(); k++) {
        	Card c;
        	if (k + 1 == playerSelected) {
        		c = game.playerDeck(k+1).peek(0);
        	}
        	else {
        		c = null;
        	}
            String cardImageFileName = imageFileName(c, false);
            URL imageURL = getClass().getResource(cardImageFileName);
            //System.out.println("player "+ Integer.toString(k+1) + " deck card URL " + imageURL.toString());
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                playerDeckCards[k].setIcon(icon);
                playerDeckCards[k].setVisible(true);
            } else {
                throw new RuntimeException(
                    "Card image not found: \"" + cardImageFileName + "\"");
            }
        }
        
        openDeckSize = game.openDeck().size();
        if (openDeckSize >= 1) {
        	// display the top card if available
        	Card c = game.openDeck().peek(0);
            String cardImageFileName = imageFileName(c, false);
            URL imageURL = getClass().getResource(cardImageFileName);
            // System.out.println("open deck card URL " + imageURL.toString());
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                openDeckCards[0].setIcon(icon);
                openDeckCards[0].setVisible(true);
            } else {
                throw new RuntimeException(
                    "Card image not found: \"" + cardImageFileName + "\"");
            }
        }
        statusMsg.setText(game.players()
            + " players selected.");
        statusMsg.setVisible(true);
        totalsMsg.setText("Player " + Integer.toString(playerSelected) + "'s turn.");
        totalsMsg.setVisible(true);
        pack();
        panel.repaint();
    }

    /**
     * Initialize the display.
     */
    private void initDisplay()  {
        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        // If board object's class name follows the standard format
        // of ...Board or ...board, use the prefix for the JFrame title
        String className = game.getClass().getSimpleName();
        int classNameLen = className.length();
        int gameLen = "Game".length();
        String gameStr = className.substring(classNameLen - gameLen);
        if (gameStr.equals("Game") || gameStr.equals("game")) {
            int titleLength = classNameLen - gameLen;
            setTitle(className.substring(0, titleLength));
        }

        // Place the player piles on the board
        // and adjust JFrame height if necessary
        int numPiles = game.players();
        int height = DEFAULT_HEIGHT;

        this.setSize(new Dimension(DEFAULT_WIDTH, height));
        panel.setLayout(null);
        panel.setPreferredSize(
            new Dimension(DEFAULT_WIDTH - 20, height - 20));
        // display only the top card initially
        openDeckCards = new JLabel[1];
        openDeckCards[0] = new JLabel();
        panel.add(openDeckCards[0]);
        // System.out.println("middle pile " + pileCoords[0]);
        openDeckCards[0].setBounds(pileCoords[0].x, pileCoords[0].y,
                CARD_WIDTH, CARD_HEIGHT);
        
        playerDeckCards = new JLabel[game.players()];
        playerId = new JLabel[game.players()];
        for (int k = 0; k < game.players(); k++) {
            playerDeckCards[k] = new JLabel();
            panel.add(playerDeckCards[k]);
            playerDeckCards[k].setBounds(pileCoords[k+1].x, pileCoords[k+1].y,
                                        CARD_WIDTH, CARD_HEIGHT);
            playerDeckCards[k].addMouseListener(new MyMouseListener());
            selections[k] = false;
            
            playerId[k] = new JLabel();
            panel.add(playerId[k]);
            playerId[k].setBounds(pileCoords[k+1].x + CARD_WIDTH + 10, pileCoords[k+1].y + 30,
                    250, 30);
            playerId[k].setText("Player " + Integer.toString(k+1) + "'s Pile");
            playerId[k].setVisible(true);
        }
        replaceButton = new JButton();
        replaceButton.setText("Replace");
        panel.add(replaceButton);
        replaceButton.setBounds(BUTTON_LEFT, BUTTON_TOP, 100, 30);
        replaceButton.addActionListener(this);

        restartButton = new JButton();
        restartButton.setText("Restart");
        panel.add(restartButton);
        restartButton.setBounds(BUTTON_LEFT, BUTTON_TOP + BUTTON_HEIGHT_INC,
                                        100, 30);
        restartButton.addActionListener(this);

        statusMsg = new JLabel(
            game.players() + " players selected.");
        panel.add(statusMsg);
        statusMsg.setBounds(LABEL_LEFT, LABEL_TOP, 250, 30);

        winMsg = new JLabel();
        winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
        winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
        winMsg.setForeground(Color.GREEN);
        winMsg.setText("You win!");
        panel.add(winMsg);
        winMsg.setVisible(false);

        lossMsg = new JLabel();
        lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
        lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
        lossMsg.setForeground(Color.RED);
        lossMsg.setText("Sorry, you lose.");
        panel.add(lossMsg);
        lossMsg.setVisible(false);

        totalsMsg = new JLabel("You've won " + totalWins
            + " out of " + totalGames + " games.");
        totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC,
                                  250, 30);
        panel.add(totalsMsg);

//        if (!board.anotherPlayIsPossible()) {
//            signalLoss();
//        }

        pack();
        getContentPane().add(panel);
        getRootPane().setDefaultButton(replaceButton);
        // set it up for initial play
        move = game.getNextMove();
        System.out.println("** Next Move: " + move);
        panel.setVisible(true);
    }

    /**
     * Deal with the user clicking on something other than a button or a card.
     */
    private void signalError() {
        Toolkit t = panel.getToolkit();
        t.beep();
    }

    /**
     * Returns the image that corresponds to the input card.
     * Image names have the format "[Rank][Suit].GIF" or "[Rank][Suit]S.GIF",
     * for example "aceclubs.GIF" or "8heartsS.GIF". The "S" indicates that
     * the card is selected.
     *
     * @param c Card to get the image for
     * @param isSelected flag that indicates if the card is selected
     * @return String representation of the image
     */
    private String imageFileName(Card c, boolean isSelected) {
        String str = "cards/";
        if (c == null) {
            return "cards/back1.GIF";
        }
        str += c.getRank() + c.getSuit();
        if (isSelected) {
            str += "S";
        }
        str += ".GIF";
        return str;
    }

    /**
     * Respond to a button click (on either the "Replace" button
     * or the "Restart" button).
     * @param e the button click action event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(replaceButton)) {
            // Gather all the selected cards.
            List<Integer> selection = new ArrayList<Integer>();
            /*
            for (int k = 0; k < board.size(); k++) {
                if (selections[k]) {
                    selection.add(new Integer(k));
                }
            }
            // Make sure that the selected cards represent a legal replacement.
            if (!board.isLegal(selection)) {
                signalError();
                return;
            }
            for (int k = 0; k < board.size(); k++) {
                selections[k] = false;
            }
            // Do the replace.
            board.replaceSelectedCards(selection);
            if (board.isEmpty()) {
                signalWin();
            } else if (!board.anotherPlayIsPossible()) {
                signalLoss();
            }
        */
            repaint();
        } else if (e.getSource().equals(restartButton)) {
        	/*
            board.newGame();
            getRootPane().setDefaultButton(replaceButton);
            winMsg.setVisible(false);
            lossMsg.setVisible(false);
            if (!board.anotherPlayIsPossible()) {
                signalLoss();
                lossMsg.setVisible(true);
            }
            for (int i = 0; i < selections.length; i++) {
                selections[i] = false;
            }
            */
            repaint();
        } else {
            signalError();
            return;
        }
    }

    /**
     * Display a win.
     */
    private void signalWin() {
        getRootPane().setDefaultButton(restartButton);
        winMsg.setVisible(true);
        totalWins++;
        totalGames++;
    }

    /**
     * Display a loss.
     */
    private void signalLoss() {
        getRootPane().setDefaultButton(restartButton);
        lossMsg.setVisible(true);
        totalGames++;
    }

    /**
     * Receives and handles mouse clicks.  Other mouse events are ignored.
     */
    private class MyMouseListener implements MouseListener {

        /**
         * Handle a mouse click on a card by toggling its "selected" property.
         * Each card is represented as a label.
         * @param e the mouse event.
         */
        public void mouseClicked(MouseEvent e) {
            for (int k = 0; k < game.players(); k++) {
                if (e.getSource().equals(playerDeckCards[k])) {
                	if (k+1 == game.playerSelected()) {
                    	// if it is this player's turn, move card from player to open deck
                    	// System.out.println("clicked player " + Integer.toString(k + 1) + " pile");
                    	playerSelected = game.playerSelected();
                		System.out.println("player " + playerSelected + " deck " + game.playerDeck(playerSelected));

                		Card c = game.playerDeck(playerSelected).deal();
                		game.openDeck().addToEnd(c);
                		System.out.println("open deck: " + game.openDeck());

                    	move = game.getNextMove();
                		game.setNextPlayerTurn();
                		playerSelected = game.playerSelected();
                		System.out.println("** Next Move: " + move);
                		System.out.println("player selected: " + playerSelected);
                		boolean canSlap = game.canSlap();
                		System.out.println("** Can slap? " + (canSlap?"yes":"no"));
                		// playerSelected = playerSelected % (game.players()) + 1;
                		// System.out.println("new player selected: " + playerSelected);
                	}
                    repaint();
                    return;
                }
            }
            signalError();
        }

        /**
         * Ignore a mouse exited event.
         * @param e the mouse event.
         */
        public void mouseExited(MouseEvent e) {
        }

        /**
         * Ignore a mouse released event.
         * @param e the mouse event.
         */
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * Ignore a mouse entered event.
         * @param e the mouse event.
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Ignore a mouse pressed event.
         * @param e the mouse event.
         */
        public void mousePressed(MouseEvent e) {
        }
    }
}
