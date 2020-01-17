package Core;

import java.util.ArrayList;
import java.util.List;

/**
 *  Generic GameRule covering all the common methods of the
 *         various GameRule classes
 */
public class GameRuleGeneric {

	private GameRule rule_;
	public GameState state_;
	public Move move_;
	private int size_;
	private int cols_;
	private int rows_;
	private GamePiece[] board_;

	GameRuleGeneric ( GameRule rule ) {
		rule_ = rule;
		size_ = rule_.getBoardSize();
		rows_ = rule_.getBoardSize();
		cols_ = rule_.getBoardSize();
		state_ = new GameState(size_);
		board_ = state_.getBoard().getBoard();
	}

	/**
	 * Determines if the current move is legal. This only considers generic cases
	 */
	public boolean isLegalMove ( GameState state, Move move ) {
		state_ = state;
		board_ = state_.getBoard().getBoard();
		move_ = move;
		int center = state_.getBoard().getCenter();


		// generic illegal moves
		if( state_.getWinner() != GamePiece.NONE) {
			return false; // game has been won
		}
		if ( move.moveCol_ > rule_.getBoardSize()
				|| move.moveRow_ > rule_.getBoardSize() ) {
			return false; // move is off the board
		}
		if ( state_.getLastMove().piece_ == move.piece_ ) {
			return false; // moves must alternate
		}
		if ( GamePiece.NONE != state_.gameBoard_.getBoardCell(move_.moveRow_,
		                                                      move_.moveCol_) ) {
			return false; // move space is occupied
		}
		if ( GamePiece.NONE == move_.piece_ || GamePiece.VOID == move_.piece_ ) {
			return false; // must place a valid GamePiece
		}
		if ( !rule_.isLegalMove(state_,move_) ) {
			return false; // check game specific moves
		}
		return true;

	}

	/**
	 * evaluate the move by cycling though the various rows, columns, and
	 * diagonals and feed into column, row, and diagonal evaluation functions.
	 */
	public GamePiece eval () {
		GamePiece winner = GamePiece.NONE;
		for ( int col = 0 ; col < cols_ ; col++ ) {
			winner = evalCol(col);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}

			winner = evalDiag(0,col,1);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}

			winner = evalDiag(0,col,-1);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
		}
		for ( int row = 0 ; row < rows_ ; row++ ) {
			winner = evalRow(row);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
			winner = evalDiag(row,0,1);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
			winner = evalDiag(row,cols_ - 1,-1);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
		}
		return GamePiece.NONE;
	}

	/**
	 * the following 3 functions cycle through the pieces in a column, row, and
	 * diagonal and add them to a sequence of pieces for evaluation if a game
	 * action (winning or capture) occurred.
	 */
	public GamePiece evalCol ( int testCol ) {
		List<Move> sequence_ = new ArrayList<Move>();
		for ( int r = 0 ; r < rows_ ; r++ ) {
			sequence_.add(new Move(board_[r * cols_ + testCol],r,testCol));
		}
		return rule_.evalWinner(sequence_,state_);
	}

	public GamePiece evalRow ( int testRow ) {
		List<Move> sequence_ = new ArrayList<Move>();
		for ( int c = 0 ; c < cols_ ; c++ ) {
			sequence_.add(new Move(board_[cols_ * testRow + c],testRow,c));
		}
		return rule_.evalWinner(sequence_,state_);
	}

	public GamePiece evalDiag ( int row, int col, int coldir ) {
		List<Move> sequence_ = new ArrayList<Move>();
		for ( int r = row, c = col ; r < rows_
				&& ((coldir > 0 && c < cols_) || (coldir < 0 && c >= -1)) ; r++, c +=
				coldir ) {
			sequence_.add(new Move(board_[r * cols_ + c],r,c));
		}
		return rule_.evalWinner(sequence_,state_);
	}

	public int getBoardSize () {
		return rule_.getBoardSize();
	}

	public GameState getGameState() {
		return state_;
	}
}
