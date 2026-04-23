import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class RoomManagementRoot extends StackPane {
    private final int maxRooms = 25;
    private final ArrayList<Room> rooms = HotelData.getRooms();
    private final ArrayList<Customer> customers = HotelData.getCustomers();
    private ListView<String> roomListView;

    public RoomManagementRoot() {
        makeRoot();
    }

    public void makeRoot() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Room Management");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.BLUE);

        Label text = new Label("All Rooms in Hotel De Luna: ");
        text.setFont(Font.font("", 15));

        Button addRoomBtn = new Button("Add Room");
        Button backBtn = new Button("Back");

        // ListView of rooms:
        roomListView = new ListView<>();
        roomListView.setMaxHeight(SceneManager.listViewMaxHeight);
        roomListView.setMaxWidth(SceneManager.listViewMaxWidth);

        refreshRoomList();

        roomListView.setOnMouseClicked(e -> {
            String roomName = roomListView.getSelectionModel().getSelectedItem();
            if (roomName != null) {
                roomClicked(roomName);
            }
        });

        // Add Room button action
        addRoomBtn.setOnAction(e -> {
            if (rooms.size() >= maxRooms) {
                showMaxRoomsError();
            } else {
                showAddRoomScene();
            }
        });

        backBtn.setOnAction(e -> {
            SceneManager.goBack();
        });

        root.getChildren().addAll(title, text, roomListView, addRoomBtn, backBtn);
        getChildren().add(root);
    }

    // Method to refresh the room list
    private void refreshRoomList() {
        roomListView.getItems().clear();
        for (Room room : rooms) {
            roomListView.getItems().add(room.getRoomName());
        }
    }

    private void roomClicked(String roomName) {
        Room roomSelected = null;

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label(roomName + " Details");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        title.setTextFill(Color.CORAL);

        // Find room
        for (Room room : rooms) {
            if (room.getRoomName().equals(roomName)) {
                roomSelected = room;
                break;
            }
        }

        if (roomSelected == null)
            return;

        Label roomDetailsLabel = new Label(
                "Room Name: " + roomSelected.getRoomName() + "\nRoom Fee: $" +
                        String.format("%.2f", roomSelected.getRoomFee()));
        roomDetailsLabel.setFont(Font.font("", 15));

        // Reservations section
        Label reservationsTitle = new Label("Reservations:");
        reservationsTitle.setFont(Font.font("", FontWeight.BOLD, 14));

        ListView<String> reservationsList = new ListView<>();
        reservationsList.setMaxHeight(200);
        reservationsList.setMaxWidth(400);
        reservationsList.setFocusTraversable(false);

        ArrayList<Customer> reservations = HotelData.getCustomersForRoom(roomName);

        if (reservations.isEmpty()) {
            reservationsList.getItems().add("No reservations for this room");
        } else {
            for (Customer reservation : reservations) {
                reservationsList.getItems().add(
                        reservation.getName() + " | " +
                                reservation.getCheckInDate() + " to " +
                                reservation.getCheckOutDate());
            }
        }

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit Room");
        Button deleteBtn = new Button("Delete Room");
        Button backBtn = new Button("Back");

        final Room finalRoom = roomSelected;

        editBtn.setOnAction(e -> showEditRoomScene(finalRoom));
        deleteBtn.setOnAction(e -> showDeleteRoomScene(finalRoom));
        backBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(editBtn, deleteBtn, backBtn);

        root.getChildren().addAll(title, roomDetailsLabel, reservationsTitle,
                reservationsList, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showMaxRoomsError() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Room Limit Reached");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.RED);

        Label message = new Label("Cannot add more rooms.\nMaximum of " + maxRooms + " rooms reached!");
        message.setFont(Font.font("", 14));
        message.setTextAlignment(TextAlignment.CENTER);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> SceneManager.goBack());

        root.getChildren().addAll(title, message, backBtn);
        SceneManager.switchTo(new Scene(root));
    }

    private void showAddRoomScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Add New Room");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField roomNameField = new TextField();
        roomNameField.setPromptText("e.g., Room 101");
        TextField roomFeeField = new TextField();
        roomFeeField.setPromptText("e.g., 150.00");

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Room Name:"), 0, 0);
        grid.add(roomNameField, 1, 0);
        grid.add(new Label("Room Fee:"), 0, 1);
        grid.add(roomFeeField, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String roomName = roomNameField.getText().trim();
            String feeText = roomFeeField.getText().trim();

            if (roomName.isEmpty() || feeText.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            // Check if room name already exists
            for (Room room : rooms) {
                if (room.getRoomName().equalsIgnoreCase(roomName)) {
                    errorLabel.setText("Room with this name already exists!");
                    return;
                }
            }

            try {
                double fee = Double.parseDouble(feeText);
                if (fee <= 0) {
                    errorLabel.setText("Room fee must be positive!");
                    return;
                }

                Room newRoom = new Room(roomName, fee);
                rooms.add(newRoom);
                // update csv file:
                HotelData.updateRooms(rooms);
                refreshRoomList();
                showSuccessScene("Room " + roomName + " has been added successfully!");

            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid room fee! Please enter a valid number.");
            }
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showEditRoomScene(Room room) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Edit Room");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.BLUE);

        Label subtitle = new Label("Edit room details for " + room.getRoomName());
        subtitle.setFont(Font.font("", 14));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField roomNameField = new TextField(room.getRoomName());
        TextField roomFeeField = new TextField(String.valueOf(room.getRoomFee()));

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("", FontWeight.BOLD, 12));

        grid.add(new Label("Room Name:"), 0, 0);
        grid.add(roomNameField, 1, 0);
        grid.add(new Label("Room Fee:"), 0, 1);
        grid.add(roomFeeField, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String newName = roomNameField.getText().trim();
            String feeText = roomFeeField.getText().trim();

            if (newName.isEmpty() || feeText.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            // Check if new name conflicts with existing room (except current room)
            for (Room r : rooms) {
                if (!r.getRoomName().equals(room.getRoomName()) &&
                        r.getRoomName().equalsIgnoreCase(newName)) {
                    errorLabel.setText("Room with this name already exists!");
                    return;
                }
            }

            try {
                double newFee = Double.parseDouble(feeText);
                if (newFee <= 0) {
                    errorLabel.setText("Room fee must be positive!");
                    return;
                }

                // Update customers who have this room
                for (Customer customer : customers) {
                    if (customer.getRoomName().equals(room.getRoomName())) {
                        customer.setRoomName(newName);
                        customer.setTotalBill(newFee);
                    }
                }

                room.setRoomName(newName);
                room.setRoomFee(newFee);
                
                // update csv file:
                HotelData.updateRooms(rooms);
                HotelData.updateCustomers(customers);
                refreshRoomList();
                showSuccessScene("Room updated successfully!");

            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid room fee! Please enter a valid number.");
            }
        });

        cancelBtn.setOnAction(e -> SceneManager.goBack());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        root.getChildren().addAll(title, subtitle, grid, errorLabel, buttonBox);
        SceneManager.switchTo(new Scene(root));
    }

    private void showDeleteRoomScene(Room room) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Delete Room");
        title.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        title.setTextFill(Color.RED);

        // Check if room has reservations
        ArrayList<Customer> reservations = HotelData.getCustomersForRoom(room.getRoomName());

        if (!reservations.isEmpty()) {
            Label errorMessage = new Label("Cannot Delete Room");
            errorMessage.setFont(Font.font("", FontWeight.BOLD, 16));
            errorMessage.setTextFill(Color.RED);

            Label detailMessage = new Label(
                    "Room " + room.getRoomName() + " has " + reservations.size() +
                            " active reservation(s).\nCannot delete room with existing reservations!");
            detailMessage.setFont(Font.font("", 14));
            detailMessage.setTextAlignment(TextAlignment.CENTER);

            Button backBtn = new Button("Back");
            backBtn.setOnAction(e -> SceneManager.goBack());

            root.getChildren().addAll(title, errorMessage, detailMessage, backBtn);
        } else {
            Label confirmMessage = new Label("Are you sure you want to delete:");
            confirmMessage.setFont(Font.font("", 14));

            Label roomInfo = new Label(room.getRoomName() + "\n(Fee: $" +
                    String.format("%.2f", room.getRoomFee()) + ")");
            roomInfo.setFont(Font.font("", FontWeight.BOLD, 16));
            roomInfo.setTextFill(Color.DARKRED);

            Label warningLabel = new Label("This action cannot be undone!");
            warningLabel.setFont(Font.font("", FontWeight.BOLD, 12));
            warningLabel.setTextFill(Color.RED);

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER);

            Button confirmBtn = new Button("Yes, Delete");
            Button cancelBtn = new Button("Cancel");

            confirmBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

            confirmBtn.setOnAction(e -> {
                rooms.remove(room);
                // update csv file:
                HotelData.updateRooms(rooms);
                refreshRoomList();
                showSuccessScene("Room " + room.getRoomName() + " deleted successfully!");
            });

            cancelBtn.setOnAction(e -> SceneManager.goBack());

            buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
            root.getChildren().addAll(title, confirmMessage, roomInfo, warningLabel, buttonBox);
        }

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