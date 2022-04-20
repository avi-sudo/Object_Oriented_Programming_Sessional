//this class holds the name password and ratings of users, it is also used while validating
//login and registrations

package DataTypes;

import java.io.Serializable;

public class UserData implements Serializable {
    private String userName;
    private String password;
    private int rating;
    private int type; //1 for login, 2 for registration, 0 for general purpose

    public UserData(String userName, String password,int rating, int type){
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }

    public int getRating() {return rating;}

    public void setRating(int rating) {
        this.rating = rating;
    }
}
