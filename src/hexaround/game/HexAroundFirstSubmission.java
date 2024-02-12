package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.required.*;

import java.util.Collection;
import java.util.LinkedList;

public class HexAroundFirstSubmission implements IHexAround1 {

    private LinkedList<PlayerConfiguration> players = new LinkedList<PlayerConfiguration>();
    private LinkedList<CreatureDefinition> creatures = new LinkedList<CreatureDefinition>();

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this constructor. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        // Nothing to do.
    }

    /**
     * Get the players.
     * @return The list of players.
     */
    public LinkedList<PlayerConfiguration> getPlayers() {
        return new LinkedList<>(this.players);
    }

    /**
     * Get the creatures.
     * @return The list of creatures.
     */
    public LinkedList<CreatureDefinition> getCreatures() {
        return new LinkedList<>(this.creatures);
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
        // MAKE HELPER TO CONVERT X,Y TO R,C AND VICE VERSA
        // board > use offset and half to convert to axial coords
        // then check if out of bounds or if in bounds, if that tile has any creatures
        return null;
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        // check first creature on tile (should have made a helper for the method above)
        // and get its property
        return false;
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x
     * @param y
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        // use helpers from before
        return false;
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        // find formula to calculate axial dist, only need to check if this is within the range for this piece
        // do not need to worry about if it is legal
        return false;
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature
     * @param x
     * @param y
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        // just add the creature name to the list
        // MIGHT NEED TO RE-DO CREATURES LIST TO USE CREATURENAME INSTEAD
        // ask on discord how to handle this since there can be multiple pieces
        // on the same tile (and from the same team), this could get confusing
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
     * Set the players list to the given list of players.
     *
     * @param players List of players.
     */
    public void setPlayers(Collection<PlayerConfiguration> players) {
        this.players = new LinkedList<PlayerConfiguration>(players);
    }

    /**
     * Set the creatures list to the given list of creatures.
     *
     * @param creatures List of creatures.
     */
    public void setCreatures(Collection<CreatureDefinition> creatures) {
        this.creatures = new LinkedList<CreatureDefinition>(creatures);
    }
}
