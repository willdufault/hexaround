

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
        occupiedHexes = new HashMap<>();
    }

    /**
     * @param x
     * @param y
     * @return the Creature definition of the creature at (x, y) or
     *  null if there is none.
     */
    public CreatureName getCreatureAt(int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);

        if(!this.occupiedHexes.containsKey(hex)) return null;

        return occupiedHexes.get(makeCoordinate(x, y)).get(0).creature();
    }

    public HexCoordinate getButterflyTile(boolean team) {
        return team ? this.blueButterfly : this.redButterfly;
    }

    /**
     *
     * @param x
     * @param y
     * @return The list of creatures at the given position.
     */
    public LinkedList<CreaturePiece> getCreaturesAt(int x, int y) {
        HexCoordinate hex = makeCoordinate(x, y);

        return new LinkedList<>(this.occupiedHexes.getOrDefault(hex, new LinkedList<>()));
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isCreatureAt(int x, int y) {
        return occupiedHexes.containsKey(makeCoordinate(x, y));
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
     *
     * @param x
     * @param y
     * @param team
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
     * Recursively count all nodes directly connected to hex.
     */
    private int getClusterSize(HexCoordinate hex, HashSet<HexCoordinate> seen) {
        if(!this.occupiedHexes.containsKey(hex)) return 0;

        if(seen.contains(hex)) return 0;
        seen.add(hex);

        // Include this hex.
        int count = 1;

        for(HexCoordinate neighbor : hex.neighbors())
            count += getClusterSize(neighbor, seen);

        return count;
    }

    /**
     * Put a creature on the board.
     * @param creature
     * @param x
     * @param y
     * @return true if okay, false if there is a piece on the location
     */
    public void placeCreatureAt(CreatureName creature, boolean team, int x, int y) {

        //todo: need to pass in an index to place the creature at that position in the list
        HexCoordinate hex = makeCoordinate(x, y);

        if(!this.occupiedHexes.containsKey(hex))
            this.occupiedHexes.put(hex, new LinkedList<>());

        this.occupiedHexes.get(hex).add(new CreaturePiece(creature, team));

        if(creature.name().equals(CreatureName.BUTTERFLY.name())) {
            if(team)
                this.blueButterfly = hex;

            else
                this.redButterfly = hex;
        }
    }

    /**
     * Removes the creature at the given coordinate.
     */
    public void removeCreature(CreatureName creature, boolean team, int x, int y) {

        // todo: need to return the index the creature was at so i can place it back after
        HexCoordinate hex = makeCoordinate(x, y);
        LinkedList<CreaturePiece> pieces = this.occupiedHexes.get(hex);

        if(pieces.size() == 1) {
            this.occupiedHexes.remove(hex);
            return;
        }

        // Remove the first matching instance.
        for(CreaturePiece cp : pieces) {
            if (cp.creature().name().equals(creature.name()) && cp.team() == team) {
                pieces.remove(cp);
                break;
            }
        }
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
