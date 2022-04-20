package Client;

import DataTypes.Move;
import DataTypes.Requester;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SpectateScreenController extends AnimationTimer {

    public Label player1Label;
    public Label player2Label;
    public Button nextButton;
    public Button previousButton;
    List<String> moves;
    String id, player1, player2;
    private boolean isAnimating = false;
    private boolean isGameOver = false;
    public Button leaveButton;
    public Label gameLabel;
    double source; //holds the y co-ordinate of the uppermost row
    double destinations[] = new double[6]; //holds the y co-ordinates of all rows
    Color myColor; //red for user1 and blue for user2
    Color backGroundColor = new Color(.847, .89, .769, 1);
    private int var=0; //variable used in animation
    public int col, dest;
    private long lastFrame;
    private int velocity = 8; //velocity of the falling piece
    public Circle circle00,circle01,circle02,circle03,circle04,circle05,circle06,
            circle10,circle11,circle12,circle13,circle14,circle15,circle16,
            circle20,circle21,circle22,circle23,circle24,circle25,circle26,
            circle30,circle31,circle32,circle33,circle34,circle35,circle36,
            circle40,circle41,circle42,circle43,circle44,circle45,circle46,
            circle50,circle51,circle52,circle53,circle54,circle55,circle56;
    public Circle tempCircle0;
    public Circle tempCircle1;
    public Circle tempCircle2;
    public Circle tempCircle3;
    public Circle tempCircle4;
    public Circle tempCircle5;
    public Circle tempCircle6;
    private int pieceCount[] = new int[7]; //stores how many pieces have been put in each column
    public AnchorPane vbox0;
    public AnchorPane vbox1;
    public AnchorPane vbox2;
    public AnchorPane vbox3;
    public AnchorPane vbox4;
    public AnchorPane vbox5;
    public AnchorPane vbox6;
    private Main main;
    private Client client;
    private int moveCount = 0;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    //initializes necessary variables
    public void initialize(String id, String filePath) {
        moves = new ArrayList<>();
        this.id = id;
        String tid;
        try {
            System.out.println("reading");
            String line;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while (true) {
                line = br.readLine();
                if (line == null) break;
                String[] temp;
                temp = line.split(",");
                tid = temp[0];
                if(tid.equals(id)) {
                    player1 = temp[1];
                    player2 = temp[2];
                    player1Label.setText(player1);
                    player2Label.setText(player2);
                    for(int i=3; i<temp.length; i++) {
                        moves.add(temp[i]);
                    }
                    break;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        moveCount = 0;
        myColor = Color.RED;
        gameLabel.setText(player1 + " to move");
        for(int i=0;i<7;i++) {
            pieceCount[i] = 0;
        }
        circle00.setFill(backGroundColor);
        circle01.setFill(backGroundColor);
        circle02.setFill(backGroundColor);
        circle03.setFill(backGroundColor);
        circle04.setFill(backGroundColor);
        circle05.setFill(backGroundColor);
        circle06.setFill(backGroundColor);
        circle10.setFill(backGroundColor);
        circle11.setFill(backGroundColor);
        circle12.setFill(backGroundColor);
        circle13.setFill(backGroundColor);
        circle14.setFill(backGroundColor);
        circle15.setFill(backGroundColor);
        circle16.setFill(backGroundColor);
        circle20.setFill(backGroundColor);
        circle21.setFill(backGroundColor);
        circle22.setFill(backGroundColor);
        circle23.setFill(backGroundColor);
        circle24.setFill(backGroundColor);
        circle25.setFill(backGroundColor);
        circle26.setFill(backGroundColor);
        circle30.setFill(backGroundColor);
        circle31.setFill(backGroundColor);
        circle32.setFill(backGroundColor);
        circle33.setFill(backGroundColor);
        circle34.setFill(backGroundColor);
        circle35.setFill(backGroundColor);
        circle36.setFill(backGroundColor);
        circle40.setFill(backGroundColor);
        circle41.setFill(backGroundColor);
        circle42.setFill(backGroundColor);
        circle43.setFill(backGroundColor);
        circle44.setFill(backGroundColor);
        circle45.setFill(backGroundColor);
        circle46.setFill(backGroundColor);
        circle50.setFill(backGroundColor);
        circle51.setFill(backGroundColor);
        circle52.setFill(backGroundColor);
        circle53.setFill(backGroundColor);
        circle54.setFill(backGroundColor);
        circle55.setFill(backGroundColor);
        circle56.setFill(backGroundColor);
        tempCircle0.setFill(backGroundColor);
        tempCircle1.setFill(backGroundColor);
        tempCircle2.setFill(backGroundColor);
        tempCircle3.setFill(backGroundColor);
        tempCircle4.setFill(backGroundColor);
        tempCircle5.setFill(backGroundColor);
        tempCircle6.setFill(backGroundColor);
        destinations[0] = circle00.getLayoutY();
        destinations[1] = circle10.getLayoutY();
        destinations[2] = circle20.getLayoutY();
        destinations[3] = circle30.getLayoutY();
        destinations[4] = circle40.getLayoutY();
        destinations[5] = circle50.getLayoutY();
        source = destinations[5];
        moveCount = 0;
        isAnimating = false;
        isGameOver = false;
    }

    public void startAnimation(int col, int dest) {
        this.col = col;
        this.dest = dest;
        start();
    }

    public void handle(long now) {
        move(now);
    }

    public void move(long now) {
        double elapsedSec;
        if(col == 0) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle0.setLayoutY(tempCircle0.getLayoutY() + velocity);
            }

            if(tempCircle0.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle0.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle00.setFill(myColor);
                }
                else if(dest == 1) {
                    circle10.setFill(myColor);
                }
                else if(dest == 2) {
                    circle20.setFill(myColor);
                }
                else if(dest == 3) {
                    circle30.setFill(myColor);
                }
                else if(dest == 4) {
                    circle40.setFill(myColor);
                }
                else if(dest == 5) {
                    circle50.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                //processMove(moves.get(moveCount));
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                isAnimating = false;
            }
        }
        else if(col == 1) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle1.setLayoutY(tempCircle1.getLayoutY() + velocity);
            }

            if(tempCircle1.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle1.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle01.setFill(myColor);
                }
                else if(dest == 1) {
                    circle11.setFill(myColor);
                }
                else if(dest == 2) {
                    circle21.setFill(myColor);
                }
                else if(dest == 3) {
                    circle31.setFill(myColor);
                }
                else if(dest == 4) {
                    circle41.setFill(myColor);
                }
                else if(dest == 5) {
                    circle51.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
        else if(col == 2) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle2.setLayoutY(tempCircle2.getLayoutY() + velocity);
            }

            if(tempCircle2.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle2.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle02.setFill(myColor);
                }
                else if(dest == 1) {
                    circle12.setFill(myColor);
                }
                else if(dest == 2) {
                    circle22.setFill(myColor);
                }
                else if(dest == 3) {
                    circle32.setFill(myColor);
                }
                else if(dest == 4) {
                    circle42.setFill(myColor);
                }
                else if(dest == 5) {
                    circle52.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
        else if(col == 3) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle3.setLayoutY(tempCircle3.getLayoutY() + velocity);
            }

            if(tempCircle3.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle3.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle03.setFill(myColor);
                }
                else if(dest == 1) {
                    circle13.setFill(myColor);
                }
                else if(dest == 2) {
                    circle23.setFill(myColor);
                }
                else if(dest == 3) {
                    circle33.setFill(myColor);
                }
                else if(dest == 4) {
                    circle43.setFill(myColor);
                }
                else if(dest == 5) {
                    circle53.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
        else if(col == 4) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle4.setLayoutY(tempCircle4.getLayoutY() + velocity);
            }

            if(tempCircle4.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle4.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle04.setFill(myColor);
                }
                else if(dest == 1) {
                    circle14.setFill(myColor);
                }
                else if(dest == 2) {
                    circle24.setFill(myColor);
                }
                else if(dest == 3) {
                    circle34.setFill(myColor);
                }
                else if(dest == 4) {
                    circle44.setFill(myColor);
                }
                else if(dest == 5) {
                    circle54.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    System.out.println("end of the game");
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
        else if(col == 5) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle5.setLayoutY(tempCircle5.getLayoutY() + velocity);
            }

            if(tempCircle5.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle5.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle05.setFill(myColor);
                }
                else if(dest == 1) {
                    circle15.setFill(myColor);
                }
                else if(dest == 2) {
                    circle25.setFill(myColor);
                }
                else if(dest == 3) {
                    circle35.setFill(myColor);
                }
                else if(dest == 4) {
                    circle45.setFill(myColor);
                }
                else if(dest == 5) {
                    circle55.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
        else if(col == 6) {
            elapsedSec = (now - lastFrame) / 10000000000000.0;
            var += (int) elapsedSec;
            if(var > 50) {
                var = 0;
                tempCircle6.setLayoutY(tempCircle6.getLayoutY() + velocity);
            }

            if(tempCircle6.getLayoutY() > destinations[dest]) {
                System.out.println("stopping animation");
                tempCircle6.setLayoutY(destinations[dest]);
                if(dest == 0) {
                    circle06.setFill(myColor);
                }
                else if(dest == 1) {
                    circle16.setFill(myColor);
                }
                else if(dest == 2) {
                    circle26.setFill(myColor);
                }
                else if(dest == 3) {
                    circle36.setFill(myColor);
                }
                else if(dest == 4) {
                    circle46.setFill(myColor);
                }
                else if(dest == 5) {
                    circle56.setFill(myColor);
                }
                stop();
                if(myColor.equals(Color.RED)) {
                    myColor = Color.BLUE;
                    gameLabel.setText(player2 + " to move");
                }
                else {
                    myColor = Color.RED;
                    gameLabel.setText(player1 + " to move");
                }
                if(moveCount == moves.size()-2) {
                    isGameOver = true;
                    processMove(moves.get(moveCount));
                }
                //processMove(moves.get(moveCount));
                isAnimating = false;
            }
        }
    }

    public void processMove(String move) {
        moveCount++;
        if(move.equals("cancel")) {
            isGameOver = true;
            if(moves.get(moves.size()-1).equals("1")) {
                gameLabel.setText(player2 + " quit");
            }
            else gameLabel.setText(player1 + " quit");
            isAnimating = false;
            return;
        }
        else if(move.equals("end")) {
            isGameOver = true;
            if(moves.get(moves.size()-1).equals("1")) {
                gameLabel.setText(player1 + " Won");
            }
            else gameLabel.setText(player2 + " Won");
            isAnimating = false;
            return;
        }
        else if(move.equals("draw")) {
            isGameOver = true;
            gameLabel.setText("Game drawn!!!");
            isAnimating = false;
            return;
        }
        else {
            pieceCount[Integer.parseInt(move)]++;
            if(move.equals("0")) {
                tempCircle0.setFill(myColor);
                tempCircle0.setLayoutY(81);
            }
            if(move.equals("1")) {
                tempCircle1.setFill(myColor);
                tempCircle1.setLayoutY(81);
            }
            if(move.equals("2")) {
                tempCircle2.setFill(myColor);
                tempCircle2.setLayoutY(81);
            }
            if(move.equals("3")) {
                tempCircle3.setFill(myColor);
                tempCircle3.setLayoutY(81);
            }
            if(move.equals("4")) {
                tempCircle4.setFill(myColor);
                tempCircle4.setLayoutY(81);
            }
            else if(move.equals("5")) {
                tempCircle5.setFill(myColor);
                tempCircle5.setLayoutY(81);
            }
            else if(move.equals("6")) {
                tempCircle6.setFill(myColor);
                tempCircle6.setLayoutY(81);
            }
            startAnimation(Integer.parseInt(move), pieceCount[Integer.parseInt(move)]-1);
        }
    }

    public void leaveGame(ActionEvent actionEvent) {
        try {
            main.showSpectateMenu();
        }
        catch (Exception e) {
            System.out.println("error loading the homepage");
        }
    }

    public void showPrevious(ActionEvent actionEvent) {
        if(moveCount > 0 && !isAnimating) {
            if(myColor.equals(Color.RED)) {
                myColor = Color.BLUE;
                gameLabel.setText(player2 + " to move");
            }
            else {
                myColor = Color.RED;
                gameLabel.setText(player1 + " to move");
            }
            isGameOver = false;
            isAnimating = false;
            moveCount--;
            String move = moves.get(moveCount);
            int count = -1;
            if(!(move.equals("cancel") || move.equals("end") || move.equals("draw"))) {
                pieceCount[Integer.parseInt(move)]--;
                count = pieceCount[Integer.parseInt(move)];
            }
            else {
                moveCount--;
                move = moves.get(moveCount);
                pieceCount[Integer.parseInt(move)]--;
                count = pieceCount[Integer.parseInt(move)];
            }

            if(move.equals("0")) {
                if(count == 0) circle00.setFill(backGroundColor);
                else if(count == 1) circle10.setFill(backGroundColor);
                else if(count == 2) circle20.setFill(backGroundColor);
                else if(count == 3) circle30.setFill(backGroundColor);
                else if(count == 4) circle40.setFill(backGroundColor);
                else if(count == 5) circle50.setFill(backGroundColor);
                tempCircle0.setFill(backGroundColor);
                tempCircle0.setLayoutY(81);
            }
            if(move.equals("1")) {
                if(count == 0) circle01.setFill(backGroundColor);
                else if(count == 1) circle11.setFill(backGroundColor);
                else if(count == 2) circle21.setFill(backGroundColor);
                else if(count == 3) circle31.setFill(backGroundColor);
                else if(count == 4) circle41.setFill(backGroundColor);
                else if(count == 5) circle51.setFill(backGroundColor);
                tempCircle1.setFill(backGroundColor);
                tempCircle1.setLayoutY(81);
            }
            if(move.equals("2")) {
                if(count == 0) circle02.setFill(backGroundColor);
                else if(count == 1) circle12.setFill(backGroundColor);
                else if(count == 2) circle22.setFill(backGroundColor);
                else if(count == 3) circle32.setFill(backGroundColor);
                else if(count == 4) circle42.setFill(backGroundColor);
                else if(count == 5) circle52.setFill(backGroundColor);
                tempCircle2.setFill(backGroundColor);
                tempCircle2.setLayoutY(81);
            }
            if(move.equals("3")) {
                if(count == 0) circle03.setFill(backGroundColor);
                else if(count == 1) circle13.setFill(backGroundColor);
                else if(count == 2) circle23.setFill(backGroundColor);
                else if(count == 3) circle33.setFill(backGroundColor);
                else if(count == 4) circle43.setFill(backGroundColor);
                else if(count == 5) circle53.setFill(backGroundColor);
                tempCircle3.setFill(backGroundColor);
                tempCircle3.setLayoutY(81);
            }
            if(move.equals("4")) {
                if(count == 0) circle04.setFill(backGroundColor);
                else if(count == 1) circle14.setFill(backGroundColor);
                else if(count == 2) circle24.setFill(backGroundColor);
                else if(count == 3) circle34.setFill(backGroundColor);
                else if(count == 4) circle44.setFill(backGroundColor);
                else if(count == 5) circle54.setFill(backGroundColor);
                tempCircle4.setFill(backGroundColor);
                tempCircle4.setLayoutY(81);
            }
            else if(move.equals("5")) {
                if(count == 0) circle05.setFill(backGroundColor);
                else if(count == 1) circle15.setFill(backGroundColor);
                else if(count == 2) circle25.setFill(backGroundColor);
                else if(count == 3) circle35.setFill(backGroundColor);
                else if(count == 4) circle45.setFill(backGroundColor);
                else if(count == 5) circle55.setFill(backGroundColor);
                tempCircle5.setFill(backGroundColor);
                tempCircle5.setLayoutY(81);
            }
            else if(move.equals("6")) {
                if(count == 0) circle06.setFill(backGroundColor);
                else if(count == 1) circle16.setFill(backGroundColor);
                else if(count == 2) circle26.setFill(backGroundColor);
                else if(count == 3) circle36.setFill(backGroundColor);
                else if(count == 4) circle46.setFill(backGroundColor);
                else if(count == 5) circle56.setFill(backGroundColor);
                tempCircle6.setFill(backGroundColor);
                tempCircle6.setLayoutY(81);
            }
        }
    }

    public void showNext(ActionEvent actionEvent) {
        if(!isAnimating && !isGameOver) {
            isAnimating = true;
            processMove(moves.get(moveCount));
        }
    }
}
