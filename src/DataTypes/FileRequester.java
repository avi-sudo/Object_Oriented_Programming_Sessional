package DataTypes;

public class FileRequester extends Requester {
    private String host;

    public FileRequester(String host) {
        super("file");
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }
}


