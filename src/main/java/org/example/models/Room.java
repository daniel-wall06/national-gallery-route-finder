package org.example.models;

public class Room {
    private int roomNumber;
    private String roomName;

    public Room(int roomNumber, String roomName) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
