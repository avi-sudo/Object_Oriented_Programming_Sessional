package Server;

import DataTypes.*;
import com.sun.org.apache.regexp.internal.RE;
import sun.rmi.runtime.Log;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class ServerReadThread implements Runnable {
    private Thread thr;
    private NetworkUtil nc;
    Server server;
    private boolean flag = true;
    boolean isImageSent = false;
    String imagePath;

    public ServerReadThread(NetworkUtil nc) {
        System.out.println("initialized server read thread");
        this.nc = nc;
        this.thr = new Thread(this);
        thr.start();
    }

    public void setImagePath(String path) {
        imagePath = path + "background.png";
    }

    public void setServer(Server server){
        this.server = server;
    }

    public void run() {
        try {
            while (flag == true) {
                Object o = nc.read();
                if (o != null) {
                    if(o instanceof UserData){
                        System.out.println("User data found.");
                        UserData ud = (UserData) o;
                        if(ud.getType() == 1){
                            UsernameValidator uv = server.validateLogin(ud);
                            nc.write(uv);
                            System.out.println("SENT uv");
                            if(uv.getIsValid()){
                                //adding the username of the successfully logged in user to the map
                                server.addToMap(ud.getUserName(),nc);
                                UserData temp = server.getFromClientList(ud.getUserName());
                                nc.write(temp);
                            }
                            //Sending the details of the user
                        }
                        else if(ud.getType() == 2){
                            UsernameValidator uv = server.validateRegistration(ud);
                            nc.write(uv);
                            if(uv.getIsValid()){
                                //adding the username of the successfully logged in user to the map
                                server.addToMap(ud.getUserName(),nc);
                                UserData temp = server.getFromClientList(ud.getUserName());
                                nc.write(temp);
                            }
                        }
                    }

                    if(o instanceof Game) {
                        server.addNewGame((Game) o);
                    }

                    if(o instanceof LogOutData) {
                        System.out.println("found logout data");
                        LogOutData lod = (LogOutData) o;
                        server.completeLogOut(lod.getName());
                    }

                    if(o instanceof Move) {
                        Move move = (Move) o;
                        System.out.println("Found the move");

                        Game g = server.getCurrentGame(move.getPlayer());
                        if(g != null) {
                            //add the move to the current game
                            server.addMove(move, move.getPlayer());
                            //sending the move to the spectators
                            for(int i=0; i<g.getSpectators().size(); i++) {
                                System.out.println("sending to spectator " + g.getSpectators().get(i));
                                server.sendToClient(move, g.getSpectators().get(i));
                            }

                            if(!((move.getType().equals("end")) || (move.getType().equals("draw")))) {
                                if(g.getUser1().getUserName().equals(move.getPlayer())) {
                                    server.sendToClient(move, g.getUser2().getUserName());
                                    System.out.println("Sent the move to " +
                                            g.getUser2().getUserName());
                                }
                                else {
                                    server.sendToClient(move, g.getUser1().getUserName());
                                    System.out.println("Sent the move to " +
                                            g.getUser1().getUserName());
                                }
                            }
                            else {
                                System.out.println("game has ended");
                                server.storeGame(g);
                                if(move.getType().equals("end"))
                                    System.out.println(move.getPlayer() + " won");
                                else System.out.println("Game was drawn");
                                if(g.getUser1().getUserName().equals(move.getPlayer())) {
                                    server.updateRatings(move.getPlayer(),
                                            g.getUser2().getUserName(), move.getType());
                                }
                                else {
                                    server.updateRatings(move.getPlayer(),
                                            g.getUser1().getUserName(), move.getType());
                                }
                                server.removeGame(move.getPlayer());
                            }
                        }
                    }

                    if(o instanceof Requester) {
                        Requester requester = (Requester) o;
                        if(requester.getType().equals("openGames")) {
                            nc.write(server.getOpenGamesList());
                        }
                        else if(requester.getType().equals("leaveGame")) {
                            Game g = server.getCurrentGame(requester.getHost());
                            if(g != null && g.getUser2() != null)
                                server.storeGame(g);
                            server.removeGame(requester.getHost());
                        }
                        else if(requester.getType().equals("leaveGame2")) {
                            Game g = server.getCurrentGame(requester.getHost());
                            if(g != null) {
                                g.removeSpectator(requester.getViewer());
                            }
                        }
                        else if(requester.getType().equals("joinGame")) {
                            System.out.println("join request found");
                            boolean isSuccessful =
                            server.joinGame(requester.getHost(), requester.getChallenger());
                            nc.write(isSuccessful);
                        }
                        else if(requester.getType().equals("viewGame")) {
                            System.out.println("view request found for " + requester.getHost() );
                            try {
                                Game g = server.getCurrentGame(requester.getHost());
                                g.addSpectator(requester.getViewer());
                                server.sendToClient(g, requester.getViewer());
                                Move[] moves = new Move[g.getMoves().size()];
                                for(int i=0; i<g.getMoves().size();i++) {
                                    moves[i] = g.getMoves().get(i);
                                }
                                server.sendToClient(moves, requester.getViewer());
                                System.out.println("game size = " + g.getMoves().size());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else if(requester.getType().equals("immediateResponse")){
                            System.out.println("immediate response request found");
                            nc.write(false);
                        }
                        else if(requester.getType().equals("opponentName")) {
                            System.out.println("found opponent name requester");
                            String player = requester.getHost();
                            Game g = server.getCurrentGame(player);
                            if(g.getUser1().getUserName().equals(player)) {
                                nc.write(g.getUser2().getUserName());
                                System.out.println("Sent the opponent's name");
                            }
                            else {
                                nc.write(g.getUser1().getUserName());
                                System.out.println("Sent the opponent's name");
                            }
                        }
                        else if(requester.getType().equals("image")) {
                            if(!isImageSent) {
                                isImageSent = true;
                                //sending the background image now
                                System.out.println("sending image");
                                FileInputStream fis = new FileInputStream(imagePath);
                                byte[] image = new byte[fis.available()];
                                fis.read(image);
                                ImageSender is = new ImageSender(image);
                                nc.write(is);
                            }
                        }
                        else if(requester.getType().equals("file")) {
                            //sending the old games through file
                            server.sendFile(requester.getHost());
                            //sending the live games
                            nc.write(server.getRunningGamesList());
                        }
                    }

                    if(o instanceof String) {
                        System.out.println((String) o);
                    }

                    if(o instanceof TerminationData) {
                        System.out.println("Closing the thread");
                        TerminationData td = (TerminationData) o;
                        flag = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("In finally of server readthread");
            nc.closeConnection();
        }
    }
}




