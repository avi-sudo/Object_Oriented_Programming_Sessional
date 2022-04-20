package DataTypes;

public class LeaveGameRequester extends Requester {
    private String host;
    private String viewer;

    public LeaveGameRequester(String host) {
        //a player is leaving a game
        super("leaveGame");
        this.host = host;
    }

    public LeaveGameRequester(String viewer, String host) {
        super("leaveGame2");
        this.viewer = viewer;
        this.host = host;
    }

    @Override
    public String getViewer() {
        return viewer;
    }

    @Override
    public String getHost() {
        return host;
    }
}
