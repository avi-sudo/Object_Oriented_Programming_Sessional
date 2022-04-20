package Client;

import DataTypes.*;
import Server.NetworkUtil;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Client {

    private String name;
    private NetworkUtil nu;
    Main main;
    private UserData me;
    private boolean isSet = false; //true if me is set

    public Client(String serverAddress, int serverPort) {
        try {
            NetworkUtil nc = new NetworkUtil(serverAddress, serverPort);
            nu = nc;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean getIsSet() {
        return isSet;
    }

    public void resetIsSet() {
        isSet = false;
    }

    public void setMyself(UserData ud) {
        System.out.println("Setting myself");
        System.out.println("my rating = " + ud.getRating());
        me = ud;
        isSet = true;
    }

    public UserData getMyself() {
        return me;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Main getMain(){
        return this.main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkUtil getNetworkUtil(){
        return nu;
    }

    //to write or send anything to the server, no writeThread for now
    public void sendToServer(Object o){
        nu.write(o);
    }

    /*public Games getActiveGames() {
        try {
            System.out.println("Sending requester");
            sendToServer(new Requester("activeGames"));
            System.out.println("Sent");
            Games temp = (Games) getNetworkUtil().read();
            return temp;
        }
        catch (Exception e) {
            System.out.println("Error in sending the requester");
        }
        return null;
    }*/
}


