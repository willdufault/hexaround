package hexaround.game;

import hexaround.board.Board;
import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.rules.CreatureName;
import hexaround.rules.CreatureProperty;
import hexaround.move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class HexAroundFirstSubmission implements IHexAround1 {

    private ArrayList<PlayerConfiguration> players = null;
    private ArrayList<CreatureDefinition> creatures = null;
    private Board board = null;

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this constructor. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        this.players = new ArrayList<>();
        this.creatures = new ArrayList<>();
        this.board = new Board();
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return this.board.getCreatureAt(x, y).orElse(null);
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        Optional<CreatureName> creature = this.board.getCreatureAt(x, y);

        if(creature.isEmpty()) return false;

        return this.creatureHasProperty(creature.get(), property);
    }

    /**
     * Determine if the creature has the property.
     * @param creature The creature.
     * @param property The property to check for.
     * @return True if the creature has the property.
     */
    private boolean creatureHasProperty(CreatureName creature, CreatureProperty property) {
        // A hashmap would be better here, but the list is so small, it doesn't really matter.
        for(CreatureDefinition definition : this.creatures) {
            if(definition.name() != creature) continue;

            return definition.properties().contains(property);
        }

        // Creature is not in the game.
        return false;
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        // use helpers from before
        return this.getCreatureAt(x, y) != null;
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1 The x-coordinate of the piece.
     * @param y1 The y-coordinate of the piece.
     * @param x2 The x-coordinate of the destination.
     * @param y2 The y-coordinate of the destination.
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        if(!isOccupied(x1, y1)) return false;

        return this.getMaxDistance(this.getCreatureAt(x1, y1)) <= this.board.calculateDistance(x1, y1, x2, y2);
    }

    /**
     * Get the max distance of the given creature.
     * @param creature The creature.
     * @return The max allowed distance for the creature.
     */
    private int getMaxDistance(CreatureName creature) {
        // A hashmap would be better here, but the list is so small, it doesn't really matter.
        for(CreatureDefinition definition : this.creatures) {
            if(definition.name() != creature) continue;

            return definition.maxDistance();
        }

        // Creature is not in the game.
        return -1;
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature The creature to put on the board.
     * @param x The x-coordinate to put the creature on.
     * @param y The y-coordinate to put the creature on.
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        // TODO: Add legality checks here.
        this.board.placeCreature(creature, x, y);

        // TODO: Eventually be a move response.
        return null;
    }

    /**
     * This is never used in this submission. You do not have to do anything.
     * @param creature
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        // nice, skip for now
        return null;
    }

    /**
     * Get the players.
     * @return The list of players.
     */
    public ArrayList<PlayerConfiguration> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * Get the creatures.
     * @return The list of creatures.
     */
    public ArrayList<CreatureDefinition> getCreatures() {
        return new ArrayList<>(this.creatures);
    }

    /**
     * Set the players list to the given list of players.
     *
     * @param players List of players.
     */
    public void setPlayers(Collection<PlayerConfiguration> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * Set the creatures list to the given list of creatures.
     *
     * @param creatures List of creatures.
     */
    public void setCreatures(Collection<CreatureDefinition> creatures) {
        this.creatures = new ArrayList<>(creatures);
    }
}
