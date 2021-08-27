public class Board
{
    private final int[][] field;
    private final boolean[][] startValue;

    public Board()
    {
        field = new int[9][9];
        startValue = new boolean[9][9];
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                field[i][j] = 0;
                startValue[i][j] = false;
            }
        }
    }

    public int[][] getField()
    {
        return field;
    }

    public void setField(int column, int line, int value)
    {
        this.field[column][line] = value;
    }

    public boolean isStartingValue(int column, int line)
    {
        return startValue[column][line];
    }

    public void setAsStartValue(int column, int line)
    {
        startValue[column][line] = true;
    }

    public void setCurrentAsStart()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (field[i][j] != 0)
                {
                    startValue[i][j] = true;
                }
            }
        }
    }

    public String toString()
    {
        String output = "-----------------------------------------\n";
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (j % 3 == 0)
                {
                    output += "||";
                }
                else
                {
                    output += "|";
                }
                if (field[j][i] == 0)
                {
                    output += "   ";
                }
                else
                {
                    output += " " + field[j][i] + " ";
                }
            }
            output += "||\n-----------------------------------------\n";
        }
        return output;
    }

    public Board copy()
    {
        Board board = new Board();
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                board.setField(i, j, this.field[i][j]);
            }
        }
        return board;
    }
}
