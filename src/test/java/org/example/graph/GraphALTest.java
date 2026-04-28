package org.example.graph;

import org.example.models.Room;
import org.junit.jupiter.api.Test;

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
        graphAL.connectRooms(1,2,100);
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
        graphAL.connectRooms(1,2,100);
        graphAL.connectRooms(2,3,100);
        graphAL.connectRooms(3,4,100);

        List<List<Room>> routes = graphAL.findRoutesDFS(1, 4, 1);
        System.out.println(routes);
        assertFalse(routes.isEmpty());

    }
}