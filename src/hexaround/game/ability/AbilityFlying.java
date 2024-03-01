package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.move.MoveResponse;
import hexaround.game.rule.CreatureName;

import static hexaround.game.move.MoveResult.*;
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
    public MoveResponse isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                               boolean removes, int fromX, int fromY, int toX, int toY, int distance) {
        if(fromX == toX && fromY == toY) {
            return new MoveResponse(MOVE_ERROR, "Not allowed to skip turns.");
        }

        if(board.isFull(toX, toY) && !removes) {
            return new MoveResponse(MOVE_ERROR, "That tile is full.");
        }

        if(board.isOccupied(toX, toY) && !intruding) {
            return new MoveResponse(MOVE_ERROR, "That tile is occupied and this piece is not intruding.");
        }

        if(makeCoordinate(fromX, fromY).distanceTo(makeCoordinate(toX, toY)) > distance) {
            return new MoveResponse(MOVE_ERROR, "That tile too far.");
        }

        if(board.isSurrounded(fromX, fromY)) {
            return new MoveResponse(MOVE_ERROR, "Flying pieces can't move when surrounded.");
        }

        // Check for connectedness.
        int index = board.removeCreature(creature, team, fromX, fromY);
        board.placeCreatureAt(creature, team, toX, toY);

        boolean connected = board.isColonyConnected();

        board.removeCreature(creature, team, toX, toY);
        board.placeCreatureAt(creature, team, fromX, fromY, index);

        return connected ? new MoveResponse(OK, "Legal move.")
                : new MoveResponse(MOVE_ERROR, "That move leaves the colony disconnected.");
    }
}
