package hexaround.game.ability;

import hexaround.game.board.HexAroundBoard;
import hexaround.game.board.coordinate.HexCoordinate;

import java.util.HashSet;

import static hexaround.game.board.coordinate.HexCoordinate.makeCoordinate;

public abstract class AbstractAbility {
    /**
     * Determine if a piece at (fromX, fromY) can be dragged to (toX, toY).
     * @param board The hex board.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @return True if the piece at (fromX, fromY) can be dragged to (toX, toY).
     */
    public boolean canDrag(HexAroundBoard board, int fromX, int fromY, int toX, int toY) {
        // Assuming the tile at (toX, toY) is not occupied, the tile at (fromX, fromY) must share at least 2 non-occupied
        // neighbors with the tile at (toX, toY).
        HashSet<HexCoordinate> toNeighbors = new HashSet<>(makeCoordinate(toX, toY).neighbors());
        int count = 0;

        for(HexCoordinate hex : makeCoordinate(fromX, fromY).neighbors()) {
            if(!board.isOccupied(hex.x(), hex.y())) {
                count += 1;

                if(count == 2) {
                    return true;
                }
            }
        }

        return false;
    }
}
