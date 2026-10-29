package Internship;

import java.io.*;
import java.util.*;

// ---------------- STOCK ----------------
class Stock {
    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void display() {
        System.out.printf("%-10s %-20s $%.2f%n",
                symbol, companyName, price);
    }
}

// ---------------- TRANSACTION ----------------
class Transaction {
    private String type;
    private String symbol;
    private int quantity;
    private double price;
    private Date date;

    public Transaction(String type, String symbol,
                       int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.date = new Date();
    }

    public void display() {
        System.out.printf("%-8s %-8s %-10d $%-10.2f %s%n",
                type, symbol, quantity, price, date);
    }
}

// ---------------- PORTFOLIO ----------------
class Portfolio {
    private HashMap<String, Integer> holdings;
    private ArrayList<Transaction> transactions;

    public Portfolio() {
        holdings = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public void buy(Stock stock, int quantity) {
        holdings.put(
                stock.getSymbol(),
                holdings.getOrDefault(stock.getSymbol(), 0) + quantity
        );

        transactions.add(
                new Transaction(
                        "BUY",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                )
        );
    }

    public boolean sell(Stock stock, int quantity) {
        int currentQuantity =
                holdings.getOrDefault(stock.getSymbol(), 0);

        if (currentQuantity < quantity) {
            return false;
        }

        holdings.put(
                stock.getSymbol(),
                currentQuantity - quantity
        );

        if (holdings.get(stock.getSymbol()) == 0) {
            holdings.remove(stock.getSymbol());
        }

        transactions.add(
                new Transaction(
                        "SELL",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                )
        );

        return true;
    }

    public int getQuantity(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }

    public void displayPortfolio(HashMap<String, Stock> market) {
        System.out.println("\n========== PORTFOLIO ==========");

        if (holdings.isEmpty()) {
            System.out.println("Portfolio is empty.");
            return;
        }

        double totalValue = 0;

        System.out.printf("%-10s %-10s %-15s%n",
                "Symbol", "Quantity", "Value");

        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {

            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Stock stock = market.get(symbol);

            if (stock != null) {
                double value = quantity * stock.getPrice();
                totalValue += value;

                System.out.printf(
                        "%-10s %-10d $%-14.2f%n",
                        symbol,
                        quantity,
                        value
                );
            }
        }

        System.out.println("--------------------------------");
        System.out.printf(
                "Total Portfolio Value: $%.2f%n",
                totalValue
        );
    }

    public void displayTransactions() {
        System.out.println("\n========== TRANSACTIONS ==========");

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.printf(
                "%-8s %-8s %-10s %-12s %s%n",
                "Type", "Symbol", "Quantity", "Price", "Date"
        );

        for (Transaction transaction : transactions) {
            transaction.display();
        }
    }

    public void saveToFile(String filename) {
        try {
            PrintWriter writer = new PrintWriter(
                    new FileWriter(filename)
            );

            for (Map.Entry<String, Integer> entry :
                    holdings.entrySet()) {

                writer.println(
                        entry.getKey() + "," + entry.getValue()
                );
            }

            writer.close();

            System.out.println("Portfolio saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving portfolio.");
        }
    }

    public void loadFromFile(String filename) {
        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(filename));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 2) {
                    holdings.put(
                            data[0],
                            Integer.parseInt(data[1])
                    );
                }
            }

            reader.close();

            System.out.println("Portfolio loaded successfully.");

        } catch (IOException e) {
            System.out.println(
                    "No saved portfolio found."
            );
        }
    }
}

// ---------------- USER ----------------
class User {
    private String name;
    private double cash;
    private Portfolio portfolio;

    public User(String name, double cash) {
        this.name = name;
        this.cash = cash;
        this.portfolio = new Portfolio();
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public double getCash() {
        return cash;
    }

    public void buyStock(Stock stock, int quantity) {

        double cost = stock.getPrice() * quantity;

        if (cash < cost) {
            System.out.println("Insufficient cash.");
            return;
        }

        cash -= cost;

        portfolio.buy(stock, quantity);

        System.out.printf(
                "Bought %d shares of %s for $%.2f%n",
                quantity,
                stock.getSymbol(),
                cost
        );
    }

    public void sellStock(Stock stock, int quantity) {

        int owned =
                portfolio.getQuantity(stock.getSymbol());

        if (owned < quantity) {
            System.out.println(
                    "You do not own enough shares."
            );
            return;
        }

        double revenue = stock.getPrice() * quantity;

        if (portfolio.sell(stock, quantity)) {
            cash += revenue;

            System.out.printf(
                    "Sold %d shares of %s for $%.2f%n",
                    quantity,
                    stock.getSymbol(),
                    revenue
            );
        }
    }

    public void displayAccount(
            HashMap<String, Stock> market) {

        System.out.println("\n========== ACCOUNT ==========");
        System.out.println("User: " + name);
        System.out.printf("Cash: $%.2f%n", cash);

        portfolio.displayPortfolio(market);
    }
}

// ---------------- MARKET ----------------
class Market {

