package Client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConnectionScreenController {
    public Button connectButton;
    public TextField portBox;
    public TextField ipBox;
    public Label errorLabel;
    public ImageView imageView;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setImage(String url) {
        System.out.println("Setting image from url");
        Image image = new Image(url);
        imageView.setImage(image);
    }

    public void showError() {
        errorLabel.setText("Failed to connect.");
    }

    public void connect(ActionEvent actionEvent) {
        String ip = ipBox.getText();
        int port = Integer.parseInt(portBox.getText());
        main.Connect(ip, port);
    }
}
