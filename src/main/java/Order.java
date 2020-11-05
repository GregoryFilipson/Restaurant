import java.util.Random;

public class Order {
    Random random = new Random();
    private final String numberOfOrder;

    public Order() {
       numberOfOrder = "заказ №" + random.nextInt(30);
    }

    @Override
    public String toString() {
        return numberOfOrder;
    }
}
