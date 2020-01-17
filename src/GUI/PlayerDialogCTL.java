package GUI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Core.Controller;
import Core.GameRule;
import Core.GameVariant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import GUI.MainController;

/**
 * This class handles the options dialog box so the user can effectively choose
 * the game type that they want to play
 * 
 */
public class PlayerDialogCTL {
	
	private String hostname_ = null;
	private int port_ = 0;

	@FXML
	private ChoiceBox<String> playerOptionBox_;

	@FXML
	private Button OK_;

	private String playerMode_;

	@FXML
	private TextField txtBoxHost_;

	@FXML
	private TextField txtBoxPort_;

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
		pickChoice(playerOptionBox_.getValue());
		if(!(txtBoxHost_.getText().equals("Enter Host Name")) && !(txtBoxPort_.getText().equals("Enter Port #"))) {
			hostname_ = txtBoxHost_.getText();
			port_ = Integer.parseInt(txtBoxPort_.getText());
		}

		OK_.getScene().getWindow().hide();
	}

	/**
	 * Puts all the available choices in a list so they can be displayed on the
	 * screen
	 */
	private void populateChoice () {

		ObservableList<String> availableChoices = null;
		availableChoices = FXCollections.observableArrayList("1 Player", "2 Player Local", "2 Player Network", "Join Game", "Spectate Network");
		playerOptionBox_.setItems(availableChoices);
		playerOptionBox_.setValue(availableChoices.get(0));
		
		txtBoxHost_.setText("localhost");
		txtBoxPort_.setText("9000");
	}

	/**
	 * Game type choice selected
	 * 
	 * @param choice
	 *          - the selected game type
	 */
	private void pickChoice ( String choice ) {

		playerMode_ = choice;

	}

	/**
	 * getter for the selected game type
	 * 
	 * @return - which game type is chosen
	 */
	public String getChoice () {
		return playerMode_;
	}

	public String getHost() {
		return hostname_;
	}

	public int getPort() {
		return port_;
	}

	@FXML
	void ClearText_(MouseEvent event) {
		TextField txtbox = (TextField)event.getSource();
		txtbox.setText("");
	}

	@FXML
	void getSelectedMode_(MouseEvent event) {
		String currentMode = playerOptionBox_.getValue();
		if(currentMode.equals("Join Game") || currentMode.equals("2 Player Network") || currentMode.equals("Spectate Network")) {
			txtBoxHost_.setDisable(false);
			txtBoxPort_.setDisable(false);

		} else {
			txtBoxHost_.setDisable(true);
			txtBoxPort_.setDisable(true);

		}
	}
	
	

}
