package DataTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    UserData user1;
    UserData user2 = null;
    List<Move> moves = new ArrayList<>();
    boolean isFull = false;
    private int ID;
    private List<String> spectators = new ArrayList<>();

    public Game(UserData user1){
        this.user1 = user1;
    }

    public Game(UserData user1, UserData user2) {
        this.user1 = user1;
        this.user2 = user2;
        isFull = true;
    }

    public void addUser2(UserData user2) {
        this.user2 = user2;
        isFull = true;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void addMove(Move m) {
        moves.add(m);
    }

    public UserData getUser1() {
        return user1;
    }

    public UserData getUser2() {
        return user2;
    }

    public boolean getIsFull() {
        return isFull;
    }

    public void addSpectator(String spectator) {
        spectators.add(spectator);
        System.out.println("added " + spectator + " to the game");
    }

    public void removeSpectator(String spectator) {
        spectators.remove(spectator);
        System.out.println("removed " + spectator);
    }

    public List<Move> getMoves() {
        return moves;
    }

    public List<String> getSpectators() {
        return spectators;
    }
}
