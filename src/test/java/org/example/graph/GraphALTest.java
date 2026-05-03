package org.example.graph;

import org.example.models.Artwork;
import org.example.models.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphALTest {

    @Test
    void addRoom() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        assertNotNull(graphAL.getNode(1));

    }

    @Test
    void connectRooms() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        graphAL.connectRooms(1, 2, 100);
        GraphNodeAL<Room> node = graphAL.getNode(1);
        assertFalse(node.getAdjList().isEmpty());

    }

    @Test
    void findRoutesDFS() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        graphAL.addRoom(new Room(3, "Room 3"));
        graphAL.addRoom(new Room(4, "Room 4"));
        graphAL.connectRooms(1, 2, 100);
        graphAL.connectRooms(2, 3, 100);
        graphAL.connectRooms(3, 4, 100);
        graphAL.connectRooms(1, 4, 150);
        graphAL.connectRooms(2, 4, 150);

        List<List<Room>> routes = graphAL.findRoutesDFS(1, 4, 3);
        System.out.println("--- All Possible Routes ---");
        for (List<Room> path : routes) {
            System.out.println(path);
            System.out.println("------------------------------------------------------------------------------");
        }
        assertFalse(routes.isEmpty());

    }

    @Test
    void findRouteDijkstra() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        graphAL.addRoom(new Room(3, "Room 3"));
        graphAL.addRoom(new Room(4, "Room 4"));
        graphAL.connectRooms(1, 2, 100);
        graphAL.connectRooms(2, 3, 100);
        graphAL.connectRooms(3, 4, 100);
        graphAL.connectRooms(1, 4, 150);
        graphAL.connectRooms(2, 4, 150);
        graphAL.connectRooms(3, 4, 150);
        graphAL.connectRooms(1, 4, 150);

        List<Room> route = graphAL.findShortestRouteDijkstra(1, 4, new ArrayList<>());
        System.out.println(route);
        assertNotNull(route);
        assertEquals(2, route.size());

    }

    @Test
    void nullDijkstra() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        List<Room> route = graphAL.findShortestRouteDijkstra(1, 2, new ArrayList<>());
        System.out.println(route);
        assertNull(route);
    }

    @Test
    void dijkstraStartDest() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));

        List<Room> route = graphAL.findShortestRouteDijkstra(1, 1, new ArrayList<>());
        System.out.println(route);
        assertNotNull(route);
    }

    @Test
    void dijkstraRoomDoesntExist() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));

        List<Room> route = graphAL.findShortestRouteDijkstra(1, 2, new ArrayList<>());
        System.out.println(route);
        assertNull(route);
    }

    @Test
    void mostInterestingRoute() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        Room room2 = new Room(2, "Room 2");
        room2.addArtwork(new Artwork("Sunflowers", "Van Gogh"));
        graphAL.addRoom(room2);
        graphAL.addRoom(new Room(3, "Room 3"));
        graphAL.addRoom(new Room(4, "Room 4"));
        graphAL.addRoom(new Room(5, "Room 5"));
        graphAL.addRoom(new Room(6, "Room 6"));

        graphAL.connectRooms(1, 2, 100);
        graphAL.connectRooms(2, 3, 100);
        graphAL.connectRooms(3, 4, 100);
        graphAL.connectRooms(1, 4, 300);


        List<Room> route = graphAL.findMostInterestingRoute(1, 4, List.of("Van Gogh"));
        for (Room room : route) {
            System.out.println(room);
        }
        assertNotNull(route);
        assertTrue(route.contains(room2));

    }

    @Test
    void findShortestRouteWithWaypoints() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        graphAL.addRoom(new Room(3, "Room 3"));
        graphAL.addRoom(new Room(4, "Room 4"));
        graphAL.connectRooms(1, 2, 100);
        graphAL.connectRooms(2, 3, 100);
        graphAL.connectRooms(3, 4, 100);
        graphAL.connectRooms(1, 4, 50);

        List<Room> route = graphAL.findShortestRouteWithWaypoints(1, 4, List.of(2));
        System.out.println(route);
        assertNotNull(route);
        assertTrue(route.contains(graphAL.getNode(2).getData()));
    }

    @Test
    void findShortestRouteAvoidingRoom() {
        GraphAL graphAL = new GraphAL();
        graphAL.addRoom(new Room(1, "Room 1"));
        graphAL.addRoom(new Room(2, "Room 2"));
        graphAL.addRoom(new Room(3, "Room 3"));
        graphAL.addRoom(new Room(4, "Room 4"));
        graphAL.addRoom(new Room(5, "Room 5"));

        graphAL.connectRooms(1, 2, 100);
        graphAL.connectRooms(2, 3, 100);
        graphAL.connectRooms(3, 4, 100);
        graphAL.connectRooms(4,5,100);
        graphAL.connectRooms(1, 3, 150);

        List<Room> route = graphAL.findShortestRouteDijkstra(1, 5, List.of(2));
        System.out.println(route);
        assertNotNull(route);
        assertFalse(route.contains(graphAL.getNode(2).getData()));
    }
}