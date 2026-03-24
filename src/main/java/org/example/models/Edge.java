package org.example.models;

/**
 * Represents an edge in a graph connecting to {@link Room} objects.
 *
 * <p>An Edge contains:
 * <ul>Source Room</ul>
 * <ul>Destination Room</ul>
 * <ul>Distance between the two rooms measured in pixels.</ul>
 * </p>
 */
public class Edge {
    /**
     * Starting Room of the edge.
     */
    private Room source;
    /**
     * End Room of the edge.
     */
    private Room destination;
    /**
     * Distance between the two rooms, measured in pixels.
     */
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
