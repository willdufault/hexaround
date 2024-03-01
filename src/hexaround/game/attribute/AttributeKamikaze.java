package hexaround.game.attribute;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.rule.CreatureName;

import java.util.LinkedList;
import java.util.Map;

public class AttributeKamikaze implements IAttribute {
    /**
     * Force the piece under the recently moved piece back into its team's inventory.
     * @param board The hex board.
     * @param playerInventories Both team's creature inventories.
     * @param creature A creature name.
     * @param team A player team.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @param index The position of the piece at its old tile.
     */
    @Override
    public void takeEffect(HexAroundBoard board, Map<Boolean, Map<CreatureName, Integer>> playerInventories,
                           CreatureName creature, boolean team, int fromX, int fromY, int toX, int toY, int index) {
        LinkedList<CreaturePiece> toCreatures = board.getCreaturesAt(toX, toY);
        if(toCreatures.size() == 1) {
            return;
        }

        CreaturePiece under = toCreatures.get(toCreatures.size() - 2);
        CreatureName underCreatureName = under.creature();
        board.removeCreature(underCreatureName, under.team(), toX, toY);
        playerInventories.get(team).put(underCreatureName, playerInventories.get(team).get(underCreatureName) + 1);

        if(under.creature() == CreatureName.BUTTERFLY) {
            board.clearButterflyTile(under.team());
        }
    }
}
