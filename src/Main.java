import java.util.Scanner;

public class Main
{
    private static final Scanner scanner = new Scanner(System.in);

    // TODO: comment code, to few comments and some of the existing are also outdated
    // TODO: write tests
    // TODO: implement an output writer and an input reader to be able to write a sudoku to a file and also read it later on
    public static void main(String[] args)
    {
        int difficulty = askForDifficulty();
        Bot bot = new Bot();
        Generator g = new Generator();
        Board board = g.generateBoard(difficulty, bot);
        //bot.getSolution(board, 1);
        //System.out.println(Bot.solutionBoards.get(0));
        play(board, bot);
    }

    private static void play(Board b, Bot bot)
    {
        bot.solutionBoards.clear();
        Board startingBoard = b.copy();
        Rules ruler = new Rules();
        System.out.println(b);
        System.out.println("Gib zuerst die Zeile, dann die Spalte und dann den Wert den du dort setzen möchtest ein.");
        System.out.println("Um ein Feld zu leeren gib 0 als Wert ein.");
        while (!ruler.isBoardFinished(b))
        {
            int line, column, value;
            boolean valid = false;
            do
            {
                String inputLine = scanner.nextLine();
                scanner.reset();
                if (inputLine.toString().contains("solve"))
                {
                    if (bot.getSolution(b, 1) >= 1)
                    {
                        System.out.println(bot.solutionBoards.get(0));
                        return;
                    }
                }
                String inputColumn = scanner.nextLine();
                scanner.reset();
                String inputValue = scanner.nextLine();
                scanner.reset();
                if ((inputLine.length() == 1 && Character.isDigit(inputLine.charAt(0))) && (inputColumn.length() == 1 && Character.isDigit(inputColumn.charAt(0)) && (inputValue.length() == 1 && Character.isDigit(inputValue.charAt(0)))))
                {
                    line = Integer.parseInt(inputLine);
                    column = Integer.parseInt(inputColumn);
                    value = Integer.parseInt(inputValue);
                    if ((line <= 9 && line > 0) && (column <= 9 && column > 0) && (value <= 9 && value >= 0))
                    {
                        if (startingBoard.getField()[column - 1][line - 1] == 0)
                        {
                            b.setField(column - 1, line - 1, value);
                            valid = true;
                        }
                        else
                        {
                            System.out.println("Feld bereits belegt!");
                        }
                    }
                    else
                    {
                        System.out.println("Ungültige Zahl.");
                    }
                }
                else
                {
                    System.out.println("Ungültige Eingabe.");
                }
            } while (!valid);
            System.out.println(b);
        }
    }

    private static int askForDifficulty()
    {
        System.out.println("Hallo, ich bin Sudoku!");
        do
        {
            System.out.println("Wähle deine Schwierigkeitsstufe aus: 1 für leicht, 2 für mittel und 3 für schwer");
            String input = scanner.nextLine();
            scanner.reset();
            int difficulty;
            if (input.length() == 1 && Character.isDigit(input.charAt(0)))
            {
                difficulty = Integer.parseInt(input);
                if (difficulty <= 3 && difficulty >= 1)
                {
                    return difficulty;
                }
                else
                {
                    System.out.print("Ungültige Zahl.");
                }
            }
            else
            {
                System.out.print("Ungültige Eingabe.");
            }
        } while (true);
    }
}
