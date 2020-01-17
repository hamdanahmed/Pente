package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Core.GameVariant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class AboutDialogCTL {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Text text_;

	@FXML
	private BorderPane HelpDialog_;

	@FXML
	private Button OK_;

	@FXML
	private Button getHelp_;

	@FXML
	void CloseDialog ( ActionEvent event ) {
		OK_.getScene().getWindow().hide();
	}

	@FXML
	void initialize () {

		/*
		  Image shrike = new Image("file:src/images/MusashiShrike.jpeg");
		BackgroundImage aboutBackground = new
		  BackgroundImage(shrike,BackgroundRepeat.NO_REPEAT,
		  BackgroundRepeat.NO_REPEAT,
		  BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
		  HelpDialog_.setBackground(new Background(aboutBackground)); 
		  */
		  //this allows
		  //for setting the back ground of the about menu to something, however at
		  //the moment CSS is making the menu laggy when ever i try to change the
		  //color of the font to something that doesnt blend in with the background
		 

		text_.setText(" Launching the GUI (Main) will launch a completely self-"
		    + "contained 1 Player Pente Standard game ready to play. \r\n" + "\r\n"
		    + "                The top of the game board shows the Game"
		    + " Variant, Player(s) using this GUI (White, Black, Both, None),"
		    + " and the Player Mode (1 Player, 2 Player Local, 2 Player"
		    + " Network, Join Game, Spectate). \r\n" + "\r\n"
		    + "                The bottom of the game board shows the Captured"
		    + " Pieces count, a timer since the current game started (locally)"
		    + ", the number of moves made, and an indicator of which player’s"
		    + " move it currently is,  on the right and left as a round game"
		    + " piece of the proper color. There is also a Restart Game button"
		    + " which will restart the current game in progress. \r\n" + "\r\n"
		    + "                The game board itself is a 19 x 19 grid of play"
		    + " spaces represented as grid intersections. The center space is"
		    + " grey as many game variations require the first move there."
		    + " There is a centered dark green area comprised of spaces 2"
		    + " intersections away from center to illustrate the restricted"
		    + " area for the Tournament variation. \r\n" + "\r\n"
		    + "                The File – New menu item will create a new game"
		    + " using the currently selected Game Variation and Player Mode"
		    + " (Options menu) \r\n" + "\r\n"
		    + "                Choosing a game to play can be done via the"
		    + " Options - Game Variations menu item. The game will start after"
		    + " clicking OK using the current Player Mode. \r\n" + "\r\n"
		    + "                Choosing a player mode can be done via the"
		    + " Options – Choose Player Mode menu item. There are 5 player"
		    + " mode options.  \r\n" + "\r\n"
		    + "                1 Player uses the local server to run the"
		    + " game and is played against a computer player.  \r\n" + "\r\n"
		    + "                2 Player Local uses the local server to run"
		    + " the game and the GUI is shared by 2 players. \r\n" + "\r\n"
		    + "                2 Player Network shows an option for selecting"
		    + " the host and port to create a 2 player game which will run on"
		    + " the server at that address. The local server can be used as"
		    + " well. The initiating player will be the first player and can"
		    + " make the first move immediately. They will then have to wait"
		    + " for another remote player to join the game. \r\n" + "\r\n"
		    + "                Join Game gives an option to join a 2 Player"
		    + " Network initiated game previously started by another remote"
		    + " user on some server. The host and port of that server can be"
		    + " entered if different than the local host. It will choose the"
		    + " most recently initiated 2 Player Network game on that server."
		    + " The joined game is added to a list for other users to observe."
		    + " \r\n" + "\r\n"
		    + "                Spectate Network gives an option to observe a 2"
		    + " Player Network game in progress. It chooses the first"
		    + " available game to observe, regardless of what is available."
		    + " This Spectate Network option does not have the ability to"
		    + " alter the game in progress.  ");

	}

}