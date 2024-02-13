package hexaround;

import hexaround.game.*;
import hexaround.rules.CreatureProperty;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.rules.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    String hgcFile1 = "testConfigurations/FirstConfiguration.hgc";
    IHexAround1 game1 = null;

    public Submission1Test() throws IOException {
        // Not testing buildGameManager because it was given.
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

        assertTrue(this.game1.canReach(0, 0, 0, 2));
    }

    @Test
    void testEmptyCanReach() {
        assertFalse(this.game1.canReach(0, 0, 0, 0));
    }
}
