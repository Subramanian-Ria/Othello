import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Set;

//Interface that all pieces implement
public interface Piece {
	
	public String GetType();
	//returns a string with the Piece type, e.g. Pawn, Rook, etc. Used for specific exception messages.
	//class checking is mainly accomplished through the .class method
	
	public Point GetPos();
	//Returns the current position of the piece (this is stored in the array as well but in order to prevent
	//constant checking of the array on calling a piece move, is also stored in the piece)
	
	public PieceCol GetColor();
	//Returns the color of a piece
	
	public Set<Point> Move(Piece[][] map);
	//Returns all possible movement values for a piece. Overriten by each piece. Takes in the board state to
	//filter out invalid moves. NOTE - moves that put the king in check are filtered out in Move for the king
	//but discoverd checks are determined in board state. Also implements most of enpassant/castling
	//NOTE - does not move the piece, just returns possible moves
		
	public void UpdatePos(Point p);
	//Updates the piece's position value - this is called when the piece is moved. Adjusts certain private
	//values depending on context, e.g. Pawn's hasmoved boolean that checks if the pawn has already moved
	public int getScore();
	//Returns piece Value
	
	public Image getImage() throws IOException;
	
	public Image getGlowImage() throws IOException;
}

