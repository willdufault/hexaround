package hexaround.board;

import hexaround.config.CreatureDefinition;

import java.util.LinkedList;

/**
 *
 */
public class Tile {

    private LinkedList<CreatureDefinition> creatures = new LinkedList<>();

    public Tile() {}

    public void addCreature(CreatureDefinition creature) {
        this.creatures.add(creature);
    }

    public CreatureDefinition removeCreature() {
        // Not sure how to handle this yet.
        // If there is a stack of creatures, how to know which one to move?
        // Player gives coordinates, and you give options for which piece to move?
        // What if two red ducks on this tile and neither are trapped? How to decide which to move?
        return null;
    }
}
