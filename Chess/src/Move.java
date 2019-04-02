import java.awt.Point;
//class that acts as a move storage (only used for AI)
//actual move storage for undo is much more involved
public class Move {
	public Point initialPoint;
	public Point finalPoint;
	public Move(Point i, Point f) {
		initialPoint = i;
		finalPoint = f;
	}
}
