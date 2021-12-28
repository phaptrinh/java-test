import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
    
    private final List<Integer> VALID_NOTES = Arrays.asList(10_000, 20_000, 50_000, 100_000, 200_000);
    private final double WINRATE1 = 0.1;
    private final double WINRATE2 = 0.5;
    private LocalDate currDate;
    private HashMap<String, Integer> selectedProduct;
    private int balance;
    private int cost;
    private int change;
    private Promotion promotion;

    public VendingMachine() {
        currDate = LocalDate.now();
        balance = 0;
        cost = 0;
        change = 0;
        selectedProduct = new HashMap<>();
        promotion = new Promotion(LocalDate.now(), WINRATE1);
    }

    public void reset() { // after a successful transaction (purchase or cancel), reset the machine
        currDate = LocalDate.now();
        balance = 0;
        cost = 0;
        change = 0;
        selectedProduct = new HashMap<>();
        if (!currDate.equals(promotion.getDate())) { // check whether the new day come
            if (promotion.getRemainingBudget() == 0) {
                promotion = new Promotion(currDate, WINRATE1); // reset new Promotion with winrate = 0.1 
            }
            promotion = new Promotion(currDate, WINRATE2); // reset new Promotion with winrate = 0.5
        }
        // System.out.println("Remain: " + promotion.getRemainingBudget());
    }

    public boolean isPromotionApplied() { // check if the transaction is eligible for the promotion
        if (selectedProduct.size() == 0) {
            return false;
        }
        for (String product : selectedProduct.keySet()) { // 3 or greater the same product are okay
            if (selectedProduct.get(product) >= 3) {
                return true;
            }
        }
        return false;
    }

    public void calculateChange() { // calculate the change after deposit or select products
        change = balance - cost;
    }

    public void deposit(int value) { // put notes into the machine
        balance += value;
        calculateChange();
    }

    public void select(int value) { // select and add product to cart
        int quantity;
        if (value == 1) {
            if (change < Product.COKE.getPrice()) {
                System.out.println("[ERR]\tNot enough money!");
                return;
            }
            quantity = selectedProduct.get(Product.COKE.getName()) == null ? 1 : selectedProduct.get(Product.COKE.getName()) + 1;
            selectedProduct.put(Product.COKE.getName(), quantity);
            cost += Product.COKE.getPrice();
        }
        else if (value == 2) {
            if (change < Product.PEPSI.getPrice()) {
                System.out.println("[ERR]\tNot enough money!");
                return;
            }
            quantity = selectedProduct.get(Product.PEPSI.getName()) == null ? 1 : selectedProduct.get(Product.PEPSI.getName()) + 1;
            selectedProduct.put(Product.PEPSI.getName(), quantity);
            cost += Product.PEPSI.getPrice();
        }
        else if (value == 3) {
            if (change < Product.SODA.getPrice()) {
                System.out.println("[ERR]\tNot enough money!");
                return;
            }
            quantity = selectedProduct.get(Product.SODA.getName()) == null ? 1 : selectedProduct.get(Product.SODA.getName()) + 1;
            selectedProduct.put(Product.SODA.getName(), quantity);
            cost += Product.SODA.getPrice();
        }
        System.out.println("[OK]\tSuccessful transaction!");
        calculateChange();
    }

    public void cancel() { // cancel the request and refound 
        if (balance == 0) {
            System.out.println("[ERR]\tYou haven't put any money to the machine!");
        }
        else {
            System.out.println("[OK]\tCancel successfully! Here is your " + balance + " VND");
            reset();
        }
    }

    public void purchase() { // purchase all selected product 
        if (balance == 0) {
            System.out.println("[ERR]\tYou haven't put any money to the machine!");
        }
        else if (cost == 0) {
            System.out.println("[ERR]\tYour cart is empty!");
        }
        else {
            System.out.print("[OK]\tPurchase successfully! Here is your " + selectedProduct);
            System.out.print(change != 0 ? " and please receice your change " + change + " VND\n" : "\n");
            
            if (isPromotionApplied()) {
                if (promotion.isWin()) {
                    System.out.print("\tLUCKY! ");
                    promotion.applyPromotion();
                }
                else {
                    System.out.println("\tBetter luck next time!");
                }
            }

            reset();
        }
    }

    public void printListCommand() {
        System.out.println("All available commands:");
        System.out.println("D - Deposit money to the machine");
        System.out.println("S - Select a product and add to cart");
        System.out.println("C - Cancel the request and receive a refund");
        System.out.println("P - Purchase all product in cart");
        System.out.println("H - Display help command");
        System.out.println("T - Turn off the machine");

    }

    public void run() {
        while (true) {
            System.out.println("==============<MENU>==============");
            System.out.println("Date: " + currDate);
            System.out.println(promotion.getRemainingBudget() != 0 ? "Promotion: Now on with winrate " + (int) (promotion.getWinRate()*100) + "%" : "Promotion: Finished");
            System.out.println("----------------------------------");
            System.out.println("Your balance: " + balance);
            System.out.println("Total cost: " + cost);
            System.out.println("Your change: " + change);
            System.out.println("Your cart: " + selectedProduct);
            System.out.println(isPromotionApplied() ? "Your promotion: Have " + (int) (promotion.getWinRate()*100) + "% change to \n\t\tget a free product" : "Your promotion: No");
            System.out.println("----------------------------------");
            System.out.println("      Please enter a command!     ");
            System.out.println("==================================");
            System.out.print("> ");

            Scanner sc = new Scanner(System.in);
            String cmd = sc.nextLine();
            switch (cmd) {
                case "D":
                    do {
                        try {
                            System.out.print("Enter a note: ");
                            String input = sc.next();
                            if (input.equals("B")) {
                                break;
                            }
                            int value = Integer.parseInt(input);
                            if (VALID_NOTES.contains(value)) {
                                deposit(value);
                                System.out.println("[OK]\tSuccessful transaction!");
                                break;
                            }
                            System.out.println("[ERR]\tPlease enter a valid note or enter B for back to menu!");
                            
                        } catch (Exception e) {
                            System.out.println("[ERR]\tPlease enter a valid note or enter B for back to menu!");
                            sc.nextLine();
                        }
                    } while (true);
                    break;
            
                case "S":
                    do {
                        try {
                            System.out.print("Select a product 1-Coke | 2-Pepsi | 3-Soda: ");
                            String input = sc.next();
                            if (input.equals("B")) {
                                break;
                            }
                            int value = Integer.parseInt(input);
                            if (value == 1 || value == 2 || value == 3) {
                                select(value);
                                break;
                            }
                            System.out.println("[ERR]\tPlease enter a valid number or enter B for back to menu!");
                        } catch (Exception e) {
                            System.out.println("[ERR]\tPlease enter a valid number or enter B for back to menu!");
                            sc.nextLine();
                        }
                    } while (true);
                    break;

                case "C":
                    cancel();
                    break;
                
                case "P":
                    purchase();
                    break;
                
                case "H":
                    printListCommand();
                    break;
                
                case "T":
                    System.out.println("[OK]\tThe machine is turning off\n\tThank you & See you next time!");
                    System.exit(1);

                default:
                    break;
            }
            System.out.println("                *                ");            
        }
    }
}