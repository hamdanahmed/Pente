package GUI;

/**
 * // GUI controller - All client GUI actions are carried out by this class.
 */

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Core.GameVariant;
import Core.Move;
import Core.Controller;
import Core.GamePiece;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import Core.GameState;

public class MainController {

	// used by Timer/count/player/playermode labels
	private SimpleIntegerProperty timeSeconds_ = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty moveCount_ = new SimpleIntegerProperty(0);
	private SimpleStringProperty variantDisplay_ = new SimpleStringProperty("");
	private SimpleStringProperty playerModeDisplay_ =
	    new SimpleStringProperty("");
	private SimpleStringProperty playerDisplay_ = new SimpleStringProperty("");
	private Timeline timeline_;
	private GamePiece player_; // which player this GUI is for, in network games =
	                           // WHITE or BLACK, in local games = NONE
	private String hostname_; // hostname of the server
	private int port_; // port on which server is running

	private boolean onStartUp_ = true;// this boolean is used to prevent the alert
	                                  // menu
	// from firing on the initial startup

	@FXML
	private Boolean blackMove_; // is it the black players move? This is used to
	                            // control displays

	@FXML
	private Label timerLabel_;

	@FXML
	private Label moveCountLabel_;

	@FXML
	private Label gameVariantLabel_;

	@FXML
	private Label playerModeLabel_;

	@FXML
	private Label playerLabel_;

	@FXML
	private Circle white_;

	@FXML
	private Circle black_;

	@FXML
	private Label blackCaptures_;

	@FXML
	private Label whiteCaptures_;

	@FXML
	private CustomCanvas canvas_;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	// used for confirmation messages
	Alert alert_ = new Alert(AlertType.CONFIRMATION);

	private volatile GameState gameState_;
	private GamePiece currentPlayer_; // player who will move next
	private GameVariant gameVariant_; // current GameVariant in use
	private List<GameVariant> gameVariants_; // list of available GameVariants
	private String playerMode_; // current playerMode (1 player, 2 player local, 2
	                            // player network, etc)
	protected ClientComm comm_; // communication object used by this controller
	private Thread thread_; // thread to launch comm_

	@FXML // This method is called by the FXMLLoader when initialization is
	      // complete
	void initialize () {
		whiteCaptures_.setText("0");
		blackCaptures_.setText("0");
		// Configure the Timer Label
		timerLabel_.setText(timeSeconds_.toString());
		timerLabel_.setTextFill(Color.RED);
		timerLabel_.setStyle("-fx-font-size: 2em;");
		timerLabel_.textProperty().bind(timeSeconds_.asString());

		// Configure the moveCount label
		moveCountLabel_.setText(moveCount_.toString());
		moveCountLabel_.setTextFill(Color.BLUE);
		moveCountLabel_.setStyle("-fx-font-size: 2em;");
		moveCountLabel_.textProperty().bind(moveCount_.asString());

		// Configure the gameVariant label
		gameVariantLabel_.setText(variantDisplay_.toString());
		gameVariantLabel_.setTextFill(Color.BLACK);
		gameVariantLabel_.setStyle("-fx-font-size: 2em;");
		gameVariantLabel_.textProperty().bind(variantDisplay_);

		// Configure the playerMode label
		playerModeLabel_.setText(playerModeDisplay_.toString());
		playerModeLabel_.setTextFill(Color.BLACK);
		playerModeLabel_.setStyle("-fx-font-size: 2em;");
		playerModeLabel_.textProperty().bind(playerModeDisplay_);

		// Configure the playerMode label
		playerLabel_.setText(playerDisplay_.toString());
		playerLabel_.setTextFill(Color.BLACK);
		playerLabel_.setStyle("-fx-font-size: 2em;");
		playerLabel_.textProperty().bind(playerDisplay_);

		// launch new comm_ object
		hostname_ = "localhost";
		port_ = 9000;
		SetComm(hostname_,port_);

		getGameVariants(); // get list of available GameVariants
		gameVariant_ = gameVariants_.get(0); // initially use the first one (1
		                                     // player)
		playerMode_ = "1 Player";
		player_ = GamePiece.BLACK; // identifies what player this GUI is acting as.
		                           // NONE is both players using this GUI. Default
		                           // is 1 Player game = BLACK
		newGame(); // start new game

	}

	private void initBoard () {
		// initialize the board

		// configure the game pieces
		blackMove_ = true;
		currentPlayer_ = player_;

		if ( player_ == GamePiece.NONE ) {
			currentPlayer_ = GamePiece.BLACK;
			playerDisplay_.set("Both");
		}
		if ( player_ == GamePiece.VOID ) {
			playerDisplay_.set("OBSERVER");
		}
		white_.setOpacity(blackMove_ ? 0 : 1);
		black_.setOpacity(blackMove_ ? 1 : 0);
	}

