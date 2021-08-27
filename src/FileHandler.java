import java.io.*;

public class FileHandler
{
    public boolean writeFile(Board b, String path)
    {
        try
        {
            File file = new File(path);
            if (file.getParentFile().mkdirs() && file.createNewFile())
            {
                System.out.println("New file successfully created!");
            }
            else
            {
                System.out.println("File already exists! Overwriting file...");
            }
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            byte fileContent[] = getFileOutput(b);
            fileOutputStream.write(fileContent);
        }
        catch (IOException e)
        {
            System.out.println("Error writing file!");
            return false;
        }
        return true;
    }

    public Board readFile(String path)
    {
        Board b = new Board();
        int input[] = new int[81];
        try (FileInputStream fileInputStream = new FileInputStream(path))
        {
            for (int i = 0; i < 81; i++)
            {
                int nextSymbol = fileInputStream.read();
                if (nextSymbol != -1)
                {
                    input[i] = nextSymbol;
                }
                else
                {
                    System.out.println("Invalid File!");
                    fileInputStream.close();
                    return null;
                }
            }
            if (fileInputStream.read() != -1)
            {
                System.out.println("File did not end as expected!");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading file!");
            return null;
        }
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                int nextSymbol = input[i * 9 + j];
                if (nextSymbol >= 10)
                {
                    b.setField(i, j, nextSymbol - 10);
                    b.setAsStartValue(i, j);
                }
                else
                {
                    b.setField(i, j, nextSymbol);
                }
            }
        }
        return b;
    }

    private byte[] getFileOutput(Board b)
    {
        // if the number is set by the user save it normally, if the number is fix because it was in the starting board save it +10
        byte output[] = new byte[81];
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                output[i * 9 + j] = (byte)(b.isStartingValue(i, j) ? b.getField()[i][j] + 10 : b.getField()[i][j]);
            }
        }
        return output;
    }
}
