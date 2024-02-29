package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.rule.CreatureName;

import static hexaround.game.board.coordinate.HexCoordinate.makeCoordinate;

public class AbilityFlying extends AbstractAbility implements IAbility {
    /**
     * Determine if the move from (fromX, fromY) to (toX, toY) is legal.
     *
     * @param board     The hex board.
     * @param creature  The name of the creature.
     * @param team      The team the creature is on.
     * @param intruding Whether the creature at (fromX, fromY) has the intruding attribute.
     * @param fromX     The source x coordinate.
     * @param fromY     The source y coordinate.
     * @param toX       The destination x coordinate.
     * @param toY       The destination y coordinate.
     * @param distance  The max distance this piece can move.
     * @return True if the move is legal.
     */
    @Override
    public boolean isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                               int fromX, int fromY, int toX, int toY, int distance) {
        if(fromX == toX && fromY == toY) {
            System.out.println("Not allowed to skip turns.");
            return false;
        }

        if(board.isFull(toX, toY)) {
            System.out.println("That tile is full.");
            return false;
        }

        if(board.isOccupied(toX, toY) && !intruding) {
            System.out.println("That tile is occupied and this piece is not intruding.");
            return false;
        }

        if(makeCoordinate(fromX, fromY).distanceTo(makeCoordinate(toX, toY)) > distance) {
            System.out.println("That tile too far.");
            return false;
        }

        if(board.isSurrounded(fromX, fromY)) {
            System.out.println("Flying pieces can't move when surrounded.");
            return false;
        }

        // Check for connectedness.
        int index = board.removeCreature(creature, team, fromX, fromY);
        board.placeCreatureAt(creature, team, toX, toY);

        boolean connected = board.isColonyConnected();

        board.removeCreature(creature, team, toX, toY);
        board.placeCreatureAt(creature, team, fromX, fromY, index);

        if(!connected) {
            System.out.println("That move leaves the colony disconnected.");
        }

        return connected;
    }
}
