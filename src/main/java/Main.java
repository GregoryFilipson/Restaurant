public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        new Thread(null, restaurant::waiterIsWorking, "Официант Дмитрий").start();
        new Thread(null, restaurant::waiterIsWorking, "Официант Сэм").start();
        new Thread(null, restaurant::guestIsHere, "Посетитель Арсений").start();
        new Thread(null, restaurant::guestIsHere, "Посетитель Елена").start();
        new Thread(null, restaurant::guestIsHere, "Посетитель Геннадий").start();
        new Thread(null, restaurant::guestIsHere, "Посетитель Григорий").start();
        new Thread(null, restaurant::guestIsHere, "Посетитель Ирина").start();
    }
}