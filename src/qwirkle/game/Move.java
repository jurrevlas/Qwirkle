package qwirkle.game;

import qwirkle.network.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * The addition of one or more tiles to the board.
 */
public class Move {

    private List<Tile> tiles;

    private Type type;

    public enum Type {
        MOVE, CHANGE, SKIP
    }

    /**
     * Constructor.
     */
    public Move() {
        this.tiles = new ArrayList<>();
        this.type = Type.MOVE;
    }

    /**
     * Add tile to the move.
     *
     * @param t   Tile to add
     * @param row Row location
     * @param col Col location
     */
    public void addTile(Tile t, int col, int row) {
        t.setRow(row);
        t.setCol(col);

        tiles.add(t);
    }

    public void setType(Type type) {
        this.type = type;
    }


    /**
     * Return the type of the move.
     *
     * @return
     */

    public Type getType() {
        return this.type;
    }

    /**
     * Returns all the tiles in the move.
     *
     * @return Tiles
     */
    public List<Tile> getTiles() {
        return this.tiles;
    }

    @Override
    public String toString() {
        String move = "";

        for (int i = 0; i < tiles.size(); i++) {
            move += tiles.get(i).toChars() + Protocol.Server.Settings.DELIMITER2;
            move += tiles.get(i).getCol() + Protocol.Server.Settings.DELIMITER2;
            move += tiles.get(i).getRow() + Protocol.Server.Settings.DELIMITER2;
        }

        return move;
    }
}
