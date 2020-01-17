package Core;

import java.io.Serializable;

/**
 *  Represents the gameboard for the game
 */
public class GameBoard  implements Serializable {

	private static final long serialVersionUID = 1L;
	protected GamePiece[] board_;
	private int rows_, cols_;
	private int center_;

	public GameBoard ( int size ) {

		if ( 1 == size % 2 ) {
			rows_ = size;
			cols_ = size;
			center_ = (rows_ - 1) / 2;
			board_ = new GamePiece[size * size];
			clear();
		} else {
			// not legal board
		}
	}

	/**
	 * Clear the game board.
	 */
	public void clear () {
		for ( int i = 0 ; i < board_.length ; i++ ) {
			board_[i] = GamePiece.NONE;
		}
	}

	/**
	 * @param the
	 *          board cell to set by components
	 */
	public void setBoardCell ( GamePiece piece, int row, int col ) {
		board_[row * cols_ + col] = piece;
	}

	/**
	 * @param the
	 *          board cell to set by move
	 */
	public void setBoardCell ( Move move ) {
		board_[move.moveRow_ * cols_ + move.moveCol_] = move.piece_;
	}

	/**
	 * @param the
	 *          board cell to get
	 */
	public GamePiece getBoardCell ( int row, int col ) {
		return board_[row * cols_ + col];
	}

	/**
	 * @return the board
	 */
	public GamePiece[] getBoard () {
		return board_;
	}

	/**
	 * @return the center of the board
	 */
	public int getCenter () {
		return center_;
	}

}
