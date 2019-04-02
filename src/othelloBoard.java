//import org.jetbrains.annotations.Contract;
import java.awt.*;

import java.util.ArrayList;
import java.util.Scanner;
public class othelloBoard {

    private boardSpace board[][] = new boardSpace[8][8];
    private int pointsTotalBlack = 0;
    private int pointsTotalWhite = 0;
    public playerColor turn = playerColor.BLACK;
    private takenPieces takenBlack = new takenPieces();
    private int count = 0;
    public enum boardSpace
    {
        EMPTY,
        BLACK,
        WHITE
    }
    public enum playerColor
    {
        BLACK,
        WHITE
    }
    Scanner sc = new Scanner(System.in);

    public boolean won()
    {
        return false;
    }
    public void build()
    {
        populate();
        testPrint();
        setTakenBlack();
    }
    public int move(int xPos, int yPos)
    {
        if(!checkMove(xPos, yPos))
        {
            return -1;
        }
        switch (turn)
        {
            case BLACK:
                board[xPos][yPos] = boardSpace.BLACK;
                break;
            case WHITE:
                board[xPos][yPos] = boardSpace.WHITE;
                break;
        }
        return 0;
    }
    private boolean checkMove(int xPos, int yPos)
    {
        if(((0 > xPos) || (xPos > 7)) && ((0 > yPos) || (yPos > 7)))
        {
            System.out.println("X & Y Out of Bounds");
            return false;
        }

        else if((0 > xPos) || (xPos > 7))
        {
            System.out.println("X Out of Bounds");
            return false;
        }
        else if((0 > yPos) || (yPos > 7))
        {
            System.out.println("Y Out of Bounds");
            return false;
        }
        if(board[xPos][yPos] != boardSpace.EMPTY)
        {
            System.out.println("That space is not empty");
            return false;
        }
        boolean valid = false;
        switch (turn)
        {
            case BLACK:
                valid = checkBlack(xPos, yPos);
                break;
            case WHITE:
                valid = checkWhite(xPos, yPos);
                break;
        }
        if(valid)
        {
            return true;
        }
        System.out.println("Invalid Move Location");
        return false;
    }

