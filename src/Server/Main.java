package Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage myStage;
    ConnectionScreenController connectionScreenController;
    RunningScreenController runningScreenController;
    Server server;
    Runnable runnable;

    public static void main(String args[])
    {
        launch(args);
    }

    public void start(Stage primaryStage) {
        myStage = primaryStage;
        myStage.setTitle("Connect 4");
        myStage.setResizable(false);
        myStage.setOnCloseRequest(e -> exit());
        try {
            showConnectionScreen();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading the connection screen.");
        }
    }

    public void showConnectionScreen() throws  Exception {
        FXMLLoader fml = new FXMLLoader();
        fml.setLocation(getClass().getResource("ConnectionScreen.fxml"));
        Parent root = fml.load();
        // Loading the controller
        connectionScreenController = fml.getController();
        connectionScreenController.setMain(this);
        Scene scene = new Scene(root,1300,950);
        myStage.setScene(scene);
        myStage.show();
    }

    public void showRunningScreen() throws  Exception {
        FXMLLoader fml = new FXMLLoader();
        fml.setLocation(getClass().getResource("RunningScreen.fxml"));
        Parent root = fml.load();
        // Loading the controller
        runningScreenController = fml.getController();
        runningScreenController.setMain(this);
        Scene scene = new Scene(root,1300,950);
        myStage.setScene(scene);
        myStage.show();
        runningScreenController.startAnimation();
    }

    public void initiate(String fileDir, int port) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server(fileDir,port);
            }
        });
        thread.start();
        showRunningScreen();
    }

    public void exit() {
        System.out.println("Exiting");
        System.exit(0);
    }
}
