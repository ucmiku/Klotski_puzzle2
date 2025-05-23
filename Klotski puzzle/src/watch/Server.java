package watch;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("服务器已启动");

        List<PrintWriter> spectators = new ArrayList<>();
        int[] caoCaoPos = {1, 0}; // x,y

        while (true) {
            Socket client = serverSocket.accept();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("POS:" + caoCaoPos[0] + "," + caoCaoPos[1]);

            spectators.add(out);
            System.out.println("新观战者加入，当前人数: " + spectators.size());

            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    while (true) {
                        String msg = in.readLine();
                        if (msg == null) break;
                        System.out.println("收到消息: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("客户端断开");
                }
            }).start();
        }
    }

    // 广播
    static void broadcastPos(List<PrintWriter> spectators, int x, int y) {
        for (PrintWriter out : spectators) {
            out.println("POS:" + x + "," + y);
        }
    }
}