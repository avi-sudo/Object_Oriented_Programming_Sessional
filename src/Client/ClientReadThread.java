package Client;

import DataTypes.*;
import Server.NetworkUtil;
import Server.Server;
import javafx.application.Platform;

public class ClientReadThread implements Runnable {
    private Thread thr;
    private NetworkUtil nc;
    GameScreenController gameScreenController;
    Client client;
    private Main main;
    private boolean loop = true; //thread runs while it is true
    //private String type;//if type == move, only then will it read strings

    public ClientReadThread(NetworkUtil nc) {
        System.out.println("initialized Client read thread");
        this.nc = nc;
      //  this.type = type;
        this.thr = new Thread(this);
        thr.start();
    }

    public Thread getThread() {
        return thr;
    }

    public void setLoop(boolean value) {
        loop = value;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void setGameScreenController(GameScreenController gameScreenController) {
        this.gameScreenController = gameScreenController;
    }

    public void run() {
        try {
            while (loop == true) {
                Object o = nc.read();
                if (o != null) {
                    if(o instanceof Boolean) {
                        loop = false;
                        Boolean b = (Boolean) o;
                        if(b) {
                            System.out.println("Found true");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try{
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
                            return;
                        }
                    }
                    else if(o instanceof Move) {
                        Move move = (Move) o;

                        if(move.getType().equals("cancel")) {
                            //opponent quit the game
                            loop = false;
                        }

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
                    /*else if(o instanceof String) {
                        gameScreenController.setOpponent((String) o);
                    }*/
                }
            }
            System.out.println("End of the thread");
        } catch (Exception e) {
            System.out.println("in the catch of client read thread");
            System.out.println(e);
            loop = false;
        } finally {
            System.out.println("in clientreadthread's finally");
            return;
        }
    }
}





