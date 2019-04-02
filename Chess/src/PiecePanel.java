import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
//ScorePanel and title
public class PiecePanel extends JPanel{
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200,1000);
	}
	public void setscore(boolean b) {
		if (b) {
			BS.setText(Game.app.b.getScore(b));
		}
		else {
			WS.setText(Game.app.b.getScore(b));
		}
	}
	public JLabel BS = new JLabel("0");
	private JLabel WS = new JLabel("0");
	public PiecePanel() {
		super();
		BS.setFont(new Font("MyFont",Font.BOLD,50));
		BS.setForeground(new Color(102,51,0));
		WS.setFont(new Font("MyFont",Font.BOLD,50));
		WS.setForeground(new Color(102,51,0));
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx=1;
		gc.weighty=1;
		gc.gridheight=2;
		gc.gridx=0;
		gc.gridy=0;
		this.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 0, new Color(102,51,0)));
		this.setBackground(new Color(255,255,183));
		try {
			JLabel ChessTitle = new JLabel(new ImageIcon(ImageIO.read(new File("Files/Chess.png"))));
			JLabel BlackScore = new JLabel(new ImageIcon(ImageIO.read(new File("Files/Black Score_.png")).getScaledInstance(180, -1, Image.SCALE_DEFAULT)));
			JLabel WhiteScore = new JLabel(new ImageIcon(ImageIO.read(new File("Files/White Score_.png")).getScaledInstance(180, -1, Image.SCALE_DEFAULT)));
			add(ChessTitle,gc);
			gc.gridy=2;
			add(BlackScore,gc);
			gc.gridheight=1;
			gc.gridy=4;
			add(BS,gc);
			gc.gridheight=2;
			gc.gridy=6;
			add(WhiteScore,gc);
			gc.gridheight=1;
			gc.gridy=8;
			add(WS,gc);
			gc.gridheight=1;

			
		} catch (IOException e) {
		}
	}
}
