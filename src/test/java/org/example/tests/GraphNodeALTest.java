package org.example.tests;

import org.example.graph.GraphNodeAL;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphNodeALTest {

    @org.junit.jupiter.api.Test
    void connectToNodeDirected() {
         GraphNodeAL<Integer> node1 = new GraphNodeAL<>(1);
         GraphNodeAL<Integer> node2 = new GraphNodeAL<>(2);

         node1.connectToNodeDirected(node2,100);

         assertEquals(1, node1.getAdjList() .size());
         assertEquals(node2, node1.getAdjList().getFirst());
         assertEquals(100, node1.getDistance().getFirst());
    }

    @org.junit.jupiter.api.Test
    void connectToNodeUndirected() {
        GraphNodeAL<Integer> node1 = new GraphNodeAL<>(1);
        GraphNodeAL<Integer> node2 = new GraphNodeAL<>(2);

        node2.connectToNodeUndirected(node1,100);

        assertEquals(1, node2.getAdjList().size());
        assertEquals(node1, node2.getAdjList().getFirst());
        assertEquals(100, node2.getDistance().getFirst());

        assertEquals(1, node1.getAdjList().size());
        assertEquals(node2, node1.getAdjList().getFirst());
        assertEquals(100, node1.getDistance().getFirst());
    }
}