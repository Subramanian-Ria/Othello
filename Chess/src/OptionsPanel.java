import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
//implements the options panel
//getMinimumSize overrided to ensure correct sizing in main
public class OptionsPanel extends JPanel{
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800,200);
	}
	public OptionsPanel() throws IOException {
		super();
		this.setBorder(BorderFactory.createTitledBorder
				(BorderFactory.createLineBorder(new Color(102,51,0), 10),"Options"));
		this.setBackground(new Color(255,255,183));
		Icon U = new ImageIcon(ImageIO.read(new File("Files/Undo.png")).getScaledInstance(-1, 70, Image.SCALE_DEFAULT));
		Icon S = new ImageIcon(ImageIO.read(new File("Files/Settings.png")).getScaledInstance(-1, 70, Image.SCALE_DEFAULT));
		Icon R = new ImageIcon(ImageIO.read(new File("Files/Readme.png")).getScaledInstance(-1, 70, Image.SCALE_DEFAULT));
		Icon Q = new ImageIcon(ImageIO.read(new File("Files/Quit.png")).getScaledInstance(-1, 70, Image.SCALE_DEFAULT));
		JButton Undo = new JButton() {
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400,200);
			}
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(400,200);
			}
		};
		Undo.setMargin(new Insets(0,0,0,0));
		Undo.setBorder(new EmptyBorder(0,0,0,0));
		Undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Game.app.b.Undo();
			}
		});
		this.setBackground(new Color(255,255,183));
		JButton Quit = new JButton(){
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400,200);
			}
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(400,200);
			}
		};
		Quit.setMargin(new Insets(0,0,0,0));
		Quit.setBorder(new EmptyBorder(0,0,0,0));
		Quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.app.dispose();
			}
		});
		JButton Settings = new JButton (){
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400,200);
			}
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(400,200);
			}
		};
		Settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SettingsPanel pan = new SettingsPanel
						(Game.app.b.toRotate,Game.app.b.toPreview,Game.app.b.AI,Game.app.b.AICol);
			}
		});
		Settings.setMargin(new Insets(0,0,0,0));
		Settings.setBorder(new EmptyBorder(0,0,0,0));
		JButton Readme = new JButton(){
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400,200);
			}
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(400,200);
			}
		};
		Readme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog Readme = new JDialog();
				JLabel Instructions = new JLabel("<html>Welcome to Chess! <br/>"
												+ "The standard rules of chess apply. A couple of things to note: <br/>"
												+ "Move by selecting the piece you want to move then clicking <br/>"
												+ "the square you want to move to. The program will automatically <br/>"
												+ "prevent you from making illegal moves. The default state has move <br/>"
												+ "preview and rotation on. Rotation can be a bit jarring and you <br/>"
												+ "might want to turn it off. AI Black/White can also be turned on <br/>"
												+ "all of these settings are in the settings panel which can be <br/>"
												+ "accessed through the button at the bottom of the screen. <br/>"
												+ "Checkmate and Promotion are popups that appear when relevant. <br/>"
												+ "draws require manual restart due to the variety of situations that <br/>"
												+ "prompt them.");
				Readme.add(Instructions);
				Readme.setSize(400, 300);
				Readme.setModal(true);
				Readme.setVisible(true);
			}
		});
		Readme.setMargin(new Insets(0,0,0,0));
		Readme.setBorder(new EmptyBorder(0,0,0,0));
		
		
		//Undo
		add(Undo);
		//Quit
		add(Quit);
		//Readme
		add(Readme);
		//Settings
		add(Settings);
		
		Undo.setIcon(U);
		Quit.setIcon(Q);
		Settings.setIcon(S);
		Readme.setIcon(R);
	
	}
	
}
