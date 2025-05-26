package GUI;

import game_logic.Board;
import loginmodel.LoginSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class Login extends JFrame {
    private String ID;
    private String PASSWORD;
    public static LoginSystem loginSystem = new LoginSystem();
    private static Board b = new Board();
    public static boolean IsVisitor;
    protected static SelectLevel selectLevel;



    public static Board getB() {
        return b;
    }

    public void setB(Board b) {
        this.b = b;
    }

    public static void setLoginSystem(LoginSystem loginSystem) {
        Login.loginSystem = loginSystem;
    }

    public LoginSystem getLoginSystem() {
        return loginSystem;
    }

    public String getID() {
        return ID;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public static SelectLevel getSelectLevel() {
        return selectLevel;
    }

    public Login(){


        JTextField id = new JTextField(10);
        JTextField password = new JTextField(10);
        JButton login = new JButton("登陆");
        JButton visitor = new JButton("游客模式");


        setTitle("三国华容道");
        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GridBagLayout gbLayout = new GridBagLayout();
        JPanel panel = new JPanel(gbLayout);
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.insets = new Insets(10,10,10,10);

        //登陆：
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        panel.add(new JLabel("账号："),gbConstraints);

        gbConstraints.gridx = 1;
        panel.add(id,gbConstraints);

        gbConstraints.gridx = 0;
        gbConstraints.gridy = 1;
        panel.add(new JLabel("密码："),gbConstraints);

        gbConstraints.gridx = 1;
        panel.add(password,gbConstraints);

        gbConstraints.gridx = 0;
        gbConstraints.gridy = 2;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(login,gbConstraints);

        gbConstraints.gridx = 1;
        gbConstraints.gridy = 2;
        panel.add(visitor,gbConstraints);

        visitor.addActionListener(new ActionListener() {//游客模式的相应
            @Override
            public void actionPerformed(ActionEvent e) {
                IsVisitor = true;
                ID = "Administer";
                PASSWORD = "6";

                loginSystem.setPassword(PASSWORD);
                loginSystem.setUsername(ID);

                JOptionPane.showMessageDialog(panel, "游客模式将不会记录数据！");
                b.setLoginSystem(loginSystem);
                dispose();

                GameBoard gameBoard = null;
                gameBoard = new GameBoard(b,IsVisitor);
                gameBoard.setVisible(true);
            }
        });

        login.addActionListener(new ActionListener() {//登陆键的响应
            @Override
            public void actionPerformed(ActionEvent e) {
                IsVisitor = false;
                ID = id.getText();
                PASSWORD = password.getText();


                if(ID == null || ID.isEmpty() || PASSWORD == null || PASSWORD.isEmpty()){
                    JOptionPane.showMessageDialog(panel,"账号密码不能为空！");
                    return;
                }

                loginSystem.setPassword(PASSWORD);
                loginSystem.setUsername(ID);

                while (!loginSystem.authenticateUser()) {
                    JOptionPane.showMessageDialog(panel, "账号或密码错误，请重试！");
                    return;
                }
                JOptionPane.showMessageDialog(panel, "登陆成功！");
                b.setLoginSystem(loginSystem);

                if(loginSystem.loginStatus == 1) {
                    try {
                        loginSystem.readdata(b);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(panel,"读取游戏数据失败！");
                        ex.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(panel, "读取游戏数据中......");
                if(loginSystem.ReadError){
                    JOptionPane.showMessageDialog(panel,"数据损坏，已创建新游戏！");
                }
                dispose();
                selectLevel = new SelectLevel();
                selectLevel.setVisible(true);
            }
        });



        this.add(panel);
    }

    public static class images {
        public static final String folder_PATH = System.getProperty("user.home") + "\\Desktop\\Klotski puzzle 5.12\\";
        public static Image bg = Toolkit.getDefaultToolkit().getImage(folder_PATH+"1.jpg");
    }
}
