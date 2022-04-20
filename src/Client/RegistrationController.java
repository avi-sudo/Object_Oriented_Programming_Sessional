package Client;

import DataTypes.UserData;
import DataTypes.UsernameValidator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {
    public TextField IDBox;
    public PasswordField passwordBox;
    public Label passwordLabel1;
    public Label passwordLabel2;
    public Button createButton;
    public Button previousButton;
    public Label IDLabel1;
    public Label IDLabel2;
    Main main;
    Client client;

    public void setMain(Main main){
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void createID(ActionEvent actionEvent) {
        client.sendToServer(new UserData(IDBox.getText(), passwordBox.getText(), 0,
                2));
    }

    public void gotoPrevious(ActionEvent actionEvent) {
        try{
            main.showLoginScreen();
        }
        catch (Exception e){
            System.out.println("Error loading the Log In screen.");
        }
    }

    public void validate(UsernameValidator uv) {
        try{
            if(uv.getIsValid()){
                System.out.println("Created successfully.");
                main.showHomePage();
            }
            else{
                IDLabel1.setText("");
                IDLabel2.setText("");
                passwordLabel1.setText("");
                passwordLabel2.setText("");

                if(uv.getErrorType() == 4) {
                    IDLabel1.setText("Only characters(A-Z,a-z)");
                    IDLabel2.setText("and numbers allowed.");
                }
                else if(uv.getErrorType() == 5) {
                    IDLabel1.setText("Username must be of");
                    IDLabel2.setText("6-15 characters.");
                }
                else if(uv.getErrorType() == 1) {
                    IDLabel1.setText("Name already in use");
                    IDLabel2.setText("Try another name");
                }
                else if(uv.getErrorType() == 2) {
                    passwordLabel1.setText("Only characters(A-Z,a-z)");
                    passwordLabel2.setText("and numbers allowed.");
                }
                else if(uv.getErrorType() == 3) {
                    passwordLabel1.setText("Password must be of");
                    passwordLabel2.setText("6-15 characters");
                }
            }
        }
        catch(Exception e1){
            System.out.println("Error in registration");
        }
    }
}
