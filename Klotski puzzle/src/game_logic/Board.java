package game_logic;

import loginmodel.LoginSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    boolean[][] is_available = new boolean[7][6];//为了后续判断是否能移动，建立比棋盘大一格的判断二维数组
    public Block[] blocks = new Block[10];
    private ArrayList<String> process = new ArrayList<>();
    private LoginSystem loginSystem;
    private String WithdrawName;
    private int WithdrawBlockNumber;
    private int steps = process.size();
    boolean can_be_moved;

    public int getWithdrawBlockNumber() {
        return WithdrawBlockNumber;
    }

    public void setWithdrawBlockNumber(int withdrawBlockNumber) {
        WithdrawBlockNumber = withdrawBlockNumber;
    }

    public int getSteps() {
        return steps;
    }

    public LoginSystem getLoginSystem() {
        return loginSystem;
    }

    public void setLoginSystem(LoginSystem loginSystem) {
        this.loginSystem = loginSystem;
    }

    public void setIs_available(boolean[][] is_available) {
        this.is_available = is_available;
    }

    public void changeIs_available(int x,int y,boolean b){
        this.is_available[x][y] = b;
    }

    public ArrayList<String> getProcess() {
        return process;
    }

    public void setProcess(ArrayList<String> process) {
        this.process = process;
    }

    public Board(){
        for(int i = 0;i <= 9;i++){
            blocks[i] = new Block();//初始化blocks
        }
        //默认棋盘
        blocks[0].setBlock("caocao",2,1,2,2);
        blocks[1].setBlock("huangzhong",1,1,1,2);
        blocks[2].setBlock("zhangfei",4,1,1,2);
        blocks[3].setBlock("machao",1,3,1,2);
        blocks[4].setBlock("zhaoyun",4,3,1,2);
        blocks[5].setBlock("guanyu",2,3,2,1);
        blocks[6].setBlock("zuone",1,5,1,1);
        blocks[7].setBlock("zutwo",2,4,1,1);
        blocks[8].setBlock("zuthree",3,4,1,1);
        blocks[9].setBlock("zufour",4,5,1,1);//逻辑：将方块左上角那一格的位置作为方块的坐标

        is_available[5][2] = true;
        is_available[5][3] = true;//棋盘刚开始的状态，下面两个空位
    }//初始化棋盘，is_available用于记录该位置是否为空，true表示为空

    //用于储存棋盘的坐标
    public ArrayList<String> getcordinate(){
        ArrayList<String> tmpstrs = new ArrayList<>();
        for(Block b : blocks){
            String tmpstr1 = String.valueOf(b.getX_cordinate());
            String tmpstr2 = String.valueOf(b.getY_cordinate());
            tmpstrs.add(tmpstr1);
            tmpstrs.add(tmpstr2);
        }
        return tmpstrs;
    }

    //4种方向是否可以移动的判断， 用于自动通关类
    public boolean canMoveLeft(Block block){
        for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
            if(!is_available[i][block.getX_cordinate() - 1])return false;//检查左边是否有空位
        }
        return true;
    }

    public boolean canMoveUp(Block block){
        for(int i = block.getX_cordinate(); i <= block.getX_length() + block.getX_cordinate() - 1; i++){
            if(!is_available[block.getY_cordinate() - 1][i])return false;//检查上边是否有空位
        }
        return true;
    }

    public boolean canMoveRight(Block block){
        for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
            if(!is_available[i][block.getX_cordinate() + block.getX_length()])return false;//检查右边是否有空位
        }
        return true;
    }

    public boolean canMoveDown(Block block){
        for(int i = block.getX_cordinate(); i <= block.getX_length() + block.getX_cordinate() - 1; i++){
            if(!is_available[block.getY_cordinate() + block.getY_length()][i])return false;//检查下边是否有空位
        }
        return true;
    }

    //移动逻辑
    public void movement(char checkdirection,Block targetblock){
        Block block = new Block();

        for(Block b : blocks){
            if(b == targetblock){
                block = b;
            }
        }

        can_be_moved = true;

        if(checkdirection == 'l'){//判断移动以及方块的移动
            for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
                if(!is_available[i][block.getX_cordinate() - 1])can_be_moved = false;//检查左边是否有空位
            }
            if(can_be_moved) {
                for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
                    is_available[i][block.getX_cordinate() - 1] = false;
                    is_available[i][block.getX_cordinate() + block.getX_length() - 1] = true;//移动后左边被占用，右边空出来
                }
                block.setX_cordinate(block.getX_cordinate() - 1);//更新方块坐标
                process.add(block.getName() + ",l");
            }
        }
        else if(checkdirection == 'u'){
            for(int i = block.getX_cordinate(); i <= block.getX_length() + block.getX_cordinate() - 1; i++){
                if(!is_available[block.getY_cordinate() - 1][i])can_be_moved = false;//检查上边是否有空位
            }
            if(can_be_moved){
                for (int i = block.getX_cordinate(); i <= block.getX_cordinate() + block.getX_length() - 1; i++) {
                    is_available[block.getY_cordinate() - 1][i] = false;
                    is_available[block.getY_cordinate() + block.getY_length() - 1][i] = true;//移动后上边被占用，下边空出来
                }
                block.setY_cordinate(block.getY_cordinate() - 1);//更新方块坐标
                process.add(block.getName() + ",u");
            }
        }
        else if(checkdirection == 'd'){
            for(int i = block.getX_cordinate(); i <= block.getX_length() + block.getX_cordinate() - 1; i++){
                if(!is_available[block.getY_cordinate() + block.getY_length()][i])can_be_moved = false;//检查下边是否有空位
            }
            if(can_be_moved){
                for (int i = block.getX_cordinate(); i <= block.getX_length() + block.getX_cordinate() - 1; i++) {
                    is_available[block.getY_cordinate() + block.getY_length()][i] = false;
                    is_available[block.getY_cordinate()][i] = true;//移动后下边被占用，上边空出来
                }
                block.setY_cordinate(block.getY_cordinate() + 1);//更新方块坐标
                process.add(block.getName() + ",d");
            }
        }
        else if(checkdirection == 'r'){
            for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
                if(!is_available[i][block.getX_cordinate() + block.getX_length()])can_be_moved = false;//检查右边是否有空位
            }
            if(can_be_moved) {
                for(int i = block.getY_cordinate(); i <= block.getY_cordinate() + block.getY_length() - 1; i++){
                    is_available[i][block.getX_cordinate() + block.getX_length()] = false;
                    is_available[i][block.getX_cordinate()] = true;//移动后右边被占用，左边空出来
                }
                block.setX_cordinate(block.getX_cordinate() + 1);//更新方块坐标
                process.add(block.getName() + ",r");
            }
        }
    }

    //标记被撤回的方块，用于撤回按钮
    public String getWithdrawName() {
        return WithdrawName;
    }

    //撤回逻辑
    public boolean withdraw(){
        String s1 = process.getLast();
        char[] c = s1.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c1 : c){
            if(c1 == ',')break;
            sb.append(c1);
        }
        String s = sb.toString();
        WithdrawName = s;
        for(int j = 0; j < 10;j ++){
            if(blocks[j].getName().equals(s)){
                boolean can_be_moved = true;
                Block targetb = blocks[j];
                WithdrawBlockNumber = j;
                char checkdirection = s1.charAt(s1.length() - 1);
                if(checkdirection != 'l'&&checkdirection != 'r'&&checkdirection != 'u'&&checkdirection != 'd'){
                    return false;
                }

                if(checkdirection == 'r'){//判断移动以及方块的移动
                    for(int i = targetb.getY_cordinate();i <= targetb.getY_cordinate() + targetb.getY_length() - 1;i++){
                        if(!is_available[i][targetb.getX_cordinate() - 1])can_be_moved = false;//检查左边是否有空位
                    }
                    if(!can_be_moved) {
                        System.out.println("The block can't be moved");
                        return false;
                    }
                    else {
                        for(int i = targetb.getY_cordinate();i <= targetb.getY_cordinate() + targetb.getY_length() - 1;i++){
                            is_available[i][targetb.getX_cordinate() - 1] = false;
                            is_available[i][targetb.getX_cordinate() + targetb.getX_length() - 1] = true;//移动后左边被占用，右边空出来
                        }
                        targetb.setX_cordinate(targetb.getX_cordinate() - 1);//更新方块坐标
                    }
                }
                else if(checkdirection == 'd'){
                    for(int i = targetb.getX_cordinate();i <= targetb.getX_length() + targetb.getX_cordinate() - 1;i++){
                        if(!is_available[targetb.getY_cordinate() - 1][i])can_be_moved = false;//检查上边是否有空位
                    }
                    if(!can_be_moved) {
                        System.out.println("The block can't be moved");
                        return false;
                    }
                    else {
                        for (int i = targetb.getX_cordinate(); i <= targetb.getX_cordinate() + targetb.getX_length() - 1; i++) {
                            is_available[targetb.getY_cordinate() - 1][i] = false;
                            is_available[targetb.getY_cordinate() + targetb.getY_length() - 1][i] = true;//移动后上边被占用，下边空出来
                        }
                        targetb.setY_cordinate(targetb.getY_cordinate() - 1);//更新方块坐标
                    }
                }
                else if(checkdirection == 'u'){
                    for(int i = targetb.getX_cordinate();i <= targetb.getX_length() + targetb.getX_cordinate() - 1;i++){
                        if(!is_available[targetb.getY_cordinate() + targetb.getY_length()][i])can_be_moved = false;//检查下边是否有空位
                    }
                    if(!can_be_moved) {
                        System.out.println("The block can't be moved");
                        return false;
                    }
                    else {
                        for (int i = targetb.getX_cordinate(); i <= targetb.getX_length() + targetb.getX_cordinate() - 1; i++) {
                            is_available[targetb.getY_cordinate() + targetb.getY_length()][i] = false;
                            is_available[targetb.getY_cordinate()][i] = true;//移动后下边被占用，上边空出来
                        }
                        targetb.setY_cordinate(targetb.getY_cordinate() + 1);//更新方块坐标
                    }
                }
                else if(checkdirection == 'l'){
                    for(int i = targetb.getY_cordinate();i <= targetb.getY_cordinate() + targetb.getY_length() - 1;i++){
                        if(!is_available[i][targetb.getX_cordinate() + targetb.getX_length()])can_be_moved = false;//检查右边是否有空位
                    }
                    if(!can_be_moved) {
                        System.out.println("The block can't be moved");
                        return false;
                    }
                    else {
                        for(int i = targetb.getY_cordinate();i <= targetb.getY_cordinate() + targetb.getY_length() - 1;i++){
                            is_available[i][targetb.getX_cordinate() + targetb.getX_length()] = false;
                            is_available[i][targetb.getX_cordinate()] = true;//移动后右边被占用，左边空出来
                        }
                        targetb.setX_cordinate(targetb.getX_cordinate() + 1);//更新方块坐标
                    }
                }
            }
        }
        process.removeLast();
        return true;
    }

    //胜利判断逻辑
    public boolean isVictory(){
        if(blocks[0].getX_cordinate() == 2 && blocks[0].getY_cordinate() == 4)return true;
        return false;
    }

}
