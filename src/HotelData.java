import java.util.ArrayList;

public class HotelData {

    private static ArrayList<Customer> customers = LoadData.loadCustomers();
    private static ArrayList<Room> rooms = LoadData.loadRooms();
    private static ArrayList<Employee> employees = LoadData.loadEmployees();
    static {
        for (Customer c : customers) {
            c.setTotalBill();
        }
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public static ArrayList<Room> getRooms() {
        return rooms;
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static void updateRooms(ArrayList<Room> newRooms) {
        LoadData.writeToRooms(newRooms);
        rooms = LoadData.loadRooms();  // Reload from CSV to get fresh data
    }
    
    public static void updateCustomers(ArrayList<Customer> newCustomers) {
        LoadData.writeToCustomers(newCustomers);
        customers = LoadData.loadCustomers();  // Reload from CSV
        for (Customer c : customers) {
            c.setTotalBill();
        }
    }
    
    public static void updateEmployees(ArrayList<Employee> newEmployees) {
        LoadData.writeToEmployees(newEmployees);
        employees = LoadData.loadEmployees();  // Reload from CSV
        LoginAgent.giveEmployees(HotelData.getEmployees());    }

    public static ArrayList<Customer> getCustomersForRoom(String roomName) {
        ArrayList<Customer> customersBookedRoom = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getRoomName().equals(roomName)) {
                customersBookedRoom.add(customer);
            }
        }
        return customersBookedRoom;
    }
}
