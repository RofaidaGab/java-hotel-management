import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Collections;

public class LoadData {

    public static ArrayList<Employee> loadEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("employees.csv"))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",", -1); // must keep empty parts

                String name = data[0].trim();
                String phone = data[1].trim();

                double salary = 0.0;
                if (!data[2].trim().isEmpty()) {
                    salary = Double.parseDouble(data[2].trim());
                }

                String username = data[3].trim();
                String password = data[4].trim();

                Employee employee = new Employee(name, phone, salary, username, password);

                employees.add(employee);
            }

        } catch (IOException e) {
            System.out.println("employees.csv not found");
        }

        return employees;
    }

    public static ArrayList<Room> loadRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("rooms.csv"))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",", -1); // must keep empty parts

                String roomName = data[0].trim();

                double roomFee = Double.parseDouble(data[1].trim());

                Room room = new Room(roomName, roomFee);

                rooms.add(room);
            }

        } catch (IOException e) {
            System.out.println("rooms.csv not found");
        }

        return rooms;
    }

    public static ArrayList<Customer> loadCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customers.csv"))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",", -1); // must keep empty parts

                String name = data[0].trim();
                String phone = data[1].trim();
                String roomName = data[2].trim();
                LocalDate checkInDate = LocalDate.parse(data[3].trim());
                LocalDate checkOutDate = LocalDate.parse(data[4].trim());

                Customer customer = new Customer(name, phone, roomName, checkInDate, checkOutDate);

                customers.add(customer);
            }
        

        } catch (IOException e) {
            System.out.println("customers.csv not found");
        }
        Collections.sort(customers);
        return customers;
    }

    public static void writeToEmployees(ArrayList<Employee> employees) {
        try (PrintWriter pw = new PrintWriter("employees.csv")) {
            pw.println("name,phoneNumber,salary,username,password");
            for (Employee employee : employees) {
                pw.printf("%s,%s,%.2f,%s,%s\n", employee.getName(), employee.getPhoneNumber(), employee.getSalary(),
                        employee.getUsername(), employee.getPassword());
            }
        } catch (IOException e) {
            System.out.println("employees.csv not found");
        }
    }

    public static void writeToCustomers(ArrayList<Customer> customers) {
        try (PrintWriter pw = new PrintWriter("customers.csv")) {
            pw.println("name,phoneNumber,roomName,checkInDate,checkOutDate");

            for (Customer customer : customers) {
                pw.printf("%s,%s,%s,%s,%s\n", customer.getName(), customer.getPhoneNumber(), customer.getRoomName(),
                        customer.getCheckInDate(), customer.getCheckOutDate());
            }
        } catch (IOException e) {
            System.out.println("customers.csv not found");
        }
        Collections.sort(customers);
    }

    public static void writeToRooms(ArrayList<Room> rooms) {
        try (PrintWriter pw = new PrintWriter("rooms.csv")) {
            pw.println("roomName,roomFee");

            for (Room room : rooms) {
                pw.printf("%s,%.2f\n", room.getRoomName(), room.getRoomFee());
            }
        } catch (IOException e) {
            System.out.println("rooms.csv not found");
        }
    }
}
