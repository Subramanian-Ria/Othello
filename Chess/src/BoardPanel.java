import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;



//Board Panel implements the main GUI (the Actual Board) component. It generates a 2d array of JPanels
//which paint pieces on them according to a Board class, b. Each JPanel has an action listener to handle clicks
//BoardPanel also handles checkmate and pawn promotion
//BoardPanel has the following functions:
	//**Much of the action listener addition/implementation of squares is done in the constructor as anon fun
	//getCol() - returns the color
	//getMinimum size - overriden to ensure correct sizing in the mainframe (combines the board/options/score)
	//getScore - self explanatory
	//makecheckmatepanel - makes a panel for checkmate
	//makepanel - makes a panel for transform
	//orienter - adjusts the y coordinate of functions to correctly implement rotation (the panels dont rotate
	//but now refer to the opposite y coordinate on the board, ensuring that repaint generates a rotated image)
	//repaintsquares - repaints all the JPanels
	//Undo - extends the undo function from the board
	//AIMove - the AI moves
//Variables:
	//b - the board (Board class)
	//squararr - array of jpanels, used for repaintsquares()
	//pieceClicked - stores the clicked Piece
	//piecetoMove/piecetoTransform - used for move/transform functions/to store relevant pieces
	//moves - stores the moves of the clicked piece to generate move preview
	//Orr - toggles the rotation
	//toRotate/toPreview - wether or not rotation/preview happens
	//modval - only used for rotation
	//col - stores color string for button creation
	//AI/AICol - whether or not AI is on/which color
	//aidep - depth that ai function goes at + 1 (a value of 3 leads to depth 4)
