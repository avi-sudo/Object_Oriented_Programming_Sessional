package DataTypes;

public class ImageRequester extends Requester {
    private String host;

    public ImageRequester(String host) {
        super("image");
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }
}


