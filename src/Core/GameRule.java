package Core;
import java.util.List;

/**
 *  Interface for GamRule classes
 */
public interface GameRule {

	public boolean isLegalMove ( GameState state, Move move );

	public GamePiece evalWinner ( List<Move> sequence, GameState state );

	public GamePiece evalRow ( List<Move> sequence, GameState state );

	public GamePiece evalCapture ( List<Move> sequence, GameState state );

	public int getBoardSize ();

}
