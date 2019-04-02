import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
//Implements a rudimentary AI for chess
//The AI uses a minmax function with alpha-beta pruning to assess evaluation functions
//Functions:
	//bestscore - implements the minmax function and depth. uses recursion through depth to generate the different 
		//depth "tiers" and asseses the expected board value for each initial move
	//getBestMov - assigns bestscore to all moves at tier 1 and returns the best move 
	//getEval - evaluates the board state (uses checkthreats which encourages defense/offense and mobility which
	//encourages increasing piece mobility
//NOTE - the ai will always transform to a queen




public class AIWIP {
	public static class CantMove extends Exception {
        public CantMove(String msg) {
            super(msg);
        }
    }
	public static Move getBestMov(Board b, PieceCol c, int depth) throws CantMove {
		HashSet<Move> allMoves = validmov(b,c);
		if (allMoves==null) {
			throw new CantMove("Cant Move");
		}
		TreeMap<Double,Move> MovScores = new TreeMap<Double,Move>();
		PieceCol next = null;
		switch (c) {
		case WHITE:
			next=PieceCol.BLACK;
			break;
		case BLACK:
			next=PieceCol.WHITE;
			break;
		}
		for (Move m: allMoves) {
			boolean toundo = false;
			try {
				b.updateboardstate(m.initialPoint, m.finalPoint);
				b.aitoggleturn();
				double score = bestscore(b, b.getColor(), depth,10000,-10000);
				MovScores.put(score, m);
				toundo=true;

			} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException e1) {

			} catch (Board.PawnTransformException e) {
				try {
					b.transform(m.finalPoint,3);
					b.aitoggleturn();
					double score = bestscore(b, b.getColor(), depth,10000,-10000);
					MovScores.put(score, m);
					toundo=true;
				} catch (Board.NoPieceException e1) {
				}
			}
			if (toundo) {
				b.Undo();
			}

		}
		Move bes = null;
		switch (c) {
		case WHITE:
			bes=MovScores.get(MovScores.lastKey());
			break;
		case BLACK:
			bes=MovScores.get(MovScores.firstKey());
			break;
		}
		return bes;
	}
	private static double bestscore(Board b, PieceCol c, int depth,double upper,double lower) {
		HashSet<Move> allMoves = validmov(b,c);
		double score = 0;
		if (depth == 0) {
			return getEval(b);
		}
		switch (c) {
		case WHITE:
			score = -10000;		
			for (Move m: allMoves) {
				boolean toundo = false;
				try {
					b.updateboardstate(m.initialPoint, m.finalPoint);
					b.aitoggleturn();
					double g = score;
					score = Math.max(score, bestscore(b, PieceCol.BLACK, depth-1,upper,lower ));
					toundo=true;
				} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException
						 e1) {
				}catch (Board.PawnTransformException e) {
					try {
						b.transform(m.finalPoint,3);
						b.aitoggleturn();
						double g = score;
						score = Math.max(score, bestscore(b, PieceCol.BLACK, depth-1,upper,lower ));
						toundo=true;
					} catch (Board.NoPieceException e1) {
					}
				}
				if (toundo) {
					b.Undo();
				}
				
				lower = Math.max(score, lower);
				if (lower > upper) {
					return score;
				}
			}
			break;
		case BLACK:
			score = 10000;
			for (Move m: allMoves) {						
				boolean toundo = false;
				try {
						b.updateboardstate(m.initialPoint, m.finalPoint);
						b.aitoggleturn();
						double g = score;
						score = Math.min(score, bestscore(b, PieceCol.WHITE, depth-1,upper,lower  ));
						toundo=true;
				} catch (Board.NoPieceException | Board.WrongColorException | Board.InvalidMoveException
						 e1) {

				}catch (Board.PawnTransformException e) {
					try {
						b.transform(m.finalPoint,3);
						b.aitoggleturn();
						double g = score;
						score = Math.min(score, bestscore(b, PieceCol.WHITE, depth-1,upper,lower  ));
						toundo=true;
					} catch (Board.NoPieceException e1) {
					}
				}
				if (toundo) {
					b.Undo();
				}
				
				upper = Math.min(score, upper);
				if (upper <= lower) {
					return score;
				}
			}
			break;
		}
		return score;
	}
	private static HashSet<Move> validmov (Board b, PieceCol c) { 
		HashSet<Move> allMoves = new HashSet<Move>();
		Piece[][] map = b.getBoardlit();
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (map[i][j]!=null&&map[i][j].GetColor()==c) {
					Point IP = map[i][j].GetPos();
					for (Point p: b.moveFilter(map[i][j])) {
						allMoves.add(new Move(IP,p));
					}
				}
			}
		}
		return allMoves;
	}
	private static double Mobility (Piece[][] b, Piece p, double mobMult) {
		double val = 0;
		double maxmove = 1000000000;
		if (p.getClass()==Queen.class) {
			maxmove = 108;
		}
		else if (p.getClass()==Bishop.class) {
			maxmove = 14;
		}
		else if (p.getClass()==Rook.class) {
			maxmove=14;
		}
		else if (p.getClass()==Knight.class) {
			maxmove=8;
		}
		else if (p.getClass()==Pawn.class) {
			maxmove=6;
		}
		double x = p.Move(b).size();
		val = val + (x/maxmove)*(p.getScore())*mobMult;
		return val;
	}
	private static double checkDefThreats (Piece[][] map, Piece p, double threatMultiplier, double defenseMultiplier) {
		double threatScore = 0;
		Set<Point> mov = new HashSet<Point>();
		if (p.getClass()==Pawn.class) {
			Pawn ppawn = (Pawn) p;
			mov = ppawn.Threaten(map);
		}
		else {
			mov = p.Move(map);		
		}
		if (p.getClass()==King.class) {
			King hasCastledCheck = (King) p;
			if (hasCastledCheck.hascastled) {
				threatScore = threatScore + 3;
			}
		}
		for (Point points: mov) {
			if (map[points.x][points.y]!=null&&map[points.x][points.y].getClass()==King.class) {
				if (map[points.x][points.y].GetColor()!=p.GetColor()) {
					threatScore = threatScore + 2;
				}
			}
			else if (map[points.x][points.y]!=null&&map[points.x][points.y].GetColor()!=p.GetColor()) {
				threatScore = threatScore + (threatMultiplier*map[points.x][points.y].getScore());
			}
			else if (map[points.x][points.y]!=null&&map[points.x][points.y].GetColor()==p.GetColor()) {
				threatScore = threatScore + (defenseMultiplier*map[points.x][points.y].getScore());
			}
		}
		return threatScore;
	}
	private static double getEval (Board b) {
		Piece[][] map = b.getBoardlit();
		double Score = 0;
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (map[i][j]!=null) {
					Piece p = map[i][j];
					switch(p.GetColor()) {
					case WHITE:
						Score = Score + p.getScore()+Mobility(map,p,.05)+checkDefThreats(map,p,.1,.2);
						break;
					case BLACK:
						Score = Score -(p.getScore())-Mobility(map,p,.05)-checkDefThreats(map,p,.1,.2);
						break;
						
						
					}
				}
			}
		}
		return Score;
	}
}
