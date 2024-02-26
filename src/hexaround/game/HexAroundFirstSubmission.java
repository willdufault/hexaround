package hexaround.game;

import hexaround.config.*;
import hexaround.game.board.*;
import hexaround.game.board.coordinate.*;
import hexaround.game.move.MoveResponse;
import hexaround.game.rules.CreatureName;
import hexaround.game.rules.CreatureProperty;

import java.util.*;

import static hexaround.game.move.MoveResult.*;

public class HexAroundFirstSubmission implements IHexAround1{

    private HexAroundBoard board = null;
    private Map<CreatureName, CreatureDefinition> creatures;

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        // Nothing to do
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x
     * @param y
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return board.getCreatureAt(x, y);
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
        boolean result = false;
        CreatureName creature = board.getCreatureAt(x, y);
        if (creature != null) {
            CreatureDefinition cd = creatures.get(creature);
            if (cd != null) {
                result = cd.properties().contains(property);
            }
        }
        return result;
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
        return board.isCreatureAt(x,y );
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
        boolean result = false;
        CreatureName creature = getCreatureAt(x1, y1);
        if (creature != null) {
            CreatureDefinition cd = creatures.get(creature);
            int maxDistance = cd.maxDistance();
            HexCoordinate c1 = new HexCoordinate(x1, y1);
            int actualDistance = c1.distanceTo(new HexCoordinate(x2, y2));
            result = maxDistance >= actualDistance;
        }
        return result;
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
        board.placeCreatureAt(creature, x, y);

        // New for submission 2.
        // Copy-pasted "Legal move".
        return new MoveResponse(OK, "Legal move");
    }

    /**
     * New for submission 2.
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        if(!isLegalMove(creature, fromX, fromY, toX, toY))
            // Copy-pasted "Colony is not connected, try again".
            return new MoveResponse(MOVE_ERROR, "Colony is not connected, try again");

        this.board.removeCreature(fromX, fromY);
        this.board.placeCreatureAt(creature, toX, toY);
        // Copy-pasted "Legal move".
        return new MoveResponse(OK, "Legal move");
    }

    // New for submission 2.

    /**
     * New for submission 2.
     *
     * Return true if the given move satisfies the following conditions:
     *   1) The move is within the piece's max distance.
     *   2) The colony will remain connected after the piece is moved.
     */
    private boolean isLegalMove(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        if(!board.isCreatureAt(fromX, fromY)) return false;

        if(!canReach(fromX, fromY, toX, toY)) return false;

        // Move the piece and check that the colony is still connected.
        this.board.removeCreature(fromX, fromY);
        this.board.placeCreatureAt(creature, toX, toY);
        boolean connected = this.board.isColonyConnected();

        // Move the piece back.
        this.board.removeCreature(toX, toY);
        this.board.placeCreatureAt(creature, fromX, fromY);

        return connected;
    }

    /************************************ Helpers *********************************/
    public void setBoard(HexAroundBoard board) {
        this.board = board;
    }

    public void setCreatures(Collection<CreatureDefinition> creatureDefs) {
        creatures = new HashMap<>();
        for (CreatureDefinition cd : creatureDefs) {
            creatures.put(cd.name(), cd);
        }
    }
}
