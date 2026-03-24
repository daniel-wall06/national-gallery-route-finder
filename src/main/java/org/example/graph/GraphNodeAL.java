package org.example.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in a graph using adjacency list.
 * @param <T> - data stored in the node.
 */
public class GraphNodeAL<T> {
    public T data;
    //List of connected nodes
    public List<GraphNodeAL<T>> adjList = new ArrayList<GraphNodeAL<T>>();
    //List of edge weights to the corresponding node.
    public List<Integer> distance = new ArrayList<>();
    //Flag to mark node when traversing the graph.
    public boolean visited = false;

    public GraphNodeAL(T data) {
        this.data = data;
    }

    /**
     * Create a directed graph from current node to specified node.
     * @param destNode - The node to connect to.
     * @param distance - Distance weight of the edge.
     */
    public void connectToNodeDirected(GraphNodeAL<T> destNode, int distance){
        adjList.add(destNode);
        this.distance.add(distance);
    }

    /**
     * Create an undirected graph between current node and specified node.
     * @param destNode - Node to connect to.
     * @param distance - Distance weight of the edge.
     */
    public void connectToNodeUndirected(GraphNodeAL<T> destNode, int distance){
        adjList.add(destNode);
        this.distance.add(distance);
        destNode.adjList.add(this);
        destNode.distance.add(distance);
    }



}
