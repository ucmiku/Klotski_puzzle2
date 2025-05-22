package game_logic;

public class Block {
    private int x_cordinate;
    private int y_cordinate;
    private int x_length;
    private int y_length;
    private String name;//块的五个属性：左上角方格的x,y坐标；x，y方向上的长度；块的名字

    public void setX_cordinate(int x_cordinate) {
        this.x_cordinate = x_cordinate;
    }

    public void setY_cordinate(int y_cordinate) {
        this.y_cordinate = y_cordinate;
    }

    public int getX_cordinate() {
        return x_cordinate;
    }

    public void setBlock(String name,int x_cordinate,int y_cordinate,int x_length,int y_length) {
        this.x_cordinate = x_cordinate;
        this.y_cordinate = y_cordinate;
        this.x_length = x_length;
        this.y_length = y_length;
        this.name = name;
    }//块的初始化

    public String getName() {
        return name;
    }

    public int getY_cordinate() {
        return y_cordinate;
    }

    public int getY_length() {
        return y_length;
    }

    public int getX_length() {
        return x_length;
    }
}
