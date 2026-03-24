package org.example.models;

/**
 * Represents a room with a unique name and number.
 */
public class Room {
    //Room Number from map
    private int roomNumber;
    //Room Name from map
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
