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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class HelpDialogCTL {

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
    private ChoiceBox<String> helpOptionsBox_;

    @FXML
    void CloseDialog(ActionEvent event) {
    	OK_.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
    	populateList();
    	getHelp_.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			text_.setText(setLabel());
    		}
    	});
    }
    
    protected void populateList() {
    	String penteStandard = "Standard Pente";
    	String penteTournament = "Tournament Pente";
    	String penteNoCap = "NoCapture Pente";
    	String penteKeryo = "Keryo Pente";
    	String penteFreestyle = "FreeStyle Pente";
    	String penteFive = "FiveInARow Pente";
    	String gomStandard = "Standard Gomoku";
    	String gomFreestyle = "FreeStyle Gomoku";
    	String gomCaro = "Caro Gomoku";

    	ObservableList<String> types = FXCollections.observableArrayList(
    			penteStandard,penteTournament,penteNoCap,penteKeryo,penteFreestyle,
    			penteFive,gomStandard,gomFreestyle,gomCaro);
    	helpOptionsBox_.setItems(types);
    	helpOptionsBox_.setValue(types.get(0));
    }
    
    protected String setLabel () {
    	String instr = helpOptionsBox_.getValue();
    	
    	switch (instr) {
    	case "Tournament Pente":
    		return "The players first move must be in the center. "
    				+ "To make up for the advantage, the second player must play three or more intersections"
    				+ " away from the center for the following turn. The rest of the rules line up with the"
    				+ " standard variation.";
    	case "Standard Pente":
    		return "The game ends if one player gets five stones in a row, or if one player"
    				+ " captures five pairs from their opponent. You take turns placing stones"
    				+ " with your opponent until one of these conditions is met.";
    	case "NoCapture Pente":
    		return "The stones remain where they are placed until the end of the game,"
    				+ " meaning that captures are not allowed. This restricts the way to win"
    				+ " to only five in a row, making this similar to Gomoku.";
    	case "Keryo Pente":
    		return "This mode starts by placing a stone in the center. After this, the game "
    				+ "proceeds as normal other than the fact that sets of three stones"
    				+ " can be captured in this version. Now you can win by either capturing"
    				+ " fifteen stones total, or by getting five in a row.";
    	case "FreeStyle Pente":
    		return "The first move may be made anywhere on the board. The rest of the Standard"
    				+ " Pente rules apply.";
    	case "FiveInARow Pente":
    		return "This game mode allows your first move to be anywhere on the board, "
    				+ "and sets the only win condition to be five in a row.";
    	case "Standard Gomoku":
    		return "Standard Gomoku revolves around creating lines only. Captures don't occur, "
    				+ "meaning that the only way to win is with five in a row. In this version, "
    				+ "lines over five in a row (called overlines) don't count.";
    	case "FreeStyle Gomoku":
    		return "FreeStyle Gomoku has the same rules of Standard Gomoku, with the exception "
    				+ "being that overlines DO count as wins.";
    	case "Caro Gomoku":
    		return "Caro Gomoku allows both overlines and five in a row wins, with special rules"
    				+ " specifically for five in a row. Here, for the five in a row, the ends "
    				+ "of the line must both be unblocked by the other color.";	
    	}
    	
    	text_.setText(instr);
    	return "How did you manage to pick an option which doesn't exist?";
    }
}