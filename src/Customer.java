import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends Person implements Comparable<Customer> {
    private String roomName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalBill;

    public Customer(String name, String phoneNumber, String roomName, LocalDate checkInDate, LocalDate checkOuDate) {
        super(name, phoneNumber);
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOuDate;
        

    }

    // === GETTERS ===
    public String getRoomName() {
        return roomName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalBill() {
        return totalBill;
    }

    // === SETTERS ===
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setTotalBill() {
     ArrayList<Room> rooms = HotelData.getRooms();

        double roomFee = 0.0;
        for (Room room : rooms) {
            if (room.getRoomName().equals(roomName))
                roomFee = room.getRoomFee();
        }
        int nights = 0;
        LocalDate temp = checkInDate;

        while (temp.isBefore(checkOutDate)) {
            nights++;
            temp = temp.plusDays(1);
        }
        this.totalBill = nights * roomFee;

    }

    public void setTotalBill(double roomFee) {

        int nights = 0;
        LocalDate temp = checkInDate;

        while (temp.isBefore(checkOutDate)) {
            nights++;
            temp = temp.plusDays(1);
        }
        this.totalBill = nights * roomFee;

    }

    @Override
    public int compareTo(Customer customer) {
        if ((this.name).equals(customer.name)) {
            return (this.checkInDate).compareTo(customer.checkInDate);
        } else {
            return (this.name).compareTo(customer.name);
        }
    }

}
