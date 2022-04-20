package Server;

import DataTypes.LeaveGameRequester;
import DataTypes.Requester;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RunningScreenController extends AnimationTimer {
    public Circle circle2;
    public Circle circle3;
    public Button terminateButton;
    public Label activeUsersLabel;
    private Main main;
    private static int var = 0;
    public int lastFrame;

    public void setLabels(String l1) {
        if(!l1.equals("-1")) {
            activeUsersLabel.setText(l1);
        }
    }

    public void startAnimation() {
        start();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void terminate(ActionEvent actionEvent) {
        try{
            main.exit();
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
