# TDD
  
---  

| Name | Type | Done | Description |  
| :-: | :--: | :--: | :---------- |  
| -- | Refactor | X | Refactored starter code for better reusability. |  
| `testPlaceAndGetCreature` | Test | X | Test `placeCreature()` and `getCreatureAt()`. |  
| `testGetCreatureEmpty` | Test | X | Test `getCreatureAt()` with an empty tile. |  
| `testPieceHasProperty` | Test | X | Test `hasProperty()` with a piece that has the given property. |  
| `testPieceNotHasProperty` | Test | X | Test `hasProperty()` with a piece that does not have the given property. |  
| `testYesIsOccupied` | Test | X | Test `isOccupied()` with an occupied tile. |
| `testNoIsOccupied` | Test | X | Test `isOccupied()` with an empty tile. |
| `testYesCanReach` | Test | X | Test `canReach()` with a distance within reach. |
| `testNoCanReach` | Test | X | Test `canReach()` with a distance out of reach. |
| `testEmptyCanReach` | Test | X | Test `canReach()` with an empty source tile. |
| `testTrackPlayerTurn` | Test | X | Test the move count tracker. |
| `testTrackTeam` | Test | X | Test the team tracker. |
| `testFirstMoveLegal` | Test | X | Test that placing the first piece is legal. |
| `testSecondMoveIllegalOccupied` | Test | X | Test that placing a piece on top of another piece fails. |
| `testSecondMoveLegal` | Test | X | Test that placing a piece next to the first piece is legal. |
| `testButterflyRoundFourRequired` | Test | X | Test that players must place the butterfly by round four. |
| `testButterflyKamikazeRoundFourRequired` | Test | X | Test that butterflies are forced if kamikaze'd after round three. |
| `testPlaceNextToEnemyPieceIllegal` | Test | X | Test that placing a piece next to the enemy after the first round is illegal. |
| `testMoveButterflyClose` | Test | X | Test moving a butterfly 1 tile succeeds. |
| `testMoveButterflyFar` | Test | X | Test moving a butterfly multiple tiles fails. |
| `testMoveButterflyOnTop` | Test | X | Test that butterflies can't be moved on top of another piece. |
| `testMoveButterflyOnFullTile` | Test | X | Test that butterflies can't be moved on top of full tiles (2 creatures). |
| `testMoveButterflyDraggable` | Test | X | Test that moving a butterfly is legal when it is "draggable". |
| `testMoveButterflyNotDraggable` | Test | X | Test that moving a butterfly is illegal when it is not "draggable". |
| `testMoveWalkingNotConnected` | Test | X | Test that walking to a disconnected tile fails. |
| `testMoveWalkingTooFar` | Test | X | Test that walking more than the max distance fails. |
| `testMoveWalkingOnTopNotIntruding` | Test | X | Test that walking to an occupied tile without intruding fails. |
| `testMoveWalkingOnTopIntruding` | Test | X | Test that walking to an occupied tile with intruding succeeds. |
| `testNoMatchingPiece` | Test | X |Test that trying to move a piece that doesn't exist on a tile fails. |
| `testMoveWalkingMiddleNotConnected` | Test | X | Test that walking a path that is disconnected in the middle fails. |
| `testMoveWalkingInPlaceFail` | Test | X | Test that walking in place fails. |
| `testMoveWalkingInPlaceIntrudingFail` | Test | X | Test that walking in place with intruding fails. |
| `testMoveWalkingOnTopIntrudingMiddle` | Test | X | Test that walking over a creature with intruding succeeds. |
| `testMoveWalkingOnFullIntrudingMiddle` | Test | X | Test that walking over a full tile with intruding succeeds. |
| -- | Refactor | X | Created variables to reduce code reuse. |
| `testMoveFlyingInPlaceFail` | Test | X | Test that flying in place fails. |
| `testMoveFlyingInPlaceIntrudingFail` | Test | X | Test that flying in place with intruding fails. |
| `testMoveFlyingClose` | Test | X | Test that flying to a tile within distance succeeds. |
| `testMoveFlyingFar` | Test | X | Test that flying to a tile out of distance fails. |
| `testMoveFlyingNotConnected` | Test | X | Test that flying to a disconnected tile fails. |
| `testMoveFlyingOverFull` | Test | X | Test that flying over a full tile succeeds. |
| `testMoveFlyingLandOnCreatureNoIntruding` | Test | X | Test that landing on an occupied tile without intruding fails. |
| `testMoveFlyingLandOnCreatureIntruding` | Test | X | Test that landing on an occupied tile with intruding succeeds.|
| `testMoveFlyingLandOnFull` | Test | X | Test that landing on a full tile fails. |
| `testMoveFlyingDraggable` | Test | X | Test that flying works when the piece is "draggable". |
| `testMoveFlyingNotDraggable` | Test | X | Test that flying works when the piece is not "draggable". |
| `testMoveFlyingSurrounded` | Test | X | Test that flying fails when a piece is surrounded. |
| `testMoveRunningInPlaceFail` | Test | X | Test that running in place fails. |
| `testMoveRunningIntrudingFail` | Test | X | Test that running in place with intruding fails. |
| `testMoveRunningClose` | Test | X | Test that running a distance less than max distance fails. |
| `testMoveRunningFar` | Test | X | Test that running a distance more than max distance fails. |
| `testMoveRunningExact` | Test | X | Test that running the exact distance succeeds. |
| `testMoveRunningCloseIntruding` | Test | X | Test that running a distance less than max distance with intruding fails. |
| `testMoveRunningFarIntruding` | Test | X | Test that running a distance more than max distance with intruding fails. |
| `testMoveRunningFarNotConnected` | Test | X | Test that running a distance more than the max distance that leaves the colony disconnected fails. |
| `testMoveRunningExactIntruding` | Test | X | Test that running the exact distance with intruding succeeds. |
| `testMoveRunningExactIntrudingOccupied` | Test | X | Test that running the exact distance with intruding and occupied succeeds. |
| `testMoveRunningExactMiddleNotConnected` | Test | X | Test that running the exact distance fails when the middle of the path leaves the colony disconnected. |
| `testMoveRunningExactOnFullIntrudingMiddle` | Test | X | Test that running the exact distance with intruding over a full tile and on another creature succeeds. |
| `testMoveRunningIntrudingLandOnFull` | Test | X | Test that running the exact distance with intruding and landing on a creature succeeds. |
| `testMoveRunningKamikazeLandOnFull` | Test | X | Test that running the exact distance and landing on a full tile with kamikaze succeeds. |
| `testMoveRunningLandOnOccupied` | Test | X | Test that running the exact distance and landing on a creature without intruding fails. |
| `testMoveJumpingInPlaceFail` | Test | X | Test that jumping in place fails. |
| `testMoveJumpingInPlaceIntrudingFail` | Test | X | Test that jumping in place with intruding fails. |
| `testMoveJumpingCloseStraight` | Test | X | Test that jumping to a linear tile less than the max distance succeeds. |
| `testMoveJumpingCloseNotStraight` | Test | X | Test that jumping to a non-linear tile less than the max distance fails. |
| `testMoveJumpingFar` | Test | X | Test that jumping to a tile further than the max distance fails. |
| `testMoveJumpingNotConnected` | Test | X | Test that jumping to a tile that leaves the colony disconnected fails. |
| `testMoveJumpingOverFull` | Test | X | Test that jumping to a linear tile over a full tile succeeds. |
| `testMoveJumpingLandOnCreatureNoIntruding` | Test | X | Test that jumping to an occupied, linear tile without intruding fails. |
| `testMoveJumpingLandOnCreatureIntruding` | Test | X | Test that jumping to an occupied, linear tile without intruding succeeds. |
| `testMoveJumpingDraggable` | Test | X | Test that jumping with a piece that is "draggable" succeeds. |
| `testMoveJumpingNotDraggable` | Test | X | Test that jumping with a piece that is not "draggable" succeeds. |
| `testMoveJumpingLandOnFull` | Test | X | Test that jumping to a linear, full tile fails. |
| `testMoveJumpingKamikazeLandOnFull` | Test | X | Test that jumping to a linear, full tile with kamikaze succeeds. |
| -- | Refactor | X | Refactored abilities to return a MoveResult for more descriptive messages. |
| -- | Refactor | X | Created variables to reduce repeat code. |
| `testOrderStaysMoveSearchBottom` | Test | X | Test that piece order on tiles is maintained when moving the bottom piece. |
| `testOrderStaysMoveSearchTop` | Test | X | Test that piece order on tiles is maintained when moving the top piece. |
| `testPlaceCreatureNotInInventory` | Test | X | Test that attempting to place a creature not in the player's inventory fails. |
| `testPlaceTooManyCreatures` | Test | X | Test that attempting to place more creatures than a player started with fails. |
| `testKamikazeEmpty` | Test | X | Test that landing on a non-occupied tile with kamikaze succeeds. |
| `testKamikazeOccupied` | Test | X | Test that landing on an occupied tile with kamikaze succeeds. |
| `testKamikazeFull` | Test | X | Test that landing on a full tile with kamikaze succeeds. |
| `testKamikazeUpdateInventory` | Test | X | Test that landing on a piece with kamikaze returns it to the player's inventory. |
| `testSwappingEmpty` | Test | X | Test that landing on a non-occupied tile with swapping succeeds. |
| `testSwappingButterflyFail` | Test | X | Test that landing on a butterfly with swapping does not swap the butterfly. |
| `testSwappingFull` | Test | X | Test that landing on a full tile with kamikaze succeeds. |
| -- | Refactor | X | Fixed draggability bug and added win/tie checks. |
| `testPlaceBlueWin` | Test | X | Test that placing a piece to trigger a blue win succeeds. |
| `testPlaceRedWin` | Test | X | Test that placing a piece to trigger a red win succeeds. |
| `testMoveBlueWin` | Test | X | Test that moving a piece to trigger a blue win succeeds. |
| `testMoveRedWin` | Test | X | Test that moving a piece to trigger a red win succeeds. |
| `testDraw` | Test | X | Test that moving a piece to trigger a draw succeeds. |

---  

# Design Patterns

| Pattern | File | Description |  
| :-----: | :--: | :---------- |  
| Builder | `HexAroundFirstSubmission.java` | Used the builder design pattern to create a game manager builder without having to pass the entire configuration. |  
| Strategy | `HexAroundFirstSubmission.java` | Used a map to store an instance of each ability/attribute and called the `isValidMove()` and `takeEffect()` methods respectively to eliminate needing to manually check what type of ability/attribute. |
| Factory | `HexCoordinate.java` | Used the factory pattern to create HexCoordinate instances to eliminate needing to call the constructor every time. |