package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.coordinate.HexCoordinate;
import hexaround.game.move.MoveResponse;
import hexaround.game.rule.CreatureName;

import java.util.HashSet;

import static hexaround.game.move.MoveResult.*;
import static hexaround.game.board.coordinate.HexCoordinate.makeCoordinate;

public class AbilityRunning extends AbstractAbility implements IAbility {
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
    public MoveResponse isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                                    boolean removes, int fromX, int fromY, int toX, int toY, int distance) {
        if(makeCoordinate(fromX, fromY).distanceTo(makeCoordinate(toX, toY)) != distance) {
            return new MoveResponse(MOVE_ERROR, "Distance between tiles must match the max distance.");
        }

        if(!board.hasOccupiedNeighbor(toX, toY)) {
            return new MoveResponse(MOVE_ERROR, "That move leaves the colony disconnected.");
        }

        if(board.isFull(toX, toY) && !removes) {
            return new MoveResponse(MOVE_ERROR, "That tile is full.");
        }

        if(board.isOccupied(toX, toY) && !intruding) {
            return new MoveResponse(MOVE_ERROR, "That tile is occupied and this piece is not intruding.");
        }

        return this.pathExists(board, creature, team, intruding, fromX, fromY, toX, toY, distance, new HashSet<>())
                ? new MoveResponse(OK, "Legal move.")
                : new MoveResponse(MOVE_ERROR, "No legal path exists.");
    }

    /**
     * Recursively determine if there is a path from (x, y) to (toX, toY).
     * @param board The hex board.
     * @param creature A creature name.
     * @param team The creature's team.
     * @param intruding Whether the creature has the intruding attribute.
     * @param x The current x coordinate.
     * @param y The current y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @param remaining The number of remaining moves.
     * @param visited A HashSet storing the visited tiles for each path.
     * @return True if a path exists.
     */
    private boolean pathExists(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                              int x, int y, int toX, int toY, int remaining, HashSet<HexCoordinate> visited) {
        if(!board.isColonyConnected()) {
            return false;
        }

        if(remaining == 0) {
            return x == toX && y == toY;
        }

        HexCoordinate hex = makeCoordinate(x, y);
        if(visited.contains(hex)) {
            return false;
        }

        for(HexCoordinate neighbor : hex.neighbors()) {
            int nextX = neighbor.x();
            int nextY = neighbor.y();

            if(!intruding && board.isOccupied(nextX, nextY)) {
                continue;
            }

            if(!intruding && !canDrag(board, x, y, nextX, nextY)) {
                continue;
            }

            int index = board.removeCreature(creature, team, x, y);
            board.placeCreatureAt(creature, team, nextX, nextY);
            visited.add(hex);

            boolean result = this.pathExists(board, creature, team, intruding, nextX, nextY, toX, toY,
                    remaining - 1, visited);

            board.removeCreature(creature, team, nextX, nextY);
            board.placeCreatureAt(creature, team, x, y, index);
            visited.remove(hex);

            if(result) {
                return true;
            }
        }

        return false;
    }
}
