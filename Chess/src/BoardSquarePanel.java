
import javax.swing.JPanel;

//Basically just there for storing the x and y position of the Panel. Jpanel otherwise
//Any other changes are done through override in the construction of each BoardSquarePanel
public class BoardSquarePanel extends JPanel{
	private int x;
	private int y;
	public BoardSquarePanel(int i, int j) {
		x=i;
		y=j;
		}
	public int getXPos() {
		return x;
	}
	public int getYPos() {
		return y;
	}
}
