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
        if(!board.isColonyConnected()) {
            return false;
        }

        if(x == toX && y == toY) {
            return true;
        }

        if(remaining == 0) {
            return false;
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

            int index = board.removeCreature(creature, team, x, y);
            board.placeCreatureAt(creature, team, nextX, nextY);

            boolean result = this.pathExists(board, creature, team, intruding, nextX, nextY, toX, toY,
                    remaining - 1, record);

            board.removeCreature(creature, team, nextX, nextY);
            board.placeCreatureAt(creature, team, x, y, index);

            if(result) {
                return true;
            }
        }

        return false;
    }
}
