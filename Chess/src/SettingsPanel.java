import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
//Panel that shows up when settings is selected from options
public class SettingsPanel extends JDialog{
	public SettingsPanel(Boolean r, Boolean pr, Boolean intel, PieceCol c) {
		super(Game.app);
		SettingsPanel todisp = this;
		this.setModal(true);
		JPanel setpan = new JPanel();
		this.setSize(300, 500);
		setpan.setBorder(BorderFactory.createTitledBorder
				(BorderFactory.createLineBorder(new Color(102,51,0), 10),"Settings"));
		setpan.setBackground(new Color(255,255,183));
		setpan.setSize(300, 500);
		JLabel Rot = new JLabel("Rotation?");
		JLabel Prev = new JLabel ("Move Preview?");
		JLabel AI = new JLabel ("AI?");
		JButton Y1 = new JButton("Yes");
		JButton N1 = new JButton("No");
		JButton Y2 = new JButton("Yes");
		JButton N2 = new JButton("No");
		JButton Ok = new JButton("OK");
		JButton Off = new JButton("Off");
		JButton AIBlack = new JButton("Black");
		JButton AIWhite = new JButton("White");
		Off.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.app.b.AI=false;
				Off.setEnabled(false);
				AIWhite.setEnabled(true);
				AIBlack.setEnabled(true);
			}
		});
		AIBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.app.b.AI=true;
				Game.app.b.AICol=PieceCol.BLACK;
				if (Game.app.b.Orr) {
					Game.app.b.AIMove();
				}
				Off.setEnabled(true);
				AIWhite.setEnabled(true);
				AIBlack.setEnabled(false);
			}
		});
		AIWhite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.app.b.AI=true;
				Game.app.b.AICol=PieceCol.WHITE;
				if (!Game.app.b.Orr) {
					Game.app.b.AIMove();
				}
				Off.setEnabled(true);
				AIWhite.setEnabled(false);
				AIBlack.setEnabled(true);
			}
		});
		if (!intel) {
			Off.setEnabled(false);
			AIWhite.setEnabled(true);
			AIBlack.setEnabled(true);
		}
		else {
			if (c==PieceCol.WHITE) {
				Off.setEnabled(true);
				AIWhite.setEnabled(false);
				AIBlack.setEnabled(true);
			}
			else {
				Off.setEnabled(true);
				AIWhite.setEnabled(true);
				AIBlack.setEnabled(false);
			}
		}
		Ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				todisp.dispose();
			}
		});
		if (r) {
			Y1.setEnabled(false);
		}
		else {
			N1.setEnabled(false);
		}
		if (pr) {
			Y2.setEnabled(false);
		}
		else {
			N2.setEnabled(false);
		}
		Y1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Game.app.b.AI&&Game.app.b.AICol==PieceCol.WHITE) {
					Game.app.b.switchModval();
				}
				Game.app.b.toRotate=true;
				Game.app.b.repaintsquares();
				N1.setEnabled(true);
				Y1.setEnabled(false);
			}
		});
		N1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Game.app.b.AI&&Game.app.b.AICol==PieceCol.WHITE) {
					Game.app.b.switchModval();
				}
				Game.app.b.toRotate=false;
				Game.app.b.repaintsquares();
				N1.setEnabled(false);
				Y1.setEnabled(true);
			}
		});
		Y2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Game.app.b.toPreview=true;
				Game.app.b.repaintsquares();
				N2.setEnabled(true);
				Y2.setEnabled(false);
			}
		});
		N2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Game.app.b.toPreview=false;
				Game.app.b.repaintsquares();
				N2.setEnabled(false);
				Y2.setEnabled(true);
			}
		});
		setpan.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridwidth=3;
		gc.gridx=0;
		gc.gridy=0;
		setpan.add(Rot,gc);
		gc.gridwidth=1;
		gc.gridy=1;
		setpan.add(Y1,gc);
		gc.gridx=1;
		setpan.add(N1,gc);
		gc.gridwidth=3;
		gc.gridx=0;
		gc.gridy=2;
		setpan.add(Prev,gc);
		gc.gridwidth=1;
		gc.gridy=3;
		setpan.add(Y2,gc);
		gc.gridx=1;
		setpan.add(N2,gc);
		gc.gridx=0;
		gc.gridwidth=3;
		gc.gridy=5;
		setpan.add(AI,gc);
		gc.gridwidth=1;
		gc.gridy=6;
		setpan.add(Off,gc);
		gc.gridx=1;
		setpan.add(AIWhite,gc);
		gc.gridx=2;
		setpan.add(AIBlack,gc);
		gc.gridy=7;
		gc.gridwidth=3;
		gc.gridx=0;
		gc.insets=new Insets(30,0,0,0);
		gc.fill=GridBagConstraints.BOTH;
		setpan.add(Ok,gc);
		this.add(setpan);
		this.setVisible(true);
	}
}
