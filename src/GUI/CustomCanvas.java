
package GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * This is the canvas that the board and all other features are drawn on. The
 * canvas gets updated with each different move.
 * 
 */
public class CustomCanvas extends Canvas {

	public CustomCanvas () {
		super(760,760);
		draw();
	}

	/**
	 * Draw method to draw the board and appropriate features on it
	 */
	public void draw () {
		int width = (int) this.getWidth() / 19;
		int height = (int) this.getHeight() / 19;
		this.getGraphicsContext2D().setStroke(Color.BLACK);
		this.getGraphicsContext2D().setLineWidth(2);

		for ( int i = 0 ; i < 19 ; i++ ) {
			for ( int j = 0 ; j < 19 ; j++ ) {
				this.getGraphicsContext2D().setFill(Color.LIGHTGREEN);
				if ( i > 6 && i < 12 && j > 6 && j < 12 ) {
					this.getGraphicsContext2D().setFill(Color.GREEN);
				}
				if ( i == 9 && j == 9 ) {
					this.getGraphicsContext2D().setFill(Color.DARKGRAY);
				}

				this.getGraphicsContext2D().fillRect(j * width,i * height,width,height);

			}
		}
		for ( int i = 0 ; i < 18 ; i++ ) {
			for ( int j = 0 ; j < 18 ; j++ ) {

				this.getGraphicsContext2D().strokeRect(j * width + width / 2,
				                                       i * height + height / 2,width,
				                                       height);
			}
		}

		this.getGraphicsContext2D().strokeRect(0,0,this.getWidth(),
		                                       this.getHeight());
	}

}
