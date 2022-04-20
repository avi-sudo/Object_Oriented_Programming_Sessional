package Server;

import DataTypes.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private List<Game> games = new ArrayList<>(); //list of active games
    private List<UserData> clientList = new ArrayList<>();
    private HashMap<String, NetworkUtil> activeUsers = new HashMap<String, NetworkUtil>(); //list of all logged in users
    // and their network utility
    private ServerSocket serverSocket;
    private ServerReadThread[] readThreads = new ServerReadThread[100];
    private int clientCount = 0;
    private FileRW frw;
    String fileName;
    String fileName2;
    String fileName3;
    String dir;
    private int gameCount;

    Server(String fileDir, int port) {
        try {
            dir = fileDir;
            setFileName(fileDir);
            readNecessaryFiles();
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted");
                serve(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server starts:" + e);
        }
        finally {
            System.out.println("inside finally");
            System.out.println("closing server");
        }
    }

    public void setFileName(String dir) {
        //sets the directory of the needed files
        fileName = dir + "userDatas.txt";
        fileName2 = dir + "gameCount.txt";
        fileName3 = dir + "gamesDatabase.txt";
    }

    public void readNecessaryFiles() {
        frw = new FileRW();
        frw.fileRead(fileName, clientList);
        gameCount = frw.fileReadGameCount(fileName2);
        System.out.println("Game Count = " + gameCount);
    }

    public void serve(Socket clientSocket) {
        clientCount++;
        NetworkUtil nc = new NetworkUtil(clientSocket);
        readThreads[clientCount-1] = new ServerReadThread(nc);
        readThreads[clientCount-1].setServer(this);
        readThreads[clientCount-1].setImagePath(dir);
    }

    public UsernameValidator validateLogin(UserData ud) {
        //validate from the existing files
        //check if user is already logged in
        if(activeUsers.get(ud.getUserName()) != null) {
            //user is already logged in
            UsernameValidator uv = new UsernameValidator(false, "login", 2);
            return uv;
        }

        for(int i=0; i<clientList.size(); i++){
            UserData temp = clientList.get(i);
            if(ud.getUserName().equals(temp.getUserName()) &&
                    ud.getPassword().equals(temp.getPassword())){
                UsernameValidator uv = new UsernameValidator(true, "login", 0);
                return uv;
            }
        }

        UsernameValidator uv = new UsernameValidator(false, "login", 1);
        return uv;
    }

    public UsernameValidator validateRegistration(UserData ud) {
        //*****check username validity*****
        String name = ud.getUserName();
        //check if the length is correct
        if(name.length()<6 || name.length()>15) {
            UsernameValidator uv = new UsernameValidator(false, "registration",
                    5);
            return uv;
        }
        //check if there is any invalid character
        for(int i=0; i<name.length(); i++) {
            char temp = name.charAt(i);
            if(!((temp>='a' && temp<='z') || (temp>= 'A' && temp<= 'Z') ||
                    (temp>= '0' && temp<= '9'))) {
                UsernameValidator uv = new UsernameValidator(false, "registration",
                        4);
                return uv;
            }
        }
        //check if the username is already in use
        for(int i=0; i<clientList.size(); i++){
            UserData temp = clientList.get(i);
            if(name.equals(temp.getUserName())){
                UsernameValidator uv = new UsernameValidator(false, "registration",
                        1);
                return uv;
            }
        }

        //*****check password validity*****
        String pass = ud.getPassword();
        //check if the length is correct
        if(pass.length()<6 || pass.length()>15) {
            UsernameValidator uv = new UsernameValidator(false, "registration",
                    3);
            return uv;
        }
        //check if there is any invalid character
        for(int i=0; i<pass.length(); i++) {
            char temp = pass.charAt(i);
            if(!((temp>='a' && temp<='z') || (temp>= 'A' && temp<= 'Z') ||
                    (temp>= '0' && temp<= '9'))) {
                UsernameValidator uv = new UsernameValidator(false, "registration",
                        2);
                return uv;
            }
        }

        //*****no error found****
        UserData ud2 = new UserData(ud.getUserName(),ud.getPassword(),1300,0);
        clientList.add(ud2);
        frw.fileAppend(fileName, ud2);
        UsernameValidator uv = new UsernameValidator(true, "registration",
                0);
        return uv;
    }

    public void addToMap(String name, NetworkUtil nc){
        activeUsers.put(name, nc);
    }


    public void sendToClient(Object o, String name){ //sends object o to client with the given name
        NetworkUtil temp = activeUsers.get(name);
        temp.write(o);
    }

    public List<UserData> getClientList() {
        return clientList;
    }

    public void addNewGame(Game game) {
        games.add(game);
        game.setID(gameCount++);
        frw.fileWrite(fileName2, Integer.toString(gameCount));
        System.out.println("new game created");
    }

    //returns true if successful
    public boolean joinGame(String host, UserData challenger) {
        Game temp;
        for(int i=0; i<games.size(); i++) {
            temp = games.get(i);
            if(temp.getUser1().getUserName().equals(host) &&
                    !temp.getIsFull()) {
                temp.addUser2(challenger);
                System.out.println("joined the game successfully");
                sendToClient((boolean)true, host); //opponent found
                return true;
            }
        }
        return false;
    }

    public UserData getFromClientList(String name) {
        for(int i=0; i<clientList.size(); i++) {
            if(clientList.get(i).getUserName().equals(name))
                return clientList.get(i);
        }
        return null;
    }

    public void completeLogOut(String name) {
        activeUsers.remove(name);
        System.out.println("Removed " + name +  " successfully");
    }

    public Games getOpenGamesList() {
        System.out.println("in getOpenGamesList method");
        List<Game> openGames = new ArrayList<Game>();
        for(int i=0; i<games.size(); i++) {
            Game g = games.get(i);
            if(g.getIsFull() == false) {
                openGames.add(g);
                System.out.println("added " + g.getUser1().getUserName() + "'s game to" +
                        " the open games list");
            }
        }
        Games gm = new Games(openGames);
        return gm;
    }

    public Games getRunningGamesList() {
        System.out.println("in getRunningGamesList method");
        List<Game> runningGames = new ArrayList<Game>();
        for(int i=0; i<games.size(); i++) {
            Game g = games.get(i);
            if(g.getIsFull() == true) {
                runningGames.add(g);
                System.out.println("added " + g.getUser1().getUserName() + "'s game to" +
                        " the Running games list");
            }
        }
        Games gm = new Games(runningGames);
        return gm;
    }

    //delete active games of the user
    public void removeGame(String name) {
        try{
            for(int i=0; i<games.size(); i++) {
                if (games.get(i).getUser1().getUserName().equals(name)) {
                    System.out.println("Game of " + games.get(i).getUser1().getUserName() +
                            " removed successfully");
                    games.remove(i);
                }
                else if (games.get(i).getIsFull()) {
                    System.out.println("checking user2 in remove games");
                    if (games.get(i).getUser2().getUserName().equals(name)) {
                        System.out.println("Game of " + games.get(i).getUser2().getUserName() +
                                " removed successfully");
                        games.remove(i);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("error in removing the game");
            e.printStackTrace();
        }
    }

    public Game getCurrentGame(String player) {
        //returns the current game the player is in
        for(int i=0; i<games.size(); i++) {
            Game g = games.get(i);
            if(g.getUser1().getUserName().equals(player))
                return g;
            else if(g.getIsFull()) {
                if(g.getUser2().getUserName().equals(player)) {
                    return g;
                }
            }
        }
        return null;
    }

    public int getNewRating(int myRating, int opponentRating, double result) {
        //result-> 1 for win, 0 for loss, 0.5 for draw

        double e1 = myRating/400.0;
        double e2 = opponentRating/400.0;
        double R1 = java.lang.Math.pow(10, e1);
        double R2 = java.lang.Math.pow(10, e2);
        double expectedScore = R1/(R1+R2);
        int kfactor = 25; //higher factor means steeper change in ratings
        int newRating = (int) (myRating + (kfactor * (result - expectedScore)));
        return newRating;
    }

    public void addMove(Move move, String player) {
        for(int i=0; i<games.size(); i++) {
            Game g = games.get(i);
            if(g.getUser1().getUserName().equals(player)) {
                g.addMove(move);
                System.out.println("move added");
            }
            else if(g.getIsFull()) {
                if(g.getUser2().getUserName().equals(player)) {
                    g.addMove(move);
                    System.out.println("move added");
                }
            }
        }
    }

    public void updateRatings(String player, String opponent, String result) {
        System.out.println("will update ratings");
        UserData ud1 = getFromClientList(player);
        UserData ud2 = getFromClientList(opponent);
        int newRatingPlayer, newRatingOpponent;
        if(result.equals("end")) {
            System.out.println(player + " won");
            newRatingPlayer = getNewRating(ud1.getRating(), ud2.getRating(), 1);
            newRatingOpponent = getNewRating(ud2.getRating(), ud1.getRating(), 0);
        }
        else {
            newRatingPlayer = getNewRating(ud1.getRating(), ud2.getRating(), .5);
            newRatingOpponent = getNewRating(ud2.getRating(), ud1.getRating(), .5);
        }

        System.out.println("nrp = " + newRatingPlayer);
        System.out.println("nro = " + newRatingOpponent);

        for(int i=0; i<clientList.size(); i++) {
            if(clientList.get(i).equals(ud1)) {
                clientList.get(i).setRating(newRatingPlayer);
            }
            if(clientList.get(i).equals(ud2)) {
                clientList.get(i).setRating(newRatingOpponent);
            }
        }

        frw.fileWrite(fileName, clientList);

        try {
            sendToClient(new RatingUpdater(newRatingPlayer), player);
            sendToClient(new RatingUpdater(newRatingOpponent), opponent);
        }
        catch (Exception e) {
            System.out.println("error in sending data after rating updates");
        }
    }

    public void storeGame(Game game) {
        frw.fileAppend(fileName3, game);
    }

    public void sendFile(String name) {
        try {
            System.out.println("sending file");
            FileInputStream fis = new FileInputStream(fileName3);
            byte[] file = new byte[fis.available()];
            fis.read(file);
            activeUsers.get(name).write(new FileSender(file));
            System.out.println("Sent the file");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
