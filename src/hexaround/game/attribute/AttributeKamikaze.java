package hexaround.game.attribute;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.rule.CreatureName;

import java.util.LinkedList;
import java.util.Map;

public class AttributeKamikaze implements IAttribute {
    /**
     * todo (in interface)
     */
    @Override
    public void takeEffect(HexAroundBoard board, Map<Boolean, Map<CreatureName, Integer>> playerInventories,
                           CreatureName creature, boolean team, int fromX, int fromY, int toX, int toY, int index) {
        LinkedList<CreaturePiece> toCreatures = board.getCreaturesAt(toX, toY);

        if(toCreatures.size() == 1) {
            return;
        }

        CreaturePiece under = toCreatures.get(toCreatures.size() - 2);
        CreatureName underCreature = under.creature();
        board.removeCreature(underCreature, under.team(), toX, toY);
        playerInventories.get(team).put(underCreature, playerInventories.get(team).get(underCreature) + 1);

        if(under.creature() == CreatureName.BUTTERFLY) {
            board.clearButterflyTile(under.team());
        }
    }
}
