
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;
//IGNORE - SPECIFIC AND FORMAL WERE NOT WRITTEN, ONLY USED DURING IMPLEMENTATION
public class tests {
	
	
	@Test
	
	public void movetwopawn() throws Board.NoPieceException, 
										Board.WrongColorException, 
										Board.InvalidMoveException, 
										Board.CheckmateException, Board.PawnTransformException {
		Board newboard = new Board(false);
		Point whitePawnPos = new Point(4,1);
		System.out.println(newboard.moveFilter(newboard.getPiece(whitePawnPos)));
		System.out.println(newboard.getPiece(whitePawnPos).GetType());
		newboard.updateboardstate(whitePawnPos, new Point (4,3));
		whitePawnPos = new Point(4,3);
		System.out.println(newboard.moveFilter(newboard.getPiece(whitePawnPos)));
		System.out.println(newboard.getPiece(whitePawnPos).GetType());
		newboard.toggleturn();
		
		Point blackPawnPos = new Point(3,6);
		System.out.println(newboard.getPiece(blackPawnPos).GetType());
		System.out.println(newboard.moveFilter(newboard.getPiece(blackPawnPos)));
		System.out.println(newboard.getPiece(blackPawnPos).GetType());
		newboard.updateboardstate(blackPawnPos, new Point (3,4));
		blackPawnPos = new Point(3,4);
		System.out.println(newboard.moveFilter(newboard.getPiece(blackPawnPos)));
		System.out.println(newboard.getPiece(blackPawnPos).GetType());
		newboard.toggleturn();
		
		//System.out.println(newboard.moveFilter(newboard.getPiece(whitePawnPos)));
		//System.out.println(newboard.getPiece(whitePawnPos).GetType());
		newboard.updateboardstate(whitePawnPos, new Point (3,4));
		System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.updateboardstate(new Point (4,6), new Point (4,4));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.updateboardstate(new Point (3,4), new Point (4,5));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.updateboardstate(new Point (2,7), new Point (4,5));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.toggleturn();
		newboard.updateboardstate(new Point (3,7), new Point (3,1));
		System.out.println("hasmoved");
		newboard.toggleturn();
		newboard.toggleturn();
		newboard.updateboardstate(new Point (1,7), new Point (2,5));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.toggleturn();
		//System.out.println(newboard.moveFilter(newboard.getPiece(new Point (4,7))));
		newboard.updateboardstate(new Point (4,7), new Point (2,7));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.toggleturn();
		newboard.updateboardstate(new Point (3,7), new Point (3,4));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.toggleturn();
		newboard.updateboardstate(new Point (3,4), new Point (4,4));
		//System.out.println(newboard.getBoard());
		newboard.toggleturn();
		newboard.toggleturn();
		newboard.updateboardstate(new Point (3,1), new Point (1,3));
		System.out.println("\n"+newboard.getBoard());
		try {
			newboard.toggleturn();
		}
		catch (Board.CheckmateException e) {
			System.out.println("dfghj");
		}

	}
}
