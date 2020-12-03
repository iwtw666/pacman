package pacman;

import pacman.board.BoardItem;
import pacman.board.PacmanBoard;
import pacman.game.PacmanGame;
import pacman.ghost.*;
import pacman.hunter.Hunter;
import pacman.hunter.Phil;
import pacman.util.Direction;
import pacman.util.Position;

import java.util.Scanner;

/**
 * @author B1ex1ayU
 */
public class Test1 {

    public static void main(String[] args) {
        Hunter hunter = new Phil();
        PacmanBoard board = new PacmanBoard(20, 10);
        board.setEntry(new Position(15, 6), BoardItem.GHOST_SPAWN);
        board.setEntry(new Position(3, 3), BoardItem.PACMAN_SPAWN);
        board.setEntry(new Position(1, 2), BoardItem.WALL);
        board.setEntry(new Position(2, board.getHeight() - 2), BoardItem.WALL);
        board.setEntry(new Position(board.getWidth() - 3, 1), BoardItem.WALL);
        board.setEntry(new Position(board.getWidth() - 2,
                board.getHeight() - 3), BoardItem.WALL);
        board.reset();
        PacmanGame game = new PacmanGame("test", "author", hunter, board);
        game.reset();
        System.out.println(hunter.getPosition().toString());
        print(game);

        Scanner keyboard = new Scanner(System.in);

        char ch = ' ';
        while (ch != 'q') {
            System.out.println("\nEnter 'w','a','s','d' to move, 'q' to quit.");

            ch = (char) keyboard.next().charAt(0);

            switch (ch) {
                case 'w' :
                    hunter.setDirection(Direction.UP);
                    break;
                case 'a' :
                    hunter.setDirection(Direction.LEFT);
                    break;
                case 's' :
                    hunter.setDirection(Direction.DOWN);
                    break;
                case 'd' :
                    hunter.setDirection(Direction.RIGHT);
                    break;
                default:
                    System.out.println("error");
            }
            game.tick();
            print(game);
        }
    }

    private static void print(PacmanGame game) {
        Hunter hunter = game.getHunter();
        PacmanBoard board = game.getBoard();
        Blinky blinky = (Blinky) game.getGhosts().get(0);
        Clyde clyde = (Clyde) game.getGhosts().get(1);
        Inky inky = (Inky) game.getGhosts().get(2);
        Pinky pinky = (Pinky) game.getGhosts().get(3);
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (hunter.getPosition().equals(new Position(x, y))) {
                    System.out.print("H ");
                } else if (blinky.getPosition().equals(new Position(x, y))) {
                    System.out.print("b ");
                } else if (clyde.getPosition().equals(new Position(x, y))) {
                    System.out.print("c ");
                } else if (inky.getPosition().equals(new Position(x, y))) {
                    System.out.print("i ");
                } else if (pinky.getPosition().equals(new Position(x, y))) {
                    System.out.print("p ");
                } else if (board.getEntry(new Position(x, y)).getChar() == 'X') {
                    System.out.print(board.getEntry(new Position(x, y)).getChar() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        for (Ghost g : game.getGhosts()) {
            System.out.println(g + "  chaseTarget[x:" +
                    g.chaseTarget(game).getX() + "  y:" +
                    g.chaseTarget(game).getY() + "]   " +
                    g.getType().toString());

        }
        System.out.println(hunter + "   distance to CLYDE: " +
                hunter.getPosition().distance(game.getGhosts().get(1).getPosition()));
    }

}
