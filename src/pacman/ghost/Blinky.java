package pacman.ghost;

import pacman.game.PacmanGame;
import pacman.util.Position;

/**
 * Blinky is a ghost that behaves in a very aggressive manner
 * towards a hunter. When not chasing the hunter down, Blinky
 * likes to hang out in the top right corner of the board in a
 * red glow.
 *
 * @ass1
 */
public class Blinky extends Ghost {

    /**
     * Get Blinkys colour.
     * @return "#d54e53"
     * @ass1
     */
    @Override
    public String getColour() {
        return "#d54e53";
    }

    /**
     * Get Blinkys type/name.
     * @return BLINKY;
     * @ass1
     */
    @Override
    public GhostType getType() {
        return GhostType.BLINKY;
    }

    /**
     * Blinky targets the hunters position.
     *
     * @param game - to read the board from.
     * @return hunter position
     */
    @Override
    public Position chaseTarget(PacmanGame game) {
        return game.getHunter().getPosition();
    }

    /**
     * Blinky's home position is one block outside of the top right of the
     * game board. The top left position of the board is (0, 0).
     *
     * @param game - to read the board from.
     * @return One diagonal block out from the top right corner.
     */
    @Override
    public Position home(PacmanGame game) {
        return new Position(game.getBoard().getWidth(), -1);
    }
}
