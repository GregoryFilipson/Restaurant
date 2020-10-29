import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<Guest> guestQueue = new LinkedList<>();
    private final Object lock = new Object();
    private final int COOKING_TIME = 2000;
    private final int TIME_TO_COME_THE_GUEST = 300;
    private final int TIME_FOR_EATING = 1000;
    private final int TIME_BETWEEN_GUESTS = 1100;
    private final int LIMIT_GUESTS = 5;

    public void guestIsHere() {
        synchronized (guestQueue) {
            try {
                Thread.sleep(TIME_BETWEEN_GUESTS);
                System.out.println(Thread.currentThread().getName() + " в ресторане!");
                guestQueue.offer(new Guest());
                guestQueue.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (lock) {
            try {
                lock.wait();
                System.out.println(Thread.currentThread().getName() + " приступил к еде");
                Thread.sleep(TIME_FOR_EATING);
                System.out.println(Thread.currentThread().getName() + " вышел из ресторана.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void waiterIsWorking() {
        System.out.println(Thread.currentThread().getName() + " на работе!");
        while (guestQueue.size() <= LIMIT_GUESTS) {
                synchronized (guestQueue) {
                    if (guestQueue.size() == 0) {
                    try {
                        guestQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(Thread.currentThread().getName() + " взял заказ...");
            try {
                Thread.sleep(COOKING_TIME);
                guestQueue.poll();
                System.out.println(Thread.currentThread().getName() + " несет заказ. Наконец то!");
                Thread.sleep(TIME_TO_COME_THE_GUEST);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                lock.notify();
                }
            if (guestQueue.size()==0) {
                break;
            }
        }
    }
}