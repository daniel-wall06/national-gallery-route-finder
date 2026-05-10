package org.example.data;
import org.example.graph.GraphAL;
import org.example.graph.GraphNodeAL;
import org.example.models.Artwork;
import org.example.models.Room;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loads graph data from csv file
 */
public class GalleryDataLoader {

    /**
     * Reads rooms.csv and connections.csv and returns a populated GraphAL.
     * @return a fully loaded GraphAL
     */
    public GraphAL load() {
        GraphAL graph = new GraphAL();
        try {
            InputStream is = GalleryDataLoader.class.getModule().getResourceAsStream("org/example/data/rooms.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.startsWith("roomN") || line.trim().isEmpty() || line.startsWith(",")) continue;
                String[] parts = line.split(",", 4);
                if (parts.length < 2) continue;
                int roomNum = Integer.parseInt(parts[0].trim());
                String roomName = parts[1].trim();
                Room room = new Room(roomNum, roomName);
                if (parts.length >= 4 && !parts[2].trim().isEmpty()) {
                    String title = parts[2].trim();
                    String artist = parts[3].trim();
                    room.addArtwork(new Artwork(title, artist));
                }
                graph.addRoom(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            InputStream is = GalleryDataLoader.class.getModule().getResourceAsStream("org/example/data/connections.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.startsWith("roomN") || line.trim().isEmpty() || line.startsWith(",")) continue;
                String[] parts = line.split(",");
                int room1 = Integer.parseInt(parts[0]);
                int room2 = Integer.parseInt(parts[1]);
                int distance = Integer.parseInt(parts[2]);
                graph.connectRooms(room1, room2, distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

            return graph;
        }


    }


