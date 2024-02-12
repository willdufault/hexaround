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

import java.io.*;
import java.util.LinkedList;

public class HexAroundGameBuilder {
    public static IHexAround1 buildGameManager(String configurationFile) throws IOException {
        HexAroundConfigurationMaker configurationMaker = new HexAroundConfigurationMaker(configurationFile);
        GameConfiguration configuration = configurationMaker.makeConfiguration();

        // TODO: Use the configuration to build your game manager
        // Make the code readable and use helper methods as needed.
        // Add setters and getters to the game manager that the builder calls.

        HexAroundFirstSubmission gameManager = new HexAroundFirstSubmission();
        gameManager.setPlayers(configuration.players());
        gameManager.setCreatures(configuration.creatures());

        return gameManager;
    }
}
