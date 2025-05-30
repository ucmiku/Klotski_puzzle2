package GUI;

import game_logic.AI;
import game_logic.Board;
import game_logic.Block;
import game_logic.Boards;
import loginmodel.LoginSystem;
import music.music;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends JFrame {
    //在线观看
    private ServerSocket serverSocket;
    private ArrayList<PrintWriter> clientWriters = new ArrayList<>();
    //展示的board
    private static Board board;
    //各个按钮
    private BlockButton selectedButton;
    private JButton[] moveButton = new JButton[4];
    private JButton loadgame = new JButton("加载游戏");
    private JButton restartgame = new JButton("重新开始");
    private JButton saveGame = new JButton("保存数据");
    private JButton withdraw = new JButton("撤回");
    private JButton AutoSolution = new JButton("召唤神力");
    private JButton BacktoSelect = new JButton("返回选关");
    private ArrayList<BlockButton> Characters = new ArrayList<>();
    public static ArrayList<tool> Tools = new ArrayList<>();
    //背景初始化
    private Image backgroundImage=images.backboard;
    private Image chessboardImage = images.chessboardImage;

    public static int seconds; // 时间
    public static int seconds1;
    public Timer playtime;
    private JLabel timeLabel;
    private JLabel timeLabel2;
    private boolean isRunning = false;

    //panel初始化
    JPanel BoardPanel = new JPanel(null);
    JPanel GamePanel;
    JPanel MovePanel = new JPanel(null);
    JPanel ChessBoard = new JPanel(null);
    String[] name = {"←","→","↑","↓"};

    public GameBoard(Board b, boolean IsVisitor)  {
        startServer();
        board = b;
        if(IsVisitor)BacktoSelect.setName("退出游戏");
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

        //移动方向键的初始化
        for(int i = 0;i < 4;i++){
            moveButton[i] = new JButton(name[i]);
            moveButton[i].setFont(new Font("SimSun", Font.BOLD,15));
            moveButton[i].setForeground(Color.BLUE);
        }

        moveButton[0].setBounds(0,50,50,50);
        moveButton[1].setBounds(100,50,50,50);
        moveButton[2].setBounds(50,0,50,50);
        moveButton[3].setBounds(50,100,50,50);

        //道具组件
        AutoSolution.setBounds(570, 110, 100, 50);
        AutoSolution.setForeground(Color.BLUE);

        if(SelectLevel.level==3||SelectLevel.level==4){
            addToolBlock("Clock");
            addToolBlock("Hammer");
            GamePanel.add(AutoSolution);
        }

        for(JButton jButton : moveButton){
            MovePanel.add(jButton);
        }

        GamePanel.add(MovePanel);
        MovePanel.setVisible(true);

        setTitle("三国华容道 - 服务器");
        setSize(new Dimension(750, 450));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置按钮
        saveGame.setBounds(330, 270, 100, 50);
        saveGame.setForeground(Color.BLUE);
        if (!IsVisitor) GamePanel.add(saveGame);

        withdraw.setBounds(450, 270, 100, 50);
        withdraw.setForeground(Color.BLUE);
        GamePanel.add(withdraw);

        restartgame.setBounds(450,340,100,50);
        restartgame.setForeground(Color.BLUE);
        GamePanel.add(restartgame);

        loadgame.setBounds(330,340,100,50);
        loadgame.setForeground(Color.BLUE);
        if (!IsVisitor) GamePanel.add(loadgame);

        BacktoSelect.setBounds(570,50,100,50);
        BacktoSelect.setForeground(Color.BLUE);
        GamePanel.add(BacktoSelect);

        // 提示标签
        tips.setBounds(330, 230, 400, 30);
        tips.setForeground(Color.WHITE);
        tips.setFont(new Font("宋体", Font.BOLD, 15));
        tips.setOpaque(false);
        GamePanel.add(tips);

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

        // 添加棋子块
        for (int i = 0; i < 10; i++) {
            addChessBlock(board.blocks[i].getName(),
                    board.blocks[i].getX_length(),
                    board.blocks[i].getY_length(),
                    board.blocks[i].getX_cordinate(),
                    board.blocks[i].getY_cordinate());
        }

        //关卡1：随机删除一个除了曹操以外的方块
        if (SelectLevel.level==1 && !SelectLevel.isL4){
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

        // 按钮事件监听
        withdraw.addActionListener(e -> {
            if (!board.getProcess().isEmpty()) {
                if(!board.withdraw()){
                    try {
                        b.getLoginSystem().reread(board);
                        if(b.getLoginSystem().ReadError){
                            JOptionPane.showMessageDialog(GamePanel,"数据损坏，已创建新游戏！");
                            for(int i = 0;i < 10 ;i++){
                                if(Characters.get(i).getX() != board.blocks[i].getX_cordinate() * 60 || Characters.get(i).getY() != board.blocks[i].getY_cordinate() * 60)animateMove(Characters.get(i),board.blocks[i].getX_cordinate() * 60,board.blocks[i].getY_cordinate() * 60);
                            }
                            restartGameTimer();
                            BoardPanel.requestFocus();
                            return;
                        }
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                for (BlockButton b1 : Characters) {
                    if (b1.getName().equals(board.getWithdrawName())) {
                        animateMove(b1,board.blocks[board.getWithdrawBlockNumber()].getX_cordinate() * 60,
                                board.blocks[board.getWithdrawBlockNumber()].getY_cordinate() * 60);
                    }
                }
                BoardPanel.repaint();
                broadcastMove("w"); // 广播撤回操作
            } else {
                JOptionPane.showMessageDialog(GamePanel, "无可撤回的步数！");
            }
            BoardPanel.requestFocus();
        });

        saveGame.addActionListener(e -> {
            b.getLoginSystem().save(board.getcordinate(), board.getProcess());
            JOptionPane.showMessageDialog(GamePanel, "已保存游戏记录！");
            SelectLevel.l4.setVisible(true);
            BoardPanel.requestFocus();
        });

        loadgame.addActionListener(e -> {
            try {
                pauseGameTimer();
                board = b.getLoginSystem().readdata(b);
                for(int i = 0;i < 10 ;i++){
                    if(board.blocks[i].getX_cordinate() == 0)Characters.get(i).setVisible(false);
                    if(Characters.get(i).getX() != board.blocks[i].getX_cordinate() * 60 || Characters.get(i).getY() != board.blocks[i].getY_cordinate() * 60)animateMove(Characters.get(i),board.blocks[i].getX_cordinate() * 60,board.blocks[i].getY_cordinate() * 60);                    repaint();
                }
                startGameTimer();
                updateTimeLabel();
                if(SelectLevel.level == 3||SelectLevel.level == 4){
                    Tools.get(0).setUsed(b.getLoginSystem().tool1 == 0);
                    Tools.get(1).setUsed(b.getLoginSystem().tool2 == 0);
                }

                broadcast("load");// 广播游戏状态
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            BoardPanel.requestFocus();
        });

        restartgame.addActionListener(e -> {
            pauseGameTimer();
            board = new Board();


            for(tool t : Tools) {
                t.setUsed(false);
            }

            int index=0;
            if(SelectLevel.level == 2){
                Random rand = new Random();
                index = rand.nextInt(5);
                board = new Boards().boards[index];
            }

            for(int i = 0;i < 10 ;i++){
                Characters.get(i).setVisible(true);
                animateMove(Characters.get(i),board.blocks[i].getX_cordinate() * 60,board.blocks[i].getY_cordinate() * 60);
            }

            if(SelectLevel.level == 1){
                Random rand = new Random();
                index = rand.nextInt(8) + 1;
                for(int i = board.blocks[index].getY_cordinate();i <= board.blocks[index].getY_cordinate() + board.blocks[index].getY_length() - 1;i++)
                    for(int j = board.blocks[index].getX_cordinate();j <= board.blocks[index].getX_cordinate() + board.blocks[index].getX_length() - 1;j++){
                        board.changeIs_available(i,j,true);
                    }
                board.blocks[index].setX_cordinate(0);
                board.blocks[index].setY_cordinate(0);
                Characters.get(index).setLocation(0,0);
                Characters.get(index).setVisible(false);
            }

            restartGameTimer();
            broadcast("r," + index);
            BoardPanel.requestFocus();
        });

        AutoSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> str = (ArrayList<String>) AI.solve(board);
                str.removeLast();
                Timer solutionTimer = new Timer(300, new ActionListener() {
                    private int currentStep = 0;

                    @Override
                    public void actionPerformed(ActionEvent timerEvent) {
                        if (currentStep >= str.size()) {
                            ((Timer)timerEvent.getSource()).stop();
                            return;
                        }

                        String solution = str.get(currentStep++);
                        char[] c = solution.toCharArray();
                        StringBuilder sb = new StringBuilder();
                        for(char c1 : c){
                            if(c1 == ',') break;
                            sb.append(c1);
                        }
                        String characterName = sb.toString();
                        char direction = solution.charAt(solution.length() - 1);

                        for (int i = 0; i < 10; i++) {
                            if (Characters.get(i).getName().equals(characterName)) {
                                board.movement(direction, board.blocks[i]);
                                animateMoveSlow(Characters.get(i),
                                        board.blocks[i].getX_cordinate() * 60,
                                        board.blocks[i].getY_cordinate() * 60);
                                broadcastMove(characterName + "," + direction); // 广播移动
                                break;
                            }
                        }

                        BoardPanel.repaint();
                        BoardPanel.requestFocus();
                    }
                });

                solutionTimer.start();
                BoardPanel.requestFocus();
            }
        });

        BacktoSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseGameTimer();
                if (!IsVisitor) {
                    closingPanel.setVisible(true);
                    closingPanel.Yes.addActionListener(ev -> {
                        b.getLoginSystem().save(board.getcordinate(), board.getProcess());
                        SelectLevel.l4.setVisible(true);
                        BlockButton.i = 0;
                        tool.i = 0;
                        closingPanel.setVisible(false);
                        dispose();
                        closeNetworkResources();
                        Login.selectLevel.setVisible(true);
                    });
                    closingPanel.No.addActionListener(ev -> {
                        BlockButton.i = 0;
                        tool.i = 0;
                        Login.loginSystem.save(new Board().getcordinate(),new Board().getProcess());
                        SelectLevel.l4.setVisible(false);
                        dispose();
                        closeNetworkResources();
                        closingPanel.setVisible(false);
                        Login.selectLevel.setVisible(true);
                    });
                }
                else {
                    BlockButton.i = 0;
                    tool.i = 0;
                    closingPanel.setVisible(false);
                    dispose();
                    closeNetworkResources();
                    System.exit(0);
                }
                BoardPanel.requestFocus();
            }
        });

        //左方向键
        moveButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedButton == null){
                    JOptionPane.showMessageDialog(GamePanel,"请选择一个方块！");
                    return;
                }
                for (Block block : board.blocks) {
                    if (block.getName().equals(selectedButton.getName())) {
                        board.movement('l', block);
                        if(block.getX_cordinate() * 60 != selectedButton.getX() || block.getY_cordinate() * 60 != selectedButton.getY())animateMove(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60,'l');
                        broadcastMove(selectedButton.getName() + ",l"); // 广播移动
                        if (board.isVictory()) {
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            broadcastVictory(); // 广播胜利
                            dispose();
                        }
                        break;
                    }
                }
                BoardPanel.requestFocus();
            }
        });

        //右方向键
        moveButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedButton == null){
                    JOptionPane.showMessageDialog(GamePanel,"请选择一个方块！");
                    return;
                }
                for (Block block : board.blocks) {
                    if (block.getName().equals(selectedButton.getName())) {
                        board.movement('r', block);
                        if(block.getX_cordinate() * 60 != selectedButton.getX() || block.getY_cordinate() * 60 != selectedButton.getY())animateMove(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60,'r');
                        broadcastMove(selectedButton.getName() + ",r"); // 广播移动
                        if (board.isVictory()) {
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            broadcastVictory(); // 广播胜利
                            dispose();
                        }
                        break;
                    }
                }
                BoardPanel.requestFocus();
            }
        });

        //上方向键
        moveButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedButton == null){
                    JOptionPane.showMessageDialog(GamePanel,"请选择一个方块！");
                    return;
                }
                for (Block block : board.blocks) {
                    if (block.getName().equals(selectedButton.getName())) {
                        board.movement('u', block);
                        if(block.getX_cordinate() * 60 != selectedButton.getX() || block.getY_cordinate() * 60 != selectedButton.getY())animateMove(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60,'u');
                        broadcastMove(selectedButton.getName() + ",u"); // 广播移动
                        if (board.isVictory()) {
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            broadcastVictory(); // 广播胜利
                            dispose();
                        }
                        break;
                    }
                }
                BoardPanel.requestFocus();
            }
        });

        //下方向键
        moveButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedButton == null){
                    JOptionPane.showMessageDialog(GamePanel,"请选择一个方块！");
                    return;
                }
                for (Block block : board.blocks) {
                    if (block.getName().equals(selectedButton.getName())) {
                        board.movement('d', block);
                        if(block.getX_cordinate() * 60 != selectedButton.getX() || block.getY_cordinate() * 60 != selectedButton.getY())animateMove(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60,'d');
                        broadcastMove(selectedButton.getName() + ",d"); // 广播移动
                        if (board.isVictory()) {
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            broadcastVictory(); // 广播胜利
                            dispose();
                        }
                        break;
                    }
                }
                BoardPanel.requestFocus();
            }
        });

        // 键盘监听
        BoardPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                music.playsound();
                if (selectedButton == null) return;

                char c;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> c = 'u';
                    case KeyEvent.VK_DOWN -> c = 'd';
                    case KeyEvent.VK_LEFT -> c = 'l';
                    case KeyEvent.VK_RIGHT -> c = 'r';
                    default -> { return; }
                }
                music.playsound();
                for (Block block : board.blocks) {
                    if (block.getName().equals(selectedButton.getName())) {
                        board.movement(c, block);
                        broadcastMove(selectedButton.getName() + "," + c); // 广播移动
                        if(block.getX_cordinate() * 60 != selectedButton.getX() || block.getY_cordinate() * 60 != selectedButton.getY())animateMove(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60);
                        else animateMove2(selectedButton,block.getX_cordinate() * 60,block.getY_cordinate() * 60,c);
                        if (board.isVictory()) {
                            tool.i = 0;
                            BlockButton.i = 0;
                            winpanel frame = new winpanel();
                            frame.addjpg();
                            pauseGameTimer();
                            broadcastVictory(); // 广播胜利
                            dispose();
                            closeNetworkResources();
                        }
                        break;
                    }
                }
            }
        });

        // 窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (IsVisitor) System.exit(0);
                closingPanel.setVisible(true);
                closingPanel.Yes.addActionListener(ev -> {
                    b.getLoginSystem().save(board.getcordinate(), board.getProcess());
                    SelectLevel.l4.setVisible(true);
                    System.exit(0);
                });
                closingPanel.No.addActionListener(ev -> {
                    closingPanel.setVisible(false);
                    System.exit(0);
                });
            }
        });

        // 计时器设置
        playtime = new Timer(1000, e -> {
            if (isRunning) {
                seconds--;
                seconds1++;
                if (seconds <= 0) {
                    dispose();
                    losepanel panel = new losepanel();
                    closeNetworkResources();
                    panel.addjpg();
                    pauseGameTimer();
                    broadcastDefeat(); // 广播失败
                }
                updateTimeLabel();
                broadcastTime();
            }
        });

        startGameTimer();
        BoardPanel.setFocusable(true);
        BoardPanel.requestFocus();
        GamePanel.add(ChessBoard);
        ChessBoard.setVisible(true);
    }

    // 服务器相关方法
    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345);
                System.out.println("服务器已启动，等待客户端连接...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("客户端连接: " + clientSocket.getInetAddress());

                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientWriters.add(writer);

                    new Thread(new ClientHandler(clientSocket)).start();

                    // 发送初始游戏状态
                    broadcastGameState();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;

        public ClientHandler(Socket socket) {
            try {
                this.clientSocket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    // 处理客户端消息
                    System.out.println("收到客户端消息: " + message);
                }
            } catch (IOException e) {
                System.out.println("客户端断开连接");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 广播消息给所有客户端
    private void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // 广播游戏状态
    private void broadcastGameState() {
        StringBuilder state = new StringBuilder();
        for (Block block : board.blocks) {
            state.append("man,").append(block.getX_cordinate()).append(",").append(block.getY_cordinate()).append("\n");
        }
        broadcast(state.toString());
    }

    private void broadcasttool(int index){
        broadcast("tool," + index);
    }

    // 广播移动
    private void broadcastMove(String move) {
        broadcast(move);
    }

    // 广播胜利
    private void broadcastVictory() {
        broadcast("v");
    }

    // 广播失败
    private void broadcastDefeat() {
        broadcast("d");
    }

    // 广播时间
    private void broadcastTime() {
        broadcast("time,"+seconds);
    }

    //棋子方块的添加
    private void addChessBlock(String name, int width, int height, int x, int y) {
        BlockButton button = new BlockButton(width * 60, height * 60, false, name);
        button.setBounds(0,0,width * 60,height * 60);
        Characters.add(button);
        if(x == 0 && y == 0)Characters.getLast().setVisible(false);
        animateMove(button,x * 60,y * 60);

        //选中状态判断
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

    //道具方块的添加
    private void addToolBlock(String name) {
        tool button = new tool(60,60,name);
        button.setBounds(570,270 + 70 * tool.i,60,60);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        Tools.add(button);
        if(tool.i==1&&SelectLevel.level==4){
            button.setUsed(LoginSystem.tool1 != 1);
        }else if(tool.i==0&&SelectLevel.level==4){
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

    //两种道具的功能
    private void ToolClock(){
        pauseGameTimer();
        plusGameTimer();
    }

    private void deleteZu(){
        Random rand = new Random();
        int index = rand.nextInt(4) + 6;
        board.changeIs_available(board.blocks[index].getY_cordinate(),board.blocks[index].getX_cordinate(),true);
        broadcasttool(index);
        board.blocks[index].setX_cordinate(0);
        board.blocks[index].setY_cordinate(0);
        Characters.get(index).setLocation(0,0);
        Characters.get(index).setVisible(false);
    }

    //计时器设置
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
        broadcastTime(); // 广播时间
    }

    private void plusGameTimer(){
        Random rand = new Random();
        seconds += rand.nextInt(150) + 30;
        isRunning = true;
        playtime.start();
        updateTimeLabel();
        broadcastTime(); // 广播时间
    }

    private void startGameTimer() {
        seconds = (board.getLoginSystem().loginStatus == 1) ?
                board.getLoginSystem().time : 300;
        isRunning = true;
        playtime.start();
        updateTimeLabel();
        broadcastTime(); // 广播时间
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
        final int animationDelay = 10;

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

    //socket的关闭
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                for (PrintWriter writer : clientWriters) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            super.processWindowEvent(e);
        }
    }

    private void closeNetworkResources() {
        try {
            // 关闭所有客户端连接
            for (PrintWriter writer : clientWriters) {
                if (writer != null) {
                    writer.close();
                }
            }
            clientWriters.clear();

            // 关闭服务器Socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("服务器已关闭");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}