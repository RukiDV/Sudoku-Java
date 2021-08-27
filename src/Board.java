public class Board
{
    private int[][] field;

    public Board()
    {
        field = new int[9][9];
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                field[i][j] = 0;
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

    public void setCompleteField(int[][] field)
    {
        this.field = field;
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
