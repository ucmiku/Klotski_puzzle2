package loginmodel;

import game_logic.Block;
import game_logic.Board;
import GUI.GameBoard;
import GUI.SelectLevel;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginSystem {
    private static final String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop\\Klotski puzzle\\";
    private String username;
    private String password;
    public int loginStatus; // 0=未登录, 1=登录成功, 2=新用户注册
    public int time;
    public boolean ReadError;
    ArrayList<String> tmp;

    public LoginSystem() {
        ReadError = false;
        ensureDirectoryExists();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void ensureDirectoryExists() {
        File dir = new File(DESKTOP_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean authenticateUser() {

        File userFile = new File(DESKTOP_PATH + username + ".txt");

        try {
            if (userFile.createNewFile()) {
                // 新用户注册
                loginStatus = 2;
                savePassword(password);
                save(new Board().getcordinate(),new Board().getProcess());
                return true;
            } else {
                // 现有用户登录
                if (validatePassword(password)) {
                    loginStatus = 1;
                    return true;
                } else {
                    System.out.println("密码错误");
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("登录过程出错: " + e.getMessage());
            return false;
        }
    }

    public Board readdata(Board b) throws FileNotFoundException{
        boolean isValid = true;
        try (Scanner scanner = new Scanner(new File(DESKTOP_PATH + username + ".txt"))) {
            scanner.nextLine();
            time=Integer.parseInt(scanner.nextLine());
            boolean[][] isavaiable = new boolean[7][6];
            if (time<=0||time>300){
                isValid = false;
            }
            for(Block bb : b.blocks){
                int x = Integer.parseInt(scanner.nextLine());
                if(x + bb.getX_length() - 1 > 4 || x < 0)isValid = false;
                bb.setX_cordinate(x);
                int y = Integer.parseInt(scanner.nextLine());
                if(y + bb.getY_length() - 1 > 5 || y < 0)isValid = false;
                bb.setY_cordinate(y);
                for(int i = y;i <= y + bb.getY_length() - 1;i++)
                    for(int j = x;j <= x + bb.getX_length() - 1;j++){
                        if(isavaiable[i][j]){
                            isValid = false;
                        }
                        isavaiable[i][j] = true;//读取判断是否为空的状态
                    }

            }

            for(int i = 1;i <= 5;i++)
                for(int j = 1;j <= 4;j++)
                    isavaiable[i][j] = !isavaiable[i][j];//因为false代表占用，因此需要取反
            tmp = new ArrayList<>();
            while(true){
                if(!scanner.hasNextLine())break;
                tmp.add(scanner.nextLine());
            }

            b.setProcess(tmp);
            b.setIs_available(isavaiable);
            if(!isValid)reread(b);
        }catch(Exception e){
            reread(b);
        }
        return b;
    }

    private void savePassword(String password) throws IOException {
        try (FileWriter writer = new FileWriter(DESKTOP_PATH + username + ".txt")) {
            writer.write(password);
        }
    }

    private boolean validatePassword(String inputPassword) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(DESKTOP_PATH + username + ".txt"))) {
            String storedPassword = scanner.next();
            return storedPassword.equals(inputPassword);
        }
    }

    private void saveUserData(ArrayList<String> data) {
        if (loginStatus > 0) {
            writeToFile(data);
        }
    }

    private void writeToFile(ArrayList<String> data) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DESKTOP_PATH + username + ".txt", true))) {
            for (String line : data) {
                writer.newLine();
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("写入失败: " + e.getMessage());
        }
    }
    private void rewrite() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DESKTOP_PATH + username + ".txt", false))) {
            writer.write(password);
            writer.newLine();
            int time=GameBoard.seconds;
            if (time<=0||time>300){
                time=300;
            }
            writer.write(String.valueOf(time));
        } catch (IOException e) {
            System.err.println("写入失败: " + e.getMessage());
        }
    }

    public void save(ArrayList<String> data1,ArrayList<String> data2){
       rewrite();
       saveUserData(data1);
       saveUserData(data2);
    }

    public void reread(Board b) throws FileNotFoundException {
            ReadError = true;
            save(new Board().getcordinate(),new Board().getProcess());
            readdata(b);
            loginStatus=2;
    }


}