import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class EmployeeManagementRoot extends StackPane {
    private final ArrayList<Employee> employees = HotelData.getEmployees();
    private ListView<String> employeeListView;

    public EmployeeManagementRoot() {
        makeRoot();
    }

    public void makeRoot() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Employee Management");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.BLUE);

        Label text = new Label("All Employees in Hotel De Luna: ");
        text.setFont(Font.font("", 15));

        Button addEmployeeBtn = new Button("Add Employee");
        Button backBtn = new Button("Back");

        // ListView of employees
        employeeListView = new ListView<>();
        employeeListView.setMaxHeight(SceneManager.listViewMaxHeight);
        employeeListView.setMaxWidth(SceneManager.listViewMaxWidth);

        refreshEmployeeList();

        employeeListView.setOnMouseClicked(e -> {
            String employeeName = employeeListView.getSelectionModel().getSelectedItem();
            if (employeeName != null) {
                employeeClicked(employeeName);
            }
        });

        // Add Employee button action
        addEmployeeBtn.setOnAction(e -> {
            showAddEmployeeScene();
        });

        backBtn.setOnAction(e -> {
            SceneManager.goBack();
        });

        root.getChildren().addAll(title, text, employeeListView, addEmployeeBtn, backBtn);
        getChildren().add(root);
    }

    private void refreshEmployeeList() {
        employeeListView.getItems().clear();
        for (Employee employee : employees) {
            employeeListView.getItems().add(employee.getName());
        }
    }

    private void employeeClicked(String employeeName) {
        Employee employeeSelected = null;

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label(employeeName + " Details");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.CORAL);
        Label errLabel = new Label("");
        errLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        errLabel.setTextFill(Color.RED);
        // Find employee
        for (Employee employee : employees) {
            if (employee.getName().equals(employeeName)) {
                employeeSelected = employee;
                break;
            }
        }

        if (employeeSelected == null)
            return;

        Label employeeDetailsLabel = new Label(
                "Name: " + employeeSelected.getName() +
                        "\nPhone Number: " + employeeSelected.getPhoneNumber() +
                        "\nSalary: $" + String.format("%.2f", employeeSelected.getSalary()) +
                        "\nUsername: " + employeeSelected.getUsername());
        employeeDetailsLabel.setFont(Font.font("", 15));

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit Employee");
        Button deleteBtn = new Button("Delete Employee");
        Button backBtn = new Button("Back");

        final Employee finalEmployee = employeeSelected;
        Employee currentEmployee = LoginAgent.getCurrentEmployee();
        editBtn.setOnAction(e -> {
            if (currentEmployee.equals(finalEmployee) || currentEmployee.getName().equals("admin"))
                showEditEmployeeScene(finalEmployee);
            else {
                errLabel.setText("You Can Only Edit Your Data\nYou are Not an Admin!");
            }
        });
        deleteBtn.setOnAction(e -> {
            // Check if the logged-in user is the admin
            if (currentEmployee.getName().equals("admin")) {
                showDeleteEmployeeScene(finalEmployee);
            } else {
                // Display the error 
                errLabel.setText("Access Denied: Only Admin can delete employees!");
            }
        });
        backBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(editBtn, deleteBtn, backBtn);

        root.getChildren().addAll(title, employeeDetailsLabel, buttonBox, errLabel);
        SceneManager.switchTo(new Scene(root));
    }

    private void showAddEmployeeScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Add New Employee");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Jane Smith");
        TextField phoneField = new TextField();
        phoneField.setPromptText("e.g., 123-456-7890");
        TextField salaryField = new TextField();
        salaryField.setPromptText("e.g., 3000.00");
        TextField usernameField = new TextField();
        usernameField.setPromptText("e.g., jsmith");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String salaryText = salaryField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (name.isEmpty() || phone.isEmpty() || salaryText.isEmpty() ||
                    username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            // Check if username already exists
            for (Employee emp : employees) {
                if (emp.getUsername().equalsIgnoreCase(username)) {
                    errorLabel.setText("Username already exists!");
                    return;
                }
            }

            try {
                double salary = Double.parseDouble(salaryText);
                if (salary <= 0) {
                    errorLabel.setText("Salary must be positive!");
                    return;
                }

                Employee newEmployee = new Employee(name, phone, salary, username, password);
                employees.add(newEmployee);
                HotelData.updateEmployees(employees);
                refreshEmployeeList();
                showSuccessScene("Employee " + name + " has been added successfully!");

            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid salary! Please enter a valid number.");
            }
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showEditEmployeeScene(Employee employee) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Edit Employee");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        Label subtitle = new Label("Edit details for " + employee.getName());
        subtitle.setFont(Font.font("", 14));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField nameField = new TextField(employee.getName());
        TextField phoneField = new TextField(employee.getPhoneNumber());
        TextField salaryField = new TextField(String.valueOf(employee.getSalary()));
        TextField usernameField = new TextField(employee.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Leave blank to keep current password");

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("New Password:"), 0, 4);
        grid.add(passwordField, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String newName = nameField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String salaryText = salaryField.getText().trim();
            String newUsername = usernameField.getText().trim();
            String newPassword = passwordField.getText();

            if (newName.isEmpty() || newPhone.isEmpty() || salaryText.isEmpty() || newUsername.isEmpty()) {
                errorLabel.setText("Please fill in all required fields!");
                return;
            }

            // Check if new username conflicts with existing employee (except current
            // employee)
            for (Employee emp : employees) {
                if (!emp.getUsername().equals(employee.getUsername()) &&
                        emp.getUsername().equalsIgnoreCase(newUsername)) {
                    errorLabel.setText("Username already exists!");
                    return;
                }
            }

            try {
                double newSalary = Double.parseDouble(salaryText);
                if (newSalary <= 0) {
                    errorLabel.setText("Salary must be positive!");
                    return;
                }

                employee.setName(newName);
                employee.setPhoneNumber(newPhone);
                employee.setSalary(newSalary);
                employee.setUsername(newUsername);

                // Only update password if a new one was entered
                if (!newPassword.isEmpty()) {
                    employee.setPassword(newPassword);
                }

                HotelData.updateEmployees(employees);
                refreshEmployeeList();
                showSuccessScene("Employee updated successfully!");

            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid salary! Please enter a valid number.");
            }
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, subtitle, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showDeleteEmployeeScene(Employee employee) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Delete Employee");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.RED);

        Label confirmMessage = new Label("Are you sure you want to delete:");
        confirmMessage.setFont(Font.font("", 14));

        Label employeeInfo = new Label(employee.getName() + "\n" + employee.getUsername() +
                "\n(Salary: $" + String.format("%.2f", employee.getSalary()) + ")");
        employeeInfo.setFont(Font.font("", FontWeight.BOLD, 16));
        employeeInfo.setTextFill(Color.DARKRED);
        employeeInfo.setTextAlignment(TextAlignment.CENTER);

        Label warningLabel = new Label("This action cannot be undone!");
        warningLabel.setFont(Font.font("", FontWeight.BOLD, 12));
        warningLabel.setTextFill(Color.RED);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Yes, Delete");
        Button cancelBtn = new Button("Cancel");

        confirmBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

        confirmBtn.setOnAction(e -> {
            employees.remove(employee);
            HotelData.updateEmployees(employees);
            refreshEmployeeList();
            showSuccessScene("Employee " + employee.getName() + " deleted successfully!");
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
        root.getChildren().addAll(title, confirmMessage, employeeInfo, warningLabel, buttonBox);

        SceneManager.switchTo(new Scene(root));
    }

    private void showSuccessScene(String message) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Success!");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.GREEN);

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("", 14));
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        Button okBtn = new Button("OK");
        okBtn.setOnAction(e -> {
            // Go back twice
            SceneManager.goBack();
            SceneManager.goBack();
        });

        root.getChildren().addAll(title, messageLabel, okBtn);
        SceneManager.switchToWithoutHistory(new Scene(root));
    }
}