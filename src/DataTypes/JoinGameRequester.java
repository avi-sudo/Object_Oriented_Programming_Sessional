package DataTypes;

public class JoinGameRequester extends Requester {
    private String host;
    private UserData challenger;
    private String viewer;

    public JoinGameRequester(String host, UserData challenger) {
        //this is a requester to join an open game
        super("joinGame");
        this.host = host;
        this.challenger = challenger;
    }

    public JoinGameRequester(String host, String viewer) {
        //this is a requester to see a running game
        super("viewGame");
        this.host = host;
        this.viewer = viewer;
    }

    @Override
    public String getHost() {
        return host;
    }

    public String getViewer() {
        return viewer;
    }

    @Override
    public UserData getChallenger() {
        return challenger;
    }
}
