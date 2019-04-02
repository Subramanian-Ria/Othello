import java.awt.Point;
import java.util.Set;
import java.util.HashSet;
//class that pieces extend for shared methods
public class PieceClass {
	private PieceCol pieceCol;
	private Point pos;
	private String name;
	
	public PieceClass(String n, PieceCol c, Point p) {
		pieceCol = c;
		name = n;
		pos = p;		
	}
	
	public String GetType() {
		return name;
	}
	
	public Point GetPos() {
		return pos;
	}
	
	public PieceCol GetColor() {
		return pieceCol;
	}
	
	public void UpdatePos (Point p) {
		pos = p;
	}
	
	@Override
	public String toString() {
		String n=null;
		switch(pieceCol) {
		case WHITE:
			n="W";
			break;
		case BLACK:
			n="B";
			break;
		}
		return n + this.GetType();
	}
	private Point moveuphelper(Point p) {
		Point q = new Point (p.x,p.y+1);
		return q;
	}
	private Point movedownhelper(Point p) {
		Point q = new Point (p.x,p.y-1);
		return q;
	}
	private Point movelefthelper(Point p) {
		Point q = new Point (p.x-1,p.y);
		return q;
	}
	private Point moverighthelper(Point p) {
		Point q = new Point (p.x+1,p.y);
		return q;
	}
	private Point funarrayemulatorrook(int i,Point p) {
		Point m = null;
		if (i==0) {
			m= moveuphelper(p);
		}
		if (i==1) {
			m= movedownhelper(p);
		}
		if (i==2) {
			m= movelefthelper(p);
		}
		if (i==3) {
			m= moverighthelper(p);
		}
		return m;
	}
	private Point funarrayemulatorbishop(int i,Point p) {
		Point m = null;
		if (i==0) {
			m= movelefthelper(moveuphelper(p));
		}
		if (i==1) {
			m= moverighthelper(moveuphelper(p));
		}
		if (i==2) {
			m= moverighthelper(movedownhelper(p));
		}
		if (i==3) {
			m= movelefthelper(movedownhelper(p));
		}
		return m;
	}
	
	public Set<Point> movehelp (Piece[][] map, Boolean isrookmove) {
		//movehelp implements diagonal and rook move (queens, rooks, and bishops have the same move rules)
		//isrookmove defaults to bishop move if false
		Boolean canmoveone = true;
		Boolean canmovetwo = true;
		Boolean canmovethree = true;
		Boolean canmovefour = true;
		Set<Point> y = new HashSet<Point>();
		//move booleans are in order of (up, down, left, right for rook movement and
		// NW, NE, SE, SW for bishop movement), not specified to reduce redundant code.
		Boolean[] bools = {canmoveone,canmovetwo,canmovethree,canmovefour};
		for (int i=0; i<4; i++) {
			Point move = (Point) pos.clone();
			while (bools[i]) {
				if (isrookmove) {
					move = funarrayemulatorrook(i,move);
				}
				else {
					move = funarrayemulatorbishop(i,move);
				}
				if (!(move.x<0||move.x>7||move.y<0||move.y>7)) {
					if (map[move.x][move.y]==null) {
						y.add(move);
					}
					else {
						if (map[move.x][move.y].GetColor()==pieceCol) {
							bools[i]=false;
						}
						else {
							y.add(move);
							bools[i]=false;
						}
					}
				}
				else {
					bools[i]=false;
				}
			}
		}
		return y;
	}
}
