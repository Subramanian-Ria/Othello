import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Bishop extends PieceClass implements Piece{

	public Bishop(PieceCol c, Point p) {
		super("Bishop", c, p);
	}
	public int getScore(){
		return 3;
	}
	@Override
	public Set<Point> Move(Piece[][] map) {
		return super.movehelp(map, false);
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhiteBishop.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackBishop.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/BishopGlow.png"));
	}
}
