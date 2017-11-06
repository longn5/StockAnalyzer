public class driver {

	private static WeeklysOptions userOptions = new WeeklysOptions();
	private static StockPrices userStockPrices = new StockPrices();
	private static UserIO userIO = new UserIO();

	public static void main(String[] args) {

		int userSelection;

		//prints the main menu in console
		userIO.mainMenu();

		while ((userSelection = userIO.userMainMenuIO()) != 99) {
			
			userIO.mainMenuSelection(userSelection);
			userIO.mainMenu();
		}
	}
}
