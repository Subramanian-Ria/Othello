import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JFrame;
//main window, combines piecepanel, boardpanel and scorepanel
public class MainFrame extends JFrame{
	public BoardPanel b = new BoardPanel(true,true);
	public PiecePanel p = new PiecePanel();
	public MainFrame(String s) throws IOException {
		super(s);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,1000);
		OptionsPanel o = new OptionsPanel();
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill=GridBagConstraints.BOTH;
		
		//PiecePanel Layout
		gc.gridx=0;
		gc.gridy=0;
		gc.gridheight=2;
		gc.weightx=.2;
		gc.weighty=1;
		add(p,gc);
		
		//BoardLayout
		gc.gridx=1;
		gc.gridy=0;
		gc.gridheight=1;
		gc.weightx=.8;
		gc.weighty=.8;
		
		add(b,gc);
		
		//OptionsPanel
		gc.gridx=1;
		gc.gridy=1;
		gc.weighty=.2;
		add(o,gc);
	}
}
