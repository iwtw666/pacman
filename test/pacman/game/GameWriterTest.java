package pacman.game;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pacman.board.BoardItem;
import pacman.board.PacmanBoard;
import pacman.ghost.Ghost;
import pacman.ghost.GhostType;
import pacman.ghost.Phase;
import pacman.hunter.Hunter;
import pacman.hunter.Speedy;
import pacman.util.Direction;
import pacman.util.Position;

import java.io.*;

/** 
* GameWriter Tester. 
* 
* @author <Will_the_Wizard>
* @since <pre>24, Sep, 2019</pre>
* @version 2.5 - solved 90% problems (not handle IOE)
*/ 
public class GameWriterTest {

/**
* delete test file.
*
* @throws Exception might occur when delete the file
*/
@After
public void after() throws Exception {
    File testFile = new File("./test/out_test.txt");
    testFile.delete();
}

/** 
* 
* Method: write(Writer writer, PacmanGame game) 
* 
*/ 
@Test
public void testWrite() throws Exception {
    PacmanBoard board = new PacmanBoard(5, 4);
    Position pacP = new Position(1, 1);
    Position ghostP = new Position(2, 2);
    board.setEntry(pacP, BoardItem.PACMAN_SPAWN);
    board.setEntry(ghostP, BoardItem.GHOST_SPAWN);
    Hunter h = new Speedy();
    PacmanGame game = new PacmanGame("test", "1123", h, board);
    game.getScores().setScore("test", 125);
    game.getScores().setScore("Alex", 30);
    game.getScores().increaseScore(20);
    for (Ghost g : game.getGhosts()) {
        if (g.getType() == GhostType.INKY) {
            g.setPhase(Phase.CHASE, 20);
        }
        if (g.getType() == GhostType.PINKY) {
            g.setDirection(Direction.DOWN);
        }
    }
    // write and output a file in root/test/ directory named "out.txt"
    FileWriter file = new FileWriter("./test/out_test.txt");
    GameWriter.write(file, game);
    file.close();
    // try to read the file and check by line
    FileReader reader = new FileReader("./test/out_test.txt");
    BufferedReader read = new BufferedReader(reader);
    Assert.assertEquals("[Board]", read.readLine());
    Assert.assertEquals("5,4", read.readLine());
    Assert.assertEquals("XXXXX", read.readLine());
    Assert.assertEquals("XP00X", read.readLine());
    Assert.assertEquals("X0$0X", read.readLine());
    Assert.assertEquals("XXXXX", read.readLine());
    Assert.assertEquals("", read.readLine());
    Assert.assertEquals("[Game]", read.readLine());
    Assert.assertEquals("title = test", read.readLine());
    Assert.assertEquals("author = 1123", read.readLine());
    Assert.assertEquals("lives = 4", read.readLine());
    Assert.assertEquals("level = 0", read.readLine());
    Assert.assertEquals("score = 20", read.readLine());
    Assert.assertEquals("hunter = 0,0,UP,0,SPEEDY", read.readLine());
    Assert.assertEquals("blinky = 2,2,UP,SCATTER:10", read.readLine());
    Assert.assertEquals("inky = 2,2,UP,CHASE:20", read.readLine());
    Assert.assertEquals("pinky = 2,2,DOWN,SCATTER:10", read.readLine());
    Assert.assertEquals("clyde = 2,2,UP,SCATTER:10", read.readLine());
    Assert.assertEquals("", read.readLine());
    Assert.assertEquals("[Scores]", read.readLine());
    Assert.assertEquals("Alex : 30", read.readLine());
    Assert.assertEquals("test : 125", read.readLine());
    Assert.assertNull(read.readLine());
    read.close();
}
} 
