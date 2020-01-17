package Core;

import java.util.Arrays;

/**
 * 
 */
public class GameAI {
	GameState currentState_;
	GameBoard OriginalBoard_;
	GamePiece[] BoardSeq_;
	Game currentGame_;
	GameVariant gameType_;
	GameRuleGeneric rules_;
	GameRule current_rules;
	Game game_;
	GameRuleGeneric rules;
	GameState state;

	/**
	 * 
	 */
	public GameAI ( GameState state ) {
		// TODO Auto-generated constructor stub
		this.currentState_ = state;
		this.OriginalBoard_ = currentState_.getBoard();
		this.BoardSeq_ = OriginalBoard_.getBoard();
		this.gameType_ = currentState_.getGameVariant();
		rules_ = new GameRuleGeneric(gameType_.getRule());
		current_rules = gameType_.getRule();
	}

	public Move MakeMove () {

		Move move = null;
		game_ = new Game(gameType_);
		// re-creating the orginal board for this game object
		for ( int row = 0 ; row < 19 ; row++ ) {
			for ( int col = 0 ; col < 19 ; col++ ) {
				Move currentMove =
				    new Move(OriginalBoard_.getBoardCell(row,col),row,col);
				game_.playerMove(currentMove);
			}
		}
		rules = game_.getRules();
		state = rules.getGameState();

		// -----------------------------------------
		int[][] moveScores = new int[19][19];
		for ( int row = 0 ; row < 19 ; row++ ) {
			for ( int col = 0 ; col < 19 ; col++ ) {
				moveScores[row][col] = 0;
			}
		}
		int rowforMove = 0, colforMove = 0;
		Move lastMove = state.getLastMove();
		int[][] scr = getScores(OriginalBoard_,lastMove);
		int max = 0;
		for ( int i = 0 ; i < 19 ; i++ ) {
			for ( int j = 0 ; j < 19 ; j++ ) {
				if ( scr[i][j] > max ) {
					max = scr[i][j];
					rowforMove = i;
					colforMove = j;
				}
			}
		}
		if ( max != 0 ) {
			System.out.println("MAX:-----------"+max);
			move = new Move(GamePiece.WHITE,rowforMove,colforMove);
			return move;
		} else {
			return null;
		}
	}

	private int[][] getScores ( GameBoard board, Move currentMove ) {
		int currentRow = currentMove.moveRow_;
		int currentCol = currentMove.moveCol_;
		Move[] nearbyMoves = new Move[8];
		int scores[][] = new int[19][19];
		int[][] dir = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 },
		                { 1, 0 }, { 1, -1 }, { 0, -1 }

		};

		for ( int i = 0 ; i < 8 ; i++ ) {
			if ( currentRow + dir[i][0] >= 0 && currentRow + dir[i][0] < 19
			    && currentCol + dir[i][1] >= 0 && currentCol + dir[i][1] < 19 ) {
				Move current = new Move(
				                        board.getBoardCell(currentRow + dir[i][0],
				                                           currentCol + dir[i][1]),
				                        currentRow + dir[i][0],currentCol + dir[i][1]);
				nearbyMoves[i] = current;
			} else {
				nearbyMoves[i] = null;
			}
		}

		for ( int i = 0 ; i < 8 ; i++ ) {
			if ( nearbyMoves[i] != null ) {
				if ( board.getBoardCell(nearbyMoves[i].moveRow_,
				                        nearbyMoves[i].moveCol_) == GamePiece.NONE ) {
					Move move = new Move(GamePiece.WHITE,nearbyMoves[i].moveRow_,
					                     nearbyMoves[i].moveCol_);
					game_.playerMove(move);
					// eval the piece
					GamePiece winner = rules.eval();
					// remove the added piece
					game_.gameState_.gameBoard_.setBoardCell(GamePiece.NONE,
					                                         nearbyMoves[i].moveRow_,
					                                         nearbyMoves[i].moveCol_);
					if ( winner == GamePiece.WHITE ) {
						scores[nearbyMoves[i].moveRow_][nearbyMoves[i].moveCol_] = 1000;
					} else {
						int points = (int)(Math.random()*5+1);
						scores[nearbyMoves[i].moveRow_][nearbyMoves[i].moveCol_] += points;

					}
				} else {
					scores[nearbyMoves[i].moveRow_][nearbyMoves[i].moveCol_] = 0;
				}
			}
		}
		return scores;
	}

}
