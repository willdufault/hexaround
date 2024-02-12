package hexaround;

import hexaround.board.Board;
import hexaround.game.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.required.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    HexAroundFirstSubmission gameManager = null;

    @Test
    void firstTest() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager =
            HexAroundGameBuilder.buildGameManager(
                "testConfigurations/FirstConfiguration.hgc");

        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(GRASSHOPPER,gameManager.getCreatureAt(5, 42));
    }

    @Test
    void printBoard() {
        Board board = new Board(5);
        System.out.println(board);

        assert(true);
    }
}
