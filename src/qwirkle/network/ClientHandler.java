package qwirkle.network;

import qwirkle.controllers.ServerController;
import qwirkle.game.Game;
import qwirkle.game.Player;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * Client handler that handles connections with clients
 */
public class ClientHandler extends Thread {

    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;

    private boolean running;
    private Player player;
    private String username;

    /**
     * @param s Socket where client is on
     */
    public ClientHandler(Socket s) {
        this.socket = s;

        try {
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), Protocol.Server.Settings.ENCODING));
            out = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), Protocol.Server.Settings.ENCODING));
        } catch (IOException e) {
            //TODO error logs
            System.out.println(e.getMessage());
            shutdown();
        }

        System.out.println("New client connected");
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                String s = in.readLine();
                if(s != null) {
                    parseMessage(s);
                } else {
                    shutdown();
                    running = false;
                }
            } catch (IOException e) {
                //TODO error logs
                System.out.println(e.getMessage());
                shutdown();
            }
        }
    }

    /**
     * Parse message so it can be send
     *
     * @param msg The message to be send
     */
    private void parseMessage(String msg) {
        System.out.println(msg); //TODO remove debug
        String[] msg_split = msg.split(String.valueOf(Protocol.Server.Settings.DELIMITER));
        String command = msg_split[0];
        final String[] params = Arrays.copyOfRange(msg_split, 1, msg_split.length);

        Runnable r;

        switch (command) {
            case Protocol.Client.HALLO:
                sendHello();
                break;
            case Protocol.Client.REQUESTGAME:
                sendWaitFor(ServerController.getInstance().joinLobby(params[0], this));
                break;
        }
    }

    /**
     * Send the message
     *
     * @param message The message to be send
     */
    private void sendMessage(String message) {
        try {
            out.write(message + System.lineSeparator());
            out.flush();
        } catch (IOException e) {
            //TODO errors
            System.out.println(e.getMessage());
            shutdown();
        }
    }

    /**
     * Send hellom, the first message
     */
    public void sendHello() {
        String cmd = Protocol.Server.HALLO + Protocol.Server.Settings.DELIMITER +
                ServerController.getInstance().getServerName();
        sendMessage(cmd);
    }


    /**
     * Send to the client that you have to wait for an amount of players
     *
     * @param players Players the client has to wait for
     */
    private void sendWaitFor(int players) {
        String cmd = Protocol.Server.OKWAITFOR + Protocol.Server.Settings.DELIMITER + players;
        sendMessage(cmd);
    }

    /**
     * Notify the player that the game starts
     *
     * @param players All the players
     */
    public void sendStartGame(String[] players) {
        String cmd = Protocol.Server.STARTGAME + Protocol.Server.Settings.DELIMITER + players;
        sendMessage(cmd);
    }


    /**
     * Notify the player that the game has ended including why and the winner
     *
     * @param reason Why the game has ended
     * @param winner Winner of the game
     */
    public void sendEnd(Game.End reason, String winner) {
        String cmd = Protocol.Server.GAME_END + Protocol.Server.Settings.DELIMITER + reason +
                Protocol.Server.Settings.DELIMITER + winner;
        sendMessage(cmd);
    }

    /**
     * Set the player for the handler
     *
     * @param p player
     */
    public void setPlayer(Player p) {
        this.player = p;
    }

    /**
     * Shutdown cleanly
     */
    private void shutdown() {
        try {
            in.close();
            out.close();
            socket.close();
            ServerController.getInstance().removeHandler(this);
        } catch (IOException e) {
            //TODO errors
            System.out.println(e.getMessage());
        }
    }
}
