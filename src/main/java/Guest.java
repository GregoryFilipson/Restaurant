public class Guest {
    private final Order order;

    public Guest() {
        this.order = new Order();
    }

    public Order getOrder() {
        return order;
    }
}
