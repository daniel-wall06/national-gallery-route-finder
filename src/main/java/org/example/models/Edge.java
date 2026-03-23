package org.example.models;

public class Edge {
    private Room source;
    private Room destination;
    private int pixelDistance;

    public Edge(Room source, Room destination, int pixelDistance) {
        this.source = source;
        this.destination = destination;
        this.pixelDistance = pixelDistance;
    }
    public Room getSource() {
        return source;
    }
    public void setSource(Room source) {
        this.source = source;
    }
    public Room getDestination() {
        return destination;
    }
    public void setDestination(Room destination) {
        this.destination = destination;
    }
    public int getPixelDistance() {
        return pixelDistance;
    }
    public void setPixelDistance(int pixelDistance) {
        this.pixelDistance = pixelDistance;
    }

}
