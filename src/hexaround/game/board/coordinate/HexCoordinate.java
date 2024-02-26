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

package hexaround.game.board.coordinate;


import java.util.*;

/**
 * This class represents a Coordinate in the HexAround game. It
 * currently only has a default constructor that takes the two
 * axis values (x, y).
 */
public class HexCoordinate {
    private final int x;
    private final int y;

    /**
     * The only constructor allowed
     * @param x: first axis value
     * @param y: second axis value
     */
    public HexCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Factory method.
     * @param x
     * @param y
     * @return the specified coordinate
     */
    public static HexCoordinate makeCoordinate(int x, int y) {
        return new HexCoordinate(x, y);
    }

    /**
     * This algorithm here is described in (no longer available)
     * {@link http://keekerdc.com/2011/03/hexagon-grids-coordinate-systems-and-distance-calculations/}
     *
     * Other algorithms can be found in
     * {@link https://news.extly.com/2022-joomla-developer/developer-news/14312-everything-you-ever-wanted-to-know-about-hexagonal-grids-redblobgames-com.html}
     *
     * @param otherCoordinate
     * @return the distance between two coordinates
     */
    public int distanceTo(HexCoordinate otherCoordinate) {
        HexCoordinate other = otherCoordinate;
        int distance;
        final int z = -x - y;
        final int otherz = -other.getX() - other.getY();
        final int d1 = Math.abs(other.getX() - x);
        final int d2 = Math.abs(other.getY() - y);
        final int d3 = Math.abs(otherz-z);
        distance = Math.max(d1, d2);
        distance = Math.max(distance, d3);
        return distance;
    }

    /**
     * @param otherCoordinate
     * @return true if the other coordinate is in a straight
     * line (defined by the coordinate type)
     */
    public boolean isLinear(HexCoordinate otherCoordinate) {
        HexCoordinate other = otherCoordinate;
        return x == other.getX() || y == other.getY()
            || (x+y) == (other.getX() + other.getY());
    }

    /**
     * @return a collection of all of the coordinates that
     * are neighbors (usually adjacent coordinates) of this
     * coordinate
     */
    public Collection<HexCoordinate> neighbors() {
        Collection<HexCoordinate> neighbors = new LinkedList<>();
        neighbors.add(new HexCoordinate(x, y-1));
        neighbors.add(new HexCoordinate(x+1, y-1));
        neighbors.add(new HexCoordinate(x+1, y));
        neighbors.add(new HexCoordinate(x, y+1));
        neighbors.add(new HexCoordinate(x-1, y+1));
        neighbors.add(new HexCoordinate(x-1, y));
        return neighbors;
    }

    /* Generated methods */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "HexCoordinate{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HexCoordinate that)) return false;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
