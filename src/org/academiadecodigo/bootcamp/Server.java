package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by codecadet on 04/07/2017.
 */
public class Server {

    private static final int PORT = 5000;
    private ArrayList<ClientHandler> usersList;


    public static void main(String[] args) {
        Server server = new Server();
        server.executeServer();

    }

    public Server() {

        this.usersList = new ArrayList<>();
    }

    public void executeServer() {

        try {

            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                usersList.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendAll(String spokenShit) {

        for (ClientHandler user : usersList) {
            user.send(spokenShit);
        }

    }


    public class ClientHandler implements Runnable {

        Socket clientSocket;
        PrintWriter out;
        BufferedReader in;
        String nickname = "";
        int level = 1;
        int exp = 0;
        int requiredExp = level * 150;
        int strength = 1;
        int intellect = 1;
        int agility = 1;
        int stamina = 1;
        int freeStatPoints = 0;
        double hp = 10 * (stamina * 0.5);

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try {

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("What is your desired nickname, stranger?");
                nickname = in.readLine();

                while(nickname.equals("")){
                    out.println("You need a name...");
                    nickname = in.readLine();
                }

                out.println("Welcome to RPG Chat, 0.1, " + nickname + "! Type rules to learn more.");

                while (true) {

                    String spokenBefore = "";
                    String spokenShit = in.readLine();

                    if (spokenShit == null) {
                        return;
                    }

                    if (spokenBefore.equals(spokenShit)){
                        out.println("Don't spam fool!");
                        return;
                    }

                    if (spokenShit.equals("mystatus")) {
                        out.println(myStatus());
                    }

                    if (spokenShit.equals("rules")) {
                        out.println("Welcome to RPG Chat 0.1.\r\n" +
                                "To check your character's status type 'mystatus'\r\n" +
                        "You level up by typing on the chat.\r\n" + "Perma death on disconnect or if your hp is 0 or below.\r\n" +
                        "When you level up you are awarded 3 stat points that you can allocate to the stat you deem best. Use 'levelstrength', 'levelstamina', 'levelintellect' or 'levelagility' to pick what to level until you are out of points.\r\n" +
                        "Write 'freepoints' to see how many stat points you have free to spend\r\n" + "Good luck, traveler.\r\n" + "\r\n" + "Will implement: Random drops, inventory, creatures, guilds and bosses.");
                    }

                    if (spokenShit.equals("levelstrength")) {
                        out.println(levelStrength());
                    }

                    if (spokenShit.equals("levelstamina")) {
                        out.println(levelStamina());
                    }

                    if (spokenShit.equals("levelintellect")) {
                        out.println(levelIntellect());
                    }

                    if (spokenShit.equals("levelagility")) {
                        out.println(levelAgility());
                    }

                    if (spokenShit.equals("freepoints")) {
                        out.println(myFreePoints());
                    }

                    spokenBefore = spokenShit;

                    String lastWords = nickname + " [Lvl." + level +  "]: " + spokenShit;

                    System.out.println(lastWords); //LOG

                    sendAll(lastWords);

                    levelUpdate(spokenShit);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String spokenShit) {

            out.println(spokenShit);
        }

        public void dead(){
            if (hp <= 0){

                try {
                    out.println("You died.");
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void levelUpdate(String spokenShit) {

            if (exp + spokenShit.length() > requiredExp) {
                level++;
                exp = (exp + spokenShit.length()) - requiredExp;
                out.println(nickname + " has leveled up!!! He is now level " + level + ".");
                freeStatPoints += 3;
                return;
            }

            exp += spokenShit.length();

        }

        public String myStatus() {
            return nickname + "--> Level: " + level + ". Exp: " + exp + "/" + requiredExp + ", Inventory: ";
        }

        public String myFreePoints() {
            return "Free Stat Points ---> " + freeStatPoints;
        }

        public String levelStrength() {
            if (freeStatPoints <= 0) {
                return "You have no free points.";
            }
            strength += 1;
            freeStatPoints -= 1;

            return "Successfully leveled! Current stats: Stamina - " + stamina + ", Strength - " + strength + ", Intellect - " + intellect + ", Agility - " + agility + ".";
        }

        public String levelIntellect() {
            if (freeStatPoints <= 0) {
                return "You have no free points.";
            }
            intellect += 1;
            freeStatPoints -= 1;

            return "Successfully leveled! Current stats: Stamina - " + stamina + ", Strength - " + strength + ", Intellect - " + intellect + ", Agility - " + agility + ".";
        }

        public String levelAgility() {
            if (freeStatPoints <= 0) {
                return "You have no free points.";
            }
            agility += 1;
            freeStatPoints -= 1;

            return "Successfully leveled! Current stats: Stamina - " + stamina + ", Strength - " + strength + ", Intellect - " + intellect + ", Agility - " + agility + ".";
        }

        public String levelStamina() {
            if (freeStatPoints <= 0) {
                return "You have no free points.";
            }
            stamina += 1;
            freeStatPoints -= 1;

            return "Successfully leveled! Current stats: Stamina - " + stamina + ", Strength - " + strength + ", Intellect - " + intellect + ", Agility - " + agility + ".";
        }
    }
}
