package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Core.GameRule;
import Core.GameVariant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import GUI.MainController;

/**
 * This class handles the options dialog box so the user can effectively choose
 * the game type that they want to play
 * 
 */
public class OptionsDialogCTL {

	@FXML
	private ChoiceBox<String> gameOptionBox_;

	@FXML
	private Button OK_;

	private GameVariant gameVariant_;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // This method is called by the FXMLLoader when initialization is
	      // complete
	void initialize () {
		populateChoice();

	}

	/**
	 * Closes the dialog when the user clicks "OK" or clicks the X in the corner
	 * 
	 * @param event
	 *          - user clicking to close the box
	 */
	@FXML
	void CloseDialog ( ActionEvent event ) {
		pickChoice(gameOptionBox_.getValue());
		OK_.getScene().getWindow().hide();
	}

	/**
	 * Puts all the available choices in a list so they can be displayed on the
	 * screen
	 */
	private void populateChoice () {

		ObservableList<String> availableChoices = null;
		availableChoices = FXCollections.observableArrayList();
		for ( GameVariant g : GameVariant.getGameVariants() ) {
			availableChoices.add(g.getName());
		}
		// availableChoices.addAll(list);
		gameOptionBox_.setItems(availableChoices);
		gameOptionBox_.setValue(availableChoices.get(0));
	}

	/**
	 * Game type choice selected
	 * 
	 * @param choice
	 *          - the selected game type
	 */
	private void pickChoice ( String choice ) {
		for ( GameVariant g : GameVariant.values() ) {
			if ( g.getName().equals(choice) ) {
				gameVariant_ = g;
			}
		}
	}

	/**
	 * getter for the selected game type
	 * 
	 * @return - which game type is chosen
	 */
	public GameVariant getChoice () {
		return gameVariant_;
	}

}
