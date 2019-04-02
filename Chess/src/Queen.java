import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Queen extends PieceClass implements Piece{

	public Queen(PieceCol c, Point p) {
		super("Queen", c, p);
	}
	public int getScore(){
		return 9;
	}
	@Override
	public Set<Point> Move(Piece[][] map) {
		Set<Point> y = super.movehelp(map, true);
		y.addAll(super.movehelp(map, false));
		return y;
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhiteQueen.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackQueen.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/QueenGlow.png"));
	}
}
