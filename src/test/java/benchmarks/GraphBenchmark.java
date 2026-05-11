package benchmarks;

import org.example.data.GalleryDataLoader;
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
        try {
            graph = new GalleryDataLoader().load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void benchmarkDijkstra() {
        graph.findShortestRouteDijkstra(1, 46, new ArrayList<>());
    }

    @Benchmark
    public void benchmarkDFS() {
        graph.findRoutesDFS(1, 46, 5, new ArrayList<>());
    }
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}