package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;

public interface IAbilityOver extends IAbility {
    public boolean isValidMove(HexAroundBoard board, int fromX, int fromY, int toX, int toY);
}
