import java.time.LocalDate;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class CustomerManagementRoot extends StackPane {
    private final ArrayList<Customer> customers = HotelData.getCustomers();
    private final ArrayList<Room> rooms = HotelData.getRooms();
    private ListView<String> customerListView;

    public CustomerManagementRoot() {
        makeRoot();
    }

    public void makeRoot() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Customer Management");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.BLUE);

        Label text = new Label("All Customers in Hotel De Luna: ");
        text.setFont(Font.font("", 15));

        Button addCustomerBtn = new Button("Add Customer");
        Button backBtn = new Button("Back");

        // ListView of customers
        customerListView = new ListView<>();
        customerListView.setMaxHeight(SceneManager.listViewMaxHeight);
        customerListView.setMaxWidth(SceneManager.listViewMaxWidth);

        refreshCustomerList();

        customerListView.setOnMouseClicked(e -> {
            String customerInfo = customerListView.getSelectionModel().getSelectedItem();
            if (customerInfo != null) {
                customerClicked(customerInfo);
            }
        });

        // Add Customer button action
        addCustomerBtn.setOnAction(e -> {
            showAddCustomerScene();
        });

        backBtn.setOnAction(e -> {
            SceneManager.goBack();
        });

        root.getChildren().addAll(title, text, customerListView, addCustomerBtn, backBtn);
        getChildren().add(root);
    }

    private void refreshCustomerList() {
        customerListView.getItems().clear();
        for (Customer customer : customers) {
            customerListView.getItems().add(customer.getName() + " - " + customer.getRoomName());
        }
    }

    private void customerClicked(String customerInfo) {
        String customerName = customerInfo.split(" - ")[0];
        String roomName = customerInfo.split(" - ")[1];
        Customer customerSelected = null;

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label(customerName + " Details");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.CORAL);

        // Find customer - match by name AND room since same name can book different rooms
        for (Customer customer : customers) {
            if (customer.getName().equals(customerName) && customer.getRoomName().equals(roomName)) {
                customerSelected = customer;
                break;
            }
        }

        if (customerSelected == null)
            return;

        Label customerDetailsLabel = new Label(
                "Name: " + customerSelected.getName() +
                "\nPhone Number: " + customerSelected.getPhoneNumber() +
                "\nRoom Name: " + customerSelected.getRoomName() +
                "\nCheck-In Date: " + customerSelected.getCheckInDate() +
                "\nCheck-Out Date: " + customerSelected.getCheckOutDate() +
                "\nTotal Bill: $" + String.format("%.2f", customerSelected.getTotalBill()));
        customerDetailsLabel.setFont(Font.font("", 15));

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit Customer");
        Button deleteBtn = new Button("Delete Customer");
        Button backBtn = new Button("Back");

        final Customer finalCustomer = customerSelected;

        editBtn.setOnAction(e -> showEditCustomerScene(finalCustomer));
        deleteBtn.setOnAction(e -> showDeleteCustomerScene(finalCustomer));
        backBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(editBtn, deleteBtn, backBtn);

        root.getChildren().addAll(title, customerDetailsLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showAddCustomerScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Add New Customer");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., John Doe");
        TextField phoneField = new TextField();
        phoneField.setPromptText("e.g., 123-456-7890");
        
        ComboBox<String> roomCombo = new ComboBox<>();
        for (Room room : rooms) {
            roomCombo.getItems().add(room.getRoomName());
        }
        roomCombo.setPromptText("Select a room");

        DatePicker checkInPicker = new DatePicker();
        DatePicker checkOutPicker = new DatePicker();

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Room:"), 0, 2);
        grid.add(roomCombo, 1, 2);
        grid.add(new Label("Check-In Date:"), 0, 3);
        grid.add(checkInPicker, 1, 3);
        grid.add(new Label("Check-Out Date:"), 0, 4);
        grid.add(checkOutPicker, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String roomName = roomCombo.getValue();
            LocalDate checkIn = checkInPicker.getValue();
            LocalDate checkOut = checkOutPicker.getValue();

            if (name.isEmpty() || phone.isEmpty() || roomName == null || checkIn == null || checkOut == null) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
                errorLabel.setText("Check-in date must be before check-out date!");
                return;
            }

            // Check for room availability
            for (Customer customer : customers) {
                if (customer.getRoomName().equals(roomName)) {
                    boolean overlaps = checkIn.isBefore(customer.getCheckOutDate()) &&
                            checkOut.isAfter(customer.getCheckInDate());
                    if (overlaps) {
                        errorLabel.setText("Room is not available for selected dates!");
                        return;
                    }
                }
            }

            Customer newCustomer = new Customer(name, phone, roomName, checkIn, checkOut);
            newCustomer.setTotalBill(); // Calculate bill based on room and dates
            customers.add(newCustomer);
            HotelData.updateCustomers(customers);
            refreshCustomerList();
            showSuccessScene("Customer " + name + " has been added successfully!");
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showEditCustomerScene(Customer customer) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Edit Customer");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        Label subtitle = new Label("Edit details for " + customer.getName());
        subtitle.setFont(Font.font("", 14));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField nameField = new TextField(customer.getName());
        TextField phoneField = new TextField(customer.getPhoneNumber());
        
        ComboBox<String> roomCombo = new ComboBox<>();
        for (Room room : rooms) {
            roomCombo.getItems().add(room.getRoomName());
        }
        roomCombo.setValue(customer.getRoomName());

        DatePicker checkInPicker = new DatePicker(customer.getCheckInDate());
        DatePicker checkOutPicker = new DatePicker(customer.getCheckOutDate());

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Room:"), 0, 2);
        grid.add(roomCombo, 1, 2);
        grid.add(new Label("Check-In Date:"), 0, 3);
        grid.add(checkInPicker, 1, 3);
        grid.add(new Label("Check-Out Date:"), 0, 4);
        grid.add(checkOutPicker, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String newName = nameField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String newRoomName = roomCombo.getValue();
            LocalDate newCheckIn = checkInPicker.getValue();
            LocalDate newCheckOut = checkOutPicker.getValue();

            if (newName.isEmpty() || newPhone.isEmpty() || newRoomName == null || 
                newCheckIn == null || newCheckOut == null) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            if (newCheckIn.isAfter(newCheckOut) || newCheckIn.isEqual(newCheckOut)) {
                errorLabel.setText("Check-in date must be before check-out date!");
                return;
            }

            // Check for room availability (excluding current customer)
            for (Customer c : customers) {
                if (c != customer && c.getRoomName().equals(newRoomName)) {
                    boolean overlaps = newCheckIn.isBefore(c.getCheckOutDate()) &&
                            newCheckOut.isAfter(c.getCheckInDate());
                    if (overlaps) {
                        errorLabel.setText("Room is not available for selected dates!");
                        return;
                    }
                }
            }

            // Update customer
            customer.setName(newName);
            customer.setPhoneNumber(newPhone);
            customer.setRoomName(newRoomName);
            customer.setCheckInDate(newCheckIn);
            customer.setCheckOutDate(newCheckOut);
            customer.setTotalBill(); // Recalculate bill

            HotelData.updateCustomers(customers);
            refreshCustomerList();
            showSuccessScene("Customer updated successfully!");
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, subtitle, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showDeleteCustomerScene(Customer customer) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Delete Customer");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.RED);

        Label confirmMessage = new Label("Are you sure you want to delete:");
        confirmMessage.setFont(Font.font("", 14));

        Label customerInfo = new Label(customer.getName() + "\n" + customer.getRoomName() + 
                "\n(" + customer.getCheckInDate() + " to " + customer.getCheckOutDate() + ")");
        customerInfo.setFont(Font.font("", FontWeight.BOLD, 16));
        customerInfo.setTextFill(Color.DARKRED);
        customerInfo.setTextAlignment(TextAlignment.CENTER);

        Label warningLabel = new Label("This action cannot be undone!");
        warningLabel.setFont(Font.font("", FontWeight.BOLD, 12));
        warningLabel.setTextFill(Color.RED);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Yes, Delete");
        Button cancelBtn = new Button("Cancel");

        confirmBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

        confirmBtn.setOnAction(e -> {
            customers.remove(customer);
            HotelData.updateCustomers(customers);
            refreshCustomerList();
            showSuccessScene("Customer " + customer.getName() + " deleted successfully!");
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
        root.getChildren().addAll(title, confirmMessage, customerInfo, warningLabel, buttonBox);

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
            // Go back twice to return to home page
            SceneManager.goBack();
            SceneManager.goBack();
            
            // Refresh the home page room grid
            SceneManager.refreshHomePage();
        });

        root.getChildren().addAll(title, messageLabel, okBtn);
        SceneManager.switchToWithoutHistory(new Scene(root));
    }
}