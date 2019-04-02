
import java.io.IOException;
import javax.swing.SwingUtilities;
//Runs the game, app is a variable for JDialogs/to allow other classes to reference the window
public class Game {
	public static MainFrame app;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					app = new MainFrame("Chess");
				} catch (IOException e) {
				}
			}
		});
	}
}

