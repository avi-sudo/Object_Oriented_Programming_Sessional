package Client;

import DataTypes.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main extends Application {
    final static int SIZE = 10;  //used for the number of pages
    public Stage myStage;
    public Parent[] roots = new Parent[SIZE]; //all fxml files will be stored here
    public FXMLLoader[] loaders = new FXMLLoader[SIZE];
    Scene[] scenes = new Scene[SIZE];
    private Client client;
    private boolean isClientSet = false;
    WaitingScreenController waitingScreenController;
    HomePageController homePageController;
    LoginController loginController;
    RegistrationController registrationController;
    GameScreenController gameScreenController;
    OpenGamesScreenController openGamesScreenController;
    SpectateMenuController spectateMenuController;
    SpectateScreenController spectateScreenController;
    SpectateLiveScreenController spectateLiveScreenController;
    ConnectionScreenController connectionScreenController;
    ClientReadThread2 crt2;
    String fileName;

    @Override
    public void start(Stage primaryStage) throws Exception{
        myStage = primaryStage;
        myStage.setTitle("Connect 4");
        myStage.setResizable(false);
        myStage.setOnCloseRequest(e -> closeProperly()); //close the program properly
        showConnectionScreen();
    }

    public void setFileName(String name) {
        this.fileName = name;
        homePageController.setImage(fileName);
    }

    //obsolete for now
    public Client getClient() {
        return client;
    }

    public void showConnectionScreen() throws Exception {
        if(loaders[8] == null){
            loaders[8] = new FXMLLoader();
            loaders[8].setLocation(getClass().getResource("ConnectionScreen.fxml"));
            roots[8] = loaders[8].load();
            // Loading the controller
            connectionScreenController = loaders[8].getController();
            connectionScreenController.setMain(this);
            String fName = "Resources/Client/connectionscreenbackground.png";
            File file = new File(fName);
            connectionScreenController.setImage(file.toURI().toString());
        }
        changeScene(8);
    }

    public void showLoginScreen() throws Exception {
        if(loaders[0] == null){
            loaders[0] = new FXMLLoader();
            loaders[0].setLocation(getClass().getResource("LoginScreen.fxml"));
            roots[0] = loaders[0].load();
            // Loading the controller
            loginController = loaders[0].getController();
            loginController.setMain(this);
            loginController.setClient(client);
            crt2.setLoginController(loginController);
        }
        loginController.clearWritings();
        crt2.setType("login");
        changeScene(0);
    }

    public void showRegistrationScreen() throws Exception{
        if(loaders[1] == null){
            loaders[1] = new FXMLLoader();
            loaders[1].setLocation(getClass().getResource("RegistrationScreen.fxml"));
            roots[1] = loaders[1].load();
            // Loading the controller
            registrationController = loaders[1].getController();
            registrationController.setMain(this);
            registrationController.setClient(client);
            crt2.setRegistrationController(registrationController);
        }
        crt2.setType("registration");
        changeScene(1);
    }

    public void showHomePage() throws Exception {
        if(loaders[2] == null){
            loaders[2] = new FXMLLoader();
            loaders[2].setLocation(getClass().getResource("HomePage.fxml"));
            roots[2] = loaders[2].load();
            // Loading the controller
            homePageController = loaders[2].getController();
            homePageController.setMain(this);
            homePageController.setClient(client);
            crt2.setHomePageController(homePageController);
            //requesting image for background
            client.getNetworkUtil().write(new ImageRequester(client.getMyself().getUserName()));
        }
        homePageController.setLabels(client.getMyself().getUserName(),
                client.getMyself().getRating());
        crt2.setType("homepage");
        changeScene(2);
        System.out.println("Let's go to the home page");
    }

    public void showWaitingScreen() throws Exception {
        if(loaders[3] == null){
            loaders[3] = new FXMLLoader();
            loaders[3].setLocation(getClass().getResource("WaitingScreen.fxml"));
            roots[3] = loaders[3].load();
            // Loading the controller
            waitingScreenController = loaders[3].getController();
            waitingScreenController.setMain(this);
            waitingScreenController.setClient(client);
            crt2.setWaitingScreenController(waitingScreenController);
        }
        crt2.setType("waitingscreen");
        waitingScreenController.startAnimation();
        //waitingScreenController.initializeReadThread();
        changeScene(3);
        System.out.println("Let's go to the waiting screen");
    }

    public void showOpenGamesScreen() throws Exception {
        if(loaders[4] == null){
            loaders[4] = new FXMLLoader();
            loaders[4].setLocation(getClass().getResource("OpenGamesScreen.fxml"));
            roots[4] = loaders[4].load();
            // Loading the controller
            openGamesScreenController = loaders[4].getController();
            openGamesScreenController.setMain(this);
            openGamesScreenController.setClient(client);
            crt2.setOpenGamesScreenController(openGamesScreenController);
        }
        crt2.setType("opengames");
        openGamesScreenController.getOpenGames();
        changeScene(4);
        System.out.println("Let's go to the open games screen");
    }

    void showGameScreen(boolean value) throws Exception {
        if(loaders[5] == null) {
            loaders[5] = new FXMLLoader();
            loaders[5].setLocation(getClass().getResource("GameScreen.fxml"));
            roots[5] = loaders[5].load();
            gameScreenController = loaders[5].getController();
            gameScreenController.setMain(this);
            gameScreenController.setClient(client);
        }
        crt2.setGameScreenController(gameScreenController);
        crt2.setType("gamescreen");
        gameScreenController.setisMyMove(value); //true for user1 and false for user2
        gameScreenController.initialize();
        changeScene(5);
        System.out.println("Let's go to the gamescreen");
    }

    public void showSpectateMenu() throws Exception {
        if(loaders[6] == null){
            loaders[6] = new FXMLLoader();
            loaders[6].setLocation(getClass().getResource("SpectateMenuScreen.fxml"));
            roots[6] = loaders[6].load();
            // Loading the controller
            spectateMenuController = loaders[6].getController();
            spectateMenuController.setMain(this);
            spectateMenuController.setClient(client);
            crt2.setSpectateMenuController(spectateMenuController);
        }
        crt2.setType("spectatemenu");
        spectateMenuController.getGames();
        changeScene(6);
        System.out.println("Let's go to the spectate menu screen");
    }


    void showSpectateScreen(String id, String filePath) throws Exception {
        if(loaders[7] == null) {
            loaders[7] = new FXMLLoader();
            loaders[7].setLocation(getClass().getResource("SpectateScreen.fxml"));
            roots[7] = loaders[7].load();
        }
        spectateScreenController = loaders[7].getController();
        spectateScreenController.setMain(this);
        spectateScreenController.setClient(client);
        crt2.setSpectateScreenController(spectateScreenController);
        crt2.setType("spectatescreen");
        spectateScreenController.initialize(id, filePath);
        changeScene(7);
        System.out.println("Let's go to the spectatescreen");
    }

    public void showSpectateLiveScreen() throws Exception {
        if(loaders[9] == null) {
            loaders[9] = new FXMLLoader();
            loaders[9].setLocation(getClass().getResource("SpectateLiveScreen.fxml"));
            roots[9] = loaders[9].load();
            spectateLiveScreenController = loaders[9].getController();
            spectateLiveScreenController.setMain(this);
            spectateLiveScreenController.setClient(client);
            crt2.setSpectateLiveScreenController(spectateLiveScreenController);
        }
        crt2.setType("spectatelivescreen");
        spectateLiveScreenController.initialize();
        changeScene(9);
        System.out.println("Let's go to the spectatelivescreen");
    }

    //this method loads any scene
    public void changeScene(int i) {
        if(scenes[i] == null){
            scenes[i] = new Scene(roots[i],1300,950);
        }
        myStage.setScene(scenes[i]);
        myStage.show();
    }

    public void closeProperly() {
        try {
            if(client.getIsSet()) {
                client.sendToServer(new LogOutData(client.getMyself().getUserName()));
                client.sendToServer(new Move(-1, -1, "cancel",
                        client.getMyself().getUserName()));
                if(spectateLiveScreenController != null) {
                    client.sendToServer(new LeaveGameRequester(client.getMyself().getUserName(),
                            spectateLiveScreenController.player1));
                }
                client.sendToServer(new TerminationData(client.getMyself().getUserName()));
                //if the user was logged in, we send his name through the termination data,
                //so that all his running games can be handled properly
            }
            else {
                client.sendToServer(new TerminationData("default"));
            }
        }
        catch (Exception e) {
            System.out.println("not connected");
        }
        System.exit(0);
    }

    public void Connect(String ip, int port) {
        System.out.println("Connecting");
        String serverAddress = ip;
        int serverPort = port;
        boolean isSuccessful = false;

        try {
            client = new Client(serverAddress, serverPort);
            isSuccessful = client.getNetworkUtil().getStatus();
            System.out.println("success = "  + isSuccessful);
        }
        catch (Exception e) {
            connectionScreenController.showError();
        }
        if(isSuccessful) {
            client.setMain(this);
            crt2 = new ClientReadThread2(client.getNetworkUtil());
            crt2.setClient(client);
            crt2.setMain(this);
            try {
                showLoginScreen();
            }
            catch (Exception e) {
                System.out.println("error showing login screen");
            }
        }
    }

    public void showGamePage() {
        changeScene(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
