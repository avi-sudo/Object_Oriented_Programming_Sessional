package Client;

import DataTypes.LeaveGameRequester;
import DataTypes.Requester;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class WaitingScreenController extends AnimationTimer {
    public Circle circle2;
    public Circle circle3;
    public Button cancelButton;
    private Main main;
    private Client client;
    private static int var = 0;
    public int lastFrame;

    public void startAnimation() {
        start();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void cancel(ActionEvent actionEvent) {
        try{
            /*client.sendToServer(new Requester("cancelGame",
                    client.getMyself().getUserName()));
            System.out.println("Game cancelled");*/
            client.sendToServer(new LeaveGameRequester(client.getMyself().getUserName()));
            stop();
            main.showHomePage();
        }
        catch (Exception e) {
            System.out.println("error loading the home page");
            e.printStackTrace();
        }
        finally {
            System.out.println("in the finally of waiting screen controller");
        }
    }

    @Override
    public void handle(long now) {
        animate(now);
    }

    public void animate(long now) {
        double elapsedSec = (now - lastFrame) / 10000000000000.0;
        var += (int) elapsedSec;

        if(var<500){
            circle2.setRadius(0);
            circle3.setRadius(0);
        }
        if(var>500) {
            circle2.setRadius(7);
        }
        if(var>1000) {
            circle3.setRadius(7);
        }
        if (var>1500){
            var = 0;
        }
    }
}
