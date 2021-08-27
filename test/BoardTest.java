import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest
{
    Board board;

    @BeforeEach
    void setUp()
    {
        board = new Board();
        board.setField(1, 0, 3);
        board.setField(3, 1, 1);
        board.setField(4, 1, 9);
        board.setField(5, 1, 5);
        board.setField(7, 2, 6);
        board.setField(2, 2, 8);
        board.setField(0, 3, 8);
        board.setField(4, 3, 6);
        board.setField(0, 4, 4);
        board.setField(3, 4, 8);
        board.setField(8, 4, 1);
        board.setField(4, 5, 2);
        board.setField(1, 6, 6);
        board.setField(6, 6, 2);
        board.setField(7, 6, 8);
        board.setField(3, 7, 4);
        board.setField(4, 7, 1);
        board.setField(5, 7, 9);
        board.setField(8, 7, 5);
        board.setField(7, 8, 7);
    }

    @Test
    void copyTest()
    {
        Board testBoard = board.copy();
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                assertEquals(board.getField()[i][j], testBoard.getField()[i][j]);
            }
        }
        testBoard.setField(6, 8, 3);
        testBoard.setField(1, 0, 9);
        assertEquals(0, board.getField()[6][8]);
        assertEquals(3, board.getField()[1][0]);
        assertEquals(3, testBoard.getField()[6][8]);
        assertEquals(9, testBoard.getField()[1][0]);
    }
}
