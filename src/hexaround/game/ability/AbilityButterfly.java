package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;

public class AbilityButterfly implements IAbilityAround {
    @Override
    public boolean isValidMove(HexAroundBoard board, int fromX, int fromY, int toX, int toY, boolean intruding) {
        return false;
    }
}