	// opens the options dialog to choose a GameVariant
	@FXML
	private void OpenOptionsDialog ( ActionEvent event ) throws Exception {
		FXMLLoader fxmlLoader =
		    new FXMLLoader(getClass().getResource("OptionsDialog.fxml"));
		Parent root = fxmlLoader.load();
		OptionsDialogCTL controller = (OptionsDialogCTL) fxmlLoader.getController();
		Stage stage = new Stage();
		stage.setTitle("Select Game");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		// if ( controller.getChoice() != null ) {
		gameVariant_ = controller.getChoice();
		newGame();
		// }
	}

	// opens the Player dialog to choose the playerMode
	@FXML
	private void OpenPlayerDialog ( ActionEvent event ) throws Exception {
		FXMLLoader fxmlLoader =
		    new FXMLLoader(getClass().getResource("PlayerDialog.fxml"));
		Parent root = fxmlLoader.load();
		PlayerDialogCTL controller = (PlayerDialogCTL) fxmlLoader.getController();
		Stage stage = new Stage();
		stage.setTitle("Select Game");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		// if ( controller.getChoice() != null ) {
		playerMode_ = controller.getChoice();
		hostname_ = controller.getHost();
		port_ = controller.getPort();

		// sets the playerMode_ to whatever the user picked
		switch ( playerMode_ ) {
		case "1 Player": // 1 player is always BLACK
			player_ = GamePiece.BLACK;
			break;
		case "2 Player Network": // hosting player is always BLACK
			player_ = GamePiece.BLACK;
			break;
		case "Join Game": // joining player is always WHITE
			player_ = GamePiece.WHITE;
			break;
		case "Spectate Network": // no player is participating
			player_ = GamePiece.VOID;
			break;
		default: // both players using this GUI
			player_ = GamePiece.NONE;
			hostname_ = "localhost";
			port_ = 9000;
		}
		SetComm(hostname_,port_);
		newGame(); // starts game
		// }
	}

	@FXML
	private void OpenHelpDialog ( ActionEvent event ) throws Exception {
		FXMLLoader fxmlLoader =
		    new FXMLLoader(getClass().getResource("HelpDialog.fxml"));
		Parent root = fxmlLoader.load();
		HelpDialogCTL controller = (HelpDialogCTL) fxmlLoader.getController();
		Stage stage = new Stage();
		stage.setTitle("Help");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		// does not proceed if the user closes the dialog box
	}
	
	@FXML
	private void OpenAboutDialog ( ActionEvent event ) throws Exception {
		FXMLLoader fxmlLoader =
		    new FXMLLoader(getClass().getResource("AboutDialog.fxml"));
		Parent root = fxmlLoader.load();
		AboutDialogCTL controller = (AboutDialogCTL) fxmlLoader.getController();
		Stage stage = new Stage();
		stage.setTitle("About");		
		Scene scene = new Scene(root);
		
		
		
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		
		String holder = (" Launching the GUI (Main) will launch a completely self-contained 1 Player Pente Standard game ready to play. \r\n"
		    + "\r\n"
		    + "                The top of the game board shows the Game Variant, Player(s) using this GUI (White, Black, Both, None), and the Player Mode (1 Player, 2 Player Local, 2 Player Network, Join Game, Spectate). \r\n"
		    + "\r\n"
		    + "                The bottom of the game board shows the Captured Pieces count, a timer since the current game started (locally), the number of moves made, and an indicator of which player’s move it currently is,  on the right and left as a round game piece of the proper color. There is also a Restart Game button which will restart the current game in progress. \r\n"
		    + "\r\n"
		    + "                The game board itself is a 19 x 19 grid of play spaces represented as grid intersections. The center space is grey as many game variations require the first move there. There is a centered dark green area comprised of spaces 2 intersections away from center to illustrate the restricted area for the Tournament variation. \r\n"
		    + "\r\n"
		    + "                The File – New menu item will create a new game using the currently selected Game Variation and Player Mode (Options menu) \r\n"
		    + "\r\n"
		    + "                Choosing a game to play can be done via the Options - Game Variations menu item. The game will start after clicking OK using the current Player Mode. \r\n"
		    + "\r\n"
		    + "                Choosing a player mode can be done via the Options – Choose Player Mode menu item. There are 5 player mode options.  \r\n"
		    + "\r\n"
		    + "                1 Player uses the local server to run the game and is played against a computer player.  \r\n"
		    + "\r\n"
		    + "                2 Player Local uses the local server to run the game and the GUI is shared by 2 players. \r\n"
		    + "\r\n"
		    + "                2 Player Network shows an option for selecting the host and port to create a 2 player game which will run on the server at that address. The local server can be used as well. The initiating player will be the first player and can make the first move immediately. They will then have to wait for another remote player to join the game. \r\n"
		    + "\r\n"
		    + "                Join Game gives an option to join a 2 Player Network initiated game previously started by another remote user on some server. The host and port of that server can be entered if different than the local host. It will choose the most recently initiated 2 Player Network game on that server. The joined game is added to a list for other users to observe. \r\n"
		    + "\r\n"
		    + "                Spectate Network gives an option to observe a 2 Player Network game in progress. It chooses the first available game to observe, regardless of what is available. This Spectate Network option does not have the ability to alter the game in progress.  ");
		
		
		// does not proceed if the user closes the dialog box
	}
	
	
	

