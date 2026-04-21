package org.example.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in a graph using adjacency list.
 * @param <T> - data stored in the node.
 */
public class GraphNodeAL<T> {
    private T data;
    //List of connected nodes
    private List<GraphNodeAL<T>> adjList = new ArrayList<GraphNodeAL<T>>();
    //List of edge weights to the corresponding node.
    private List<Integer> distance = new ArrayList<>();
    //Flag to mark node when traversing the graph.
    private boolean visited = false;

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
    public void setData(T data) {
        this.data = data;
    }
    public T getData() {
        return data;
    }
    public List<GraphNodeAL<T>> getAdjList() {
        return adjList;
    }
    public List<Integer> getDistance() {
        return distance;
    }
    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public void setDistance(List<Integer> distance) {
        this.distance = distance;
    }
    public void setAdjList(List<GraphNodeAL<T>> adjList) {
        this.adjList = adjList;
    }



}
