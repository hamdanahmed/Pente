package Core;

import java.util.List;

/**
 *  GameRule for the Pente Freestyle game variant
 */
public class GameRuleGomokuCaro implements GameRule {

	protected final int BoardSize_ = 19;
	protected final boolean firstMoveCenter_ = false;
	protected final GamePiece firstMoveGamePiece_ = GamePiece.BLACK;
	// protected final boolean nextMoveRestricted_ = false;
	// protected final int nextMoveRange_ = 0;
	protected final boolean canCapture_ = false;
	protected final boolean capturesWin_ = false;
	protected final int captureSizeMin_ = 2;
	protected final int captureSizeMax_ = 2;
	protected final int captureCountToWin_ = 5;
	protected final int captureTotalCountToWin_ = 0;
	protected final boolean winExactly_ = false;
	protected final int winCount_ = 5;
	// protected final boolean winUnblocked_ = false;
	// protected final boolean threeXthree_ = false;
	protected int blackCaptures_;
	protected int whiteCaptures_;

	public GameRuleGomokuCaro () {

	}

	/**
	 * Determines if the current move is legal for this game variant
	 */
	public boolean isLegalMove ( GameState state, Move move ) {

		int center = state.getBoard().getCenter();
		// game specific illegal moves

		if ( firstMoveGamePiece_ != move.piece_ && state.isFirstMove() ) {
			return false; // first move must be specified GamePiece
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
		boolean gotWinCount = false;
		GamePiece winPiece = GamePiece.NONE;
		boolean startOpen = false;	// tracks if a run starts with an Open (GamePiece.NONE precedes it)
		
		// iterate through the sequence
		for ( int i = 0 ; i < sequence.size() ; i++ ) {
			if ( sequence.get(i).piece_ == GamePiece.NONE ) { // reset tracker
				runCount = 0;
				if(startOpen && gotWinCount) {
					return winPiece; // winCount_ gamePieces in a row, and run started and finished open,  return winner
					
				}
				startOpen = true;
				
			} else if ( runCount == 0 ) { // track first gamePiece
				runCount++;
				
			} else if ( sequence.get(i).piece_.equals(last.piece_)
			    && last.piece_ != GamePiece.NONE ) { // another same gamepiece found
					runCount++;
					
			} else if ( sequence.get(i).piece_ != last.piece_
			    && sequence.get(i).piece_ != GamePiece.NONE ) {
				// sequential different gamepiece found
					runCount = 1;
					startOpen = false;
					gotWinCount = false;
					
			} else {
				// do nothing
			}
			if ( runCount == winCount_ && startOpen) {
				gotWinCount = true;
				winPiece = last.piece_;
				
			}
			if ( runCount > winCount_ ) {
				return sequence.get(i).piece_; // > winCount_ gamePieces in a row, return winner
				
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
		return GamePiece.NONE;
		
	}
}
