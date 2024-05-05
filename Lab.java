public class Lab {
    private int number;
    private int capacity;
    private String location;
    private boolean status;
    private Time bookingDate;

    public Lab(int number, int capacity, String location, boolean status) {
        this.number = number;
        this.capacity = capacity;
        this.location = location;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Time getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Time bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        if (!status) {
            return "Lab " + number + " - " + location + " - Capacity: " + capacity + " - Available";
        } else {
            return "Lab " + number + " - " + location + " - Capacity: " + capacity + " - Reserved on: " + bookingDate;
        }
    }

}
