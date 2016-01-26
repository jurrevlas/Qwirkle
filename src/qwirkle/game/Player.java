package qwirkle.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    private String username;

    private List<Tile> hand;

    private Game game;

    private int score;

    public Player() {
        hand = new ArrayList<>();
    }

    public void addToHand(Tile t) {
        this.hand.add(t);
    }

    public void removeFromHand(Tile t) {
        this.hand.remove(t);
    }

    public void removeFromHand(int index) {
        this.hand.remove(index);
    }

    public List<Tile> getHand() {
        return hand;
    }

    public void setGame(Game g) {
        this.game = g;
    }

    public Game getGame() {
        return this.game;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return this.username;
    }

    public abstract Move determineMove();

    public boolean moveAllowed(Move move) {
        List<Tile> list = move.getTiles();

        for(int i = 0; i < list.size(); i++) {
            if(!hand.contains(list.get(i))) {
                return false;
            }
        }

        return true;
    }
}
