import java.util.Scanner;

public class turn extends othelloBoard{
    public void run()
    {
        build();
        while(!won())
        {
            switch (turn)
            {
                case BLACK:
                    blackTurn();
                    break;
                case WHITE:
                    whiteTurn();
                    break;
            }
            testPrint();
        }
    }
    Scanner sc = new Scanner(System.in);
    private void whiteTurn()
    {
        System.out.println("White turn");
        System.out.println("Enter X");
        int xPos = sc.nextInt() - 1;
        System.out.println(xPos + 1);
        System.out.println("Enter Y");
        int yPos = sc.nextInt() - 1;
        System.out.println(yPos + 1);
        if(move(xPos, yPos) < 0)
        {
            whiteTurn();
        }
        else
        {
            turn = playerColor.BLACK;
        }
    }
    private void blackTurn()
    {
        System.out.println("Black turn");
        System.out.println("Enter X");
        int xPos = sc.nextInt() - 1;
        System.out.println(xPos + 1);
        System.out.println("Enter Y");
        int yPos = sc.nextInt() - 1;
        System.out.println(yPos + 1);
        if(move(xPos, yPos) < 0)
        {
            blackTurn();
        }
        else
        {
            turn = playerColor.WHITE;
        }
    }
}
