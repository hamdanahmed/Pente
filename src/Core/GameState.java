package Core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * represents the state of the game
 */

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	protected volatile GameBoard gameBoard_;
	protected volatile List<Move> gameMoves_;
	protected volatile int blackCaptures_, whiteCaptures_, blackCount_, whiteCount_;
	protected volatile GamePiece winner_;
	protected boolean lastMovePlaced = false;
	protected PropertyChangeSupport pcs_;
	protected GameVariant gameVariant_;
	private int size_;

	public GameState ( int size ) {
		
		size_ = size;
		pcs_ = new PropertyChangeSupport(this);
		init();
	}
	
	public void init() {
		gameBoard_ = new GameBoard(size_);
		gameMoves_ = new ArrayList<Move>();
		winner_ = GamePiece.NONE;
		blackCaptures_ = 0;
		whiteCaptures_ = 0;
		blackCount_ = 0;
		whiteCount_ = 0;
		lastMovePlaced = false;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener observer) {
		pcs_.addPropertyChangeListener(observer);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener observer) {
		pcs_.removePropertyChangeListener(observer);
	}


	public void createMove ( Move move ) {
		gameBoard_.setBoardCell(move);
		gameMoves_.add(move);
		//pcs_.firePropertyChange("GameState",null,this);
	}

	public boolean isFirstMove () {
		if ( gameMoves_.isEmpty() ) {
			return true;
		}
		return false;
	}

	public boolean isSecondMove () {
		if ( 2 == gameMoves_.size() ) {
			return true;
		}
		return false;
	}

	public Move getLastMove () {
		if ( gameMoves_.isEmpty() ) {
			return new Move(GamePiece.NONE,0,0);
		} else {
			return gameMoves_.get(gameMoves_.size() - 1);
		}
	}

	public GameBoard getBoard () {
		return gameBoard_;
	}
	
	public int getMoveCount() {
		return gameMoves_.size();
	}
	
	public int getBlackCaptures() {
		return blackCaptures_;
	}
	
	public int getWhiteCaptures() {
		return whiteCaptures_;
	}
	
	public int getBlackCount() {
		return blackCount_;
	}
	
	public int getWhiteCount() {
		return whiteCount_;
	}
	
	public void setWinner(GamePiece winner) {
		winner_ = winner;
		//pcs_.firePropertyChange("GameState",null,this);
	}
	
	public GamePiece getWinner() {
		return winner_;
	}
	
	public boolean getLastMovePlaced() {
		return lastMovePlaced;
	}
	
	public void setlastMovePlaced(boolean set){
		lastMovePlaced = set;
		//pcs_.firePropertyChange("GameState",null,this);
	}
	
	public void firePCS() {
		pcs_.firePropertyChange("GameState",null,this);
	}

	/**
	 * @return the gameVariant
	 */
	public GameVariant getGameVariant () {
		return gameVariant_;
	}

	/**
	 * @param gameVariant the gameVariant to set
	 */
	public void setGameVariant ( GameVariant gameVariant ) {
		gameVariant_ = gameVariant;
	}
	
}
