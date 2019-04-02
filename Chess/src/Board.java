import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

//Board handles most of the computation for moves/turn ends/etc. and stores the board state/ensures rule following.
//Some of the move filtering is already done on a piece level but discovered checks are implemented here.
//Board has the following functions:
	//getBoard() - returns a string of the board state, only used for println in testing
	//getBoardlit() - returns the boardstate, used for testing
	//getColor() - returns which Color's turn it is. Used to implement rotation, maintain correct user interaction (preventing black from clicking white)
	//getPiece() - returns the piece at a given point. 
	//toggleturn() - switches the turn. Has the following helpers:
		//checkmatehelper - checks for checkmate
		//enpassanthelper - ensures that pawns can only enpassant during the correct turn (1st after they move 2 up)
	//getTurnString() - returns which Color's turn it is as a string. used for error messages
	//moveFilter - filters out moves that lead to discovered check
	//updateboardstate - moves pieces/implements taking pieces
	//ischeck is onnly used for AI implementation
	//transform - deals with pawn promotion
	//Undo - undos moves, used in AI and for Undo Button
//Variables:
	//boardstate stores the boardstate in a 2D array
	//whiteking and blackking keep track of king position
	//turn is whos color turn it is
	//hasmoved and movetoggle are only for testing (the game prevents white from taking two turns in a row
		//but switching movetoggle allows for that to test
	//MoveHistory and Move are for the Undo function, a move is added to MoveHistory on turn toggle
	//Score is self explanatory
