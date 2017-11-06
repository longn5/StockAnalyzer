import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * Calculation Class
 * Class Objective:
 * To calculate various technical analysis indicators from
 * data collected from external file and/or from the web.
 * Takes the calculations and filters matching stock symbols,
 * then display the results to console and also saves to an
 * external file as a csv to be compatiable with various
 * charting web services.
 */

public class Calculation
{
	//*********************************************************
	//calculateMinPrice Function STARTS
	//*********************************************************

    /*this function filters out and displays the symbols that
    // is under the threshold of the user defined minimum price*/

	public void calculateMinPrice(StockPrices userStockPrices, int userMinPrice) 
	{
		for (String key : userStockPrices.getSymbolsCurrentPrice().keySet()) 
		{
			if (userStockPrices.getSymbolsCurrentPrice().get(key) >= userMinPrice) 
			{
				System.out.println(key + ": " + userStockPrices.getSymbolsCurrentPrice().get(key));
			}
		}
	}

	//*********************************************************
	//calculateMinPrice Function ENDS
	//*********************************************************

    //*********************************************************
    //movingAverage Function BEGINS
    //*********************************************************

    /*This function takes in a user defined period (e.g 20 hour)
    and takes the average of the closing price of the user defined
    period.
    */

	public void movingAverage(StockPrices userStockPrices, int period, int length)
    {
		Map<String, ArrayList<Double>> symbolsSorted = new TreeMap<String, ArrayList<Double>>(
				userStockPrices.getSymbols5MinPrice());

		try
        {
			for (String key : symbolsSorted.keySet())
			{
				System.out.print(key + ": "); // prints key before looping through the ArrayList of values for the key

				for (int i = 0; i < length; i++)
				{
					double periodAvg = 0;

					for (int j = 0 + i; j < period + i; j++)
					{
						periodAvg += symbolsSorted.get(key).get(j);
					}

					periodAvg = periodAvg / period;
					periodAvg = Math.round(periodAvg * 100);
					periodAvg = periodAvg / 100;

					System.out.print(periodAvg + " ");
				}

				System.out.println();
			}
		} catch (IndexOutOfBoundsException e)
        {
			System.out.println("Not enough values.");
		}
	}

    //*********************************************************
    //movingAverage Function ENDS
    //*********************************************************

    //*********************************************************
    //exponentialMovingAveage Function BEGINS
    //*********************************************************

    /*This function takes in a user defined period (e.g 20 hour)
    and takes the EPONENTIAL average of the closing price of
    the user defined period.
    */

	public void exponentialMovingAverage(StockPrices userStockPrices, int period, int length)
	{
		Map<String, ArrayList<Double>> symbolsSorted = new TreeMap<String, ArrayList<Double>>(
				userStockPrices.getSymbols5MinPrice());

		try
        {
			for (String key : symbolsSorted.keySet())
			{
				System.out.print(key + ": "); // prints key before looping through the ArrayList of values for the key

				for (int i = 0; i < length; i++) {
					// calculate SMA begin
					double movingAvg = 0;

					for (int j = 0 + i; j < period + i; j++)
					{
						movingAvg += symbolsSorted.get(key).get(j);
					}

					movingAvg = movingAvg / period;
					movingAvg = Math.round(movingAvg * 100);
					movingAvg = movingAvg / 100;
					// calculate SMA end

					// calculate multiplier begin
					double multiplier = 0.0000;
					double period2 = (double) period;
					multiplier = (2 / (period2 + 1));
					// end calculate multiplier end

					// calculate EMA begin
					double eMA = 0;
					double currentPrice = symbolsSorted.get(key).get(0); // index at 0 is current price
					eMA = ((currentPrice - movingAvg) * multiplier + movingAvg);
					// end calculate EMA end

					System.out.print(eMA + " ");
				}
				System.out.println();
			}
		} catch (IndexOutOfBoundsException e)
        {
			System.out.println("Not enough values. (From exponentialMovingAverage function)");
		}
	}

    //*********************************************************
    //exponentialMovingAveage Function ENDS
    //*********************************************************

    //*********************************************************
    //near200EMA Function BEGINS
    //*********************************************************

    /*This function locates and display the stock symbols which
    are close the 200 EMA line and displays to console (also saves
    a local copy for the user to use it somewhere else.
    */

	public void near200EMA(StockPrices userStockPrices, int timeFrame, int period, int length, int bullishBearish) {

		Map<String, ArrayList<Double>> symbolsSorted = new TreeMap<String, ArrayList<Double>>();
		
		if (timeFrame == 1)         //for 5 minute timeframe
		{
			symbolsSorted = userStockPrices.getSymbols5MinPrice();
		}
		else if (timeFrame == 2)    //for one hour timeframe
		{
			symbolsSorted = userStockPrices.getSymbols1HourPrice();
		}

		ArrayList<String> near200EMAList = new ArrayList<String>();

		try
        {
			for (String key : symbolsSorted.keySet()) {

				double eMA = 0;
				double currentPrice = symbolsSorted.get(key).get(0); // index at 0 is the current price

				for (int i = 0; i < length; i++)
				{
				    // calculate SMA
					double movingAvg = 0;

					for (int j = 0 + i; j < period + i; j++)
					{
						movingAvg += symbolsSorted.get(key).get(j);
					}

					movingAvg = movingAvg / period;
					movingAvg = Math.round(movingAvg * 100);
					movingAvg = movingAvg / 100;
					// end calculate SMA

					// calculate multiplier
					double multiplier = 0.0000;

					double period2 = (double) period;

					multiplier = (2 / (period2 + 1));
					// end calculate multiplier

					// calculate EMA
					eMA = ((currentPrice - movingAvg) * multiplier + movingAvg);

				}

				double near200EMA = 0;

				// get percentage of price is near the EMA
				near200EMA = (currentPrice - eMA) / eMA;

				//user input for dissplay bullish or bearish values
				switch (bullishBearish)
                {
                    case 1:
                        if (near200EMA < 0.02 && near200EMA > 0)
                        {
                            System.out.println(key);
                            near200EMAList.add(key);
                        }
                        break;
                    case 2:
                        if (near200EMA > -0.02 && near200EMA < 0)
                        {
                            System.out.println(key);
                            near200EMAList.add(key);
                        }
                        break;
				}
			}
		} catch (IndexOutOfBoundsException e)
        {
			System.out.println(e + "Not enough values. (from near200EMA function) ");
		}

		boolean printToFileError = false;
		//Save match search symbols that matches the user's requirements and save to
        //a csv file to upload to a graphical stock chart program.1
		try
        {
			PrintWriter writer = new PrintWriter("src/near200EMA.csv");
			for (int i = 0; i < near200EMAList.size(); i++)
			{
				writer.println(near200EMAList.get(i));
			}
			writer.close();
		} catch (FileNotFoundException e)
        {
			e.printStackTrace();
			printToFileError = true;
		}

		if (printToFileError == false)
		{
			System.out.println("List saved for upload to StockCharts.com.");
		}
	}

    //*********************************************************
    //near200EMA Function ENDS
    //*********************************************************
}
