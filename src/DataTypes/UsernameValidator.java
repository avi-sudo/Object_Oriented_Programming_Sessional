package DataTypes;

import java.io.Serializable;

public class UsernameValidator implements Serializable {
    private boolean isValid;
    private String type; //login or registration
    private int errorType;/*in login-> 1 for wrong credentials, 2 for already logged in
    in registration-> 1 for username already exists, 2 for password format error,
    3 for password length error, 4 for username format error, 5 for username length error */

    public UsernameValidator(boolean isValid, String type, int errorType) {
        this.isValid = isValid;
        this.type = type;
        this.errorType = errorType;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public String getType() {
        return type;
    }

    public int getErrorType() {
        return errorType;
    }
}
