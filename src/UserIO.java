import java.util.Scanner;

public class UserIO {

    private static WeeklysOptions userOptions = new WeeklysOptions();
    private static StockPrices userStockPrices = new StockPrices();
    private static Calculation userCalculation = new Calculation();

    private static Scanner userInput = new Scanner(System.in);

    //*********************************************************
    //mainMenu function BEGINS
    //*********************************************************

    public void mainMenu() {
        System.out.println("*******************************");
        System.out.println("Long's Personal Stock Analyzer");
        System.out.println("1) Show Symbols");
        System.out.println("2) Show Prices");
        System.out.println("3) Update Prices");
        System.out.println("4) Update Prices(5min)");
        System.out.println("5) Update Prices(1Hour)");
        System.out.println("6) Save to file");
        System.out.println("7) Min Price");
        System.out.println("8) Print Test");
        System.out.println("9) Print Test MA");
        System.out.println("10) Print Test EMA");
        System.out.println("11) Find Symbol Price Close to EMA");
        System.out.println("99) Quit.");
        System.out.println("*******************************");
    }

    //*********************************************************
    //mainMenu function ENDS
    //*********************************************************

    //*********************************************************
    //userMainMenuIO function BEGINS
    //*********************************************************

    public int userMainMenuIO() {

        int num;

        System.out.println("Make a selection: ");
        num = userInput.nextInt();

        return num;
    }

    //*********************************************************
    //userMainMenuIO function ENDS
    //*********************************************************

    //*********************************************************
    //mainMenuSelection function BEGINS
    //*********************************************************

    public void mainMenuSelection(int userMenuInput) {

        int userPeriod = 0;
        int userLength = 0;
        int userBullishBearish = 0;
        int userTimeFrame = 0;

        switch (userMenuInput) {
            case 1:
                userOptions.printList();
                return;
            case 2:
                userStockPrices.printCurrentPrice();
                return;
            case 3:
                System.out.println("Updating prices...");
                if (userStockPrices.updateCurrentPrice()) {
                    System.out.println("Price updated.");
                } else {
                    System.out.println("Price NOT updated.");
                }
                return;
            case 4:
                System.out.println("Updating 5 minute price data...");
                if (userStockPrices.update5MinPrice()) {
                    System.out.println("Price updated.");
                } else {
                    System.out.println("Price NOT updated.");
                }
                return;
            case 5:
                System.out.println("Updating 1 Hour price data...");
                if (userStockPrices.update1HourPrice()) {
                    System.out.println("Price updated.");
                } else {
                    System.out.println("Price NOT updated.");
                }
                return;
            case 6:
                System.out.println("Saving files...");
                if (userStockPrices.saveToFile() && userStockPrices.save5MinPriceToFile()
                        && userStockPrices.save1HourPriceToFile()) {
                    System.out.println("File saved.");
                } else {
                    System.out.println("File NOT saved.");
                }
                return;
            case 7:
                System.out.print("Enter min stock price: ");
                int userNum = userInput.nextInt();
                userCalculation.calculateMinPrice(userStockPrices, userNum);
                return;
            case 8:
                System.out.println("Print Test...");
                userStockPrices.printTest();
                return;
            case 9:

                System.out.print("Enter MA Period: ");
                userPeriod = userInput.nextInt();
                System.out.print("Enter MA Length: ");
                userLength = userInput.nextInt();
                System.out.println("Printing Moving Averages...");
                userCalculation.movingAverage(userStockPrices, userPeriod, userLength);
            case 10:

                System.out.print("Enter EMA Period: ");
                userPeriod = userInput.nextInt();
                System.out.print("Enter EMA Length: ");
                userLength = userInput.nextInt();
                System.out.println("Printing Exponential Moving Averages...");
                userCalculation.exponentialMovingAverage(userStockPrices, userPeriod, userLength);
            case 11:
                System.out.println("EMA Selection Menu. ");
                System.out.println("1) 5 Min.");
                System.out.println("2) 1 Hour");
                System.out.print("Chose timeframe: ");
                userTimeFrame = userInput.nextInt();

                System.out.print("Enter EMA Period: ");
                userPeriod = userInput.nextInt();
                System.out.print("Enter EMA Length (only 1): ");
                userLength = userInput.nextInt();
                System.out.print("Above(1) or Below(2) EMA: ");
                userBullishBearish = userInput.nextInt();
                System.out.println("Finding Symbols close to EMA that is above (or below) EMA...");
                userCalculation.near200EMA(userStockPrices, userTimeFrame, userPeriod, userLength, userBullishBearish);
        }
    }

    //*********************************************************
    //mainMenuSelection function BEGINS
    //*********************************************************
}
