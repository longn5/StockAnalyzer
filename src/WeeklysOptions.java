import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WeeklysOptions {

	private ArrayList<String> stockSymbols = new ArrayList<String>();


    //*********************************************************
    //Constructor BEGINS
    //*********************************************************

	WeeklysOptions() {
		File file = new File("src/NewWeeklyList.csv");
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			while ((text = reader.readLine()) != null) {
				stockSymbols.add(text);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
	}

    //*********************************************************
    //Constructor ENDS
    //*********************************************************

	public String getStockSymbols(int index) {
		return stockSymbols.get(index);
	}

	public void setStockSymbols(ArrayList<String> stockSymbols) {
		this.stockSymbols = stockSymbols;
	}

	public void printList() {
		for (int i = 0; i < stockSymbols.size(); i++) {
			System.out.println(stockSymbols.get(i));
		}
	}

	public int getSize() {
		return stockSymbols.size();
	}
}
