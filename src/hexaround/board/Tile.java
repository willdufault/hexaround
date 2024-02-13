package hexaround.board;

import hexaround.config.CreatureDefinition;
import hexaround.rules.CreatureName;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.Optional;

/**
 *
 */
public class Tile {
    private LinkedList<CreatureName> creatures = null;

    public Tile() {
        this.creatures = new LinkedList<>();
    }

    /**
     *
     * @param creature
     */
    public void addCreature(CreatureName creature) {
        this.creatures.add(creature);
    }

    /**
     *
     * @return
     */
    public LinkedList<CreatureName> getCreatures() {
        return new LinkedList<>(this.creatures);
    }

    /**
     *
     * @return
     */
    public Optional<CreatureName> getFirstCreature() {
        if(this.creatures.isEmpty()) return Optional.empty();

        return Optional.of(this.creatures.get(0));
    }
}
