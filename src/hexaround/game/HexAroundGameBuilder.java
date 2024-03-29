/*
 * ******************************************************************************
 *  This files was developed for CS4233: Object-Oriented Analysis & Design.
 *  The course was taken at Worcester Polytechnic Institute.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  * Copyright ©2016-2017 Gary F. Pollice
 *  ******************************************************************************
 *
 * This class:
 * MUST be MODIFIED to create an instance of the IHexAroundGameManager interface.
 * MAY NOT be MOVED from this package.
 */

package hexaround.game;

import hexaround.config.*;
import hexaround.game.board.*;

import java.io.*;

public class HexAroundGameBuilder {
    /**
     * Create an instance of a HexAround game.
     * @param configurationFile The game configuration.
     * @return The game manager.
     * @throws IOException If file is not found.
     */
    public static IHexAroundFinal buildGameManager(String configurationFile) throws IOException {
        HexAroundConfigurationMaker configurationMaker =
            new HexAroundConfigurationMaker(configurationFile);
        GameConfiguration configuration = configurationMaker.makeConfiguration();
        HexAroundFirstSubmission gameManager = new HexAroundFirstSubmission();

        gameManager.setBoard(new HexAroundBoard());
        gameManager.setCreatureMap(configuration.creatures());
        gameManager.setPlayerInventories(configuration.players());

        return gameManager;
    }
}
