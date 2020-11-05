import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<Guest> guestQueue = new LinkedList<>();
    private final Queue<Order> ordersQueue = new LinkedList<>();
    private final Queue<Order> ordersOnKitchen = new LinkedList<>();
    private final Object kitchen = new Object();
    private final Object order = new Object();

    private final int COOKING_TIME = 1000;
    private final int TIME_TO_COME_THE_GUEST = 300;
    private final int TIME_FOR_EATING = 1000;
    private final int TIME_BETWEEN_GUESTS = 1100;


    public void guestIsHere() {
        guestQueue.offer(new Guest());
        System.out.println(Thread.currentThread().getName() + " пришел в ресторан!");
        try {
            Thread.sleep(TIME_BETWEEN_GUESTS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        synchronized (guestQueue) {
            guestQueue.notify(); //будит официанта чтобы он пришел
        }

        synchronized (order) {
            try {
                order.wait(); //ждет пока официант примет заказ
                System.out.println(Thread.currentThread().getName() + " сделал " + ordersQueue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        guestQueue.poll(); //удаляем гостя

        synchronized (kitchen) {
            try {
                kitchen.wait(COOKING_TIME); // ждем пока кухня приготовит
                System.out.println(Thread.currentThread().getName() + " приступил к еде, его " + ordersOnKitchen.poll());
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
        System.out.println(Thread.currentThread().getName() + " на работе!");
        while (true) {
            synchronized (guestQueue) {
                if (guestQueue.isEmpty()) {
                    try {
                        guestQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            synchronized (order) {
                if (ordersQueue.isEmpty()) { //если список заказов пуст
                    if (!guestQueue.isEmpty()) { //если есть еще люди
                        ordersQueue.offer(new Order()); //принимаем заказ
                        System.out.println(Thread.currentThread().getName() + " принимает " +
                                ordersQueue.element());
                        ordersOnKitchen.offer(ordersQueue.element());
                        order.notify(); //пробуждаем посетителя для того чтобы получить подтверждение
                    }
                }
            }

            synchronized (kitchen) {
                if (!ordersOnKitchen.isEmpty()) { //если этот заказ не удален
                    try {
                        Thread.sleep(COOKING_TIME);
                        System.out.println(Thread.currentThread().getName() + " несет "
                                + ordersOnKitchen.element() + " Наконец то!");
                        Thread.sleep(TIME_TO_COME_THE_GUEST);
                        kitchen.notify(); //пробуждаем посетителя
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (guestQueue.isEmpty()) {
                break;
            } else {
                ordersQueue.poll(); //удаляем заказ из списка заказов официанта
            }
        }
    }
}