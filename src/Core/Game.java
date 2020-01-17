package Core;

import java.io.Serializable;

/**				 Game class represents the running game, creates the
 *         appropriate rule based on the input GameVariant, and then retrieves
 *         the gameState reference from that rule This is the main entry point
 *         for a game, and the playerMove method is used to make moves. A
 *         GamePiece is returned representing which player (BLACK or WHITE) won
 *         the game, NONE if no winner from the last move, or VOID if the move
 *         was not legal
 */
public class Game {
	
	public GameState gameState_;
	protected GameRuleGeneric rules_;
	protected GameVariant gameVariant_;

	public Game ( GameVariant gameType ) {

		gameVariant_ = gameType;
		startGame();
	}

	public GamePiece playerMove ( Move move ) {
		
		
		gameState_.setlastMovePlaced(false);
		if ( rules_.isLegalMove(gameState_,move) ) {
			System.out.println("move is legal");
			gameState_.createMove(move);
			gameState_.setlastMovePlaced(true);
			gameState_.setWinner(rules_.eval());
			gameState_.firePCS();
			return gameState_.getWinner();
		}
		System.out.println("move is NOT legal");
		return GamePiece.NONE;
	}
	
	public void startGame() {
		rules_ = new GameRuleGeneric(gameVariant_.getRule());
		gameState_ = rules_.state_;
		gameState_.setGameVariant(gameVariant_);
	}
	
	public GameRuleGeneric getRules() {
		return this.rules_;
	}
	
}
