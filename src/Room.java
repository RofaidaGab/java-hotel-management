

public class Room {
    private String roomName;
    private double roomFee;
    public Room(){}
    public Room(String roomName, double roomFee) {
        this.roomName = roomName;
        this.roomFee = roomFee;
    }

    // === GETTERS ===
    public String getRoomName() {
        return roomName;
    }

    public double getRoomFee() {
        return roomFee;
    }

   
    // === SETTERS ===
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomFee(double roomFee) {
        this.roomFee = roomFee;
    }

   

}
