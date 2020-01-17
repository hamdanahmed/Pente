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
 */
public class PlayerDialog extends Application {

	@FXML
	private ChoiceBox<String> PlayerOptionBox_;

	public void start ( Stage primaryStage ) {
		try {
			BorderPane root = (BorderPane) FXMLLoader
			    .load(getClass().getResource("PlayerDialog.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Select Player Mode");
			primaryStage.show();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
}
