package pacman.ghost;

import pacman.game.Entity;
import pacman.game.PacmanGame;
import pacman.util.Direction;
import pacman.util.Position;

/**
 * An Abstract Ghost which is a game entity.
 *
 * @ass1
 */
public abstract class Ghost extends Entity {

    // whether the ghost is dead
    private boolean dead;
    // current phase of this ghost
    private Phase phase;
    // duration of current phase
    private int phaseDuration;

    /**
     * Creates a ghost which is alive and starts in the SCATTER phase
     * with a duration of Phase.SCATTER.duration(). This ghost also has
     * a default position of (0, 0) and a default direction of facing
     * up.
     *
     * @ass1
     */
    public Ghost() {
        super();
        dead = false;
        phase = Phase.SCATTER;
        phaseDuration = Phase.SCATTER.getDuration();
    }

    /**
     * Sets the Ghost Phase and its duration overriding any current
     * phase information.
     *
     * if Phase is null then no changes are made. If the duration is
     * less than zero then the duration is set to 0.
     *
     * @param newPhase to set the ghost to.
     * @param duration of ticks for the phase to last for.
     * @ass1
     */
    public void setPhase(Phase newPhase, int duration) {
        if (newPhase != null) {
            phase = newPhase;
            phaseDuration = Integer.max(0, duration);
        }
    }

    /**
     * Get the phase that the ghost currently is in.
     * @return the set phase.
     * @ass1
     */
    public Phase getPhase() {
        return phase;
    }

    /*
     * NextPhase decreases our phase duration and moves us to the
     * next phase if it is 0.
     *
     * - CHASE goes to SCATTER.
     * - FRIGHTENED && SCATTER go to CHASE.
     */
    private void nextPhase() {
        phaseDuration = Integer.max(0, phaseDuration - 1);
        if (phaseDuration == 0) {
            switch (getPhase()) {
                case CHASE:
                    setPhase(Phase.SCATTER, Phase.SCATTER.getDuration());
                    break;
                case FRIGHTENED:
                case SCATTER:
                    setPhase(Phase.CHASE, Phase.CHASE.getDuration());
                    break;
            }
        }
    }

    /**
     * Gets the phase info of the ghost.
     * @return the phase and duration formatted as such: "PHASE:DURATION".
     * @ass1
     */
    public String phaseInfo() {
        return String.format("%s:%d", phase, phaseDuration);
    }

    /**
     * Gets the ghosts colour.
     * @return hex version of the ghosts colour, e.g. #FFFFFF for white.
     * @ass1
     */
    public abstract String getColour();

    /**
     * Gets the ghosts type.
     * @return this ghosts type.
     * @ass1
     */
    public abstract GhostType getType();

    /**
     * Kills this ghost by setting its status to isDead.
     * @ass1
     */
    public void kill() {
        this.dead = true;
    }

    /**
     * Checks if this ghost is dead.
     * @return true if dead, false otherwise.
     * @ass1
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Resets the ghost back to an initial state where:
     *
     * <ul>
     *     <li>It is alive</li>
     *     <li>With a Phase of SCATTER with duration SCATTER.getDuration()</li>
     *     <li>Facing in the Direction.UP</li>
     *     <li>With a Position of ( 0, 0 )</li>
     * </ul>
     * @ass1
     */
    public void reset() {
        dead = false;
        this.phase = Phase.SCATTER;
        this.phaseDuration = Phase.SCATTER.getDuration();
        this.setDirection(Direction.UP);
        this.setPosition(new Position(0, 0));
    }

    /**
     * Gets the target block that we should be heading towards when in the
     * chase phase.
     *
     * @param game - to read the board from.
     * @return the ghosts target position.
     */
    public abstract Position chaseTarget(PacmanGame game);

    /**
     * Gets the home block that we should be heading towards when in the
     * scatter phase.
     *
     * @param game - to read the board from.
     * @return the ghosts home position.
     */
    public abstract Position home(PacmanGame game);

