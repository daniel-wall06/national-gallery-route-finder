package org.example.graph;

import org.example.models.Room;

import java.util.*;

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

    /**
     * Finds the shortest route based on the distances between rooms.
     * @param startRoom - starting room number.
     * @param destRoom - destination room number.
     *
     * @return a list containing the shortest route found by the algorithm.
     */
    public List<Room> findShortestRouteDijkstra(int startRoom, int destRoom) {
        GraphNodeAL<Room> startNode = nodes.get(startRoom);
        GraphNodeAL<Room> destNode = nodes.get(destRoom);
        if(startNode == null || destNode == null) return null;
        List<GraphNodeAL<Room>> encountered = new ArrayList<>();
        List<GraphNodeAL<Room>> unencountered = new ArrayList<>();
        Map<GraphNodeAL<Room>, Integer> distances = new HashMap<>();
        for(GraphNodeAL<Room> node : nodes.values()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        unencountered.add(startNode);
        GraphNodeAL<Room> currentNode = startNode;
        do{
            currentNode = unencountered.remove(0);
            encountered.add(currentNode);
            if(currentNode.getData().equals(destNode.getData())){
                List<Room> path = new ArrayList<>();
                path.add(0, currentNode.getData());
                GraphNodeAL<Room> traceNode = currentNode;
                while(traceNode != startNode) {
                    for(GraphNodeAL<Room> n : encountered) {
                        for(int i = 0; i < n.getAdjList().size(); i++) {
                            if(n.getAdjList().get(i) == traceNode &&
                                    distances.get(traceNode) - n.getDistance().get(i) == distances.get(n)) {
                                path.add(0, n.getData());
                                traceNode = n;
                                break;
                            }
                        }
                    }
                }
                return path;

            }
            for(int i = 0; i < currentNode.getAdjList().size(); i++) {
                GraphNodeAL<Room> neighbour = currentNode.getAdjList().get(i);
                if(!encountered.contains(neighbour)) {
                    int newDist = distances.get(currentNode) + currentNode.getDistance().get(i);
                    if(newDist < distances.get(neighbour)) {
                        distances.put(neighbour, newDist);
                    }
                    if(!unencountered.contains(neighbour)) {
                        unencountered.add(neighbour);
                    }
                }
            }
            unencountered.sort(Comparator.comparingInt(distances::get));
        } while(!unencountered.isEmpty());

        return null;
    }
}
