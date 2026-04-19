package org.example.graph;

import org.example.models.Room;

import java.util.HashMap;
import java.util.Map;

public class GraphAL {
    private Map<Integer, GraphNodeAL<Room>> nodes = new HashMap<>();

    public void addRoom(Room room) {
        GraphNodeAL<Room> node = new GraphNodeAL<>(room);
        nodes.put(room.getRoomNumber(), node);
    }
    public void connectRooms(int roomNum1, int roomNum2, int distance){
        GraphNodeAL<Room> node1 = nodes.get(roomNum1);
        GraphNodeAL<Room> node2 = nodes.get(roomNum2);
        if(node1 != null && node2 != null){
            node1.connectToNodeUndirected(node2, distance);
        }
    }
    public GraphNodeAL<Room> getNode(int roomNumber){
        return nodes.get(roomNumber);
    }
}
