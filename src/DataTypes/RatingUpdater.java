package DataTypes;

import java.io.Serializable;

public class RatingUpdater implements Serializable {
    private int rating;

    public RatingUpdater(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}
