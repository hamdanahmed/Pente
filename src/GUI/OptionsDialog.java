package GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Opens up the options dialog box to select a game type
 * 
 * 
 */
public class OptionsDialog extends Application {

	@FXML
	private ChoiceBox<String> gameOptionBox_;

	public void start ( Stage primaryStage ) {
		try {
			BorderPane root = (BorderPane) FXMLLoader
			    .load(getClass().getResource("OptionsDialog.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Select Game");
			primaryStage.show();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
}
