package hexaround.board;

import hexaround.rules.CreatureName;

import java.util.HashMap;
import java.util.Optional;

public class Board {
    private HashMap<CoordinatePair, Tile> tiles = null;

    public Board() {
        this.tiles = new HashMap<>();
    }

    /**
     *
     */
    public int calculateDistance(int x1, int y1, int x2, int y2) {
        return (Math.abs(x1 - x2) + Math.abs(x1 + y1 - x2 - y2) + Math.abs(y1 - y2)) / 2;
    }

    /**
     *
     * @param x
     * @param y
     */
    private void createTileIfNotExists(int x, int y) {
       CoordinatePair key = new CoordinatePair(x, y);

       if(this.tiles.containsKey(key)) return;

       this.tiles.put(key, new Tile());
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Optional<CreatureName> getCreatureAt(int x, int y) {
        CoordinatePair key = new CoordinatePair(x, y);

        if(!this.tiles.containsKey(key)) return Optional.empty();

        return this.tiles.get(key).getFirstCreature();
    }

    /**
     *
     * @param creature
     * @param x
     * @param y
     */
    public void placeCreature(CreatureName creature, int x, int y) {
        this.createTileIfNotExists(x, y);

        this.tiles.get(new CoordinatePair(x, y)).addCreature(creature);
    }
}
