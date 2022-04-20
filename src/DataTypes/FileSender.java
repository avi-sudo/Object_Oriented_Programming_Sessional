package DataTypes;

import java.io.Serializable;

public class FileSender implements Serializable {
    private byte[] file;

    public FileSender(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }
}