	@FXML
	void QuitProgram ( ActionEvent event ) {
		// implement graceful local listener shutdown

		alert_.setTitle("Attempting An Irevocable Process");
		alert_.setHeaderText("Are you sure you want to quit?");
		alert_.setContentText("choose ok to quit");
		Optional<ButtonType> choice = alert_.showAndWait();

		if ( choice.get() == ButtonType.OK ) {
			SetComm("localhost",9000);
			sendMessage("shutdown");
			Platform.exit();
		}
	}

	// starts a new game
	@FXML
	private void newGame () {

		if ( onStartUp_ ) {

			initBoard();
			sendMessage(playerMode_); // sends new game message
			if ( !playerMode_.equals("Join Game")
			    && !playerMode_.equals("Spectate Network") ) {
				sendObject(gameVariant_);
			}
			canvas_.draw();
			// set up display labels
			setDisplays();
			resetTimer();

			onStartUp_ = false;
		} else {
			// if this is not the initial start up then we ask to make sure the user
			// wants to start a new game
			alert_.setTitle("Attempting An Irevocable Process");
			alert_.setHeaderText("Are you sure you want to start a new game?");
			alert_.setContentText("choose ok to start a new game");
			Optional<ButtonType> choice = alert_.showAndWait();

			if ( choice.get() == ButtonType.OK ) {
				initBoard();
				sendMessage(playerMode_); // sends new game message
				if ( !playerMode_.equals("Join Game")
				    && !playerMode_.equals("Spectate Network") ) {
					sendObject(gameVariant_);
				}
				canvas_.draw();
				// set up display labels
				setDisplays();
				resetTimer();
			}
		}
	}

	@FXML
	private void resetGame () {

		alert_.setTitle("Attempting An Irevocable Process");
		alert_.setHeaderText("Are you sure you want to reset the game?");
		alert_.setContentText("choose ok to reset");
		Optional<ButtonType> choice = alert_.showAndWait();

		if ( choice.get() == ButtonType.OK ) {
			if ( player_ != GamePiece.VOID ) {
				initBoard();
				sendMessage("resetGame");
				canvas_.draw();
				// set up display labels
				setDisplays();
				resetTimer();
			}
		}
	}

	private void resetTimer () {
		// reset timer
		if ( timeline_ != null ) {
			timeline_.stop();
		}
		timeSeconds_.set(0);
		timeline_ = new Timeline();
		timeline_.getKeyFrames().add(new KeyFrame(Duration.seconds(3600),
		                                          new KeyValue(timeSeconds_,3600)));
		timeline_.playFromStart();
		moveCount_.set(0);

	}

	@FXML
	private void mousePressed ( MouseEvent event ) {

		int row, col;

		// if there is no winner and the current player is the player for this GUI
		// or both players use this GUI, then try to place move
		if ( gameState_.getWinner() == GamePiece.NONE
		    && (player_ == currentPlayer_ || player_ == GamePiece.NONE) ) {
			System.out.println();
			col = (int) Math.floor(event.getX() / canvas_.getWidth() * 19);
			row = (int) Math.floor(event.getY() / canvas_.getHeight() * 19);
			sendMessage("Move"); // send move
			sendObject(new Move(currentPlayer_,row,col));
		} else {
			update();
		}
	}

