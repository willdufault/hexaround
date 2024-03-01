package hexaround.game;

import hexaround.config.*;
import hexaround.game.ability.*;
import hexaround.game.attribute.*;
import hexaround.game.board.*;
import hexaround.game.board.coordinate.*;
import hexaround.game.board.piece.CreaturePiece;
import hexaround.game.move.MoveResponse;
import hexaround.game.rule.CreatureName;
import hexaround.game.rule.CreatureProperty;
import hexaround.game.rule.PlayerName;

import java.util.*;

import static hexaround.game.move.MoveResult.*;

public class HexAroundFirstSubmission implements IHexAroundFinal {

    private HexAroundBoard board = null;
    private Map<CreatureName, CreatureDefinition> creatureMap = null;
    private Map<CreatureProperty, IAbility> abilityMap = null;
    private Map<CreatureProperty, IAttribute> attributeMap = null;
    private Map<Boolean, Map<CreatureName, Integer>> playerInventories = null;
    private boolean team = true;
    // 2 moves = 1 turn. (One move from both players.)
    private int moveCount = 0;
    private boolean gameOver = false;

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        this.initializeAbilityMap();
        this.initializeAttributeMap();
    }

    public boolean getTeam() {
        return this.team;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return board.getCreatureAt(x, y, 0);
    }

    /**
     * Get the creature at a given coordinate with a position at the tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param index The position at the tile.
     * @return The creature's name.
     */
    public CreatureName getCreatureAt(int x, int y, int index) {
        return board.getCreatureAt(x, y, index);
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        boolean result = false;
        CreatureName creature = board.getCreatureAt(x, y, 0);
        if (creature != null) {
            CreatureDefinition cd = creatureMap.get(creature);
            if (cd != null) {
                result = cd.properties().contains(property);
            }
        }
        return result;
    }

    /**
     * Get the movement ability of the given creature.
     * @param creature A creature name.
     * @return The CreatureProperty (movement ability) of this creature.
     */
    private CreatureProperty getAbility(CreatureName creature) {
        for(CreatureProperty property : this.creatureMap.get(creature).properties()) {
            if(this.abilityMap.containsKey(property)) {
                return property;
            }
        }

        return null;
    }

    /**
     * Get all attributes of a given creature.
     * @param creature A creature name.
     * @return A list of all attributes for the given creature.
     */
    private LinkedList<CreatureProperty> getAttributes(CreatureName creature) {
        LinkedList<CreatureProperty> attributes = new LinkedList<>();

        for(CreatureProperty property : this.creatureMap.get(creature).properties()) {
            if(this.attributeMap.containsKey(property)) {
                attributes.add(property);
            }
        }

        return attributes;
    }

    /**
     * Determine if a creature has a given property.
     * @param creature A creature name.
     * @param property A creature property.
     * @return True if the creature has the property.
     */
    private boolean creatureHasProperty(CreatureName creature, CreatureProperty property) {
        return this.creatureMap.get(creature).properties().contains(property);
    }

    /**
     * Given the x and y coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        return board.isOccupied(x, y);
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        boolean result = false;
        CreatureName creature = getCreatureAt(x1, y1);
        if (creature != null) {
            CreatureDefinition cd = creatureMap.get(creature);
            int maxDistance = cd.maxDistance();
            HexCoordinate c1 = new HexCoordinate(x1, y1);
            int actualDistance = c1.distanceTo(new HexCoordinate(x2, y2));
            result = maxDistance >= actualDistance;
        }
        return result;
    }

    /**
     * Determines if the current team's butterfly is down.
     * @param team The player whose butterfly to check.
     * @return True if the butterfly is down.
     */
    private boolean isButterflyDown(boolean team) {
        // BLUE MOVES FIRST.
        return this.board.getButterflyTile(team) != null;
    }

    /**
     * Determines if the given creature is a butterfly.
     * @param creature A creature name.
     * @return True if the given creature is a butterfly.
     */
    private boolean isCreatureButterfly(CreatureName creature) {
        return creature.equals(CreatureName.BUTTERFLY);
    }

    /**
     * Determine if the given team has the given creature.
     * @param team The team.
     * @param creature The creature name.
     * @return True if the player has the creature.
     */
    private boolean playerHasCreature(boolean team, CreatureName creature) {
        return this.playerInventories.get(team).containsKey(creature);
    }

    /**
     * Determine if the player has at least 1 of the given creature in their inventory.
     * @param team The team.
     * @param creature The creature name.
     * @return True if the team can play the creature.
     */
    private boolean playerHasEnough(boolean team, CreatureName creature) {
        return this.playerInventories.get(team).get(creature) > 0;
    }

    /**
     * Update the count of a creature in a player's inventory.
     * @param team The player team.
     * @param creature A creature name.
     * @param delta The change amount.
     */
    private void updateInventory(boolean team, CreatureName creature, int delta) {
        this.playerInventories.get(team).put(creature, this.playerInventories.get(team).get(creature) + delta);
    }

    /**
     * Determine if a given team's butterfly is surrounded.
     * @param team The player team.
     * @return True if the player's butterfly is surrounded.
     */
    private boolean isButterflySurrounded(boolean team) {
        HexCoordinate butterfly = board.getButterflyTile(team);

        if(butterfly == null) {
            return false;
        }

        return this.board.isSurrounded(butterfly.x(), butterfly.y());
    }

    /**
     * Get the current status of the game.
     * @return The status of the game (blue win, red win, draw, continue game).
     */
    private MoveResponse getGameOverStatus() {
        boolean blueSurrounded = this.isButterflySurrounded(true);
        boolean redSurrounded = this.isButterflySurrounded(false);

        if(blueSurrounded && redSurrounded) {
            return new MoveResponse(DRAW, "Draw. Both butterflies surrounded.");
        }

        if(blueSurrounded) {
            return new MoveResponse(RED_WON, "Red wins. Blue's butterfly is surrounded.");
        }

        if(redSurrounded) {
            return new MoveResponse(BLUE_WON, "Blue wins. Red's butterfly is surrounded.");
        }

        return new MoveResponse(OK, "Game continues.");
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature A creature name.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        if(this.gameOver) {
            return new MoveResponse(MOVE_ERROR, "Game is over.");
        }

        if(!this.playerHasCreature(this.team, creature)) {
            return new MoveResponse(MOVE_ERROR, "Player does not have that creature.");
        }

        if(!this.playerHasEnough(this.team, creature)) {
            return new MoveResponse(MOVE_ERROR, "Player is out of those creatures.");
        }

        if(this.isOccupied(x, y)) {
            return new MoveResponse(MOVE_ERROR, "Tile is already occupied.");
        }

        if(this.moveCount >= 1 && !this.board.hasOccupiedNeighbor(x, y)) {
            return new MoveResponse(MOVE_ERROR, "The colony must remain connected.");
        }

        if(this.moveCount >= 2 && this.board.neighborsContainTeam(x, y, !this.team)) {
            return new MoveResponse(MOVE_ERROR, "Cannot place a piece next to an enemy piece.");
        }

        if(this.moveCount >= 6 && !this.isButterflyDown(this.team) && !this.isCreatureButterfly(creature)) {
            return new MoveResponse(MOVE_ERROR, "Player must place their butterfly.");
        }

        this.board.placeCreatureAt(creature, this.team, x, y);
        this.updateInventory(this.team, creature, -1);
        this.moveCount += 1;
        this.team = !this.team;

        MoveResponse gameOverResponse = this.getGameOverStatus();
        if(!gameOverResponse.moveResult().equals(OK)) {
            this.gameOver = true;
            return gameOverResponse;
        }

        return new MoveResponse(OK, "Legal move.");
    }

    /**
     * Attempt to move a piece from (fromX, fromY) to (toX, toY).
     * @param creature A creature name.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @return A MoveResponse with the status and message of the attempted move.
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        if(this.gameOver) {
            return new MoveResponse(MOVE_ERROR, "Game is over.");
        }

        if(!this.board.getCreaturesAt(fromX, fromY).contains(new CreaturePiece(creature, this.team))) {
            return new MoveResponse(MOVE_ERROR, "There is no matching creature piece to move on that tile.");
        }

        // If this is non-empty, the creature is either kamikaze or swapping and can land on full tiles.
        LinkedList<CreatureProperty> attributes = this.getAttributes(creature);

        // If the attempted move is legal or not.
        MoveResponse legalMoveResponse = this.abilityMap.get(this.getAbility(creature)).isLegalMove(this.board, creature,
                this.team, this.creatureHasProperty(creature, CreatureProperty.INTRUDING), attributes.size() > 0,
                fromX, fromY, toX, toY, this.creatureMap.get(creature).maxDistance());

        if(!legalMoveResponse.moveResult().equals(OK)) {
            return legalMoveResponse;
        }

        int index = this.board.removeCreature(creature, this.team, fromX, fromY);
        this.board.placeCreatureAt(creature, this.team, toX, toY);
        this.moveCount += 1;
        this.team = !this.team;

        for(CreatureProperty attribute : attributes) {
            this.attributeMap.get(attribute).takeEffect(board, this.playerInventories, creature, this.team,
                    fromX, fromY, toX, toY, index);
        }

        MoveResponse gameOverResponse = this.getGameOverStatus();
        if(!gameOverResponse.moveResult().equals(OK)) {
            this.gameOver = true;
            return gameOverResponse;
        }

        return legalMoveResponse;
    }

    /************************************ Helpers *********************************/
    public void setBoard(HexAroundBoard board) {
        this.board = board;
    }

    public void setCreatureMap(Collection<CreatureDefinition> creatureDefs) {
        this.creatureMap = new HashMap<>();
        for (CreatureDefinition cd : creatureDefs) {
            this.creatureMap.put(cd.name(), cd);
        }
    }

    public void setPlayerInventories(Collection<PlayerConfiguration> playerConfigs) {
        this.playerInventories = new HashMap<>();

        for(PlayerConfiguration pc : playerConfigs) {
            boolean team = pc.Player().equals(PlayerName.BLUE);

            this.playerInventories.put(team, new HashMap<>(pc.creatures()));
        }
    }

    public void initializeAbilityMap() {
        this.abilityMap = new HashMap<>();
        this.abilityMap.put(CreatureProperty.WALKING, new AbilityWalking());
        this.abilityMap.put(CreatureProperty.RUNNING, new AbilityRunning());
        this.abilityMap.put(CreatureProperty.JUMPING, new AbilityJumping());
        this.abilityMap.put(CreatureProperty.FLYING, new AbilityFlying());
    }

    public void initializeAttributeMap() {
        this.attributeMap = new HashMap<>();
        this.attributeMap.put(CreatureProperty.KAMIKAZE, new AttributeKamikaze());
        this.attributeMap.put(CreatureProperty.SWAPPING, new AttributeSwapping());
    }
}