    private HashMap<String, Stock> stocks;

    public Market() {
        stocks = new HashMap<>();

        stocks.put(
                "AAPL",
                new Stock("AAPL", "Apple Inc.", 225.00)
        );

        stocks.put(
                "MSFT",
                new Stock("MSFT", "Microsoft", 510.00)
        );

        stocks.put(
                "GOOG",
                new Stock("GOOG", "Alphabet", 250.00)
        );

        stocks.put(
                "AMZN",
                new Stock("AMZN", "Amazon", 230.00)
        );
    }

    public void displayMarket() {

        System.out.println("\n========== MARKET DATA ==========");

        System.out.printf(
                "%-10s %-20s %s%n",
                "Symbol", "Company", "Price"
        );

        System.out.println("---------------------------------------");

        for (Stock stock : stocks.values()) {
            stock.display();
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public HashMap<String, Stock> getStocks() {
        return stocks;
    }

    // Simulate price movement
    public void updatePrices() {

        Random random = new Random();

        for (Stock stock : stocks.values()) {

            double change =
                    (random.nextDouble() - 0.5) * 10;

            double newPrice =
                    stock.getPrice() + change;

            if (newPrice > 1) {
                stock.setPrice(newPrice);
            }
        }

        System.out.println(
                "Market prices have been updated."
        );
    }
}

// ---------------- MAIN PROGRAM ----------------
public class StockTradingSimulator {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Market market = new Market();

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        User user = new User(name, 10000.00);

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("       STOCK TRADING SYSTEM");
            System.out.println("================================");
            System.out.println("1. Display Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Update Market Prices");
            System.out.println("7. Save Portfolio");
            System.out.println("8. Load Portfolio");
            System.out.println("9. Exit");
            System.out.println("================================");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    market.displayMarket();
                    break;

                case 2:

                    System.out.print(
                            "Enter stock symbol: "
                    );

                    String buySymbol =
                            scanner.next().toUpperCase();

                    Stock buyStock =
                            market.getStock(buySymbol);

                    if (buyStock == null) {
                        System.out.println(
                                "Stock not found."
                        );
                        break;
                    }

                    System.out.print(
                            "Enter quantity: "
                    );

                    int buyQuantity =
                            scanner.nextInt();

                    if (buyQuantity <= 0) {
                        System.out.println(
                                "Invalid quantity."
                        );
                        break;
                    }

                    user.buyStock(
                            buyStock,
                            buyQuantity
                    );

                    break;

                case 3:

                    System.out.print(
                            "Enter stock symbol: "
                    );

                    String sellSymbol =
                            scanner.next().toUpperCase();

                    Stock sellStock =
                            market.getStock(sellSymbol);

                    if (sellStock == null) {
                        System.out.println(
                                "Stock not found."
                        );
                        break;
                    }

                    System.out.print(
                            "Enter quantity: "
                    );

                    int sellQuantity =
                            scanner.nextInt();

                    if (sellQuantity <= 0) {
                        System.out.println(
                                "Invalid quantity."
                        );
                        break;
                    }

                    user.sellStock(
                            sellStock,
                            sellQuantity
                    );

                    break;

                case 4:
                    user.displayAccount(
                            market.getStocks()
                    );
                    break;

                case 5:
                    user.getPortfolio()
                            .displayTransactions();
                    break;

                case 6:
                    market.updatePrices();
                    market.displayMarket();
                    break;

                case 7:
                    user.getPortfolio()
                            .saveToFile("portfolio.txt");
                    break;

                case 8:
                    user.getPortfolio()
                            .loadFromFile("portfolio.txt");
                    break;

                case 9:
                    System.out.println(
                            "Thank you for using the simulator!"
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice."
                    );
            }

        } while (choice != 9);

        scanner.close();
    }
}
