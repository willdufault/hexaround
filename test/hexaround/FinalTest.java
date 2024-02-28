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
    HexAroundFirstSubmission game1 = null;

    // TODO: TEST WITH THIS INTERFACE B/C HE MIGHT TEST WITH THIS.
//    IHexAround1 game1 = null;

    public FinalTest() throws IOException {
        // NOTE: CHANGED buildGameManager() RETURN TYPE TO CALL GETTERS/SETTERS
        this.game1 = HexAroundGameBuilder.buildGameManager("testConfigurations/FirstConfiguration.hgc");
    }

    @Test
    void testPlaceAndGetCreature() {
        this.game1.placeCreature(GRASSHOPPER, 5, 42);

        assertEquals(GRASSHOPPER, this.game1.getCreatureAt(5, 42));
    }

    @Test
    void testGetCreatureEmpty() {
        assertNull(this.game1.getCreatureAt(0, 0));
    }

    @Test
    void testPieceHasProperty() {
        this.game1.placeCreature(GRASSHOPPER, 0, 0);

        assertTrue(this.game1.hasProperty(0, 0, CreatureProperty.JUMPING));
    }

    @Test
    void testPieceNotHasProperty() {
        this.game1.placeCreature(GRASSHOPPER, 0, 0);

        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.FLYING));
    }

    @Test
    void testEmptyHasProperty() {
        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.WALKING));
    }

    @Test
    void testYesIsOccupied() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

        assertTrue(this.game1.isOccupied(0, 0));
    }

    @Test
    void testNoIsOccupied() {
        assertFalse(this.game1.isOccupied(0, 0));
    }

    @Test
    void testYesCanReach() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

        assertTrue(this.game1.canReach(0, 0, 0, 1));
    }

    @Test
    void testNoCanReach() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

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
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        assertEquals(1, this.game1.getMoveCount());

        this.game1.placeCreature(BUTTERFLY, 0, 1);
        assertEquals(2, this.game1.getMoveCount());

        // Move creature.
        this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0);
        assertEquals(3, this.game1.getMoveCount());

        this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1);
        assertEquals(4, this.game1.getMoveCount());
    }

    @Test
    void testTrackTeam() {
        assertTrue(this.game1.getTeam());

        // Place creature.
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        assertFalse(this.game1.getTeam());

        this.game1.placeCreature(BUTTERFLY, 0, 1);
        assertTrue(this.game1.getTeam());

        // Move creature.
        this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0);
        assertFalse(this.game1.getTeam());

        this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1);
        assertTrue(this.game1.getTeam());
    }

    @Test
    void testFirstMoveLegal() {
        assertEquals(new MoveResponse(OK, "Legal move"), this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalOccupied() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Tile is already occupied."),
                this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalNotConnected() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "The colony must remain connected."),
                this.game1.placeCreature(BUTTERFLY, 2, 0));
    }

    @Test
    void testSecondMoveLegal() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);

        assertEquals(new MoveResponse(OK, "Legal move"),
                this.game1.placeCreature(BUTTERFLY, 1, 0));
    }

    @Test
    void testButterflyRoundFourRequired() {
        this.game1.placeCreature(GRASSHOPPER, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        this.game1.placeCreature(GRASSHOPPER, -1, 0);
        this.game1.placeCreature(GRASSHOPPER, 2, 0);

        this.game1.placeCreature(GRASSHOPPER, -2, 0);
        this.game1.placeCreature(GRASSHOPPER, 3, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Player must place their butterfly."),
                this.game1.placeCreature(GRASSHOPPER, -3, 0));

        assertEquals(new MoveResponse(OK, "Legal move"),
                this.game1.placeCreature(BUTTERFLY, -3, 0));

    }

    @Test
    void testPlaceNextToEnemyPieceIllegal() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        this.game1.placeCreature(GRASSHOPPER, -1, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Cannot place a piece next to an enemy piece."),
                this.game1.placeCreature(GRASSHOPPER, -2, 0));

    }

    @Test
    void testMoveButterfly() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        assertEquals(new MoveResponse(OK, "Legal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 1));
    }

    @Test
    void testMoveButterflyFar() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 2, 0));
    }

    @Test
    void testMoveButterflyOnTop() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }

    @Test
    void testMoveButterflyOnFullTile() {
        this.game1.placeCreature(BUTTERFLY, 0, 0);
        this.game1.placeCreature(BUTTERFLY, 1, 0);

        this.game1.placeCreature(GRASSHOPPER, 0, -1);
        this.game1.placeCreature(GRASSHOPPER, 2, 0);

        this.game1.moveCreature(GRASSHOPPER, 0, -1, 1, -1);
        this.game1.moveCreature(GRASSHOPPER, 2, 0, 1, 0);

        assertEquals(new MoveResponse(MOVE_ERROR, "Illegal move"),
                this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }
}
