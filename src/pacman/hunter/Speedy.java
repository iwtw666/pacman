package pacman.hunter;


import pacman.game.PacmanGame;

/**
 * A Speedy hunter that has a special ability that allows the hunter
 * to travel twice as fast.
 *
 * @ass1
 */
public class Speedy extends Hunter {

    /**
     * Creates a Speedy Hunter with its special ability.
     *
     * see {@link Hunter#Hunter()}
     * @ass1
     */
    public Speedy() {
        super();
    }

    /**
     * Creates a Speedy Hunter by copying the internal state of
     * another hunter.
     *
     * see {@link pacman.hunter.Hunter#Hunter(Hunter)}
     *
     * @param original hunter to copy from
     * @ass1
     */
    public Speedy(Hunter original) {
        super(original);
    }

    /**
     * If Speedy's special is active then we move twice instead of once.
     * While moving we still do all the normal steps that Hunter.move
     * (PacmanGame) does.
     * This will result in Speedy decreasing its special duration by two.
     *
     * @param game - that stores the internal state.
     */
    @Override
    public void move(PacmanGame game) {
        if (isSpecialActive()) {
            super.move(game);
        }
        super.move(game);
    }

    /**
     * Represents this Speedy in a comma-seperated string format. Format is:
     * "x,y,DIRECTION,specialDuration,SPEEDY". DIRECTION is the uppercase
     * enum type value. Example: "4,5,LEFT,12,SPEEDY"。
     *
     * @return "x,y,DIRECTION,specialDuration,SPEEDY"
     */
    @Override
    public String toString() {
        return getPosition().getX() + "," + getPosition().getY() + ","
                + getDirection().toString() + ","
                + this.getSpecialDurationRemaining() + ",SPEEDY";
    }
}
