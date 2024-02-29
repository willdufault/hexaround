package hexaround.game.attribute;

import hexaround.game.board.coordinate.HexCoordinate;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.rule.CreatureName;

import java.util.HashMap;

public class AttributeKamikaze implements IAttribute {
    /**
     * todo (in interface)
     */
    @Override
    public void takeEffect(HashMap<HexCoordinate, CreaturePiece> board, HashMap<CreatureName, Integer> blueCreatures,
                           HashMap<CreatureName, Integer> redCreatures, CreatureName creature, boolean team,
                           int fromX, int fromY, int toX, int toY, int index) {

    }
}
