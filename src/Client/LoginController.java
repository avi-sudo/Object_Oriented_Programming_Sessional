package Client;

import DataTypes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LoginController {
    public Label errorLabel1;
    public Label errorLabel2;
    private Client client;
    public Circle blueCircle;
    public Circle redCircle;
    public Button loginButton;
    public PasswordField passwordBox;
    public TextField idBox;
    public Label createAccountLabel;
    private boolean colourTrack = true;
    Main main;

    public void setMain(Main main){
        this.main = main;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void changeColour(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == blueCircle){
            if(colourTrack){
                blueCircle.setFill(Color.RED);
                colourTrack = false;
            }
            else{
                blueCircle.setFill(Color.BLUE);
                colourTrack = true;
            }
        }
        else if(mouseEvent.getSource() == redCircle){
            if(colourTrack){
                redCircle.setFill(Color.BLUE);
                colourTrack = false;
            }
            else{
                redCircle.setFill(Color.RED);
                colourTrack = true;
            }
        }
        else if(mouseEvent.getSource() == createAccountLabel){
            if(colourTrack){
                createAccountLabel.setTextFill(Color.PURPLE);
                colourTrack = false;
            }
            else{
                createAccountLabel.setTextFill(new Color(.153,.188,.404,
                        1));
                colourTrack = true;
            }
        }
    }

    public void tryLogin(ActionEvent actionEvent) {
       client.sendToServer(new UserData(idBox.getText(), passwordBox.getText(), 0, 1));
    }

    public void createAccount(MouseEvent mouseEvent) {
        System.out.println("switch to scene 1 here");
        try{
            idBox.clear();
            passwordBox.clear();
            main.showRegistrationScreen();
        }
        catch(Exception e){
            System.out.println("Cannot show the registration screen.");
        }
    }

    public void showLoginFailedAlert(int errorType){
        if(errorType == 1) {
            System.out.println("incorrect credentials");
            errorLabel1.setText("The username and password you");
            errorLabel2.setText("provided is not correct.");
        }
        else {
            System.out.println("User is already logged in");
            errorLabel1.setText("The user is already logged in.");
        }
    }

    public void validate(UsernameValidator uv) {
        try {
            if(uv.getIsValid()) {
                System.out.println("Logged in succesfully");
                passwordBox.clear();
                main.showHomePage();
            }
            else {
                System.out.println("Log in failed.");
                showLoginFailedAlert(uv.getErrorType());
            }
        }
        catch (Exception e) {
            System.out.println("error in validate method");
            e.printStackTrace();
        }
    }

    public void clearWritings() {
        //clears the error labels and the passsword field
        passwordBox.clear();
        errorLabel1.setText("");
        errorLabel2.setText("");
    }
}
