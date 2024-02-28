package hexaround;

import hexaround.game.*;
import hexaround.game.move.MoveResponse;
import hexaround.game.move.MoveResult;
import hexaround.game.rule.CreatureProperty;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.game.rule.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;
import static hexaround.game.move.MoveResult.*;

public class FinalTest {
    String hgcFile1 = "testConfigurations/FirstConfiguration.hgc";
    String hgcTest = "testConfigurations/TestConfiguration.hgc";
    HexAroundFirstSubmission game1 = null;
    MoveResponse legalMove = new MoveResponse(OK, "Legal move");

    // TODO: TEST WITH THIS INTERFACE B/C HE MIGHT TEST WITH THIS.
    // IHexAround1 game1 = null;

    public FinalTest() throws IOException {
        // TODO: NOTE: CHANGED buildGameManager() RETURN TYPE TO CALL GETTERS/SETTERS
        this.game1 = HexAroundGameBuilder.buildGameManager(hgcTest);
    }

    @Test
    void testPlaceAndGetCreature() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 5, 42));

        assertEquals(CRAB, this.game1.getCreatureAt(5, 42));
    }

    @Test
    void testGetCreatureEmpty() {
        assertNull(this.game1.getCreatureAt(0, 0));
    }

    @Test
    void testPieceHasProperty() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));

        assertTrue(this.game1.hasProperty(0, 0, CreatureProperty.JUMPING));
    }

    @Test
    void testPieceNotHasProperty() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));

        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.FLYING));
    }

    @Test
    void testEmptyHasProperty() {
        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.WALKING));
    }

    @Test
    void testYesIsOccupied() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertTrue(this.game1.isOccupied(0, 0));
    }

    @Test
    void testNoIsOccupied() {
        assertFalse(this.game1.isOccupied(0, 0));
    }

    @Test
    void testYesCanReach() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertTrue(this.game1.canReach(0, 0, 0, 1));
    }

    @Test
    void testNoCanReach() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertFalse(this.game1.canReach(0, 0, 0, 2));
    }

    @Test
    void testEmptyCanReach() {
        assertFalse(this.game1.canReach(0, 0, 0, 0));
    }

    @Test
    void testTrackPlayerTurn() {
        assertEquals(0, this.game1.getMoveCount());

        // Place creature.
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(1, this.game1.getMoveCount());

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 1));
        assertEquals(2, this.game1.getMoveCount());

        // Move creature.
        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
        assertEquals(3, this.game1.getMoveCount());

        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1));
        assertEquals(4, this.game1.getMoveCount());
    }

    @Test
    void testTrackTeam() {
        assertTrue(this.game1.getTeam());

        // Place creature.
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertFalse(this.game1.getTeam());

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 1));
        assertTrue(this.game1.getTeam());

        // Move creature.
        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
        assertFalse(this.game1.getTeam());

        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1));
        assertTrue(this.game1.getTeam());
    }

    @Test
    void testFirstMoveLegal() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalOccupied() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Tile is already occupied."),
                this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "The colony must remain connected."),
                this.game1.placeCreature(BUTTERFLY, 2, 0));
    }

    @Test
    void testSecondMoveLegal() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));
    }

    @Test
    void testButterflyRoundFourRequired() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Player must place their butterfly."),
                this.game1.placeCreature(CRAB, -3, 0));

        assertEquals(legalMove,
                this.game1.placeCreature(BUTTERFLY, -3, 0));

    }

    @Test
    void testPlaceNextToEnemyPieceIllegal() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Cannot place a piece next to an enemy piece."),
                this.game1.placeCreature(CRAB, -2, 0));

    }

    @Test
    void testMoveButterflyClose() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 1));
    }

    @Test
    void testMoveButterflyFar() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 2, 0));
    }

    @Test
    void testMoveButterflyOnTop() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }

    @Test
    void testMoveButterflyOnFullTile() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 0, -1, 1, -1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, 0, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 1, -1));
    }

    @Test
    void testMoveButterflyDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.moveCreature(BUTTERFLY, 0, 0, -1, 1));
    }

    @Test
    void testMoveButterflyNotDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, -1, 1));
    }

    @Test
    void testMoveWalkingNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(CRAB, 0, 0, 0, 2));
    }

    @Test
    void testMoveWalkingTooFar() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(CRAB, -1, 0, 3, -1));
    }

    @Test
    void testMoveWalkingOnTopNoIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(CRAB, -1, 0, 1, 0));
    }

    @Test
    void testMoveWalkingOnTopIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
    }

    @Test
    void testNoMatchingPiece() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "There is no matching creature piece to move on that tile."),
                this.game1.moveCreature(BUTTERFLY, -1, 0, 1, 0));
    }

    @Test
    void testWalkingMiddleNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, -1));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 3, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -2));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 4, -2));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(CRAB, 0, -2, 3, -2));
    }

    @Test
    void testWalkingInPlaceFail() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 0));
    }
}
