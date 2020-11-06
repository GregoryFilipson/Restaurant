import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<Order> orderQueue = new LinkedList<>();
    private final Object kitchen = new Object();
    private final Object orderSynh = new Object();
    private final int COOKING_TIME = 1000;
    private final int TIME_TO_COME_THE_GUEST = 300;
    private final int TIME_FOR_EATING = 1000;
    private final int TIME_BETWEEN_GUESTS = 1100;
    private final int TIME_BEFORE_RESTAURANT_WILL_BE_CLOSED = 6000;

    public void guestIsHere() {
        try {
            Thread.sleep(TIME_BETWEEN_GUESTS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " пришел в ресторан!");


        synchronized (orderQueue) {
            orderQueue.offer(new Order(Thread.currentThread().getName()));
            orderQueue.notify(); //будит официанта чтобы он пришел
        }

        synchronized (orderSynh) {
            try {
                orderSynh.wait(); //ждет пока официант примет заказ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (kitchen) {
            try {
                kitchen.wait(); // ждем пока кухня приготовит
                System.out.println(Thread.currentThread().getName() + " приступил к еде!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(TIME_FOR_EATING);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " вышел из ресторана.");
    }

    public void waiterIsWorking() {
        Order order = null;
        System.out.println(Thread.currentThread().getName() + " на работе!");
        while (true) {
            synchronized (orderQueue) {
                if (orderQueue.isEmpty()) {
                    try {
                        orderQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized (orderSynh) {
                if (!orderQueue.isEmpty()) { //если есть еще люди в очереди
                    order = orderQueue.poll();
                    order.setWaiterName(Thread.currentThread().getName()); //прописываем официанта в заказе
                    System.out.println(Thread.currentThread().getName() + " принимает заказ у " +
                            order.getClientName());
                    orderSynh.notify(); //пробуждаем посетителя для того чтобы получить подтверждение
                }
            }

            synchronized (kitchen) {
                try {
                    Thread.sleep(COOKING_TIME);
                    kitchen.notify(); //пробуждаем посетителя
                    System.out.println(Thread.currentThread().getName() + " несет заказ для "
                            + order + ". Наконец то!");
                    Thread.sleep(TIME_TO_COME_THE_GUEST);
                    order.setOrderExecuted(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (orderQueue.isEmpty()) {
                try {
                    Thread.sleep(TIME_BEFORE_RESTAURANT_WILL_BE_CLOSED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}