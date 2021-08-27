public class Rules
{
    public Rules()
    {
    }

    public int getSquareIndex(int column, int line)
    {
        column /= 3;
        line /= 3;
        return column + line * 3;
    }

    public boolean checkSquare(Board b, int index)
    {
        boolean[] numbers = new boolean[10];
        for (int i = 0; i < 10; i++)
        {
            numbers[i] = false;
        }
        for (int i = (index % 3) * 3; i < 3 + (index % 3) * 3; i++)
        {
            for (int j = (index / 3) * 3; j < 3 + (index / 3) * 3; j++)
            {
                int currentNumber = b.getField()[i][j];
                if (numbers[currentNumber])
                {
                    if (currentNumber != 0)
                    {
                        return false;
                    }
                }
                else
                {
                    numbers[currentNumber] = true;
                }
            }
        }
        return true;
    }

    public boolean checkLineColumn(Board b, int index, boolean line)
    {
        boolean[] numbers = new boolean[10];
        for (int i = 0; i < 10; i++)
        {
            numbers[i] = false;
        }
        int currentNumber;
        for (int i = 0; i < 9; i++)
        {
            if (line)
            {
                currentNumber = b.getField()[i][index];
            }
            else
            {
                currentNumber = b.getField()[index][i];
            }
            if (numbers[currentNumber])
            {
                if (currentNumber != 0)
                {
                    return false;
                }
            }
            else
            {
                numbers[currentNumber] = true;
            }
        }
        return true;
    }

    public boolean checkNumberSquare(Board b, int column, int line, int number)
    {
        Board boardCopy = b.copy();
        boardCopy.setField(column, line, number);
        if (!checkLineColumn(boardCopy, line, true))
        {
            boardCopy.setField(column, line, 0);
            return false;
        }
        if (!checkLineColumn(boardCopy, column, false))
        {
            boardCopy.setField(column, line, 0);
            return false;
        }
        if (!checkSquare(boardCopy, getSquareIndex(column, line)))
        {
            boardCopy.setField(column, line, 0);
            return false;
        }
        boardCopy.setField(column, line, 0);
        return true;
    }

    public boolean isBoardAllowed(Board b)
    {
        for (int i = 0; i < 9; i++)
        {
            if (!checkLineColumn(b, i, true))
            {
                return false;
            }
            if (!checkLineColumn(b, i, false))
            {
                return false;
            }
            if (!checkSquare(b, i))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isBoardFinished(Board b)
    {
        if (isBoardAllowed(b))
        {
            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 9; j++)
                {
                    if (b.getField()[i][j] == 0)
                    {
                        return false;
                    }
                }
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}
