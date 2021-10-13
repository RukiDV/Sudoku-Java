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

    private void addSolution(Board b)
    {
        if (killThreads)
        {
            return;
        }
        synchronized (solutionBoards)
        {
            solutionBoards.add(b);
        }
//        System.out.println(solutionBoards.get(solutionBoards.size() - 1));
//        System.out.println("Solution added! New Size: " + solutionBoards.size());
    }

    private void checkAndDecrementThreadCount(int recursionDepth)
    {
        if (recursionDepth == 0)
        {
            // recursionDepth = 0 means that this thread is finished after the next return
            // so the thread gets removed from the active thread list and the threadCounter gets decremented
            // TODO: threads that finished their execution need to get joined
            // probably save another ArrayList where all threads get saved that finished their execution
            // the main threads always joins all threads in this list while waiting
            synchronized (threads)
            {
                threads.remove(Thread.currentThread());
            }
            threadCounter.decrementAndGet();
        }
    }

    // solutionCount = 0 searches for every possible solution
    public int getSolution(Board b, int solutionCount)
    {
        // create motherThread that starts searching for solutions and thereby also creates new threads
        threadCounter.incrementAndGet();
        Thread motherThread = new Thread(() -> solveBoard(b, 0));
        motherThread.start();
        // search for every possible solution, so just wait until all threads are finished
        if (solutionCount == 0)
        {
            while (threadCounter.get() > 0)
            {
                Thread.yield();
            }
        }
        // search for solutionCount solutions, so check every time if there are already enough solutions found
        // the other option is, that all threads finish without finding the desired count of solutions
        else
        {
            while (solutionBoards.size() < solutionCount && threadCounter.get() > 0)
            {
                Thread.yield();
            }
        }
        // killThreads doesn't need to be thread safe, it is only set once here at this position
        killThreads = true;
        // wait until all threads have finished their execution
        // a thread can create a new thread and add it to the ArrayList while we are already iterating over it
        // ArrayList isn't threadsafe, so don't use it in that way
        while (threadCounter.get() > 0) ;
        try
        {
            // join all threads
            for (Thread t: threads)
            {
                if (t != null && t.isAlive())
                {
                    t.join();
                }
            }
            threads.clear();
            motherThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // reset killThreads to be able to use this bot again
        killThreads = false;
        return solutionBoards.size();
    }

    public void solveBoard(Board b, int recursionDepth)
    {
        // threads got ordered to finish, so just check if we are finished after this level and return
        if (killThreads)
        {
            checkAndDecrementThreadCount(recursionDepth);
            return;
        }
        // fill board with unique numbers
        while (nextNumber(b)) ;
        // when the board is finished, add current board to solutions
        if (ruler.isBoardFinished(b))
        {
            addSolution(b);
            checkAndDecrementThreadCount(recursionDepth);
            return;
        }
        ArrayList<ArrayList<ArrayList<Integer>>> arrayBoard = getOpportunities(b);
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
            // get threadCounter and increment it if it is < 12
            int threadIndex = threadCounter.getAndUpdate((value) ->
                    value < 12 ? value + 1 : value);
            if (threadIndex < 12)
            {
                Board threadRecursionBoard = b.copy();
                threadRecursionBoard.setField(column, line, arrayBoard.get(column).get(line).remove(arrayBoard.get(column).get(line).size() - 1));
                Thread t = new Thread(() -> solveBoard(threadRecursionBoard, 0));
                synchronized (threads)
                {
                    threads.add(t);
                    t.start();
                }
            }
            else
            {
                Board recursionBoard = b.copy();
                recursionBoard.setField(column, line, arrayBoard.get(column).get(line).remove(arrayBoard.get(column).get(line).size() - 1));
                solveBoard(recursionBoard, recursionDepth + 1);
            }
        }
        checkAndDecrementThreadCount(recursionDepth);
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getOpportunities(Board workBoard)
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
                        if (ruler.checkNumber(workBoard, i, j, k))
                        {
                            arrayBoard.get(i).get(j).add(k);
                        }
                    }
                }
            }
        }
        return arrayBoard;
    }

    public boolean nextNumber(Board b)
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
                        if (ruler.checkNumber(b, i, j, k))
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
