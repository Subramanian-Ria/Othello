import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import java.util.HashSet;

public class King extends PieceClass implements Piece{
	private boolean hasmoved;
	public boolean hascastled; //only used for AI functions
	public King(PieceCol c, Point p) {
		super("King", c, p);
		hasmoved = false;
		hascastled=false;
	}
	public int getScore(){
		return 10000000;
	}
	
	public boolean check (Piece[][] map, Point position) {
		Set<Point> possibles = new HashSet<Point>();
		for (int i = 0; i <8 ; i++) {
			for (int j = 0; j<8; j++) {
				Piece totest = map[i][j];
				if (totest !=null) {
					if (totest.GetColor()!=this.GetColor()) {
						if (totest.getClass()==Pawn.class) {
							Pawn totestpawn = (Pawn) totest;
							possibles.addAll(totestpawn.Threaten(map));
						}
						else if (totest.getClass()==King.class) {
							King totestpawn = (King) totest;
							possibles.addAll(totestpawn.Threaten(map));

						}
						else {
							possibles.addAll(totest.Move(map));
						}
					}
				}
			}
		}
		return possibles.contains(position);
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
	private Point funarrayemulator(int i,Point p) {
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
		if (i==4) {
			m= movelefthelper(moveuphelper(p));
		}
		if (i==5) {
			m= moverighthelper(moveuphelper(p));
		}
		if (i==6) {
			m= moverighthelper(movedownhelper(p));
		}
		if (i==7) {
			m= movelefthelper(movedownhelper(p));
		}
		return m;
	}

	
	public Set<Point> Threaten(Piece[][] map) {
		Set<Point> y = new HashSet<Point>();
		for (int i=0;i<8;i++) {
			Point p = funarrayemulator(i,this.GetPos());
			if (!(p.x<0||p.x>7||p.y<0||p.y>7)) {
				Piece totest = map[p.x][p.y];
				if (totest==null) {
					y.add(p);
				}
				else {
					if (!(totest.GetColor()==this.GetColor())) {
						y.add(p);
					}
				}
			}
		}
		return y;
	}
	@Override
	public Set<Point> Move(Piece[][] map) {
		Set<Point> y = new HashSet<Point>();
		if (!this.Threaten(map).isEmpty()) {
			y.addAll(this.Threaten(map));
		}		
		//the following if statement implements castling (a king move) on both sides. Will throw array out of bounds if the chessboard
		//is sized incorrectly
		if (!hasmoved) {
			Point kingPos = this.GetPos();
			Piece kingrooktest = map[kingPos.x+3][kingPos.y];
			if (kingrooktest!=null && kingrooktest.getClass()==Rook.class) {
				Rook kingrook = (Rook)kingrooktest;
				Piece oneindentpiece = map[kingPos.x+1][kingPos.y];
				Piece twoindentpiece = map[kingPos.x+2][kingPos.y];
				Point oneindent = new Point(kingPos.x+1,kingPos.y);
				Point twoindent = new Point(kingPos.x+2,kingPos.y);
				if (!kingrook.gethasmoved()&&
					oneindentpiece==null&&
					twoindentpiece==null&&
					!this.check(map, oneindent)&&
					!this.check(map, twoindent)) {
					y.add(twoindent);
				}
			}
			Piece queenrooktest = map[kingPos.x-4][kingPos.y];
			if (queenrooktest!=null && queenrooktest.getClass()==Rook.class) {
				Rook queenrook = (Rook)queenrooktest;
				Piece oneindentqueenPiece = map[kingPos.x-1][kingPos.y];
				Piece twoindentqueenPiece = map[kingPos.x-2][kingPos.y];
				Piece threeindentqueenPiece = map[kingPos.x-3][kingPos.y];
				Point oneindentqueen = new Point(kingPos.x-1,kingPos.y);
				Point twoindentqueen = new Point(kingPos.x-2,kingPos.y);
				if (!queenrook.gethasmoved()&&
					oneindentqueenPiece==null&&
					twoindentqueenPiece==null&&
					threeindentqueenPiece==null&&
					!this.check(map, oneindentqueen)&&
					!this.check(map, twoindentqueen)) 
				{
					y.add(twoindentqueen);
				}
			}
		}
		return y;
	}
	
	@Override
	public void UpdatePos(Point toupdate) {
		if (!hasmoved) {
			hasmoved = true;
		}
		super.UpdatePos(toupdate);
	}
	public boolean gethasmoved() {
		return hasmoved;
	}	
	public void toggleHasMoved() {
		hasmoved=false;
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhiteKing.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackKing.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/KingGlow.png"));
	}

}
