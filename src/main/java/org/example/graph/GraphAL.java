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
     *
     * @param room the room to add
     */
    public void addRoom(Room room) {
        GraphNodeAL<Room> node = new GraphNodeAL<>(room);
        nodes.put(room.getRoomNumber(), node);
    }

    /**
     * Connects two rooms with an undirected weighted edge.
     *
     * @param roomNum1 first room number
     * @param roomNum2 second room number
     * @param distance pixel distance between the rooms
     */
    public void connectRooms(int roomNum1, int roomNum2, int distance) {
        GraphNodeAL<Room> node1 = nodes.get(roomNum1);
        GraphNodeAL<Room> node2 = nodes.get(roomNum2);
        if (node1 != null && node2 != null) {
            node1.connectToNodeUndirected(node2, distance);
        }
    }

    public GraphNodeAL<Room> getNode(int roomNumber) {
        return nodes.get(roomNumber);
    }

    public Collection<GraphNodeAL<Room>> getAllNodes() {
        return nodes.values();
    }

    /**
     * Finds multiple routes between two rooms using depth-first search.
     *
     * @param startRoom starting room number
     * @param destRoom  destination room number
     * @param maxRoutes maximum number of routes to return
     * @return list of routes, each route being a list of rooms
     */
    public List<List<Room>> findRoutesDFS(int startRoom, int destRoom, int maxRoutes, List<Integer> avoidRooms) {
        GraphNodeAL<Room> startNode = nodes.get(startRoom);
        GraphNodeAL<Room> destNode = nodes.get(destRoom);
        List<List<Room>> routes = new ArrayList<>();
        List<Room> currentRoute = new ArrayList<>();
        if (startNode == null || destNode == null) return routes;
        currentRoute.add(startNode.getData());
        dfsHelper(startNode, destNode, currentRoute, routes, maxRoutes, avoidRooms);
        return routes;
    }

    private void dfsHelper(GraphNodeAL<Room> current, GraphNodeAL<Room> destination, List<Room> currentRoute, List<List<Room>> routes, int maxRoutes, List<Integer> avoidRooms) {
        if (routes.size() >= maxRoutes) return;
        if (current.equals(destination)) {
            routes.add(new ArrayList<>(currentRoute));
            return;
        }
        current.setVisited(true);
        for (GraphNodeAL<Room> neighbour : current.getAdjList()) {
            if (!neighbour.isVisited() && !avoidRooms.contains(neighbour.getData().getRoomNumber())) {
                currentRoute.add(neighbour.getData());
                dfsHelper(neighbour, destination, currentRoute, routes, maxRoutes, avoidRooms);
                currentRoute.remove(currentRoute.size() - 1);
            }
        }
        current.setVisited(false);
    }

    /**
     * Finds the shortest route based on the distances between rooms.
     *
     * @param startRoom - starting room number.
     * @param destRoom  - destination room number.
     * @return a list containing the shortest route found by the algorithm.
     */
    public List<Room> findShortestRouteDijkstra(int startRoom, int destRoom, List<Integer> avoidRooms) {
        GraphNodeAL<Room> startNode = nodes.get(startRoom);
        GraphNodeAL<Room> destNode = nodes.get(destRoom);
        if (startNode == null || destNode == null) return null;
        List<GraphNodeAL<Room>> encountered = new ArrayList<>();
        List<GraphNodeAL<Room>> unencountered = new ArrayList<>();
        Map<GraphNodeAL<Room>, Integer> distances = new HashMap<>();
        for (GraphNodeAL<Room> node : nodes.values()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        unencountered.add(startNode);
        GraphNodeAL<Room> currentNode = startNode;
        do {
            currentNode = unencountered.remove(0);
            encountered.add(currentNode);
            if (currentNode.getData().equals(destNode.getData())) {
                List<Room> path = new ArrayList<>();
                path.add(0, currentNode.getData());
                GraphNodeAL<Room> traceNode = currentNode;
                while (traceNode != startNode) {
                    for (GraphNodeAL<Room> n : encountered) {
                        for (int i = 0; i < n.getAdjList().size(); i++) {
                            if (n.getAdjList().get(i) == traceNode &&
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
            for (int i = 0; i < currentNode.getAdjList().size(); i++) {
                GraphNodeAL<Room> neighbour = currentNode.getAdjList().get(i);
                if (!encountered.contains(neighbour) && !avoidRooms.contains(neighbour.getData().getRoomNumber())) {
                    int newDist = distances.get(currentNode) + currentNode.getDistance().get(i);
                    if (newDist < distances.get(neighbour)) {
                        distances.put(neighbour, newDist);
                    }
                    if (!unencountered.contains(neighbour)) {
                        unencountered.add(neighbour);
                    }
                }
            }
            unencountered.sort(Comparator.comparingInt(distances::get));
        } while (!unencountered.isEmpty());

        return null;
    }

    /**
     * Finds the most interesting route between rooms using Dijkstra's algorithm
     * Rooms that contain artwork by an artist in the provided list are given a discount to the distance
     *
     * @param startRoom starting room number
     * @param destRoom  destination room number
     * @param artists   list of artists the visitor is interested in
     * @return list of rooms on the most interesting route, or null if no path found
     */
    public List<Room> findMostInterestingRoute(int startRoom, int destRoom, List<String> artists, List<Integer> avoidRooms) {
        GraphNodeAL<Room> startNode = nodes.get(startRoom);
        GraphNodeAL<Room> destNode = nodes.get(destRoom);
        if (startNode == null || destNode == null) return null;
        List<GraphNodeAL<Room>> encountered = new ArrayList<>();
        List<GraphNodeAL<Room>> unencountered = new ArrayList<>();
        Map<GraphNodeAL<Room>, Integer> distances = new HashMap<>();
        Map<GraphNodeAL<Room>, GraphNodeAL<Room>> prev = new HashMap<>();
        for (GraphNodeAL<Room> node : nodes.values()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        unencountered.add(startNode);
        GraphNodeAL<Room> currentNode = startNode;
        do {
            currentNode = unencountered.remove(0);
            encountered.add(currentNode);
            if (currentNode.getData().equals(destNode.getData())) {
                List<Room> path = new ArrayList<>();
                GraphNodeAL<Room> traceNode = destNode;
                while (traceNode != null) {
                    path.add(0, traceNode.getData());
                    traceNode = prev.get(traceNode);
                }
                return path;

            }
            for (int i = 0; i < currentNode.getAdjList().size(); i++) {
                GraphNodeAL<Room> neighbour = currentNode.getAdjList().get(i);
                if(!encountered.contains(neighbour) && !avoidRooms.contains(neighbour.getData().getRoomNumber())) {
                    int newDist = distances.get(currentNode) + currentNode.getDistance().get(i);
                    for (String artist : artists) {
                        if (neighbour.getData().hasArtistWork(artist)) {
                            newDist -= 50;
                            break;

                        }
                    }
                    newDist = Math.max(1, newDist);

                    if (newDist < distances.get(neighbour)) {
                        distances.put(neighbour, newDist);
                        prev.put(neighbour, currentNode);
                    }
                    if (!unencountered.contains(neighbour)) {
                        unencountered.add(neighbour);
                    }
                }
            }
            unencountered.sort(Comparator.comparingInt(distances::get));
        } while (!unencountered.isEmpty());

        return null;

    }

    public List<Room> findShortestRouteWithWaypoints(int startRoom, int destRoom, List<Integer> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) {
            return findShortestRouteDijkstra(startRoom, destRoom, new ArrayList<>());
        }
        List<Integer> sequence = new ArrayList<>();
        sequence.add(startRoom);
        sequence.addAll(waypoints);
        sequence.add(destRoom);

        List<Room> fullRoute = new ArrayList<>();
        for (int i = 0; i < sequence.size() - 1; i++) {
            List<Room> segment = findShortestRouteDijkstra(sequence.get(i), sequence.get(i + 1), new ArrayList<>());
            if (segment == null) return new ArrayList<>();
            if (i == 0) {
                fullRoute.addAll(segment);
            } else {
                fullRoute.addAll(segment.subList(1, segment.size()));
            }
        }
        return fullRoute;
    }

    public List<List<Room>> findRoutesDFSWithWaypoints(int startRoom, int destRoom, int maxRoutes, List<Integer> waypoints, List<Integer> avoidRooms) {
        List<Integer> sequence = new ArrayList<>();
        sequence.add(startRoom);
        sequence.addAll(waypoints);
        sequence.add(destRoom);
        List<Room> fullRoute = new ArrayList<>();
        for(int i = 0; i < sequence.size() - 1; i++) {
            List<List<Room>> segments = findRoutesDFS(sequence.get(i), sequence.get(i + 1), 1, avoidRooms);
            if(segments.isEmpty()) return new ArrayList<>();
            List<Room> segment = segments.get(0);
            if(i == 0) {
                fullRoute.addAll(segment);
            } else {
                fullRoute.addAll(segment.subList(1, segment.size()));
            }
        }
        List<List<Room>> result = new ArrayList<>();
        result.add(fullRoute);
        return result;
    }

}