public class BoardPanel extends JPanel{
	private JPanel[][] squararr = new JPanel[8][8];
	private Board b = new Board(true);
	private Boolean pieceClicked;
	private Point piecetoMove;
	private Point piecetoTransform;
	private Set<Point> moves;
	public Boolean Orr = false;
	public Boolean toRotate;
	public Boolean toPreview;
	private int modval = 1;
	public Boolean AI = false;
	public PieceCol AICol = PieceCol.BLACK;
	private int aidep =2;
	public void switchModval() {
		modval = 1-modval;
	}
	public String getCol () {
		String s = "";
		if(!Orr) {
			s="White";
		}
		else {
			s="Black";
		}
		return s;
	}
	public void repaintsquares () {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				squararr[i][j].repaint();
			}
    	}
	}
	public int orienter (int i) {
		int z=i;
		if (Orr&&toRotate) {
			z= 7-i;
		}
		return z;
	}
	public void AIMove() {
		int pieces = 0;
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (b.getBoardlit()[i][j]!=null) {
					pieces = pieces+1;
				}
			}
		}
		if (pieces <=10) {
			aidep=3;
		}
		if (pieces <=5) {
			aidep = 4;
		}
		Move bestmove;
		try {
			bestmove = AIWIP.getBestMov(b, b.getColor(), aidep);
			b.updateboardstate(bestmove.initialPoint, bestmove.finalPoint);
		} catch (AIWIP.CantMove e1) {
			//Implement draw if time
		} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException
				| Board.PawnTransformException e) {
		}
		try {
			b.toggleturn();
		} catch (Board.CheckmateException e) {
			repaintsquares();
			makecheckmatepanel(b.getTurnString(),this);
		}
		Orr=!Orr;
		if (toRotate) {
			modval=1-modval;
		}
		pieceClicked=false;
		moves=null;
		piecetoMove=null;
		repaintsquares();
	}
	public void Undo() {
		Boolean bol = b.Undo();
		if (bol) {
			Orr=!Orr;
			if (toRotate) {
				modval=1-modval;
			}
			pieceClicked=false;
			moves=null;
			piecetoMove=null;
			repaintsquares();
		}
	}
	public String getScore(boolean isBlack) {
		String s = null;
		if(isBlack) {
			s=Integer.toString(b.ScoreB);
		}
		else {
			s=Integer.toString(b.ScoreW);
		}
		return s;
	}
	public void makecheckmatepanel (String s, BoardPanel bp) {
		JPanel CheckMatePanel = new JPanel();
		CheckMatePanel.setBorder(BorderFactory.createTitledBorder
				(BorderFactory.createLineBorder(new Color(102,51,0), 10),"Options"));
		CheckMatePanel.setBackground(new Color(255,255,183));
		JDialog CheckDial = new JDialog (Game.app,true);
		CheckMatePanel.setLayout(new GridBagLayout());
		GridBagConstraints gcCheck = new GridBagConstraints();
		gcCheck.weightx=1;
		JButton quit = new JButton("Quit");
		JButton reset = new JButton("Restart");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Game.app.dispose();
			}
			
		});
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				b=new Board(true);
				bp.repaint();
				Game.app.p.setscore(false);
				Game.app.p.setscore(true);
				Orr=!Orr;
				if (toRotate) {
					modval=1-modval;
				}
				aidep =2;
				pieceClicked=false;
				bp.repaint();
				CheckDial.dispose();
			}
			
		});
		gcCheck.gridx=0;
		gcCheck.gridy=1;
		gcCheck.weightx=.5;
		CheckMatePanel.add(quit,gcCheck);
		gcCheck.gridx=1;
		CheckMatePanel.add(reset,gcCheck);
		CheckMatePanel.setBorder(BorderFactory.createTitledBorder("Congrats! "+s+" is in checkmate."));
		CheckDial.add(CheckMatePanel);
		CheckDial.setSize(400, 300);
		CheckDial.setVisible(true);
	}
	public void makepanel (BoardPanel bp) {
		JPanel transPanel = new JPanel();
		JDialog transDial = new JDialog (Game.app,true);
		transPanel.setBorder(BorderFactory.createTitledBorder
				(BorderFactory.createLineBorder(new Color(102,51,0), 10),"Options"));
		transPanel.setBackground(new Color(255,255,183));
		try {
			String col = getCol();
			JButton Kn = new JButton(new ImageIcon(ImageIO.read(new File("Files/" + col + "Knight.png"))));
			JButton Bs = new JButton(new ImageIcon(ImageIO.read(new File("Files/" + col + "Bishop.png"))));
			JButton Rk = new JButton(new ImageIcon(ImageIO.read(new File("Files/" + col + "Rook.png"))));
			JButton Qu = new JButton(new ImageIcon(ImageIO.read(new File("Files/" + col + "Queen.png"))));
			JButton[] transButtonArr = {Kn,Bs,Rk,Qu};
			transPanel.setSize(600, 200);
			transPanel.setLayout(new GridBagLayout());
			GridBagConstraints gcTrans = new GridBagConstraints();
			gcTrans.fill=GridBagConstraints.BOTH;
			gcTrans.weightx=.25;
			gcTrans.weighty=1;
			for (int i=0; i<4; i++) {
				final int z = i;
				transButtonArr[i].setSize(100, 100);
				transButtonArr[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						try {
							b.transform(piecetoTransform, z);
						} catch (Board.NoPieceException e) {
						}
						transDial.dispose();
						repaintsquares();
						try {
							b.toggleturn();
							Orr=!Orr;
							if (toRotate) {
								modval=1-modval;
							}
							pieceClicked=false;
							moves=null;
							piecetoMove=null;
							repaintsquares();
							if (AI&&AICol==b.getColor()) {
								AIMove();
							}
						} catch (Board.CheckmateException e) {
							repaintsquares();
							makecheckmatepanel(b.getTurnString(),bp);
						}
					}
				});
				gcTrans.gridx=i;
				transPanel.add(transButtonArr[i],gcTrans);
			}
			transDial.add(transPanel);
			transDial.setSize(600, 200);
		} catch (IOException e3) {
		}
		transDial.setVisible(true);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800,800);
	}
	public BoardPanel(boolean tr, boolean tp) {
		super();
		BoardPanel bpforreset = this;
		toRotate=tr;
		toPreview=tp;
		Orr=false;
		aidep =2;
		pieceClicked=false;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(new Color(102,51,0), 10));
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx= .125;
		gc.weighty=.125;
		gc.fill=GridBagConstraints.BOTH;
		for (int i=0;i<8;i++) {
			gc.gridx=i;
			for (int j=0;j<8;j++) {
				BoardSquarePanel square = new BoardSquarePanel(i,7-j) {
					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						try {
							Piece pc = b.getPiece(new Point (this.getXPos(),orienter(this.getYPos())));
							Dimension size = this.getSize();
							if (pc!=null) {
								Image img = pc.getImage().getScaledInstance(70, 75, 0);
								g.drawImage(img, size.width/10+3, size.height/10, null);
								if (pieceClicked) {
									if (pc.GetPos().equals(piecetoMove)) {
										Image img2 = pc.getGlowImage().getScaledInstance(83, 83, 0);
										g.drawImage(img2, size.width/10-4, size.height/10-4, null);
									}
									if (toPreview&&moves!=null&&moves.contains(new Point(this.getXPos(),orienter(this.getYPos())))) {
										Image img3 = ImageIO.read(new File("Files/RedGlow.png")).getScaledInstance(100, 95, 0);
										g.drawImage(img3, 0, 0, null);

									}
								}
							}
							else {
								
							}
						} 
						catch (Board.NoPieceException e) {
							if (toPreview&&moves!=null&&moves.contains(new Point(this.getXPos(),orienter(this.getYPos())))) {
								try {
									Image img = ImageIO.read(new File("Files/GreenGlow.png")).getScaledInstance(100, 95, 0);
									g.drawImage(img, 0, 0, null);
								} catch (IOException e1) {
								}
							}
						} 
						catch (IOException e) {
						}
						if ((this.getXPos()+this.getYPos())%2==modval) {
								this.setBackground(new Color(255,255,183));
						}
						else {
								this.setBackground(Color.BLACK);		
						}
						
					}
				};
				squararr[i][j] = square;
				square.addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e) {
						if (!AI||AI&&b.getColor()!=AICol) {
							Point a = new Point(square.getXPos(),orienter(square.getYPos()));
		                    try {
		                    	Piece tocheck = b.getPiece(a);
								if (!pieceClicked&&tocheck.GetColor()==b.getColor()) {
										moves= b.moveFilter(tocheck);
										pieceClicked=true;
								    	piecetoMove = a;
								    	repaintsquares();
								}
								else {
									if (pieceClicked) {
										try {
											b.updateboardstate(piecetoMove, a);
											b.toggleturn();
											Orr=!Orr;
											if (toRotate) {
												modval=1-modval;
											}
											repaintsquares();
											if (AI&&AICol==b.getColor()) {
												AIMove();
											}
										} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException e2) {
										} catch (Board.CheckmateException e1) {
											repaintsquares();
											makecheckmatepanel(b.getTurnString(),bpforreset);
										} catch (Board.PawnTransformException e1) {
											String pointtoConvert[] = e1.getMessage().substring(17,22).split(",y=");
											piecetoTransform = 
												new Point(Integer.parseInt(pointtoConvert[0]), Integer.parseInt(pointtoConvert[1]));
											makepanel(bpforreset);
										}
									}
									pieceClicked=false;
									moves=null;
									piecetoMove=null;
									repaintsquares();
								}
							} catch (Board.NoPieceException e1) {
								if (pieceClicked) {
									try {
										b.updateboardstate(piecetoMove, a);
										moves=null;
										pieceClicked=false;
										piecetoMove=null;
										repaintsquares();
										b.toggleturn();
										Orr=!Orr;
										if (toRotate) {
											modval=1-modval;
										}
										repaintsquares();
										if (AI&&AICol==b.getColor()) {
											AIMove();
										}
									} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException e2) {
										//repeated code, revise to adjust try catches
										moves=null;
										pieceClicked=false;
										piecetoMove=null;
										repaintsquares();
									} catch (Board.CheckmateException e2) {
										repaintsquares();
										makecheckmatepanel(b.getTurnString(),bpforreset);
									} catch (Board.PawnTransformException e2) {
										String pointtoConvert[] = e2.getMessage().substring(17,22).split(",y=");
										piecetoTransform = 
											new Point(Integer.parseInt(pointtoConvert[0]), Integer.parseInt(pointtoConvert[1]));
										makepanel(bpforreset);
									}
								}
							}
		                }
		             }
				});				
				gc.gridy=j;
				add(square,gc);
			}
		}
	}
}
