import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<Guest> guestQueue = new LinkedList<>();
    private final int COOKING_TIME = 2000;
    private final int TIME_TO_COME_THE_GUEST = 1500;
    private final int TIME_FOR_EATING = 1000;
    private final int TIME_BETWEEN_GUESTS = 1000;

    public void guestIsHere() {
        synchronized (this) {
            try {
                Thread.sleep(TIME_BETWEEN_GUESTS);
                System.out.println(Thread.currentThread().getName() + " в ресторане!");
                guestQueue.offer(new Guest());
                notify(); //должен быть в конце! Сделать 2 блока синхронизации?
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(COOKING_TIME + TIME_TO_COME_THE_GUEST);
            System.out.println(Thread.currentThread().getName() + " приступил к еде");
            Thread.sleep(TIME_FOR_EATING);
            System.out.println(Thread.currentThread().getName() + " вышел из ресторана.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waiterIsWorking() {
        System.out.println(Thread.currentThread().getName() + " на работе!");
        synchronized (this) {
            if (guestQueue.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println(Thread.currentThread().getName() + " взял заказ...");
                    Thread.sleep(COOKING_TIME);
                    guestQueue.poll();
                    System.out.println(Thread.currentThread().getName() + " несет заказ. Наконец то!");
                    Thread.sleep(TIME_TO_COME_THE_GUEST);
                    notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

