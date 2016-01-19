package ss.qwirkle.common.ui;

import ss.qwirkle.common.game.Board;
import ss.qwirkle.common.game.Tile;
import ss.qwirkle.server.Server;
import ss.qwirkle.server.controllers.ServerController;

import java.lang.reflect.Array;
import java.util.Scanner;

public class TUI implements UserInterface {
    private String message;
    private String gameInfo;
    private boolean showSetup;
    private String promptMessage;
    final String ANSI_CLS = "\u001b[2J";
    final String ANSI_HOME = "\u001b[H";
    private Board board;
    Scanner scanner;
    //private hand
    public static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public TUI(){
        this.scanner = new Scanner(System.in);
        gameInfo = "Qwirkle";
        message = "test";
        board = new Board(10);
        Tile tile = new Tile('E','E');
        board.addTile(0,0,tile);
    }

    @Override
    public void run(String[] args) {
        if (ServerController.getInstance().showSetup()){
            showSetup = true;
            showSetup(ServerController.getInstance());
        }
        message = ServerController.getInstance().getPort()+"";
        promptMessage = "";
        printScreen();
    }

    private void showSetup(ServerController controller) {
        message("Running Setup");
        controller.setName(promptString("Name:"));
        controller.setPort(Integer.parseInt(promptString("Port:")));

    }

    @Override
    public void message(String message) {
        this.message = message;
        printScreen();
    }


    @Override
    public boolean prompt(String message, String yes, String no) {
        promptString(promptMessage);
        String answer = scanner.next();
        if (answer == "y"){
            return true;
        }
        if (answer == "n") {
            return false;
        }
        return false;
    }
    public String promptString(String promptMessage){
        setPromptString(promptMessage);
        printScreen();
        return scanner.next();

    }

    public void printScreen(){
        clearConsole();
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
        System.out.println(gameInfo);
        System.out.println(message);
        if (!showSetup) {
            System.out.println(board.toIconString());
        }
        System.out.println(promptMessage);

    }

    public void setPromptString(String promptMessage) {
        this.promptMessage = promptMessage;
    }
}
