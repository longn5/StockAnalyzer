import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
 * StockPrice Class
 * Class Objective:
 * To update and store stock prices from the web (google finance).
 * Saves data to file and then reread data when Program restarts.
 */

public class StockPrices {
    // Initialize local objects
    WeeklysOptions weeklysOptions = new WeeklysOptions();
    private Map<String, Double> symbolsCurrentPrice = new HashMap<String, Double>();
    private Map<String, ArrayList<Double>> symbols5MinPrice = new HashMap<String, ArrayList<Double>>();
    private Map<String, ArrayList<Double>> symbols1HourPrice = new HashMap<String, ArrayList<Double>>();

    //*********************************************************
    //Constructor BEGINS
    //*********************************************************

    /*The constructor for the StockPrice class will read from local
    files to load up various timeframe dataset into the program to
    be further manipulate by the calculation class.
     */

    StockPrices() {
        File file = new File("src/Current_Price.txt");          // load up current price data
        File fiveMinPrice = new File("src/5Min_Price.txt");     // load up 5 min price data
        File oneHourPrice = new File("src/1Hour_Price.txt");    // load up 1 hour price data
        BufferedReader reader = null;

        // reads from file (Current_Price.txt)
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {

                String delims = " ";                        // delimiter variable
                String[] splitText = text.split(delims);    // splits the symbol from the price

                // we know the symbol is the first word in array and price is second
                symbolsCurrentPrice.put(splitText[0], Double.parseDouble(splitText[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reads from file (5Min_Price.txt)
        try {
            reader = new BufferedReader(new FileReader(fiveMinPrice));
            String text = null;

            while ((text = reader.readLine()) != null) {
                ArrayList<Double> quoteList = new ArrayList<Double>();

                String delims = " ";
                String[] splitText = text.split(delims);

                // variable i is at one because index 0 is where the symbol is located
                for (int i = 1; i < splitText.length; i++) {
                    quoteList.add(Double.parseDouble(splitText[i]));
                }
                // we know the symbol is the first word in array and price is second
                symbols5MinPrice.put(splitText[0], quoteList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reads from file (1Hour_Price.txt)
        try {
            reader = new BufferedReader(new FileReader(oneHourPrice));
            String text = null;

            while ((text = reader.readLine()) != null) {
                ArrayList<Double> quoteList = new ArrayList<Double>();

                String delims = " ";
                String[] splitText = text.split(delims);

                // variable i is at one because index 0 is where the symbol is located
                for (int i = 1; i < splitText.length; i++) {
                    quoteList.add(Double.parseDouble(splitText[i]));
                }

                // we know the symbol is the first word in array and price is second
                symbols1HourPrice.put(splitText[0], quoteList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //*********************************************************
    //Constructor ENDS
    //*********************************************************

    public Map<String, Double> getSymbolsCurrentPrice() {
        return symbolsCurrentPrice;
    }

    public void setSymbolsCurrentPrice(Map<String, Double> symbolsCurrentPrice) {
        this.symbolsCurrentPrice = symbolsCurrentPrice;
    }

    public Map<String, ArrayList<Double>> getSymbols5MinPrice() {
        return symbols5MinPrice;
    }

    public Map<String, ArrayList<Double>> getSymbols1HourPrice() {
        return symbols1HourPrice;
    }

    //*********************************************************
    //updateCurrentPrice Function BEGINS
    //*********************************************************

    /*This function updates
     */

    boolean updateCurrentPrice() {

        String tempSymbol;
        Double tempPrice = null;
        String tempHTML = null;
        Document doc = null;
        String bodyText;

        for (int i = 0; i < weeklysOptions.getSize(); i++) {
            tempSymbol = weeklysOptions.getStockSymbols(i);

            // html link updates symbol to current price by looking at minute quote
            tempHTML = "http://www.google.com/finance/getprices?q=" + tempSymbol + "&i=60&p=1m&f=c";

            try {
                doc = Jsoup.connect(tempHTML).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bodyText = doc.body().text();

            String delims = " ";
            String[] tokens = bodyText.split(delims);

            for (int j = 0; j < tokens.length; j++) {
                if (Character.isDigit(tokens[j].charAt(0))) {

                    // we know we are returning the current price at 1 min
                    // period with a length of 1 minute delay
                    tempPrice = Double.parseDouble(tokens[j]);
                }
            }

            symbolsCurrentPrice.put(tempSymbol, tempPrice);
        }

        if (symbolsCurrentPrice.size() > 0)
            return true;
        else
            return false;
    }

    //*********************************************************
    //updateCurrentPrice Function BEGINS
    //*********************************************************

    //*********************************************************
    //update5MinPrice Function BEGINS
    //*********************************************************

    boolean update5MinPrice() {
        String tempSymbol;
        String tempHTML = null;
        Document doc = null;
        String bodyText = null;

        for (int i = 0; i < weeklysOptions.getSize(); i++) {
            ArrayList<Double> tempList = new ArrayList<Double>();
            ArrayList<Double> quoteList = new ArrayList<Double>();

            tempSymbol = weeklysOptions.getStockSymbols(i);
            // 300 seconds equals 5 min, 5 day length
            tempHTML = "http://www.google.com/finance/getprices?q=" + tempSymbol + "&i=300&p=5d&f=c";

            try {
                doc = Jsoup.connect(tempHTML).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bodyText = doc.body().text();

            String delims = " ";
            String[] tokens = bodyText.split(delims);

            for (int j = 0; j < tokens.length; j++) {
                if (Character.isDigit(tokens[j].charAt(0))) {

                    // we know we are returning the current price at 1 min
                    // period with a length of 1 minute delay
                    tempList.add(Double.parseDouble(tokens[j]));
                }
            }

            // reverse prices to have most recent at the beginning of list
            for (int k = 0, size = tempList.size() - 1; k < tempList.size(); k++) {
                quoteList.add(tempList.get(size - k));
            }

            symbols5MinPrice.put(tempSymbol, quoteList);
        }

        if (symbols5MinPrice.size() > 0)
            return true;
        else
            return false;
    }

    //*********************************************************
    //update5MinPrice Function ENDS
    //*********************************************************

    //*********************************************************
    //update1HourPrice Function BEGINS
    //*********************************************************

    boolean update1HourPrice() {
        String tempSymbol;
        String tempHTML = null;
        Document doc = null;
        String bodyText = null;

        for (int i = 0; i < weeklysOptions.getSize(); i++) {
            ArrayList<Double> tempList = new ArrayList<Double>();
            ArrayList<Double> quoteList = new ArrayList<Double>();

            tempSymbol = weeklysOptions.getStockSymbols(i);
            // 300 seconds equals 5 min, 5 day length
            tempHTML = "http://www.google.com/finance/getprices?q=" + tempSymbol + "&i=3600&p=45d&f=c";

            try {
                doc = Jsoup.connect(tempHTML).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Cannot get price for: " + tempSymbol);
            }

            bodyText = doc.body().text();

            String delims = " ";
            String[] tokens = bodyText.split(delims);

            for (int j = 0; j < tokens.length; j++) {
                if (Character.isDigit(tokens[j].charAt(0))) {

                    // we know we are returning the current price at 1 min
                    // period with a length of 1 minute delay
                    tempList.add(Double.parseDouble(tokens[j]));
                }
            }
            // quoteList.addAll(tempList);

            // reverse prices to have most recent at the beginning of list
            for (int k = 0, size = tempList.size() - 1; k < tempList.size(); k++) {
                quoteList.add(tempList.get(size - k));
            }

            if (quoteList.size() > 200) {
                symbols1HourPrice.put(tempSymbol, quoteList);
            }
        }
        if (symbols1HourPrice.size() > 0)
            return true;
        else
            return false;
    }

    //*********************************************************
    //update1HourPrice Function ENDS
    //*********************************************************

    //*********************************************************
    //printCurrentPrice Function BEINGS
    //*********************************************************

    public void printCurrentPrice() {
        // Commented out is the loop to print an unsorted hashmap of the symbols
        // and their values
        /*
		 * System.out.println("*******************"); System.out.println(
		 * "Unsorted Symbols: "); System.out.println("*******************");
		 * 
		 * for (String key : symbolsCurrentPrice.keySet()) {
		 * System.out.println(key + ": " + symbolsCurrentPrice.get(key)); }
		 */
        // created a new list to convert hashmap into treemap to sort the
        // symbols values
        Map<String, Double> symbolsSorted = new TreeMap<String, Double>(symbolsCurrentPrice);

        System.out.println("*******************");
        System.out.println("Sorted Symbols: ");
        System.out.println("*******************");

        for (String key : symbolsSorted.keySet()) {
            System.out.println(key + ": " + symbolsSorted.get(key));
        }
    }

    //*********************************************************
    //printCurrentPrice Function ENDS
    //*********************************************************

    //*********************************************************
    //printTest Function BEGINS
    //*********************************************************

    public void printTest() {

        Map<String, ArrayList<Double>> symbolsSorted = new TreeMap<String, ArrayList<Double>>(symbols1HourPrice);
        ArrayList<String> listToSave = new ArrayList<String>();

        for (String key : symbolsSorted.keySet()) {
            int priceListSize = 0;

            for (int i = 0; i < symbolsSorted.get(key).size(); i++) {
                priceListSize++;
            }

            if (priceListSize < 200) {
                System.out.print(key + ": ");
                System.out.println(priceListSize);
            }
        }

        // Loop to filter out symbols less than $20 (DEFAULT, but can
        // change value).
        // Use only when initializing new symbol list
		/*
		 * 
		 * for (String key : symbolsSorted.keySet()) {
		 * 
		 * double currentPrice = 0;
		 * 
		 * currentPrice = symbolsSorted.get(key).get(0);
		 * 
		 * if(currentPrice > 20) { listToSave.add(key); System.out.println(key);
		 * } }
		 * 
		 * try { PrintWriter writer = new PrintWriter("src/NewWeeklyList.csv");
		 * for (int i = 0; i < listToSave.size(); i++) {
		 * writer.println(listToSave.get(i)); }
		 * 
		 * writer.close();
		 * 
		 * } catch (FileNotFoundException e) { e.printStackTrace(); }
		 */

    }

    //*********************************************************
    //printTest Function ENDS
    //*********************************************************

    //*********************************************************
    //saveToFile Function BEINGS
    //*********************************************************

    public boolean saveToFile() {

        try {
            PrintWriter writer = new PrintWriter("src/Current_Price.txt");
            for (String key : symbolsCurrentPrice.keySet()) {
                writer.println(key + " " + symbolsCurrentPrice.get(key));
            }
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //*********************************************************
    //saveToFile Function ENDS
    //*********************************************************

    //*********************************************************
    //save5MinPriceToFile Function BEINGS
    //*********************************************************

    public boolean save5MinPriceToFile() {
        try {
            PrintWriter writer = new PrintWriter("src/5Min_Price.txt");
            for (String key : symbols5MinPrice.keySet()) {
                writer.print(key + " ");

                for (int i = 0; i < symbols5MinPrice.get(key).size(); i++) {
                    writer.print(symbols5MinPrice.get(key).get(i) + " ");
                }
                writer.println("");
            }
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //*********************************************************
    //save5MinPriceToFile Function ENDS
    //*********************************************************

    //*********************************************************
    //save1HourPriceToFile Function BEGINS
    //*********************************************************

    public boolean save1HourPriceToFile() {

        Map<String, ArrayList<Double>> symbolsSorted = new TreeMap<String, ArrayList<Double>>(symbols1HourPrice);

        try {
            PrintWriter writer = new PrintWriter("src/1Hour_Price.txt");
            for (String key : symbolsSorted.keySet()) {
                writer.print(key + " ");

                for (int i = 0; i < symbolsSorted.get(key).size(); i++) {
                    writer.print(symbolsSorted.get(key).get(i) + " ");
                }
                writer.println("");
            }

            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //*********************************************************
    //save1HourPriceToFile Function ENDS
    //*********************************************************
}
