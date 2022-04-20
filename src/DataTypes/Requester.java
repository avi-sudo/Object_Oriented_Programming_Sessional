package DataTypes;

import java.io.Serializable;

public class Requester implements Serializable {
    private String type;
    private String host = null; //only applicable while joining a game
    private UserData challenger = null; //only applicable while joining a game
    //openGames-> to get the list of open games
    //fullGames-> to get the list of full games
    //joinGame-> to request to join a game, comes with a parameter containing the host's name
    //and the userdata of the challenger
    //leaveGame-> to cancel a game, comes with the name of the host
    //immediateResponse-> server sends false to the client, used to terminate the client
    //read thread
    //opponentName -> to get the name of current opponent

    public Requester(String type) {
        this.type = type;
    }

    public Requester(String type, String host) {
        this.type = type;
        this.host = host;
    }

    public Requester(String type, String host, UserData challenger) {
        this.type = type;
        this.host = host;
        this.challenger = challenger;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public UserData getChallenger() {
        return challenger;
    }

    public String getViewer() {
        return "default";
    }
}
