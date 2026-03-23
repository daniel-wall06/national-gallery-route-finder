package org.example.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> {
    public T data;
    public List<GraphNodeAL<T>> adjList = new ArrayList<GraphNodeAL<T>>();
    public List<Integer> distance = new ArrayList<>();
    public boolean visited = false;

    public GraphNodeAL(T data) {
        this.data = data;
    }

    public void connectToNodeDirected(GraphNodeAL<T> destNode, int distance){
        adjList.add(destNode);
        this.distance.add(distance);
    }
    public void connectToNodeUndirected(GraphNodeAL<T> destNode, int distance){
        adjList.add(destNode);
        this.distance.add(distance);
        destNode.adjList.add(this);
        destNode.distance.add(distance);
    }



}
