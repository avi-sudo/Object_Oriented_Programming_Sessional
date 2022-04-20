package DataTypes;

import java.io.Serializable;

public class TerminationData implements Serializable {
    private String name;

    public TerminationData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