    private void populate()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                board[j][i] = boardSpace.EMPTY;
            }
        }
        board[3][3] = boardSpace.BLACK;
        board[4][3] = boardSpace.WHITE;
        board[3][4] = boardSpace.WHITE;
        board[4][4] = boardSpace.BLACK;
    }
    public void testPrint()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                switch (board[j][i])
                {
                    case EMPTY:
                        System.out.print("| ");
                        break;
                    case BLACK:
                        System.out.print("B ");
                        break;
                    case WHITE:
                        System.out.print("W ");
                        break;
                }
            }
            System.out.println();
        }
    }
    private void setTakenBlack()
    {
        takenBlack.color = playerColor.BLACK;
        for(int i = 0; i < 8; i++)
        {
            ArrayList<Point> tempList = new ArrayList<>();
            takenBlack.takenPieces[i] = tempList;
        }
    }
    private boolean checkBlack(int xPos, int yPos)
    {
        /*location[] loc = new location[8];
        count = 0;
        int points = 0;
        for(int i = 0; i < 8; i++)
        {
            takenBlack.takenPieces[i].clear();
        }
        int xDiffPos = 0;
        int xDiffNeg = 0;
        int yDiffPos = 0;
        int yDiffNeg = 0;
        if(yPos == 0)
        {
            yDiffPos = 1;
        }
        else if(yPos == 7)
        {
            yDiffNeg = 1;
        }
        else
        {
            yDiffPos = 1;
            yDiffNeg = 1;
        }
        if(xPos == 0)
        {
            xDiffPos = 1;
        }
        else if(xPos == 7)
        {
            xDiffNeg = 1;
        }
        else
        {
            xDiffPos = 1;
            xDiffNeg = 1;
        }
        loc = possibleMoveChart(xPos, yPos, xDiffPos, xDiffNeg, yDiffPos, yDiffNeg);
        //count = loc.length;
        for(int i = 0; i < count; i++)
        {
            int x = loc[i].x;
            int y = loc[i].y;
            int valid = 0;
            Point p = new Point();
            p.x = x;
            p.y = y;
            takenBlack.takenPieces[i].add(p);
            while(valid == 0)
            {
                x += loc[i].dirX;
                y += loc[i].dirY;
                p.x = x;
                p.y = y;
                switch (board[x][y])
                {
                    case WHITE:
                        points++;
                        takenBlack.takenPieces[i].add(p);
                        break;
                    case BLACK:
                        valid = 1;
                        break;
                    case EMPTY:
                        valid = -1;
                        points = 0;
                        takenBlack.takenPieces[i].clear();
                        break;
                }
            }
            addPoints(points);
        }
        if(points > 0)
        {*/
            return true;
        /*}
        //TODO: CHECK VALIDITY
        //TODO: CHECK IF BETTER TO CREATE A FUNCTION TO GENERATE ALL POSSIBLE MOVES??
        return false;*/
    }
    private location[] possibleMoveChart(int xPos, int yPos, int xDiffPos, int xDiffNeg, int yDiffPos, int yDiffNeg)
    {
        count = 0;
        location[] loc = new location[8];
        for(int i = (yPos - Math.abs(yDiffNeg)); i < (yPos + Math.abs(yDiffPos)); i++)
        {
            for(int j = (xPos - Math.abs(xDiffNeg)); j < (xPos + Math.abs(xDiffPos)); j++)
            {
                if(j == (xPos) && i == (yPos))
                {
                    j++;
                }
                if(board[j][i] == boardSpace.WHITE)
                {
                    loc[count].x = j;
                    loc[count].y = i;
                    loc[count].dirX = (j - xPos);
                    loc[count].dirY = (i - yPos);
                    count++;
                }
            }
        }
        return loc;
    }
    private boolean checkWhite(int xPos, int yPos)
    {

        return false;
    }
    public boolean findValidMoves()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int xDiffPos = 0;
                int xDiffNeg = 0;
                int yDiffPos = 0;
                int yDiffNeg = 0;
                if(i == 0)
                {
                    yDiffPos = 1;
                }
                else if(i == 7)
                {
                    yDiffNeg = -1;
                }
                else
                {
                    yDiffPos = 1;
                    yDiffNeg = -1;
                }
                if(j == 0)
                {
                    xDiffPos = 1;
                }
                else if(j == 7)
                {
                    xDiffNeg = -1;
                }
                else
                {
                    xDiffPos = 1;
                    xDiffNeg = -1;
                }
                generatePossibilities(j, i, xDiffPos, xDiffNeg, yDiffPos, yDiffNeg, turn);
            }
        }
        return false;
    }
    public void generatePossibilities(int xPos, int yPos, int xDiffPos, int xDiffNeg, int yDiffPos, int yDiffNeg, playerColor turnTemp)
    {
        location loc[] = new location[8];
        System.out.print("(" + xPos + ", " + yPos + "): ");
        int yDir = 0;
        int xDir = 0;
        int count = 0;
        for(int i = (yPos + yDiffNeg); i < (yPos + yDiffPos + 1); i++) {
            if(i == yPos - 1)
            {
                yDir = -1;
            }
            else if(i == yPos + 1)
            {
                yDir = 1;
            }
            else
            {
                yDir = 0;
            }
            for (int j = (xPos + xDiffNeg); j < (xPos + xDiffPos + 1); j++) {
                if ((j != xPos) || (i != yPos)) {
                    //System.out.print(j + ", " + i + " | ");
                    if(j == xPos - 1)
                    {
                        xDir = -1;
                    }
                    else if(i == yPos + 1)
                    {
                        xDir = 1;
                    }
                    else
                    {
                        xDir = 0;
                    }
                    loc[count].x = j;
                    loc[count].y = i;
                    loc[count].dirX = xDir;
                    loc[count].dirY = yDir;
                    count++;
                }

            }
        }
        System.out.println("(" + xPos + ", " + yPos + "): ");
        for(int i = 0; i < loc.length; i++)
        {
            System.out.print(loc[count].x + ", " + loc[count].y +" ");
            System.out.print("dir X: " + loc[count].dirX + ", " + "dir y: " + loc[count].dirY);
            System.out.println();
            //TODO: PRINT LOC ARRAY
        }
        System.out.println();
    }
    private void addPoints(int points)
    {
        pointsTotalBlack += points;
    }
    public int getPoints(playerColor p)
    {
        if(p == playerColor.BLACK)
        {
            return pointsTotalBlack;
        }
        else
        {
            return pointsTotalWhite;
        }
    }
}