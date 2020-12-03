package pacman.game;

import pacman.board.BoardItem;
import pacman.board.PacmanBoard;
import pacman.ghost.Ghost;
import pacman.ghost.GhostType;
import pacman.ghost.Phase;
import pacman.hunter.*;
import pacman.util.Direction;
import pacman.util.Position;
import pacman.util.UnpackableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * GameReader Reads in a saved games state and returns a game instance.
 */
public class GameReader {

    /**
     * Reads in a game according to the the following specification:
     * A Game File has 3 blocks ( Board, Game, Scores ) which must be in
     * order of Board first, Game second and Scores last. Each block is
     * defined by its name enclosed in square brackets.
     * e.g: [Board], [Game], [Scores]
     * There must be no empty lines before the first block and in between
     * blocks there must be a single blank line.
     *  <h3>[Board]</h3>
     *  First line in the block is a comma separated getWidth and getHeight.
     *  There must be (getHeight) lines of (getWidth) following the first line.
     *  Each character in these lines ( after stripping ) must be of
     *  the BoardItem keys.
     *  <h3>[Game]</h3>
     *  Contains newline separated list of assignments in any order which are
     *  unique. An assignment is a 'Key = Value' where the Key and Value
     *  should have all whitespace removed before reading.
     *  <table border="1">
     *  <caption>Assignment Definitions</caption>
     *  <tr>
     *      <td> Name </td>
     *      <td> Value format </td>
     *  </tr>
     *  <tr><td>title</td><td>string</td></tr>
     *  <tr><td>author</td><td>string</td></tr>
     *  <tr><td>lives</td><td>Integer greater than or equal to zero</td></tr>
     *  <tr><td>level</td><td>Integer greater than or equal to zero</td></tr>
     *  <tr><td>score</td><td>Integer greater than or equal to zero</td></tr>
     *  <tr><td>hunter</td><td>A comma separated list of attributes in the
     *  following order: <br>x, y, DIRECTION, special duration, HunterType<br>
     *  where x and y are integers, DIRECTION is the string representation
     *  of a DIRECTION and the special duration is a integer greater than
     *  or equal to zero.</td></tr>
     *  <tr><td>blinky|inky|pinky|clyde</td><td>A comma separated list of
     *  attributes in the following order:
     *  <br>x,y,DIRECTION,PHASE:PhaseDuration<br>
     *  where x and y are Integers, DIRECTION is the string representation of
     *  a DIRECTION and PHASE is a string representation of the PhaseType
     *  with a duration that is an Integer greater than or equal to
     *  zero</td></tr>
     *  </table>
     *  <h3>[Scores]</h3>
     *  A newline seperated list of unique scores in the form of:
     *  'Name : Value' where name is a string and value is an Integer.
     *
     * @param reader - to read the save game from.
     * @return a PacmanGame that reflects the state from the reader.
     * @throws UnpackableException - when the saved data is invalid.
     * @throws IOException - when unable to read from the reader.
     */
    public static PacmanGame read(Reader reader)
            throws UnpackableException, IOException {
        BufferedReader in = new BufferedReader(reader);
        List<String> fileInfo = new ArrayList<String>();
        String line = null;
        int width = 1;
        int height = 1;
        List<String> boarditemsL = null;
        String title = null;
        String author = null;
        int lives = 0;
        int level = 0;
        int score = 0;
        String hunterStr = null;
        String blinkyStr = null;
        String inkyStr = null;
        String pinkyStr = null;
        String clydeStr = null;
        List<String> scoresL = null;
        // read all lines in a list without leading and trailing white space
        while ((line = in.readLine()) != null) {
            fileInfo.add(line.trim());
        }
        in.close();
        try {
            check0(fileInfo);
        } catch (Exception ea) {
            throw new UnpackableException();
        }
        try {
            // remove blank lines and comments
            Iterator<String> fileIterator = fileInfo.iterator();
            while (fileIterator.hasNext()) {
                String element = fileIterator.next();
                if ("".equals(element) || ";".equals(element.substring(0, 1))) {
                    fileIterator.remove();
                }
            } try {
                check1(fileInfo);
            } catch (Exception eb) {
                throw new UnpackableException();
            }
            // get all required information
            for (int i = 0; i < fileInfo.size(); i++) {
                if ("[Board]".equals(fileInfo.get(i))) {
                    width = Integer.parseInt(fileInfo.get(i + 1)
                            .split(",")[0]);
                    height = Integer.parseInt(fileInfo.get(i + 1)
                            .split(",")[1]);
                    boarditemsL = fileInfo.subList(i + 2, i + 2 + height);
                } else if ("[Game]".equals(fileInfo.get(i))) {
                    title = fileInfo.get(i + 1).split(" = ")[1];
                    author = fileInfo.get(i + 2).split(" = ")[1];
                    lives = Integer.parseInt(fileInfo.get(i + 3)
                            .split(" = ")[1]);
                    level = Integer.parseInt(fileInfo.get(i + 4)
                            .split(" = ")[1]);
                    score = Integer.parseInt(fileInfo.get(i + 5)
                            .split(" = ")[1]);
                    hunterStr = fileInfo.get(i + 6).split(" = ")[1];
                    blinkyStr = fileInfo.get(i + 7).split(" = ")[1];
                    inkyStr = fileInfo.get(i + 8).split(" = ")[1];
                    pinkyStr = fileInfo.get(i + 9).split(" = ")[1];
                    clydeStr = fileInfo.get(i + 10).split(" = ")[1];
                } else if ("[Scores]".equals(fileInfo.get(i))) {
                    scoresL = fileInfo.subList(i + 1, fileInfo.size());
                }
            }
            // set BoardItems (using setBoarditems() method)
            PacmanBoard pacBoard = new PacmanBoard(width, height);
            setBoarditems(boarditemsL, width, height, pacBoard);
            // get hunter (using setHunter() method)
            Hunter hunter = setHunter(hunterStr);
            // set all game information
            PacmanGame game = new PacmanGame(title, author, hunter, pacBoard);
            game.setLives(lives);
            game.setLevel(level);
            game.getScores().increaseScore(score);
            if (scoresL.size() >= 1) {
                for (String s : scoresL) {
                    game.getScores().setScore(s.split(" : ")[0],
                            Integer.parseInt(s.split(" : ")[1]));
                }
            }
            // set ghosts (using setGhost() method)
            setGhost(blinkyStr, inkyStr, pinkyStr, clydeStr, game);
            return game;
        } catch (Exception e) {
            throw new UnpackableException();
        }
    }

