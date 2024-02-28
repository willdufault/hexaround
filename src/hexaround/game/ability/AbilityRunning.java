package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.coordinate.HexCoordinate;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;

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
    @Override
    public boolean isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding, int fromX, int fromY, int toX, int toY, int distance) {
        return false;
    }

    /**
     * Recursively determine if there is a path from (x, y) to (toX, toY).
     *
     * @param board     The hex board.
     * @param creature  A creature name.
     * @param team      The creature's team.
     * @param intruding Whether the creature has the intruding attribute.
     * @param x         The current x coordinate.
     * @param y         The current y coordinate.
     * @param toX       The destination x coordinate.
     * @param toY       The destination y coordinate.
     * @param remaining The number of remaining moves.
     * @param record    A HashMap storing the highest number of moves remaining for each visited tile.
     * @return True if a path exists.
     */
    @Override
    public boolean pathExists(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding, int x, int y, int toX, int toY, int remaining, HashMap<HexCoordinate, Integer> record) {
        return false;
    }
}
