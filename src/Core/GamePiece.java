package Core;
import java.awt.Color;

/**
 * used to hold valid gamepieces
 */

public enum GamePiece { // BLACK,WHITE are actual players pieces, NONE is an
                        // empty space, VOIDS is returned for an illegal move.
	NONE(null), BLACK(Color.BLACK), WHITE(Color.WHITE), VOID(Color.GRAY);

	private Color color_;

	private GamePiece ( Color color ) {
		color_ = color;
	}

}
