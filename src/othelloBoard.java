import java.awt.*;
import java.util.ArrayList;

public class othelloBoard
{

    //The actual othello/reversi board
    private boardSpace board[][] = new boardSpace[8][8];

    //array of objects that store all potential moves that can be made in any given turn for each player
    potentialMove[][] OMoves = new potentialMove[8][8];
    potentialMove[][] XMoves = new potentialMove[8][8];

    public boolean oneOneX = false;
    public boolean oneOneO = false;

    //current turn
    public playerColor turn = playerColor.O;

    //an enum containing the possible states of a square on the board
    //the board is an array of boardSpaces
    public enum boardSpace
    {
        EMPTY,
        O,
        X
    }

    //enum for the two player colors
    public enum playerColor
    {
        O,
        X
    }

    //checks if the board is in a won state
    public boolean won()
    {
        initPotMoves();
        if(oneOneO)
        {
            board[0][0] = boardSpace.O;
        }
        else if(oneOneX)
        {
            board[0][0] = boardSpace.X;
        }
        else
        {
            board[0][0] = boardSpace.EMPTY;
        }
        findValidMoves(playerColor.O);
        findValidMoves(playerColor.X);
        boolean availableMoves = false;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(OMoves[j][i].valid)
                {
                    availableMoves = true;
                }
                else if(XMoves[j][i].valid)
                {
                    availableMoves = true;
                }
            }
        }
        if(availableMoves)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //calls the functions to set up the board for play
    public void build()
    {
        populate();
        initPotMoves();
        turn = playerColor.X;
        findValidMoves(turn);
    }

    //initializes the pieces on the board
    private void populate()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                board[j][i] = boardSpace.EMPTY;
            }
        }
        board[3][3] = boardSpace.O;
        board[4][3] = boardSpace.X;
        board[3][4] = boardSpace.X;
        board[4][4] = boardSpace.O;
    }

    //initializes each potentialMove object inside the 2d array that stores them
    public void initPotMoves()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                OMoves[j][i] = new potentialMove();
                XMoves[j][i] = new potentialMove();
                for(int k = 0; k < 8; k++)
                {
                    OMoves[j][i].takenPieces[k] = new ArrayList<>();
                    XMoves[j][i].takenPieces[k] = new ArrayList<>();
                }
            }
        }
    }

    //receives coordinates, checks whether or not the move is valid by calling the checkMove function
    //makes changes to the board if the move is valid
    public int movePiece(int xPos, int yPos)
    {
        if(!checkMove(xPos, yPos))
        {
            return -1;
        }
        //changes all pieces taken by the move made to the opposing player's
        switch (turn)
        {
            case O:
                board[xPos][yPos] = boardSpace.O;
                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < OMoves[xPos][yPos].takenPieces[i].size(); j++)
                    {
                        if(OMoves[xPos][yPos].takenPieces[i].get(j).x < 0)
                        {
                            OMoves[xPos][yPos].takenPieces[i].get(j).x++;
                        }
                        if(OMoves[xPos][yPos].takenPieces[i].get(j).y < 0)
                        {
                            OMoves[xPos][yPos].takenPieces[i].get(j).y++;
                        }
                        board[OMoves[xPos][yPos].takenPieces[i].get(j).x][OMoves[xPos][yPos].takenPieces[i].get(j).y] = boardSpace.O;
                    }
                }
                break;
            case X:
                board[xPos][yPos] = boardSpace.X;
                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < XMoves[xPos][yPos].takenPieces[i].size(); j++)
                    {
                        if(XMoves[xPos][yPos].takenPieces[i].get(j).x < 0)
                        {
                            XMoves[xPos][yPos].takenPieces[i].get(j).x++;
                        }
                        if(XMoves[xPos][yPos].takenPieces[i].get(j).y < 0)
                        {
                            XMoves[xPos][yPos].takenPieces[i].get(j).y++;
                        }
                        board[XMoves[xPos][yPos].takenPieces[i].get(j).x][XMoves[xPos][yPos].takenPieces[i].get(j).y] = boardSpace.X;
                    }
                }
                break;
        }
        return 0;
    }

    //checks that the inputted move is not out of bounds and that the space is empty
    //also calls other functions to check if the move is valid
    public boolean checkMove(int xPos, int yPos)
    {
        if(((0 > xPos) || (xPos > 7)) || ((0 > yPos) || (yPos > 7)))
        {
            return false;
        }
        if(board[xPos][yPos] != boardSpace.EMPTY)
        {
            return false;
        }
        switch (turn)
        {
            case O:
                if(OMoves[xPos][yPos].valid) return true;
                break;
            case X:
                if(XMoves[xPos][yPos].valid) return true;
                break;
        }
        return false;
    }

    //generates all moves and potential taken pieces such that they are not out of bounds
    //and then passes them to the checkLocation function to see if they are legal moves
    public boolean findValidMoves(playerColor turn)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int[] dir = {0, 0, 0, 0};
                if(i == 0)
                {
                    //y diff pos
                    dir[2] = 1;
                }
                else if(i == 7)
                {
                    //y diff neg
                    dir[3] = -1;
                }
                else
                {
                    //y diff pos
                    dir[2] = 1;
                    //y diff neg
                    dir[3] = -1;
                }
                if(j == 0)
                {
                    //x diff pos
                    dir[0] = 1;
                }
                else if(j == 7)
                {
                    //x diff neg
                    dir[1] = -1;
                }
                else
                {
                    //x diff pos
                    dir[0] = 1;
                    //x diff neg
                    dir[1] = -1;
                }
                if(turn == playerColor.O && board[j][i] == boardSpace.EMPTY)
                {
                    OMoves[j][i] = checkLocation(j, i, dir, turn, board);
                }
                else if(turn == playerColor.X && board[j][i] == boardSpace.EMPTY)
                {
                    XMoves[j][i] = checkLocation(j, i, dir, turn, board);
                }
            }
        }
        return false;
    }

    //checks whether or not a given move is valid for a given player and generates all the pieces that would be flipped if that move were played
    private potentialMove checkLocation(int xPos, int yPos, int[] dir, playerColor turnTemp, boardSpace[][] tempBoard)
    {
        potentialMove p = new potentialMove();
        p.location.x = xPos;
        p.location.y = yPos;
        p.valid = false;
        p = initTakenPieces(p);

        int tempCount = 0;
        for(int i = 0; i < dir.length; i++)
        {
            if(dir[i] != 0)
            {
                tempCount++;
            }
        }
        if(tempCount == 0)
        {
            p.valid = false;
            return p;
        }

        boardSpace playing;
        boardSpace opponent;

        if(turnTemp == playerColor.O)
        {
            playing = boardSpace.O;
            opponent = boardSpace.X;
        }
        else
        {
            playing = boardSpace.X;
            opponent = boardSpace.O;
        }

        int xDir;
        int yDir;
        int count = 0;
        boardSpace currentSpace;
        for(int i = yPos + dir[3]; i <= yPos + dir[2]; i++)
        {
            if(i == yPos + 1)
            {
                yDir = 1;
            }
            else if(i == yPos - 1)
            {
                yDir = -1;
            }
            else
            {
                yDir = 0;
            }
            for(int j = xPos + dir[1]; j <= xPos + dir[0]; j++)
            {
                if(j != xPos || i != yPos)
                {
                    if(j == xPos + 1)
                    {
                        xDir = 1;
                    }
                    else if(j == xPos - 1)
                    {
                        xDir = -1;
                    }
                    else
                    {
                        xDir = 0;
                    }
                    currentSpace = tempBoard[j][i];
                    int tempX = j;
                    int tempY = i;
                    boolean valid = false;
                    while(currentSpace == opponent && tempX > -1 && tempX < 8 && tempY > -1 && tempY < 8)
                    {
                        if(tempX + xDir > -1 && tempX + xDir < 8 && tempY + yDir > -1 && tempY + yDir < 8)
                        {
                            if(currentSpace == opponent && tempBoard[tempX + xDir][tempY + yDir] == playing)
                            {
                                valid = true;
                            }
                            currentSpace = tempBoard[tempX + xDir][tempY + yDir];
                        }
                        Point tempPoint = new Point();
                        tempPoint.x = tempX;
                        tempPoint.y = tempY;
                        p.takenPieces[count].add(tempPoint);
                        tempX += xDir;
                        tempY += yDir;
                    }
                    if(!valid)
                    {
                            p.takenPieces[count].clear();
                    }
                    else
                    {
                        p.valid = true;
                    }
                    count++;
                }
            }
        }
        return p;
    }

    //initializes each arrayList inside the potentialMove object
    private potentialMove initTakenPieces(potentialMove p)
    {
        for(int i = 0; i < p.takenPieces.length; i++)
        {
            p.takenPieces[i] = new ArrayList<>();
        }
        return p;
    }

    //calculates the number of points with the current board state given a player color to check for
    public int getPoints(playerColor turn)
    {
        boardSpace counting = boardSpace.EMPTY;
        switch(turn)
        {
            case X:
                counting = boardSpace.X;
                break;
            case O:
                counting = boardSpace.O;
                break;
        }
        int points = 0;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[j][i] == counting)
                {
                    points++;
                }
            }
        }
        return points;
    }

    //returns the state of a specific space on the board
    public boardSpace getState(int x, int y)
    {
        return board[x][y];
    }
}

//object to create an array of legal moves
class potentialMove extends othelloBoard
{
    Point location = new Point();
    boolean valid;
    ArrayList<Point>[] takenPieces = new ArrayList[8];
}