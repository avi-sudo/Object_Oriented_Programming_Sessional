package DataTypes;

import java.io.Serializable;

public class LogOutData implements Serializable {
    private String name;

    public LogOutData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
