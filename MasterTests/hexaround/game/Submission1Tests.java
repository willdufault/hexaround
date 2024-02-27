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

package hexaround.game;

import hexaround.game.move.MoveResponse;
import hexaround.game.move.MoveResult;
import hexaround.game.rules.CreatureName;
import hexaround.game.rules.CreatureProperty;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import util.*;

import java.io.*;
import java.util.stream.*;

import static hexaround.game.rules.CreatureName.*;
import static hexaround.game.rules.CreatureProperty.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

public class Submission1Tests extends BaseTestMaster {
    private static boolean firstTest = true;

    private final int TEST_POINTS = 25;
    private static IHexAround1 manager;
    @AfterAll
    static void testBreakdown() {
        firstTest = true;
    }

    @BeforeEach
    public void setup () {
        try {
            manager = HexAroundGameBuilder.buildGameManager("MasterConfigurations/Submission1.hgc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Submission1Tests() {
        if (firstTest) {
            testReporter.startNewTestGroup("Submission 1", TEST_POINTS);
        }
        firstTest = false;
    }

    /******************************** The tests ********************************/
    @Test
    void notOccupiedHex() {
        startTest("Not occupied hex", 3);
        assertFalse(manager.isOccupied(1, 5));
        markTestPassed();
    }

    @Test
    void occupiedHex() {
        startTest("Not occupied hex", 3);
        manager.placeCreature(HORSE, 0, 0);
        assertTrue(manager.isOccupied(0, 0));
        markTestPassed();
    }

    @ParameterizedTest
    @MethodSource("propertySupplier")
    void propertyTest(String testName, int testValue, boolean expected, CreatureName creature,
                      CreatureProperty property, int x, int y) {
        startTest(testName, testValue);
        manager.placeCreature(BUTTERFLY, 0, 0);
        manager.placeCreature(creature, x, y);
        assertEquals(expected, manager.hasProperty(x, y, property));
        markTestPassed();
    }

    static Stream<Arguments> propertySupplier() {
        return Stream.of(
            arguments("Butterfly is QUEEN", 2, true, BUTTERFLY, WALKING, 0, 1),
            arguments("Butterfly is QUEEN", 2, true, BUTTERFLY, QUEEN, -1, 1),
            arguments("Dove is KAMIKAZE", 2, true, DOVE, KAMIKAZE, 1, -1),
            arguments("Dove is not WALKING", 2, false, DOVE, WALKING, 0, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("getCreatureSupplier")
    void getCreatureTest(String testName, int testValue, CreatureName expected,
                         int x, int y) {
        startTest(testName, testValue);
        configureBoard();
        assertEquals(expected, manager.getCreatureAt(x, y));
        markTestPassed();
    }

    static Stream<Arguments> getCreatureSupplier() {
        return Stream.of(
            arguments("Butterfly at (0, 0)", 1, BUTTERFLY, 0, 0),
            arguments("Dov1 at (0, 0)", 1, DOVE, 0, 1),
            arguments("Horse at (0, -2)", 1, HORSE, 0, -2),
            arguments("No creature at (10, 10)", 2, null, 10, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("canReachSupplier")
    void canReachTest(String tesName, int testValue, boolean expected,
                      int x1, int y1, int x2, int y2) {
        startTest(currentTestName, testValue);
        configureBoard();
        assertEquals(expected, manager.canReach(x1, y1, x2, y2));
        markTestPassed();
    }

    static Stream<Arguments> canReachSupplier() {
        return Stream.of(
            arguments("Butterfly can move 1", 2, true, 0, 0, -1, 0),
            arguments("Crab cannot move (0, 2) to (1, 4)", 2, false, 0, 2, 1, 4),
            arguments("Horce cannot move (0, -2) to (-2, 0)", 2, true, 0, -2, -2, 0)
        );
    }

    // Submission 2.
    @Test
    void testPlaceCreature() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.OK, "Legal move"), manager.placeCreature(DOVE, 20, 20));
    }

    @Test
    void testMoveCreatureConnectedClose() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.OK, "Legal move"), manager.moveCreature(BUTTERFLY, 1, 1, 1, 0));
    }

    @Test
    void testMoveCreatureConnectedFar1() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again"),
                manager.moveCreature(BUTTERFLY, 1, 1, 1, -2));
    }

    @Test
    void testMoveCreatureConnectedFar2() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again"),
                manager.moveCreature(BUTTERFLY, 0, -2, 0, 3));
    }

    @Test
    void testMoveCreatureNotConnectedClose() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again"),
                manager.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }

    @Test
    void testMoveCreatureNotConnectedFar() {
        configureBoard();
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again"),
                manager.moveCreature(BUTTERFLY, 1, 1, 10, 0));
    }

        /******************************** Helpers ********************************/
        private void configureBoard() {
            manager.placeCreature(BUTTERFLY, 0, 0);
            manager.placeCreature(DOVE, 0, 1);
            manager.placeCreature(CRAB, 0, -1);
            manager.placeCreature(CRAB, 0, 2);
            manager.placeCreature(HORSE, 0, -2);
            manager.placeCreature(BUTTERFLY, 1, 1);
            manager.placeCreature(DOVE, -1, -1);
            manager.placeCreature(GRASSHOPPER, -1, 2);
        }
}
