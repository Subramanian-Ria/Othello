import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Rook extends PieceClass implements Piece{
	private boolean hasmoved;
	public boolean gethasmoved() {
		return hasmoved;
	}
	public int getScore(){
		return 5;
	}
	public void toggleHasMoved() {
		hasmoved=false;
	}
	public Rook(PieceCol c, Point p) {
		super("Rook", c, p);
		hasmoved = false;
	}
	@Override
	public Set<Point> Move(Piece[][] map) {
		return super.movehelp(map, true);
	}
	@Override
	public void UpdatePos(Point toupdate) {
		if (!hasmoved) {
			hasmoved = true;
		}
		super.UpdatePos(toupdate);
	}
	@Override
	public Image getImage() throws IOException {
		Image i = null;
		switch (this.GetColor()) {
		case WHITE:
			i = ImageIO.read(new File("Files/WhiteRook.png"));
			break;
		case BLACK:
			i = ImageIO.read(new File("Files/BlackRook.png"));
			break;
		}
		return i;
	}
	@Override
	public Image getGlowImage() throws IOException {
		return ImageIO.read(new File("Files/RookGlow.png"));
	}
}
