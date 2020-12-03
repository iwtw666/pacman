package pacman.game;


import pacman.ghost.Ghost;
import pacman.ghost.GhostType;

import java.io.*;


/**
 * Writes the PacmanGame to a standard format.
 */
public class GameWriter {

    /**
     * Saves a PacmanGame to a writer using the following rules:
     *
     * The first line of the file will be the Board block header: "[Board]".
     * Following this on the line below will be the width and height comma
     * separated with no leading zeros and no spaces. After this on the next
     * line is the Game Board which is to be the toString representation of
     * the board.
     *
     * One blank line.
     *
     * On the next line is the "[Game]" block which will output the
     * following assignments in order ( title, author, lives, level, score,
     * hunter, blinky, inky, pinky, clyde ). The assignments are to have a
     * single space before and after the equals sign. The assignments for (
     * hunter, blinky, inky, pinky, clyde) are to be the toString
     * representation of these entities. Each assignment is to be on its own
     * line.
     *
     * One blank line.
     *
     * The last block is the "[Scores]" block which should be output as a
     * multiline list of the scores where the name and value of the score
     * are sperated by a ":". The scores should be output sorted by Name
     * ScoreBoard.getEntriesByName(). The last score should not have a newline.
     *
     * @param writer - to output the data to.
     * @param game - to encode into the save data format.
     * @throws IOException - during an issue with saving to the file.
     */
    public static void write(Writer writer, PacmanGame game)
            throws IOException {
        // choosing bufferedwriter
        String sepa = System.lineSeparator();
        BufferedWriter out = new BufferedWriter(writer);
        out.write("[Board]" + sepa);
        out.write(game.getBoard().getWidth() + ","
                + game.getBoard().getHeight() + sepa);
        out.write(game.getBoard().toString() + sepa);
        out.newLine();
        // game information
        out.write("[Game]" + sepa);
        out.write("title = " + game.getTitle() + sepa);
        out.write("author = " + game.getAuthor() + sepa);
        out.write("lives = " + game.getLives() + sepa);
        out.write("level = " + game.getLevel() + sepa);
        out.write("score = " + game.getScores().getScore() + sepa);
        out.write("hunter = " + game.getHunter().toString() + sepa);
        // write ghosts
        writeGhost(out, game, sepa);
        out.newLine();
        // scores information
        int scoresSize = game.getScores().getEntriesByName().size();
        out.write("[Scores]" + sepa);
        if (scoresSize >= 1) {
            for (int i = 0; i < scoresSize - 1; i++) {
                out.write(game.getScores().getEntriesByName()
                        .get(i) + sepa);
            }
            out.write(game.getScores().getEntriesByName()
                    .get(scoresSize - 1));
        }
        out.flush();
        out.close();
    }

    /**
     * helper method to help write ghosts.
     *
     * @param out - to output the data to.
     * @param game - to encode into the save data format.
     * @param sepa System.lineSeparator().
     * @throws IOException - during an issue with saving to the file.
     */
    private static void writeGhost(Writer out, PacmanGame game, String sepa)
            throws IOException {
        for (Ghost g : game.getGhosts()) {
            if (g.getType() == GhostType.BLINKY) {
                out.write("blinky = " + g.toString()
                        + sepa);
            }
        }
        for (Ghost g : game.getGhosts()) {
            if (g.getType() == GhostType.INKY) {
                out.write("inky = " + g.toString()
                        + sepa);
            }
        }
        for (Ghost g : game.getGhosts()) {
            if (g.getType() == GhostType.PINKY) {
                out.write("pinky = " + g.toString()
                        + sepa);
            }
        }
        for (Ghost g : game.getGhosts()) {
            if (g.getType() == GhostType.CLYDE) {
                out.write("clyde = " + g.toString()
                        + sepa);
            }
        }
    }
}
