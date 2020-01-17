package Core;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 *	used for controlling game interactions to the Core
 */
public class Controller implements PropertyChangeListener{
	
	private volatile Game game_;
	private ServerComm serverComm_;
	private String playerMode_;

	public Controller() {
		
		System.out.println("CONTROLLER: Started");
		playerMode_ = "1 Player";		// always initially a 1 player game
	}

		// propertychange always sends a GameState object to the client
	@Override
	public void propertyChange ( PropertyChangeEvent evt ) {
		System.out.println("CTL: PROPERTY CHANGE EVENT");
		serverComm_.sendMessage("GameState");
		serverComm_.sendObject(evt.getNewValue());
	}

		// starts a new game with a GameVariant type
	public void newGame(GameVariant gameType) {
		
		game_ = new Game(gameType);
		game_.gameState_.addPropertyChangeListener(this);	// adds controller as a propertychangelistener on the resulting GameState object
		game_.gameState_.firePCS();	// force GameState to be sent initially
	}
	
		// resets a game
	public void resetGame() {
		game_.gameState_.init();	// initialize the current GameState
		game_.gameState_.firePCS();	// send updated GameState
	}

		// tries to place a client requested move
	public void placeMove(Move move) {
		System.out.println("submitting move " + move);
		GamePiece gp = game_.playerMove(move);		// attempts move
		System.out.println("Move Win Result " + gp);	// display move win result
		
		//handle 1 player game automatic move
		switch (playerMode_) {
			case "1 Player":
					// if the last attempted move was successful and 1 player mode, and nobody won yet
				if(game_.gameState_.getLastMovePlaced() && game_.gameState_.getWinner() == GamePiece.NONE ) {
					// computer picks next move
					gp = game_.playerMove(autoMove());
					while (!game_.gameState_.getLastMovePlaced()) {		// last move not successful
						gp = game_.playerMove(autoMove());							// try again
					}
				}
				break;
		}

	}
	
		// returns available GameVariants from Core
	public ArrayList<GameVariant> getGameVariants() {
		return GameVariant.getGameVariants();
	}

//generates an automatic move
  public Move autoMove(){
      Move move = null;
      GameAI  AI= new GameAI(game_.gameState_);
      
      if(AI.MakeMove() != null) {
          move = AI.MakeMove();
          
          if(move.piece_==GamePiece.NONE) {
              int size = game_.gameState_.getBoard().getBoard().length;
              int place = (int) (Math.random() * size);        // pick random array spot
              int cols = 19;

              while (game_.gameState_.getBoard().getBoard()[place] != GamePiece.NONE) {    // if the spot chosen is occupied
                  place = (int) (Math.random() * size);        // try again
              }
              
              System.out.println("AutoMove index - " + place + " row " + place/cols + " col " + place % cols);
              move = new Move(GamePiece.WHITE,place/cols , place % cols);    // set move
          }
      } else {
          move = null;
          int size = game_.gameState_.getBoard().getBoard().length;
          int place = (int) (Math.random() * size);        // pick random array spot
          int cols = 19;

          while (game_.gameState_.getBoard().getBoard()[place] != GamePiece.NONE) {    // if the spot chosen is occupied
              place = (int) (Math.random() * size);        // try again
          }
          
          System.out.println("AutoMove index - " + place + " row " + place/cols + " col " + place % cols);
          move = new Move(GamePiece.WHITE,place/cols , place % cols);    // set move
      }

      return move;
  }

	
		// sets the serverComm object for this controller to use
	public void setServerComm(ServerComm serverComm) {
		serverComm_ = serverComm;
	}
	
		// sets the playerMode and external references
	public void setPlayerMode(String mode) {
		
		playerMode_ = mode;
		switch (playerMode_) {
		
			case "2 Player Network":
				Listener.registerController(this);	// registers this controller as a joinable game with the Listener
				break;
				
			case "Join Game":											// joins a network game
				Controller join = Listener.getController();		// gets the controller for a previously initiated network game
				if(join != null) {
					game_ = join.game_;		// returns a reference to the game and adds this controller as a PCL
					game_.gameState_.addPropertyChangeListener(this);
					game_.gameState_.firePCS();
					Listener.registerGame(game_);			// registers this network game as observable (Spectate)
				}
				break;
				
			case "Spectate Network":
				Game game = Listener.getGame();		// gets an available observable game
				if(game != null) {
					game_ = game;// returns a reference to the game and adds this controller as a PCL
					game_.gameState_.addPropertyChangeListener(this);
					game_.gameState_.firePCS();
				}
				break;
		
		}
		
		
	}
		// returns string representation of this controller
	public String toString() {
		
		return String.valueOf(this.hashCode());
	}
	
	public void shutdown() throws IOException {
		Listener.shutdown();
	}
	
}
