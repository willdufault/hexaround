package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.move.MoveResponse;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;

public interface IAbility {
    /**
     * Determine if the move from (fromX, fromY) to (toX, toY) is legal.
     *
     * @param board The hex board.
     * @param creature The name of the creature.
     * @param team The team the creature is on.
     * @param intruding Whether the creature at (fromX, fromY) has the intruding attribute.
     * @param removes Whether the creature at (fromX, fromY) has an ability that removes the top creature from a tile.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @param distance The max distance this piece can move.
     * @return True if the move is legal.
     */
    MoveResponse isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                             boolean removes, int fromX, int fromY, int toX, int toY, int distance);
}
