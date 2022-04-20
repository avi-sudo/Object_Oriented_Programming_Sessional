package Client;

import DataTypes.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SpectateMenuController {
    public Button cancelButton;
    public Button viewButton;
    public ListView<String> gameList;
    public TextField filterBox;
    public Button filterButton;
    public ListView<String> liveGameList;
    private Main main;
    private Client client;
    private String filePath;
    private String filter = "";

    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void showGames(String filePath) {
        this.filePath = filePath;
        List<String> games = new ArrayList<>();
        String id, player1, player2;
        try {
            System.out.println("reading");
            String line;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while (true) {
                line = br.readLine();
                if (line == null) break;
                String[] temp;
                temp = line.split(",");
                id = temp[0];
                player1 = temp[1];
                player2 = temp[2];
                String result = temp[temp.length-1];
                if(filter.equals("")) {
                    if(result.equals("1"))
                        games.add(id + " "  + player1 + " vs " + player2 + " (1-0)");
                    else if(result.equals("2"))
                        games.add(id + " "  + player1 + " vs " + player2 + " (0-1)");
                    else
                        games.add(id + " "  + player1 + " vs " + player2 + " (0.5-0.5)");
                }
                else if(filter.equals(player1) || filter.equals(player2))
                    if(result.equals("1"))
                        games.add(id + " "  + player1 + " vs " + player2 + " (1-0)");
                    else if(result.equals("2"))
                        games.add(id + " "  + player1 + " vs " + player2 + " (0-1)");
                    else
                        games.add(id + " "  + player1 + " vs " + player2 + " (0.5-0.5)");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int size = gameList.getItems().size();
        System.out.println("SIZE = " + size);
        if(size>0)
            gameList.getItems().remove(0, size);
        for(int i=0; i<games.size(); i++) {
            gameList.getItems().add(games.get(i));
        }
    }

    public void showLiveGames(Games games) {
        System.out.println("Successfully read the games");
        int size = liveGameList.getItems().size();
        System.out.println("SIZE = " + size);
        if(size>0)
            liveGameList.getItems().remove(0, size);
        for(int i=0; i<games.getGames().size(); i++) {
            Game temp = games.getGames().get(i);
            liveGameList.getItems().add(temp.getUser1().getUserName() +  " (" +
                    temp.getUser1().getRating() +")" + " vs " +
                    temp.getUser2().getUserName() + " (" +
                    temp.getUser2().getRating() +")");
        }
    }

    public void getGames() {
        client.getNetworkUtil().write(new FileRequester(client.getMyself().getUserName()));
        System.out.println("File requester sent.");
    }

    public void view(ActionEvent actionEvent) {
        if(gameList.getSelectionModel().getSelectedItem() != null) {
            String[] temp = gameList.getSelectionModel().getSelectedItem().split(" ");
            String id = temp[0];
            try {
                main.showSpectateScreen(id, filePath);
            }
            catch (Exception e) {
                System.out.println("error loading the spectate screen");
                e.printStackTrace();
            }
        }
    }

    public void cancel(ActionEvent actionEvent) {
        try {
            main.showHomePage();
        }
        catch (Exception e) {
            System.out.println("Error loading the homepage");
        }
    }

    public void filter(ActionEvent actionEvent) {
        filter = filterBox.getText();
        showGames(filePath);
    }

    public void view2(ActionEvent actionEvent) {
        if(liveGameList.getSelectionModel().getSelectedItem() != null) {
            String[] temp = liveGameList.getSelectionModel().getSelectedItem().split(" ");
            String id = temp[0];
            try {
                //trying to view the game
                main.showSpectateLiveScreen();
                client.sendToServer(new JoinGameRequester(id, client.getMyself().getUserName()));
            }
            catch (Exception e) {
                System.out.println("error loading the spectateLive screen");
                e.printStackTrace();
            }
        }
    }
}
