package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.rule.CreatureName;

import static hexaround.game.board.coordinate.HexCoordinate.makeCoordinate;

public class AbilityQueen extends AbstractAbility implements IAbility {
    /**
     * Determine if the move from (fromX, fromY) to (toX, toY) is legal.
     * @param board The hex board.
     * @param creature The name of the creature.
     * @param team The team the creature is on.
     * @param intruding Whether the creature at (fromX, fromY) has the intruding attribute.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @return True if the move is legal.
     */
    public boolean isLegalMove(HexAroundBoard board, CreatureName creature, boolean team, boolean intruding,
                               int fromX, int fromY, int toX, int toY, int dids) {

       // todo: queen is the property that ends the game when surrounded, this is just walking 1. do walking and replace
       // instances of queen with walking 1 in the game manager


        if(board.isFull(toX, toY)) {
            return false;
        }

        if(makeCoordinate(fromX, fromY).distanceTo(makeCoordinate(toX, toY)) != 1) {
            return false;
        }

        if(!intruding && !this.canDrag(board, fromX, fromY, toX, toY)) {
            return false;
        }

        board.removeCreature(creature, team, fromX, fromY);
        board.placeCreatureAt(creature, team, toX, toY);

        boolean connected = board.isColonyConnected();

        board.removeCreature(creature, team, toX, toY);
        board.placeCreatureAt(creature, team, fromX, fromY);

        return connected;
    }
}
