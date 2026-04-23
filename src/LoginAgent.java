import java.util.ArrayList;

public class LoginAgent {
    private static Employee currentEmployee;
    private static ArrayList<Employee> employees = new ArrayList<>();

    public static void giveEmployees(ArrayList<Employee> list) {
        employees = list;
    }

    public static boolean validate(String username, String password) {
        // check for admin
        if (username.equals("admin") && password.equals("admin")) {
     
            currentEmployee = new Employee("admin", "##########", 0.0,"admin", "admin");
            return true;
        }

        // Standard check for everyone else
        if (employees != null) {
            for (Employee e : employees) {
                if (e.login(username, password)) {
                    currentEmployee = e;
                    return true;
                }
            }
        }

        return false;
    }

    public static Employee getCurrentEmployee() {
        return currentEmployee;
    }
}