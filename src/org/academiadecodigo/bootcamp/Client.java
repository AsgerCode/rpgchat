package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 04/07/2017.
 */
public class Client {

    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Client client = new Client();
        client.executeClient();
    }

    public Client() {
        try {
            this.clientSocket = new Socket(HOST, PORT);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void executeClient() {

        Thread thread = new Thread(new ServerToTerminal());
        thread.start();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine();
            out.println(userInput);

        }
    }

    public class ServerToTerminal implements Runnable {

        @Override
        public void run() {

            try {

                while (true) {

                    String fromServer = in.readLine();

                    if (fromServer == null) {
                        return;
                    }

                    System.out.println(fromServer);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
