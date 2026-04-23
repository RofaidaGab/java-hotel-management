
public class Employee extends Person implements ILoginable {

    private String username;
    private String password;
    private double salary;
    private boolean loggedIn;

    @Override
    public boolean login(String username, String password) {
        if (this.username.equals(username) &&
                this.password.equals(password)) {

            loggedIn = true;
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        loggedIn = false;
    }

    public Employee() {

    }

    public Employee(String name, String phoneNumber, double salary, String username, String password) {
        super(name, phoneNumber);
        this.username = username;
        this.password = password;
        this.salary = salary;
    }

    // === GETTERS ===
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getSalary() {
        return salary;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    // === SETTERS ===
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

}
