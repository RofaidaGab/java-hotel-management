import java.time.LocalDate;
import java.util.*;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class HomePageRoot extends StackPane {

    private ArrayList<Room> rooms = HotelData.getRooms();
    private ArrayList<Customer> customers = HotelData.getCustomers();

    //These are made instance variables so they can be accessed by the refreshDisplay()
    private GridPane roomGrid;
    private DatePicker checkInPicker;
    private DatePicker checkOutPicker;
    private Label avLabel;

    public HomePageRoot() {
        makeRoot();
        SceneManager.setHomePageRoot(this); // THIS IS THE HOMEPAGEROOT
    }

    private void makeRoot() {

        Label welcome = new Label("Welcome admin!");
        avLabel = new Label("");
        avLabel.setTextFill(Color.BLACK);
        avLabel.setFont(Font.font("", FontWeight.BOLD, 12));
        welcome.setTextFill(Color.DARKGREEN);
        welcome.setFont(Font.font("", FontWeight.BOLD, 14));
        Employee currentEmployee = LoginAgent.getCurrentEmployee();
        if (!currentEmployee.getName().equals("admin")) {
            welcome.setText("Welcome " + currentEmployee.getName() + "!");
        }

        // ===== ROOM GRID =====
        roomGrid = new GridPane();
        roomGrid.setHgap(10);
        roomGrid.setVgap(10);
        roomGrid.setAlignment(Pos.CENTER);

        // ===== CONTROLS =====
        checkInPicker = new DatePicker(); // date pickers
        checkOutPicker = new DatePicker();
        // === Buttons ====
        Button checkBtn = new Button("Check availability");
        Button logOutBtn = new Button("Log Out");
        Button mngRoomsBtn = new Button("Manage Rooms");
        Button mngEmpsBtn = new Button("Manage Employees");
        Button mngCustsBtn = new Button("Manage Customers");
        Label dateErrorLabel = new Label(""); // Initially empty until an error occurs
        dateErrorLabel.setTextFill(Color.RED);
        dateErrorLabel.setFont(Font.font("", FontWeight.BOLD, 12));
        // Buttons Hbox
        HBox btnHbox = new HBox(mngRoomsBtn, mngCustsBtn,
                mngEmpsBtn);
        btnHbox.setSpacing(25);
        btnHbox.setPadding(new Insets(10));
        btnHbox.setAlignment(Pos.CENTER);

        // ===== CONTROL Grid =====

        GridPane controlGrid = new GridPane();
        controlGrid.setHgap(10);
        controlGrid.setVgap(10);
        controlGrid.setPadding(new Insets(5));
        controlGrid.setAlignment(Pos.CENTER);

        controlGrid.add(new Label("Enter The Stay Period:"), 0, 0, 2, 1);
        controlGrid.add(new Label("From:"), 0, 1);
        controlGrid.add(checkInPicker, 1, 1);
        controlGrid.add(new Label("To:"), 2, 1);
        controlGrid.add(checkOutPicker, 3, 1);

        // ===== BUTTON ACTION =====

        // CheckButton
        checkBtn.setOnAction(e -> {
            LocalDate checkIn = checkInPicker.getValue();
            LocalDate checkOut = checkOutPicker.getValue();

            if (checkIn == null || checkOut == null) {
                dateErrorLabel.setText("Please select both dates");
                return;
            }

            if (checkIn.isAfter(checkOut)) {
                dateErrorLabel.setText("Check-In Date Cannot Be After Check-Out Date!");
                return;
            }

            dateErrorLabel.setText("");
            avLabel.setText("The available rooms are:");
            roomGrid.getChildren().clear();
            makeRoomRectangles(roomGrid, checkIn, checkOut);
        });
        // LogoutButton:
        logOutBtn.setOnAction(e -> {
            SceneManager.clearHistory(); // Clear history on logout
            SceneManager.switchToWithoutHistory(new Scene(new LoginRoot()));
        });

        // RoomManagementButton:
        mngRoomsBtn.setOnAction(e -> {
            SceneManager.switchTo(new Scene(new RoomManagementRoot()));
        });
        // CustomerManagementButton:
        mngCustsBtn.setOnAction(e -> {
            SceneManager.switchTo(new Scene(new CustomerManagementRoot()));
        });
        // EmployeeManagementButton:
        mngEmpsBtn.setOnAction(e -> {
            SceneManager.switchTo(new Scene(new EmployeeManagementRoot()));
        });
        // ===== ROOT =====
        VBox root = new VBox(10, welcome, avLabel, roomGrid, controlGrid, checkBtn, dateErrorLabel, btnHbox,
                logOutBtn);
        root.setAlignment(Pos.CENTER);

        getChildren().add(root);
    }

    public void refreshDisplay() {
        // Reload data from HotelData
        rooms = HotelData.getRooms();
        customers = HotelData.getCustomers();

        // If dates are selected, refresh the grid with the same dates
        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();

        if (checkIn != null && checkOut != null && !checkIn.isAfter(checkOut)) {
            avLabel.setText("The available rooms are:");
            roomGrid.getChildren().clear();
            makeRoomRectangles(roomGrid, checkIn, checkOut);
        }
    }

    private void makeRoomRectangles(GridPane pane, LocalDate checkInDate, LocalDate checkOutDate) {

        int row = 0;
        int col = 0;

        for (Room room : rooms) {

            Rectangle rectangle = new Rectangle(60, 60);
            rectangle.setFill(Color.GREEN);
            rectangle.setStroke(Color.BLACK);

            Text name = new Text(room.getRoomName());
            StackPane stack = new StackPane(rectangle, name);

            // color logic:
            for (Customer customer : customers) {
                if (customer.getRoomName().equals(room.getRoomName())) {

                    boolean overlaps = checkInDate.isBefore(customer.getCheckOutDate()) &&
                            checkOutDate.isAfter(customer.getCheckInDate());

                    boolean fullyInside = !checkInDate.isBefore(customer.getCheckInDate()) &&
                            !checkOutDate.isAfter(customer.getCheckOutDate());

                    if (overlaps) {
                        if (fullyInside) {
                            rectangle.setFill(Color.RED);
                            break;
                        } else {
                            rectangle.setFill(Color.ORANGE);
                        }

                    }

                }
            }

            pane.add(stack, col, row);

            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
            stack.setOnMouseEntered(e -> stack.setStyle("-fx-cursor: hand;"));

            stack.setOnMouseClicked(e -> {
                rectanglClicked(room);
            });
        }
    }

    private void rectanglClicked(Room room) {

        VBox custList = new VBox(10);
        custList.setPrefSize(SceneManager.maxWidth, SceneManager.maxHeight);
        custList.setAlignment(Pos.CENTER);
        custList.setPadding(new Insets(20));

        Label titleLabel = new Label("Bookings for " + room.getRoomName());
        titleLabel.setFont(Font.font("", FontWeight.BOLD, 16));

        ArrayList<Customer> customerBookedRoom = HotelData.getCustomersForRoom(room.getRoomName());
        Collections.sort(customerBookedRoom);
        ListView<String> listView = new ListView<>();

        if (customerBookedRoom.isEmpty()) {
            listView.getItems().add("No bookings for this room");
        } else {
            for (Customer customer : customerBookedRoom) {
                listView.getItems().add(customer.getName() + " - " +
                        customer.getCheckInDate() + " to " + customer.getCheckOutDate());
            }

            listView.setOnMouseClicked(e -> {
                if (listView.getSelectionModel().getSelectedItem() != null) {
                    customerClicked(listView.getSelectionModel().getSelectedItem());
                }
            });
        }

        listView.setMaxHeight(SceneManager.listViewMaxHeight);
        listView.setMaxWidth(SceneManager.listViewMaxWidth);

        Button backButton = new Button("Back to Room Grid");
        backButton.setOnAction(e -> {
            SceneManager.goBack(); // Go back to home
        });

        custList.getChildren().addAll(titleLabel, listView, backButton);
        SceneManager.switchTo(new Scene(custList));
    }

    // customer is Clicked:
    private void customerClicked(String customerRecord) {
        String[] customerData = customerRecord.split(" - ");
        String name = customerData[0];
        String checkIn = customerData[1].split(" to ")[0];

        for (Customer customer : customers) {
            if ((customer.getName()).equals(name)
                    && customer.getCheckInDate().equals(LocalDate.parse(checkIn.trim()))) {

                Label titleLabel = new Label("Customer " + customer.getName() + " Info:");
                titleLabel.setFont(Font.font("", FontWeight.BOLD, 16));

                VBox listBox = new VBox(titleLabel,
                        new Label("Name: " + customer.getName()),
                        new Label("Phone Number: " + customer.getPhoneNumber()),
                        new Label("Room Name: " + customer.getRoomName()),
                        new Label("Check-In Date: " + customer.getCheckInDate().toString()),
                        new Label("Check-Out Date: " + customer.getCheckOutDate().toString()),
                        new Label("Total Bill: " + customer.getTotalBill()));
                listBox.setSpacing(10);
                listBox.setPadding(new Insets(15));
                listBox.setAlignment(Pos.CENTER);

                // go back a scene
                Button backButton = new Button("Back to Customers List");
                backButton.setOnAction(e -> {
                    SceneManager.goBack();
                });

                listBox.getChildren().add(backButton);
                SceneManager.switchTo(new Scene(listBox));
                break; // Exit loop when customer found
            }
        }
    }

}