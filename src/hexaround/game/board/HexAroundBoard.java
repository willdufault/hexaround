

/*
 * Copyright (c) 2023. Gary F. Pollice
 *
 * This files was developed for personal or educational purposes. All rights reserved.
 *
 *  You may use this software for any purpose except as follows:
 *  1) You may not submit this file without modification for any educational assignment
 *      unless it was provided to you as part of starting code that does not require modification.
 *  2) You may not remove this copyright, even if you have modified this file.
 */

package hexaround.game.board;

import hexaround.game.board.coordinate.*;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.rule.CreatureName;

import java.util.*;

import static hexaround.game.board.coordinate.HexCoordinate.*;

/**
 * This class manages the game board. It provides all information about the board,
 * the state of the board, and other things that the board knows about.
 * The board is assumed to be infinite.
 */
public class HexAroundBoard {
    private Map<HexCoordinate, LinkedList<CreaturePiece>> occupiedHexes = null;
    private HexCoordinate blueButterfly = null;
    private HexCoordinate redButterfly = null;

    public HexAroundBoard() {
        this.occupiedHexes = new HashMap<>();
    }

    /**
     * Get the creature at a given tile and position in the tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param index The position at the tile.
     * @return the Creature definition of the creature at (x, y) or null if there is none.
     */
    public CreatureName getCreatureAt(int x, int y, int index) {
        HexCoordinate hex = makeCoordinate(x, y);

        if(!this.occupiedHexes.containsKey(hex)) {
            return null;
        }

        LinkedList<CreaturePiece> creatures = this.occupiedHexes.get(makeCoordinate(x, y));

        if(index >= creatures.size()) {
            return null;
        }

        return creatures.get(index).creature();
    }

    public HexCoordinate getButterflyTile(boolean team) {
        return team ? this.blueButterfly : this.redButterfly;
    }

    /**
     * Clear the butterfly tile for the given team.
     * @param team The team whose butterfly should be cleared.
     */
    public void clearButterflyTile(boolean team) {
        if(team) {
            this.blueButterfly = null;
        }

        else {
            this.redButterfly = null;
        }
    }

    /**
     * Get the list of creatures at a tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The list of creatures at the given position.
     */
    public LinkedList<CreaturePiece> getCreaturesAt(int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);

        return new LinkedList<>(this.occupiedHexes.getOrDefault(hex, new LinkedList<>()));
    }

    /**
     * Determine if a given coordinate has at least one piece.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return True if the given coordinate is occupied.
     */
    public boolean isOccupied(int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);

        return this.occupiedHexes.containsKey(hex) && this.occupiedHexes.get(hex).size() > 0;
    }

    /**
     * Determine if a given coordinate has two creatures on it.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return True if the given coordinate is full.
     */
    public boolean isFull(int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);

        return this.occupiedHexes.containsKey(hex) && this.occupiedHexes.get(hex).size() == 2;
    }

    /**
     * Determine if a given coordinate has at least one occupied neighbor.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return True if there is at least one occupied neighbor.
     */
    public boolean hasOccupiedNeighbor(int x, int y) {
        for(HexCoordinate neighbor : makeCoordinate(x, y).neighbors()) {
            if(this.isOccupied(neighbor.x(), neighbor.y())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine if a piece's neighbors contains at least one piece matching the given team.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param team The team to check for.
     * @return True if any neighboring tiles of (x, y) contain a piece on the given team.
     */
    public boolean neighborsContainTeam(int x, int y, boolean team) {
        for(HexCoordinate neighbor : makeCoordinate(x, y).neighbors()) {
            if(!this.occupiedHexes.containsKey(neighbor)) {
                continue;
            }

            for(CreaturePiece piece : this.occupiedHexes.get(neighbor)) {
                if(piece.team() == team) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Return true if the colony is connected.
     */
    public boolean isColonyConnected() {
        Set<HexCoordinate> keys = this.occupiedHexes.keySet();

        // Check that the number of hexes connected to the first hex matches the total number of occupied hexes.
        return this.getClusterSize(keys.iterator().next(), new HashSet<>()) == keys.size();
    }

    /**
     * Recursively count the number of connected hexes.
     * @param hex The current hex coordinate.
     * @param seen The hex coordinate seen so far.
     * @return The size of this hex cluster.
     */
    private int getClusterSize(HexCoordinate hex, HashSet<HexCoordinate> seen) {
        if(!this.occupiedHexes.containsKey(hex)) {
            return 0;
        }

        if(seen.contains(hex)){
            return 0;
        }
        seen.add(hex);

        // Include this hex.
        int count = 1;

        for(HexCoordinate neighbor : hex.neighbors())
            count += getClusterSize(neighbor, seen);

        return count;
    }

    /**
     * Place a creature at the given position on the given team.
     * @param creature A creature name.
     * @param team A player team.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void placeCreatureAt(CreatureName creature, boolean team, int x, int y) {
        int index = 0;

        HexCoordinate hex = makeCoordinate(x, y);
        if(this.occupiedHexes.containsKey(hex)) {
            index = this.occupiedHexes.get(hex).size();
        }

        this.placeCreatureAt(creature, team, x, y, index);
    }

    /**
     * Put a creature on the board.
     * @param creature A creature name.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void placeCreatureAt(CreatureName creature, boolean team, int x, int y, int index) {
        HexCoordinate hex = makeCoordinate(x, y);

        if(!this.occupiedHexes.containsKey(hex))
            this.occupiedHexes.put(hex, new LinkedList<>());

        this.occupiedHexes.get(hex).add(index, new CreaturePiece(creature, team));

        if(creature.equals(CreatureName.BUTTERFLY)) {
            if (team) {
                this.blueButterfly = hex;
            }

            else {
                this.redButterfly = hex;
            }
        }
    }

    /**
     * Remove a creature from a coordinate.
     * @param creature A creature name.
     * @param team A player team.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The position (index) of the creature at its hex.
     */
    public int removeCreature(CreatureName creature, boolean team, int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);
        LinkedList<CreaturePiece> pieces = this.occupiedHexes.get(hex);

        if(pieces.size() == 1) {
            this.occupiedHexes.remove(hex);
            return 0;
        }

        int index = pieces.indexOf(new CreaturePiece(creature, team));
        pieces.remove(index);

        return index;
    }

    /**
     * Determines if a given tile is surrounded by pieces.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return True if the tile is surrounded.
     */
    public boolean isSurrounded(int x, int y) {
        for(HexCoordinate neighbor : makeCoordinate(x, y).neighbors()) {
            if(!this.isOccupied(neighbor.x(), neighbor.y())) {
                return false;
            }
        }

        return true;
    }
}
