package DataTypes;

import java.io.Serializable;

public class Move implements Serializable {
    private String player; //who made the move
    private int column; //on which column the move will be made
    private int row; //which row will be filled
    private String type;
    //type = normal-> normal move
    //type = end-> the game ended
    //type = cancel-> someone quit the game

    public Move(int column, int row, String type, String player) {
        this.player = player;
        this.column = column;
        this.row = row;
        this.type = type;
    }

    public String getPlayer() {
        return player;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public String getType() {
        return type;
    }
}
