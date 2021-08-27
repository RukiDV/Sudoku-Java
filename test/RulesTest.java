import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RulesTest
{
    Rules rules;
    Board board;

    @BeforeEach
    void setUp()
    {
        rules = new Rules();
        board = new Board();
        board.setField(0, 0, 1);
        board.setField(0, 1, 7);
        board.setField(0, 2, 6);
        board.setField(0, 3, 9);
        board.setField(0, 4, 5);
        board.setField(0, 5, 4);
        board.setField(0, 6, 8);
        board.setField(0, 7, 3);
        board.setField(0, 8, 2);
        board.setField(1, 0, 4);
        board.setField(1, 1, 3);
        board.setField(1, 2, 8);
        board.setField(1, 3, 2);
        board.setField(1, 4, 7);
        board.setField(1, 5, 1);
        board.setField(1, 6, 9);
        board.setField(1, 7, 6);
        board.setField(1, 8, 5);
        board.setField(2, 0, 9);
        board.setField(2, 1, 2);
        board.setField(2, 2, 5);
        board.setField(2, 3, 3);
        board.setField(2, 4, 8);
        board.setField(2, 5, 6);
        board.setField(2, 6, 4);
        board.setField(2, 7, 7);
        board.setField(2, 8, 1);
        board.setField(3, 0, 8);
        board.setField(3, 1, 4);
        board.setField(3, 2, 3);
        board.setField(3, 3, 7);
        board.setField(3, 4, 2);
        board.setField(3, 5, 9);
        board.setField(3, 6, 1);
        board.setField(3, 7, 5);
        board.setField(3, 8, 6);
        board.setField(4, 0, 7);
        board.setField(4, 1, 6);
        board.setField(4, 2, 2);
        board.setField(4, 3, 1);
        board.setField(4, 4, 4);
        board.setField(4, 5, 5);
        board.setField(4, 6, 3);
        board.setField(4, 7, 9);
        board.setField(4, 8, 8);
        board.setField(5, 0, 5);
        board.setField(5, 1, 1);
        board.setField(5, 2, 9);
        board.setField(5, 3, 8);
        board.setField(5, 4, 6);
        board.setField(5, 5, 3);
        board.setField(5, 6, 2);
        board.setField(5, 7, 4);
        board.setField(5, 8, 7);
        board.setField(6, 0, 6);
        board.setField(6, 1, 9);
        board.setField(6, 2, 4);
        board.setField(6, 3, 5);
        board.setField(6, 4, 1);
        board.setField(6, 5, 8);
        board.setField(6, 6, 7);
        board.setField(6, 7, 2);
        board.setField(6, 8, 3);
        board.setField(7, 0, 2);
        board.setField(7, 1, 5);
        board.setField(7, 2, 1);
        board.setField(7, 3, 4);
        board.setField(7, 4, 3);
        board.setField(7, 5, 7);
        board.setField(7, 6, 6);
        board.setField(7, 7, 8);
        board.setField(7, 8, 9);
        board.setField(8, 0, 3);
        board.setField(8, 1, 8);
        board.setField(8, 2, 7);
        board.setField(8, 3, 6);
        board.setField(8, 4, 9);
        board.setField(8, 5, 2);
        board.setField(8, 6, 5);
        board.setField(8, 7, 1);
        board.setField(8, 8, 4);
    }

    @Test
    void testCheckSquare()
    {
        for (int i = 0; i < 9; i++)
        {
            assertTrue(rules.checkSquare(board, i));
        }

        board.setField(8, 8, 5);
        assertFalse(rules.checkSquare(board, 8));

        board.setField(8, 8, 0);
        board.setField(7, 7, 0);
        board.setField(8, 6, 0);

        assertTrue(rules.checkSquare(board, 8));
    }

    @Test
    void testCheckLineColumn()
    {
        for (int i = 0; i < 9; i++)
        {
            assertTrue(rules.checkLineColumn(board, i, true));
            assertTrue(rules.checkLineColumn(board, i, false));
        }

        board.setField(0, 0, 1);
        board.setField(1, 0, 1);

        assertFalse(rules.checkLineColumn(board, 0, true));

        board.setField(0, 0, 1);
        board.setField(0, 1, 1);

        assertFalse(rules.checkLineColumn(board, 0, false));
    }
}

