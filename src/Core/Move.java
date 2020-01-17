package Core;

import java.io.Serializable;

/**
 * represents a move in the Pente game, contains a GamePiece, a
 *         row and column for the move.
 */
public class Move implements Serializable{

	private static final long serialVersionUID = 1L;
	public GamePiece piece_ = GamePiece.NONE;
	public int moveRow_ = 0;
	public int moveCol_ = 0;

	/**
	 * @param piece
	 * @param moveRow
	 * @param moveCol
	 */
	public Move ( GamePiece piece, int moveRow, int moveCol ) {
		piece_ = piece;
		moveRow_ = moveRow;
		moveCol_ = moveCol;
	}
	
public boolean equals(Move other) {
		
		if(this.piece_ == other.piece_ && this.moveCol_ == other.moveCol_ && this.moveRow_ == other.moveRow_) {
			return true;
		}
		return false;
		
	}

}
