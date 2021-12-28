import java.time.LocalDate;
import java.util.Random;

public class Promotion {
    private final int BUDGET = 50_000;
    private LocalDate date;
    private double winRate;
    private int remainingBudget;

    public Promotion(LocalDate date, double winRate) {
        this.date = date;
        this.winRate = winRate;
        remainingBudget = BUDGET;
    }

    public double getWinRate() {
        return winRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(int remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public boolean isWin() {
        return Math.random() <= 0.1 ? true : false;
        // return true;
    }

    public void applyPromotion() {
        Random random = new Random();
        int randomProduct = getRemainingBudget() > 20_000 ? random.nextInt(3) + 1 : random.nextInt(2) + 1;
        int newRemainingBudget;
        switch (randomProduct) {
            case 3:
                newRemainingBudget = getRemainingBudget() - Product.SODA.getPrice();
                setRemainingBudget(newRemainingBudget);
                System.out.println("You have received a free " + Product.SODA.getName() + "!");
                break;
            case 2:
                System.out.println("You have received a free " + Product.PEPSI.getName() + "!");
            case 1:
                System.out.println("You have received a free " + Product.COKE.getName() + "!");
            default:
                newRemainingBudget = getRemainingBudget() - Product.PEPSI.getPrice();
                setRemainingBudget(newRemainingBudget);
                break;
        }
    }
}
