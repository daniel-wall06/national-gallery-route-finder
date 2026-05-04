package org.example.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room with a unique name and number.
 */
public class Room {
    //Room Number from map
    private int roomNumber;
    //Room Name from map
    private String roomName;
    //List of artwork
    private List<Artwork> artworks;

    public Room(int roomNumber, String roomName) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.artworks = new ArrayList<>();
    }
    public boolean addArtwork(Artwork artwork) {
        return artworks.add(artwork);
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
    public List<Artwork> getArtworks() {
        return artworks;
    }
    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }
    @Override
    public String toString() {
        return roomName;
    }

    public boolean hasArtistWork(String artist) {
        for(Artwork a : artworks) {
            if(a.getArtist().equalsIgnoreCase(artist)) {
                return true;
            }
        }
        return false;
    }

}
