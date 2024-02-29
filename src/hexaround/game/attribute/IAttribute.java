package hexaround.game.attribute;

import hexaround.game.board.coordinate.HexCoordinate;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;

public interface IAttribute {
    /**
     * todo (in interface)
     * @param board
     * @param blueCreatures
     * @param redCreatures
     * @param creature
     * @param team
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    void takeEffect(HashMap<HexCoordinate, CreaturePiece> board, HashMap<CreatureName, Integer> blueCreatures,
                    HashMap<CreatureName, Integer> redCreatures, CreatureName creature, boolean team,
                    int fromX, int fromY, int toX, int toY, int index);
}
