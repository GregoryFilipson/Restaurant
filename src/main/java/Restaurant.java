import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<Guest> guestQueue = new LinkedList<>();
    private final Queue<Order> ordersQueue = new LinkedList<>();
    private final Kitchen kitchen = new Kitchen();
    private final int COOKING_TIME = 1000;
    private final int TIME_TO_COME_THE_GUEST = 300;
    private final int TIME_FOR_EATING = 1000;
    private final int TIME_BETWEEN_GUESTS = 1100;


    public void guestIsHere() {
        System.out.println(Thread.currentThread().getName() + " пришел в ресторан!");
        guestQueue.offer(new Guest());
        try {
            Thread.sleep(TIME_BETWEEN_GUESTS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        synchronized (guestQueue) {
            guestQueue.notify(); //будит официанта чтобы он пришел
        }

        synchronized (ordersQueue) {
            try {
                ordersQueue.wait(); //ждет пока официант примет заказ
                System.out.println(Thread.currentThread().getName() + " сделал заказ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        guestQueue.poll(); //удаляем гостя

        synchronized (kitchen) {
            try {
                kitchen.wait(COOKING_TIME); // ждем пока кухня приготовит
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " приступил к еде");

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
            synchronized (ordersQueue) {
                if (ordersQueue.isEmpty()) { //если список заказов пуст
                    if (!guestQueue.isEmpty()) { //если есть еще люди
                        ordersQueue.offer(new Order()); //принимаем заказ
                        System.out.println(Thread.currentThread().getName() + " принимает заказ...");
                        ordersQueue.notify(); //пробуждаем посетителя для того чтобы получить подтверждение
                    }
                }
            }

            synchronized (kitchen) {
                if (!ordersQueue.isEmpty()) { //если этот заказ не удален
                    try {
                        Thread.sleep(COOKING_TIME);
                        System.out.println(Thread.currentThread().getName() + " несет заказ. Наконец то!");
                        Thread.sleep(TIME_TO_COME_THE_GUEST);
                        kitchen.notify(); //пробуждаем посетителя
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (guestQueue.isEmpty()) {
                break;
            }
            else {
                ordersQueue.poll(); //удаляем заказ из списка заказов официанта
            }
        }
    }
}