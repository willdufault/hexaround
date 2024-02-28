package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.coordinate.HexCoordinate;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;

import static hexaround.game.board.coordinate.HexCoordinate.makeCoordinate;

public class AbilityWalking extends AbstractAbility implements IAbility {
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
    public boolean isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                               int fromX, int fromY, int toX, int toY, int distance) {
        if(board.isFull(toX, toY)) {
            return false;
        }

        if(board.isOccupied(toX, toY) && !intruding) {
            return false;
        }

        if(makeCoordinate(fromX, fromY).distanceTo(makeCoordinate(toX, toY)) > distance) {
            return false;
        }

        return this.pathExists(board, creature, team, intruding, fromX, fromY, toX, toY, distance, new HashMap<>());
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
     * @param record A HashMap storing the highest number of moves remaining for each visited tile.
     * @return True if a path exists.
     */
    public boolean pathExists(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                       int x, int y, int toX, int toY, int remaining, HashMap<HexCoordinate, Integer> record) {
        if(remaining < 0) {
            return false;
        }

        if(!board.isColonyConnected()) {
            return false;
        }

        if(x == toX && y == toY) {
            return true;
        }

        HexCoordinate hex = makeCoordinate(x, y);
        if(record.getOrDefault(hex, 0) >= remaining) {
            return false;
        }
        record.put(hex, remaining);

        for(HexCoordinate neighbor : hex.neighbors()) {
            int nextX = neighbor.x();
            int nextY = neighbor.y();

            if(!intruding && board.isOccupied(nextX, nextY)) {
                continue;
            }

            if(!intruding && !canDrag(board, x, y, nextX, nextY)) {
                continue;
            }

            // todo: need to place creature back in the same position they were in originally
            // todo: MATTERS IF CREATURE ON TOP IS INTRUDING, NEED TO PUT THEM BACK ON TOP
            // todo: if top creature is intruding, can't move anyway. need to check for this.
            board.removeCreature(creature, team, x, y);
            board.placeCreatureAt(creature, team, nextX, nextY);

            boolean result = this.pathExists(board, creature, team, intruding, nextX, nextY, toX, toY, remaining - 1, record);

            board.removeCreature(creature, team, nextX, nextY);
            board.placeCreatureAt(creature, team, x, y);

            if(result) {
                return true;
            }
        }

        return false;
    }
}
