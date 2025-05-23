package watch;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class Client extends JFrame {
    private JLabel posLabel = new JLabel("曹操位置: 等待连接...");
    private Socket socket;

    public Client() {
        setLayout(new FlowLayout());
        add(posLabel);
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        connectToServer();
    }

    void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // 简单读取服务器消息
            new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readLine();
                        if (msg.startsWith("POS:")) {
                            String pos = msg.substring(4);
                            updatePosition(pos);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("与服务器断开连接");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "连接服务器失败");
        }
    }

    void updatePosition(String pos) {
        SwingUtilities.invokeLater(() -> {
            posLabel.setText("曹操位置: " + pos);
        });
    }

    public static void main(String[] args) {
        new Client();
    }
}