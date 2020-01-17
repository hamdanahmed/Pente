package Core;

import java.util.List;

/**
 *  GameRule for the Pente Tournament game variant
 */
public class GameRulePenteTournament implements GameRule {

	protected final int BoardSize_ = 19;
	protected final boolean firstMoveCenter_ = true;
	protected final GamePiece firstMoveGamePiece_ = GamePiece.BLACK;
	protected final boolean nextMoveRestricted_ = true;
	protected final int nextMoveRange_ = 2;
	protected final boolean canCapture_ = true;
	protected final boolean capturesWin_ = true;
	protected final int captureSizeMin_ = 2;
	protected final int captureSizeMax_ = 2;
	protected final int captureCountToWin_ = 10;
	protected final int captureTotalCountToWin_ = 0;
	protected final boolean winExactly_ = false;
	protected final int winCount_ = 5;
	protected final boolean winUnblocked_ = false;
	// protected final boolean threeXthree_ = false;
	protected int blackCaptures_;
	protected int whiteCaptures_;

	public GameRulePenteTournament () {

	}

	/**
	 * Determines if the current move is legal for this game variant
	 */
	public boolean isLegalMove ( GameState state, Move move ) {

		int center = state.getBoard().getCenter();
		// game specific illegal moves
		if ( firstMoveCenter_ && state.isFirstMove() && move.moveCol_ != center
		    && move.moveRow_ != center ) {
			return false; // first move must be on center
		}

		if ( firstMoveGamePiece_ != move.piece_ && state.isFirstMove() ) {
			return false; // first move must be specified GamePiece
		}

		if ( nextMoveRestricted_ && move.moveCol_ >= center - nextMoveRange_
		    && move.moveCol_ <= center + nextMoveRange_
		    && move.moveRow_ >= center - nextMoveRange_
		    && move.moveRow_ <= center + nextMoveRange_ && state.isSecondMove() ) {
			return false; // 2nd first player move must be outside of restricted space
		}

		return true; // move is legal
	}

	public int getBoardSize () {
		return BoardSize_;
	}

	/**
	 * Evaluates the result of the move
	 */
	@Override
	public GamePiece evalWinner ( List<Move> sequence, GameState state ) {

		GamePiece winner = GamePiece.NONE;

		winner = evalRow(sequence,state);
		if ( GamePiece.NONE == winner ) {
			winner = evalCapture(sequence,state);
		}
		if ( GamePiece.NONE != winner ) {
			return winner;
		}
		return GamePiece.NONE;

	}

	/**
	 * Evaluates if a win occurred from 5+ in a row
	 */
	@Override
	public GamePiece evalRow ( List<Move> sequence, GameState state ) {

		Move last = new Move(GamePiece.NONE,0,0);
		int runCount = 0; // counts a run of same gamepieces

		// iterate through the sequence
		for ( int i = 0 ; i < sequence.size() ; i++ ) {
			if ( sequence.get(i).piece_ == GamePiece.NONE ) { // reset tracker
				runCount = 0;
			} else if ( runCount == 0 ) { // track first gamePiece
				runCount++;
			} else if ( sequence.get(i).piece_.equals(last.piece_)
			    && last.piece_ != GamePiece.NONE ) { // another same gamepiece found
				runCount++;
			} else if ( sequence.get(i).piece_ != last.piece_
			    && sequence.get(i).piece_ != GamePiece.NONE ) {
				// sequential different gamepiece found
				runCount = 1;
			} else {
				// do nothing
			}
			if ( runCount >= winCount_ ) {
				return last.piece_; // 5 gamePieces in a row, return winner
			}
			last = sequence.get(i); // record last gamePiece
		}

		return GamePiece.NONE;
	}

	/**
	 * Determines if the current move resulted in a capture and if that resulted
	 * in a win
	 */
	@Override
	public GamePiece evalCapture ( List<Move> sequence, GameState state ) {
		Move last = new Move(GamePiece.NONE,0,0);
		int runCount = 0; // counts a run of same gamepieces
		boolean switched = false; // flag ta switch of gamepieces occured
		boolean captured = false; // flag to denote a capture occured

		
	// iterate through the sequence
		
			for ( int i = 0 ; i < sequence.size() ; i++ ) {
				if ( sequence.get(i).piece_ == GamePiece.NONE ) { // reset tracker
					switched = false;
					captured = false;
					runCount = 0;
				} else if ( runCount == 0 ) { // track first gamePiece
					runCount++;
				} else if ( sequence.get(i).piece_.equals(last.piece_)
				    && last.piece_ != GamePiece.NONE ) { // another same gamepiece found
					runCount++;
				} else if ( sequence.get(i).piece_ != last.piece_
				    && sequence.get(i).piece_ != GamePiece.NONE && !switched ) {
					// sequential different gamepiece found
					switched = true;
					runCount = 1;
				} else if ( sequence.get(i).piece_ != last.piece_
				    && sequence.get(i).piece_ != GamePiece.NONE && switched
				    && runCount >= captureSizeMin_ && runCount <= captureSizeMax_
				    &&( sequence.get(i).equals(state.getLastMove())		// no self capture
				    || sequence.get(i-(runCount +1)).equals(state.getLastMove()))) {
						captured = true; // captured 
						if ( captured ) {
						// remove captured pieces.
							for ( int j = 1 ; j <= runCount ; j++ ) {
								state.getBoard().setBoardCell(GamePiece.NONE,
								                              sequence.get(i - j).moveRow_,
								                              sequence.get(i - j).moveCol_);
							}
							
						}
					 // increment captured counters
						if ( sequence.get(i - 1).piece_ == GamePiece.WHITE ) {
							state.whiteCaptures_+= runCount;
						}
						if ( sequence.get(i - 1).piece_ == GamePiece.BLACK ) {
							state.blackCaptures_+= runCount;
						}
						switched = false;
						runCount = 1;
				} else {
				// do nothing
				}
			
			last = sequence.get(i); // record last gamePiece
		}
		if ( state.whiteCaptures_ >= captureCountToWin_ ) {
			return GamePiece.BLACK;
		}
		if ( state.blackCaptures_ >= captureCountToWin_ ) {
			return GamePiece.WHITE;
		}
		return GamePiece.NONE;
	}
}