	// updates/draws the display
	private void update () {

		int row, col;

		gameVariant_ = gameState_.getGameVariant();
		GamePiece[] gp = gameState_.getBoard().getBoard();
		canvas_.draw();
		// draw GamePieces
		for ( int i = 0 ; i < gp.length ; i++ ) {
			row = i / 19;
			col = i % 19;
			if ( gp[i] == GamePiece.BLACK || gp[i] == GamePiece.WHITE ) {
				canvas_.getGraphicsContext2D().setStroke(Color.BLACK);
				canvas_.getGraphicsContext2D()
				    .strokeOval(col * canvas_.getWidth() / 19,
				                row * canvas_.getHeight() / 19,canvas_.getWidth() / 20,
				                canvas_.getHeight() / 20);
				canvas_.getGraphicsContext2D().setFill(Color.WHITE);
				if ( gp[i] == GamePiece.BLACK ) {
					canvas_.getGraphicsContext2D().setFill(Color.BLACK);
				}
				canvas_.getGraphicsContext2D().fillOval(col * canvas_.getWidth() / 19,
				                                        row * canvas_.getHeight() / 19,
				                                        canvas_.getWidth() / 20,
				                                        canvas_.getHeight() / 20);
			}
		}
		// set current player indicator and movecount
		white_.setOpacity(blackMove_ ? 0 : 1);
		black_.setOpacity(blackMove_ ? 1 : 0);
		moveCount_ = new SimpleIntegerProperty(moveCount_.intValue() + 1);
		moveCountLabel_.textProperty().bind(moveCount_.asString());

		if ( gameState_.getLastMovePlaced() && !player_.equals(GamePiece.VOID) ) { // ask
		                                                                           // if
		                                                                           // last
		                                                                           // move
		                                                                           // was
		                                                                           // legal
			blackMove_ = !blackMove_; // switch player
			currentPlayer_ = GamePiece.WHITE;
			if ( blackMove_ ) {
				currentPlayer_ = GamePiece.BLACK;
			}
			// set labels
			white_.setOpacity(blackMove_ ? 0 : 1);
			black_.setOpacity(blackMove_ ? 1 : 0);
			moveCount_ = new SimpleIntegerProperty(gameState_.getMoveCount());
			moveCountLabel_.textProperty().bind(moveCount_.asString());
			whiteCaptures_.setText(Integer.toString(gameState_.getWhiteCaptures()));
			blackCaptures_.setText(Integer.toString(gameState_.getBlackCaptures()));
		}
		// Check for a winner
		if ( gameState_.getWinner() != GamePiece.NONE ) { // game has been won
			timeline_.stop();
			// launch WINNER dialog here
			Alert winner =
			    new Alert(Alert.AlertType.INFORMATION,gameState_.getWinner()
			        + " is the winner! \nPlease restart the game to play again.");
			winner.setTitle("Game Over!");
			winner.setHeaderText(null);
			winner.showAndWait();
		}
	}

	// asks server for list of available GameVariants
	private void getGameVariants () {
		sendMessage("getGameVariants");
		synchronized ( comm_ ) {
			try {
				comm_.wait();
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// sets the instance var to the List
	public void setGameVariants ( List<GameVariant> list ) {
		gameVariants_ = list;
	}

	// sets the instance var to the desired GameVariant
	public void setGameVariant ( GameVariant game ) {
		gameVariant_ = game;

	}

	// sets the instance var to the supplied GameState and updates the display
	public void setGameState ( GameState gameState ) {
		gameState_ = gameState;
		System.out.println("GUI CTL: setGameState");
		// Application thread needs to run update to alter the GUI, needs to use
		// runLater
		Platform.runLater(new Runnable() {
			public void run () {
				update();
			}
		});
	}

	// the following 4 methods are used for message communication, calling the
	// appropriate comm_ method
	private void sendMessage ( String message ) {
		comm_.sendMessage(message);
	}

	private String readMessage () {
		String message = comm_.receiveMessage();
		return message;
	}

	private void sendObject ( Object object ) {
		comm_.sendObject(object);
	}

	private Object readObject () {
		Object object = comm_.receiveObject();
		return object;
	}

	private void SetComm ( String hostname, int port ) {
		// launch new comm_ object
		comm_ = new ClientComm();
		comm_.setClient(this);
		comm_.setHost(hostname);
		comm_.setPort(port);
		thread_ = new Thread(comm_);
		thread_.start();
		synchronized ( comm_ ) { // wait for ClientComm run() to complete
			try {
				comm_.wait();
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setDisplays () {

		variantDisplay_.setValue(gameVariant_.getName());
		playerModeDisplay_.setValue(playerMode_);
		playerDisplay_.setValue(player_.toString());

		if ( player_ == GamePiece.NONE ) { // both players
			currentPlayer_ = GamePiece.BLACK;
			playerDisplay_.set("Both");
		}
		if ( player_ == GamePiece.VOID ) { // Spectate
			playerDisplay_.set("OBSERVER");
		}
	}

}
