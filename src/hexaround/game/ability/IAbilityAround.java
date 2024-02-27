package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;

public interface IAbilityAround extends IAbility {
    public boolean isValidMove(HexAroundBoard board, int fromX, int fromY, int toX, int toY, boolean intruding);
}
