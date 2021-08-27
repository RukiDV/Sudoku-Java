import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest
{
    private Board botBoard;
    private Bot bot;

    @BeforeEach
    void setUp()
    {
        bot = new Bot();
        botBoard = new Board();
        botBoard.setField(1, 0, 3);
        botBoard.setField(3, 1, 1);
        botBoard.setField(4, 1, 9);
        botBoard.setField(5, 1, 5);
        botBoard.setField(7, 2, 6);
        botBoard.setField(2, 2, 8);
        botBoard.setField(0, 3, 8);
        botBoard.setField(4, 3, 6);
        botBoard.setField(0, 4, 4);
        botBoard.setField(3, 4, 8);
        botBoard.setField(8, 4, 1);
        botBoard.setField(4, 5, 2);
        botBoard.setField(1, 6, 6);
        botBoard.setField(6, 6, 2);
        botBoard.setField(7, 6, 8);
        botBoard.setField(3, 7, 4);
        botBoard.setField(4, 7, 1);
        botBoard.setField(5, 7, 9);
        botBoard.setField(8, 7, 5);
        botBoard.setField(7, 8, 7);
    }

    @Test
    void testUniqueSquare()
    {
        botBoard.setField(0, 2, 2);
        botBoard.setField(1, 2, 9);
        botBoard.setField(0, 1, 6);
        botBoard.setField(1, 1, 7);
        botBoard.setField(2, 1, 4);
        botBoard.setField(2, 0, 5);
        // check when only one field is left in the 3x3 square
        assertTrue(bot.checkUniqueSquare(botBoard, 0, 0, 1, 0));
        botBoard.setField(0, 1, 0);
        botBoard.setField(1, 1, 0);
        botBoard.setField(2, 1, 0);
        // check when there are more fields free in the 3x3 square, but they are not allowed cause of a line rule
        assertTrue(bot.checkUniqueSquare(botBoard, 0, 0, 1, 0));
        botBoard.setField(2, 0, 0);
        assertFalse(bot.checkUniqueSquare(botBoard, 0, 0, 1, 0));
        assertFalse(bot.checkUniqueSquare(botBoard, 2, 0, 1, 0));
        botBoard.setField(7, 7, 3);
        botBoard.setField(6, 8, 4);
        botBoard.setField(8, 6, 9);
        assertTrue(bot.checkUniqueSquare(botBoard, 8, 8, 1, 8));
        assertFalse(bot.checkUniqueSquare(botBoard, 8, 8, 6, 8));
    }

    @Test
    void testUniqueColumn()
    {
        botBoard.setField(6, 2, 3);
        botBoard.setField(3, 8, 3);
        assertTrue(bot.checkUniqueColumn(botBoard, 4, 4, 3));
        botBoard.setField(3, 8, 0);
        assertFalse(bot.checkUniqueColumn(botBoard, 4, 4, 3));
    }

    @Test
    void testUniqueLine()
    {
        botBoard.setField(6, 2, 3);
        botBoard.setField(0, 8, 3);
        assertTrue(bot.checkUniqueLine(botBoard, 7, 7, 3));
        assertFalse(bot.checkUniqueLine(botBoard, 1, 7, 2));
    }
}