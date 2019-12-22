package Shobu;

import org.junit.Test;

import javax.rmi.CORBA.Util;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testDefaultBoardConfiguration() {
        Board b = new Board(true);
        String defaultGameBoard = "oooo|oooo\n" + "    |    \n" + "    |    \n" + "xxxx|xxxx\n" + "----|----\n" + "oooo|oooo\n" + "    |    \n" + "    |    \n" + "xxxx|xxxx\n";
        assertEquals(defaultGameBoard, b.toString());
    }

    @Test
    public void testMovingStone() {
        // When I move a stone from one place to another, it should be present in the new location and not in the old.
        Board b = new Board(true);
        int expectedId = b.getStone(new Vector2(0, 0)).getId();
        b.moveStone(new Vector2(0,0), new Vector2(0, 1));
        assertEquals(expectedId, b.getStone(new Vector2(0, 1)).getId());
        assertEquals(null, b.getStone(new Vector2(0,0)));
    }

    @Test
    public void testMovingAcrossBoundaries() {
        Board b = new Board(true); // This has the default game board configuration, so we move an existing stone.
        b.moveStone(new Vector2(3,3), new Vector2(4, 3));
        assertEquals(15, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.BLACK));
        assertEquals(16, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));
    }

    @Test
    public void testAccessors() {
        Board b = new Board(false);
        StoneFactory sf = StoneFactory.getInstance();
        Stone s1 = sf.createStone(Stone.COLOR.WHITE);
        Stone s2 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(0,0), s1);
        b.setStone(new Vector2(2,5), s2);

        assertTrue(b.getStone(new Vector2(0,0)).getId() == s1.getId());
        assertTrue(b.getStone(new Vector2(2,5)).getId() == s2.getId());
        assertTrue(new Vector2(0,0).equals(b.getStoneLocation(s1.getId())));
        assertTrue(new Vector2(2,5).equals(b.getStoneLocation(s2.getId())));
    }

    @Test
    public void testPushingStones() {

        // Push one stone in a line downwards a single space
        Board b = new Board(false);
        StoneFactory sf = StoneFactory.getInstance();
        Stone s1 = sf.createStone(Stone.COLOR.WHITE);
        Stone s2 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(0,0), s1);
        b.setStone(new Vector2(0,1), s2);
        System.out.println("Board setup for moving one space down test");
        System.out.println(b.toString());
        Move m = new Move(new Vector2(0,0), new Vector2(0, 1)); // one space downwards
        b.pushStones(m);
        System.out.println("After moving one space down");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(0, 1)).getId());
        assertEquals(s2.getId(), b.getStone(new Vector2(0, 2)).getId());
        assertEquals(2, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));

        // Push one stone in a line downwards two spaces
        b = new Board(false);
        sf = StoneFactory.getInstance();
        s1 = sf.createStone(Stone.COLOR.WHITE);
        s2 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(0,0), s1);
        b.setStone(new Vector2(0,1), s2);
        System.out.println("Board setup for moving two space down test");
        System.out.println(b.toString());
        m = new Move(new Vector2(0,0), new Vector2(0, 2)); // one space downwards
        b.pushStones(m);
        System.out.println("After moving two spaces down");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(0, 2)).getId());
        assertEquals(s2.getId(), b.getStone(new Vector2(0, 3)).getId());
        assertEquals(2, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));

        // Push one stone diagonal one space
        b = new Board(false);
        sf = StoneFactory.getInstance();
        s1 = sf.createStone(Stone.COLOR.WHITE);
        s2 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(0,0), s1);
        b.setStone(new Vector2(1,1), s2);
        System.out.println("Board setup for moving one space diagonal test");
        System.out.println(b.toString());
        m = new Move(new Vector2(0,0), new Vector2(1, 1)); // one space downwards
        b.pushStones(m);
        System.out.println("After moving one space diagonal");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(1, 1)).getId());
        assertEquals(s2.getId(), b.getStone(new Vector2(2, 2)).getId());
        assertEquals(2, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));

        // Push two stones diagonal one space
        b = new Board(false);
        sf = StoneFactory.getInstance();
        s1 = sf.createStone(Stone.COLOR.WHITE);
        s2 = sf.createStone(Stone.COLOR.WHITE);
        Stone s3 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(0,0), s1);
        b.setStone(new Vector2(1,1), s2);
        b.setStone(new Vector2(2,2), s3);
        System.out.println("Board setup for moving two stones one space diagonal test");
        System.out.println(b.toString());
        m = new Move(new Vector2(0,0), new Vector2(1, 1)); // one space downwards
        b.pushStones(m);
        System.out.println("After moving two stones diagonal one space");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(1, 1)).getId());
        assertEquals(s2.getId(), b.getStone(new Vector2(2, 2)).getId());
        assertEquals(s3.getId(), b.getStone(new Vector2(3, 3)).getId());
        assertEquals(3, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));

        // Push one stone off the board test
        b = new Board(false);
        sf = StoneFactory.getInstance();
        s1 = sf.createStone(Stone.COLOR.WHITE);
        s2 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(1,0), s1);
        b.setStone(new Vector2(0,0), s2);
        System.out.println("Board setup for pushing one stone off board test");
        System.out.println(b.toString());
        m = new Move(new Vector2(1,0), new Vector2(-1, 0)); // one space downwards
        b.pushStones(m);
        System.out.println("After pushing one stone off board");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(0, 0)).getId());
        assertEquals(1, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));

        // Push two stones diagonal off the board
        b = new Board(false);
        sf = StoneFactory.getInstance();
        s1 = sf.createStone(Stone.COLOR.WHITE);
        s2 = sf.createStone(Stone.COLOR.WHITE);
        s3 = sf.createStone(Stone.COLOR.WHITE);
        b.setStone(new Vector2(1,1), s1);
        b.setStone(new Vector2(2,2), s2);
        b.setStone(new Vector2(3,3), s3);
        System.out.println("Board setup for moving two stones two space diagonal test");
        System.out.println(b.toString());
        m = new Move(new Vector2(1,1), new Vector2(2, 2)); // one space downwards
        b.pushStones(m);
        System.out.println("After moving two stones diagonal two space");
        System.out.println(b.toString());
        assertEquals(s1.getId(), b.getStone(new Vector2(3, 3)).getId());
        assertEquals(1, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));
    }
}
