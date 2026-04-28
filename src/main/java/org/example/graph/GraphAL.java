package org.example.graph;

import org.example.models.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom graph data structure for the Gallery floor plan.
 * Rooms are vertices and doorway connections are weighted edges.
 */
public class GraphAL {
    private Map<Integer, GraphNodeAL<Room>> nodes = new HashMap<>();

    /**
     * Adds a room to the graph.
     * @param room the room to add
     */
    public void addRoom(Room room) {
        GraphNodeAL<Room> node = new GraphNodeAL<>(room);
        nodes.put(room.getRoomNumber(), node);
    }

    /**
     * Connects two rooms with an undirected weighted edge.
     * @param roomNum1 first room number
     * @param roomNum2 second room number
     * @param distance pixel distance between the rooms
     */
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
    /**
     * Finds multiple routes between two rooms using depth-first search.
     * @param startRoom starting room number
     * @param destRoom destination room number
     * @param maxRoutes maximum number of routes to return
     * @return list of routes, each route being a list of rooms
     */
    public List<List<Room>> findRoutesDFS(int startRoom, int destRoom, int maxRoutes) {
        GraphNodeAL<Room> startNode = nodes.get(startRoom);
        GraphNodeAL<Room> destNode = nodes.get(destRoom);
        List<List<Room>> routes = new ArrayList<>();
        List<Room> currentRoute = new ArrayList<>();
        if(startNode == null || destNode == null) return routes;
        currentRoute.add(startNode.getData());
        dfsHelper(startNode, destNode, currentRoute, routes, maxRoutes);
        return routes;
    }
    private void dfsHelper(GraphNodeAL<Room> current, GraphNodeAL<Room> destination, List<Room> currentRoute, List<List<Room>> routes, int maxRoutes) {
        if(routes.size() >= maxRoutes) return;
        if(current.equals(destination)){
            routes.add(new ArrayList<>(currentRoute));
            return;
        }
        current.setVisited(true);
        for(GraphNodeAL<Room> neighbour : current.getAdjList()) {
            if(!neighbour.isVisited()){
                currentRoute.add(neighbour.getData());
                dfsHelper(neighbour, destination, currentRoute, routes, maxRoutes);
                currentRoute.remove(currentRoute.size() - 1);
            }
        }
        current.setVisited(false);
    }
}
