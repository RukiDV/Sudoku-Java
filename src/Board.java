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
        Rules ruler = new Rules();
        String output = "\u001B[46m-----------------------------------------\u001B[0m\n";
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (j % 3 == 0)
                {
                    output += "\u001B[46m||\u001B[0m";
                }
                else
                {
                    output += "|";
                }
                output += " ";
                if (field[j][i] == 0)
                {
                    output += " ";
                }
                else if (!isStartingValue(j, i))
                {
                    if (!ruler.checkNumber(this, j, i, field[j][i]))
                    {
                        output += "\u001B[31m";
                    }
                    else
                    {
                        output += "\u001B[32m";
                    }
                    output += field[j][i] + "\u001B[0m";
                }
                else
                {
                    output += field[j][i];
                }
                output += " ";
            }
            output += "\u001B[46m||\u001B[0m";
            output += (i + 1) % 3 == 0 ? "\n\u001B[46m-----------------------------------------\u001B[0m\n"
                    : "\n\u001B[46m--\u001B[0m-----------\u001B[46m--\u001B[0m-----------\u001B[46m--\u001B[0m-----------\u001B[46m--\u001B[0m\n";
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
                if (startValue[i][j])
                {
                    board.setAsStartValue(i, j);
                }
                board.setField(i, j, this.field[i][j]);
            }
        }
        return board;
    }
}
