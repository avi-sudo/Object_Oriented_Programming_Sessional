package Server;

import DataTypes.Game;
import DataTypes.Move;
import DataTypes.UserData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

class FileRW {
    void fileRead(String fileName, List<UserData> clientList){
        try {
            System.out.println("reading");
            String line;
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while (true) {
                line = br.readLine();
                if (line == null) break;
                String[] temp;
                temp = line.split(",");
                clientList.add(new UserData(temp[0], temp[1], Integer.parseInt(temp[2]),
                        0));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int fileReadGameCount(String fileName){
        try {
            System.out.println("reading gameCount");
            String line;
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            line = br.readLine();
            br.close();
            return Integer.parseInt(line);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    void fileWrite(String fileName, List<UserData> clientList){
        //when ratings are updated, we use this function to update the whole userDatas file
        try {
            System.out.println("writing");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            String message;
            for(int i=0; i<clientList.size(); i++){
                UserData ud = clientList.get(i);
                message = ud.getUserName() + "," + ud.getPassword() + "," +
                        ud.getRating();
                bw.write(message);
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fileWrite(String fileName, String message) {
        try {
            System.out.println("writing");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(message);
            bw.write("\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fileAppend(String fileName, String message) {
        try {
            System.out.println("appending");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write(message);
            bw.write("\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fileAppend(String fileName, UserData ud) {
        try {
            System.out.println("appending");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            String message = ud.getUserName() + "," + ud.getPassword() + "," +
                    ud.getRating();
            bw.write(message);
            bw.write("\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fileAppend(String fileName, Game game) {
        try {
            System.out.println("appending game");
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write(game.getID() + ",");
            bw.write(game.getUser1().getUserName() + ",");
            bw.write(game.getUser2().getUserName() + ",");
            List<Move> moves = game.getMoves();
            for(int i=0; i<moves.size(); i++) {
                Move m = moves.get(i);
                if(m.getType().equals("end")) {
                    bw.write(m.getType() + ",");
                    if(m.getPlayer().equals(game.getUser1().getUserName())) {
                        bw.write("1"); //player 1 won
                    }
                    else bw.write("2"); //player 2 won
                    bw.write("\n");
                }
                else if(m.getType().equals("draw")) {
                    bw.write("draw" + "," + "-1");
                    bw.write("\n");
                }
                else if(m.getType().equals("cancel")) {
                    //we find out who cancelled the game
                    bw.write(m.getType() + ",");
                }
                else {
                    bw.write(m.getColumn() + ",");
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
