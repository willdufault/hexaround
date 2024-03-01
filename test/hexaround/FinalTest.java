package hexaround;

import hexaround.game.*;
import hexaround.game.move.MoveResponse;
import hexaround.game.rule.CreatureProperty;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.game.rule.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;
import static hexaround.game.move.MoveResult.*;

public class FinalTest {
    String hgcTest = "configurations/TestConfiguration.hgc";
    String hgcTestAttributes = "configurations/TestConfigurationAttributes.hgc";

    IHexAroundFinal game1 = null;
    IHexAroundFinal game2 = null;
    // Move responses.
    MoveResponse legalMove = new MoveResponse(OK, "Legal move.");
    MoveResponse noPath = new MoveResponse(MOVE_ERROR, "No legal path exists.");
    MoveResponse noSkippingTurns = new MoveResponse(MOVE_ERROR, "Not allowed to skip turns.");
    MoveResponse fullTile = new MoveResponse(MOVE_ERROR, "That tile is full.");
    MoveResponse occupiedNoIntruding = new MoveResponse(MOVE_ERROR,
            "That tile is occupied and this piece is not intruding.");
    MoveResponse tileFar = new MoveResponse(MOVE_ERROR, "That tile too far.");
    MoveResponse notStraight = new MoveResponse(MOVE_ERROR,
            "Jumping pieces must move in a straight line.");
    MoveResponse colonyDisconnected = new MoveResponse(MOVE_ERROR, "That move leaves the colony disconnected.");
    MoveResponse notMaxDistance = new MoveResponse(MOVE_ERROR,
            "Distance between tiles must match the max distance.");
    MoveResponse cantMoveSurrounded = new MoveResponse(MOVE_ERROR, "Flying pieces can't move when surrounded.");

