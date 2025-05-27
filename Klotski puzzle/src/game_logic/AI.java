package game_logic;

import java.util.*;

public class AI {

    //游戏状态
    static class GameState{
        Board board;
        List<String>moveHistory;

        //两种构造函数，分别对应初始化棋盘和移动后的棋盘
        public GameState(Board board){
            this.board = deepCopyBoard(board);
            this.moveHistory = new ArrayList<>();
        }

        public GameState(GameState other){
            this.board = deepCopyBoard(other.board);
            this.moveHistory = new ArrayList<>(other.moveHistory);
        }
        //强等于
        public boolean equals(Object objects){
            if(this == objects)return true;
            if(!(objects instanceof GameState))return false;

            GameState other = (GameState) objects;
            for(int i = 0;i < 10;i++){
                Block thisBlock = this.board.blocks[i];
                Block otherBlock = other.board.blocks[i];
                if(thisBlock.getX_cordinate() != otherBlock.getX_cordinate()
                || thisBlock.getY_cordinate() != otherBlock.getY_cordinate())
                    return false;
            }
            return true;
        }

        //提高性能，使用字符串作为键记录状态
        public String getStateKey(){
            StringBuilder sb = new StringBuilder();
            for(Block block : board.blocks){
                sb.append(block.getX_cordinate()).append(',').append(block.getY_cordinate()).append('|');
            }
            return sb.toString();
        }

    }

    public static Board deepCopyBoard(Board original){
        Board newBoard = new Board();
        for(int i = 0;i < 10;i ++){
            Block originalBlock = original.blocks[i];
            Block copyBlock = newBoard.blocks[i];
            copyBlock.setBlock(originalBlock.getName(),
                    originalBlock.getX_cordinate(),
                    originalBlock.getY_cordinate(),
                    originalBlock.getX_length(),
                    originalBlock.getY_length());
        }

        boolean[][] originalAvailable = original.is_available;
        boolean[][] copyAvailable = new boolean[7][6];
        for (int i = 0; i < 7; i++) {
            System.arraycopy(originalAvailable[i], 0, copyAvailable[i], 0, 6);
        }
        newBoard.setIs_available(copyAvailable);

        return newBoard;
    }

    //BFS主体
    public static List<String> solve(Board initialBoard){
        //队列记录最短的路径
        Queue<GameState> queue = new LinkedList<>();
        //集合记录已经访问过的状态
        Set<String> visited = new HashSet<>();

        //初始状态
        GameState initialState = new GameState(initialBoard);
        queue.add(initialState);
        visited.add(initialState.getStateKey());

        //可能的移动方向
        char[] directions = {'l','r','u','d'};

        //循环主体
        while(!queue.isEmpty()){
            GameState currentState = queue.poll();
            if (currentState.moveHistory.size() > 200) continue;

            //胜利检测
            if(currentState.board.blocks[0].getX_cordinate() == 2 &&
                    currentState.board.blocks[0].getY_cordinate() == 4) {
                return currentState.moveHistory;
            }

            if (visited.size() % 1000 == 0) {
                System.out.println("已探索状态: " + visited.size() +
                        " 队列大小: " + queue.size());
            }

            //开始尝试移动每个方块的四个方向
            for(int index = 0;index < 10;index ++){
                if(currentState.board.blocks[index].getX_cordinate()==0||!currentState.board.canMoveUp(currentState.board.blocks[index])&&!currentState.board.canMoveLeft(currentState.board.blocks[index])&&!currentState.board.canMoveDown(currentState.board.blocks[index])&&!currentState.board.canMoveRight(currentState.board.blocks[index])){
                    continue;
                }
                for(char direction : directions){
                    //创建新状态
                    GameState newState = new GameState(currentState);
                    Block targetBlock = newState.board.blocks[index];


                    //移动
                    newState.board.movement(direction,targetBlock);

                    //检测is_available是否与之前相等，若不相等则是发生了移动
                    if(newState.board.can_be_moved){
                        //检测是否访问过该状态
                        if(!visited.contains(newState.getStateKey())){
                            newState.moveHistory.add(targetBlock.getName() + ',' + direction);
                            queue.add(newState);
                            visited.add(newState.getStateKey());
                        }
                    }
                }
            }
        }
        //无解
        return Collections.emptyList();
    }
}
