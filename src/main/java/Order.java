public class Order {
    private String clientName;
    private String waiterName;
    private boolean isOrderExecuted = false;

    public Order(String clientName) {
        this.clientName = clientName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean isOrderExecuted() {
        return isOrderExecuted;
    }

    public void setOrderExecuted(boolean orderExecuted) {
        isOrderExecuted = orderExecuted;
    }

    @Override
    public String toString() {
        return clientName;
    }
}
