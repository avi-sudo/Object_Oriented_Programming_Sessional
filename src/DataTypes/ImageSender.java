package DataTypes;

import java.io.Serializable;

public class ImageSender implements Serializable {
    private byte[] image;

    public ImageSender(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
