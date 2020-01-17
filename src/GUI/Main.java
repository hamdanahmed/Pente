
package GUI;

import Core.Listener;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

// starting point for GUI side of the program
public class Main extends Application {
	
	Thread thread_;
	Listener listener_;
	@Override
	public void start ( Stage primaryStage ) {
		
		// start local listener as thread
		listener_ = new Listener();
		 thread_ = new Thread(listener_);
			thread_.start();
			
			// launch GUI
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PenteBoard.fxml"));
			
			BorderPane root = fxmlLoader.load();
			MainController controller = (MainController)fxmlLoader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Ultimate Pente");
			primaryStage.setOnCloseRequest(WindowEvent -> {controller.QuitProgram(null);});
			primaryStage.show();
	
			
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
		
	public static void main ( String[] args ) {
		launch(args);
	}
	
	
}
