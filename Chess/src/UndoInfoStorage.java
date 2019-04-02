import java.awt.Point;
import java.util.Set;
//Stores info about a move for the undo function
public class UndoInfoStorage {
	public Piece move;
	public Piece taken;
	public Boolean toggleHasMoved;
	public Boolean toggleTakeHasMoved;
	public Point enpassantPos;
	public Point initialPoint;
	public Point finalPoint;
	public Point RookPos;
	public Point RooktoMovePos;
	public Set<Piece> enpassantPawns;
	public int ScoreChange;
	public UndoInfoStorage(Piece mov, Piece tak, Boolean tog, Boolean togtak, Point enpa, Point ip, Point fp, Point c, Point d, Set<Piece> ep,int scoreadd) {
		move=mov;
		taken = tak;
		toggleHasMoved = tog;
		toggleTakeHasMoved = togtak;
		enpassantPos = enpa;
		initialPoint=ip;
		finalPoint=fp;
		RookPos=c;
		RooktoMovePos = d;
		enpassantPawns = ep;
		ScoreChange = scoreadd;
	}
}
