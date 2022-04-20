package Client;

import DataTypes.Move;
import DataTypes.Requester;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Timer;
import java.util.TimerTask;

public class GameScreenController extends AnimationTimer {

    Timer timer = new Timer();
    private boolean isChecked = true;
    public boolean hasGameEnded = false;
    private boolean shouldTimerRun = true;
    String[][] board = new String[6][7]; //holds the information of the board status
    public Button leaveButton;
    public Label gameLabel;
    public Label timerLabel;
    boolean isMouseClicked;
    double source; //holds the y co-ordinate of the uppermost row
    double destinations[] = new double[6]; //holds the y co-ordinates of all rows
    boolean isMyMove; //do anything if the variable is true
    Color myColor; //red for user1 and blue for user2
    Color backGroundColor = new Color(.847, .89, .769, 1);
    private int var=0; //variable used in animation
    private int col, dest;
    int time = 30; //controls the timer
    private long lastFrame;
    private int velocity = 10; //velocity of the falling piece
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
    public Circle upperCircle0;
    public Circle upperCircle1;
    public Circle upperCircle2;
    public Circle upperCircle3;
    public Circle upperCircle4;
    public Circle upperCircle5;
    public Circle upperCircle6;
    private Main main;
    private Client client;
    private String opponent = null; //name of the opponent
    private boolean additonalTracker;
    private boolean isInitialized = false;
    private boolean isTimerCreated = false;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
        System.out.println("opponent = " + opponent);
    }

    //initializes necessary variables
    public void initialize() {
        if(!isInitialized) {
            isInitialized = true;

            System.out.println("initializing");

            for(int i=0;i<6;i++) {
                for(int j=0;j<7;j++) {
                    board[i][j] = "empty";
                }
            }

            for(int i=0; i<7; i++)
                pieceCount[i] = 0;

            hasGameEnded = false;
            shouldTimerRun = true;
            isChecked = true;
            time = 30;
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
            hasGameEnded = false;
            System.out.println("Calling start timer from initialize");
            if(!isTimerCreated) {
                isTimerCreated = true;
                startTimer();
            }
        }
    }

    public void startAnimation(int col, int dest) {
        this.col = col;
        this.dest = dest;
        //updating the board
        String moveColour;
        if(myColor.equals(Color.RED))
            moveColour = "red";
        else moveColour = "blue";
        board[dest][col] = moveColour;
        System.out.println("updated board " + dest + ' ' + col + " with " + moveColour);
        isChecked = false;
        time = 0;
        start();
    }

    public void setisMyMove(boolean isMyMove) {
        this.isMyMove = isMyMove;
        this.isMouseClicked = !isMyMove;

        if(isMyMove) {
            myColor = Color.RED;
            gameLabel.setText("Your move!!!");
        }
        else {
            myColor = Color.BLUE;
            gameLabel.setText("Opponent's move!!!");
        }

        isInitialized = false;
    }

    public void changeColourBackForced() {
        //to change to background colour immediately after user makes a move
        upperCircle1.setFill(backGroundColor);
        upperCircle2.setFill(backGroundColor);
        upperCircle3.setFill(backGroundColor);
        upperCircle4.setFill(backGroundColor);
        upperCircle5.setFill(backGroundColor);
        upperCircle6.setFill(backGroundColor);
        upperCircle0.setFill(backGroundColor);
        upperCircle1.setStroke(backGroundColor);
        upperCircle2.setStroke(backGroundColor);
        upperCircle3.setStroke(backGroundColor);
        upperCircle4.setStroke(backGroundColor);
        upperCircle5.setStroke(backGroundColor);
        upperCircle6.setStroke(backGroundColor);
        upperCircle0.setStroke(backGroundColor);
    }

    public void changeColour(MouseEvent mouseEvent) {
        if(isMyMove) {
            if(mouseEvent.getSource().equals(vbox0)) {
                upperCircle0.setFill(myColor);
                upperCircle0.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox1)) {
                upperCircle1.setFill(myColor);
                upperCircle1.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox2)) {
                upperCircle2.setFill(myColor);
                upperCircle2.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox3)) {
                upperCircle3.setFill(myColor);
                upperCircle3.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox4)) {
                upperCircle4.setFill(myColor);
                upperCircle4.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox5)) {
                upperCircle5.setFill(myColor);
                upperCircle5.setStroke(Color.BLACK);
            }
            if(mouseEvent.getSource().equals(vbox6)) {
                upperCircle6.setFill(myColor);
                upperCircle6.setStroke(Color.BLACK);
            }
        }
    }

    public void changeColourBack(MouseEvent mouseEvent) {
        //the background color
        upperCircle1.setFill(backGroundColor);
        upperCircle2.setFill(backGroundColor);
        upperCircle3.setFill(backGroundColor);
        upperCircle4.setFill(backGroundColor);
        upperCircle5.setFill(backGroundColor);
        upperCircle6.setFill(backGroundColor);
        upperCircle0.setFill(backGroundColor);
        upperCircle1.setStroke(backGroundColor);
        upperCircle2.setStroke(backGroundColor);
        upperCircle3.setStroke(backGroundColor);
        upperCircle4.setStroke(backGroundColor);
        upperCircle5.setStroke(backGroundColor);
        upperCircle6.setStroke(backGroundColor);
        upperCircle0.setStroke(backGroundColor);
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
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
                isMouseClicked = false;
                if(!additonalTracker) {
                    isMyMove = true;
                    if(myColor.equals(Color.RED)) {
                        myColor = Color.BLUE;
                    }
                    else myColor = Color.RED;
                }
                time = 30;
                shouldTimerRun = true;
                checkBoard();
                stop();
            }
        }
    }

    public void dropPiece(MouseEvent mouseEvent) {
        if(isMyMove) {
            additonalTracker = true;
            if(isMouseClicked == false) {
                isMouseClicked = true;
                if(mouseEvent.getSource().equals(upperCircle0) ||
                        mouseEvent.getSource().equals(vbox0)) {
                    if(pieceCount[0] < 6) {
                        tempCircle0.setFill(myColor);
                        tempCircle0.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[0]++;
                        client.sendToServer(new Move(0,pieceCount[0]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(0,pieceCount[0]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle1) ||
                        mouseEvent.getSource().equals(vbox1)) {
                    if(pieceCount[1] < 6) {
                        tempCircle1.setFill(myColor);
                        tempCircle1.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[1]++;
                        client.sendToServer(new Move(1,pieceCount[1]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(1,pieceCount[1]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle2) ||
                        mouseEvent.getSource().equals(vbox2)) {
                    if(pieceCount[2] < 6) {
                        tempCircle2.setFill(myColor);
                        tempCircle2.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[2]++;
                        client.sendToServer(new Move(2,pieceCount[2]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(2,pieceCount[2]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle3) ||
                        mouseEvent.getSource().equals(vbox3)) {
                    if(pieceCount[3] < 6) {
                        tempCircle3.setFill(myColor);
                        tempCircle3.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[3]++;
                        client.sendToServer(new Move(3,pieceCount[3]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(3,pieceCount[3]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle4) ||
                        mouseEvent.getSource().equals(vbox4)) {
                    if(pieceCount[4] < 6) {
                        tempCircle4.setFill(myColor);
                        tempCircle4.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[4]++;
                        client.sendToServer(new Move(4,pieceCount[4]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(4,pieceCount[4]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle5) ||
                        mouseEvent.getSource().equals(vbox5)) {
                    if(pieceCount[5] < 6) {
                        tempCircle5.setFill(myColor);
                        tempCircle5.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[5]++;
                        client.sendToServer(new Move(5,pieceCount[5]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(5,pieceCount[5]-1);
                    }
                    else isMouseClicked = false;
                }
                if(mouseEvent.getSource().equals(upperCircle6) ||
                        mouseEvent.getSource().equals(vbox6)) {
                    if(pieceCount[6] < 6) {
                        tempCircle6.setFill(myColor);
                        tempCircle6.setLayoutY(81);
                        changeColourBackForced();
                        isMyMove = false;
                        pieceCount[6]++;
                        client.sendToServer(new Move(6,pieceCount[6]-1,"normal",
                                client.getMyself().getUserName()));
                        shouldTimerRun = false;
                        startAnimation(6,pieceCount[6]-1);
                    }
                    else isMouseClicked = false;
                }
            }
        }
    }

    public void processMove(Move move) {
        if(move.getType().equals("cancel")) {
            //the opponent quit the game
            hasGameEnded = true;
            gameLabel.setText("You Won!!");
            time = 0;
            isMyMove = false;
            isMouseClicked = true;
            client.sendToServer(new Move(-1, -1, "end",
                    client.getMyself().getUserName()));
        }
        else {
            additonalTracker = false;
            pieceCount[move.getColumn()]++;
            if(myColor.equals(Color.RED)) {
                myColor = Color.BLUE;
            }
            else {
                myColor = Color.RED;
            }
            if(move.getColumn() == 0) {
                tempCircle0.setFill(myColor);
                tempCircle0.setLayoutY(81);
            }
            else if(move.getColumn() == 1) {
                tempCircle1.setFill(myColor);
                tempCircle1.setLayoutY(81);
            }
            else if(move.getColumn() == 2) {
                tempCircle2.setFill(myColor);
                tempCircle2.setLayoutY(81);
            }
            else if(move.getColumn() == 3) {
                tempCircle3.setFill(myColor);
                tempCircle3.setLayoutY(81);
            }
            else if(move.getColumn() == 4) {
                tempCircle4.setFill(myColor);
                tempCircle4.setLayoutY(81);
            }
            else if(move.getColumn() == 5) {
                tempCircle5.setFill(myColor);
                tempCircle5.setLayoutY(81);
            }
            else if(move.getColumn() == 6) {
                tempCircle6.setFill(myColor);
                tempCircle6.setLayoutY(81);
            }
            shouldTimerRun = false;
            startAnimation(move.getColumn(), move.getRow());
        }
    }

    public void leaveGame(ActionEvent actionEvent) {
        if(!hasGameEnded) {
            client.sendToServer(new Move(-1,-1,"cancel",
                    client.getMyself().getUserName()));
            System.out.println("game removed");
        }

        try {
            isInitialized = false;
            main.showHomePage();
        }
        catch (Exception e) {
            System.out.println("error loading the homepage");
        }
    }

    public int isGameOver(String colour) {
        //checks the board
        //returns 0 for no
        //returns 1 for yes
        //returns -1 for draw

        //***checking the rows first***
        for(int i=0; i<6; i++) {
            for(int j=0; j<4; j++) {
                if(board[i][j].equals(colour) && board[i][j+1].equals(colour) &&
                        board[i][j+2].equals(colour) && board[i][j+3].equals(colour)) {
                    return 1;
                }
            }
        }

        //***checking the columns now***
        for(int j=0; j<7; j++) {
            for(int i=0; i<3; i++) {
                if(board[i][j].equals(colour) && board[i+1][j].equals(colour) &&
                        board[i+2][j].equals(colour) && board[i+3][j].equals(colour)) {
                    return 1;
                }
            }
        }

        //***checking the diagonals now***
        //24 cases to be checked
        if((board[0][3].equals(colour) && board[1][4].equals(colour) && board[2][5].equals(colour)
                && board[3][6].equals(colour)) ||
            (board[0][2].equals(colour) && board[1][3].equals(colour) && board[2][4].equals(colour)
                    && board[3][5].equals(colour)) ||
            (board[1][3].equals(colour) && board[2][4].equals(colour) && board[3][5].equals(colour)
                    && board[4][6].equals(colour)) ||
            (board[0][1].equals(colour) && board[1][2].equals(colour) && board[2][3].equals(colour)
                    && board[3][4].equals(colour)) ||
            (board[1][2].equals(colour) && board[2][3].equals(colour) && board[3][4].equals(colour)
                    && board[4][5].equals(colour)) ||
            (board[2][3].equals(colour) && board[3][4].equals(colour) && board[4][5].equals(colour)
                    && board[5][6].equals(colour)) ||
            (board[0][0].equals(colour) && board[1][1].equals(colour) && board[2][2].equals(colour)
                    && board[3][3].equals(colour)) ||
            (board[1][1].equals(colour) && board[2][2].equals(colour) && board[3][3].equals(colour)
                    && board[4][4].equals(colour)) ||
            (board[2][2].equals(colour) && board[3][3].equals(colour) && board[4][4].equals(colour)
                    && board[5][5].equals(colour)) ||
            (board[1][0].equals(colour) && board[2][1].equals(colour) && board[3][2].equals(colour)
                    && board[4][3].equals(colour)) ||
            (board[2][1].equals(colour) && board[3][2].equals(colour) && board[4][3].equals(colour)
                    && board[5][4].equals(colour)) ||
            (board[2][0].equals(colour) && board[3][1].equals(colour) && board[4][2].equals(colour)
                    && board[5][3].equals(colour)) ||
            (board[0][3].equals(colour) && board[1][2].equals(colour) && board[2][1].equals(colour)
                    && board[3][0].equals(colour)) ||
            (board[0][4].equals(colour) && board[1][3].equals(colour) && board[2][2].equals(colour)
                    && board[3][1].equals(colour)) ||
            (board[1][3].equals(colour) && board[2][2].equals(colour) && board[3][1].equals(colour)
                    && board[0][4].equals(colour)) ||
            (board[0][5].equals(colour) && board[1][4].equals(colour) && board[2][3].equals(colour)
                    && board[3][2].equals(colour)) ||
            (board[1][4].equals(colour) && board[2][3].equals(colour) && board[3][2].equals(colour)
                    && board[4][1].equals(colour)) ||
            (board[2][3].equals(colour) && board[3][2].equals(colour) && board[4][1].equals(colour)
                    && board[5][0].equals(colour)) ||
            (board[0][6].equals(colour) && board[1][5].equals(colour) && board[2][4].equals(colour)
                    && board[3][3].equals(colour)) ||
            (board[1][5].equals(colour) && board[2][4].equals(colour) && board[3][3].equals(colour)
                && board[4][2].equals(colour)) ||
            (board[2][4].equals(colour) && board[3][3].equals(colour) && board[4][2].equals(colour)
                    && board[5][1].equals(colour)) ||
            (board[1][6].equals(colour) && board[2][5].equals(colour) && board[3][4].equals(colour)
                    && board[4][3].equals(colour)) ||
            (board[2][5].equals(colour) && board[3][4].equals(colour) && board[4][3].equals(colour)
                    && board[5][2].equals(colour)) ||
            (board[2][6].equals(colour) && board[3][5].equals(colour) && board[4][4].equals(colour)
                    && board[5][3].equals(colour))) {
            return 1;
        }

        //***checking for draw now***
        boolean isDraw = true;
        for(int i=0; i<7; i++) {
            if(pieceCount[i] < 6) {
                isDraw = false;
                break;
            }
        }

        if(isDraw)
            return -1;

        //***game is not over yet
        return 0;
    }

    public void checkBoard() {
        System.out.println("Checking");
        int check;
        String checkingColour;
        if(!isMyMove) {
            gameLabel.setText("opponent's move");
            //it was the user's move
            if(myColor.equals(Color.RED))
                checkingColour = "red";
            else checkingColour = "blue";
            check = isGameOver(checkingColour);
            if(check == 1) {
                //user won the game
                isMyMove = false;
                isMouseClicked = true;
                //update ratings
                gameLabel.setText("You Won!!");
                if(!hasGameEnded) {
                    client.sendToServer(new Move(-1, -1, "end",
                            client.getMyself().getUserName()));
                }
                hasGameEnded = true;
                time = 0;
            }
            else if(check == -1) {
                //game was drawn
                isMyMove = false;
                isMouseClicked = true;
                //update ratings
                gameLabel.setText("Game was drawn!!");
                if(!hasGameEnded) {
                    client.sendToServer(new Move(-1, -1, "draw",
                            client.getMyself().getUserName()));
                }
                hasGameEnded = true;
                time = 0;
            }
        }
        else {
            //it was opponent's move
            gameLabel.setText("Your move");
            if(myColor.equals(Color.RED))
                checkingColour = "blue";
            else checkingColour = "red";
            check = isGameOver(checkingColour);
            if(check == 1) {
                //user lost the game
                hasGameEnded = true;
                isMyMove = false;
                isMouseClicked = true;
                //update ratings
                gameLabel.setText("You Lost!!");
                time = 0;
            }
            else if(check == -1) {
                //game was drawn
                hasGameEnded = true;
                isMyMove = false;
                isMouseClicked = true;
                //update ratings
                gameLabel.setText("Game was drawn!!");
                time = 0;
            }
        }
    }

    public void startTimer() {
        time = 30;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(time > 0 && !hasGameEnded && shouldTimerRun) {
                            timerLabel.setText("" + time);
                            time--;
                        }
                        else if(isMyMove && !hasGameEnded && shouldTimerRun) {
                            shouldTimerRun = false;
                            //send a random move
                            int tempVar = 0;
                            for(int i=0; i<7; i++) {
                                if(pieceCount[i] < 6) {
                                    tempVar = i;
                                    break;
                                }
                            }
                            if(tempVar == 0) {
                                tempCircle0.setFill(myColor);
                                tempCircle0.setLayoutY(81);
                            }
                            else if(tempVar == 1) {
                                tempCircle1.setFill(myColor);
                                tempCircle1.setLayoutY(81);
                            }
                            else if(tempVar == 2) {
                                tempCircle2.setFill(myColor);
                                tempCircle2.setLayoutY(81);
                            }
                            else if(tempVar == 3) {
                                tempCircle3.setFill(myColor);
                                tempCircle3.setLayoutY(81);
                            }
                            else if(tempVar == 4) {
                                tempCircle4.setFill(myColor);
                                tempCircle4.setLayoutY(81);
                            }
                            else if(tempVar == 5) {
                                tempCircle5.setFill(myColor);
                                tempCircle5.setLayoutY(81);
                            }
                            else if(tempVar == 6) {
                                tempCircle6.setFill(myColor);
                                tempCircle6.setLayoutY(81);
                            }

                            changeColourBackForced();
                            isMyMove = false;
                            additonalTracker = true; //******
                            isMouseClicked = true;  //******
                            pieceCount[tempVar]++;
                            client.sendToServer(new Move(tempVar,
                                    pieceCount[tempVar]-1,"normal",
                                    client.getMyself().getUserName()));
                            startAnimation(tempVar,pieceCount[tempVar]-1);
                        }
                        else if(shouldTimerRun) {
                            shouldTimerRun = false;
                        }
                    }
                });
            }
        };

        try{
            timer.schedule(timerTask, 0, 1000);
        }
        catch (Exception e) {
            System.out.println("error in timer schedule");
            e.printStackTrace();
        }
    }
}