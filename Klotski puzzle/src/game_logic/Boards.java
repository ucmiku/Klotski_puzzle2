package game_logic;

public class Boards {
    public Board[] boards = new Board[5];

    public Boards(){
        for(int i = 0;i < 5 ;++i){
            boards[i] = new Board();
        }

        boards[0].blocks[0].setX_cordinate(2);//caocao
        boards[0].blocks[0].setY_cordinate(1);
        boards[0].blocks[1].setX_cordinate(3);//huangzhong
        boards[0].blocks[1].setY_cordinate(3);
        boards[0].blocks[2].setX_cordinate(2);//zhangfei
        boards[0].blocks[2].setY_cordinate(3);
        boards[0].blocks[3].setX_cordinate(1);//machao
        boards[0].blocks[3].setY_cordinate(2);
        boards[0].blocks[4].setX_cordinate(4);//zhaoyun
        boards[0].blocks[4].setY_cordinate(2);
        boards[0].blocks[5].setX_cordinate(1);//guanyu
        boards[0].blocks[5].setY_cordinate(5);
        boards[0].blocks[6].setX_cordinate(1);//zuone
        boards[0].blocks[6].setY_cordinate(4);
        boards[0].blocks[7].setX_cordinate(3);//zutwo
        boards[0].blocks[7].setY_cordinate(5);
        boards[0].blocks[8].setX_cordinate(4);//zuthree
        boards[0].blocks[8].setY_cordinate(5);
        boards[0].blocks[9].setX_cordinate(4);//zufour
        boards[0].blocks[9].setY_cordinate(4);
        boards[0].changeIs_available(5,2,false);
        boards[0].changeIs_available(5,3,false);
        boards[0].changeIs_available(1,1,true);
        boards[0].changeIs_available(1,4,true);

        boards[1].blocks[0].setX_cordinate(2);//caocao
        boards[1].blocks[0].setY_cordinate(1);
        boards[1].blocks[1].setX_cordinate(3);//huangzhong
        boards[1].blocks[1].setY_cordinate(3);
        boards[1].blocks[2].setX_cordinate(1);//zhangfei
        boards[1].blocks[2].setY_cordinate(1);
        boards[1].blocks[3].setX_cordinate(2);//machao
        boards[1].blocks[3].setY_cordinate(3);
        boards[1].blocks[4].setX_cordinate(1);//zhaoyun
        boards[1].blocks[4].setY_cordinate(3);
        boards[1].blocks[5].setX_cordinate(2);//guanyu
        boards[1].blocks[5].setY_cordinate(5);
        boards[1].blocks[6].setX_cordinate(4);//zuone
        boards[1].blocks[6].setY_cordinate(1);
        boards[1].blocks[7].setX_cordinate(4);//zutwo
        boards[1].blocks[7].setY_cordinate(2);
        boards[1].blocks[8].setX_cordinate(4);//zuthree
        boards[1].blocks[8].setY_cordinate(3);
        boards[1].blocks[9].setX_cordinate(4);//zufour
        boards[1].blocks[9].setY_cordinate(4);
        boards[1].changeIs_available(5,2,false);
        boards[1].changeIs_available(5,3,false);
        boards[1].changeIs_available(5,1,true);
        boards[1].changeIs_available(5,4,true);

        boards[2].blocks[0].setX_cordinate(1);//caocao
        boards[2].blocks[0].setY_cordinate(1);
        boards[2].blocks[1].setX_cordinate(3);//huangzhong
        boards[2].blocks[1].setY_cordinate(1);
        boards[2].blocks[2].setX_cordinate(3);//zhangfei
        boards[2].blocks[2].setY_cordinate(4);
        boards[2].blocks[3].setX_cordinate(4);//machao
        boards[2].blocks[3].setY_cordinate(1);
        boards[2].blocks[4].setX_cordinate(4);//zhaoyun
        boards[2].blocks[4].setY_cordinate(4);
        boards[2].blocks[5].setX_cordinate(3);//guanyu
        boards[2].blocks[5].setY_cordinate(3);
        boards[2].blocks[6].setX_cordinate(1);//zuone
        boards[2].blocks[6].setY_cordinate(3);
        boards[2].blocks[7].setX_cordinate(1);//zutwo
        boards[2].blocks[7].setY_cordinate(4);
        boards[2].blocks[8].setX_cordinate(2);//zuthree
        boards[2].blocks[8].setY_cordinate(3);
        boards[2].blocks[9].setX_cordinate(2);//zufour
        boards[2].blocks[9].setY_cordinate(4);
        boards[2].changeIs_available(5,2,false);
        boards[2].changeIs_available(5,3,false);
        boards[2].changeIs_available(5,1,true);
        boards[2].changeIs_available(5,2,true);

        boards[3].blocks[0].setX_cordinate(2);//caocao
        boards[3].blocks[0].setY_cordinate(1);
        boards[3].blocks[1].setX_cordinate(1);//huangzhong
        boards[3].blocks[1].setY_cordinate(1);
        boards[3].blocks[2].setX_cordinate(1);//zhangfei
        boards[3].blocks[2].setY_cordinate(3);
        boards[3].blocks[3].setX_cordinate(2);//machao
        boards[3].blocks[3].setY_cordinate(4);
        boards[3].blocks[4].setX_cordinate(4);//zhaoyun
        boards[3].blocks[4].setY_cordinate(3);
        boards[3].blocks[5].setX_cordinate(2);//guanyu
        boards[3].blocks[5].setY_cordinate(3);
        boards[3].blocks[6].setX_cordinate(4);//zuone
        boards[3].blocks[6].setY_cordinate(1);
        boards[3].blocks[7].setX_cordinate(4);//zutwo
        boards[3].blocks[7].setY_cordinate(2);
        boards[3].blocks[8].setX_cordinate(1);//zuthree
        boards[3].blocks[8].setY_cordinate(5);
        boards[3].blocks[9].setX_cordinate(4);//zufour
        boards[3].blocks[9].setY_cordinate(5);
        boards[3].changeIs_available(5,2,false);
        boards[3].changeIs_available(5,3,false);
        boards[3].changeIs_available(4,3,true);
        boards[3].changeIs_available(5,3,true);
    }
}