    public FinalTest() throws IOException {
        this.game1 = HexAroundGameBuilder.buildGameManager(hgcTest);
        this.game2 = HexAroundGameBuilder.buildGameManager(hgcTestAttributes);
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
    void testEmptyHasProperty() {
        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.WALKING));
    }

    @Test
    void testPieceNotHasProperty() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));

        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.FLYING));
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

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -3, 0));
    }

    @Test
    void testButterflyKamikazeRoundFourRequired() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game2.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game2.placeCreature(TURTLE, 0, -1));
        assertEquals(legalMove, this.game2.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game2.moveCreature(TURTLE, 0, -1, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Player must place their butterfly."),
                this.game2.placeCreature(CRAB, 3, 0));

        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 3, 0));
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

        assertEquals(tileFar, this.game1.moveCreature(BUTTERFLY, 0, 0, 2, 0));
    }

    @Test
    void testMoveButterflyOnTop() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(occupiedNoIntruding, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }

    @Test
    void testMoveButterflyOnFullTile() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 0, -1, 1, -1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, 0, 1, -1));

        assertEquals(fullTile, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, -1));
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

        assertEquals(noPath, this.game1.moveCreature(BUTTERFLY, 0, 0, -1, 1));
    }

    @Test
    void testMoveWalkingNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(CRAB, 0, 0, 0, 2));
    }

    @Test
    void testMoveWalkingTooFar() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(tileFar, this.game1.moveCreature(CRAB, -1, 0, 3, -1));
    }

    @Test
    void testMoveWalkingOnTopNotIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(occupiedNoIntruding, this.game1.moveCreature(CRAB, -1, 0, 1, 0));
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
    void testMoveWalkingMiddleNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, -1));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 3, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -2));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 4, -2));

        assertEquals(noPath, this.game1.moveCreature(CRAB, 0, -2, 3, -2));
    }

    @Test
    void testMoveWalkingInPlaceFail() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 0));
    }

    @Test
    void testMoveWalkingInPlaceIntrudingFail() {
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(TURTLE, 0, 0, 0, 0));
    }

    @Test
    void testMoveWalkingOnTopIntrudingMiddle() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
    }

    @Test
    void testMoveWalkingOnFullIntrudingMiddle() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
    }

    @Test
    void testMoveFlyingInPlaceFail() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 0, 0));
    }

    @Test
    void testMoveFlyingInPlaceIntrudingFail() {
        assertEquals(legalMove, this.game1.placeCreature(DOVE, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(DOVE, 0, 0, 0, 0));
    }

    @Test
    void testMoveFlyingClose() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 2, 0));
    }

    @Test
    void testMoveFlyingFar() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(tileFar, this.game1.moveCreature(HUMMINGBIRD, -1, 0, 3, 0));
    }

    @Test
    void testMoveFlyingNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 0));
    }

    @Test
    void testMoveFlyingOverFull() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(legalMove, this.game1.moveCreature(HUMMINGBIRD, -1, 0, 2, 0));
    }

    @Test
    void testMoveFlyingLandOnCreatureNoIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(occupiedNoIntruding, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 0, 1));
    }

    @Test
    void testMoveFlyingLandOnCreatureIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(DOVE, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(legalMove, this.game1.moveCreature(DOVE, 0, 0, 0, 1));
    }

    @Test
    void testMoveFlyingLandOnFull() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(DOVE, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
        assertEquals(fullTile, this.game1.moveCreature(DOVE, 2, 0, 1, 0));
    }

    @Test
    void testMoveFlyingDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 1));
    }

    @Test
    void testMoveFlyingNotDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 1));
    }

    @Test
    void testMoveFlyingSurrounded() {
        assertEquals(legalMove, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(cantMoveSurrounded, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 2));
    }

    @Test
    void testMoveRunningInPlaceFail() {
        assertEquals(legalMove, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(SPIDER, 0, 0, 0, 0));
    }

    @Test
    void testMoveRunningIntrudingFail() {
        assertEquals(legalMove, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(SPIDER, 0, 0, 0, 0));
    }

    @Test
    void testMoveRunningClose() {
        assertEquals(legalMove, this.game1.placeCreature(HORSE, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(HORSE, 0, 0, 1, -1));
    }

    @Test
    void testMoveRunningFar() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(HORSE, -1, 0, 3, -1));
    }

    @Test
    void testMoveRunningExact() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(HORSE, -1, 0, 1, 1));
    }

    @Test
    void testMoveRunningCloseIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(SPIDER, 0, 0, 1, -1));
    }

    @Test
    void testMoveRunningFarIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(SPIDER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(notMaxDistance, this.game1.moveCreature(SPIDER, -1, 0, 3, -1));
    }

    @Test
    void testMoveRunningFarNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(HORSE, -1, 0, -4, 0));
    }

    @Test
    void testMoveRunningExactIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 3, -1));

        assertEquals(legalMove, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningExactIntrudingOccupied() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(legalMove, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningExactMiddleNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, -1));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 3, -1));

        assertEquals(legalMove, this.game1.placeCreature(HORSE, 0, -2));
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 4, -2));

        assertEquals(noPath, this.game1.moveCreature(HORSE, 0, -2, 3, -2));
    }

    @Test
    void testMoveRunningExactOnFullIntrudingMiddle() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, -1, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningIntrudingLandOnFull() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 3, 0));

        assertEquals(legalMove, this.game1.placeCreature(SPIDER, -3, 0));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 3, 0, 2, 0));

        assertEquals(fullTile, this.game1.moveCreature(SPIDER, -3, 0, 2, 0));
    }

    @Test
    void testMoveRunningKamikazeLandOnFull() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(SPIDER, 2, 0));

        assertEquals(legalMove, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(legalMove, this.game2.moveCreature(SPIDER, 2, 0, 1, 0));
    }

    @Test
    void testMoveRunningLandOnOccupied() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(occupiedNoIntruding, this.game1.moveCreature(HORSE, -1, 0, 2, 0));
    }

    @Test
    void testMoveJumpingInPlaceFail() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(GRASSHOPPER, 0, 0, 0, 0));
    }

    @Test
    void testMoveJumpingInPlaceIntrudingFail() {
        assertEquals(legalMove, this.game1.placeCreature(RABBIT, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(noSkippingTurns, this.game1.moveCreature(RABBIT, 0, 0, 0, 0));
    }

    @Test
    void testMoveJumpingCloseStraight() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(legalMove, this.game1.moveCreature(GRASSHOPPER, 0, 0, 2, 0));
    }

    @Test
    void testMoveJumpingCloseNotStraight() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(notStraight, this.game1.moveCreature(GRASSHOPPER, 0, 0, 2, -1));
    }

    @Test
    void testMoveJumpingFar() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(tileFar, this.game1.moveCreature(GRASSHOPPER, -1, 0, 3, 0));
    }

    @Test
    void testMoveJumpingNotConnected() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 0));
    }

    @Test
    void testMoveJumpingOverFull() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(legalMove, this.game1.moveCreature(GRASSHOPPER, -1, 0, 2, 0));
    }

    @Test
    void testMoveJumpingLandOnCreatureNoIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(occupiedNoIntruding, this.game1.moveCreature(GRASSHOPPER, 0, 0, 0, 1));
    }

    @Test
    void testMoveJumpingLandOnCreatureIntruding() {
        assertEquals(legalMove, this.game1.placeCreature(RABBIT, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(legalMove, this.game1.moveCreature(RABBIT, 0, 0, 0, 1));
    }

    @Test
    void testMoveJumpingDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 1));
    }

    @Test
    void testMoveJumpingNotDraggable() {
        assertEquals(legalMove, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(legalMove, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 1));
    }

    @Test
    void testMoveJumpingLandOnFull() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(RABBIT, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
        assertEquals(fullTile, this.game1.moveCreature(RABBIT, 2, 0, 1, 0));
    }

    @Test
    void testMoveJumpingKamikazeLandOnFull() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(RABBIT, 2, 0));

        assertEquals(legalMove, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(legalMove, this.game2.moveCreature(RABBIT, 2, 0, 1, 0));
    }

    @Test
    void testOrderStaysMoveSearchBottom() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(CRAB, 2, 0, 4, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));
    }

    @Test
    void testOrderStaysMoveSearchTop() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 3, -2));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));

        assertEquals(colonyDisconnected, this.game1.moveCreature(TURTLE, 2, 0, 4, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));
    }

    @Test
    void testPlaceCreatureNotInInventory() {
        assertEquals(new MoveResponse(MOVE_ERROR, "Player does not have that creature."),
                this.game1.placeCreature(DUCK, 0, 0));
    }

    @Test
    void testPlaceTooManyCreatures() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Player is out of those creatures."),
                this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -3, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 4, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -4, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 5, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -5, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 6, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Player is out of those creatures."),
                this.game1.placeCreature(CRAB, -6, 0));
    }

    @Test
    void testKamikazeEmpty() {
        assertEquals(legalMove, this.game2.placeCreature(TURTLE, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.moveCreature(TURTLE, 0, 0, 1, -1));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, -1));
    }

    @Test
    void testKamikazeOccupied() {
        assertEquals(legalMove, this.game2.placeCreature(TURTLE, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.moveCreature(TURTLE, 0, 0, 1, 0));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, 0));
    }

    @Test
    void testKamikazeFull() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(TURTLE, 2, 0));

        assertEquals(legalMove, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(legalMove, this.game2.moveCreature(TURTLE, 2, 0, 1, 0));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, 0, 1));
        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0, 0));
    }

    @Test
    void testKamikazeUpdateInventory() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game2.placeCreature(TURTLE, -2, 0));
        assertEquals(legalMove, this.game2.placeCreature(CRAB, 3, 0));

        assertEquals(legalMove, this.game2.moveCreature(TURTLE, -2, 0, 1, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 3, -1));
    }

    @Test
    void testSwappingEmpty() {
        assertEquals(legalMove, this.game2.placeCreature(DOVE, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.moveCreature(DOVE, 0, 0, 1, -1));

        assertEquals(DOVE, this.game2.getCreatureAt(1, -1));
    }

    @Test
    void testSwappingButterflyFail() {
        assertEquals(legalMove, this.game2.placeCreature(DOVE, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.moveCreature(DOVE, 0, 0, 1, 0));

        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0));
    }

    @Test
    void testSwappingFull() {
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game2.placeCreature(DOVE, 2, 0));

        assertEquals(legalMove, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(legalMove, this.game2.moveCreature(DOVE, 2, 0, 1, 0));

        assertEquals(DOVE, this.game2.getCreatureAt(1, 0, 1));
        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0, 0));
        assertEquals(CRAB, this.game2.getCreatureAt(2, 0, 0));
    }

    @Test
    void testPlaceBlueWin() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.moveCreature(CRAB, -1, 1, 0, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 1));

        assertEquals(legalMove, this.game1.moveCreature(CRAB, 0, -1, 1, -1));

        assertEquals(new MoveResponse(BLUE_WON, "Blue wins. Red's butterfly is surrounded."),
                this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(new MoveResponse(MOVE_ERROR, "Game is over."), this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testPlaceRedWin() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, -2));
        assertEquals(new MoveResponse(RED_WON, "Red wins. Blue's butterfly is surrounded."),
                this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Game is over."), this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testMoveBlueWin() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.moveCreature(CRAB, -1, 1, 0, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(new MoveResponse(BLUE_WON, "Blue wins. Red's butterfly is surrounded."),
                this.game1.moveCreature(CRAB, 0, -1, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Game is over."),
                this.game1.moveCreature(CRAB, 0, 0, 0, 0));
    }

    @Test
    void testMoveRedWin() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, -2));
        assertEquals(new MoveResponse(RED_WON, "Red wins. Blue's butterfly is surrounded."),
                this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Game is over."), this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testDraw() {
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(legalMove, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(legalMove, this.game1.placeCreature(CRAB, -1, 2));
        assertEquals(legalMove, this.game1.placeCreature(CRAB, 2, -2));

        assertEquals(legalMove, this.game1.moveCreature(CRAB, -1, 2, 0, 1));

        assertEquals(new MoveResponse(DRAW, "Draw. Both butterflies surrounded."),
                this.game1.moveCreature(CRAB, 2, -2, 1, -1));

        assertEquals(new MoveResponse(MOVE_ERROR, "Game is over."), this.game1.placeCreature(CRAB, -1, -1));
    }
}