//Variety of exceptions for the GUI and other parts of the program to handle, also used for error checking
public class Board {
	private Piece[][] boardstate;
	public Point blackking;
	public Point whiteking;
	private PieceCol turn;
	private boolean hasmoved;
	private boolean movetoggle;
	private LinkedList<UndoInfoStorage> MoveHistory;
	private UndoInfoStorage Move;
	public int ScoreB;
	public int ScoreW;
	
	
	public static class NoPieceException extends Exception {
        public NoPieceException(String msg) {
            super(msg);
        }
    }
	public static class WrongColorException extends Exception {
        public WrongColorException(String msg) {
            super(msg);
        }
    }
	public static class InvalidMoveException extends Exception {
        public InvalidMoveException(String msg) {
            super(msg);
        }
    }
	public static class CheckmateException extends Exception {
        public CheckmateException(String msg) {
            super(msg);
        }
    }
	public static class PawnTransformException extends Exception {
        public PawnTransformException(String msg) {
            super(msg);
        }
    }
	public static class DrawException extends Exception {
        public DrawException(String msg) {
            super(msg);
        }
    }
	private void enpassanthelper() {
		int z = 0;
		switch(turn) {
			case WHITE : 
				z=3;
				break;
			case BLACK: 
				z=4;
				break;
		}
	
		for (int i = 0; i<8; i++) {
			Piece toCheck = boardstate[i][z];
			if (toCheck!=null && toCheck.getClass()==Pawn.class) {
				Pawn pawnToCheck = (Pawn) boardstate[i][z];
				pawnToCheck.ToggleEnpassant();
				Move.enpassantPawns.add(pawnToCheck);
			}
		}
	}
	public boolean checkmatehelper() {
		boolean result = true;
			for (int i=0;i<8;i++) {
				for (int j=0;j<8;j++) {
					if (boardstate[i][j]!=null&& boardstate[i][j].GetColor()==turn &&
							!this.moveFilter(boardstate[i][j]).isEmpty()) {
						result = false;
						i=10;
						j=10;
					}
				}
			}
		return result;
	}
	//ischeck only used for testing/AI and checkmate
	public boolean ischeck() {
		boolean check = false;
		switch(turn) {
		case WHITE:
			King tocheck = (King) boardstate[whiteking.x][whiteking.y];
			check = tocheck.check(boardstate, whiteking);
			break;
		case BLACK:
			King tocheck2 = (King) boardstate[blackking.x][blackking.y];
			check = tocheck2.check(boardstate, blackking);
		}
		return check;
	}
	public void aitoggleturn() {
		MoveHistory.add(Move);
		Move = new UndoInfoStorage(null,null,false,false,null,null, null,null,null, new HashSet<Piece>(),0);
		switch(turn) {
		case WHITE : 
			turn = PieceCol.BLACK;
			break;
		case BLACK:	
			turn=PieceCol.WHITE;
			break;
	}
	movetoggle=false;
	this.enpassanthelper();
	}
	public void toggleturn() throws CheckmateException{
		MoveHistory.add(Move);
		Move = new UndoInfoStorage(null,null,false,false,null,null, null,null,null, new HashSet<Piece>(),0);
		switch(turn) {
			case WHITE : 
				if (hasmoved) {
					King g = (King) boardstate[whiteking.x][whiteking.y];
					if (!movetoggle && g.check(boardstate, whiteking)) {
						throw new CheckmateException (this.getTurnString() + " is in checkmate!");
					}
				}
				//Adjusts the JPANEL for Score
				Game.app.p.setscore(false);
				//
				turn = PieceCol.BLACK;
				break;
			case BLACK:
				if (hasmoved) {
					King g = (King) boardstate[blackking.x][blackking.y];
					if (!movetoggle && g.check(boardstate, blackking)) {
						throw new CheckmateException (this.getTurnString() + " is in checkmate!");
					}
				}
				//Adjusts the JPANEL for Score
				Game.app.p.setscore(true);
				//				
				turn=PieceCol.WHITE;
				break;
		}
		movetoggle=false;
		this.enpassanthelper();
		if (this.checkmatehelper()) {
			if (ischeck()) {
				throw new CheckmateException(this.getTurnString() + " is in checkmate!");
			}
		};
	}
	public String getTurnString() {
		String r = null;
		switch(turn) {
			case WHITE : 
				r= "White";
				break;
			case BLACK:
				r="Black";
				break;
		}
		return r;
	}
	public Board(boolean h) {
		ScoreB=0;
		ScoreW=0;
		hasmoved=h;
		turn = PieceCol.WHITE;
		MoveHistory = new LinkedList<UndoInfoStorage>();
		Move = new UndoInfoStorage(null,null,false,false,null,null, null,null,null, new HashSet<Piece>(),0);
		boardstate = new Piece[8][8];
		for (int i=0;i<8;i++) {
			boardstate[i][1] = new Pawn(PieceCol.WHITE,new Point(i,1));
			boardstate[i][6] = new Pawn(PieceCol.BLACK,new Point(i,6));
			if (i==0) {
				boardstate[i][0] = new Rook(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Rook(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==1) {
				boardstate[i][0] = new Knight(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Knight(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==2) {
				boardstate[i][0] = new Bishop(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Bishop(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==3) {
				boardstate[i][0] = new Queen(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Queen(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==4) {
				whiteking = new Point(i,0);
				blackking = new Point(i,7);
				boardstate[i][0] = new King(PieceCol.WHITE, whiteking);
				boardstate[i][7] = new King(PieceCol.BLACK, blackking);
			}
			else if (i==5) {
				boardstate[i][0] = new Bishop(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Bishop(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==6) {
				boardstate[i][0] = new Knight(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Knight(PieceCol.BLACK, new Point(i,7));
			}
			else if (i==7) {
				boardstate[i][0] = new Rook(PieceCol.WHITE, new Point(i,0));
				boardstate[i][7] = new Rook(PieceCol.BLACK, new Point(i,7));
			}
		}
	}
	public String getBoard() {
		return Arrays.deepToString(boardstate).replace("], ", "]\n");
	}
	public Piece[][] getBoardlit() {
		return boardstate;
	}
	public PieceCol getColor() {
		return turn;
	}
	public Piece getPiece (Point pos) throws NoPieceException{
		Piece z = boardstate[pos.x][pos.y];
		if (z==null) {
			throw new NoPieceException("No Such Piece");
		}
		else {
			return z;
		} 
	}
	public Set<Point> moveFilter (Piece movePiece) {
		Point initialPoint = movePiece.GetPos();
		Set<Point> unfMoves = movePiece.Move(boardstate);
		Point kingwinitial = whiteking;
		Point kingbinitial = blackking;
		Point[] arrayMoves = unfMoves.toArray(new Point[unfMoves.size()]);
		for (int i = 0; i<arrayMoves.length;i++) {
			Point movePoint = arrayMoves[i];
			Piece toTake = boardstate[movePoint.x][movePoint.y];
			if (movePiece.getClass()==King.class) {
				King move = (King) movePiece;
				if (move.GetColor()==PieceCol.WHITE) {
					whiteking = movePoint;
				}
				else {
					blackking = movePoint;
				}
			}
			boardstate[initialPoint.x][initialPoint.y] = null;
			boardstate[movePoint.x][movePoint.y]=movePiece;
			switch(turn) {
			case WHITE : 
				King CheckStatus = (King) boardstate[whiteking.x][whiteking.y];
				if (CheckStatus.check(boardstate, whiteking)) {
					unfMoves.remove(movePoint);
				}
				break;
			case BLACK:
				King CheckStatusb = (King) boardstate[blackking.x][blackking.y];
				if (CheckStatusb.check(boardstate, blackking)) {
					unfMoves.remove(movePoint);
				}
				break;
			}
			boardstate[initialPoint.x][initialPoint.y] = movePiece;
			boardstate[movePoint.x][movePoint.y]=toTake;
			whiteking=kingwinitial;
			blackking=kingbinitial;
		}
		return unfMoves;
	}
	public void updateboardstate(Point initialPoint, Point movePoint) 
			throws NoPieceException, WrongColorException, InvalidMoveException, PawnTransformException{
		Piece toMove = boardstate[initialPoint.x][initialPoint.y];
		Piece toTake = boardstate[movePoint.x][movePoint.y];
		if (toMove==null) {
			throw new NoPieceException("No Such Piece");
		}
		else {
			Move.move=toMove;
			if (!(toMove.GetColor()==turn)) {
				throw new WrongColorException("It's  " + this.getTurnString() + "'s turn");
			}
			else {
				Set<Point> moves = this.moveFilter(toMove);
				if (!moves.contains(movePoint)) {
					throw new InvalidMoveException("This is not a valid move for a " + toMove.GetType() 
												    + " or it puts/keeps you in Check");
				}
				else {
					movetoggle = true;
					if (toMove.getClass() == King.class) {
						int xoffset = initialPoint.x - movePoint.x;
						if (Math.abs(xoffset)==2) {
							King kingToCheck = (King) boardstate[initialPoint.x][initialPoint.y];
							kingToCheck.hascastled=true;
							if (xoffset<0) {
								Point RookPos = new Point (movePoint.x-1,movePoint.y);
								Piece rookToMove = boardstate[7][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[7][initialPoint.y] = null;
								rookToMove.UpdatePos(RookPos);
								boardstate[movePoint.x][movePoint.y]=toMove;
								boardstate[RookPos.x][RookPos.y]=rookToMove;
								Move.RooktoMovePos=RookPos;
								Move.RookPos=new Point(7,initialPoint.y);
								
							}
							else {
								Point RookPos = new Point (movePoint.x+1,movePoint.y);
								Piece rookToMove = boardstate[0][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[0][initialPoint.y] = null;
								rookToMove.UpdatePos(RookPos);
								boardstate[movePoint.x][movePoint.y]=toMove;
								boardstate[movePoint.x+1][movePoint.y]=rookToMove;
								Move.RooktoMovePos=RookPos;
								Move.RookPos=new Point(0,initialPoint.y);
							}
							Move.initialPoint=initialPoint;
							Move.finalPoint=movePoint;
							Move.toggleHasMoved=true;
						}
						else {
							if (toTake==null) {
								King kingToCheck = (King) boardstate[initialPoint.x][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[movePoint.x][movePoint.y]=toMove;
								Move.initialPoint=initialPoint;
								Move.finalPoint=movePoint;
								if (!kingToCheck.gethasmoved()) {
									Move.toggleHasMoved=true;
								}
							}
							else {
								King kingToCheck = (King) boardstate[initialPoint.x][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[movePoint.x][movePoint.y]=toMove;
								Move.initialPoint=initialPoint;
								Move.finalPoint=movePoint;
								if (!kingToCheck.gethasmoved()) {
									Move.toggleHasMoved=true;
								}
								if (toTake.getClass()==Pawn.class) {
									Pawn PawntoTake = (Pawn) toTake;
									if (!PawntoTake.gethasmoved()) {
										Move.toggleTakeHasMoved=true;
									}
								}
								if (toTake.getClass()==Rook.class) {
									Rook RooktoTake = (Rook) toTake;
									if (!RooktoTake.gethasmoved()) {
										Move.toggleTakeHasMoved=true;
									}
								}
								toTake.UpdatePos(new Point(-1,-1));
								Move.taken=toTake;
								switch(turn) {
								case WHITE:
									ScoreW=ScoreW+toTake.getScore();
									Move.ScoreChange=toTake.getScore();
									break;
								case BLACK:
									ScoreB=ScoreB+toTake.getScore();
									Move.ScoreChange=toTake.getScore();
									break;
								}
							}
							
						}
						toMove.UpdatePos(movePoint);
						switch(turn) {
						case WHITE:
							whiteking=movePoint;
							break;
						case BLACK:
							blackking=movePoint;
							break;
						}
					}
					else if (toMove.getClass() == Pawn.class) {
						int xOffSet = Math.abs(initialPoint.x-movePoint.x);
						if (xOffSet==1) {
							if (toTake==null) {
								//Pawn piece cant be null as pawn move prevents diagonal unless enpassant met
								Pawn enpassantPawn = null;
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[movePoint.x][movePoint.y]=toMove;
								if (this.getColor()==PieceCol.WHITE) {
									enpassantPawn = (Pawn) boardstate[movePoint.x][movePoint.y-1];
									boardstate[movePoint.x][movePoint.y-1] = null;

								}
								else {
									enpassantPawn = (Pawn) boardstate[movePoint.x][movePoint.y+1];
									boardstate[movePoint.x][movePoint.y+1] = null;

								}
								Move.initialPoint=initialPoint;
								Move.finalPoint=movePoint;
								Move.enpassantPos=enpassantPawn.GetPos();
								Move.taken=enpassantPawn;
								enpassantPawn.UpdatePos(new Point(-1,-1));
								switch(turn) {
								case WHITE:
									ScoreW=ScoreW+1;
									Move.ScoreChange=1;
									break;
								case BLACK:
									ScoreB=ScoreB+1;
									Move.ScoreChange=1;
									break;
								}
							}
							else {
								Pawn pawnToCheck = (Pawn) boardstate[initialPoint.x][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[movePoint.x][movePoint.y]=toMove;
								Move.initialPoint=initialPoint;
								Move.finalPoint=movePoint;
								if (!pawnToCheck.gethasmoved()) {
									Move.toggleHasMoved=true;
								}
								if (toTake.getClass()==Pawn.class) {
									Pawn PawntoTake = (Pawn) toTake;
									if (!PawntoTake.gethasmoved()) {
										Move.toggleTakeHasMoved=true;
									}
								}
								if (toTake.getClass()==Rook.class) {
									Rook RooktoTake = (Rook) toTake;
									if (!RooktoTake.gethasmoved()) {
										Move.toggleTakeHasMoved=true;
									}
								}
								Move.taken=toTake;
								toTake.UpdatePos(new Point(-1,-1));
								switch(turn) {
								case WHITE:
									ScoreW=ScoreW+toTake.getScore();
									Move.ScoreChange=toTake.getScore();
									break;
								case BLACK:
									ScoreB=ScoreB+toTake.getScore();
									Move.ScoreChange=toTake.getScore();
									break;
								}
							}
						}
						else {
							if (toTake==null) {
								Pawn pawnToCheck = (Pawn) boardstate[initialPoint.x][initialPoint.y];
								boardstate[initialPoint.x][initialPoint.y] = null;
								boardstate[movePoint.x][movePoint.y]=toMove;
								Move.initialPoint=initialPoint;
								Move.finalPoint=movePoint;
								if (!pawnToCheck.gethasmoved()) {
									Move.toggleHasMoved=true;
								}

							}
						}
						switch(turn) {
						case WHITE:
							if (movePoint.y==7) {
								throw new PawnTransformException(movePoint.toString());
							}
							break;
						case BLACK:
							if (movePoint.y==0) {
								throw new PawnTransformException(movePoint.toString());
							}
							break;
					}
						toMove.UpdatePos(movePoint);
					}
					else {
						if (toMove.getClass()==Rook.class) {
							Rook RooktoMove = (Rook) toMove;
							if (!RooktoMove.gethasmoved()) {
								Move.toggleHasMoved=true;
							}
						}
						if (toTake==null) {
							boardstate[initialPoint.x][initialPoint.y] = null;
							boardstate[movePoint.x][movePoint.y]=toMove;
							Move.initialPoint=initialPoint;
							Move.finalPoint=movePoint;
						}
						else {
							boardstate[initialPoint.x][initialPoint.y] = null;
							boardstate[movePoint.x][movePoint.y]=toMove;
							Move.initialPoint=initialPoint;
							Move.finalPoint=movePoint;
							if (toTake.getClass()==Pawn.class) {
								Pawn PawntoTake = (Pawn) toTake;
								if (!PawntoTake.gethasmoved()) {
									Move.toggleTakeHasMoved=true;
								}
							}
							if (toTake.getClass()==Rook.class) {
								Rook RooktoTake = (Rook) toTake;
								if (!RooktoTake.gethasmoved()) {
									Move.toggleTakeHasMoved=true;
								}
							}
							toTake.UpdatePos(new Point(-1,-1));
							Move.taken=toTake;
							switch(turn) {
							case WHITE:
								ScoreW=ScoreW+toTake.getScore();
								Move.ScoreChange=toTake.getScore();
								break;
							case BLACK:
								ScoreB=ScoreB+toTake.getScore();
								Move.ScoreChange=toTake.getScore();
								break;
							}
						}
						toMove.UpdatePos(movePoint);
					}
				}
			}
		}
	}
	public void transform (Point p, int i) throws NoPieceException {
		Piece toTrans = getPiece(p);
		toTrans.UpdatePos(new Point(-1,-1));
		switch(i) {
		case 0: //Knight
			boardstate[p.x][p.y]=new Knight(toTrans.GetColor(),p);
			break;
		case 1: //Bishop
			boardstate[p.x][p.y]=new Bishop(toTrans.GetColor(),p);
			break;
		case 2: //Rook
			boardstate[p.x][p.y]=new Rook(toTrans.GetColor(),p);
			break; 
		case 3: //Queen
			boardstate[p.x][p.y]=new Queen(toTrans.GetColor(),p);
			break;
		default: //Pawn
			break;
		}
	}
	public boolean Undo() {
		if (!MoveHistory.isEmpty()) {
			UndoInfoStorage toUn = MoveHistory.getLast();
			toUn.move.UpdatePos(toUn.initialPoint);
			for (Piece p: toUn.enpassantPawns) {
				Pawn ppawn = (Pawn) p;
				ppawn.ToggleTrueEnpassant();
			}
			if (toUn.taken==null) {
				//Case 1: castle
				if (toUn.RookPos!=null) {
					boardstate[toUn.initialPoint.x][toUn.initialPoint.y] = toUn.move;
					boardstate[toUn.finalPoint.x][toUn.finalPoint.y] = null;
					Rook toMoveRook = (Rook) boardstate[toUn.RooktoMovePos.x][toUn.RooktoMovePos.y];
					boardstate[toUn.RooktoMovePos.x][toUn.RooktoMovePos.y]=null;
					boardstate[toUn.RookPos.x][toUn.RookPos.y]=toMoveRook;
					toMoveRook.UpdatePos(toUn.RookPos);
					King toMoveKing = (King) toUn.move;
					toMoveKing.hascastled=false;
					toMoveKing.toggleHasMoved();
					toMoveRook.toggleHasMoved();
					switch(turn) {
					case WHITE:
						blackking=toUn.initialPoint;
						break;
					case BLACK:
						whiteking=toUn.initialPoint;
						break;
					}
				}
				//Case 2: normal
				else {
					boardstate[toUn.initialPoint.x][toUn.initialPoint.y] = toUn.move;
					boardstate[toUn.finalPoint.x][toUn.finalPoint.y] = null;
					if (toUn.move.getClass()==Pawn.class&&toUn.toggleHasMoved) {
						Pawn toUndoHas = (Pawn) toUn.move;
						toUndoHas.toggleHasMoved();
					}
					if (toUn.move.getClass()==Rook.class&&toUn.toggleHasMoved) {
						Rook toUndoHas = (Rook) toUn.move;
						toUndoHas.toggleHasMoved();
					}
					if (toUn.move.getClass()==King.class) {
						King toUndoHas = (King) toUn.move;
						if (toUn.toggleHasMoved) {
							toUndoHas.toggleHasMoved();
						}
						switch(turn) {
						case WHITE:
							blackking=toUn.initialPoint;
							break;
						case BLACK:
							whiteking=toUn.initialPoint;
							break;
						}
					}
				}
			}
			else {
				//Case 1: EnPassant
				if (toUn.enpassantPos!=null) {
					boardstate[toUn.initialPoint.x][toUn.initialPoint.y] = toUn.move;
					boardstate[toUn.finalPoint.x][toUn.finalPoint.y] = null;
					boardstate[toUn.enpassantPos.x][toUn.enpassantPos.y] = toUn.taken;
					toUn.taken.UpdatePos(toUn.enpassantPos);
					Pawn toToggle = (Pawn) toUn.taken;
					toToggle.ToggleTrueEnpassant();
				}
				//Case 2: Normal
				else {
					boardstate[toUn.initialPoint.x][toUn.initialPoint.y] = toUn.move;
					boardstate[toUn.finalPoint.x][toUn.finalPoint.y] = toUn.taken;
					if (toUn.move.getClass()==Pawn.class&&toUn.toggleHasMoved) {
						Pawn toUndoHas = (Pawn) toUn.move;
						toUndoHas.toggleHasMoved();
					}
					if (toUn.move.getClass()==Rook.class&&toUn.toggleHasMoved) {
						Rook toUndoHas = (Rook) toUn.move;
						toUndoHas.toggleHasMoved();
					}
					if (toUn.move.getClass()==King.class) {
						King toUndoHas = (King) toUn.move;
						if (toUn.toggleHasMoved) {
							toUndoHas.toggleHasMoved();
						}
						switch(turn) {
						case WHITE:
							blackking=toUn.initialPoint;
							break;
						case BLACK:
							whiteking=toUn.initialPoint;
							break;
						}
					}
					toUn.taken.UpdatePos(toUn.finalPoint);
					if (toUn.taken.getClass()==Pawn.class&&toUn.toggleTakeHasMoved) {
						Pawn toUndoHas = (Pawn) toUn.taken;
						toUndoHas.toggleHasMoved();
					}
					if (toUn.taken.getClass()==Rook.class&&toUn.toggleTakeHasMoved) {
						Rook toUndoHas = (Rook) toUn.taken;
						toUndoHas.toggleHasMoved();
					}
				}
			}
			switch(turn) {
			case WHITE : 
				turn = PieceCol.BLACK;
				ScoreB=ScoreB-toUn.ScoreChange;
				Game.app.p.setscore(true);
				break;
			case BLACK:
				turn=PieceCol.WHITE;
				ScoreW=ScoreW-toUn.ScoreChange;
				Game.app.p.setscore(false);
				break;
			}
		MoveHistory.removeLast();
		return true;
		}
		else {
			return false;
		}
	}	
}
