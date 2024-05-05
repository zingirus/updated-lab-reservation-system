public class Test {
    public static void main(String[] args) {
        // Create an Admin object
        Admin admin = new Admin("a", 100, 9999);

        // Pass the admin object to the ReservationSystemGUI constructor
        ReservationSystemGUI reservationSystem = new ReservationSystemGUI(admin);
    }
}
