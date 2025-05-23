// Server.java（简化版，仅处理转发）
package watch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 12345;
    private static ArrayList<PrintWriter> viewers = new ArrayList<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("服务器启动，等待连接...");
                while (true) {
                    Socket socket = serverSocket.accept();
                    // 判断是玩家还是观众
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String role = reader.readLine();

                    if ("player".equals(role)) {
                        // 玩家连接，启动消息读取线程
                        new PlayerHandler(socket).start();
                    } else {
                        // 观众连接，添加到转发列表
                        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                        viewers.add(writer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 转发消息给所有观众
    private static void broadcast(String message) {
        viewers.forEach(writer -> writer.println(message));
    }

    // 玩家处理线程
    static class PlayerHandler extends Thread {
        private final Socket socket;

        public PlayerHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String move;
                while ((move = reader.readLine()) != null) {
                    Server.broadcast(move); // 转发玩家操作
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}