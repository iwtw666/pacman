package pacman.game;

import pacman.board.PacmanBoard;
import pacman.ghost.*;
import pacman.hunter.Hunter;
import pacman.score.ScoreBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * PacmanGame stores the game's state and acts as the model for the entire game.
 */
public class PacmanGame {

    // game board title
    private String title;
    // game board author
    private String author;
    // current game's hunter
    private Hunter hunter;
    // copied board for this game
    private PacmanBoard board;
    // ticks of the game
    private int tick;
    // levels of the game
    private int level;
    // lives of player
    private int lives;
    // score board of the game
    private ScoreBoard scoreBoard;
    // Blinky ghost
    private Ghost ghostB = new Blinky();
    // Clyde ghost
    private Ghost ghostC = new Clyde();
    // Inky ghost
    private Ghost ghostI = new Inky();
    // Pinky ghost
    private Ghost ghostP = new Pinky();

    /**
     * Creates a new game with the given parameters and spawns one of each
     * type of ghost (Blinky, Clyde, Inky, Pinky). The ghosts should be
     * spawned at the ghost spawn point.
     * The game should start with:
     *
     * a tick of 0.
     * a level of 0.
     * a set of 4 lives.
     * a empty scoreboard with a initial score of 0.
     *
     * @param title - of the game board.
     * @param author - of the game board.
     * @param hunter - for the current game.
     * @param board - to be copied for this game.
     * @require title != null and author != null and hunter != null
     * and board != null and board contains a spawn point for Ghosts and for
     * the hunter.
     */
    public PacmanGame(String title, String author, Hunter hunter,
                      PacmanBoard board) {
        this.title = title;
        this.author = author;
        this.hunter = hunter;
        this.board = board;
        // tick, level, lives, initial scoreboard
        this.tick = 0;
        this.level = 0;
        this.lives = 4;
        this.scoreBoard = new ScoreBoard();
        // set all ghosts at ghost spawn point.
        ghostB.setPosition(board.getGhostSpawn());
        ghostC.setPosition(board.getGhostSpawn());
        ghostI.setPosition(board.getGhostSpawn());
        ghostP.setPosition(board.getGhostSpawn());
    }

    /**
     * Get the title of the map.
     *
     * @return title of the map
     * @ensure result != null
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the author of the map.
     *
     * @return author of the map
     * @ensure result != null
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Gets the current pacman board.
     *
     * @return a mutable reference to the board
     */
    public PacmanBoard getBoard() {
        return this.board;
    }

    /**
     * Gets the number of times that tick has been called in the current
     * game. See tick
     *
     * @return the current game tick value
     */
    public int getTick() {
        return this.tick;
    }

    /**
     * Gets the current score board.
     *
     * @return a mutable reference to the score board
     */
    public ScoreBoard getScores() {
        return this.scoreBoard;
    }

    /**
     * Get the level of game.
     *
     * @return current level of the game
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the level of the game.
     *
     * @param level - to be set to.
     * @ensure newLevel = max(0, givenLevel)
     */
    public void setLevel(int level) {
        this.level = Integer.max(0, level);
    }

    /**
     * Get the lives of player.
     *
     * @return amount of lives the player currently has
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Sets the lives of the current player.
     *
     * @param lives - to be set to
     */
    public void setLives(int lives) {
        this.lives = Integer.max(0, lives);
    }

    /**
     * Get the hunter of the game.
     *
     * @return a mutable reference to the hunter.
     */
    public Hunter getHunter() {
        return this.hunter;
    }

    /**
     * Get all ghosts in game.
     * Note: Adding, removing elements to this list should not affect the
     * internal copy.
     *
     * @return a list of ghosts in the game.
     */
    public List<Ghost> getGhosts() {
        List<Ghost> list = new ArrayList<>();
        list.add(0, ghostB);
        list.add(1, ghostC);
        list.add(2, ghostI);
        list.add(3, ghostP);
        return list;
    }

    /**
     * Tick If we do not have any lives (getLives() == 0) then do nothing.
     * Otherwise we do the following in this order:
     * 1. The Hunter moves Hunter.move(PacmanGame).
     * 2. For each ghost in the game, call Hunter.hit(Ghost)
     * 3. The Ghosts that are alive move on even ticks Ghost.move(PacmanGame)
     * getTick().
     * 4. For each Ghost in the game, call Hunter.hit(Ghost) on the game's
     * hunter.
     * 5. For each ghost which is dead:
     * Reset the ghost. Set the ghost's position to the ghost spawn position
     * on the current board. Add 200 points.
     * 6. If the hunter is dead, then decrease the lives and reset all the
     * entities and place them at their spawn points.
     * 7. If the board is empty, then increase the level and set the ticks to 0
     * and reset the board and entities placing them at their spawn points.
     * 8. Increase the tick value.
     */
    public void tick() {
        if (lives != 0) {
            // step 1
            hunter.move(this);
            // step 2
            for (Ghost g : getGhosts()) {
                hunter.hit(g);
            }
            // step 3
            // tick is even and ghost is alive
            if (this.getTick() % 2 == 0) {
                for (Ghost g : getGhosts()) {
                    if (!g.isDead()) {
                        g.move(this);
                    }
                }
            }
            // step 4
            for (Ghost g : getGhosts()) {
                hunter.hit(g);
            }
            // death situations
            // step 5 ghost is dead
            for (Ghost g : getGhosts()) {
                if (g.isDead()) {
                    g.reset();
                    g.setPosition(board.getGhostSpawn());
                    scoreBoard.increaseScore(200);
                }
            }
            // step 6 hunter death
            if (hunter.isDead()) {
                lives -= 1;
                hunter.reset();
                hunter.setPosition(board.getPacmanSpawn());
                for (Ghost g : getGhosts()) {
                    g.reset();
                    g.setPosition(board.getGhostSpawn());
                }
            }
            // step 7 empty case
            if (board.isEmpty()) {
                level += 1;
                tick = 0;
                board.reset();
                hunter.reset();
                hunter.setPosition(board.getPacmanSpawn());
                for (Ghost g : getGhosts()) {
                    g.reset();
                    g.setPosition(board.getGhostSpawn());
                }
            } else {
                // step 8
                tick += 1;
            }
        }
    }

    /**
     * Resets the Game in the following way:
     * Lives is set to the default of 4.
     * Level is set to 0.
     * ScoreBoard is reset ScoreBoard.reset()
     * PacmanBoard is reset PacmanBoard.reset()
     * All entities are reset
     * All entity positions are set to their spawn locations.
     * The tick value is reset to zero.
     */
    public void reset() {
        lives = 4;
        level = 0;
        board.reset();
        scoreBoard.reset();
        // reset all entities
        hunter.reset();
        hunter.setPosition(board.getPacmanSpawn());
        for (Ghost g : getGhosts()) {
            g.reset();
            g.setPosition(board.getGhostSpawn());
        }
        tick = 0;
    }

    /**
     * For each ghost in the game, set its phase to be Phase.FRIGHTENED with
     * a duration of Phase.FRIGHTENED.getDuration().
     */
    public void setGhostsFrightened() {
        for (Ghost g : getGhosts()) {
            g.setPhase(Phase.FRIGHTENED, Phase.FRIGHTENED.getDuration());
        }
    }
}
