package Client;

import DataTypes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class OpenGamesScreenController {
    public Button refreshButton;
    public ListView<String> gameList;
    public Button joinButton;
    public Button cancelButton;
    private Main main;
    private Client client;

    public void showOpenGames(Games games) {
        System.out.println("Successfully read the games");
        int size = gameList.getItems().size();
        System.out.println("SIZE = " + size);
        if(size>0)
            gameList.getItems().remove(0, size);
        for(int i=0; i<games.getGames().size(); i++) {
            Game temp = games.getGames().get(i);
            gameList.getItems().add(temp.getUser1().getUserName() + "  " +
                    temp.getUser1().getRating());
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void refresh(ActionEvent actionEvent) {
        getOpenGames();
    }

    public void join(ActionEvent actionEvent) {
        if(gameList.getSelectionModel().getSelectedItem() != null) {
            String[] temp = gameList.getSelectionModel().getSelectedItem().split(" ");
            String host = temp[0];
            System.out.println("want to join " + host);
            //client.sendToServer(new Requester("join", host, client.getMyself()));
            client.sendToServer(new JoinGameRequester(host, client.getMyself()));
        }
    }

    public void cancel(ActionEvent actionEvent) {
        try{
            main.showHomePage();
        }
        catch (Exception e) {
            System.out.println("Error loading the home page");
            e.printStackTrace();
        }
    }

    public void getOpenGames() {
        try {
            System.out.println("Sending requester");
            client.sendToServer(new OpenGamesRequester());
            // client.sendToServer(new Requester("openGames"));
            System.out.println("Sent");
        }
        catch (Exception e) {
            System.out.println("Error in sending the requester");
        }
    }
}
