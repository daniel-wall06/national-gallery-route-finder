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
            InputStream is = getClass().getResourceAsStream("/org/example/data/rooms.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int roomNum = Integer.parseInt(parts[0]);
                String roomname = parts[1];
                graph.addRoom(new Room(roomNum, roomname));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/org/example/data/connections.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int room1 = Integer.parseInt(parts[0]);
                int room2 = Integer.parseInt(parts[1]);
                int distance = Integer.parseInt(parts[2]);
                graph.connectRooms(room1, room2, distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/org/example/data/artworks.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int roomNum = Integer.parseInt(parts[0]);
                String title = parts[1];
                String artist = parts[2];
                GraphNodeAL<Room> node = graph.getNode(roomNum);
                node.getData().addArtwork(new Artwork(title, artist));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


            return graph;
        }


    }