    /**
     * Move advances the ghost in a direction by one point on the board. The
     * direction this move is made is done as follows:
     * 1. Decrease the phase duration by 1, and if the duration is now zero,
     * then move to the next phase.
     * 2. Get the target position. If the phase is CHASE, then get the
     * chaseTarget. If the phase is SCATTER, then the position is the
     * ghost's home position. If the phase is FRIGHTENED, then choose a
     * target position with coordinates given by:
     * targetPositionX = (x*24 mod (2 * board width )) - board width,
     * targetPositionY = (y*36 mod (2 * board height)) - board height
     * where x and y are the current coordinates of the ghost.
     * 3. Choose the direction that the current Ghost position when moved 1
     * step has the smallest euclidean distance to the target position. The
     * board item in the move position must be pathable for it to be chosen.
     * The chosen direction cannot be opposite to the current direction. If
     * multiple directions have the same shortest distance, then choose the
     * direction in the order UP, LEFT, DOWN, RIGHT
     * 4. Set the direction of the Ghost to the chosen direction.
     * 5. Set the position of this Ghost to be one forward step in the chosen
     * direction.
     *
     * @param game - information needed to decide movement.
     */
    @Override
    public void move(PacmanGame game) {
        nextPhase();
        Position targetPosition;
        Position newPosition = getPosition();
        int x = getPosition().getX();
        int y = getPosition().getY();
        // Ghost position when moved 1 step
        Position newUp = new Position(x, y - 1);
        Position newLeft = new Position(x - 1, y);
        Position newDown = new Position(x, y + 1);
        Position newRight = new Position(x + 1, y);
        double minDistance = Double.MAX_VALUE;
        // initial direction (ghost's current direction)
        Direction newDirection = getDirection();
        // step 2 select target position (default is SCATTER)
        switch (phase) {
            case CHASE:
                targetPosition = chaseTarget(game);
                break;
            case FRIGHTENED:
                int targetX = (x * 24 % (2 * game.getBoard().getWidth())
                        - game.getBoard().getWidth());
                int targetY = (y * 36 % (2 * game.getBoard().getHeight())
                        - game.getBoard().getHeight());
                targetPosition = new Position(targetX, targetY);
                break;
            default:
                targetPosition = home(game);
        }
        // calculate euclidean distance for 4 directions (move 1 step)
        double upDistance = newUp.distance(targetPosition);
        double leftDistance = newLeft.distance(targetPosition);
        double downDistance = newDown.distance(targetPosition);
        double rightDistance = newRight.distance(targetPosition);
        // check 'out of board' situations (using outRange() method (a helper))
        boolean upOutrange = outRange(newUp, game);
        boolean leftOutrange = outRange(newLeft, game);
        boolean downOutrange = outRange(newDown, game);
        boolean rightOutrange = outRange(newRight, game);
        // adjust distance value to make it far greater than calculated value
        // if targetposition is unpathable or choose opposite direction
        // using adjustDistance() method (a helper)
        upDistance = adjustDistance(upOutrange, newUp, newDirection,
                upDistance, game, newUp, newLeft, newDown, newRight);
        leftDistance = adjustDistance(leftOutrange, newLeft, newDirection,
                leftDistance, game, newUp, newLeft, newDown, newRight);
        downDistance = adjustDistance(downOutrange, newDown, newDirection,
                downDistance, game, newUp, newLeft, newDown, newRight);
        rightDistance = adjustDistance(rightOutrange, newRight,
                newDirection, rightDistance, game, newUp, newLeft, newDown,
                newRight);
        // select the newDirection via smallest distance in order: UP, LEFT,
        // DOWN, RIGHT (using selectD() method)
        newDirection = selectD(upDistance, leftDistance, downDistance,
                rightDistance, minDistance, game);
        // select newPosition (move 1 step) (using selectP method)
        newPosition = selectP(newDirection, game, newUp, newLeft, newDown,
                newRight);
        // set new direction and new position (using setDnp() method)
        setDnp(upDistance, leftDistance, downDistance, rightDistance,
                newDirection, newPosition);
    }

    /**
     * a move method helper method to help check whether the new position is
     * out of board.
     *
     * @param newP new position
     * @param game game information
     * @return true if out of board, else false
     */
    private boolean outRange(Position newP, PacmanGame game) {
        return (newP.getX() < 0 || newP.getX() >= game.getBoard().getWidth()
                || newP.getY() < 0
                || newP.getY() >= game.getBoard().getHeight());
    }

    /**
     * a move method helper method to help adjust distance to be compared.
     *
     * @param outrange if new position is out of board
     * @param newP new position
     * @param newDirection new direction (current direction)
     * @param distance distance to new position (move 1 step decided by
     *                 direction)
     * @param game game information
     * @param newUp new position up
     * @param newLeft new position left
     * @param newDown new position down
     * @param newRight new position right
     * @return distance in specific direction that has adjusted
     */
    private double adjustDistance(boolean outrange, Position newP,
                                    Direction newDirection,
                                    double distance, PacmanGame game,
                                    Position newUp, Position newLeft,
                                    Position newDown, Position newRight) {
        double adjustedDistance = distance;
        Direction comDirection = newDirection;
        if (newP.equals(newUp)) {
            comDirection = Direction.UP;
        } else if (newP.equals(newLeft)) {
            comDirection = Direction.LEFT;
        } else if (newP.equals(newDown)) {
            comDirection = Direction.DOWN;
        } else if (newP.equals(newRight)) {
            comDirection = Direction.RIGHT;
        }
        if (outrange) {
            adjustedDistance = Double.MAX_VALUE;
        } else if (!game.getBoard().getEntry(newP).getPathable()
                || newDirection == comDirection.opposite()) {
            adjustedDistance = Double.MAX_VALUE;
        }
        return adjustedDistance;
    }

