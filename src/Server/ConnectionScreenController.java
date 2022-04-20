package Server;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionScreenController {
    public Button initiateButton;
    public TextField portBox;
    public TextField fileDirBox;
    public Label errorLabel;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    public void showError() {
        errorLabel.setText("Failed to initiate.");
    }

    public void initiate(ActionEvent actionEvent) {
        String fileDir = fileDirBox.getText();
        int port = Integer.parseInt(portBox.getText());
        try {
            main.initiate(fileDir, port);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("error initiating");
        }
    }
}
