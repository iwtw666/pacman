package pacman.game;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pacman.board.BoardItem;
import pacman.board.PacmanBoard;
import pacman.ghost.Ghost;
import pacman.ghost.GhostType;
import pacman.util.Position;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/**
* GameReader Tester. 
* 
* @author <Will_the_Wizard>
* @since <pre>24, Sep, 2019</pre>
* @version 2.5 - solved 90% problems (not handle IOE)
*/ 
public class GameReaderTest {

/**
* output a default test file.
*
* @throws Exception the exceptions may occur
*/
@Before
public void defaultMap() throws Exception {
    FileWriter fileout = new FileWriter("./test/default.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Default CSSE2002 Pacman Map" + sepa);
    write.write("; board Keys" + sepa);
    write.write("; getWidth, getHeight" + sepa);
    write.write(";   - X Wall" + sepa);
    write.write(";   - 0 No Item, 1 dots" +sepa);
    write.write(";   - B Big dots spawn ( occupied ), b big dot spawn " +
            "( not " + "occupied )" + sepa);
    write.write(";   - $ Ghost spawn zone" + sepa);
    write.write(";   - P pacman spawn zone" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("; Must have one blank line between each block." + sepa);
    write.write("; Comments do not count as a line" + sepa);
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.write("B : 5" + sepa);
    write.write("C : 100");
    write.flush();
    write.close();
}

/**
* output a bad test file (Board dimensions must be greater than zero).
*
* @throws Exception the exceptions may occur
*/
@Before
public void badMap1() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad1.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
                "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,-9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file ("5" is not a valid BoardItem key).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap2() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad2.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000005555000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file (Row of board is too short).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap3() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad3.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file (Missing blank line between blocks).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap4() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad4.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("; comment" + sepa);
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file (Lives must be a non-negative integer).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap5() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad5.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = -5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file (position is outside the board).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap6() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad6.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 25,9,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file (phase duration is missing).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap7() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad7.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED" + sepa);
    write.write("inky = 1,6,UP,SCATTER:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file ("LONELY" is not a valid PhaseType).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap8() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad8.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,LONELY:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}

/**
 * output a bad test file ("title" assignment is missing).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap9() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad9.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,CHASE:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : 0" + sepa);
    write.flush();
    write.close();
}
/**
 * output a bad test file (Score values must be an integer).
 *
 * @throws Exception the exceptions may occur
 */
@Before
public void badMap10() throws Exception {
    FileWriter fileout = new FileWriter("./test/bad10.map");
    BufferedWriter write = new BufferedWriter(fileout);
    String sepa = System.lineSeparator();
    write.write("; Invalid CSSE2002 Pacman Map" + sepa);
    write.write("; explain why the line above is invalid." + sepa);
    write.write("; Invalid lines should result in " +
            "an UnpackableException" + sepa);
    write.write("[Board]" + sepa);
    write.write("25,9" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.write("X10000000000000000000000X" + sepa);
    write.write("X00000B00000X000000B0000X" + sepa);
    write.write("X00000000000X00000000000X" + sepa);
    write.write("X0000000XXXXXXXXX0000000X" + sepa);
    write.write("X00000000000X00000000000X"+ sepa);
    write.write("X00000B000000000000B0000X" + sepa);
    write.write("XP0000000000X0000000000$X" + sepa);
    write.write("XXXXXXXXXXXXXXXXXXXXXXXXX" + sepa);
    write.newLine();
    write.write("[Game]" + sepa);
    write.write("title = Default CSSE2002 PacMan Map" + sepa);
    write.write("author = Evan Hughes" + sepa);
    write.write("lives = 5" + sepa);
    write.write("level = 2" + sepa);
    write.write("score = 123" + sepa);
    write.write("hunter = 1,1,LEFT,20,PHIL" + sepa);
    write.write("blinky = 3,6,UP,FRIGHTENED:15" + sepa);
    write.write("inky = 1,6,UP,LONELY:7" + sepa);
    write.write("pinky = 8,6,UP,FRIGHTENED:15" + sepa);
    write.write("clyde = 6,4,UP,CHASE:4" + sepa);
    write.newLine();
    write.write("[Scores]" + sepa);
    write.write("A : hello" + sepa);
    write.flush();
    write.close();
}

/**
* delete test file.
*
* @throws Exception might occur when delete the file
*/
@After
public void after() throws Exception {
    File testFile = new File("./test/default.map");
    File badFile1 = new File("./test/bad1.map");
    File badFile2 = new File("./test/bad2.map");
    File badFile3 = new File("./test/bad3.map");
    File badFile4 = new File("./test/bad4.map");
    File badFile5 = new File("./test/bad5.map");
    File badFile6 = new File("./test/bad6.map");
    File badFile7 = new File("./test/bad7.map");
    File badFile8 = new File("./test/bad8.map");
    File badFile9 = new File("./test/bad9.map");
    File badFile10 = new File("./test/bad10.map");
    testFile.delete();
    badFile1.delete();
    badFile2.delete();
    badFile3.delete();
    badFile4.delete();
    badFile5.delete();
    badFile6.delete();
    badFile7.delete();
    badFile8.delete();
    badFile9.delete();
    badFile10.delete();
}

/**
 * Method: read(Reader reader).
 *
 * @throws Exception may occur in test
 */
@Test
public void testRead() throws Exception {
    Position p;
    PacmanBoard test = new PacmanBoard(25, 9);
    test.setEntry(new Position(1, 1), BoardItem.DOT);
    test.setEntry(new Position(6, 2), BoardItem.BIG_DOT);
    test.setEntry(new Position(12, 2), BoardItem.WALL);
    test.setEntry(new Position(19, 2), BoardItem.BIG_DOT);
    test.setEntry(new Position(12, 3), BoardItem.WALL);
    for (int i = 8; i <= 16; i++) {
        p = new Position(i, 4);
        test.setEntry(p, BoardItem.WALL);
    }
    test.setEntry(new Position(12, 5), BoardItem.WALL);
    test.setEntry(new Position(6, 6), BoardItem.BIG_DOT);
    test.setEntry(new Position(19, 6), BoardItem.BIG_DOT);
    test.setEntry(new Position(1, 7), BoardItem.PACMAN_SPAWN);
    test.setEntry(new Position(12, 7), BoardItem.WALL);
    test.setEntry(new Position(23, 7), BoardItem.GHOST_SPAWN);

    FileReader fileIn = new FileReader("./test/default.map");
    PacmanGame A = GameReader.read(fileIn);
    // check board
    Assert.assertTrue(A.getBoard().equals(test));
    // check game information
    Assert.assertEquals("Default CSSE2002 PacMan Map", A.getTitle());
    Assert.assertEquals("Evan Hughes", A.getAuthor());
    Assert.assertEquals(5, A.getLives());
    Assert.assertEquals(2, A.getLevel());
    Assert.assertEquals(123, A.getScores().getScore());
    Assert.assertEquals("1,1,LEFT,20,PHIL", A.getHunter().toString());
    for (Ghost g : A.getGhosts()) {
        if (g.getType() == GhostType.BLINKY) {
            Assert.assertEquals("3,6,UP,FRIGHTENED:15", g.toString());
        } else if (g.getType() == GhostType.INKY) {
            Assert.assertEquals("1,6,UP,SCATTER:7", g.toString());
        } else if (g.getType() == GhostType.PINKY) {
            Assert.assertEquals("8,6,UP,FRIGHTENED:15", g.toString());
        } else if (g.getType() == GhostType.CLYDE) {
            Assert.assertEquals("6,4,UP,CHASE:4", g.toString());
        }
    }
    // check scores information
    Assert.assertEquals("A : 0",
            A.getScores().getEntriesByName().get(0));
    Assert.assertEquals("B : 5",
            A.getScores().getEntriesByName().get(1));
    Assert.assertEquals("C : 100",
            A.getScores().getEntriesByName().get(2));
}

/**
 * Method: read(Reader reader), but files are invalid files.
 *
 * @throws Exception may occur in test
 */
@Test
public void testReadbad() throws Exception {
    int num = 0;
    FileReader bad1 = new FileReader("./test/bad1.map");
    FileReader bad2 = new FileReader("./test/bad2.map");
    FileReader bad3 = new FileReader("./test/bad3.map");
    FileReader bad4 = new FileReader("./test/bad4.map");
    FileReader bad5 = new FileReader("./test/bad5.map");
    FileReader bad6 = new FileReader("./test/bad6.map");
    FileReader bad7 = new FileReader("./test/bad7.map");
    FileReader bad8 = new FileReader("./test/bad8.map");
    FileReader bad9 = new FileReader("./test/bad9.map");
    FileReader bad10 = new FileReader("./test/bad10.map");
    List<FileReader> badFiles = new ArrayList<>();
    badFiles.add(bad1);
    badFiles.add(bad2);
    badFiles.add(bad3);
    badFiles.add(bad4);
    badFiles.add(bad5);
    badFiles.add(bad6);
    badFiles.add(bad7);
    badFiles.add(bad8);
    badFiles.add(bad9);
    badFiles.add(bad10);
    for (FileReader f : badFiles) {
        try {
            PacmanGame badGame = GameReader.read(f);
        } catch (Exception e) {
            Assert.assertEquals("pacman.util.UnpackableException",
                    e.toString());
            if ("pacman.util.UnpackableException".equals(e.toString())) {
                num++;
            }
        }
    }
    Assert.assertEquals(10, num);
    System.out.println("No of UnpackableException: " + num);
}
}
