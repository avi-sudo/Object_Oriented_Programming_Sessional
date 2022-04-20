package Client;

import DataTypes.Game;
import DataTypes.LogOutData;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class HomePageController {
    public Button createButton;
    public Button joinButton;
    public Button spectateButton;
    public Label idLabel;
    public Label ratingLabel;
    public Button logOutButton;
    public AnchorPane AP;
    public ImageView iview;
    Main main;
    Client client;

    public void setLabels(String name, int rating) {
        idLabel.setText(name);
        ratingLabel.setText("Rating: " + rating);
    }

    public void setImage(String url) {
        System.out.println("Setting image from url");
        Image image = new Image(url);
        iview.setImage(image);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void showActiveGames(ActionEvent actionEvent) {
        try{
            main.showOpenGamesScreen();
        }
        catch (Exception e) {
            System.out.println("Error loading the active games screen");
            e.printStackTrace();
        }
    }

    public void createGame(ActionEvent actionEvent) {
        System.out.println("creating");
        Game game = new Game(client.getMyself());
        client.sendToServer(game);
        try{
            main.showWaitingScreen();
        }
        catch (Exception e) {
            System.out.println("Error loading waiting screen");
            e.printStackTrace();
        }
    }

    public void spectate(ActionEvent actionEvent) {
        try {
            main.showSpectateMenu();
        }
        catch (Exception e) {
            System.out.println("Error loading the spectate screen.");
            e.printStackTrace();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        try{
            client.sendToServer(new LogOutData(client.getMyself().getUserName()));
            client.resetIsSet(); //resets the isSet variable in client class to false
            main.showLoginScreen();
        }
        catch (Exception e) {
            System.out.println("Error in switching to login screen");
            e.printStackTrace();
        }
    }
}
