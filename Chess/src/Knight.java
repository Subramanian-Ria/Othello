import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import java.util.HashSet;

public class Knight extends PieceClass implements Piece{

	public Knight(PieceCol c, Point p) {
		super("Knight", c, p);
	}
	public int getScore(){
		return 3;
	}

	@Override
	public Set<Point> Move(Piece[][] map) {
		Set<Point> y = new HashSet<Point>();
		Point knightpos = this.GetPos();
		Point opone = new Point(knightpos.x+2,knightpos.y+1);
		Point optwo = new Point(knightpos.x-2,knightpos.y+1);
		Point opthree = new Point(knightpos.x+2,knightpos.y-1);
		Point opfour = new Point(knightpos.x-2,knightpos.y-1);
		Point opfive = new Point(knightpos.x+1,knightpos.y+2);
		Point opsix = new Point(knightpos.x-1,knightpos.y+2);
		Point opseven = new Point(knightpos.x+1,knightpos.y-2);
		Point opeight = new Point(knightpos.x-1,knightpos.y-2);
		Point[] options = {opone,optwo,opthree,opfour,opfive,opsix,opseven,opeight};
		for (int i=0; i <8; i++) {
			Point totest = options[i];
			if (!(totest.x<0||totest.x>7||totest.y<0||totest.y>7)) {
				if (map[totest.x][totest.y]==null) {
					y.add(totest);
				}
				else {
					if (!(map[totest.x][totest.y].GetColor()==this.GetColor())) {
						y.add(totest);
					}
				}
			}
		}
		return y;
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhiteKnight.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackKnight.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/KnightGlow.png"));
	}

}
