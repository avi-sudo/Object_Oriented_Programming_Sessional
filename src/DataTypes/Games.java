//this class can be used to send a list of games per user's request

package DataTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Games implements Serializable {
    private List<Game> games = new ArrayList<>();

    public Games(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }
}
