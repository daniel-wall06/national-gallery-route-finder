package benchmarks;

import org.example.graph.GraphAL;
import org.example.models.Room;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class GraphBenchmark {

    private GraphAL graph;

    @Setup
    public void setup() {
        graph = new GraphAL();
        graph.addRoom(new Room(1, "Room 1"));
        graph.addRoom(new Room(2, "Room 2"));
        graph.addRoom(new Room(3, "Room 3"));
        graph.addRoom(new Room(4, "Room 4"));
        graph.connectRooms(1, 2, 100);
        graph.connectRooms(2, 3, 100);
        graph.connectRooms(3, 4, 100);
        graph.connectRooms(1, 4, 50);

    }

    @Benchmark
    public void benchmarkDijkstra() {
        graph.findShortestRouteDijkstra(1, 4, new ArrayList<>());
    }

    @Benchmark
    public void benchmarkDFS() {
        graph.findRoutesDFS(1, 4, 5, new ArrayList<>());
    }
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}