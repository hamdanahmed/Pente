package Core;

/**
 *  simulates a game
 */
public class GameSimulator {

	public static Game game_;

	public static void main ( String[] args ) {
		Move[] moves; 
		GamePiece winner = GamePiece.NONE; // initial piece
		if (true) {	// test for 5 in a row.
			moves = new Move[20];
			moves[0] = new Move(GamePiece.BLACK,9,9);
			moves[1] = new Move(GamePiece.WHITE,10,10);
			moves[2] = new Move(GamePiece.BLACK,6,6);
			moves[3] = new Move(GamePiece.WHITE,7,10);
			moves[4] = new Move(GamePiece.BLACK,7,9);
			moves[5] = new Move(GamePiece.WHITE,8,9);
			moves[6] = new Move(GamePiece.BLACK,5,9);
			moves[7] = new Move(GamePiece.WHITE,4,9);
			moves[8] = new Move(GamePiece.BLACK,9,8);
			moves[9] = new Move(GamePiece.WHITE,6,11);
			moves[10] = new Move(GamePiece.BLACK,5,12);
			moves[11] = new Move(GamePiece.WHITE,9,10);
			moves[12] = new Move(GamePiece.BLACK,5,11);
			moves[13] = new Move(GamePiece.WHITE,5,10);
			moves[14] = new Move(GamePiece.BLACK,4,12);
			moves[15] = new Move(GamePiece.WHITE,6,10);
			moves[16] = new Move(GamePiece.BLACK,5,13);
			moves[17] = new Move(GamePiece.WHITE,8,10); // White should win here.
			moves[18] = new Move(GamePiece.BLACK,8,8);
			moves[19] = new Move(GamePiece.WHITE,7,7);
		}
		
		if (true) {		// test for captures 
			moves = new Move[32];
			moves[0] = new Move(GamePiece.BLACK,9,9);
			moves[1] = new Move(GamePiece.WHITE,8,9);
			moves[2] = new Move(GamePiece.BLACK,1,1);
			moves[3] = new Move(GamePiece.WHITE,7,9);
			moves[4] = new Move(GamePiece.BLACK,6,9);
			moves[5] = new Move(GamePiece.WHITE,7,9);
			moves[6] = new Move(GamePiece.BLACK,8,9);
			moves[7] = new Move(GamePiece.WHITE,10,9);
			moves[8] = new Move(GamePiece.BLACK,5,9);
			moves[9] = new Move(GamePiece.WHITE,4,9);
			moves[10] = new Move(GamePiece.BLACK,2,1);
			moves[11] = new Move(GamePiece.WHITE,3,1);
			moves[12] = new Move(GamePiece.BLACK,2,2);
			moves[13] = new Move(GamePiece.WHITE,0,1);
			moves[14] = new Move(GamePiece.BLACK,3,9);
			moves[15] = new Move(GamePiece.WHITE,3,0);
			moves[16] = new Move(GamePiece.BLACK,1,3);
			moves[17] = new Move(GamePiece.WHITE,0,4);
			moves[18] = new Move(GamePiece.BLACK,0,3);
			moves[19] = new Move(GamePiece.WHITE,2,0);
			moves[20] = new Move(GamePiece.BLACK,1,0);
			moves[21] = new Move(GamePiece.WHITE,4,0);
			moves[22] = new Move(GamePiece.BLACK,5,0);
			moves[23] = new Move(GamePiece.WHITE,6,1);
			moves[24] = new Move(GamePiece.BLACK,3,2);
			moves[25] = new Move(GamePiece.WHITE,1,3);
			moves[26] = new Move(GamePiece.BLACK,2,2);
			moves[27] = new Move(GamePiece.WHITE,1,2);
			moves[28] = new Move(GamePiece.BLACK,1,4);
			moves[29] = new Move(GamePiece.WHITE,1,1);
			moves[30] = new Move(GamePiece.BLACK,2,1);
			moves[31] = new Move(GamePiece.WHITE,4,2);
		}
		



		// play games

		for ( GameVariant g : GameVariant.getGameVariants() ) { // cycle through all
		                                                        // games
			game_ = new Game(g);
			System.out.println("Playing game " + g);
			for ( int i = 0 ; i < moves.length ; i++ ) {
				winner = game_.playerMove(moves[i]);
				if ( GamePiece.NONE != winner ) {
					System.out.println(winner + " wins in " + i + " moves\n");
				
					break;
				}
			}
			if ( GamePiece.NONE == winner ) {
				System.out.println(winner + " winner");
			}
		}

	}
}
