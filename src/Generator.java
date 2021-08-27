public class Generator
{

    public Board generateBoard(int difficulty, Bot bot)
    {
        bot.solutionBoards.clear();
        Board b = new Board();
        // set random number at random location on the field, then solve the field, this will generate random fields
        int column = (int)Math.round((Math.random() * 8));
        int line = (int)Math.round((Math.random() * 8));
        int value = (int)Math.round((Math.random() * 8) + 1);
        b.setField(column, line, value);
        //System.out.println(b);
        if (bot.getSolution(b, 1) >= 1)
        {
            b = bot.solutionBoards.get(0);
        }
        //System.out.println(b);
        // delete numbers from the field
        int fieldsToRemove = (difficulty + 3) * 10;
        for (int k = 0; k < fieldsToRemove; k++)
        {
            Board workBoard;
            int counter = 0;
            // first loop checks if the board is uniquely solvable with one random chosen location, where the number got deleted
            do
            {
                bot.solutionBoards.clear();
                // use counter to find out if we are able to delete another numbers
                counter++;
                //System.out.println(counter);
                if (counter > 10)
                {
                    break;
                }
                // second loop checks if the current location already is empty, if it is, choose new location
                do
                {
                    column = (int)Math.round(Math.random() * 8);
                    line = (int)Math.round(Math.random() * 8);
                } while (b.getField()[column][line] == 0);
                workBoard = b.copy();
                workBoard.setField(column, line, 0);
            } while (bot.getSolution(workBoard, 2) >= 2);
            // after 10 random attempts, traverse board and try every location, if none works we cannot delete another number
            if (counter > 10)
            {
                // foundOne is just for performance, if we cannot delete another number we don't need to finish the outer for loop
                boolean foundOne = false;
                for (int i = 0; i < 9 && !foundOne; i++)
                {
                    for (int j = 0; j < 9 && !foundOne; j++)
                    {
                        if (b.getField()[i][j] != 0)
                        {
                            workBoard = b.copy();
                            workBoard.setField(column, line, 0);
                            if (bot.getSolution(workBoard, 2) == 1)
                            {
                                System.out.println("i:" + i + " j:" + j);
                                b.setField(i, j, 0);
                                foundOne = true;
                            }
                        }
                    }
                }
                // break deleting loop, because we cannot delete another number
                if (!foundOne)
                {
                    System.out.println("Finished early!");
                    break;
                }
            }
            // if we exited the do while loop in a normal way, delete the field
            else
            {
                b.setField(column, line, 0);
            }
//            System.out.println(k);
        }
        //System.out.println(b);
        return b;
    }
}
