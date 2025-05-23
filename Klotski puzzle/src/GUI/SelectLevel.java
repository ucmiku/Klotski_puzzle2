package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Random;

import game_logic.Board;
import game_logic.Boards;
import loginmodel.LoginSystem;

public class SelectLevel extends JFrame {
    public static int level;
    public static int load=0;
    private JButton l1 = new JButton("缺兵少两");
    private JButton l2 = new JButton("移形换影");
    private JButton l3 = new JButton("科技革命");
    private JButton l5 = new JButton("坐观天下");
    public static JButton l4 = new JButton("继续游戏");
    private Boards boards = new Boards();

    JPanel GamePanel;
    public SelectLevel(){
        GamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponents(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(images.select, 0, 0, getWidth(), getHeight(), this);
            }
        };
        GridBagLayout gbLayout = new GridBagLayout();
        GamePanel.setLayout(gbLayout);
        GamePanel.setBounds(0, 0, 550, 250);
        setContentPane(GamePanel);

        setTitle("选关界面");
        setSize(550,250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.insets = new Insets(10,10,10,10);

        gbConstraints.gridx = 0;
        gbConstraints.gridy = 3;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        GamePanel.add(l1,gbConstraints);

        gbConstraints.gridx = 1;
        gbConstraints.gridy = 3;
        GamePanel.add(l2,gbConstraints);

        gbConstraints.gridx = 2;
        gbConstraints.gridy = 3;
        GamePanel.add(l3,gbConstraints);

        gbConstraints.gridx = 3;
        gbConstraints.gridy = 3;
        GamePanel.add(l5,gbConstraints);

        l1.addActionListener(e -> {
            level=1;
            dispose();
            GameBoard gameBoard = null;
            Board b = new Board();
            b.setLoginSystem(Login.loginSystem);
            gameBoard = new GameBoard(b,Login.IsVisitor);
            GameBoard.seconds = 300;
            gameBoard.setVisible(true);
        });
        l2.addActionListener(e -> {
            level=2;
            dispose();
            GameBoard gameBoard = null;
            Random rand = new Random();
            Board b = boards.boards[rand.nextInt(5)];
            b.setLoginSystem(Login.loginSystem);
            gameBoard = new GameBoard(b,Login.IsVisitor);
            GameBoard.seconds = 300;
            gameBoard.setVisible(true);
        });
        l3.addActionListener(e -> {
            level=3;
            dispose();
            GameBoard gameBoard = null;
            Board b = new Board();
            b.setLoginSystem(Login.loginSystem);
            gameBoard = new GameBoard(b,Login.IsVisitor);
            GameBoard.seconds = 300;
            gameBoard.setVisible(true);
        });
        l5.addActionListener(e -> {
            dispose();
            listenboard listenboard=null;
            Board b = new Board();
            b.setLoginSystem(Login.loginSystem);
            listenboard = new listenboard(b,Login.IsVisitor);
            listenboard.setVisible(true);
        });
        if(Login.loginSystem.loginStatus == 1){
            gbConstraints.gridx = 4;
            gbConstraints.gridy = 3;
            GamePanel.add(l4,gbConstraints);

            l4.addActionListener(e -> {
                load=1;
                dispose();
                GameBoard gameBoard = null;
                if(LoginSystem.level == 3||LoginSystem.level==4){
                    level=4;
                }
                gameBoard = new GameBoard(Login.getB(),Login.IsVisitor);
                gameBoard.setVisible(true);
            });
        }
    }

    public Boards getBoards() {
        return boards;
    }
}
