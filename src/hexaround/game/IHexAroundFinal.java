package hexaround.game;

import hexaround.game.rule.CreatureName;
import hexaround.game.rule.CreatureProperty;

public interface IHexAroundFinal extends IHexAroundGameManager {
    boolean getTeam();
    int getMoveCount();
    CreatureName getCreatureAt(int x, int y);
    CreatureName getCreatureAt(int x, int y, int index);
    boolean hasProperty(int x, int y, CreatureProperty property);
    boolean isOccupied(int x, int y);
    boolean canReach(int x1, int y1, int x2, int y2);
}