    /**
     * a move method helper method to help select the new direction.
     *
     * @param upDistance new up position distance
     * @param leftDistance new left position distance
     * @param downDistance new down position distance
     * @param rightDistance new right position distance
     * @param minDistance the initial minimum distance in the move
     * @param game game information
     * @return new direction to be selected
     */
    private Direction selectD(double upDistance, double leftDistance,
                              double downDistance, double rightDistance,
                              double minDistance, PacmanGame game) {
        Direction newDirection = game.getHunter().getDirection();
        double comDistance = minDistance;
        if (upDistance < minDistance) {
            comDistance = upDistance;
            newDirection = Direction.UP;
        }
        if (leftDistance < comDistance) {
            comDistance = leftDistance;
            newDirection = Direction.LEFT;
        }
        if (downDistance < comDistance) {
            comDistance = downDistance;
            newDirection = Direction.DOWN;
        }
        if (rightDistance < comDistance) {
            comDistance = rightDistance;
            newDirection = Direction.RIGHT;
        }
        return newDirection;
    }

    /**
     * a move method helper method to help select new position by new direction.
     *
     * @param newDirection new direction to be selected
     * @param game game information
     * @param newUp new position forwards UP
     * @param newLeft new position forwards LEFT
     * @param newDown new position forwards DOWN
     * @param newRight new position forwards RIGHT
     * @return new position to be selected
     */
    private Position selectP(Direction newDirection, PacmanGame game,
                             Position newUp, Position newLeft,
                             Position newDown, Position newRight) {
        Position newPosition = game.getHunter().getPosition();
        if (newDirection == Direction.UP) {
            newPosition = newUp;
        } else if (newDirection == Direction.LEFT) {
            newPosition = newLeft;
        } else if (newDirection == Direction.DOWN) {
            newPosition = newDown;
        } else if (newDirection == Direction.RIGHT) {
            newPosition = newRight;
        }
        return newPosition;
    }

    /**
     * a move method helper method. if get stuck, then keep current position
     * and current direction; else set new position and new direction.
     *
     * @param upDistance distance to up
     * @param leftDistance distance to left
     * @param downDistance distance to down
     * @param rightDistance distance to right
     * @param newDirection new direction to be set
     * @param newPosition new position to be set
     */
    private void setDnp(double upDistance, double leftDistance,
                          double downDistance, double rightDistance,
                          Direction newDirection, Position newPosition) {
        if (upDistance == Double.MAX_VALUE && leftDistance == Double.MAX_VALUE
                && downDistance == Double.MAX_VALUE
                && rightDistance == Double.MAX_VALUE) {
            setDirection(getDirection());
            setPosition(getPosition());
        } else {
            setPosition(newPosition);
            setDirection(newDirection);
        }
    }

    /**
     * Checks if another object instance is equal to this Ghost. Ghosts are
     * equal if they have the same alive/dead status, phase duration ,
     * current phase, direction and position.
     *
     * @param o other Object
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ghost)) {
            return false;
        }
        Ghost other = (Ghost) o;
        return this.dead == other.dead
                && this.phaseDuration == other.phaseDuration
                && this.phase == other.phase
                && this.getPosition().equals(other.getPosition())
                && this.getDirection().equals(other.getDirection());
    }

    /**
     * For two objects that are equal the hash should also be equal. For two
     * objects that are not equal the hash does not have to be different.
     *
     * @return hash of PacmanBoard.
     */
    @Override
    public int hashCode() {
        return this.getPosition().hashCode() + this.getDirection().hashCode()
                + this.phaseDuration;
    }

    /**
     * Represents this Ghost in a comma-seperated string format. Format is:
     * "x,y,DIRECTION,PHASE:phaseDuration". DIRECTION is the uppercase enum
     * type value for Direction. PHASE is the uppercase enum type value for
     * Phase. Example: "4,5,LEFT,FRIGHTENED:15".
     *
     * @return "x,y,DIRECTION,PHASE:phaseDuration"
     */
    @Override
    public String toString() {
        return getPosition().getX() + "," + getPosition().getY() + ","
                + getDirection().toString() + "," + getPhase().toString()
                + ":" + phaseDuration;
    }
}
