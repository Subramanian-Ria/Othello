import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import java.util.HashSet;

public class Pawn extends PieceClass implements Piece{
	private boolean hasmoved;
	private boolean enpassant;
	
	public Pawn (PieceCol c, Point p) {
		super("Pawn",c,p);
		hasmoved = false;
		enpassant = false;
	}
	public int getScore(){
		int toadd=0;
		switch (this.GetColor()) {
			case BLACK:
			toadd = Math.max((int) (7-this.GetPos().y)-4, 1);
			break;
		case WHITE:
			toadd = (int) Math.max((int) (this.GetPos().y)-4, 1);
			break;
		}
		return 1+ toadd;
	}
	public boolean gethasmoved() {
		return hasmoved;
	}
	public void toggleHasMoved() {
		hasmoved=false;
	}
	public Boolean isEnpassant() {
		return enpassant;
	}
	public void ToggleEnpassant() {
		enpassant = false;
	}
	public void ToggleTrueEnpassant() {
		enpassant = true;
	}
	
	public Set<Point> Threaten(Piece[][] map) {
		int zoffset = 0;
		switch(this.GetColor()) {
		case WHITE:
			zoffset=1;
			break;
		case BLACK:
			zoffset=-1;
			break;
		}
		Point p = this.GetPos();
		Point movediagright =  new Point(p.x+1,p.y+zoffset);
		Point movediagleft =  new Point(p.x-1,p.y+zoffset);
		Point[] x = new Point[] {movediagright,movediagleft};
		Set<Point> y = new HashSet<Point>();
		for (int i=0; i<x.length;i++) {
			Point totest = x[i];
			if (!(totest.x<0||totest.x>7||totest.y<0||totest.y>7)) {
				if (map[totest.x][totest.y]!=null&&map[totest.x][totest.y].GetColor()!=this.GetColor()) {
					y.add(totest);
				}
				else {
					Piece q = map[totest.x][totest.y-zoffset];
					if (q!=null && map[totest.x][totest.y-zoffset].getClass()==Pawn.class) {
						//enpassant conditions (checks all conditions and adds the move)
						Pawn z = (Pawn) map[totest.x][totest.y-zoffset];
						if (p.y==(7+zoffset)/2 && z!=null && 
							z.GetType().equals("Pawn") && 
							!(z.GetColor()==this.GetColor()) &&
							z.isEnpassant()) {
							y.add(totest);
						}
					}
				}
			}
		}
		return y;
	}
	
	@Override
	public Set<Point> Move(Piece[][] map) {
		int zoffset = 0;
		switch(this.GetColor()) {
		case WHITE:
			zoffset=1;
			break;
		case BLACK:
			zoffset=-1;
			break;
		}
		Point p = this.GetPos();
		Point moveup = new Point(p.x,p.y+zoffset);
		Point movetwoup = new Point(p.x,p.y+(2*zoffset));
		Set<Point> y = new HashSet<Point>();
		if (!hasmoved && !(movetwoup.x<0||movetwoup.x>7||movetwoup.y<0||movetwoup.y>7)) {
			if (map[p.x][p.y+zoffset]==null&&map[p.x][p.y+(2*zoffset)]==null)
			y.add(movetwoup);
		}
		if (!(moveup.x<0||moveup.x>7||moveup.y<0||moveup.y>7)) {
			if (map[p.x][p.y+zoffset]==null)
			y.add(moveup);
		}
		if (this.Threaten(map)!=null) {
			y.addAll(this.Threaten(map));
		}
		return y;
	}

	@Override
	public void UpdatePos(Point toupdate) {
		int zoffset = 0;
		switch(this.GetColor()) {
		case WHITE:
			zoffset=1;
			break;
		case BLACK:
			zoffset=-1;
			break;
		}
		Point p = this.GetPos();
		if (!hasmoved) {
			hasmoved = true;
		}
		if (p.x==toupdate.x && p.y==toupdate.y-(2*zoffset)) {
			enpassant = true;
		}
		super.UpdatePos(toupdate);
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhitePawn.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackPawn.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/PawnGlow.png"));
	}
	
}
