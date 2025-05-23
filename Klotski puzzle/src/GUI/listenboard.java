// listenboard.java（简化版观看界面）
package GUI;

import game_logic.Block;
import game_logic.Board;
import loginmodel.LoginSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class listenboard extends JFrame {
    private JPanel GamePanel;
    private BlockButton[] blocks = new BlockButton[10]; // 假设最多10个方块
    private PrintWriter playerWriter;
    private static Board board;
    private BlockButton selectedButton;
    private JButton[] moveButton = new JButton[4];
    private ArrayList<BlockButton> Characters = new ArrayList<>();
    public static ArrayList<tool> Tools = new ArrayList<>();
    private Image backgroundImage=images.backboard;
    private Image chessboardImage = images.chessboardImage;

    public static int seconds; // 时间
    public static int seconds1;
    public Timer playtime;
    private JLabel timeLabel;
    private JLabel timeLabel2;
    private boolean isRunning = false;

    JPanel BoardPanel = new JPanel(null);
    JPanel MovePanel = new JPanel(null);
    JPanel ChessBoard = new JPanel(null);
    String[] name = {"←","→","↑","↓"};

    public listenboard(Board b, boolean isVisitor) {
        setTitle("在线观看");
        setSize(750, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        board = b;
        // 创建主游戏面板（带背景）
        GamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        GamePanel.setLayout(null);
        GamePanel.setBounds(0, 0, 750, 450);
        setContentPane(GamePanel);

        // 创建棋盘面板
        ChessBoard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(chessboardImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        ChessBoard.setLayout(null);
        ChessBoard.setBounds(45, 47, 270, 330);

        // 初始化组件
        JLabel tips = new JLabel("请用方向键或点击按钮控制方块移动！");
        ClosingFrame closingPanel = new ClosingFrame();

        BoardPanel.setBounds(0, 10, 300, 360);
        BoardPanel.setOpaque(false);
        GamePanel.add(BoardPanel);

        MovePanel.setBounds(400,50,150,150);
        MovePanel.setOpaque(false);

        if(SelectLevel.level==3||SelectLevel.level==4){
            addToolBlock("Clock");
            addToolBlock("Hammer");
        }


        GamePanel.add(MovePanel);
        MovePanel.setVisible(true);

        setTitle("三国华容道");
        setSize(new Dimension(750, 450));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // 时间标签
        timeLabel = new JLabel("00:00");
        timeLabel.setBounds(330, 180, 150, 30);
        timeLabel.setFont(new Font("SimSun", Font.BOLD, 30));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setOpaque(false);
        timeLabel2 = new JLabel("00:00");
        timeLabel2.setBounds(332, 182, 150, 30);
        timeLabel2.setFont(new Font("SimSun", Font.BOLD, 31));
        timeLabel2.setForeground(Color.BLACK);
        timeLabel2.setOpaque(false);
        GamePanel.add(timeLabel);
        GamePanel.add(timeLabel2);

        if (SelectLevel.level==1){ //关卡1：随机删除一个除了曹操以外的方块
            Random rand = new Random();
            int index = rand.nextInt(8) + 1;
            for(int i = board.blocks[index].getY_cordinate();i <= board.blocks[index].getY_cordinate() + board.blocks[index].getY_length() - 1;i++)
                for(int j = board.blocks[index].getX_cordinate();j <= board.blocks[index].getX_cordinate() + board.blocks[index].getX_length() - 1;j++){
                    board.changeIs_available(i,j,true);
                }
            board.blocks[index].setX_cordinate(0);
            board.blocks[index].setY_cordinate(0);
            Characters.get(index).setLocation(0,0);
            Characters.get(index).setVisible(false);
        }

        // 添加棋子块
        for (int i = 0; i < 10; i++) {
            addChessBlock(board.blocks[i].getName(),
                    board.blocks[i].getX_length(),
                    board.blocks[i].getY_length(),
                    board.blocks[i].getX_cordinate(),
                    board.blocks[i].getY_cordinate());
        }

        // 连接服务器作为观众
        new Thread(this::connectAsViewer).start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 关闭连接
                dispose();
            }
        });
        playtime = new Timer(1000, e -> {
            if (isRunning) {
                seconds--;
                seconds1++;
                if (seconds <= 0) {
                    dispose();
                    losepanel panel = new losepanel();
                    panel.addjpg();
                    pauseGameTimer();
                }
                updateTimeLabel();
            }
        });

        startGameTimer();
        BoardPanel.setFocusable(true);
        BoardPanel.requestFocus();
        GamePanel.add(ChessBoard);
        ChessBoard.setVisible(true);
    }

    private void connectAsViewer() {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // 发送角色标识为观众（服务器端可忽略
            new PrintWriter(socket.getOutputStream(), true).println("viewer");

            String move;
            while ((move = reader.readLine()) != null) {
                System.out.println(move);
                String[] parts = move.split(",");
                if (parts.length != 2) continue;

                String characterName = parts[0];
                char direction = parts[1].charAt(0);


                for (Block block : board.blocks) {
                    for(BlockButton a:Characters){
                    if (block.getName().equals(characterName)) {
                        if (block.getName().equals(a.getName())) {
                        board.movement(direction, block);
                        if(block.getX_cordinate() * 60 != a.getX() || block.getY_cordinate() * 60 != a.getY())animateMove(a,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(a,block.getX_cordinate() * 60,block.getY_cordinate() * 60,direction);
                        if (board.isVictory()) {
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            dispose();
                        }}
                        break;
                    }}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addChessBlock(String name, int width, int height, int x, int y) {
        BlockButton button = new BlockButton(width * 60, height * 60, false, name);
        button.setBounds(0,0,width * 60,height * 60);
        Characters.add(button);
        if(x == 0 && y == 0)Characters.getLast().setVisible(false);
        animateMove(button,x * 60,y * 60);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedButton != null) {
                    selectedButton.setSelected(false);
                }
                selectedButton = (selectedButton != button) ? button : null;
                if (selectedButton != null) {
                    selectedButton.setSelected(true);
                }
            }
        });

        GamePanel.add(button);
    }

    private void addToolBlock(String name) {
        tool button = new tool(60,60,name);
        button.setBounds(570,200 + 70 * tool.i,60,60);
        Tools.add(button);
        if(tool.i==1&&SelectLevel.level==4){
            button.setUsed(LoginSystem.tool1 != 1);
        }else if(tool.i==2&&SelectLevel.level==4){
            button.setUsed(LoginSystem.tool2 != 1);
        }
        System.out.println(tool.i);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GamePanel.requestFocus();
                if(!button.isUsed()){
                    switch (name){
                        case "Clock" -> {
                            button.setUsed(true);
                            ToolClock();
                            button.repaint();
                        }
                        case "Hammer" -> {
                            button.setUsed(true);
                            deleteZu();
                            button.repaint();
                        }
                    }
                }
                BoardPanel.requestFocus();
            }
        });

        GamePanel.add(button);
    }

    private void ToolClock(){
        pauseGameTimer();
        plusGameTimer();
    }

    private void deleteZu(){
        Random rand = new Random();
        int index = rand.nextInt(4) + 6;
        board.changeIs_available(board.blocks[index].getY_cordinate(),board.blocks[index].getX_cordinate(),true);
        board.blocks[index].setX_cordinate(0);
        board.blocks[index].setY_cordinate(0);
        Characters.get(index).setLocation(0,0);
        Characters.get(index).setVisible(false);
    }

    public void updateTimeLabel() {
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, secs));
        timeLabel2.setText(String.format("%02d:%02d", minutes, secs));
    }

    private void restartGameTimer(){
        seconds = 300;
        isRunning = true;
        playtime.start();
        updateTimeLabel();
    }

    private void plusGameTimer(){
        Random rand = new Random();
        seconds += rand.nextInt(150) + 30;
        isRunning = true;
        playtime.start();
        updateTimeLabel();
    }

    private void startGameTimer() {
        seconds = (board.getLoginSystem().loginStatus == 1) ?
                board.getLoginSystem().time : 300;
        isRunning = true;
        playtime.start();
        updateTimeLabel();
    }

    private void pauseGameTimer() {
        isRunning = false;
        playtime.stop();
    }

    public static Board getBoard() {
        return board;
    }

    //移动动画实现
    private void animateMove(BlockButton button,int finalX,int finalY){

        final int startX = button.getX();
        final int startY = button.getY();
        final int animationSteps = 10;
        final int animationDelay = 4;

        //总移动距离
        int totalXmove = finalX - startX;
        int totalYmove = finalY - startY;


        new Timer(animationDelay, new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int x;
                int y;
                float progress = easeOutQuad((float)++currentStep / animationSteps);
                x = startX + (int)(totalXmove * progress);
                y = startY + (int)(totalYmove * progress);
                button.setLocation(x,y);
                BoardPanel.repaint();
                if(currentStep >= animationSteps){
                    button.setLocation(finalX,finalY);
                    ((Timer)e.getSource()).stop();
                    BoardPanel.repaint();
                }
            }
        }).start();
    }

    private void animateMoveSlow(BlockButton button,int finalX,int finalY){

        final int startX = button.getX();
        final int startY = button.getY();
        final int animationSteps = 10;
        final int animationDelay = 40;

        //总移动距离
        int totalXmove = finalX - startX;
        int totalYmove = finalY - startY;

        new Timer(animationDelay, new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int x;
                int y;
                float progress = easeOutQuad((float)++currentStep / animationSteps);
                x = startX + (int)(totalXmove * progress);
                y = startY + (int)(totalYmove * progress);
                button.setLocation(x,y);
                BoardPanel.repaint();
                if(currentStep >= animationSteps){
                    button.setLocation(finalX,finalY);
                    ((Timer)e.getSource()).stop();
                    BoardPanel.repaint();
                }
            }
        }).start();
    }

    private void animateMove2(BlockButton button,int finalX,int finalY,char direction){
        final int startX = button.getX();
        final int startY = button.getY();
        final int animationSteps = 10;
        final int animationDelay = 6;

        int dx = 0;
        int dy = 0;//实现对边缘碰撞发生震动的小动画

        switch (direction){
            case 'u' -> dy = -5;
            case 'r' -> dx = 5;
            case 'd' -> dy = 5;
            case 'l' -> dx = -5;
        }

        //总移动距离
        int totalXmove = finalX + dx - startX;
        int totalYmove = finalY + dy- startY;


        new Timer(animationDelay, new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int x;
                int y;
                float progress = easeOutQuad((float)++currentStep / animationSteps);
                x = startX + (int)(totalXmove * progress);
                y = startY + (int)(totalYmove * progress);
                button.setLocation(x,y);
                BoardPanel.repaint();
                if(currentStep >= animationSteps){
                    button.setLocation(finalX,finalY);
                    ((Timer)e.getSource()).stop();
                    BoardPanel.repaint();
                }
            }
        }).start();
    }

    private float easeOutQuad(float progress) {
        return 1 - (1 - progress) * (1 - progress);
    }
}