package hexaround.game.attribute;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;
import java.util.Map;

public interface IAttribute {
    /**
     * todo
     * @param board
     * @param playerInventories
     * @param creature
     * @param team
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param index
     */
    void takeEffect(HexAroundBoard board, Map<Boolean, Map<CreatureName, Integer>> playerInventories,
                    CreatureName creature, boolean team, int fromX, int fromY, int toX, int toY, int index);
}