    /**
     * helper method to set BoardItems.
     *
     * @param boarditemsL all boarditems information from file
     * @param width game board width
     * @param height game board height
     * @param pacBoard new Pacmanboard to be copied
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static void setBoarditems(List<String> boarditemsL, int width,
                                      int height, PacmanBoard pacBoard)
            throws UnpackableException {
        BoardItem boardItem = null;
        List<BoardItem> boardItems = new ArrayList<>();
        for (String s : boarditemsL) {
            char[] board = s.toCharArray();
            for (char c : board) {
                if (c == (char) 88) {
                    boardItem = BoardItem.WALL;
                } else if (c == (char) 49) {
                    boardItem = BoardItem.DOT;
                } else if (c == (char) 48) {
                    boardItem = BoardItem.NONE;
                } else if (c == (char) 66) {
                    boardItem = BoardItem.BIG_DOT;
                } else if (c == (char) 36) {
                    boardItem = BoardItem.GHOST_SPAWN;
                } else if (c == (char) 80) {
                    boardItem = BoardItem.PACMAN_SPAWN;
                } else if (c == (char) 98) {
                    boardItem = BoardItem.BIG_DOT_SPAWN;
                } else {
                    throw new UnpackableException();
                }
                boardItems.add(boardItem);
            }
        }
        int n = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Position p = new Position(i, j);
                pacBoard.setEntry(p, boardItems.get(n));
                n++;
            }
        }
    }

    /**
     * helper method to set Hunter.
     *
     * @param hunterStr hunter information from file
     * @return hunter to be copied in the game
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static Hunter setHunter(String hunterStr)
            throws UnpackableException {
        int hunterX = Integer.parseInt(hunterStr.split(",")[0]);
        int hunterY = Integer.parseInt(hunterStr.split(",")[1]);
        Direction hunterD = Direction.valueOf(hunterStr.split(",")[2]);
        int hunterS = Integer.parseInt(hunterStr.split(",")[3]);
        HunterType hunterT = HunterType.valueOf(hunterStr.split(",")[4]);
        Hunter hunter = null;
        if (hunterT == HunterType.PHIL) {
            hunter = new Phil();
        } else if (hunterT == HunterType.HUNGRY) {
            hunter = new Hungry();
        } else if (hunterT == HunterType.PHASEY) {
            hunter = new Phasey();
        } else if (hunterT == HunterType.SPEEDY) {
            hunter = new Speedy();
        } else {
            throw new UnpackableException();
        }
        hunter.setPosition(new Position(hunterX, hunterY));
        hunter.setDirection(hunterD);
        hunter.activateSpecial(hunterS);
        return hunter;
    }

    /**
     * helper method to set Ghosts.
     *
     * @param blinkyStr blinky information from file
     * @param inkyStr inky information from file
     * @param pinkyStr pinky inforamtion from file
     * @param clydeStr clyde information from file
     * @param game game need to be copied
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static void setGhost(String blinkyStr, String inkyStr,
                                 String pinkyStr, String clydeStr,
                                 PacmanGame game) throws UnpackableException {
        int blinkX = Integer.parseInt(blinkyStr.split(",")[0]);
        int blinkY = Integer.parseInt(blinkyStr.split(",")[1]);
        Direction blinkyD = Direction.valueOf(blinkyStr.split(",")[2]);
        Phase blinkyP = Phase.valueOf(blinkyStr.split(",")[3]
                .split(":")[0]);
        int blinkyS = Integer.parseInt(blinkyStr.split(",")[3]
                .split(":")[1]);

        int inkyX = Integer.parseInt(inkyStr.split(",")[0]);
        int inkyY = Integer.parseInt(inkyStr.split(",")[1]);
        Direction inkyD = Direction.valueOf(inkyStr.split(",")[2]);
        Phase inkyP = Phase.valueOf(inkyStr.split(",")[3]
                .split(":")[0]);
        int inkyS = Integer.parseInt(inkyStr.split(",")[3]
                .split(":")[1]);

        int pinkyX = Integer.parseInt(pinkyStr.split(",")[0]);
        int pinkyY = Integer.parseInt(pinkyStr.split(",")[1]);
        Direction pinkyD = Direction.valueOf(pinkyStr.split(",")[2]);
        Phase pinkyP = Phase.valueOf(pinkyStr.split(",")[3]
                .split(":")[0]);
        int pinkyS = Integer.parseInt(pinkyStr.split(",")[3]
                .split(":")[1]);

        int clydeX = Integer.parseInt(clydeStr.split(",")[0]);
        int clydeY = Integer.parseInt(clydeStr.split(",")[1]);
        Direction clydeD = Direction.valueOf(clydeStr.split(",")[2]);
        Phase clydeP = Phase.valueOf(clydeStr.split(",")[3]
                .split(":")[0]);
        int clydeS = Integer.parseInt(clydeStr.split(",")[3]
                .split(":")[1]);

        for (Ghost g : game.getGhosts()) {
            if (g.getType() == GhostType.BLINKY) {
                g.setPosition(new Position(blinkX, blinkY));
                g.setDirection(blinkyD);
                g.setPhase(blinkyP, blinkyS);
            } else if (g.getType() == GhostType.INKY) {
                g.setPosition(new Position(inkyX, inkyY));
                g.setDirection(inkyD);
                g.setPhase(inkyP, inkyS);
            } else if (g.getType() == GhostType.PINKY) {
                g.setPosition(new Position(pinkyX, pinkyY));
                g.setDirection(pinkyD);
                g.setPhase(pinkyP, pinkyS);
            } else if (g.getType() == GhostType.CLYDE) {
                g.setPosition(new Position(clydeX, clydeY));
                g.setDirection(clydeD);
                g.setPhase(clydeP, clydeS);
            } else {
                throw new UnpackableException();
            }
        }
    }

    /**
     * check values(non-negative, or width and height greater than 0).
     *
     * @param fileInfo File to a list
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static void check1(List<String> fileInfo)
            throws UnpackableException {
        int width = 1;
        int height = 1;
        for (int i = 0; i < fileInfo.size(); i++) {
            if ("[Board]".equals(fileInfo.get(i))) {
                width = Integer.parseInt(fileInfo.get(i + 1)
                        .split(",")[0]);
                height = Integer.parseInt(fileInfo.get(i + 1)
                        .split(",")[1]);
                if (width <= 0 || height <= 0) {
                    throw new UnpackableException();
                }
            } else  if ("[Game]".equals(fileInfo.get(i))) {
                int lives = Integer.parseInt(fileInfo.get(i + 3)
                        .split(" = ")[1]);
                int level = Integer.parseInt(fileInfo.get(i + 4)
                        .split(" = ")[1]);
                if (lives < 0 || level < 0) {
                    throw new UnpackableException();
                }
                try {
                    check2(fileInfo, i, width, height);
                } catch (Exception ea) {
                    throw new UnpackableException();
                }
            } else if ("[Scores]".equals(fileInfo.get(i))) {
                List<String> scoresL = fileInfo.subList(i + 1, fileInfo.size());
                if (scoresL.size() > 0) {
                    for (String s : scoresL) {
                        if (s.equals("")) {
                            throw new UnpackableException();
                        }
                        try {
                            Integer.parseInt(s.split(" : ")[1]);
                        } catch (Exception ee) {
                            throw new UnpackableException();
                        }
                    }
                }
            }
        }
    }

    /**
     * check all entities positions not out of board.
     *
     * @param fileInfo file to a list
     * @param i index in check1
     * @param width width of board
     * @param height heght of board
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static void check2(List<String> fileInfo, int i, int width,
                               int height) throws UnpackableException {
        int hunterX;
        int hunterY;
        int ghostBX;
        int ghostBY;
        int ghostIX;
        int ghostIY;
        int ghostPX;
        int ghostPY;
        int ghostCX;
        int ghostCY;
        hunterX = Integer.parseInt(fileInfo.get(i + 6).split(" = ")[1]
                .split(",")[0]);
        hunterY = Integer.parseInt(fileInfo.get(i + 6).split(" = ")[1]
                .split(",")[1]);
        ghostBX = Integer.parseInt(fileInfo.get(i + 7).split(" = ")[1]
                .split(",")[0]);
        ghostBY = Integer.parseInt(fileInfo.get(i + 7).split(" = ")[1]
                .split(",")[1]);
        ghostIX = Integer.parseInt(fileInfo.get(i + 8).split(" = ")[1]
                .split(",")[0]);
        ghostIY = Integer.parseInt(fileInfo.get(i + 8).split(" = ")[1]
                .split(",")[1]);
        ghostPX = Integer.parseInt(fileInfo.get(i + 9).split(" = ")[1]
                .split(",")[0]);
        ghostPY = Integer.parseInt(fileInfo.get(i + 9).split(" = ")[1]
                .split(",")[1]);
        ghostCX = Integer.parseInt(fileInfo.get(i + 10).split(" = ")[1]
                .split(",")[0]);
        ghostCY = Integer.parseInt(fileInfo.get(i + 10).split(" = ")[1]
                .split(",")[1]);
        if (hunterX < 0 || ghostBX < 0 || ghostCX < 0 || ghostIX < 0
                || ghostPX < 0 || hunterY < 0 || ghostBY < 0 || ghostCY < 0
                || ghostIY < 0 || ghostPY < 0 || hunterX >= width
                || ghostBX >= width || ghostCX >= width || ghostIX >= width
                || ghostPX >= width || hunterY >= height || ghostBY >= height
                || ghostCY >= height || ghostIY >= height
                || ghostPY >= height) {
            throw new UnpackableException();
        }
    }

    /**
     * check blank lines.
     *
     * @param fileInfo file to list
     * @throws UnpackableException - when the saved data is invalid.
     */
    private static void check0(List<String> fileInfo)
            throws UnpackableException {
        int start = 0;
        int middle = 0;
        for (int i = 0; i < fileInfo.size(); i++) {
            if ("[Board]".equals(fileInfo.get(i))) {
                if (fileInfo.get(i - 1).equals("")) {
                    throw new UnpackableException();
                }
                start = i;
            } else  if ("[Game]".equals(fileInfo.get(i))) {
                List<String> midList = fileInfo.subList(start, i);
                if (Collections.frequency(midList, "") != 1) {
                    throw new UnpackableException();
                }
                middle = i;
            } else if ("[Scores]".equals(fileInfo.get(i))) {
                List<String> endList = fileInfo.subList(middle, i);
                if (Collections.frequency(endList, "") != 1) {
                    throw new UnpackableException();
                }
            }
        }
    }
}
