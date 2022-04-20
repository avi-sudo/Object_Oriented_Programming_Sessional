package Client;

import DataTypes.*;
import Server.NetworkUtil;
import Server.Server;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class ClientReadThread2 implements Runnable {
    private Thread thr;
    private NetworkUtil nc;
    private Main main;
    private Client client;
    private String type = "login";
    private LoginController loginController;
    private RegistrationController registrationController;
    private HomePageController homePageController;
    private WaitingScreenController waitingScreenController;
    private OpenGamesScreenController openGamesScreenController;
    private GameScreenController gameScreenController;
    private SpectateMenuController spectateMenuController;
    private SpectateScreenController spectateScreenController;
    private SpectateLiveScreenController spectateLiveScreenController;
    private boolean loop = true;
    private int count = 0;
    //String imagePath = "E:\\JavaProjects\\Connect Four\\src\\Client\\Files\\background.png";
    String imagePath = "background.png";
    String filePath = "gamesDatabase.txt";
    //String filePath = "E:\\JavaProjects\\Connect Four\\src\\Client\\Files\\gamesDatabase.txt";
    File file;

    public ClientReadThread2(NetworkUtil nc) {
        System.out.println("initialized enhanced Client read thread");
        this.nc = nc;
        this.thr = new Thread(this);
        thr.start();
    }

    public Thread getThread() {
        return thr;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setRegistrationController(RegistrationController registrationController) {
        this.registrationController = registrationController;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    public void setWaitingScreenController(WaitingScreenController waitingScreenController) {
        this.waitingScreenController = waitingScreenController;
    }

    public void setSpectateLiveScreenController(SpectateLiveScreenController spectateLiveScreenController) {
        this.spectateLiveScreenController = spectateLiveScreenController;
    }

    public void setOpenGamesScreenController(OpenGamesScreenController openGamesScreenController) {
        this.openGamesScreenController = openGamesScreenController;
    }

    public void setGameScreenController(GameScreenController gameScreenController) {
        this.gameScreenController = gameScreenController;
    }

    public void setSpectateMenuController(SpectateMenuController spectateMenuController) {
        this.spectateMenuController = spectateMenuController;
    }

    public void setSpectateScreenController(SpectateScreenController spectateScreenController) {
        this.spectateScreenController = spectateScreenController;
    }

    public void run() {
        try {
            while (loop == true) {
                Object o = nc.read();
                if(o != null)
                    System.out.println(o);

                if(o instanceof ImageSender) {
                    File file = new File(imagePath);
                    byte[] image = ((ImageSender)o).getImage();
                    FileOutputStream fos = new FileOutputStream(imagePath);
                    fos.write(image);
                    //File file = new File(imagePath);
                    System.out.println("file = " + file.toURI().toString());
                    main.setFileName(file.toURI().toString());
                    continue;
                }

                if(o instanceof RatingUpdater) {
                    int rating = ((RatingUpdater)o).getRating();
                    System.out.println("Will update the ratings with " + rating);
                    UserData ud = client.getMyself();
                    ud.setRating(rating);
                    client.setMyself(ud);
                    continue;
                }

                if(type.equals("login")) {
                    if(o instanceof UsernameValidator) {
                        UsernameValidator uv = (UsernameValidator) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    loginController.validate(uv);
                                }
                                catch (Exception e) {
                                    System.out.println("error in thread");
                                }
                            }
                        });
                    }
                    else if(o instanceof UserData) {
                        UserData ud = (UserData) o;
                        client.setMyself(ud);
                    }
                }
                else if(type.equals("registration")) {
                    if(o instanceof UsernameValidator) {
                        UsernameValidator uv = (UsernameValidator) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    registrationController.validate(uv);
                                }
                                catch (Exception e) {
                                    System.out.println("error in thread");
                                }
                            }
                        });
                    }
                    else if(o instanceof UserData) {
                        UserData ud = (UserData) o;
                        client.setMyself(ud);
                    }
                }
                else if(type.equals("homePage")) {
                    if(o instanceof RatingUpdater) {
                        int rating = ((RatingUpdater)o).getRating();
                        System.out.println("Will update the ratings with " + rating);
                        UserData ud = client.getMyself();
                        ud.setRating(rating);
                        client.setMyself(ud);
                    }
                }
                else if(type.equals("waitingscreen")) {
                    if(o instanceof Boolean) {
                        Boolean b = (Boolean) o;
                        if(b) {
                            System.out.println("Found true");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        System.out.println("Will go to game screen from" +
                                                " platform.runlater");
                                        main.showGameScreen(true); //true means he is the user1
                                    }
                                    catch (Exception e) {
                                        System.out.println("error in thread");
                                    }
                                }
                            });
                        }
                        else {
                            System.out.println("found false from immdediate response");
                        }
                    }
                }
                else if(type.equals("opengames")) {
                    if(o instanceof Games) {
                        System.out.println("FOUND GAMES");
                        Games g = (Games) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    openGamesScreenController.showOpenGames(g);
                                }
                                catch (Exception e) {
                                    System.out.println("error in thread");
                                }
                            }
                        });
                    }
                    else if(o instanceof Boolean) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Boolean isSuccessful = (Boolean) o;
                                if(isSuccessful) {
                                    System.out.println("will go to game screen now");
                                    try {
                                        main.showGameScreen(false);
                                    }
                                    catch (Exception e) {
                                        System.out.println("failed to load the game screen");
                                    }
                                }
                            }
                        });
                    }
                }
                else if(type.equals("gamescreen")) {
                    if(o instanceof Move) {
                        Move move = (Move) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    gameScreenController.processMove(move);
                                }
                                catch (Exception e) {
                                    System.out.println("error in thread");
                                }
                            }
                        });
                    }
                }
                else if(type.equals("spectatelivescreen")) {
                    if(o instanceof Game) {
                        System.out.println("Found a game");
                        Game g = (Game) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                spectateLiveScreenController.loadBoard(g);
                            }
                        });
                    }
                    if(o instanceof Move[]) {
                        Move[] moves = (Move[]) o;
                        try {
                            spectateLiveScreenController.addMoves(moves);
                        }
                        catch (Exception e) {
                            System.out.println("another error");
                            e.printStackTrace();
                        }
                    }
                    if(o instanceof Move) {
                        Move move = (Move) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                spectateLiveScreenController.processMove(move);
                            }
                        });
                    }
                }
                else if(type.equals("spectatemenu")) {
                    if(o instanceof FileSender) {
                        byte[] f = ((FileSender)o).getFile();
                        FileOutputStream fos = new FileOutputStream(filePath);
                        fos.write(f);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                spectateMenuController.showGames(filePath);
                            }
                        });
                    }
                    else if(o instanceof Games) {
                        System.out.println("FOUND RUNNING GAMES");
                        Games g = (Games) o;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    spectateMenuController.showLiveGames(g);
                                }
                                catch (Exception e) {
                                    System.out.println("error in thread");
                                }
                            }
                        });
                    }
                }
                else if(type.equals("spectatescreen")) {

                }
            }
            System.out.println("End of the thread");
        } catch (Exception e) {
            System.out.println("in the catch of client read thread");
            e.printStackTrace();
            loop = false;
        } finally {
            System.out.println("in clientreadthread's finally");
            return;
        }
    }
}





