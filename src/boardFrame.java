import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class boardFrame extends JFrame
{
    JTextField pointsText;
    JTextField turnText;

    GamePanel panel;

    //Buttons that make up the GUI board
    JButton[][] squares;

    Container c;

    othelloBoard o = new othelloBoard();

    turn t = new turn();

    //color of the board
    Color green = new Color(0,200,75);

    public boardFrame()
    {
        panel = new GamePanel();
        add(panel, BorderLayout.CENTER);

        //text field that displays the current turn
        turnText = new JTextField();
        turnText.setEditable(false);
        add(turnText,BorderLayout.NORTH);

        //text field that displays the current points of each player
        pointsText = new JTextField();
        pointsText.setEditable(false);
        add(pointsText, BorderLayout.SOUTH);

        //sets up GUI w/ size, title etc
        setSize(600, 600);
        setResizable(false);
        setTitle("Reversi");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //calls the function that begins running the game
        t.start();
    }

    //accepts a text field and message and changes the text field to display the message
    public void changeText(JTextField text, String message, int location)
    {
        text = new JTextField(message);
        text.setEditable(false);
        if(location == 1)
        {
            add(text,BorderLayout.NORTH);
        }
        else
        {
            add(text,BorderLayout.SOUTH);
        }
    }

    class GamePanel extends JPanel implements MouseListener
    {
        //sets up the array of JButtons that makes up the board
        public GamePanel()
        {
            int tempCount = 0;
            c = getContentPane();
            setLayout(new GridLayout(8, 8));
            squares = new JButton[8][8];
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    squares[j][i] = new JButton();
                    squares[j][i].setBackground(green);
                    squares[j][i].addMouseListener(this);
                    add(squares[j][i], tempCount);
                    tempCount++;
                }
            }

        }

        //updates the GUI based on the current values in the othelloBoard classes board array
        public void printBoard()
        {
            int count = 0;
            char[][] testPrintArray = new char[8][8];
            //stores all of the values from the board array as characters in a local array
            //this method solves an error I was experiencing
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(testPrintArray[j][i] == 0)
                    {
                        if(o.getState(j, i) != othelloBoard.boardSpace.EMPTY)
                        {
                            if(o.getState(j, i) == othelloBoard.boardSpace.O) testPrintArray[j][i] = 'O';
                            else testPrintArray[j][i] = 'X';
                        }
                        else if(o.checkMove(j, i))
                        {
                            testPrintArray[j][i] = '*';
                        }
                        else testPrintArray[j][i] = '|';
                    }
                }
            }

            //translates the local array into buttons using a function call
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    switch (testPrintArray[j][i])
                    {
                        case 'X':
                            revealButton(count, "X", j, i);
                            break;
                        case 'O':
                            revealButton(count, "O", j, i);
                            break;
                        case '*':
                            revealButton(count, "*", j, i);
                            break;
                        case '|':
                            revealButton(count, "|", j, i);
                            break;
                    }
                    count++;
                }
            }
        }

        //changes the text and/or color of a button in the squares array (the JButton array that is the GUI board)
        public void revealButton (int index, String piece, int x, int y)
        {
            remove(squares[x][y]);
            Font f;
            f = new Font("Arial", Font.PLAIN, 30);
            if(piece == "O")
            {
                createButton(x, y, false, index, Color.BLACK, f, "O");
            }
            else if(piece == "X")
            {
                createButton(x, y, false, index, Color.WHITE, f, "X");
            }
            else if(piece == "*")
            {
                createButton(x, y, true, index, green, f, "*");
            }
            else
            {
                createButton(x, y, true, index, green, f, "");
            }
            setContentPane(c);
        }

        //creates and adds a JButton with a specific color, font, mouse listener state, and text
        public void createButton(int x, int y, boolean mouseLis, int index, Color color, Font f, String text)
        {
            squares[x][y] = new JButton(text);
            squares[x][y].setFont(f);
            squares[x][y].setBackground(color);
            if(mouseLis)
            {
                squares[x][y].addMouseListener(this);
            }
            else
            {
                squares[x][y].setEnabled(false);
            }
            add(squares[x][y], index);
        }

        //allows the player to make moves by clicking on the JButtons that make up the board
        public void mouseClicked(MouseEvent e)
        {
            //these calculations are made using the dimensions of the JButtons of the board
            int x = (e.getComponent().getX() - 1) / 73;
            int y = (e.getComponent().getY() - 1) / 65;
            if(o.movePiece(x, y) < 0)
            {
                JOptionPane.showMessageDialog(null, "That is an invalid move");
                t.run();
            }
            else
            {
                if(o.turn == othelloBoard.playerColor.O)
                {
                    if(x == 0 && y == 0)
                    {
                        o.oneOneO = true;
                    }
                    o.turn = othelloBoard.playerColor.X;
                }
                else
                {
                    if(x == 0 && y == 0)
                    {
                        o.oneOneX = true;
                    }
                    o.turn = othelloBoard.playerColor.O;
                }
                t.run();
            }
        }

        //as Mouse Listener is an interface, it is required that these methods are defined in this class
        //they are not used in this program so the functions are simply empty
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }

    public class turn
    {
        //runs once at the beginning of the game to initialize everything
        //after that, the run function takes care of running the game
        public void start()
        {
            o.build();
            run();
        }

        //based on whether or not the game is won, runs the necessary functions in order for a player to take their turn
        //if the game is won, then runs the function to display which player has won the game
        public void run()
        {
            boolean won = o.won();
            panel.printBoard();
            if(!won)
            {
                switch (o.turn)
                {
                    case O:
                        Turn("O");
                        break;
                    case X:
                        Turn("X");
                        break;
                }
            }

            //calls the function to display the winner based on point totals
            else
            {
                winDisplay();
            }
        }

        //if the game is won, an option pane is displayed with the player who won based on the total points at the end of the game
        public void winDisplay()
        {
            int OPoints = o.getPoints(othelloBoard.playerColor.O);
            int XPoints = o.getPoints(othelloBoard.playerColor.X);
            if(OPoints > XPoints)
            {
                JOptionPane.showMessageDialog(null, "O has won the game");
            }
            else if(XPoints > OPoints)
            {
                JOptionPane.showMessageDialog(null, "X has won the game");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "The game has resulted in a tie");
            }
        }

        //runs a player turn
        //accepts an argument that tells which player's turn it is
        public void Turn(String turn)
        {
            initTurn(turn);
            catchPassExcept();
        }

        //Opens a dialogue to check whether or not the player wants to pass their turn
        //has a try catch loop in case the player pressed cancel or closes the dialogue to prompt them again
        public void catchPassExcept()
        {
            String pass = passTurn();
            boolean noExcept = false;
            while(!noExcept) //this will keep running until the player provides a valid input of Yes or No
            {
                try
                {
                    if(pass.equals("Yes"))
                    {
                        o.turn = othelloBoard.playerColor.X;
                        t.run();
                    }
                    noExcept = true;
                }
                catch (NullPointerException e) //in case the player pressed cancel or closes the dialogue
                {
                    JOptionPane.showMessageDialog(null, "You must choose whether or not to pass your turn.");
                    pass = passTurn(); //reprompts the player
                }
            }
        }

        //returns the player input on whether or not they want to pass their turn
        public String passTurn()
        {
            String passTurn[] = {"No", "Yes"};
            String pass;
            pass = (String) JOptionPane.showInputDialog(null,
                    "Do you want to pass your turn?", "Pass Turn",
                    JOptionPane.QUESTION_MESSAGE, null, passTurn, passTurn[0]);
            return pass; //returning the player input of Yes, No, or null
        }

        //sets up everything for a player to take their turn
        public void initTurn(String turn)
        {
            //clears and inits the object that stores the potential moves that can be made
            o.initPotMoves();

            //changes the text indicating who's turn it is
            changeText(turnText, "Player " + turn + "'s Turn", 1);

            //updates point values
            int points = o.getPoints(o.turn);
            int oppPoints = o.getPoints(othelloBoard.playerColor.X);
            String pointsMessage = "O Points: " + points + " || X Points: " + oppPoints;
            changeText(pointsText, pointsMessage, 0);

            //finds valid moves on the current board and then updates the board state
            o.findValidMoves(o.turn);
            panel.printBoard();
        }
    }
}