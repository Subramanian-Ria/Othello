import java.awt.*;

public class runGame {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                boardFrame frame = new boardFrame();
            }
        });
    }
}
