import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Bot
{
    private final Rules ruler;
    public final ArrayList<Board> solutionBoards = new ArrayList<>();
    private static final ArrayList<Thread> threads = new ArrayList<>();
    private static final AtomicInteger threadCounter = new AtomicInteger();
    private static boolean killThreads = false;

    public Bot()
    {
        ruler = new Rules();
    }

    private synchronized void addSolution(Board b)
    {
        if (killThreads)
        {
            return;
        }
        solutionBoards.add(b);
//        System.out.println(solutionBoards.get(solutionBoards.size() - 1));
//        System.out.println("Solution added! New Size: " + solutionBoards.size());
    }

    private static void checkAndDecrementThreadCount(int recursionDepth)
    {
        if (recursionDepth == 0)
        {
            threadCounter.decrementAndGet();
        }
    }

    public int getSolution(Board b, int solutionCount)
    {
        threadCounter.incrementAndGet();
        Thread t = new Thread(() -> solveBoard(b, 0));
        t.start();
        // solutionCount = 0 searches for every possible solution
        if (solutionCount == 0)
        {
            while (threadCounter.get() > 0)
            {
                Thread.yield();
            }
        }
        else
        {
            while (solutionBoards.size() < solutionCount && threadCounter.get() > 0)
            {
                Thread.yield();
            }
        }
        // killThreads doesn't need to be thread safe, it is only set once here at this position
        killThreads = true;
        while (threadCounter.get() > 0) ;
        try
        {
            for (int i = 0; i < threads.size(); ++i)
            {
                // there are no dead threads, because threads get created from lower to higher indices
                // so if the joining starts at the lower indices
                if (threads.get(i) != null && threads.get(i).isAlive())
                {
                    threads.get(i).join();
                }
            }
            threads.clear();
            t.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        killThreads = false;
        return solutionBoards.size();
    }

    public void solveBoard(Board b, int recursionDepth)
    {
        if (killThreads)
        {
            checkAndDecrementThreadCount(recursionDepth);
            return;
        }
        // copy Board if a guess was wrong
        Board workBoard = b.copy();
        // fill board with unique numbers
        while (nextNumber(workBoard)) ;
        // when the board is finished assign current board to the given board, the current board will then be reached to the top
        if (ruler.isBoardFinished(workBoard))
        {
            addSolution(workBoard);
            checkAndDecrementThreadCount(recursionDepth);
            return;
        }
        ArrayList<ArrayList<ArrayList<Integer>>> arrayBoard = getOpportunities(workBoard);
        // find column, line and size of the point with the least opportunities
        int column = 0;
        int line = 0;
        int size = 10;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (arrayBoard.get(i).get(j).size() < size)
                {
                    column = i;
                    line = j;
                    size = arrayBoard.get(i).get(j).size();
                }
            }
        }
        if (size == 0)
        {
            checkAndDecrementThreadCount(recursionDepth);
            return;
        }
        // size is always >= 2, because if size = 1 it would be a unique number for this field and hence already set
        for (int i = 0; i < size; i++)
        {
            int threadIndex = threadCounter.getAndIncrement();
            if (threadIndex < 14)
            {
                // TODO: ArrayList threads also saves the already dead threads, which is kind of unnecessary
                Board threadRecursionBoard = workBoard.copy();
                threadRecursionBoard.setField(column, line, arrayBoard.get(column).get(line).remove(arrayBoard.get(column).get(line).size() - 1));
                Thread t = new Thread(() -> solveBoard(threadRecursionBoard, 0));
                synchronized (this)
                {
                    threads.add(t);
                    t.start();
                }
            }
            else
            {
                threadCounter.decrementAndGet();
                Board recursionBoard = workBoard.copy();
                recursionBoard.setField(column, line, arrayBoard.get(column).get(line).remove(arrayBoard.get(column).get(line).size() - 1));
                solveBoard(recursionBoard, recursionDepth + 1);
            }
        }
        checkAndDecrementThreadCount(recursionDepth);
    }

    private ArrayList<ArrayList<ArrayList<Integer>>> getOpportunities(Board workBoard)
    {
        // save opportunities for a number field in an array for this field
        ArrayList<ArrayList<ArrayList<Integer>>> arrayBoard = new ArrayList<>();
        for (int i = 0; i < 9; i++)
        {
            arrayBoard.add(new ArrayList<>());
            for (int j = 0; j < 9; j++)
            {
                arrayBoard.get(i).add(new ArrayList<>());
            }
        }
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                // if the board has a number at this point, add the number 10 times that the size of this array is always bigger than any other opportunity array
                if (workBoard.getField()[i][j] != 0)
                {
                    int number = workBoard.getField()[i][j];
                    for (int k = 0; k < 10; k++)
                    {
                        arrayBoard.get(i).get(j).add(number);
                    }
                }
                // else search all opportunities for this point and add them to the array
                else
                {
                    for (int k = 1; k <= 9; k++)
                    {
                        if (ruler.checkNumberSquare(workBoard, i, j, k))
                        {
                            arrayBoard.get(i).get(j).add(k);
                        }
                    }
                }
            }
        }
        return arrayBoard;
    }

    private boolean nextNumber(Board b)
    {
        // finds the next unique number for the board and sets it, it will not always find a new number
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (b.getField()[i][j] == 0)
                {
                    for (int k = 1; k <= 9; k++)
                    {
                        if (ruler.checkNumberSquare(b, i, j, k))
                        {
                            if (checkUnique(b, i, j, k))
                            {
                                b.setField(i, j, k);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkUnique(Board b, int column, int line, int number)
    {
        Board boardCopy = b.copy();
        // checks if a given number is in the point given by column and line unique in any direction
        // this means that it is not possible for the number to stand somewhere else for at least one case
        if (checkUniqueSquare(boardCopy, column, line, number, ruler.getSquareIndex(column, line)))
        {
            return true;
        }
        if (checkUniqueColumn(boardCopy, column, line, number))
        {
            return true;
        }
        if (checkUniqueLine(boardCopy, column, line, number))
        {
            return true;
        }
        return false;
    }

    public boolean checkUniqueSquare(Board b, int column, int line, int number, int index)
    {
        for (int i = (index % 3) * 3; i < 3 + (index % 3) * 3; i++)
        {
            for (int j = (index / 3) * 3; j < 3 + (index / 3) * 3; j++)
            {
                if (!(b.getField()[i][j] != 0 || (column == i && line == j)))
                {
                    b.setField(i, j, number);
                    if (ruler.checkSquare(b, index) && ruler.checkLineColumn(b, i, false) && ruler.checkLineColumn(b, j, true))
                    {
                        b.setField(i, j, 0);
                        return false;
                    }
                    b.setField(i, j, 0);
                }
            }
        }
        return true;
    }

    public boolean checkUniqueColumn(Board b, int column, int line, int number)
    {
        for (int i = 0; i < 9; i++)
        {
            if (!(b.getField()[column][i] != 0 || (line == i)))
            {
                b.setField(column, i, number);
                if (ruler.checkLineColumn(b, column, false) && ruler.checkLineColumn(b, i, true) && ruler.checkSquare(b, ruler.getSquareIndex(column, i)))
                {
                    b.setField(column, i, 0);
                    return false;
                }
                b.setField(column, i, 0);
            }
        }
        return true;
    }

    public boolean checkUniqueLine(Board b, int column, int line, int number)
    {
        for (int i = 0; i < 9; i++)
        {
            if (!(b.getField()[i][line] != 0 || (column == i)))
            {
                b.setField(i, line, number);
                if (ruler.checkLineColumn(b, i, false) && ruler.checkLineColumn(b, line, true) && ruler.checkSquare(b, ruler.getSquareIndex(i, line)))
                {
                    b.setField(i, line, 0);
                    return false;
                }
                b.setField(i, line, 0);
            }
        }
        return true;
    }
}
